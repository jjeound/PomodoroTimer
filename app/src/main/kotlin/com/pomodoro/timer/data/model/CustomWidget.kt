package com.pomodoro.timer.data.model

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.pomodoro.timer.R
import com.pomodoro.timer.ui.theme.customTypography

data class CustomWidget(
    val id: Long = 0L,
    val textStyle: TextStyle = customTypography.buttonTimerSmall,
    val backgroundImage: Uri? = null,
    val mode: Mode = Mode.POMODORO, // 0: 시계 형, 1: 디지털 형, 2: 탁상 형
    val hour: Int = 1,
    val minute: Int = 0,
    val second: Int = 0,
    val gap: Int = 5,
    val breakTime: Int = 5,
    val startSound: Int = R.raw.buzzer,
    val breakTimeSound: Int = R.raw.buzzer,
    val soundMode: SoundMode = SoundMode.NO_SOUND,
    val repeat: Int = 1,
    val fontColor: Color = Color(0xFF000000),
    val fgColor: Color = Color(0xFFF8384C),
    val bgColor: Color = Color(0x00000000),
    val handColor: Color = Color(0xFF000000),
    val edgeColor: Color = Color(0xFF000000),
    val bgMode: BgMode = BgMode.IDLE
)

enum class Mode {
    POMODORO,
    DIGITAL,
    DESK
}

enum class SoundMode {
    NO_SOUND,
    VIBRATE,
    SOUND
}

enum class BgMode {
    IDLE,
    RAIN,
    SNOW
}