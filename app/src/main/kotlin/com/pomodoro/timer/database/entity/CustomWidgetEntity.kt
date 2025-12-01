package com.pomodoro.timer.database.entity

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pomodoro.timer.data.model.BgMode
import com.pomodoro.timer.data.model.Mode
import com.pomodoro.timer.data.model.SoundMode

@Entity(tableName = "custom_widget")
data class CustomWidgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val fontFamily: FontFamily,
    val fontSize: Float,
    val fontColor: Color,
    val backgroundImage: String? = null,
    val mode: Mode,
    val hour: Int,
    val minute: Int,
    val second: Int,
    val gap: Int,
    val breakTime: Int,
    val startSound: Int,
    val breakTimeSound: Int,
    val soundMode: SoundMode,
    val repeat: Int,
    val screenColor: Color,
    val fgColor: Color,
    val bgColor: Color,
    val handColor: Color,
    val edgeColor: Color,
    val bgMode: BgMode,
    val pattern: Int,
)