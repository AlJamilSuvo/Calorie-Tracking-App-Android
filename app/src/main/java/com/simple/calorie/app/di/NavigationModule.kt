package com.simple.calorie.app.di

import com.simple.calorie.app.navigator.AppNavigator
import com.simple.calorie.app.navigator.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
abstract class NavigationModule {


    @Binds
    abstract fun bindNavigator(appNavigatorImpl: AppNavigatorImpl): AppNavigator
}