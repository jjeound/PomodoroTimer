package com.pomodoro.timer.presentation.pomodoro

import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pomodoro.timer.data.model.CustomWidget
import com.pomodoro.timer.R
import com.pomodoro.timer.ui.theme.CustomTheme
import com.pomodoro.timer.ui.theme.MyTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PomodoroTimer(
    modifier: Modifier,
    widget: CustomWidget,
    editMode: Boolean,
    onEditText: () -> Unit,
    onEditContainer: () -> Unit,
    showButtons: Boolean,
    pagerState: PagerState,
    onAddNewWidget: () -> Unit,
    widgetsSize: Int,
    viewModel: PomodoroViewModel = hiltViewModel()
){
    viewModel.setRP(widget.repeat)
    viewModel.setBT(widget.breakTime)
    val remainingTime by remember { derivedStateOf { viewModel.remainingTime } }
    val state by remember { derivedStateOf { viewModel.state } }
    val minutesTxt = when(widget.gap){
        5 -> {
            listOf("15", "20", "25", "30", "35", "40", "45", "50", "55", "0", "5", "10")
        }
        10 -> {
            listOf("", "20", "", "30", "", "40", "", "50", "", "0", "", "10")
        }
        15 -> {
            listOf("15", "", "", "30", "", "", "45", "", "", "0", "", "")
        }
        else -> {
            listOf("0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55")
        }
    }
    val context = LocalContext.current
    val vibrator = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibManager = context.getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
    }
    val vibrationEffect: (Long, Int) -> VibrationEffect = { duration, amplitude ->
        VibrationEffect.createOneShot(duration, amplitude)
    }
//    val mediaPlayer = remember {
//        listOf(MediaPlayer.create(context, widget.startSound), MediaPlayer.create(context, widget.breakTimeSound))
//    }
//    DisposableEffect(Unit) {
//        onDispose {
//            mediaPlayer.forEach {
//                it.release()
//            }
//        }
//    }
    LaunchedEffect(editMode) {
        if(editMode && state == TimerState.RUNNING){
            viewModel.onPause()
        }
    }
//    LaunchedEffect(Unit) {
//        viewModel.eventFlow.collect { event ->
//            when (event) {
//                is UiEvent.PlayStartSound -> {
//                    when(widget.soundMode){
//                        SoundMode.SOUND -> {
//                            mediaPlayer[0].start()
//                        }
//                        SoundMode.VIBRATE -> {
//                            vibrator.vibrate(vibrationEffect(150L, VibrationEffect.DEFAULT_AMPLITUDE))
//                        }
//                        SoundMode.NO_SOUND -> {}
//                    }
//                }
//                is UiEvent.PlayBreakSound -> {
//                    when(widget.soundMode){
//                        SoundMode.SOUND -> {
//                            mediaPlayer[1].start()
//                        }
//                        SoundMode.VIBRATE -> {
//                            vibrator.vibrate(vibrationEffect(300L, VibrationEffect.DEFAULT_AMPLITUDE))
//                        }
//                        SoundMode.NO_SOUND -> {}
//                    }
//                }
//            }
//        }
//    }
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
        onEditText = onEditText,
        onEditContainer = onEditContainer,
        editMode = editMode,
        showButtons = showButtons,
        pagerState = pagerState,
        onAddNewWidget = onAddNewWidget,
        widgetsSize = widgetsSize
    )
}

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
    onEditText: () -> Unit = {},
    onEditContainer: () -> Unit = {},
    editMode: Boolean = false,
    showButtons: Boolean = true,
    pagerState: PagerState,
    onAddNewWidget: () -> Unit,
    widgetsSize: Int,
){
    val windowInfo = LocalWindowInfo.current
    val screenSize = with(LocalDensity.current) {
        DpSize(
            width = windowInfo.containerSize.width.toDp(),
            height = windowInfo.containerSize.height.toDp()
        )
    }
    val isLandscape = screenSize.width > screenSize.height
    val radius = (if (isLandscape) screenSize.height else screenSize.width) / 2 * 0.8f
    Box(modifier = modifier){
        Box(
            modifier = Modifier.align(
                if(editMode) Alignment.TopCenter else Alignment.Center
            ).padding(
                top = if(editMode) 100.dp else 0.dp
            ).border(
                width = 3.dp,
                color = if(editMode) CustomTheme.colors.indicatorBox else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
        ){
            Timer(
                text = minutesTxt,
                radius = radius,
                textStyle = widget.textStyle,
                color = widget.fgColor,
                bgColor = widget.bgColor,
                fontColor = widget.fontColor,
                handColor = widget.handColor,
                edgeColor = widget.edgeColor,
                image = widget.backgroundImage,
                remainingTime = remainingTime,
                editMode = editMode,
                onEditText = onEditText,
                onEditContainer = onEditContainer,
            )
        }
        if(!editMode && showButtons){
            Box(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp)
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
        if (editMode) {
            Row(
                modifier = Modifier
                    .padding(bottom = 40.dp).align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(widgetsSize) { index ->
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
            Box(
                modifier = Modifier
                    .padding(40.dp)
                    .border(
                        width = 1.dp,
                        color = CustomTheme.colors.buttonBorder,
                        shape = CircleShape
                    ).align(Alignment.BottomEnd)
                    .clickable(
                        onClick = {
                            onAddNewWidget()
                        }
                    ),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.plus),
                    contentDescription = null,
                    tint = CustomTheme.colors.icon
                )
            }
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
    bgColor: Color,
    handColor: Color,
    edgeColor: Color,
    image: Uri?,
    remainingTime: Int = 0,
    editMode: Boolean,
    onEditText: () -> Unit = {},
    onEditContainer: () -> Unit = {},
) {
    val context = LocalDensity.current
    Box(
        modifier = Modifier.padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        val angleStep = 360f / text.size
        val radiusForTime = radius * 0.95f
        text.forEachIndexed { index, str ->
            val angle = angleStep * index
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .wrapContentSize()
                    .graphicsLayer {
                        translationX = radiusForTime.toPx() * cos(Math.toRadians(angle.toDouble())).toFloat()
                        translationY = radiusForTime.toPx() * sin(Math.toRadians(angle.toDouble())).toFloat()
                        rotationZ = angle
                    }.then(
                        if (str == "0" && editMode) {
                            Modifier.border(
                                width = 3.dp,
                                color = CustomTheme.colors.indicatorBox,
                                shape = RoundedCornerShape(12.dp)
                            ).clickable(
                                onClick = onEditText,
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                        } else {
                            Modifier
                        }
                    )
            ) {
                Text(
                    text = str,
                    style = textStyle,
                    color = fontColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.graphicsLayer {
                        rotationZ = -angle // 글자를 수평으로 유지
                    }.size(40.dp)
                )
            }
        }
        val angleStepForDot = 360f / 60
        val radiusForDot = radius * 0.78f
        val radiusForCircle = radius * 0.75f
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
                            },
                        tint = edgeColor
                    )
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.short_dot),
                        contentDescription = null,
                        modifier = Modifier
                            .width(4.dp)
                            .graphicsLayer{
                                rotationZ = angle + 90f
                            },
                        tint = edgeColor
                    )
                }
            }
        }
        val sweepAngle = (remainingTime / 3600f) * 360f
        if(image != null){
            Box(
                modifier = Modifier.clip(shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier.size(radiusForCircle * 2),
                    model = image,
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                )
            }
        }
        Canvas(
            modifier = Modifier.size(radiusForCircle * 2)
                .clickable(
                    enabled = editMode,
                    onClick = onEditContainer,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        ) {
            drawCircle(
                color = bgColor
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = if(editMode) -270f else sweepAngle,
                useCenter = true,
            )
        }
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.handle),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center).rotate(
                if(editMode) 0f else sweepAngle
            ),
            tint = handColor
        )
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
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ElevatedButton(
            onClick = onReset,
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 10.dp)
                .clip(RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = CustomTheme.colors.surface,
            )
        ) {
            Text(
                text = stringResource(id = R.string.reset),
                color = CustomTheme.colors.text,
                style = CustomTheme.typography.buttonTimerSmall
            )
        }
        ElevatedButton(
            onClick = {
                when(state){
                    TimerState.IDLE -> onStart()
                    TimerState.RUNNING -> onPause()
                    TimerState.PAUSED -> onResume()
                    TimerState.BREAK -> onStart()
                }
            },
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 10.dp)
                .clip(RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = CustomTheme.colors.surface,
            ),
        ) {
            when(state){
                TimerState.IDLE -> Text(
                    text = stringResource(id = R.string.start),
                    color = CustomTheme.colors.text,
                    style = CustomTheme.typography.buttonTimerSmall
                )
                TimerState.RUNNING -> Text(
                    text = stringResource(id = R.string.pause),
                    color = CustomTheme.colors.text,
                    style = CustomTheme.typography.buttonTimerSmall
                )
                TimerState.PAUSED -> Text(
                    text = stringResource(id = R.string.resume),
                    color = CustomTheme.colors.text,
                    style = CustomTheme.typography.buttonTimerSmall
                )
                TimerState.BREAK -> Text(
                    text = stringResource(id = R.string.start),
                    color = CustomTheme.colors.text,
                    style = CustomTheme.typography.buttonTimerSmall
                )
            }
        }
    }
}

@Preview()
@Composable
fun PomodoroTimerContentPreview() {
    MyTheme {
        PomodoroTimerContent(
            widget = CustomWidget(),
            minutesTxt = listOf("15", "20", "25", "30", "35", "40", "45", "50", "55", "0", "5", "10"),
            editMode = true,
            pagerState = rememberPagerState(0, pageCount = { 3 }),
            onAddNewWidget = {},
            widgetsSize = 3
        )
    }
}

@Preview
@Composable
fun TimerButtonsPreview() {
    MyTheme {
        TimerButtons(state = TimerState.IDLE)
    }
}
