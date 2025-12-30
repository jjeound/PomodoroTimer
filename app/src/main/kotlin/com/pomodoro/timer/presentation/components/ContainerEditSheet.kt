package com.pomodoro.timer.presentation.components

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pomodoro.timer.R
import com.pomodoro.timer.data.model.BgMode
import com.pomodoro.timer.ui.theme.CustomTheme
import com.pomodoro.timer.ui.theme.MyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContainerEditSheet(
    modifier: Modifier,
    onDismissRequest: () -> Unit,
    onColorClick: (Color) -> Unit,
    onBackgroundColorClick: (Color) -> Unit,
    onScreenColorClick: (Color) -> Unit,
    onAddImage: (String) -> Unit,
    onAddBtnClick: (index: Int) -> Unit,
    onColorPickerClick: (index: Int) -> Unit,
    onHandColorClick: (Color) -> Unit,
    onEdgeColorClick: (Color) -> Unit,
    onDeleteColor: (Color) -> Unit,
    colors: List<Color>,
    currentColor: Color,
    isLandScape: Boolean,
    onClickBgMode: (BgMode) -> Unit,
    bgMode: BgMode,
    onPatternClick: (Int) -> Unit,
    currentPattern: Int,
    patterns: List<Int>,
    index: Int,
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val pagerState = rememberPagerState(
        initialPage = index,
        pageCount = { 6 }
    )
    val albumLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            val contentResolver = context.contentResolver
            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            try {
                contentResolver.takePersistableUriPermission(it, takeFlags)
                onAddImage(it.toString())
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    if(isLandScape){
        Column(
            modifier = modifier.fillMaxHeight().padding(30.dp).background(
                color = CustomTheme.colors.surface
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.End),
                onClick = onDismissRequest
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.close),
                    contentDescription = null,
                    tint = CustomTheme.colors.icon
                )
            }
            ContainerEditSheetContent(
                modifier = Modifier.weight(1f),
                pagerState = pagerState,
                colors = colors,
                currentColor = currentColor,
                onColorClick = onColorClick,
                onBackgroundColorClick = onBackgroundColorClick,
                onScreenColorClick = onScreenColorClick,
                onHandColorClick = onHandColorClick,
                onEdgeColorClick = onEdgeColorClick,
                onAddBtnClick = onAddBtnClick,
                onColorPickerClick = onColorPickerClick,
                onImagePickerClick = {
                    albumLauncher.launch(arrayOf("image/*"))
                },
                onDeleteColor = onDeleteColor,
                bgMode = bgMode,
                onClickBgMode = onClickBgMode,
                onPatternClick = onPatternClick,
                currentPattern = currentPattern,
                patterns = patterns,
            )
        }
    } else {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = CustomTheme.colors.surface,
            scrimColor = Color.Transparent,
        ) {
            ContainerEditSheetContent(
                pagerState = pagerState,
                colors = colors,
                currentColor = currentColor,
                onColorClick = onColorClick,
                onBackgroundColorClick = onBackgroundColorClick,
                onScreenColorClick = onScreenColorClick,
                onHandColorClick = onHandColorClick,
                onEdgeColorClick = onEdgeColorClick,
                onAddBtnClick = onAddBtnClick,
                onColorPickerClick = onColorPickerClick,
                onImagePickerClick = {
                    albumLauncher.launch(arrayOf("image/*"))
                },
                onDeleteColor = onDeleteColor,
                bgMode = bgMode,
                onClickBgMode = onClickBgMode,
                onPatternClick = onPatternClick,
                currentPattern = currentPattern,
                patterns = patterns
            )
        }
    }
}

@Composable
fun ContainerEditSheetContent(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    colors: List<Color>,
    currentColor: Color,
    onColorClick: (Color) -> Unit,
    onBackgroundColorClick: (Color) -> Unit,
    onScreenColorClick: (Color) -> Unit,
    onHandColorClick: (Color) -> Unit,
    onEdgeColorClick: (Color) -> Unit,
    onAddBtnClick: (index: Int) -> Unit,
    onColorPickerClick: (index: Int) -> Unit,
    onImagePickerClick: () -> Unit,
    onDeleteColor: (Color) -> Unit,
    bgMode: BgMode,
    onClickBgMode: (BgMode) -> Unit,
    onPatternClick: (Int) -> Unit,
    currentPattern: Int,
    patterns: List<Int>,
){
    Column(
        modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(
            modifier = modifier,
            state = pagerState
        ){ page ->
            when(page){
                0 -> {
                    ColorEditForm(
                        title = R.string.color,
                        colors = colors,
                        onColorClick = {
                            onColorClick(it)
                        },
                        onColorPickerClick = {
                            onColorPickerClick(0)
                        },
                        onAddBtnClick = {
                            onAddBtnClick(0)
                        },
                        currentColor = currentColor,
                        onDeleteColor = onDeleteColor,
                        second = {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = CustomTheme.colors.divider
                            )
                            PatternForm(
                                onPatternClick = onPatternClick,
                                currentPattern = currentPattern,
                                patterns = patterns
                            )
                        }
                    )
                }
                1 -> {
                    ColorEditForm(
                        title = R.string.background_color,
                        colors = colors,
                        onColorClick = {
                            onBackgroundColorClick(it)
                        },
                        onColorPickerClick = {
                            onColorPickerClick(1)
                        },
                        onAddBtnClick = {
                            onAddBtnClick(1)
                        },
                        currentColor = currentColor,
                        onDeleteColor = onDeleteColor,
                        content = {
                            ImagePicker(
                                onClick = onImagePickerClick
                            )
                        }
                    )
                }
                2 -> {
                    ColorEditForm(
                        title = R.string.screen_color,
                        colors = colors,
                        onColorClick = {
                            onScreenColorClick(it)
                        },
                        onColorPickerClick = {
                            onColorPickerClick(2)
                        },
                        onAddBtnClick = {
                            onAddBtnClick(2)
                        },
                        currentColor = currentColor,
                        onDeleteColor = onDeleteColor,
                    )
                }
                3 -> {
                    ColorEditForm(
                        title = R.string.hand_color,
                        colors = colors,
                        onColorClick = {
                            onHandColorClick(it)
                        },
                        onColorPickerClick = {
                            onColorPickerClick(3)
                        },
                        onAddBtnClick = {
                            onAddBtnClick(3)
                        },
                        currentColor = currentColor,
                        onDeleteColor = onDeleteColor,
                    )
                }
                4 -> {
                    ColorEditForm(
                        title = R.string.edge_color,
                        colors = colors,
                        onColorClick = {
                            onEdgeColorClick(it)
                        },
                        onColorPickerClick = {
                            onColorPickerClick(4)
                        },
                        onAddBtnClick = {
                            onAddBtnClick(4)
                        },
                        currentColor = currentColor,
                        onDeleteColor = onDeleteColor,
                    )
                }
                5 -> {
                    BgEditBox(
                        bgMode = bgMode,
                        onClickBgMode = onClickBgMode
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 30.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(6) { index ->
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

@Composable
fun ColorEditForm(
    title: Int,
    colors: List<Color>,
    onColorClick: (Color) -> Unit,
    onColorPickerClick: () -> Unit,
    onAddBtnClick: () -> Unit,
    currentColor: Color,
    onDeleteColor: (Color) -> Unit,
    content: (@Composable () -> Unit)? = null,
    second: (@Composable () -> Unit)? = null,
){
    val state = rememberLazyListState()
    val context = LocalContext.current
    var editMode by remember { mutableStateOf(false) }
    val text = stringResource(R.string.color_alert)
    Column(
        modifier = Modifier.fillMaxWidth().combinedClickable(
            onClick = {},
            onDoubleClick = {
                editMode = !editMode
            },
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
        ){
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(title),
                style = CustomTheme.typography.editTitleSmall,
                color = CustomTheme.colors.text,
            )
            Box(
                modifier = Modifier.size(30.dp).align(
                    Alignment.CenterEnd
                ).clickable(
                    onClick = {
                        if (colors.take(4).all { it != currentColor}){
                            onColorPickerClick()
                        } else {
                            Toast.makeText(
                                context,
                                text,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            ){
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.eye_dropper),
                    contentDescription = null,
                    tint = CustomTheme.colors.icon
                )
            }
        }
        LazyRow(
            state = state,
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 4.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(
                colors.size,
            ) { index ->
                val color = colors[index]
                if(index < 4){
                    ColorBox(
                        containerColor = color,
                        onClick = { onColorClick(color) },
                        clicked = color == currentColor,
                        editMode = false,
                        onDeleteColor = onDeleteColor,
                        isDefault = true
                    )
                } else {
                    ColorBox(
                        containerColor = color,
                        onClick = { onColorClick(color) },
                        clicked = color == currentColor,
                        editMode = editMode,
                        onDeleteColor = onDeleteColor
                    )
                }
            }
            item {
                AddColorBox(onClick = onAddBtnClick)
            }
            item {
                content?.invoke()
            }
        }
        second?.invoke()
    }
}

@Composable
fun ColorBox(
    containerColor: Color,
    onClick: () -> Unit,
    onDeleteColor: (Color) -> Unit,
    clicked: Boolean,
    editMode: Boolean,
    isDefault: Boolean = false,
){
    Box(
        modifier = Modifier
            .size(30.dp)
            .background(containerColor, shape = CircleShape)
            .clickable(
                onClick = onClick
            ).then(
                if(isDefault){
                    Modifier.border(
                        width = 2.dp, color = CustomTheme.colors.buttonBorder,
                        shape = CircleShape
                    )
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ){
        if(clicked){
            Box(
                modifier = Modifier.size(22.dp).border(
                    width = 2.dp,
                    color = CustomTheme.colors.selectorContainerSelected,
                    shape = CircleShape
                ),
            )
        }
        if(editMode){
            Box(
                modifier = Modifier.size(18.dp).background(
                    color = CustomTheme.colors.surface,
                    shape = CircleShape
                ).border(
                    width = 1.dp,
                    color = CustomTheme.colors.buttonBorder,
                    shape = CircleShape
                ).align(Alignment.TopEnd).clickable(
                    onClick = {
                        onDeleteColor(containerColor)
                    }
                ),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    modifier = Modifier.size(15.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.close),
                    contentDescription = null,
                    tint = CustomTheme.colors.icon,
                )
            }
        }
    }
}

@Composable
fun ImagePicker(
    onClick: () -> Unit
){
    Box(
        modifier = Modifier.size(30.dp).clickable(
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.file_upload),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}

@Composable
fun AddColorBox(
    onClick: () -> Unit
){
    Box(
        modifier = Modifier.size(30.dp)
            .clickable(
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.add_button),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}


@Composable
fun PatternForm(
    onPatternClick: (Int) -> Unit,
    currentPattern: Int,
    patterns: List<Int>
){
    val state = rememberLazyListState()
    Log.d("pattern", currentPattern.toString())
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.pattern),
            style = CustomTheme.typography.editTitleSmall,
            color = CustomTheme.colors.text,
        )
        LazyRow(
            state = state,
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 4.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(
                patterns.size,
            ) { index ->
                val pattern = patterns[index]
                PatternBox(
                    pattern = pattern,
                    onClick = { onPatternClick(index) },
                    clicked = currentPattern == index
                )
            }
        }
    }
}

@Composable
fun PatternBox(
    pattern: Int,
    onClick: () -> Unit,
    clicked: Boolean,
){
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(
                color = CustomTheme.colors.indicatorBox,
                shape = CircleShape
            )
            .clickable(
                onClick = onClick
            ).then(
                if(clicked){
                    Modifier.border(
                        width = 2.dp, color = CustomTheme.colors.buttonBorder,
                        shape = CircleShape
                    )
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center,
    ){
        Icon(
            imageVector = ImageVector.vectorResource(pattern),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}


@Preview
@Composable
fun ContainerEditSheetPreview() {
    MyTheme {
        ContainerEditSheet(
            modifier = Modifier,
            onDismissRequest = {},
            onColorClick = {},
            onBackgroundColorClick = {},
            onScreenColorClick = {},
            onAddBtnClick = {},
            onAddImage = {},
            onColorPickerClick = {},
            onHandColorClick = {},
            onEdgeColorClick = {},
            colors = listOf(
                Color.Red,
                Color.Green,
                Color.Blue,
                Color.Yellow,
                Color.Cyan,
                Color.Magenta,
                Color.Black,
            ),
            currentColor = Color.Red,
            isLandScape = false,
            onDeleteColor = {},
            bgMode = BgMode.IDLE,
            onClickBgMode = {},
            onPatternClick = {},
            currentPattern = 0,
            patterns = emptyList(),
            index = 0
        )
    }
}