package com.pomodoro.timer.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.pomodoro.timer.R
import com.pomodoro.timer.ui.theme.CustomTheme
import com.pomodoro.timer.ui.theme.MyTheme
import com.pomodoro.timer.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
){
    val pages = listOf(
        Pair(R.raw.timer_animation, R.string.onboarding_title_1),
        Pair(R.raw.double_tap_animation, R.string.onboarding_title_2),
        Pair(R.raw.edit_animation, R.string.onboarding_title_3),
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 3 }
    )
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize().background(
            color = White
        ).padding(20.dp),
    ){
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState,
        ) { page ->
            val (animation, text) = pages[page]
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animation))
            val isCurrentPage = pagerState.currentPage == page
            val progress by animateLottieCompositionAsState(
                composition = composition,
                isPlaying = isCurrentPage,
                restartOnPlay = true,
                iterations = if(isCurrentPage) LottieConstants.IterateForever else 1
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(text),
                    style = CustomTheme.typography.onboardingTitle,
                    modifier = Modifier.align(Alignment.TopStart).padding(top = 80.dp, start = 20.dp)
                )
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                )
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp),
            onClick = {
                if(pagerState.currentPage == pages.lastIndex){
                    onFinish()
                }
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomTheme.colors.primary,
                contentColor = White,
            ),
        ) {
            Text(
                text = if(pagerState.currentPage == pages.lastIndex) stringResource(R.string.lets_start) else stringResource(R.string.skip),
                style = CustomTheme.typography.onboardingButtonText
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnboardingScreenPreview(){
    MyTheme {
        OnboardingScreen(
            onFinish = {}
        )
    }
}