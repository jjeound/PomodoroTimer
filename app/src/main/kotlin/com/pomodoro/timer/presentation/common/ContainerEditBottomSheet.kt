package com.pomodoro.timer.presentation.common

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
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val colors = listOf(
        Pair(Color(0xFFF94C5E), Color(0xFFE83B4D)),
        Pair(Color(0xFF9AC1F0), Color(0xFF89B0E0)),
        Pair(Color(0xFF72FA93), Color(0xFF61E982)),
        Pair(Color(0xFFF6C445), Color(0xFFE5B334)),
    )
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
            modifier = Modifier.wrapContentHeight().padding(vertical = 30.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp),
        ) {
            ColorEditForm(
                title = R.string.color,
                colors = colors,
                onColorClick = onColorClick,
                onColorPickerClick = {
                    onColorPickerClick(0)
                }
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = CustomTheme.colors.divider
            )
            ColorEditForm(
                title = R.string.background_color,
                colors = colors,
                onColorClick = onBackgroundColorClick,
                onColorPickerClick = {
                    onColorPickerClick(1)
                }
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
            HorizontalDivider(
                thickness = 1.dp,
                color = CustomTheme.colors.divider
            )
            ColorEditForm(
                title = R.string.hand_color,
                colors = colors,
                onColorClick = onHandColorClick,
                onColorPickerClick = {
                    onColorPickerClick(2)
                }
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = CustomTheme.colors.divider
            )
            ColorEditForm(
                title = R.string.edge_color,
                colors = colors,
                onColorClick = onEdgeColorClick,
                onColorPickerClick = {
                    onColorPickerClick(3)
                }
            )
        }
    }
}

@Composable
fun ColorEditForm(
    title: Int,
    colors: List<Pair<Color, Color>>,
    onColorClick: (Color) -> Unit,
    onColorPickerClick: () -> Unit,
    content: (@Composable () -> Unit)? = null,
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(title),
            style = CustomTheme.typography.editTitleSmall,
            color = CustomTheme.colors.text,
        )
        Row(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            for (colorPair in colors) {
                ColorBox(
                    containerColor = colorPair.first,
                    borderColor = colorPair.second,
                    onClick = { onColorClick(colorPair.first) }
                )
            }
            Box(
                modifier = Modifier.size(30.dp).clickable(
                    onClick = {
                        onColorPickerClick()
                    }
                )
            ){
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.rainbow),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
            content?.invoke()
        }
    }
}

@Composable
fun ColorBox(
    containerColor: Color,
    borderColor: Color,
    onClick: () -> Unit
){
    Box(
        modifier = Modifier.size(30.dp)
            .background(
                color = containerColor,
                shape = CircleShape
            ).border(
                width = 1.dp,
                color = borderColor,
                shape = CircleShape
            ).clickable(
                onClick = onClick
            ),
    )
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
            imageVector = ImageVector.vectorResource(R.drawable.plus),
            contentDescription = null,
            tint = CustomTheme.colors.icon
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
        )
    }
}