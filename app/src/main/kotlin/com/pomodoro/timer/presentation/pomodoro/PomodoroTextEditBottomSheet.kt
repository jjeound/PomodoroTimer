package com.pomodoro.timer.presentation.pomodoro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pomodoro.timer.presentation.common.ColorFontEditBox
import com.pomodoro.timer.presentation.common.ExpireMode
import com.pomodoro.timer.presentation.common.SoundEditBox
import com.pomodoro.timer.presentation.common.TimerEditBox
import com.pomodoro.timer.ui.theme.CustomTheme
import com.pomodoro.timer.ui.theme.MyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroTextEditBottomSheet(
    onDismissRequest: () -> Unit,
    onColorClick: (Color) -> Unit,
    onFontClick: (TextStyle) -> Unit,
    gap: Int,
    onClickGap: (Int) -> Unit,
    breakTime: Int,
    onChangeBreakTime: (Int) -> Unit,
    repeat: Int,
    onChangeRepeat: (Int) -> Unit,
    expireMode: ExpireMode,
    onClickExpireMode: (ExpireMode) -> Unit,
    startSound: String,
    onClickStartSound: (String) -> Unit,
    restartSound: String,
    onClickRestartSound: (String) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val pagerState = rememberPagerState(pageCount = {3})
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
                            onColorClick = onColorClick,
                            onFontClick = onFontClick,
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
                            expireMode = expireMode,
                            onClickExpireMode = onClickExpireMode,
                            startSound = startSound,
                            restartSound = restartSound,
                            onChangeStartSound = onClickStartSound,
                            onChangeRestartSound = onClickRestartSound,
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp),
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

@Preview
@Composable
fun PomodoroTextEditBottomSheetPreview() {
    MyTheme {
        PomodoroTextEditBottomSheet(
            onDismissRequest = {},
            onColorClick = {},
            onFontClick = {},
            gap = 5,
            onClickGap = {},
            breakTime = 5,
            onChangeBreakTime = {},
            repeat = 0,
            onChangeRepeat = {},
            expireMode = ExpireMode.SOUND,
            onClickExpireMode = {},
            startSound = "Yuck",
            onClickStartSound = {},
            restartSound = "Bell",
            onClickRestartSound = {},
        )
    }
}