package com.simple.calorie.app.remote.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("user_id") val userId: String
)