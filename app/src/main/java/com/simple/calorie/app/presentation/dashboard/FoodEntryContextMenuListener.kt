package com.simple.calorie.app.presentation.dashboard

import com.simple.calorie.app.data.room.entity.FoodEntry

interface FoodEntryContextMenuListener {
    fun deleteItem(foodEntry: FoodEntry)
    fun editItem(foodEntry: FoodEntry)
}