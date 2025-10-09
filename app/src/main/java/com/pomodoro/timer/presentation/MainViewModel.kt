package com.pomodoro.timer.presentation

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomodoro.timer.CustomWidget
import com.pomodoro.timer.data.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    val uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Idle)

    val defaultWidget: StateFlow<CustomWidget?> = repository.getCustomWidget()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

}

@Stable
sealed interface MainUiState {

    data object Idle : MainUiState

    data object Loading : MainUiState

    data class Error(val message: String?) : MainUiState
}


