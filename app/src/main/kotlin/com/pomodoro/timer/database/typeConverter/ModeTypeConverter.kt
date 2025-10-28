package com.pomodoro.timer.database.typeConverter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.pomodoro.timer.data.model.Mode
import javax.inject.Inject


@ProvidedTypeConverter
class ModeTypeConverter @Inject constructor() {
    @TypeConverter
    fun fromMode(mode: Mode): Int {
        return when(mode){
            Mode.POMODORO -> 0
            Mode.DIGITAL -> 1
            Mode.DESK -> 2
        }
    }

    @TypeConverter
    fun toMode(value: Int): Mode {
        return when(value){
            0 -> Mode.POMODORO
            1 -> Mode.DIGITAL
            2 -> Mode.DESK
            else -> Mode.POMODORO
        }
    }
}