package com.pomodoro.timer.database.entity

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_widget")
data class CustomWidgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val textStyle: TextStyle,
    val fontColor: Color,
    val backgroundImage: String? = null,
    val mode: Int = 0,
    val hour: Int,
    val minute: Int,
    val second: Int,
    val gap: Int,
    val breakTime: Int,
    val startSound: Int,
    val breakTimeSound: Int,
    val soundMode: Int,
    val repeat: Int,
    val fgColor: Color,
    val bgColor: Color,
    val handColor: Color,
    val edgeColor: Color
)