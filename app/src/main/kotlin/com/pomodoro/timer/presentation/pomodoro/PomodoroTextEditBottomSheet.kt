package com.pomodoro.timer.presentation.pomodoro

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pomodoro.timer.R
import com.pomodoro.timer.presentation.common.ColorBox
import com.pomodoro.timer.presentation.common.ColorFontEditBox
import com.pomodoro.timer.presentation.common.TimerEditBox
import com.pomodoro.timer.ui.theme.CustomTheme
import com.pomodoro.timer.ui.theme.MyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroTextEditBottomSheet(
    onDismissRequest: () -> Unit,
    onColorClick: (Color) -> Unit,
    onFontClick: (Font) -> Unit,
    onPlusClick: () -> Unit,
    onClickGap: (Int) -> Unit,
    onEnterBreakTime: (Int) -> Unit,
    onEnterRepeat: (Int) -> Unit,
    onClickExpireMode: (Int) -> Unit,
    onClickStartSound: (String) -> Unit,
    onClickRestartSound: (String) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val pagerState = rememberPagerState(pageCount = {3})
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        contentColor = CustomTheme.colors.surface
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
                        )
                    }
                    1 -> {
                        TimerEditBox(
                            gap = 0,
                            onClickGap = onClickGap,
                            breakTime = 0,
                            onEnterBreakTime = onEnterBreakTime,
                            repeat = 0,
                            onEnterRepeat = onEnterRepeat,
                        )
                    }
                    2 -> {
                        TimerEditBox(
                            gap = 0,
                            onClickGap = onClickGap,
                            breakTime = 0,
                            onEnterBreakTime = onEnterBreakTime,
                            repeat = 0,
                            onEnterRepeat = onEnterRepeat,
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
            onPlusClick = {},
            onClickGap = {},
            onEnterBreakTime = {},
            onEnterRepeat = {},
            onClickExpireMode = {},
            onClickStartSound = {},
            onClickRestartSound = {},
        )
    }
}