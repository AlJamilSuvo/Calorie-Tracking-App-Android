package com.simple.calorie.app.data.preference

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferenceDataStoreImpl @Inject constructor(private val sharedPreferences: SharedPreferences) :
    SharedPreferenceDataStore {

    companion object {
        const val KEY_TOKEN = "key_token"
        const val KEY_USER_ID = "key_user_id"
        const val KEY_DAILY_LIMIT = "key_daily_limit"
        const val KEY_USER_ADMIN = "key_user_admin"
        const val KEY_LAST_SYNC_TIME = "key_last_sync_time"
    }

    override fun saveAuthToken(token: String) {
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply()
    }

    override fun getAuthToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    override fun saveUserId(userId: String) {
        sharedPreferences.edit().putString(KEY_USER_ID, userId).apply()
    }

    override fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    override fun getDailyCalorieLimit(): Float {
        return sharedPreferences.getFloat(KEY_DAILY_LIMIT, 2.1f)
    }

    override fun saveDailyCalorieLimit(limit: Float) {
        sharedPreferences.edit().putFloat(KEY_DAILY_LIMIT, limit).apply()
    }

    override fun isUserAdmin(): Boolean {
        return sharedPreferences.getBoolean(KEY_USER_ADMIN, false)
    }

    override fun saveIsUserAdmin(isUserAdmin: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_USER_ADMIN, isUserAdmin).apply()
    }

    override fun getLastSyncTime(): Long {
        return sharedPreferences.getLong(KEY_LAST_SYNC_TIME, 0)
    }

    override fun saveLastSyncTime(lastSyncTime: Long) {
        sharedPreferences.edit().putLong(KEY_LAST_SYNC_TIME, lastSyncTime).apply()
    }


}