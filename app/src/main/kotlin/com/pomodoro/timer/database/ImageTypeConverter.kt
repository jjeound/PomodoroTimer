package com.pomodoro.timer.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import javax.inject.Inject


@ProvidedTypeConverter
class ImageTypeConverter @Inject constructor(){
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