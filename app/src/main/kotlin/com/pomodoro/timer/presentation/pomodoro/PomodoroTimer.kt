package com.pomodoro.timer.presentation.pomodoro

import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pomodoro.timer.data.model.CustomWidget
import com.pomodoro.timer.R
import com.pomodoro.timer.data.model.SoundMode
import com.pomodoro.timer.ui.theme.CustomTheme
import com.pomodoro.timer.ui.theme.MyTheme
import kotlin.math.atan2
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
    onDeleteWidget: () -> Unit,
    showDelete: Boolean,
    onShowDeleteChange: (Boolean) -> Unit,
    pagerEnabled: Boolean,
    isLandscape: Boolean,
    patterns: List<Int>,
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
    val mediaPlayer = listOf(MediaPlayer.create(context, widget.startSound), MediaPlayer.create(context, widget.breakTimeSound))
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.forEach {
                it.release()
            }
        }
    }
    LaunchedEffect(editMode) {
        if(editMode && state == TimerState.RUNNING){
            viewModel.onPause()
        }
    }
    LaunchedEffect(state) {
        if(state == TimerState.PAUSED || state == TimerState.IDLE){
            mediaPlayer.forEach {
                if (it.isPlaying) {
                    it.stop()
                    it.prepare()
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.PlayStartSound -> {
                    when(widget.soundMode){
                        SoundMode.SOUND -> {
                            mediaPlayer[0].start()
                        }
                        SoundMode.VIBRATE -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                                vibrator.vibrate(vibrationEffect(300L, VibrationEffect.EFFECT_TICK))
                            } else {
                                vibrator.vibrate(300L)
                            }
                        }
                        SoundMode.NO_SOUND -> {}
                    }
                }
                is UiEvent.PlayBreakSound -> {
                    when(widget.soundMode){
                        SoundMode.SOUND -> {
                            mediaPlayer[1].start()
                        }
                        SoundMode.VIBRATE -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                                vibrator.vibrate(vibrationEffect(300L, VibrationEffect.EFFECT_TICK))
                            } else {
                                vibrator.vibrate(300L)
                            }
                        }
                        SoundMode.NO_SOUND -> {}
                    }
                }
            }
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
        onEditText = onEditText,
        onEditContainer = onEditContainer,
        editMode = editMode,
        showButtons = showButtons,
        onDeleteWidget = onDeleteWidget,
        showDelete = showDelete,
        onShowDeleteChange = onShowDeleteChange,
        pagerEnabled = pagerEnabled,
        isLandscape = isLandscape,
        patterns = patterns,
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
    onDeleteWidget: () -> Unit,
    showDelete: Boolean = false,
    onShowDeleteChange: (Boolean) -> Unit = {},
    pagerEnabled: Boolean = false,
    isLandscape: Boolean,
    patterns: List<Int>,
){
    val windowInfo = LocalWindowInfo.current
    val context = LocalContext.current
    val screenSize = with(LocalDensity.current) {
        DpSize(
            width = windowInfo.containerSize.width.toDp(),
            height = windowInfo.containerSize.height.toDp()
        )
    }
    val radius = (if (isLandscape) screenSize.height else screenSize.width) / 2 * 0.8f
    var offsetY by remember { mutableFloatStateOf(0f) }
    val animatedOffsetY by animateFloatAsState(
        targetValue = offsetY,
        animationSpec = tween(durationMillis = 200),
        label = "offsetYAnim"
    )
    val text = stringResource(R.string.widget_delete_alert)
    Box(
        modifier = modifier.offset(y = animatedOffsetY.dp).then(
            if(editMode && pagerEnabled){
                Modifier.pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            if (offsetY < -150f) {
                                if(widget.id < 3){
                                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                                } else {
                                    onShowDeleteChange(true)
                                }
                            }
                            offsetY = 0f
                        },
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            offsetY = (offsetY + dragAmount).coerceIn(-300f, 0f)
                        }
                    )
                }.clickable(
                    enabled = showDelete,
                    onClick = { onShowDeleteChange(false)}
                )
            } else Modifier
        )
    ){
        Box(
            modifier = Modifier.align(
                if(editMode && !isLandscape) Alignment.TopCenter else Alignment.Center
            ).padding(
                top = if(editMode && !isLandscape) 100.dp else 0.dp
            ).border(
                width = 3.dp,
                color = if(editMode) CustomTheme.colors.indicatorBox else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
        ){
            Timer(
                text = minutesTxt,
                radius = radius,
                fontFamily = widget.fontFamily,
                fontSize = widget.fontSize,
                color = widget.fgColor,
                bgColor = widget.bgColor,
                fontColor = widget.fontColor,
                handColor = widget.handColor,
                edgeColor = widget.edgeColor,
                image = widget.backgroundImage,
                pattern = widget.pattern,
                patterns = patterns,
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
        AnimatedVisibility(
            visible = showDelete && editMode,
            modifier = if(isLandscape){
                Modifier.align(Alignment.CenterStart).padding(start = 40.dp)
            } else {
                Modifier.align(Alignment.BottomCenter).padding(bottom = 100.dp)
            }
        ) {
            IconButton(
                onClick = {
                    onShowDeleteChange(false)
                    onDeleteWidget()
                },
                modifier = Modifier,
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = CustomTheme.colors.primary.copy(
                        alpha = 0.5f
                    ),
                    contentColor = CustomTheme.colors.primary
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.trash),
                    contentDescription = "Delete",
                )
            }
        }
    }
}

@Composable
fun Timer(
    text: List<String>,
    radius: Dp = 150.dp,
    fontFamily: FontFamily,
    fontSize: Float,
    color: Color,
    fontColor: Color,
    bgColor: Color,
    handColor: Color,
    edgeColor: Color,
    pattern: Int,
    patterns: List<Int>,
    image: String?,
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
        val angleStepForDot = 360f / 60
        val radiusForDot = radius * 0.80f
        val radiusForCircle = radius * 0.77f
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
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.long_dot),
                    contentDescription = null,
                    modifier = Modifier
                        .width(2.dp)
                        .graphicsLayer{
                            rotationZ = angle + 90f
                        },
                    tint = edgeColor
                )
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
                    model = image.toUri(),
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
                sweepAngle = if(editMode) 270f else sweepAngle,
                useCenter = true,
            )
        }
        if(pattern != 0){
            IconGridWithArcClip(radiusForCircle, editMode, patterns[pattern])
        }
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.handle),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center).rotate(
                if(editMode) -90f else sweepAngle
            ),
            tint = handColor
        )
        val angleStep = 360f / text.size
        val radiusForTime = radius * 0.97f
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
                            ).size(40.dp).clickable(
                                onClick = onEditText,
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = str,
                    style = TextStyle(
                        fontSize = fontSize.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = fontColor,
                    modifier = Modifier.graphicsLayer {
                        rotationZ = -angle // 글자를 수평으로 유지
                    }
                )
            }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IconGridWithArcClip(
    radiusForCircle: Dp,
    editMode: Boolean,
    pattern: Int,
) {
    val circleRadiusPx = with(LocalDensity.current) { radiusForCircle.toPx() }
    Box(
        modifier = Modifier
            .size(radiusForCircle * 2)
            .clip(CircleShape)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(45) { index ->

                // 각 아이콘의 좌표 계산
                val col = index % 5
                val row = index / 5

                val gridWidth = circleRadiusPx * 2
                val cellSize = gridWidth / 5f

                val cx = col * cellSize + cellSize / 2f
                val cy = row * cellSize + cellSize / 2f

                // 중심 기준 좌표
                val dx = cx - circleRadiusPx
                val dy = cy - circleRadiusPx

                // dy는 아래로 증가하기 때문에 반전 필요
                val angle = Math.toDegrees(atan2(-dy, dx).toDouble()).let {
                    if (it < 0) it + 360 else it
                }

                val isInHiddenArea = angle in 90.0..180.0  // ⬅ 왼쪽 위 부분

                if (!(editMode && isInHiddenArea)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(pattern),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
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
            onDeleteWidget = {},
            isLandscape = false,
            patterns = emptyList()
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
