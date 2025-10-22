package com.pomodoro.timer.presentation.common

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pomodoro.timer.R
import com.pomodoro.timer.ui.theme.CustomTheme
import com.pomodoro.timer.ui.theme.MyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContainerEditBottomSheet(
    onDismissRequest: () -> Unit,
    onColorClick: (Color) -> Unit,
    onBackgroundColorClick: (Color) -> Unit,
    onPlusClick: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val colors = listOf(
        Pair(Color(0xFFF94C5E), Color(0xFFE83B4D)),
        Pair(Color(0xFF9AC1F0), Color(0xFF89B0E0)),
        Pair(Color(0xFF72FA93), Color(0xFF61E982)),
        Pair(Color(0xFFF6C445), Color(0xFFE5B334)),
    )
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        contentColor = CustomTheme.colors.surface
    ) {
        Column(
            modifier = Modifier.wrapContentHeight().padding(vertical = 30.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp),
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
                        modifier = Modifier.size(30.dp)
                    ){
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.rainbow),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                    Box(
                        modifier = Modifier.size(30.dp)
                            .border(
                                width = 1.dp,
                                color = CustomTheme.colors.buttonBorder,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.plus),
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
            ) {
                Text(
                    text = stringResource(R.string.background_color),
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
                            onClick = { onBackgroundColorClick(colorPair.first) }
                        )
                    }
                    Box(
                        modifier = Modifier.size(30.dp)
                    ){
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.rainbow),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                }
            }
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







@Preview
@Composable
fun ContainerEditBottomSheetPreview() {
    MyTheme {
        ContainerEditBottomSheet(
            onDismissRequest = {},
            onColorClick = {},
            onBackgroundColorClick = {},
            onPlusClick = {}
        )
    }
}