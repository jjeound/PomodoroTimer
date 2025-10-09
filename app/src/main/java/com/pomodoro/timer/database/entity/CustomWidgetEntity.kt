package com.pomodoro.timer.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_widget")
data class CustomWidgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fontSize: Float? = null,
    val fontWeight: Int? = null,
    val fontColor: Long? = null,
    val backgroundImage: String? = null, // JSON String (List<String> → JSON)
    val mode: Int = 0,
    val hour: Int? = null,
    val minute: Int? = null,
    val second: Int? = null,
    val interval: Int? = null,
    val sound: String? = null,
    val vibration: Boolean = false,
    val repeat: Int? = null
)