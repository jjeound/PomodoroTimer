package com.pomodoro.timer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pomodoro.timer.database.entity.ColorEntity
import com.pomodoro.timer.database.entity.CustomWidgetEntity
import com.pomodoro.timer.database.typeConverter.BgModeTypeConverter
import com.pomodoro.timer.database.typeConverter.ColorTypeConverter
import com.pomodoro.timer.database.typeConverter.ModeTypeConverter
import com.pomodoro.timer.database.typeConverter.SoundModeTypeConverter
import com.pomodoro.timer.database.typeConverter.FontFamilyTypeConverter


@Database(entities = [CustomWidgetEntity::class, ColorEntity::class], version = 5, exportSchema = true)
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

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            ALTER TABLE custom_widget 
            ADD COLUMN pattern INTEGER NOT NULL DEFAULT 0
            """.trimIndent()
        )
    }
}