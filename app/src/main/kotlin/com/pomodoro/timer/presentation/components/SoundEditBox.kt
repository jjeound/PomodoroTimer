package com.pomodoro.timer.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pomodoro.timer.R
import com.pomodoro.timer.ui.theme.CustomTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.pomodoro.timer.data.model.SoundMode
import com.pomodoro.timer.ui.theme.MyTheme

@Composable
fun SoundEditBox(
    soundMode: SoundMode,
    onClickSoundMode: (SoundMode) -> Unit,
    startSound: Int,
    restartSound: Int,
    onChangeStartSound: (Int) -> Unit,
    onChangeBreakTimeSound: (Int) -> Unit,
) {
    var isExpandStartSoundDropdown by remember { mutableStateOf(false) }
    var isExpandBreakTimeSoundDropdown by remember { mutableStateOf(false) }
    val startSoundList = listOf(
        Pair("buzzer", R.raw.buzzer ),
        Pair("game over", R.raw.game_over ),
        Pair("sneeze", R.raw.sneeze ),
    )
    val restartSoundList = listOf(
        Pair("buzzer", R.raw.buzzer ),
        Pair("game over", R.raw.game_over ),
        Pair("sneeze", R.raw.sneeze ),
    )
    Column(
        modifier = Modifier.wrapContentHeight().fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.sound_setting),
                style = CustomTheme.typography.editTitleSmall,
                color = CustomTheme.colors.text,
            )
            Row(
                modifier = Modifier.fillMaxWidth(0.8f).height(32.dp).background(
                    color = CustomTheme.colors.selectorContainer,
                    shape = RoundedCornerShape(12.dp)
                ),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SoundMode.entries.forEachIndexed { i, mode ->
                    Box(
                        modifier = Modifier.background(
                            color = if(mode == soundMode) CustomTheme.colors.selectorContainerSelected else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        ).weight(1f).padding(vertical = 4.dp).clickable(
                            onClick = { onClickSoundMode(mode) },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ),
                        contentAlignment = Alignment.Center
                    ){
                        when(mode){
                            SoundMode.NO_SOUND -> Text(
                                text = stringResource(R.string.no_sound),
                                style = if(mode == soundMode) CustomTheme.typography.selectorSelected else CustomTheme.typography.selectorUnSelected,
                                color = CustomTheme.colors.selectorTextSelected
                            )
                            SoundMode.VIBRATE -> Text(
                                text = stringResource(R.string.vibrate),
                                style = if(mode == soundMode) CustomTheme.typography.selectorSelected else CustomTheme.typography.selectorUnSelected,
                                color = CustomTheme.colors.selectorTextSelected
                            )
                            SoundMode.SOUND -> Text(
                                text = stringResource(R.string.sound),
                                style = if(mode == soundMode) CustomTheme.typography.selectorSelected else CustomTheme.typography.selectorUnSelected,
                                color = CustomTheme.colors.selectorTextSelected
                            )
                        }
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
                text = stringResource(R.string.start_sound),
                style = CustomTheme.typography.editTitleSmall,
                color = CustomTheme.colors.text,
            )
            Box(
                modifier = Modifier.fillMaxWidth(0.8f).height(32.dp).background(
                    color = CustomTheme.colors.textFieldContainer,
                    shape = RoundedCornerShape(12.dp)
                ).clickable(
                    onClick = {
                        isExpandStartSoundDropdown = true
                    }
                ),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = startSoundList.first { it.second == startSound }.first,
                    style = CustomTheme.typography.textField,
                    color = CustomTheme.colors.selectorTextSelected,
                )
                DropdownMenu(
                    modifier = Modifier.background(
                        color = CustomTheme.colors.textFieldContainer,
                        shape = RoundedCornerShape(12.dp)
                    ),
                    expanded = isExpandStartSoundDropdown,
                    onDismissRequest = {
                        isExpandStartSoundDropdown = false
                    },
                    shape = RoundedCornerShape(12.dp),
                ) {
                    startSoundList.forEach{
                        DropdownMenuItem(
                            modifier = Modifier.height(32.dp).background(
                                color = CustomTheme.colors.textFieldContainer,
                                shape = RoundedCornerShape(12.dp)
                            ),
                            text = {
                                Text(
                                    text = it.first,
                                    style = CustomTheme.typography.textField,
                                    color = CustomTheme.colors.selectorTextSelected,
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                isExpandStartSoundDropdown = false
                                onChangeStartSound(it.second)
                            },
                        )
                        if(it != startSoundList.last()){
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = CustomTheme.colors.divider
                            )
                        }
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
                text = stringResource(R.string.break_time_sound),
                style = CustomTheme.typography.editTitleSmall,
                color = CustomTheme.colors.text,
            )
            Box(
                modifier = Modifier.fillMaxWidth(0.8f).height(32.dp).background(
                    color = CustomTheme.colors.textFieldContainer,
                    shape = RoundedCornerShape(12.dp)
                ).clickable(
                    onClick = {
                        isExpandBreakTimeSoundDropdown = true
                    }
                ),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = restartSoundList.first { it.second == restartSound }.first,
                    style = CustomTheme.typography.textField,
                    color = CustomTheme.colors.selectorTextSelected,
                )
                DropdownMenu(
                    modifier = Modifier.background(
                        color = CustomTheme.colors.textFieldContainer,
                        shape = RoundedCornerShape(12.dp)
                    ),
                    expanded = isExpandBreakTimeSoundDropdown,
                    onDismissRequest = {
                        isExpandBreakTimeSoundDropdown = false
                    },
                    shape = RoundedCornerShape(12.dp),
                ) {
                    restartSoundList.forEach {
                        DropdownMenuItem(
                            modifier = Modifier.height(32.dp).background(
                                color = CustomTheme.colors.textFieldContainer,
                                shape = RoundedCornerShape(12.dp)
                            ),
                            text = {
                                Text(
                                    text = it.first,
                                    style = CustomTheme.typography.textField,
                                    color = CustomTheme.colors.selectorTextSelected,
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                isExpandBreakTimeSoundDropdown = false
                                onChangeBreakTimeSound(it.second)
                            },
                        )
                        if(it != restartSoundList.last()){
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = CustomTheme.colors.divider
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun SoundEditBoxPreview() {
    MyTheme {
        SoundEditBox(
            soundMode = SoundMode.SOUND,
            onClickSoundMode = {},
            startSound = 0,
            restartSound = 0,
            onChangeStartSound = {},
            onChangeBreakTimeSound = {},
        )
    }
}
