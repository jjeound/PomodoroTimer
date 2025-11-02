package com.pomodoro.timer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pomodoro.timer.database.entity.ColorEntity
import com.pomodoro.timer.database.entity.CustomWidgetEntity
import com.pomodoro.timer.database.typeConverter.BgModeTypeConverter
import com.pomodoro.timer.database.typeConverter.ColorTypeConverter
import com.pomodoro.timer.database.typeConverter.ModeTypeConverter
import com.pomodoro.timer.database.typeConverter.SoundModeTypeConverter
import com.pomodoro.timer.database.typeConverter.FontFamilyTypeConverter


@Database(entities = [CustomWidgetEntity::class, ColorEntity::class], version = 4, exportSchema = true)
@TypeConverters(value =
    [
        FontFamilyTypeConverter::class, ColorTypeConverter::class, ModeTypeConverter::class, SoundModeTypeConverter::class,
        BgModeTypeConverter::class
    ]
)
abstract class PomodoroDatabase : RoomDatabase() {
    abstract fun customWidgetDao(): CustomWidgetDao
    abstract fun colorDao(): ColorDao
}