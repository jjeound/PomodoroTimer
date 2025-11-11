package com.pomodoro.timer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pomodoro.timer.presentation.MainScreen
import com.pomodoro.timer.presentation.onboarding.OnboardingScreen
import com.pomodoro.timer.presentation.onboarding.OnboardingViewModel
import com.pomodoro.timer.ui.theme.MyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTheme {
                val viewModel: OnboardingViewModel = hiltViewModel()
                val isFirstEnter by viewModel.isFirstEnter.collectAsStateWithLifecycle()
                if(isFirstEnter != null){
                    if(isFirstEnter!!){
                        OnboardingScreen()
                    } else {
                        MainScreen()
                    }
                }
            }
        }
    }
}