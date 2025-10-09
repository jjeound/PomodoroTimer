package com.pomodoro.timer.data

import androidx.annotation.WorkerThread
import com.pomodoro.timer.CustomWidget
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    @WorkerThread
    fun getCustomWidget(): Flow<CustomWidget?>
    @WorkerThread
    fun getAllCustomWidgets(): Flow<List<CustomWidget>?>
}