package com.pomodoro.timer.database.typeConverter

import androidx.compose.ui.text.font.FontFamily
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.pomodoro.timer.ui.theme.bebasNeue
import com.pomodoro.timer.ui.theme.comfortaa
import com.pomodoro.timer.ui.theme.digitalMonoFont
import com.pomodoro.timer.ui.theme.freesentation
import com.pomodoro.timer.ui.theme.mountainsOfChristmas
import com.pomodoro.timer.ui.theme.pretendard
import javax.inject.Inject

@ProvidedTypeConverter
class FontFamilyTypeConverter @Inject constructor() {
    @TypeConverter
    fun fromFontFamily(fontFamily: FontFamily): String {
        return when (fontFamily) {
            pretendard -> "pretendard"
            comfortaa -> "comfortaa"
            freesentation -> "freesentation"
            bebasNeue -> "bebasNeue"
            digitalMonoFont -> "digitalMonoFont"
            mountainsOfChristmas -> "mountainsOfChristmas"
            else -> "pretendard"
        }
    }

    @TypeConverter
    fun toFontFamily(value: String): FontFamily {
        return when (value) {
            "pretendard" -> pretendard
            "comfortaa" -> comfortaa
            "freesentation" -> freesentation
            "bebasNeue" -> bebasNeue
            "digitalMonoFont" -> digitalMonoFont
            "mountainsOfChristmas" -> mountainsOfChristmas
            else -> pretendard
        }
    }
}