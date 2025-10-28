package com.pomodoro.timer.database.typeConverter

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import javax.inject.Inject

@ProvidedTypeConverter
class ColorTypeConverter @Inject constructor() {
    @TypeConverter
    fun fromColor(color: Color): String {
        return color.toArgb().toString(16)
    }

    @TypeConverter
    fun toColor(value: String): Color {
        return Color(value.toLong(16))
    }
}