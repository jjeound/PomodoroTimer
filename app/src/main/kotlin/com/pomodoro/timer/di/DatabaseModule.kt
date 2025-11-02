package com.pomodoro.timer.di

import android.app.Application
import androidx.room.Room
import com.pomodoro.timer.database.ColorDao
import com.pomodoro.timer.database.typeConverter.ColorTypeConverter
import com.pomodoro.timer.database.CustomWidgetDao
import com.pomodoro.timer.database.PomodoroDatabase
import com.pomodoro.timer.database.typeConverter.BgModeTypeConverter
import com.pomodoro.timer.database.typeConverter.ModeTypeConverter
import com.pomodoro.timer.database.typeConverter.SoundModeTypeConverter
import com.pomodoro.timer.database.typeConverter.FontFamilyTypeConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        application: Application,
        fontFamilyTypeConverter: FontFamilyTypeConverter,
        colorTypeConverter: ColorTypeConverter,
        modeTypeConverter: ModeTypeConverter,
        soundModeTypeConverter: SoundModeTypeConverter,
        bgModeTypeConverter: BgModeTypeConverter
    ): PomodoroDatabase {
        return Room
            .databaseBuilder(application, PomodoroDatabase::class.java, "PomodoroTimer.db")
            .fallbackToDestructiveMigration(true)
            .addTypeConverter(fontFamilyTypeConverter)
            .addTypeConverter(colorTypeConverter)
            .addTypeConverter(modeTypeConverter)
            .addTypeConverter(soundModeTypeConverter)
            .addTypeConverter(bgModeTypeConverter)
            .build()
    }

    @Provides
    @Singleton
    fun provideCustomWidgetDao(appDatabase: PomodoroDatabase): CustomWidgetDao {
        return appDatabase.customWidgetDao()
    }

    @Provides
    @Singleton
    fun provideColorDao(appDatabase: PomodoroDatabase): ColorDao {
        return appDatabase.colorDao()
    }
}