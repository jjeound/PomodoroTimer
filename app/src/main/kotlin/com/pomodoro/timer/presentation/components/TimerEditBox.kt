package com.pomodoro.timer.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pomodoro.timer.R
import com.pomodoro.timer.ui.theme.CustomTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.pomodoro.timer.ui.theme.MyTheme

@SuppressLint("FrequentlyChangingValue")
@Composable
fun TimerEditBox(
    gap: Int,
    onClickGap: (Int) -> Unit,
    breakTime: Int,
    onChangeBreakTime: (Int) -> Unit,
    repeat: Int,
    onChangeRepeat: (Int) -> Unit,
    hour: Int,
    minute: Int,
    second: Int,
    onChangeHour: (Int) -> Unit,
    onChangeMinute: (Int) -> Unit,
    onChangeSecond: (Int) -> Unit,
    mode: Int,
) {
    Column(
        modifier = Modifier.wrapContentHeight().fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        when(mode){
            0 -> GapBox(
                gap = gap,
                onClickGap = onClickGap,
            )
            1 -> TimerBox(
                hour = hour,
                minute = minute,
                second = second,
                onChangeHour = onChangeHour,
                onChangeMinute = onChangeMinute,
                onChangeSecond = onChangeSecond,
            )
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
                val listState = rememberLazyListState(initialFirstVisibleItemIndex = breakTime)

                LaunchedEffect(listState.firstVisibleItemIndex) {
                    val newValue = listState.firstVisibleItemIndex
                    onChangeBreakTime(newValue)
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier.height(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    items(31) { index ->
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = index.toString(),
                            style = CustomTheme.typography.textField,
                            color = CustomTheme.colors.textFieldText,
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
                val listState = rememberLazyListState(initialFirstVisibleItemIndex = repeat)

                LaunchedEffect(listState.firstVisibleItemIndex) {
                    val newValue = listState.firstVisibleItemIndex
                    onChangeRepeat(newValue)
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier.height(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    items(24) { index ->
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = index.toString(),
                            style = CustomTheme.typography.textField,
                            color = CustomTheme.colors.textFieldText,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GapBox(
    gap: Int,
    onClickGap: (Int) -> Unit,
){
    Column(
        modifier = Modifier.fillMaxWidth(0.8f),
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
                    ).weight(1f).padding(vertical = 4.dp).clickable(
                        onClick = { onClickGap(i*5) },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ),
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
}

@Composable
fun TimerBox(
    hour: Int,
    minute: Int,
    second: Int,
    onChangeHour: (Int) -> Unit,
    onChangeMinute: (Int) -> Unit,
    onChangeSecond: (Int) -> Unit,
){
    val time = listOf(hour, minute, second)
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
        Row(
            modifier = Modifier.fillMaxWidth().height(32.dp).background(
                color = CustomTheme.colors.selectorContainer,
                shape = RoundedCornerShape(12.dp)
            ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            time.forEachIndexed { i, t ->
                Row(
                    modifier = Modifier.weight(1f).padding(vertical = 4.dp).background(
                        color = CustomTheme.colors.textFieldContainer,
                        shape = RoundedCornerShape(12.dp)
                    ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    BasicTextField(
                        modifier = Modifier.width(16.dp),
                        value = t.toString(),
                        onValueChange = {
                            val intValue = it.toIntOrNull() ?: 0
                            when(i){
                                0 -> onChangeHour(intValue)
                                1 -> onChangeMinute(intValue)
                                2 -> onChangeSecond(intValue)
                            }
                        },
                        textStyle = CustomTheme.typography.textField,
                        singleLine = true,
                    )
                    Spacer(
                        modifier = Modifier.width(4.dp)
                    )
                    Text(
                        text = stringResource(
                            when(i){
                                0 -> R.string.hours
                                1 -> R.string.minutes
                                else -> R.string.seconds
                            }
                        ),
                        style = CustomTheme.typography.textField,
                        color = CustomTheme.colors.text,
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
}


@Preview
@Composable
fun TimerEditBoxPreview() {
    MyTheme {
        TimerEditBox(
            gap = 5,
            onClickGap = {},
            breakTime = 24,
            onChangeBreakTime = {},
            repeat = 4,
            onChangeRepeat = {},
            hour = 12,
            minute = 30,
            second = 45,
            onChangeHour = {},
            onChangeMinute = {},
            onChangeSecond = {},
            mode = 0,
        )
    }
}

@Preview
@Composable
fun GapBoxPreview() {
    MyTheme {
        GapBox(
            gap = 10,
            onClickGap = {},
        )
    }
}

@Preview
@Composable
fun TimerBoxPreview() {
    MyTheme {
        TimerBox(
            hour = 12,
            minute = 30,
            second = 45,
            onChangeHour = {},
            onChangeMinute = {},
            onChangeSecond = {},
        )
    }
}