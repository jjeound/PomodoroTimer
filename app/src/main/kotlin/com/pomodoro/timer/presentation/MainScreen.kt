package com.pomodoro.timer.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pomodoro.timer.R
import com.pomodoro.timer.data.model.CustomWidget
import com.pomodoro.timer.data.model.Mode
import com.pomodoro.timer.presentation.components.ColorPicker
import com.pomodoro.timer.presentation.components.ContainerEditSheet
import com.pomodoro.timer.presentation.components.EditButtons
import com.pomodoro.timer.presentation.pomodoro.PomodoroTextEditSheet
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
    val colors by viewModel.colors.collectAsStateWithLifecycle()
    val mode = viewModel.mode
    val showButtons = viewModel.showButtons
    val config = LocalConfiguration.current
    val isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        if(uiState != MainUiState.Loading) {
            MainScreenContent(
                widgets = widgetsByMode,
                editMode = editMode,
                onEditModeChange = { editMode = it },
                mode = mode,
                currentWidget = currentWidget,
                editingWidget = editingWidget,
                editWidget = viewModel::editWidget,
                onCancelEdit = viewModel::onCancelEdit,
                onDoneEdit = viewModel::onDoneEdit,
                onAddNewWidget = viewModel::onAddNewWidget,
                onNextWidget = viewModel::onNextWidget,
                onAddNewColor = viewModel::saveColor,
                onUpdateColor = viewModel::updateColor,
                onDeleteColor = viewModel::deleteColor,
                colors = colors,
                isLandscape = isLandscape,
                showButtons = showButtons,
                onShowButtonsChange = viewModel::onShowButtonsChange,
                onDeleteWidget = viewModel::deleteWidget
            )
        } else {
           Box(
               modifier = Modifier.fillMaxSize()
           ){
               CircularProgressIndicator(
                   modifier = Modifier.align(Alignment.Center).size(100.dp),
                   color = CustomTheme.colors.icon
               )
           }
        }
    }
}

@Composable
fun MainScreenContent(
    widgets: List<CustomWidget>,
    editMode: Boolean,
    onEditModeChange: (Boolean) -> Unit,
    mode: Mode,
    currentWidget: CustomWidget,
    editingWidget: CustomWidget,
    editWidget: (CustomWidget) -> Unit,
    onCancelEdit: () -> Unit,
    onDoneEdit: () -> Unit,
    onAddNewWidget: () -> Unit = {},
    onNextWidget: (Int) -> Unit,
    onAddNewColor: (Color) -> Unit = {},
    onUpdateColor: (oldColor: Color, newColor: Color) -> Unit,
    onDeleteColor: (Color) -> Unit = {},
    colors: List<Color> = emptyList(),
    isLandscape: Boolean,
    showButtons: Boolean,
    onShowButtonsChange: () -> Unit,
    onDeleteWidget: () -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { widgets.size }
    )
    var showTextEditBottomSheet by remember { mutableStateOf(false) }
    var showContainerEditBottomSheet by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }
    var colorPickerOption by remember { mutableIntStateOf(0) }
    var currentColor by remember { mutableStateOf(colors.first()) }
    var isAddColor by remember { mutableStateOf(true) }
    val pagerEnabled = !showTextEditBottomSheet && !showContainerEditBottomSheet && !showColorPicker
    var showDelete by remember { mutableStateOf(false) }
    LaunchedEffect(widgets.size) {
        if(widgets.isNotEmpty() && editMode) pagerState.animateScrollToPage(widgets.lastIndex)
    }
    LaunchedEffect(showTextEditBottomSheet, showContainerEditBottomSheet, editMode) {
        if (showTextEditBottomSheet || showContainerEditBottomSheet || !editMode) {
            showDelete = false
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (editMode && pagerEnabled) {
            EditButtons(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .align(Alignment.TopCenter)
                    .zIndex(1f),
                onEditModeChange = onEditModeChange,
                onDoneEdit = onDoneEdit,
                onCancelEdit = onCancelEdit
            )
        }
        if(isLandscape){
            Row(
                modifier = Modifier.fillMaxSize().align(Alignment.Center),
            ) {
                HorizontalPager(
                    modifier = Modifier.weight(1f)
                        .combinedClickable(
                            enabled = !editMode,
                            onDoubleClick = onShowButtonsChange,
                            onLongClick = {
                                onEditModeChange(true)
                            },
                            onClick = {},
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    state = pagerState,
                    userScrollEnabled = editMode && pagerEnabled
                ) {
                    LaunchedEffect(pagerState) {
                        snapshotFlow { pagerState.currentPage }
                            .collect { page ->
                                onNextWidget(page)
                            }
                    }
                    PomodoroTimer(
                        modifier = Modifier.fillMaxSize(),
                        widget = if(editMode) editingWidget else currentWidget,
                        editMode = editMode,
                        onEditText = {
                            showTextEditBottomSheet = true
                            showContainerEditBottomSheet = false
                        },
                        onEditContainer = {
                            showContainerEditBottomSheet = true
                            showTextEditBottomSheet = false
                        },
                        showButtons = showButtons,
                        onDeleteWidget = onDeleteWidget,
                        showDelete = showDelete,
                        onShowDeleteChange = { showDelete = it },
                        pagerEnabled = pagerEnabled
                    )
                }
                if (showContainerEditBottomSheet || showTextEditBottomSheet || showColorPicker) {
                    VerticalDivider(
                        thickness = 1.dp,
                        color = CustomTheme.colors.divider,
                    )
                }
                if(showContainerEditBottomSheet){
                    ContainerEditSheet(
                        modifier = Modifier.weight(1f),
                        onDismissRequest = {
                            showContainerEditBottomSheet = false
                        },
                        onColorClick = { color ->
                            currentColor = color
                            editWidget(
                                editingWidget.copy(
                                    fgColor = color
                                )
                            )
                        },
                        onBackgroundColorClick = { color ->
                            currentColor = color
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
                            isAddColor = false
                            showColorPicker = true
                            showContainerEditBottomSheet = false
                            colorPickerOption = index
                        },
                        onAddBtnClick = { index ->
                            isAddColor = true
                            showColorPicker = true
                            showContainerEditBottomSheet = false
                            colorPickerOption = index
                        },
                        onHandColorClick = { color ->
                            currentColor = color
                            editWidget(
                                editingWidget.copy(
                                    handColor = color
                                )
                            )
                        },
                        onEdgeColorClick = { color ->
                            currentColor = color
                            editWidget(
                                editingWidget.copy(
                                    edgeColor = color
                                )
                            )
                        },
                        colors = colors,
                        currentColor = currentColor,
                        isLandScape = true,
                        onDeleteColor = onDeleteColor
                    )
                }
                if(showTextEditBottomSheet){
                    when(mode){
                        Mode.POMODORO -> {
                            PomodoroTextEditSheet(
                                modifier = Modifier.weight(1f),
                                onDismissRequest = {
                                    showTextEditBottomSheet = false
                                },
                                onColorClick = { color ->
                                    currentColor = color
                                    editWidget(
                                        editingWidget.copy(
                                            fontColor = color
                                        )
                                    )
                                },
                                onColorPickerClick = { index ->
                                    isAddColor = false
                                    showColorPicker = true
                                    showTextEditBottomSheet = false
                                    colorPickerOption = index
                                },
                                onAddBtnClick = { index ->
                                    isAddColor = true
                                    showColorPicker = true
                                    showTextEditBottomSheet = false
                                    colorPickerOption = index
                                },
                                onFontClick = {
                                    editWidget(
                                        editingWidget.copy(
                                            fontFamily = it
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
                                soundMode = editingWidget.soundMode,
                                onClickSoundMode = {
                                    editWidget(
                                        editingWidget.copy(
                                            soundMode = it
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
                                restartSound = editingWidget.breakTimeSound,
                                onChangeBreakTimeSound = {
                                    editWidget(
                                        editingWidget.copy(
                                            breakTimeSound = it
                                        )
                                    )
                                },
                                colors = colors,
                                currentColor = currentColor,
                                isLandscape = true,
                                onDeleteColor = onDeleteColor,
                                fontSize = editingWidget.fontSize,
                                onFontSizeChange = { size ->
                                    editWidget(
                                        editingWidget.copy(
                                            fontSize = size
                                        )
                                    )
                                },
                            )
                        }
                        else -> {}
                    }
                }
                if(showColorPicker){
                    ColorPicker(
                        modifier = Modifier.weight(1f).padding(20.dp),
                        onClose = {
                            showColorPicker = false
                            when(colorPickerOption){
                                0 -> {
                                    editWidget(
                                        editingWidget.copy(
                                            fgColor = currentWidget.fgColor
                                        )
                                    )
                                }
                                1 -> {
                                    editWidget(
                                        editingWidget.copy(
                                            bgColor = currentWidget.bgColor,
                                        )
                                    )
                                }
                                2 -> {
                                    editWidget(
                                        editingWidget.copy(
                                            handColor = currentWidget.handColor
                                        )
                                    )
                                }
                                3 -> {
                                    editWidget(
                                        editingWidget.copy(
                                            edgeColor = currentWidget.edgeColor
                                        )
                                    )
                                }
                                4 -> {
                                    editWidget(
                                        editingWidget.copy(
                                            fontColor = currentWidget.fontColor
                                        )
                                    )
                                }
                            }
                            if( colorPickerOption == 4){
                                showTextEditBottomSheet = true
                            } else {
                                showContainerEditBottomSheet = true
                            }
                        },
                        onConfirm = { color ->
                            onAddNewColor(color)
                            currentColor = color
                            showColorPicker = false
                            if( colorPickerOption == 4){
                                showTextEditBottomSheet = true
                            } else {
                                showContainerEditBottomSheet = true
                            }
                        },
                        colorPickerOption = colorPickerOption,
                        editingWidget = editingWidget,
                        editWidget = editWidget,
                    )
                }
            }
        } else {
            HorizontalPager(
                modifier = Modifier
                    .combinedClickable(
                        enabled = !editMode,
                        onDoubleClick = onShowButtonsChange,
                        onLongClick = { onEditModeChange(true) },
                        onClick = {},
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .align(Alignment.Center),
                state = pagerState,
                userScrollEnabled = editMode && pagerEnabled
            ) {
                LaunchedEffect(pagerState) {
                    snapshotFlow { pagerState.currentPage }
                        .collect { page ->
                            onNextWidget(page)
                        }
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
                    showButtons = showButtons,
                    onDeleteWidget = onDeleteWidget,
                    showDelete = showDelete,
                    onShowDeleteChange = { showDelete = it },
                    pagerEnabled = pagerEnabled
                )
            }
            if(showContainerEditBottomSheet){
                ContainerEditSheet(
                    modifier = Modifier,
                    onDismissRequest = {
                        showContainerEditBottomSheet = false
                    },
                    onColorClick = { color ->
                        currentColor = color
                        editWidget(
                            editingWidget.copy(
                                fgColor = color
                            )
                        )
                    },
                    onBackgroundColorClick = { color ->
                        currentColor = color
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
                        isAddColor = false
                        showColorPicker = true
                        showContainerEditBottomSheet = false
                        colorPickerOption = index
                    },
                    onAddBtnClick = { index ->
                        isAddColor = true
                        showColorPicker = true
                        showContainerEditBottomSheet = false
                        colorPickerOption = index
                    },
                    onHandColorClick = { color ->
                        currentColor = color
                        editWidget(
                            editingWidget.copy(
                                handColor = color
                            )
                        )
                    },
                    onEdgeColorClick = { color ->
                        currentColor = color
                        editWidget(
                            editingWidget.copy(
                                edgeColor = color
                            )
                        )
                    },
                    colors = colors,
                    currentColor = currentColor,
                    isLandScape = false,
                    onDeleteColor = onDeleteColor
                )
            }
            if(showTextEditBottomSheet){
                when(mode){
                    Mode.POMODORO -> {
                        PomodoroTextEditSheet(
                            modifier = Modifier,
                            onDismissRequest = {
                                showTextEditBottomSheet = false
                            },
                            onColorClick = { color ->
                                currentColor = color
                                editWidget(
                                    editingWidget.copy(
                                        fontColor = color
                                    )
                                )
                            },
                            onColorPickerClick = { index ->
                                isAddColor = false
                                showColorPicker = true
                                showTextEditBottomSheet = false
                                colorPickerOption = index
                            },
                            onAddBtnClick = { index ->
                                isAddColor = true
                                showColorPicker = true
                                showTextEditBottomSheet = false
                                colorPickerOption = index
                            },
                            onFontClick = {
                                editWidget(
                                    editingWidget.copy(
                                        fontFamily = it
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
                            soundMode = editingWidget.soundMode,
                            onClickSoundMode = {
                                editWidget(
                                    editingWidget.copy(
                                        soundMode = it
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
                            restartSound = editingWidget.breakTimeSound,
                            onChangeBreakTimeSound = {
                                editWidget(
                                    editingWidget.copy(
                                        breakTimeSound = it
                                    )
                                )
                            },
                            colors = colors,
                            currentColor = currentColor,
                            isLandscape = false,
                            onDeleteColor = onDeleteColor,
                            fontSize = editingWidget.fontSize,
                            onFontSizeChange = { size ->
                                editWidget(
                                    editingWidget.copy(
                                        fontSize = size
                                    )
                                )
                            },
                        )
                    }
                    else -> {}
                }
            }
            if(showColorPicker){
                ColorPicker(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(
                        bottom = 120.dp
                    ),
                    onClose = {
                        showColorPicker = false
                        when(colorPickerOption){
                            0 -> {
                                editWidget(
                                    editingWidget.copy(
                                        fgColor = currentWidget.fgColor
                                    )
                                )
                            }
                            1 -> {
                                editWidget(
                                    editingWidget.copy(
                                        bgColor = currentWidget.bgColor,
                                    )
                                )
                            }
                            2 -> {
                                editWidget(
                                    editingWidget.copy(
                                        handColor = currentWidget.handColor
                                    )
                                )
                            }
                            3 -> {
                                editWidget(
                                    editingWidget.copy(
                                        edgeColor = currentWidget.edgeColor
                                    )
                                )
                            }
                            4 -> {
                                editWidget(
                                    editingWidget.copy(
                                        fontColor = currentWidget.fontColor
                                    )
                                )
                            }
                        }
                        if( colorPickerOption == 4){
                            showTextEditBottomSheet = true
                        } else {
                            showContainerEditBottomSheet = true
                        }
                    },
                    onConfirm = { color ->
                        if(isAddColor){
                            onAddNewColor(color)
                        } else {
                            onUpdateColor(currentColor, color)
                        }
                        currentColor = color
                        showColorPicker = false
                        if( colorPickerOption == 4){
                            showTextEditBottomSheet = true
                        } else {
                            showContainerEditBottomSheet = true
                        }
                    },
                    colorPickerOption = colorPickerOption,
                    editingWidget = editingWidget,
                    editWidget = editWidget
                )
            }
        }
        if (editMode && pagerEnabled) {
            Row(
                modifier = Modifier
                    .padding(bottom = 20.dp).align(Alignment.BottomCenter),
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


@Preview(device = TABLET)
@Composable
fun MainScreenContentTabletPreview(){
    MyTheme {
        MainScreenContent(
            widgets = listOf(
                CustomWidget(),
                CustomWidget()
            ),
            editMode = false,
            onEditModeChange = {},
            mode = Mode.POMODORO,
            currentWidget = CustomWidget(),
            editingWidget = CustomWidget(),
            editWidget = {},
            onCancelEdit = {},
            onDoneEdit = {},
            onNextWidget = {},
            onUpdateColor = { _, _ -> },
            isLandscape = true,
            showButtons = true,
            onShowButtonsChange = {},
            onDeleteWidget = {}
        )
    }
}