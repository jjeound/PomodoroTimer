package com.pomodoro.timer.presentation.pomodoro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomodoro.timer.data.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {
    var remainingTime by mutableIntStateOf(60 * 60)
        private set
    var isRunning by mutableStateOf(false)
        private set
    var state by mutableStateOf(TimerState.IDLE)
        private set
    var breakTime by mutableIntStateOf(5 * 60)
        private set
    var repeat by mutableIntStateOf(5)
        private set

    fun setBT(seconds: Int) {
        breakTime = seconds
    }

    fun setRP(count: Int) {
        repeat = count
    }

    fun onReset() {
        remainingTime = 60 * 60
        state = TimerState.IDLE
    }

    fun onPause() {
        isRunning = false
        state = TimerState.PAUSED
    }

    fun onStart() {
        if (isRunning) return
        isRunning = true
        repeat--
        state = TimerState.RUNNING
        viewModelScope.launch {
            while (isRunning && remainingTime > 0) {
                delay(1000)
                remainingTime--
            }
            if (remainingTime == breakTime) onBreak()
        }
    }

    fun onResume() {
        isRunning = true
        state = TimerState.RUNNING
    }

    fun onBreak(){
        state = TimerState.IDLE
        viewModelScope.launch {
            while (remainingTime > 0) {
                delay(1000)
                remainingTime--
            }
            if (remainingTime == 0){
                remainingTime = 60 * 60
                onStart()
            }
        }
    }
}

enum class TimerState {
    IDLE,
    RUNNING,
    PAUSED,
}
