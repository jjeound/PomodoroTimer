package com.pomodoro.timer.presentation.pomodoro

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pomodoro.timer.CustomWidget
import com.pomodoro.timer.R
import com.pomodoro.timer.presentation.AutoSlideImagePager
import com.pomodoro.timer.ui.theme.CustomTheme
import com.pomodoro.timer.ui.theme.MyTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PomodoroTimer(
    modifier: Modifier,
    widget: CustomWidget,
    viewModel: PomodoroViewModel = hiltViewModel()
){
    viewModel.setRP(widget.repeat)
    viewModel.setBT(widget.breakTime)
    val remainingTime by remember { derivedStateOf { viewModel.remainingTime } }
    val isRunning by remember { derivedStateOf { viewModel.isRunning } }
    val state by remember { derivedStateOf { viewModel.state } }
    val minutesTxt = when(widget.interval){
        5 -> {
            listOf("15", "20", "25", "30", "35", "40", "45", "50", "55", "0", "5", "10")
        }
        10 -> {
            listOf("0", "10", "20", "30", "40", "50")
        }
        15 -> {
            listOf("0", "15", "30", "45")
        }
        else -> {
            listOf("0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55")
        }
    }
    PomodoroTimerContent(
        modifier = modifier,
        widget = widget,
        minutesTxt = minutesTxt,
        remainingTime = remainingTime,
        state = state,
        onStart = viewModel::onStart,
        onPause = viewModel::onPause,
        onReset = viewModel::onReset,
        onResume = viewModel::onResume,
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PomodoroTimerContent(
    modifier: Modifier = Modifier,
    widget: CustomWidget,
    minutesTxt: List<String>,
    remainingTime: Int = 0,
    state: TimerState = TimerState.IDLE,
    onStart: () -> Unit = {},
    onPause: () -> Unit = {},
    onReset: () -> Unit = {},
    onResume: () -> Unit = {},
){
    val currentWidthDp = LocalConfiguration.current.screenWidthDp.dp
    Box(
        modifier = modifier.padding(20.dp)
    ){
        Timer(
            text = minutesTxt,
            radius = currentWidthDp / 2,
            textStyle = widget.fontStyle,
            color = widget.fgColor,
            fontColor = widget.fontStyle.color,
            images = widget.backgroundImage,
            remainingTime = remainingTime
        )
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ){
            TimerButtons(
                state = state,
                onStart = onStart,
                onPause = onPause,
                onReset = onReset,
                onResume = onResume,
            )
        }
    }
}

@Composable
fun Timer(
    text: List<String>,
    radius: Dp = 150.dp,
    textStyle: TextStyle,
    color: Color,
    fontColor: Color,
    images: List<String>?,
    remainingTime: Int = 0,
) {
    val context = LocalDensity.current
    Box(
        modifier = Modifier.size(radius * 2),
        contentAlignment = Alignment.Center
    ) {
        val angleStep = 360f / text.size
        text.forEachIndexed { index, char ->
            val angle = angleStep * index
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        translationX = radius.toPx() * cos(Math.toRadians(angle.toDouble())).toFloat()
                        translationY = radius.toPx() * sin(Math.toRadians(angle.toDouble())).toFloat()
                        rotationZ = angle
                    }
            ) {
                Text(
                    text = char,
                    style = textStyle,
                    color = fontColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.graphicsLayer {
                        rotationZ = -angle // 글자를 수평으로 유지
                    }
                )
            }
        }
        val angleStepForDot = 360f / 60
        val radiusForDot = radius * 0.8f
        repeat(60){ idx ->
            val angle = angleStepForDot * idx
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        translationX = with(context) {radiusForDot.toPx() * cos(Math.toRadians(angle.toDouble())).toFloat()}
                        translationY = with(context) {radiusForDot.toPx() * sin(Math.toRadians(angle.toDouble())).toFloat()}
                    }
            ) {
                if(idx % 5 == 0){
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.long_dot),
                        contentDescription = null,
                        modifier = Modifier
                            .width(4.dp)
                            .graphicsLayer{
                                rotationZ = angle + 90f
                            }
                    )
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.short_dot),
                        contentDescription = null,
                        modifier = Modifier
                            .width(4.dp)
                            .graphicsLayer{
                                rotationZ = angle + 90f
                            }
                    )
                }
            }
        }
        val sweepAngle = (remainingTime / 3600f) * 360f
        Canvas(modifier = Modifier.size(radiusForDot * 2)) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = true,
            )
        }
        if(images != null && images.isNotEmpty()){
            AutoSlideImagePager(
                images = images,
                modifier = Modifier.size(radiusForDot * 2)
            )
        }
    }
}

@Composable
fun TimerButtons(
    state: TimerState,
    onStart: () -> Unit = {},
    onPause: () -> Unit = {},
    onReset: () -> Unit = {},
    onResume: () -> Unit = {},
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        OutlinedButton(
            onClick = onReset,
            border = BorderStroke(1.dp, CustomTheme.colors.buttonBorderFocused),
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 10.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Text(
                text = stringResource(id = R.string.reset),
                color = CustomTheme.colors.textPrimary,
                style = CustomTheme.typography.button1
            )
        }
        OutlinedButton(
            onClick = {
                when(state){
                    TimerState.IDLE -> onStart()
                    TimerState.RUNNING -> onPause()
                    TimerState.PAUSED -> onResume()
                }
            },
            border = BorderStroke(1.dp, CustomTheme.colors.buttonBorderFocused),
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 10.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            when(state){
                TimerState.IDLE -> Text(
                    text = stringResource(id = R.string.start),
                    color = CustomTheme.colors.textPrimary,
                    style = CustomTheme.typography.button1
                )
                TimerState.RUNNING -> Text(
                    text = stringResource(id = R.string.pause),
                    color = CustomTheme.colors.textPrimary,
                    style = CustomTheme.typography.button1
                )
                TimerState.PAUSED -> Text(
                    text = stringResource(id = R.string.resume),
                    color = CustomTheme.colors.textPrimary,
                    style = CustomTheme.typography.button1
                )
            }
        }
    }
}

@Preview
@Composable
fun PomodoroTimerContentPreview() {
    MyTheme {
        PomodoroTimerContent(
            widget = CustomWidget(),
            minutesTxt = listOf("15", "20", "25", "30", "35", "40", "45", "50", "55", "0", "5", "10"),

        )
    }
}