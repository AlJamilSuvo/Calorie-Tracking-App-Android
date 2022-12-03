package com.simple.calorie.app.remote

import com.simple.calorie.app.data.preference.SharedPreferenceDataStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(private val store: SharedPreferenceDataStore) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return if (request.url.toString().endsWith("/register")) {
            chain.proceed(request)
        } else {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer " + store.getAuthToken())
                .build()
            chain.proceed(newRequest)
        }

    }
}