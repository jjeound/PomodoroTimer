package com.pomodoro.timer.presentation

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.pomodoro.timer.CustomWidget
import com.pomodoro.timer.R
import com.pomodoro.timer.presentation.common.ContainerEditBottomSheet
import com.pomodoro.timer.presentation.pomodoro.PomodoroTextEditBottomSheet
import com.pomodoro.timer.presentation.pomodoro.PomodoroTimer
import com.pomodoro.timer.ui.theme.CustomTheme
import com.pomodoro.timer.ui.theme.MyTheme

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    var editMode by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentWidget by viewModel.currentWidget.collectAsStateWithLifecycle()
    val editingWidget by viewModel.editingWidget.collectAsStateWithLifecycle()
    val widgetsByMode by viewModel.widgetsByMode.collectAsStateWithLifecycle()
    val mode = viewModel.mode
    val config = LocalConfiguration.current
    val isTablet = config.smallestScreenWidthDp >= 600
    val editable = isTablet || config.orientation == Configuration.ORIENTATION_PORTRAIT //탭은 다 가능, 폰은 세로모드에서만 가능
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        if(uiState != MainUiState.Loading) {
            if(isTablet) {
//                MainScreenContentTablet(
//                    widgets = widgets,
//                    editMode = editMode,
//                    onEditModeChange = { editMode = it },
//                )
            } else {
                MainScreenContentPhone(
                    widgets = widgetsByMode,
                    editMode = editMode,
                    onEditModeChange = { editMode = it },
                    mode = mode,
                    editable = editable,
                    currentWidget = currentWidget,
                    editingWidget = editingWidget,
                    editWidget = viewModel::editWidget,
                    onCancelEdit = viewModel::onCancelEdit,
                    onDoneEdit = viewModel::onDoneEdit,
                    onAddNewWidget = viewModel::onAddNewWidget,
                    onNextWidget = viewModel::onNextWidget
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
    currentWidget: CustomWidget,
    editingWidget: CustomWidget,
    editWidget: (CustomWidget) -> Unit,
    onCancelEdit: () -> Unit,
    onDoneEdit: () -> Unit,
    onAddNewWidget: () -> Unit = {},
    onNextWidget: (Int) -> Unit
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { widgets.size }
    )
    var showTextEditBottomSheet by remember { mutableStateOf(false) }
    var showContainerEditBottomSheet by remember { mutableStateOf(false) }
    val controller = rememberColorPickerController()
    var showColorPicker by remember { mutableStateOf(false) }
    var colorPickerOption by remember { mutableIntStateOf(0) }
    var showButtons by remember { mutableStateOf(true) }
    LaunchedEffect(widgets.size) {
        if(widgets.isNotEmpty() && editMode) pagerState.animateScrollToPage(widgets.lastIndex)
    }
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (editMode && !showTextEditBottomSheet && !showContainerEditBottomSheet) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .align(Alignment.TopCenter)
                    .zIndex(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                OutlinedButton(
                    onClick = {
                        onEditModeChange(false)
                        onCancelEdit()
                    },
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
                    onClick = {
                        onEditModeChange(false)
                        onDoneEdit()
                    },
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
            modifier = Modifier
                .combinedClickable(
                    enabled = !editMode,
                    onDoubleClick = { showButtons = !showButtons },
                    onLongClick = {
                        if (editable) {
                            onEditModeChange(true)
                        } else {
                            Toast.makeText(context, "가로 모드에서는 편집할 수 없습니다.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    onClick = {},
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .align(Alignment.Center),
            state = pagerState,
            userScrollEnabled = editMode
        ) { page ->
            LaunchedEffect(page) {
                onNextWidget(page)
            }
            PomodoroTimer(
                modifier = Modifier.fillMaxSize(),
                widget = if(editMode) editingWidget else currentWidget,
                editMode = editMode,
                onEditText = {
                    showTextEditBottomSheet = true
                },
                onEditContainer = {
                    showContainerEditBottomSheet = true
                },
                showButtons = showButtons
            )
//            when(mode){
//                0 -> {}
//                1 -> {}
//                2 -> {}
//            }
        }
        if (editMode) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp),
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
            Box(
                modifier = Modifier
                    .padding(40.dp)
                    .border(
                        width = 1.dp,
                        color = CustomTheme.colors.buttonBorder,
                        shape = CircleShape
                    )
                    .align(Alignment.BottomEnd)
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
                    tint = Color.Unspecified
                )
            }
        }
        if(showContainerEditBottomSheet){
            ContainerEditBottomSheet(
                onDismissRequest = {
                    showContainerEditBottomSheet = false
                },
                onColorClick = { color ->
                    editWidget(
                        editingWidget.copy(
                            fgColor = color
                        )
                    )
                },
                onBackgroundColorClick = { color ->
                    editWidget(
                        editingWidget.copy(
                            bgColor = color,
                            backgroundImage = null
                        )
                    )
                },
                onAddImage = {
                    editWidget(
                        editingWidget.copy(
                            bgColor = Color.Transparent,
                            backgroundImage = it
                        )
                    )
                },
                onColorPickerClick = { index ->
                    showColorPicker = true
                    showContainerEditBottomSheet = false
                    colorPickerOption = index
                }
            )
        }
        if(showTextEditBottomSheet){
            when(mode){
                0 -> {
                    PomodoroTextEditBottomSheet(
                        onDismissRequest = {
                            showTextEditBottomSheet = false
                        },
                        onColorClick = {
                            editWidget(
                                editingWidget.copy(
                                    fontColor = it
                                )
                            )
                        },
                        onColorPickerClick = { index ->
                            showColorPicker = true
                            showTextEditBottomSheet = false
                            colorPickerOption = index
                        },
                        onFontClick = {
                            editWidget(
                                editingWidget.copy(
                                    textStyle = it
                                )
                            )
                        },
                        gap = editingWidget.gap,
                        onClickGap = {
                            editWidget(
                                editingWidget.copy(
                                    gap = it
                                )
                            )
                        },
                        breakTime = editingWidget.breakTime,
                        onChangeBreakTime = {
                            editWidget(
                                editingWidget.copy(
                                    breakTime = it
                                )
                            )
                        },
                        repeat = editingWidget.repeat,
                        onChangeRepeat = {
                            editWidget(
                                editingWidget.copy(
                                    repeat = it
                                )
                            )
                        },
                        expireMode = editingWidget.expireMode,
                        onClickExpireMode = {
                            editWidget(
                                editingWidget.copy(
                                    expireMode = it
                                )
                            )
                        },
                        startSound = editingWidget.startSound,
                        onClickStartSound = {
                            editWidget(
                                editingWidget.copy(
                                    startSound = it
                                )
                            )
                        },
                        restartSound = editingWidget.restartSound,
                        onClickRestartSound = {
                            editWidget(
                                editingWidget.copy(
                                    restartSound = it
                                )
                            )
                        }
                    )
                }
            }
        }
        if(showColorPicker){
            Box(
                modifier = Modifier.align(Alignment.BottomCenter).padding(
                    bottom = 40.dp
                )
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.TopEnd).clickable(
                        onClick = {
                            showColorPicker = false
                            if( colorPickerOption == 2){
                                showTextEditBottomSheet = true
                            } else {
                                showContainerEditBottomSheet = true
                            }
                        }
                    ),
                    imageVector = ImageVector.vectorResource(R.drawable.close),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                HsvColorPicker(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(10.dp),
                    controller = controller,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        when (colorPickerOption) {
                            0 -> {
                                editWidget(
                                    editingWidget.copy(
                                        fgColor = Color(colorEnvelope.hexCode.toLong(16))
                                    )
                                )
                            }
                            1 -> {
                                editWidget(
                                    editingWidget.copy(
                                        bgColor = Color(colorEnvelope.hexCode.toLong(16)),
                                        backgroundImage = null
                                    )
                                )
                            }
                            2 -> {
                                editWidget(
                                    editingWidget.copy(
                                        fontColor = Color(colorEnvelope.hexCode.toLong(16))
                                    )
                                )
                            }
                        }
                    }
                )
            }
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
            editingWidget = CustomWidget(),
            editWidget = {},
            onCancelEdit = {},
            onDoneEdit = {},
            onNextWidget = {}
        )
    }
}