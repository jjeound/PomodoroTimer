package com.pomodoro.timer.presentation.pomodoro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pomodoro.timer.R
import com.pomodoro.timer.data.model.SoundMode
import com.pomodoro.timer.presentation.components.ColorFontEditBox
import com.pomodoro.timer.presentation.components.SoundEditBox
import com.pomodoro.timer.presentation.components.TimerEditBox
import com.pomodoro.timer.ui.theme.CustomTheme
import com.pomodoro.timer.ui.theme.MyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroTextEditSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onColorClick: (Color) -> Unit,
    onColorPickerClick: (index: Int) -> Unit,
    onAddBtnClick: (index: Int) -> Unit,
    onFontClick: (TextStyle) -> Unit,
    gap: Int,
    onClickGap: (Int) -> Unit,
    breakTime: Int,
    onChangeBreakTime: (Int) -> Unit,
    repeat: Int,
    onChangeRepeat: (Int) -> Unit,
    soundMode: SoundMode,
    onClickSoundMode: (SoundMode) -> Unit,
    startSound: Int,
    onClickStartSound: (Int) -> Unit,
    restartSound: Int,
    onChangeBreakTimeSound: (Int) -> Unit,
    colors: List<Color> = emptyList(),
    currentColor: Color,
    isLandscape: Boolean,
    onDeleteColor: (Color) -> Unit,
    fontSize: Float,
    onFontSizeChange: (Float) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val pagerState = rememberPagerState(pageCount = {3})
    if(isLandscape) {
        Box(
            modifier = modifier.fillMaxHeight().padding(30.dp).background(
                color = CustomTheme.colors.surface
            )
        ){
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = onDismissRequest
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.close),
                    contentDescription = null,
                )
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier.wrapContentHeight().fillMaxWidth(),
                ) {
                    HorizontalPager(
                        state = pagerState
                    ){ page ->
                        when(page){
                            0 -> {
                                ColorFontEditBox(
                                    onColorClick = {
                                        onColorClick(it)
                                    },
                                    onColorPickerClick = onColorPickerClick,
                                    onAddBtnClick = onAddBtnClick,
                                    onFontClick = onFontClick,
                                    currentColor = currentColor,
                                    colors = colors,
                                    onDeleteColor = onDeleteColor,
                                    fontSize = fontSize,
                                    onFontSizeChange = onFontSizeChange
                                )
                            }
                            1 -> {
                                TimerEditBox(
                                    gap = gap,
                                    onClickGap = onClickGap,
                                    breakTime = breakTime,
                                    onChangeBreakTime = onChangeBreakTime,
                                    repeat = repeat,
                                    onChangeRepeat = onChangeRepeat,
                                    hour = 0,
                                    minute = 0,
                                    second = 0,
                                    onChangeHour = {},
                                    onChangeMinute = {},
                                    onChangeSecond = {},
                                    mode = 0
                                )
                            }
                            2 -> {
                                SoundEditBox(
                                    soundMode = soundMode,
                                    onClickSoundMode = onClickSoundMode,
                                    startSound = startSound,
                                    restartSound = restartSound,
                                    onChangeStartSound = onClickStartSound,
                                    onChangeBreakTimeSound = onChangeBreakTimeSound,
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 15.dp, bottom = 30.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(3) { index ->
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
            }
        }
    } else {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = CustomTheme.colors.surface,
            scrimColor = Color.Transparent,
        ){
            Column(
                modifier = Modifier.wrapContentHeight().fillMaxWidth(),
            ) {
                HorizontalPager(
                    state = pagerState
                ){ page ->
                    when(page){
                        0 -> {
                            ColorFontEditBox(
                                onColorClick = {
                                    onColorClick(it)
                                },
                                onColorPickerClick = onColorPickerClick,
                                onAddBtnClick = onAddBtnClick,
                                onFontClick = onFontClick,
                                currentColor = currentColor,
                                colors = colors,
                                onDeleteColor = onDeleteColor,
                                fontSize = fontSize,
                                onFontSizeChange = onFontSizeChange
                            )
                        }
                        1 -> {
                            TimerEditBox(
                                gap = gap,
                                onClickGap = onClickGap,
                                breakTime = breakTime,
                                onChangeBreakTime = onChangeBreakTime,
                                repeat = repeat,
                                onChangeRepeat = onChangeRepeat,
                                hour = 0,
                                minute = 0,
                                second = 0,
                                onChangeHour = {},
                                onChangeMinute = {},
                                onChangeSecond = {},
                                mode = 0
                            )
                        }
                        2 -> {
                            SoundEditBox(
                                soundMode = soundMode,
                                onClickSoundMode = onClickSoundMode,
                                startSound = startSound,
                                restartSound = restartSound,
                                onChangeStartSound = onClickStartSound,
                                onChangeBreakTimeSound = onChangeBreakTimeSound,
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 15.dp, bottom = 30.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) { index ->
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
        }
    }
}

@Preview
@Composable
fun PomodoroTextEditSheetPreview() {
    MyTheme {
        PomodoroTextEditSheet(
            onDismissRequest = {},
            onColorClick = {},
            onColorPickerClick = {},
            onAddBtnClick = {},
            onFontClick = {},
            gap = 5,
            onClickGap = {},
            breakTime = 5,
            onChangeBreakTime = {},
            repeat = 0,
            onChangeRepeat = {},
            soundMode = SoundMode.SOUND,
            onClickSoundMode = {},
            startSound = 0,
            onClickStartSound = {},
            restartSound = 0,
            onChangeBreakTimeSound = {},
            colors = emptyList(),
            currentColor = Color.White,
            isLandscape = true,
            onDeleteColor = {},
            fontSize = 16f,
            onFontSizeChange = {}
        )
    }
}