package com.simple.calorie.app.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "food_entity")
data class FoodEntry(
    @PrimaryKey
    @SerializedName("entry_id") @ColumnInfo(name = "entry_id") val entryId: Long,
    @SerializedName("user_id") @ColumnInfo(name = "user_id") val userId: String,
    @SerializedName("food_name") @ColumnInfo(name = "food_name") val foodName: String,
    @SerializedName("calorie") @ColumnInfo(name = "calorie") val calorie: Float,
    @SerializedName("timestamp") @ColumnInfo(name = "timestamp") val timestamp: Long
)