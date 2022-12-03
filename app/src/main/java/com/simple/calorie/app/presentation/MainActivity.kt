package com.simple.calorie.app.presentation


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simple.calorie.app.R
import com.simple.calorie.app.navigator.AppNavigator
import com.simple.calorie.app.navigator.Screens
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            navigator.navigateTo(Screens.SPLASH)
        }
    }


}