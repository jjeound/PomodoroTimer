package com.pomodoro.timer.presentation.common

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.pomodoro.timer.R
import com.pomodoro.timer.ui.theme.CustomTheme
import com.pomodoro.timer.ui.theme.customTypography

@Composable
fun ColorFontEditBox(
    onColorClick: (Color) -> Unit,
    onColorPickerClick: (index: Int) -> Unit,
    onFontClick: (TextStyle) -> Unit,
) {
    val textStyles = listOf(
        customTypography.textPreview1,
        customTypography.textPreview2,
        customTypography.textPreview3,
        customTypography.textPreview4,
    )
    val colors = listOf(
        Pair(Color(0xFFF94C5E), Color(0xFFE83B4D)),
        Pair(Color(0xFF9AC1F0), Color(0xFF89B0E0)),
        Pair(Color(0xFF72FA93), Color(0xFF61E982)),
        Pair(Color(0xFFF6C445), Color(0xFFE5B334)),
    )
    Column(
        modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.color),
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
                            onColorPickerClick(2)
                        }
                    )
                ){
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.rainbow),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            }
        }
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
        }
    }
}