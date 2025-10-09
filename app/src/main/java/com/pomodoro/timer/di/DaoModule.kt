package com.pomodoro.timer.di

import com.pomodoro.timer.database.CustomWidgetDao
import com.pomodoro.timer.database.PomodoroDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    @Singleton
    fun providesCustomWidgetDao(
        database: PomodoroDatabase,
    ): CustomWidgetDao = database.customWidgetDao()
}