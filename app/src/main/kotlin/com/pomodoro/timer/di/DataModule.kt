package com.pomodoro.timer.di

import com.pomodoro.timer.data.MainRepository
import com.pomodoro.timer.data.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {
    @Binds
    fun bindsMainRepository(mainRepositoryImpl: MainRepositoryImpl): MainRepository
}