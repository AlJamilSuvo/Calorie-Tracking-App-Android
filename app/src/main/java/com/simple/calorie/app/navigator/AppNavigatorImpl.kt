package com.simple.calorie.app.navigator

import androidx.fragment.app.FragmentActivity
import com.simple.calorie.app.R
import com.simple.calorie.app.data.preference.SharedPreferenceDataStore
import com.simple.calorie.app.presentation.dashboard.admin.AdminDashboardFragment
import com.simple.calorie.app.presentation.dashboard.user.UserDashboardFragment
import com.simple.calorie.app.presentation.input.FoodEntryInputFragment
import com.simple.calorie.app.presentation.register.RegisterFragment
import com.simple.calorie.app.presentation.splash.SplashFragment
import javax.inject.Inject

class AppNavigatorImpl @Inject constructor(
    private val activity: FragmentActivity,
    private val store: SharedPreferenceDataStore
) : AppNavigator {
    override fun navigateTo(screens: Screens, entryId: Long) {
        val fragment = when (screens) {
            Screens.SPLASH -> SplashFragment.newInstance()
            Screens.REGISTER -> RegisterFragment.newInstance()
            Screens.ADD_FOOD_ENTRY -> FoodEntryInputFragment.newInstance()
            Screens.DASHBOARD -> {
                if (store.isUserAdmin()) AdminDashboardFragment.newInstance()
                else UserDashboardFragment.newInstance()
            }
            Screens.UPDATE_FOOD_ENTRY -> FoodEntryInputFragment.newInstanceForUpdate(entryId)

        }
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.root_container, fragment)
            .commit()
    }
}