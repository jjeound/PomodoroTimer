package com.pomodoro.timer.presentation

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.pomodoro.timer.CustomWidget
import com.pomodoro.timer.R
import com.pomodoro.timer.presentation.common.ContainerEditBottomSheet
import com.pomodoro.timer.presentation.pomodoro.PomodoroTextEditBottomSheet
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
    val currentWidget by viewModel.currentWidget.collectAsStateWithLifecycle()
    val mode = viewModel.mode
    val config = LocalConfiguration.current
    val isTablet = config.smallestScreenWidthDp >= 600
    val editable = isTablet || config.orientation == Configuration.ORIENTATION_PORTRAIT //탭은 다 가능, 폰은 세로모드에서만 가능
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
                    mode = mode,
                    editable = editable,
                    setCurrentWidget = viewModel::setCurrentWidget,
                    currentWidget = currentWidget,
                    editContainerColor = viewModel::editContainerColor,
                    editBgColor = viewModel::editBgColor,
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
    editable: Boolean,
    setCurrentWidget: (CustomWidget) -> Unit = {},
    currentWidget: CustomWidget,
    editContainerColor: (Color) -> Unit = {},
    editBgColor: (Color) -> Unit = {},
) {
    Log.d("widgets", widgets.toString())
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = {
        widgets.filter {
            it.mode == mode
        }.size
    })
    var showTextEditBottomSheet by remember { mutableStateOf(false) }
    var showContainerEditBottomSheet by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (editMode){
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter).zIndex(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                OutlinedButton(
                    onClick = { onEditModeChange(false) },
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(50)),
                    border = BorderStroke(1.dp, CustomTheme.colors.buttonBorder)
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        style = CustomTheme.typography.buttonEditSmall,
                        color = CustomTheme.colors.buttonText
                    )
                }
                OutlinedButton(
                    onClick = { onEditModeChange(false) },
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(50)),
                    border = BorderStroke(1.dp, CustomTheme.colors.buttonBorder)
                ) {
                    Text(
                        text = stringResource(id = R.string.done),
                        style = CustomTheme.typography.buttonEditSmall,
                        color = CustomTheme.colors.buttonText
                    )
                }
            }
        }
        HorizontalPager(
            modifier = Modifier.combinedClickable(
                enabled = !editMode,
                onLongClick = {
                    if(editable){
                        onEditModeChange(true)
                    } else {
                        Toast.makeText(context, "가로 모드에서는 편집할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                },
                onClick = {}
            ).align(Alignment.Center),
            state = pagerState
        ) { page ->
            LaunchedEffect(page) {
                setCurrentWidget(widgets[page])
            }
            when(mode){
                0 -> {
                    PomodoroTimer(
                        modifier = Modifier.fillMaxSize(),
                        widget = currentWidget,
                        editMode = editMode,
                        onEditText = {
                            showTextEditBottomSheet = true
                        },
                        onEditContainer = {
                            showContainerEditBottomSheet = true
                        }
                    )
                }
                1 -> {}
                2 -> {}
            }
        }
        if (editMode) {
            Row(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(widgets.size) { index ->
                    val isFocused = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isFocused) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isFocused) CustomTheme.colors.dotIndicatorFocused
                                else CustomTheme.colors.dotIndicatorUnfocused
                            )
                    )
                }
            }
        }
        if(showContainerEditBottomSheet){
            ContainerEditBottomSheet(
                onDismissRequest = {
                    showContainerEditBottomSheet = false
                },
                onColorClick = { color ->
                    editContainerColor(color)
                },
                onBackgroundColorClick = { color ->
                    editBgColor(color)
                },
                onPlusClick = {}
            )
        }
        if(showTextEditBottomSheet){
            when(mode){
                0 -> {
                    PomodoroTextEditBottomSheet(
                        onDismissRequest = {
                            showTextEditBottomSheet = false
                        },
                        onColorClick = {},
                        onFontClick = {},
                        onPlusClick = {},
                        onClickGap = {},
                        onEnterBreakTime = {},
                        onEnterRepeat = {},
                        onClickExpireMode = {},
                        onClickStartSound = {},
                        onClickRestartSound = {}
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
            mode = 0,
            editable = true,
            currentWidget = CustomWidget(),
        )
    }
}