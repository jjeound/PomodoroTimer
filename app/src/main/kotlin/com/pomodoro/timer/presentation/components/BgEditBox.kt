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
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pomodoro.timer.R
import com.pomodoro.timer.data.model.BgMode
import com.pomodoro.timer.ui.theme.CustomTheme

@Composable
fun BgEditBox(
    bgMode: BgMode,
    onClickBgMode: (BgMode) -> Unit
){
    val title = listOf(
        R.string.idle,
        R.string.snow
    )
    Column(
        modifier = Modifier.wrapContentHeight().fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Column(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.bg_mode),
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
                for (mode in BgMode.entries){
                    Box(
                        modifier = Modifier.background(
                            color = if(mode == bgMode) CustomTheme.colors.selectorContainerSelected else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        ).weight(1f).padding(vertical = 4.dp).clickable(
                            onClick = { onClickBgMode(mode) },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = stringResource(title[mode.ordinal]),
                            style = if(mode == bgMode) CustomTheme.typography.selectorSelected else CustomTheme.typography.selectorUnSelected,
                            color = CustomTheme.colors.selectorTextSelected
                        )
                    }
                    if(mode != BgMode.entries.last()){
                        VerticalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = CustomTheme.colors.selectorDivider,
                            thickness = 1.dp,
                        )
                    }
                }
            }
            Text(
                text = stringResource(R.string.bg_instruction),
                style = CustomTheme.typography.textField,
                color = CustomTheme.colors.text,
            )
        }
    }
}