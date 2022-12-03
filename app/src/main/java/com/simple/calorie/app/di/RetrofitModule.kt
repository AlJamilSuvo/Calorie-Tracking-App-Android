package com.simple.calorie.app.di

import com.simple.calorie.app.config.AppConfig
import com.simple.calorie.app.data.preference.SharedPreferenceDataStore
import com.simple.calorie.app.remote.AuthenticationInterceptor
import com.simple.calorie.app.remote.apis.NetworkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {


    @Provides
    @Singleton
    fun providesAuthenticationInterceptor(sharedPreferenceDataStore: SharedPreferenceDataStore): AuthenticationInterceptor {
        return AuthenticationInterceptor(sharedPreferenceDataStore)
    }

    @Provides
    @Singleton
    fun providesRetrofitClient(authenticationInterceptor: AuthenticationInterceptor): Retrofit {
        return Retrofit.Builder().baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(authenticationInterceptor)
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    })
                    .build()
            ).build()

    }

    @Provides
    @Singleton
    fun providesUserApi(retrofit: Retrofit): NetworkApi {
        return retrofit.create(NetworkApi::class.java)
    }
}