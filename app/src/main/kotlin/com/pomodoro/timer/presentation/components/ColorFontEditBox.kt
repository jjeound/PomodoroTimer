package com.pomodoro.timer.presentation.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pomodoro.timer.R
import com.pomodoro.timer.ui.theme.CustomTheme
import com.pomodoro.timer.ui.theme.MyTheme
import com.pomodoro.timer.ui.theme.customTypography

@Composable
fun ColorFontEditBox(
    onColorClick: (Color) -> Unit,
    onColorPickerClick: (index: Int) -> Unit,
    onAddBtnClick: (index: Int) -> Unit,
    onFontClick: (TextStyle) -> Unit,
    currentColor: Color,
    onDeleteColor: (Color) -> Unit,
    colors: List<Color>,
    fontSize: Float,
    onFontSizeChange: (Float) -> Unit
) {
    Log.d("fontsize", fontSize.toString())
    val textStyles = listOf(
        customTypography.textPreview1,
        customTypography.textPreview2,
        customTypography.textPreview3,
        customTypography.textPreview4,
    )
    Column(
        modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ColorEditForm(
            title = R.string.color,
            colors = colors,
            onColorClick = onColorClick,
            onColorPickerClick = {
                onColorPickerClick(4)
            },
            onAddBtnClick = {
                onAddBtnClick(4)
            },
            currentColor = currentColor,
            onDeleteColor = onDeleteColor
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = CustomTheme.colors.divider
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            Text(
                text = stringResource(R.string.font),
                style = CustomTheme.typography.editTitleSmall,
                color = CustomTheme.colors.text,
            )
            Row(
                modifier = Modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.spacedBy(33.dp)
            ) {
                repeat(4){ index ->
                    val textStyle = textStyles[index]
                    Text(
                        modifier = Modifier.clickable(
                            onClick = { onFontClick(textStyle) }
                        ),
                        text = "10",
                        style = textStyle,
                        color = CustomTheme.colors.text
                    )
                }
            }
            Slider(
                modifier = Modifier.padding(horizontal = 20.dp),
                value = fontSize,
                onValueChange = {
                    onFontSizeChange(it)
                },
                colors = SliderDefaults.colors(
                    thumbColor = CustomTheme.colors.icon,
                    activeTrackColor = CustomTheme.colors.icon,
                    inactiveTrackColor = CustomTheme.colors.icon,
                ),
                valueRange = 20f..35f
            )
        }
    }
}

@Preview
@Composable
fun ColorFontEditBoxPreview(){
    MyTheme {
        ColorFontEditBox(
            onColorClick = {},
            onColorPickerClick = {},
            onAddBtnClick = {},
            onFontClick = {},
            currentColor = Color.White,
            onDeleteColor = {},
            colors = listOf(
                Color.Red,
                Color.Black,
                ),
            fontSize = 0f,
            onFontSizeChange = {}
        )
    }
}