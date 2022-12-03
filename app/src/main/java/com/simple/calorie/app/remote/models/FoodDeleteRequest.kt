package com.simple.calorie.app.remote.models

import com.google.gson.annotations.SerializedName

data class FoodDeleteRequest(@SerializedName("entry_id") val entryId: Long)