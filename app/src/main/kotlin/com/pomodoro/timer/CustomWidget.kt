package com.pomodoro.timer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.pomodoro.timer.ui.theme.customTypography

data class CustomWidget(
    val id: Long = 0L,
    val fontStyle: TextStyle = customTypography.title1,
    val backgroundImage: List<String>? = null,
    val mode: Int = 0, // 0: 시계 형, 1: 디지털 형, 2: 탁상 형
    val hour: Int = 1,
    val minute: Int = 0,
    val second: Int = 0,
    val interval: Int = 5,
    val breakTime: Int = 5,
    val startSound: String = "default",
    val restartSound: String = "default",
    val expireMode: Int = 0,
    val repeat: Int = 0,
    val fontColor: Color = Color(0xFF000000),
    val fgColor: Color = Color(0xFFF8384C),
    val bgColor: Color = Color(0x00000000)
)
