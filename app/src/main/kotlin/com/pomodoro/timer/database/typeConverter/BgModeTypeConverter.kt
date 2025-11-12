package com.pomodoro.timer.database.typeConverter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.pomodoro.timer.data.model.BgMode
import javax.inject.Inject


@ProvidedTypeConverter
class BgModeTypeConverter @Inject constructor() {
    @TypeConverter
    fun fromBgMode(bgMode: BgMode): Int {
        return when(bgMode){
            BgMode.IDLE -> 0
            BgMode.SNOW -> 1
        }
    }

    @TypeConverter
    fun toBgMode(value: Int): BgMode {
        return when(value){
            0 -> BgMode.IDLE
            1-> BgMode.SNOW
            else -> BgMode.IDLE
        }
    }
}