package com.pomodoro.timer.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import coil.compose.AsyncImage
import com.pomodoro.timer.CustomWidget
import com.pomodoro.timer.R
import com.pomodoro.timer.presentation.pomodoro.PomodoroTimer
import com.pomodoro.timer.ui.theme.CustomTheme
import com.pomodoro.timer.ui.theme.MyTheme
import kotlinx.coroutines.delay

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    var editMode by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val widgets by viewModel.widgets.collectAsStateWithLifecycle()
    val mode = viewModel.mode
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val isTablet = with(adaptiveInfo) {
        windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        //이미지 배경 / 기본 배경
        // 타이머 중간
        // 타이머 시작, 중지, 초기화
        // 편집 모드 - 취소, 확인, 바텀시트
        // 반응형 UI
        if(uiState != MainUiState.Loading) {
            if(isTablet) {
//                MainScreenContentTablet(
//                    widgets = widgets,
//                    editMode = editMode,
//                    onEditModeChange = { editMode = it },
//                )
            } else {
                MainScreenContentPhone(
                    widgets = widgets,
                    editMode = editMode,
                    onEditModeChange = { editMode = it },
                    mode = mode
                )
            }
        }
    }
}

@Composable
fun MainScreenContentPhone(
    widgets: List<CustomWidget>,
    editMode: Boolean,
    onEditModeChange: (Boolean) -> Unit,
    mode: Int,
) {
    Log.d("widgets", widgets.toString())
    val pagerState = rememberPagerState(pageCount = {
        widgets.filter {
            it.mode == mode
        }.size
    })
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (editMode){
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                OutlinedButton(
                    onClick = { onEditModeChange(false) },
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(50))
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
                OutlinedButton(
                    onClick = { onEditModeChange(false) },
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(50))
                ) {
                    Text(text = stringResource(id = R.string.done))
                }
            }
        }
        HorizontalPager(
            modifier = Modifier.padding(
                top = 80.dp
            ).combinedClickable(
                onLongClick = { onEditModeChange(true) },
                onClick = { }
            ),
            state = pagerState
        ) { page ->
            when(mode){
                0 -> {
                    PomodoroTimer(
                        modifier = Modifier.fillMaxSize(),
                        widget = widgets[page]
                    )
                }
                1 -> {}
                2 -> {}
            }
        }
        if (editMode) {
            Row(
                modifier = Modifier.wrapContentWidth().padding(bottom = 40.dp).align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(widgets.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) CustomTheme.colors.iconRed
                                else CustomTheme.colors.iconRed
                            )
                    )
                }
            }
        }
    }
}


@Composable
fun AutoSlideImagePager(
    images: List<String>,
    modifier: Modifier
) {
    val pagerState = rememberPagerState(
        pageCount = { images.size }
    )

    // 자동 슬라이드
    LaunchedEffect(key1 = pagerState.currentPage) {
        delay(30_000) // 30초
        val nextPage = (pagerState.currentPage + 1) % images.size
        pagerState.animateScrollToPage(nextPage)
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->
        Box(modifier = modifier.clip(shape = CircleShape)) {
            AsyncImage(
                model = images[page],
                contentDescription = "Slide Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
fun MainScreenContentTabletPreview(){
    MyTheme {
        MainScreenContentPhone(
            widgets = listOf(
                CustomWidget(),
                CustomWidget()
            ),
            editMode = true,
            onEditModeChange = {},
            mode = 0
        )
    }
}