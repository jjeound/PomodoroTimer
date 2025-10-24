package com.pomodoro.timer.database

import androidx.compose.ui.text.TextStyle
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.pomodoro.timer.ui.theme.customTypography
import javax.inject.Inject

@ProvidedTypeConverter
class TextStyleTypeConverter @Inject constructor() {
    @TypeConverter
    fun fromTextStyle(textStyle: TextStyle): String {
        return when (textStyle) {
            customTypography.textPreview1 -> "pretendard"
            customTypography.textPreview2 -> "comfortaa"
            customTypography.textPreview3 -> "freesentation"
            customTypography.textPreview4 -> "bebasNeue"
            else -> "pretendard"
        }
    }

    @TypeConverter
    fun toTextStyle(value: String): TextStyle {
        return when (value) {
            "pretendard" -> customTypography.textPreview1
            "comfortaa" -> customTypography.textPreview2
            "freesentation" -> customTypography.textPreview3
            "bebasNeue" -> customTypography.textPreview4
            else -> customTypography.textPreview1
        }
    }
}