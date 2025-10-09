package com.pomodoro.timer.presentation

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun MainScreen() {
    var editMode by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize().combinedClickable(
            onLongClick = { editMode = !editMode },
            onClick = {  }
        )
    ){
        //이미지 배경 / 기본 배경
        // 타이머 중간
        // 타이머 시작, 중지, 초기화
        // 편집 모드 - 취소, 확인, 바텀시트
        // 반응형 UI

    }
}