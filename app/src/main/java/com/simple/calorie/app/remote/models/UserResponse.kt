package com.simple.calorie.app.remote.models

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("user_id") val userId: String,
    @SerializedName("token") val token: String,
    @SerializedName("is_admin") val isAdmin: Boolean
)