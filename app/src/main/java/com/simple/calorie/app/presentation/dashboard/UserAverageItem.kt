package com.simple.calorie.app.presentation.dashboard

import androidx.room.ColumnInfo

data class UserAverageItem(
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "average_calorie") val averageCalorie: Float
)