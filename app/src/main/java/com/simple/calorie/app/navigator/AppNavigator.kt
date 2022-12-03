package com.simple.calorie.app.navigator

interface AppNavigator {
    fun navigateTo(screens: Screens, entryId: Long = 0)
}