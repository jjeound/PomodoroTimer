package com.pomodoro.timer

import androidx.compose.ui.text.TextStyle

data class CustomWidget(
    val fontStyle: TextStyle? = null,
    val backgroundImage: List<String>? = null,
    val mode: Int = 0, // 0: 시계 형, 1: 디지털 형, 2: 혼합 형
    val hour: Int? = null,
    val minute: Int? = null,
    val second: Int? = null,
    val interval: Int? = null,
    val sound: String? = null,
    val vibration: Boolean = false,
    val repeat: Int? = null,
)
