package com.simple.calorie.app.presentation.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.calorie.app.data.preference.SharedPreferenceDataStore
import com.simple.calorie.app.data.room.FoodEntryLocalDataSource
import com.simple.calorie.app.data.room.entity.FoodEntry
import com.simple.calorie.app.processor.FoodEntryInputProcessor
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

@HiltViewModel
class FoodEntryInputViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localDataSource: FoodEntryLocalDataSource,
    val store: SharedPreferenceDataStore
) :
    ViewModel() {
    val calendar: Calendar = Calendar.getInstance()

    private val timeTextFlow = MutableLiveData("")

    val showProgressBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val failedMessage: MutableLiveData<String> = MutableLiveData("")
    val isAddedSucceed: MutableLiveData<Boolean> = MutableLiveData(false)

    var isEditMode = false

    var foodEntryId: Long = 0

    private val foodEntryInputProcessor = FoodEntryInputProcessor()

    init {
        updateTimeText()
    }

    suspend fun getFoodEntry(entryId: Long): FoodEntry? {
        val entry = localDataSource.getFoodEntryById(entryId)
        foodEntryId = entryId
        entry?.let {
            calendar.timeInMillis = entry.timestamp
            CoroutineScope(Dispatchers.Main).launch {
                updateTimeText()
            }
        }
        return entry
    }

    fun updateInputDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateTimeText()
    }

    fun updateInputTime(hourOfDay: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        updateTimeText()
    }

    private fun updateTimeText() {
        val format = SimpleDateFormat("HH:mm dd/mm/yyyy", Locale.ENGLISH)
        timeTextFlow.value = format.format(calendar.time)
    }

    fun getInputTimeText(): LiveData<String> = timeTextFlow

    fun addFoodEntry(foodName: String, calorieStr: String, userId: String) {
        failedMessage.value = ""
        if (!foodEntryInputProcessor.isFoodNameValid(foodName)) {
            failedMessage.value = "Food Name not valid"
            return
        }

        if (!foodEntryInputProcessor.isFoodEntryTimeValid(calendar.timeInMillis)) {
            failedMessage.value = "Time can not be in future"
            return
        }

        val calorie = foodEntryInputProcessor.getCalorieAmount(calorieStr)

        if (calorie == null) {
            failedMessage.value = "Invalid calorie amount"
            return
        }

        var keepCollecting = true

        if (isEditMode) {
            val foodEntry = FoodEntry(foodEntryId, "", foodName, calorie, calendar.timeInMillis)
            viewModelScope.launch {
                remoteRepository.update(
                    foodEntry
                ).takeWhile { keepCollecting }.collect { response ->
                    keepCollecting = processResponse(response)
                }
            }

        } else {
            var finalUserId: String? = null
            if (store.isUserAdmin()) finalUserId = userId
            viewModelScope.launch {
                remoteRepository.add(
                    foodName, calorie, calendar.timeInMillis, finalUserId
                ).takeWhile { keepCollecting }.collect { response ->
                    keepCollecting = processResponse(response)
                }
            }
        }
    }

    private fun processResponse(response: ResponseResource<FoodEntry>) : Boolean {
        when (response) {
            is ResponseResource.Failed -> {
                failedMessage.value = response.message
                showProgressBar.value = false
                return false
            }
            is ResponseResource.Pending -> {
                failedMessage.value = ""
                showProgressBar.value = true
                return true
            }
            is ResponseResource.Succeed -> {
                isAddedSucceed.value = true
                return false
            }
        }
    }
}