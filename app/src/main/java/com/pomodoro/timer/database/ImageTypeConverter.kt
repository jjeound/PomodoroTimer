package com.pomodoro.timer.database

import androidx.room.TypeConverter


class ImageTypeConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return if (value.isBlank()) emptyList()
        else value.split(",")
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }
}