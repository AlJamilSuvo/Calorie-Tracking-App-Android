package com.simple.calorie.app.presentation.dashboard

import com.simple.calorie.app.data.room.entity.FoodEntry


data class FoodEntryItemModel(
    val foodEntry: FoodEntry,
    val timestampStr: String,
    val dateStr: String,
    var isHeader: Boolean,
    var totalCalorie: Float = 0.0f,
    var totalCalorieStatus: String = ""
)