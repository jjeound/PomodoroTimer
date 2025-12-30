package com.pomodoro.timer.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.pomodoro.timer.R
import com.pomodoro.timer.data.model.CustomWidget
import com.pomodoro.timer.ui.theme.CustomTheme
import com.pomodoro.timer.ui.theme.MyTheme

@Composable
fun ColorPicker(
    modifier: Modifier,
    onClose: () -> Unit,
    onConfirm: (Color) -> Unit,
    colorPickerOption: Int,
    editWidget: (CustomWidget) -> Unit,
    editingWidget: CustomWidget,
){
    val controller = rememberColorPickerController()
    var focusedColor by remember { mutableStateOf(Color.White) }
    Box(
        modifier = modifier.background(
            color = Color.White,
            shape = RoundedCornerShape(12.dp)
        ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.align(Alignment.TopEnd).padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                modifier = Modifier.clickable(
                    onClick = onClose
                ),
                imageVector = ImageVector.vectorResource(R.drawable.close),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Icon(
                modifier = Modifier.clickable(
                    onClick = { onConfirm(focusedColor) }
                ),
                imageVector = ImageVector.vectorResource(R.drawable.check),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
        Column(
            modifier = Modifier
                .wrapContentSize().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HsvColorPicker(
                modifier = Modifier
                    .size(200.dp)
                    .padding(10.dp),
                controller = controller,
                onColorChanged = { colorEnvelope: ColorEnvelope ->
                    focusedColor = Color(colorEnvelope.hexCode.toLong(16))
                    when (colorPickerOption) {
                        0 -> {
                            editWidget(
                                editingWidget.copy(
                                    fgColor = focusedColor
                                )
                            )
                        }
                        1 -> {
                            editWidget(
                                editingWidget.copy(
                                    bgColor = focusedColor,
                                    backgroundImage = null
                                )
                            )
                        }
                        2 -> {
                            editWidget(
                                editingWidget.copy(
                                    screenColor = focusedColor
                                )
                            )
                        }
                        3 -> {
                            editWidget(
                                editingWidget.copy(
                                    handColor = focusedColor
                                )
                            )
                        }
                        4 -> {
                            editWidget(
                                editingWidget.copy(
                                    edgeColor = focusedColor
                                )
                            )
                        }
                        5 -> {
                            editWidget(
                                editingWidget.copy(
                                    fontColor = focusedColor
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


@Preview
@Composable
fun ColorPickerPreview() {
    MyTheme {
        ColorPicker(
            modifier = Modifier.fillMaxWidth(),
            onClose = {},
            onConfirm = {},
            colorPickerOption = 0,
            editWidget = {},
            editingWidget = CustomWidget(),
        )
    }
}