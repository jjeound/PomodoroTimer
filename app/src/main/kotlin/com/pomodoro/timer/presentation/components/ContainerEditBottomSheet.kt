package com.pomodoro.timer.presentation.components

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
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
import androidx.core.content.ContextCompat
import com.pomodoro.timer.R
import com.pomodoro.timer.ui.theme.CustomTheme
import com.pomodoro.timer.ui.theme.MyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContainerEditBottomSheet(
    onDismissRequest: () -> Unit,
    onColorClick: (Color) -> Unit,
    onBackgroundColorClick: (Color) -> Unit,
    onAddImage: (Uri) -> Unit,
    onColorPickerClick: (index: Int) -> Unit,
    onHandColorClick: (Color) -> Unit,
    onEdgeColorClick: (Color) -> Unit,
    colors: List<Color>,
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    var currentColor by remember { mutableStateOf(colors.first()) }
    val pagerState = rememberPagerState(pageCount = {4})
    val albumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    onAddImage(it)
                }
            }
        }
    val imageAlbumIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        addCategory(Intent.CATEGORY_OPENABLE)
    }

    val galleryPermissions = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> arrayOf(
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
            Manifest.permission.READ_MEDIA_IMAGES,
        )
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES
        )
        else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                if (permissions.all { it.value }) {
                    albumLauncher.launch(imageAlbumIntent)
                } else {
                    Toast.makeText(context, "갤러리 권한을 허용해야 합니다", Toast.LENGTH_SHORT).show()
                }
            }
        )
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = CustomTheme.colors.surface,
        scrimColor = Color.Transparent,
    ) {
        Column(
            modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HorizontalPager(
                state = pagerState
            ){ page ->
                when(page){
                    0 -> {
                        ColorEditForm(
                            title = R.string.color,
                            colors = colors,
                            onColorClick = {
                                onColorClick(it)
                                currentColor = it
                            },
                            onColorPickerClick = {
                                onColorPickerClick(0)
                            },
                            currentColor = currentColor
                        )
                    }
                    1 -> {
                        ColorEditForm(
                            title = R.string.background_color,
                            colors = colors,
                            onColorClick = {
                                onBackgroundColorClick(it)
                                currentColor = it
                            },
                            onColorPickerClick = {
                                onColorPickerClick(1)
                            },
                            currentColor = currentColor
                        ){
                            ImagePicker(
                                onClick = {
                                    if (galleryPermissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }) {
                                        albumLauncher.launch(imageAlbumIntent)
                                    } else {
                                        requestPermissionLauncher.launch(galleryPermissions)
                                    }
                                }
                            )
                        }
                    }
                    2 -> {
                        ColorEditForm(
                            title = R.string.hand_color,
                            colors = colors,
                            onColorClick = {
                                onHandColorClick(it)
                                currentColor = it
                            },
                            onColorPickerClick = {
                                onColorPickerClick(2)
                            },
                            currentColor = currentColor
                        )
                    }
                    3 -> {
                        ColorEditForm(
                            title = R.string.edge_color,
                            colors = colors,
                            onColorClick = {
                                onEdgeColorClick(it)
                                currentColor = it
                            },
                            onColorPickerClick = {
                                onColorPickerClick(3)
                            },
                            currentColor = currentColor
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

@Composable
fun ColorEditForm(
    title: Int,
    colors: List<Color>,
    onColorClick: (Color) -> Unit,
    onColorPickerClick: () -> Unit,
    currentColor: Color,
    content: (@Composable () -> Unit)? = null,
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp)
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
                        onColorPickerClick()
                    }
                )
            ){
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.eye_dropper),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }
        Row(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            for (color in colors) {
                ColorBox(
                    containerColor = color,
                    onClick = { onColorClick(color) },
                    clicked = color == currentColor
                )
            }
            AddColorBox(
                onClick = {}
            )
            content?.invoke()
        }
    }
}

@Composable
fun ColorBox(
    containerColor: Color,
    onClick: () -> Unit,
    clicked: Boolean
){
    Box(
        modifier = Modifier
            .size(30.dp)
            .background(containerColor, shape = CircleShape)
            .clickable(
                onClick = onClick
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
    }
}

@Composable
fun ImagePicker(
    onClick: () -> Unit
){
    Box(
        modifier = Modifier.size(30.dp)
            .border(
                width = 1.dp,
                color = CustomTheme.colors.buttonBorder,
                shape = CircleShape
            ).clickable(
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
fun AddColorBox(
    onClick: () -> Unit
){
    Box(
        modifier = Modifier.size(30.dp)
            .border(
                width = 1.dp,
                color = CustomTheme.colors.buttonBorder,
                shape = CircleShape
            ).clickable(
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






@Preview
@Composable
fun ContainerEditBottomSheetPreview() {
    MyTheme {
        ContainerEditBottomSheet(
            onDismissRequest = {},
            onColorClick = {},
            onBackgroundColorClick = {},
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
            )
        )
    }
}