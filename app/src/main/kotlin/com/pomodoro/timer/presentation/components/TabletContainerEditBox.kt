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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.pomodoro.timer.R
import com.pomodoro.timer.ui.theme.CustomTheme
import com.pomodoro.timer.ui.theme.MyTheme

@Composable
fun TabletContainerEditBox(
    modifier: Modifier,
    onClose: () -> Unit,
    onColorClick: (Color) -> Unit,
    onBackgroundColorClick: (Color) -> Unit,
    onAddImage: (Uri) -> Unit,
    onColorPickerClick: (index: Int) -> Unit,
    onHandColorClick: (Color) -> Unit,
    onEdgeColorClick: (Color) -> Unit,
    colors: List<Color>,
) {
    val context = LocalContext.current
    var currentColor by remember { mutableStateOf(colors.first()) }
    val colors = listOf(Color(0xFFF94C5E), Color(0xFF9AC1F0), Color(0xFF72FA93), Color(0xFFF6C445))
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
    Column(
        modifier = modifier.fillMaxHeight().padding(30.dp).background(
            color = CustomTheme.colors.surface
        ),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            horizontalArrangement = Arrangement.End,
        ) {
            Icon(
                modifier = Modifier.clickable(
                    onClick = onClose
                ),
                imageVector = ImageVector.vectorResource(R.drawable.close),
                contentDescription = null,
                tint = CustomTheme.colors.icon
            )
        }
        ColorEditForm(
            title = R.string.color,
            colors = colors,
            onColorClick = onColorClick,
            onColorPickerClick = {
                onColorPickerClick(0)
            },
            currentColor = currentColor
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
            },
            currentColor = currentColor
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
            },
            currentColor = currentColor
        )
    }
}

@Preview
@Composable
fun TabletContainerEditBoxPreview() {
    MyTheme {
        TabletContainerEditBox(
            modifier = Modifier,
            onClose = {},
            onColorClick = {},
            onBackgroundColorClick = {},
            onAddImage = {},
            onColorPickerClick = {},
            onHandColorClick = {},
            onEdgeColorClick = {},
            colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)
        )
    }
}
