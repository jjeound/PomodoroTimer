package com.pomodoro.timer.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_widget")
data class CustomWidgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val fontSize: Float,
    val fontWeight: Int,
    val fontColor: Long,
    val backgroundImage: String? = null, // JSON String (List<String> → JSON)
    val mode: Int = 0,
    val hour: Int,
    val minute: Int,
    val second: Int,
    val interval: Int,
    val breakTime: Int,
    val startSound: String,
    val restartSound: String,
    val expireMode: Int,
    val repeat: Int,
    val fgColor: Long,
    val bgColor: Long
)