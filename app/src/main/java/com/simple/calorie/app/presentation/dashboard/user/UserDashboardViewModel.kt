package com.simple.calorie.app.presentation.dashboard.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.calorie.app.data.preference.SharedPreferenceDataStore
import com.simple.calorie.app.data.room.FoodEntryLocalDataSource
import com.simple.calorie.app.data.room.entity.FoodEntry
import com.simple.calorie.app.presentation.dashboard.FoodEntryItemModel
import com.simple.calorie.app.processor.CalorieAmountInputProcessor
import com.simple.calorie.app.repositories.RemoteRepository
import com.simple.calorie.app.repositories.ResponseResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class UserDashboardViewModel @Inject constructor(
    private val foodEntryLocalDataSource: FoodEntryLocalDataSource,
    private val remoteRepository: RemoteRepository,
    val store: SharedPreferenceDataStore
) :
    ViewModel() {
    val foodEntryListLiveData: MutableLiveData<List<FoodEntryItemModel>> = MutableLiveData()

    val startCalender: Calendar = Calendar.getInstance()
    val endCalender: Calendar = Calendar.getInstance()
    private var collectDataFromDb: Boolean = true

    val startTimeString: MutableLiveData<String> = MutableLiveData()
    val endTimeString: MutableLiveData<String> = MutableLiveData()

    private val calorieAmountInputProcessor = CalorieAmountInputProcessor()


    companion object {
        const val TAG = "UserDashboardViewModel"
    }

    init {

        setEndTimeToDayEnd()
        startCalender.add(Calendar.DATE, -7)
        setStartTimeToDayStart()

        Log.d("UserViewModel", "${startCalender.time}")
        observerFoodEntryDb()
    }

    private fun setStartTimeToDayStart() {
        startCalender.set(Calendar.HOUR_OF_DAY, startCalender.getMinimum(Calendar.HOUR_OF_DAY))
        startCalender.set(Calendar.MINUTE, startCalender.getMinimum(Calendar.MINUTE))
        startCalender.set(Calendar.SECOND, startCalender.getMinimum(Calendar.SECOND))
        startCalender.set(Calendar.MILLISECOND, startCalender.getMinimum(Calendar.MILLISECOND))
        val simpleDateFormat = SimpleDateFormat("MM/dd/yyyy")
        startTimeString.value = simpleDateFormat.format(startCalender.time)
    }

    private fun setEndTimeToDayEnd() {
        endCalender.set(Calendar.HOUR_OF_DAY, startCalender.getMaximum(Calendar.HOUR_OF_DAY))
        endCalender.set(Calendar.MINUTE, startCalender.getMaximum(Calendar.MINUTE))
        endCalender.set(Calendar.SECOND, startCalender.getMaximum(Calendar.SECOND))
        endCalender.set(Calendar.MILLISECOND, startCalender.getMaximum(Calendar.MILLISECOND))
        val simpleDateFormat = SimpleDateFormat("MM/dd/yyyy")
        endTimeString.value = simpleDateFormat.format(endCalender.time)
    }

    fun observerFoodEntryDb() {
        collectDataFromDb = true


        CoroutineScope(Dispatchers.IO).launch {
            foodEntryLocalDataSource.getFoodEntry(
                startCalender.timeInMillis,
                endCalender.timeInMillis
            ).takeWhile { collectDataFromDb }.collect {

                var lastFoodEntryModel: FoodEntryItemModel? = null
                var cnt = 0.0f
                val format1 = SimpleDateFormat("HH:mm MM/dd/yyyy")
                val format2 = SimpleDateFormat("MM/dd/yyyy")
                val seenDateMap = HashMap<String, Boolean>()
                val dailyLimit = store.getDailyCalorieLimit()

                val list: List<FoodEntryItemModel> = it.map { entry ->
                    val dateTimeStr = format1.format(entry.timestamp)
                    val dateStr = format2.format(entry.timestamp)
                    var isHeader = false

                    if (!seenDateMap.containsKey(dateStr)) {
                        isHeader = true
                        lastFoodEntryModel?.totalCalorie = cnt
                        if (cnt > dailyLimit) lastFoodEntryModel?.totalCalorieStatus = "Greater"
                        else if (cnt < dailyLimit) lastFoodEntryModel?.totalCalorieStatus = "Less"
                        else lastFoodEntryModel?.totalCalorieStatus = "Equal"
                        cnt = 0.0f
                    }

                    seenDateMap[dateStr] = true

                    cnt += entry.calorie
                    val model = FoodEntryItemModel(
                        foodEntry = entry,
                        timestampStr = dateTimeStr,
                        dateStr = dateStr,
                        isHeader = isHeader
                    )
                    if (isHeader) lastFoodEntryModel = model

                    model
                }

                lastFoodEntryModel?.totalCalorie = cnt
                if (cnt > dailyLimit) lastFoodEntryModel?.totalCalorieStatus = "Greater"
                else if (cnt < dailyLimit) lastFoodEntryModel?.totalCalorieStatus = "Less"
                else lastFoodEntryModel?.totalCalorieStatus = "Equal"
                cnt = 0.0f

                foodEntryListLiveData.postValue(list)
            }
        }
    }

    fun updateStartDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        collectDataFromDb = false
        startCalender.set(Calendar.YEAR, year)
        startCalender.set(Calendar.MONTH, monthOfYear)
        startCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        if (startCalender.timeInMillis > endCalender.timeInMillis) {
            endCalender.timeInMillis = startCalender.timeInMillis
        }
        setStartTimeToDayStart()
        setEndTimeToDayEnd()
        observerFoodEntryDb()
    }


    fun updateEndDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        collectDataFromDb = false
        endCalender.set(Calendar.YEAR, year)
        endCalender.set(Calendar.MONTH, monthOfYear)
        endCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        if (startCalender.timeInMillis > endCalender.timeInMillis) {
            endCalender.timeInMillis = startCalender.timeInMillis
        }
        setStartTimeToDayStart()
        setEndTimeToDayEnd()
        observerFoodEntryDb()
    }

    fun deleteFoodEntry(foodEntry: FoodEntry) {
        var keepCollecting = true
        viewModelScope.launch {
            remoteRepository.delete(
                foodEntry.entryId
            ).takeWhile { keepCollecting }.collect { response ->
                keepCollecting = when (response) {
                    is ResponseResource.Failed -> false
                    is ResponseResource.Pending -> true
                    is ResponseResource.Succeed -> false
                }
            }
        }
    }

    fun requestSync() {
        var keepCollecting = true
        CoroutineScope(Dispatchers.IO).launch {
            remoteRepository.sync().takeWhile { keepCollecting }.collect { response ->
                keepCollecting = when (response) {
                    is ResponseResource.Failed -> false
                    is ResponseResource.Pending -> true
                    is ResponseResource.Succeed -> false
                }
            }
        }
    }

    fun updateDailyLimit(amount: String): Float {
        val processedAmount = calorieAmountInputProcessor.getCalorieAmount(amount)
            ?: return store.getDailyCalorieLimit()
        store.saveDailyCalorieLimit(processedAmount)
        collectDataFromDb = false
        observerFoodEntryDb()
        return processedAmount
    }

}