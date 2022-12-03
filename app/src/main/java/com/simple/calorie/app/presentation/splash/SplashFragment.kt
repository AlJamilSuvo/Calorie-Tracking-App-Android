package com.simple.calorie.app.presentation.splash

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.simple.calorie.app.R
import com.simple.calorie.app.data.preference.SharedPreferenceDataStore
import com.simple.calorie.app.navigator.AppNavigator
import com.simple.calorie.app.navigator.Screens
import com.simple.calorie.app.presentation.register.RegisterFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }


    @Inject
    lateinit var navigator: AppNavigator

    @Inject
    lateinit var store: SharedPreferenceDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onStart() {
        super.onStart()
        val userId = store.getUserId()
        if (userId == null) {
            navigator.navigateTo(Screens.REGISTER)
        } else {
            navigator.navigateTo(Screens.DASHBOARD)
        }

    }

}