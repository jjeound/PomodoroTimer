package com.pomodoro.timer.presentation

import android.content.res.Configuration
import android.widget.Toast
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.pomodoro.timer.data.model.CustomWidget
import com.pomodoro.timer.R
import com.pomodoro.timer.data.model.Mode
import com.pomodoro.timer.presentation.components.ContainerEditBottomSheet
import com.pomodoro.timer.presentation.components.EditButtons
import com.pomodoro.timer.presentation.components.TabletContainerEditBox
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
    val colors by viewModel.colors.collectAsStateWithLifecycle()
    val mode = viewModel.mode
    val config = LocalConfiguration.current
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isTablet = windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
    val editable = isTablet || config.orientation == Configuration.ORIENTATION_PORTRAIT //탭은 다 가능, 폰은 세로모드에서만 가능
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
                editable = editable,
                currentWidget = currentWidget,
                editingWidget = editingWidget,
                editWidget = viewModel::editWidget,
                onCancelEdit = viewModel::onCancelEdit,
                onDoneEdit = viewModel::onDoneEdit,
                onAddNewWidget = viewModel::onAddNewWidget,
                onNextWidget = viewModel::onNextWidget,
                onAddNewColor = viewModel::saveColor,
                onDeleteColor = viewModel::deleteColor,
                colors = colors,
                isTablet = isTablet,
            )
        } else {
            // 로딩중 UI
        }
    }
}

@Composable
fun MainScreenContent(
    widgets: List<CustomWidget>,
    editMode: Boolean,
    onEditModeChange: (Boolean) -> Unit,
    mode: Mode,
    editable: Boolean,
    currentWidget: CustomWidget,
    editingWidget: CustomWidget,
    editWidget: (CustomWidget) -> Unit,
    onCancelEdit: () -> Unit,
    onDoneEdit: () -> Unit,
    onAddNewWidget: () -> Unit = {},
    onNextWidget: (Int) -> Unit,
    onAddNewColor: (Color) -> Unit = {},
    onDeleteColor: (Color) -> Unit = {},
    colors: List<Color> = emptyList(),
    isTablet: Boolean,
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
        if (editMode && !showTextEditBottomSheet && !showContainerEditBottomSheet && !showColorPicker) {
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
        if(isTablet){
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight().weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
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
                            ),
                        state = pagerState,
                        userScrollEnabled = editMode
                    ) { page ->
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
                            pagerState = pagerState,
                            onAddNewWidget = onAddNewWidget,
                            widgetsSize = widgets.size
                        )
                    }
                }
                if (showContainerEditBottomSheet || showTextEditBottomSheet || showColorPicker) {
                    VerticalDivider(
                        thickness = 1.dp,
                        color = CustomTheme.colors.divider,
                    )
                }
                if(showContainerEditBottomSheet){
                    TabletContainerEditBox(
                        modifier = Modifier.weight(1f),
                        onClose = {
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
                        },
                        onHandColorClick = {
                            editWidget(
                                editingWidget.copy(
                                    handColor = it
                                )
                            )
                        },
                        onEdgeColorClick = {
                            editWidget(
                                editingWidget.copy(
                                    edgeColor = it
                                )
                            )
                        },
                        colors = colors
                    )
                }
                if(showTextEditBottomSheet){
                    when(mode){
                        Mode.POMODORO -> {
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
                                colors = colors
                            )
                        }
                        else -> {}
                    }
                }
                if(showColorPicker){
                    Box(
                        modifier = Modifier.weight(1f).padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.align(Alignment.TopEnd).clickable(
                                onClick = {
                                    showColorPicker = false
                                    if( colorPickerOption == 4){
                                        showTextEditBottomSheet = true
                                    } else {
                                        showContainerEditBottomSheet = true
                                    }
                                }
                            ),
                            imageVector = ImageVector.vectorResource(R.drawable.close),
                            contentDescription = null,
                            tint = CustomTheme.colors.icon
                        )
                        Column(
                            modifier = Modifier
                                .wrapContentSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
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
                                                    handColor = Color(colorEnvelope.hexCode.toLong(16))
                                                )
                                            )
                                        }
                                        3 -> {
                                            editWidget(
                                                editingWidget.copy(
                                                    edgeColor = Color(colorEnvelope.hexCode.toLong(16))
                                                )
                                            )
                                        }
                                        4 -> {
                                            editWidget(
                                                editingWidget.copy(
                                                    fontColor = Color(colorEnvelope.hexCode.toLong(16))
                                                )
                                            )
                                        }
                                    }
                                }
                            )
                            AlphaSlider(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .padding(10.dp)
                                    .height(35.dp),
                                controller = controller,
                            )
                            BrightnessSlider(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .padding(10.dp)
                                    .height(35.dp),
                                controller = controller,
                            )
                        }
                    }
                }
            }
        } else {
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
                    pagerState = pagerState,
                    onAddNewWidget = onAddNewWidget,
                    widgetsSize = widgets.size
                )
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
                    },
                    onHandColorClick = {
                        editWidget(
                            editingWidget.copy(
                                handColor = it
                            )
                        )
                    },
                    onEdgeColorClick = {
                        editWidget(
                            editingWidget.copy(
                                edgeColor = it
                            )
                        )
                    },
                    colors = colors
                )
            }
            if(showTextEditBottomSheet){
                when(mode){
                    Mode.POMODORO -> {
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
                            }
                        )
                    }
                    else -> {}
                }
            }
            if(showColorPicker){
                Box(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(
                        bottom = 120.dp
                    )
                ) {
                    Icon(
                        modifier = Modifier.align(Alignment.TopEnd).clickable(
                            onClick = {
                                showColorPicker = false
                                if( colorPickerOption == 4){
                                    showTextEditBottomSheet = true
                                } else {
                                    showContainerEditBottomSheet = true
                                }
                            }
                        ),
                        imageVector = ImageVector.vectorResource(R.drawable.close),
                        contentDescription = null,
                        tint = CustomTheme.colors.icon
                    )
                    Column(
                        modifier = Modifier
                            .wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
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
                                                handColor = Color(colorEnvelope.hexCode.toLong(16))
                                            )
                                        )
                                    }
                                    3 -> {
                                        editWidget(
                                            editingWidget.copy(
                                                edgeColor = Color(colorEnvelope.hexCode.toLong(16))
                                            )
                                        )
                                    }
                                    4 -> {
                                        editWidget(
                                            editingWidget.copy(
                                                fontColor = Color(colorEnvelope.hexCode.toLong(16))
                                            )
                                        )
                                    }
                                }
                            }
                        )
                        AlphaSlider(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .padding(10.dp)
                                .height(35.dp),
                            controller = controller,
                        )
                        BrightnessSlider(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .padding(10.dp)
                                .height(35.dp),
                            controller = controller,
                        )
                    }
                }
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
            editable = true,
            currentWidget = CustomWidget(),
            editingWidget = CustomWidget(),
            editWidget = {},
            onCancelEdit = {},
            onDoneEdit = {},
            onNextWidget = {},
            isTablet = true
        )
    }
}