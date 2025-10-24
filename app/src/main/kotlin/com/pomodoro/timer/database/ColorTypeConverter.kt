package com.pomodoro.timer.database

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.pomodoro.timer.ui.theme.customTypography
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