package com.pomodoro.timer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pomodoro.timer.database.entity.CustomWidgetEntity


@Database(entities = [CustomWidgetEntity::class], version = 2, exportSchema = true)
@TypeConverters(ImageTypeConverter::class)
abstract class PomodoroDatabase : RoomDatabase() {
    abstract fun customWidgetDao(): CustomWidgetDao
}