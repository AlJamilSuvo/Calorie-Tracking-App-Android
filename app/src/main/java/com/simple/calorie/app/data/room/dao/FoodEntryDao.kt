package com.simple.calorie.app.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.simple.calorie.app.data.room.entity.FoodEntry
import com.simple.calorie.app.presentation.dashboard.UserAverageItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodEntryDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(foodEntry: FoodEntry)

    @Insert(onConflict = REPLACE)
    suspend fun insert(foodEntryList: List<FoodEntry>)

    @Query("DELETE FROM FOOD_ENTITY WHERE entry_id = :entryId")
    suspend fun delete(entryId: Long)

    @Query("DELETE FROM FOOD_ENTITY WHERE entry_id in (:entryIdList)")
    suspend fun delete(entryIdList: List<Long>)

    @Query("SELECT * FROM FOOD_ENTITY WHERE timestamp >= :startTime and timestamp<=:endTime")
    fun getFoodEntry(startTime: Long, endTime: Long): Flow<List<FoodEntry>>

    @Query("SELECT * FROM FOOD_ENTITY WHERE entry_id=:entryId ORDER BY timestamp DESC")
    fun getFoodEntryById(entryId: Long): FoodEntry?

    @Query("SELECT * FROM FOOD_ENTITY ORDER BY timestamp DESC")
    fun getAllFoodEntry(): Flow<List<FoodEntry>>


    @Query("SELECT COUNT(*) FROM FOOD_ENTITY WHERE timestamp >= :startTime and timestamp<=:endTime")
    fun getEntryCount(startTime: Long, endTime: Long): Flow<Int>


    @Query("SELECT SUM(calorie)/7 AS average_calorie, user_id FROM FOOD_ENTITY WHERE timestamp >= :startTime and timestamp<=:endTime GROUP BY user_id")
    fun getUserAverageLiveData(startTime: Long, endTime: Long): Flow<List<UserAverageItem>>
}