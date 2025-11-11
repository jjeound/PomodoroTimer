package com.pomodoro.timer.presentation.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.pomodoro.timer.R

@Composable
fun OnboardingScreen(){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.timer_animation))
    val progress by animateLottieCompositionAsState(composition)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        LottieAnimation(
            composition = composition,
            progress = { progress },
        )
    }
}

