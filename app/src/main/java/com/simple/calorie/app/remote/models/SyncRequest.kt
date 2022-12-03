package com.simple.calorie.app.remote.models

import com.google.gson.annotations.SerializedName


data class SyncRequest(
    @SerializedName("last_time") val lastTime: Long
)