package com.pomodoro.timer.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import com.pomodoro.timer.ui.theme.MyTheme

@Composable
fun SoundEditBox(
    expireMode: ExpireMode,
    onClickExpireMode: (ExpireMode) -> Unit,
    startSound: String,
    restartSound: String,
    onChangeStartSound: (String) -> Unit,
    onChangeRestartSound: (String) -> Unit,
) {
    var isExpandStartSoundDropdown by remember { mutableStateOf(false) }
    var isExpandRestartSoundDropdown by remember { mutableStateOf(false) }
    val startSoundList = listOf("Chime", "Bell", "Beep")
    val restartSoundList = listOf("Chime", "Bell", "Beep")
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
                text = stringResource(R.string.expire_mode),
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
                ExpireMode.entries.forEachIndexed { i, mode ->
                    Box(
                        modifier = Modifier.background(
                            color = if(mode == expireMode) CustomTheme.colors.selectorContainerSelected else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        ).weight(1f).padding(vertical = 4.dp).clickable(
                            onClick = { onClickExpireMode(mode) }
                        ),
                        contentAlignment = Alignment.Center
                    ){
                        when(mode){
                            ExpireMode.NO_SOUND -> Text(
                                text = stringResource(R.string.no_sound),
                                style = if(mode == expireMode) CustomTheme.typography.selectorSelected else CustomTheme.typography.selectorUnSelected,
                                color = CustomTheme.colors.selectorTextSelected
                            )
                            ExpireMode.VIBRATE -> Text(
                                text = stringResource(R.string.vibrate),
                                style = if(mode == expireMode) CustomTheme.typography.selectorSelected else CustomTheme.typography.selectorUnSelected,
                                color = CustomTheme.colors.selectorTextSelected
                            )
                            ExpireMode.SOUND -> Text(
                                text = stringResource(R.string.sound),
                                style = if(mode == expireMode) CustomTheme.typography.selectorSelected else CustomTheme.typography.selectorUnSelected,
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
                    text = startSound,
                    style = CustomTheme.typography.textField,
                    color = CustomTheme.colors.text,
                )
                DropdownMenu(
                    modifier = Modifier.fillMaxWidth(0.8f).background(
                        color = CustomTheme.colors.textFieldContainer,
                        shape = RoundedCornerShape(12.dp)
                    ),
                    expanded = isExpandStartSoundDropdown,
                    onDismissRequest = {
                        isExpandStartSoundDropdown = false
                    },
                    shape = RoundedCornerShape(12.dp),
                ) {
                    startSoundList.forEach {
                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth().height(32.dp).background(
                                color = CustomTheme.colors.textFieldContainer,
                                shape = RoundedCornerShape(12.dp)
                            ),
                            text = {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = it,
                                    style = CustomTheme.typography.textField,
                                    color = CustomTheme.colors.text,
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                isExpandStartSoundDropdown = false
                                onChangeStartSound(it)
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
                text = stringResource(R.string.restart_sound),
                style = CustomTheme.typography.editTitleSmall,
                color = CustomTheme.colors.text,
            )
            Box(
                modifier = Modifier.fillMaxWidth(0.8f).height(32.dp).background(
                    color = CustomTheme.colors.textFieldContainer,
                    shape = RoundedCornerShape(12.dp)
                ).clickable(
                    onClick = {
                        isExpandRestartSoundDropdown = true
                    }
                ),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = restartSound,
                    style = CustomTheme.typography.textField,
                    color = CustomTheme.colors.text,
                )
                DropdownMenu(
                    modifier = Modifier.fillMaxWidth(0.8f).background(
                        color = CustomTheme.colors.textFieldContainer,
                        shape = RoundedCornerShape(12.dp)
                    ),
                    expanded = isExpandRestartSoundDropdown,
                    onDismissRequest = {
                        isExpandRestartSoundDropdown = false
                    },
                    shape = RoundedCornerShape(12.dp),
                ) {
                    restartSoundList.forEach {
                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth().height(32.dp).background(
                                color = CustomTheme.colors.textFieldContainer,
                                shape = RoundedCornerShape(12.dp)
                            ),
                            text = {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = it,
                                    style = CustomTheme.typography.textField,
                                    color = CustomTheme.colors.text,
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                isExpandRestartSoundDropdown = false
                                onChangeRestartSound(it)
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

enum class ExpireMode {
    NO_SOUND,
    VIBRATE,
    SOUND
}


@Preview
@Composable
fun SoundEditBoxPreview() {
    MyTheme {
        SoundEditBox(
            expireMode = ExpireMode.SOUND,
            onClickExpireMode = {},
            startSound = "Chime",
            restartSound = "Bell",
            onChangeStartSound = {},
            onChangeRestartSound = {},
        )
    }
}
