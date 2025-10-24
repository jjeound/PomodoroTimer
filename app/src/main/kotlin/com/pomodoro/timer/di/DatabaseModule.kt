package com.pomodoro.timer.di

import android.app.Application
import androidx.room.Room
import com.pomodoro.timer.database.ColorTypeConverter
import com.pomodoro.timer.database.CustomWidgetDao
import com.pomodoro.timer.database.PomodoroDatabase
import com.pomodoro.timer.database.TextStyleTypeConverter
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
        textStyleTypeConverter: TextStyleTypeConverter,
        colorTypeConverter: ColorTypeConverter
    ): PomodoroDatabase {
        return Room
            .databaseBuilder(application, PomodoroDatabase::class.java, "PomodoroTimer.db")
            .addTypeConverter(textStyleTypeConverter)
            .addTypeConverter(colorTypeConverter)
            .build()
    }

    @Provides
    @Singleton
    fun provideCustomWidgetDao(appDatabase: PomodoroDatabase): CustomWidgetDao {
        return appDatabase.customWidgetDao()
    }
}