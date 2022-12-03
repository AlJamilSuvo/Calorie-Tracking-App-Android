package com.simple.calorie.app.data.preference

interface SharedPreferenceDataStore {
    fun saveAuthToken(token: String)
    fun getAuthToken(): String?
    fun saveUserId(userId: String)
    fun getUserId(): String?
    fun getDailyCalorieLimit(): Float
    fun saveDailyCalorieLimit(limit: Float)
    fun isUserAdmin(): Boolean
    fun saveIsUserAdmin(isUserAdmin: Boolean)
    fun getLastSyncTime(): Long
    fun saveLastSyncTime(lastSyncTime: Long)
}