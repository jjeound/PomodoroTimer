package com.pomodoro.timer.presentation.pomodoro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor(): ViewModel() {
    var remainingTime by mutableIntStateOf(60 * 60)
        private set
    var state by mutableStateOf(TimerState.IDLE)
        private set
    var breakTime by mutableIntStateOf(5 * 60)
        private set
    var repeat by mutableIntStateOf(1)
        private set
    var tempRepeat by mutableIntStateOf(repeat)
        private set
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    private var timerJob: Job? = null

    fun setBT(seconds: Int) {
        breakTime = seconds
    }

    fun setRP(count: Int) {
        repeat = count
        tempRepeat = repeat
    }

    fun onReset() {
        timerJob?.cancel()
        remainingTime = 60 * 60
        state = TimerState.IDLE
        tempRepeat = repeat
    }

    fun onPause() {
        state = TimerState.PAUSED
        timerJob?.cancel()
    }

    fun onStart() {
        if (tempRepeat == 0) {
            onReset()
            return
        }
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.PlayStartSound)
        }
        tempRepeat--
        state = TimerState.RUNNING
        startTimer(remainingTime - breakTime) { onBreak() }
    }

    fun onResume() {
        if (state == TimerState.PAUSED) {
            state = TimerState.RUNNING
            startTimer(remainingTime - if (state == TimerState.RUNNING) breakTime else 0) {
                onBreak()
            }
        }
    }

    private fun startTimer(duration: Int, onFinish: () -> Unit) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var time = duration
            while (time > 0 && state == TimerState.RUNNING) {
                delay(1000)
                time--
                remainingTime = breakTime + time
            }
            if (state == TimerState.RUNNING) onFinish()
        }
    }

    fun onBreak() {
        state = TimerState.BREAK
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.PlayBreakSound)
        }
        startTimer(breakTime) {
            remainingTime = 60 * 60
            onStart()
        }
    }
}

sealed interface UiEvent{
    data object PlayStartSound: UiEvent
    data object PlayBreakSound: UiEvent
}

enum class TimerState {
    IDLE,
    RUNNING,
    PAUSED,
    BREAK
}
