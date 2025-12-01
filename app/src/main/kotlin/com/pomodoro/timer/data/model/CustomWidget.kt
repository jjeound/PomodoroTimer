package com.pomodoro.timer.data.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.pomodoro.timer.R
import com.pomodoro.timer.ui.theme.Black
import com.pomodoro.timer.ui.theme.Red
import com.pomodoro.timer.ui.theme.White
import com.pomodoro.timer.ui.theme.pretendard

@Immutable
data class CustomWidget(
    val id: Long = 0L,
    val fontFamily: FontFamily = pretendard,
    val fontSize: Float = 30f,
    val backgroundImage: String? = null,
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
    val fontColor: Color = Black,
    val screenColor: Color = White,
    val fgColor: Color = Red,
    val bgColor: Color = White,
    val handColor: Color = Black,
    val edgeColor: Color = Black,
    val bgMode: BgMode = BgMode.IDLE,
    val pattern: Int = 0,
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
    SNOW
}