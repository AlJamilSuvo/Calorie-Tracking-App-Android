package com.simple.calorie.app.remote.models

import com.google.gson.annotations.SerializedName

data class FoodEntryCreationRequest(
    @SerializedName("food_name") val foodName: String,
    @SerializedName("calorie") val calorie: Float,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("user_id") val userId: String? = null
)