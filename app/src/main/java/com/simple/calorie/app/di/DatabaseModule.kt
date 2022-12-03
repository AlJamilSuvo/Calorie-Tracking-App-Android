package com.simple.calorie.app.di

import android.content.Context
import androidx.room.Room
import com.simple.calorie.app.data.room.AppDatabase
import com.simple.calorie.app.data.room.FoodEntryLocalDataSource
import com.simple.calorie.app.data.room.dao.FoodEntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {


    @Provides
    @Singleton
    fun provideFoodEntryLocalDataSource(foodEntryDao: FoodEntryDao): FoodEntryLocalDataSource {
        return FoodEntryLocalDataSource(foodEntryDao)
    }


    @Provides
    @Singleton
    fun provideFoodEntryDao(appDatabase: AppDatabase): FoodEntryDao {
        return appDatabase.foodEntryDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

}