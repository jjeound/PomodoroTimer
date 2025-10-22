package com.pomodoro.timer.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pomodoro.timer.R
import com.pomodoro.timer.ui.theme.CustomTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.pomodoro.timer.ui.theme.MyTheme

@Composable
fun TimerEditBox(
    gap: Int,
    onClickGap: (Int) -> Unit,
    breakTime: Int,
    onEnterBreakTime: (Int) -> Unit,
    repeat: Int,
    onEnterRepeat: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.gap),
                style = CustomTheme.typography.editTitleSmall,
                color = CustomTheme.colors.text,
            )
            Row(
                modifier = Modifier.fillMaxWidth().height(32.dp).background(
                    color = CustomTheme.colors.selectorContainer,
                    shape = RoundedCornerShape(12.dp)
                ),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 1..3){
                    Box(
                        modifier = Modifier.background(
                            color = if(gap == i*5) CustomTheme.colors.selectorContainerSelected else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        ).weight(1f).padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = (i*5).toString(),
                            style = if(gap == i*5) CustomTheme.typography.selectorSelected else CustomTheme.typography.selectorUnSelected,
                            color = CustomTheme.colors.selectorTextSelected
                        )
                    }
                    if(i != 3){
                        VerticalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = CustomTheme.colors.selectorDivider,
                            thickness = 1.dp,
                        )
                    }
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
                text = stringResource(R.string.break_time),
                style = CustomTheme.typography.editTitleSmall,
                color = CustomTheme.colors.text,
            )
            Box(
                modifier = Modifier.fillMaxWidth(0.8f).height(32.dp).background(
                    color = CustomTheme.colors.textFieldContainer,
                    shape = RoundedCornerShape(12.dp)
                ),
                contentAlignment = Alignment.Center
            ){
                BasicTextField(
                    modifier = Modifier.width(16.dp),
                    value = breakTime.toString(),
                    onValueChange = {
                        val intValue = it.toIntOrNull() ?: 0
                        onEnterBreakTime(intValue)
                    },
                    textStyle = CustomTheme.typography.textField,
                    singleLine = true,
                )
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
                text = stringResource(R.string.repeat),
                style = CustomTheme.typography.editTitleSmall,
                color = CustomTheme.colors.text,
            )
            Box(
                modifier = Modifier.fillMaxWidth(0.8f).height(32.dp).background(
                    color = CustomTheme.colors.textFieldContainer,
                    shape = RoundedCornerShape(12.dp)
                ),
                contentAlignment = Alignment.Center
            ){
                BasicTextField(
                    modifier = Modifier.width(16.dp),
                    value = repeat.toString(),
                    onValueChange = {
                        val intValue = it.toIntOrNull() ?: 0
                        onEnterRepeat(intValue)
                    },
                    textStyle = CustomTheme.typography.textField,
                    singleLine = true,
                )
            }
        }
    }
}

@Preview
@Composable
fun TimerEditBoxPreview() {
    MyTheme {
        TimerEditBox(
            gap = 5,
            onClickGap = {},
            breakTime = 24,
            onEnterBreakTime = {},
            repeat = 4,
            onEnterRepeat = {},
        )
    }
}