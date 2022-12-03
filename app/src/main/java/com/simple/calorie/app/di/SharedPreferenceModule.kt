package com.simple.calorie.app.di

import android.content.Context
import android.content.SharedPreferences
import com.simple.calorie.app.data.preference.SharedPreferenceDataStore
import com.simple.calorie.app.data.preference.SharedPreferenceDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SharedPreferenceModule {

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            "shared_preference",
            Context.MODE_PRIVATE
        )
    }
}

@InstallIn(SingletonComponent::class)
@Module
abstract class SharedPreferenceDataStoreModule {

    @Binds
    @Singleton
    abstract fun provideSharedPreferenceDataStore(
        sharedPreferenceDataStoreImpl: SharedPreferenceDataStoreImpl
    ): SharedPreferenceDataStore

}