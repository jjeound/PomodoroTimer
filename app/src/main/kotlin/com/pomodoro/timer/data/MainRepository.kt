package com.pomodoro.timer.data

import androidx.annotation.WorkerThread
import com.pomodoro.timer.CustomWidget
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    @WorkerThread
    fun getCustomWidget(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Flow<CustomWidget?>

    @WorkerThread
    fun getAllCustomWidgets(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Flow<List<CustomWidget>?>

    @WorkerThread
    fun updateWidget(
        widget: CustomWidget,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Flow<Unit>

    @WorkerThread
    fun saveWidget(
        widget: CustomWidget,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Flow<Unit>

    @WorkerThread
    fun deleteWidget(
        id: Long,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Flow<Unit>
}