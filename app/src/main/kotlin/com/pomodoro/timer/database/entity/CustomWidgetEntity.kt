package com.pomodoro.timer.database.entity

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_widget")
data class CustomWidgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val fontSize: Float,
    val fontWeight: Int,
    val fontColor: String,
    val backgroundImage: String? = null,
    val mode: Int = 0,
    val hour: Int,
    val minute: Int,
    val second: Int,
    val gap: Int,
    val breakTime: Int,
    val startSound: String,
    val restartSound: String,
    val expireMode: Int,
    val repeat: Int,
    val fgColor: String,
    val bgColor: String
)