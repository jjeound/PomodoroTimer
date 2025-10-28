package com.pomodoro.timer.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pomodoro.timer.R
import com.pomodoro.timer.ui.theme.CustomTheme

@Composable
fun EditButtons(
    modifier: Modifier,
    onEditModeChange: (Boolean) -> Unit,
    onDoneEdit: () -> Unit,
    onCancelEdit: () -> Unit,
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        OutlinedButton(
            onClick = {
                onEditModeChange(false)
                onCancelEdit()
            },
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(50)),
            border = BorderStroke(1.dp, CustomTheme.colors.buttonBorder)
        ) {
            Text(
                text = stringResource(id = R.string.cancel),
                style = CustomTheme.typography.buttonEditSmall,
                color = CustomTheme.colors.buttonText
            )
        }
        OutlinedButton(
            onClick = {
                onEditModeChange(false)
                onDoneEdit()
            },
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(50)),
            border = BorderStroke(1.dp, CustomTheme.colors.buttonBorder)
        ) {
            Text(
                text = stringResource(id = R.string.done),
                style = CustomTheme.typography.buttonEditSmall,
                color = CustomTheme.colors.buttonText
            )
        }
    }
}