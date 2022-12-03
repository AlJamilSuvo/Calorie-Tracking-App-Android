package com.simple.calorie.app.data.room


import androidx.lifecycle.LiveData
import androidx.room.Query
import com.simple.calorie.app.data.room.dao.FoodEntryDao
import com.simple.calorie.app.data.room.entity.FoodEntry
import com.simple.calorie.app.presentation.dashboard.UserAverageItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FoodEntryLocalDataSource @Inject constructor(private val foodEntryDao: FoodEntryDao) {

    suspend fun insert(foodEntry: FoodEntry) {
        foodEntryDao.insert(foodEntry)
    }

    suspend fun insert(foodEntryList: List<FoodEntry>) {
        foodEntryDao.insert(foodEntryList)
    }

    suspend fun delete(entryId: Long) {
        foodEntryDao.delete(entryId)
    }

    suspend fun delete(entryIdList: List<Long>) {
        foodEntryDao.delete(entryIdList)
    }

    suspend fun getFoodEntryById(entryId: Long): FoodEntry? {
        return foodEntryDao.getFoodEntryById(entryId)
    }

    fun getFoodEntry(startTime: Long, endTime: Long): Flow<List<FoodEntry>> =
        foodEntryDao.getFoodEntry(startTime, endTime)

    fun getAllFoodEntry(): Flow<List<FoodEntry>> =
        foodEntryDao.getAllFoodEntry()

    fun getEntryCount(startTime: Long, endTime: Long) =
        foodEntryDao.getEntryCount(startTime, endTime)

    fun getUserAverageLiveData(startTime: Long, endTime: Long) =
        foodEntryDao.getUserAverageLiveData(
            startTime, endTime
        )
}