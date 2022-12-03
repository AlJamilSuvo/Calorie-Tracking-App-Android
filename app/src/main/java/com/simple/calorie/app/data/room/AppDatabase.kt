package com.simple.calorie.app.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.simple.calorie.app.data.room.dao.FoodEntryDao
import com.simple.calorie.app.data.room.entity.FoodEntry

@Database(
    entities = [FoodEntry::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodEntryDao(): FoodEntryDao
}