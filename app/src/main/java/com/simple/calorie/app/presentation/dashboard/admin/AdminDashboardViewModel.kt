package com.simple.calorie.app.presentation.dashboard.admin


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.simple.calorie.app.data.preference.SharedPreferenceDataStore
import com.simple.calorie.app.data.room.FoodEntryLocalDataSource
import com.simple.calorie.app.data.room.entity.FoodEntry
import com.simple.calorie.app.presentation.dashboard.FoodEntryItemModel
import com.simple.calorie.app.presentation.dashboard.UserAverageItem
import com.simple.calorie.app.repositories.RemoteRepository
import com.simple.calorie.app.repositories.ResponseResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
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

    val entryCntLastSevenDay: MutableLiveData<String> = MutableLiveData()
    val entryCntLastPrevSevenDay: MutableLiveData<String> = MutableLiveData()

    val userAverageItemList: MutableLiveData<List<UserAverageItem>> = MutableLiveData()

    private val startTimeOfLastSeverDay: Long
    private val endTimeOfLastSeverDay: Long


    companion object {
        const val TAG = "UserDashboardViewModel"
    }

    init {

        setEndTimeToDayEnd()
        startCalender.add(Calendar.DATE, -7)
        setStartTimeToDayStart()
        startTimeOfLastSeverDay = startCalender.timeInMillis
        endTimeOfLastSeverDay = endCalender.timeInMillis
        Log.d("UserViewModel", "${startCalender.time}")
        observerFoodEntryDb()
        observerLastSevenDaysData()
    }

    fun observerLastSevenDaysData() {

        val simpleDateFormat = SimpleDateFormat("dd/MM/yy")

        CoroutineScope(Dispatchers.IO).launch {
            foodEntryLocalDataSource.getEntryCount(startTimeOfLastSeverDay, endTimeOfLastSeverDay)
                .collect {
                    viewModelScope.launch {
                        entryCntLastSevenDay.value =
                            simpleDateFormat.format(Date(startTimeOfLastSeverDay)) + " - " + simpleDateFormat.format(
                                endTimeOfLastSeverDay
                            ) + " # " + it.toString()

                    }
                }
        }

        CoroutineScope(Dispatchers.IO).launch {
            foodEntryLocalDataSource.getUserAverageLiveData(
                startTimeOfLastSeverDay,
                endTimeOfLastSeverDay
            ).collect {
                viewModelScope.launch {
                    userAverageItemList.value = it
                }
            }
        }

        observerPrevLastSevenDaysData()

    }

    private fun observerPrevLastSevenDaysData() {
        val sevenDayDifference = 7 * 24 * 3600 * 100
        val simpleDateFormat = SimpleDateFormat("dd/MM/yy")
        val start = startTimeOfLastSeverDay - sevenDayDifference
        val end = endTimeOfLastSeverDay - sevenDayDifference

        CoroutineScope(Dispatchers.IO).launch {


            foodEntryLocalDataSource.getEntryCount(
                start,
                end
            ).collect {
                viewModelScope.launch {
                    entryCntLastPrevSevenDay.value =
                        simpleDateFormat.format(start) + " - " +
                                simpleDateFormat.format(
                                    end
                                ) + " # " + it.toString()
                }
            }
        }
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
                val format1 = SimpleDateFormat("HH:mm MM/dd/yyyy")
                val format2 = SimpleDateFormat("MM/dd/yyyy")
                val seenDateMap = HashMap<String, Boolean>()

                val list: List<FoodEntryItemModel> = it.map { entry ->
                    val dateTimeStr = format1.format(entry.timestamp)
                    val dateStr = format2.format(entry.timestamp)
                    var isHeader = false

                    if (!seenDateMap.containsKey(dateStr)) {
                        isHeader = true
                    }
                    seenDateMap[dateStr] = true
                    val model = FoodEntryItemModel(
                        foodEntry = entry,
                        timestampStr = dateTimeStr,
                        dateStr = dateStr,
                        isHeader = isHeader
                    )

                    model
                }

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


}
