package com.pomodoro.timer.database.typeConverter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.pomodoro.timer.data.model.SoundMode
import javax.inject.Inject


@ProvidedTypeConverter
class SoundModeTypeConverter @Inject constructor() {
    @TypeConverter
    fun fromSoundMode(soundMode: SoundMode): Int {
        return when(soundMode){
            SoundMode.NO_SOUND -> 0
            SoundMode.VIBRATE -> 1
            SoundMode.SOUND -> 2
        }
    }

    @TypeConverter
    fun toSoundMode(value: Int): SoundMode {
        return when(value){
            0 -> SoundMode.NO_SOUND
            1 -> SoundMode.VIBRATE
            2 -> SoundMode.SOUND
            else -> SoundMode.NO_SOUND
        }
    }
}