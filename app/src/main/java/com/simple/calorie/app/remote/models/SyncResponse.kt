package com.simple.calorie.app.remote.models

import com.google.gson.annotations.SerializedName
import com.simple.calorie.app.data.room.entity.FoodEntry

data class SyncResponse(
    @SerializedName("modified") val modifiedFoodEntryResponse: List<FoodEntry>,
    @SerializedName("deleted") val deletedFoodEntryId: List<Long>,
    @SerializedName("last_time") val lastSyncTime: Long
)