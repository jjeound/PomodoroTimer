package com.pomodoro.timer.presentation

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomodoro.timer.CustomWidget
import com.pomodoro.timer.data.MainRepository
import com.pomodoro.timer.ui.theme.customTypography
import com.pomodoro.timer.ui.theme.fontFamily
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Long

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    val uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Loading)

    private val _widgets: MutableStateFlow<List<CustomWidget>> = MutableStateFlow(emptyList())
    val widgets = _widgets.asStateFlow()

    var mode by mutableIntStateOf(0)
        private set

    init {
        getWidgets()
    }

    fun getWidgets(){
        viewModelScope.launch {
            repository.getAllCustomWidgets(
                onStart = { uiState.value = MainUiState.Loading },
                onComplete = { uiState.value = MainUiState.Idle },
                onError = { message -> uiState.value = MainUiState.Error("다시 실행해주세요")
                    Log.d("MainViewModel", "getWidgets: $message")
                }
            ).collect {
                _widgets.value = it ?: listOf(
                    CustomWidget()
                )
            }
        }
    }

    fun saveWidget(widget: CustomWidget){
        viewModelScope.launch {
            repository.saveWidget(
                widget = widget,
                onStart = { uiState.value = MainUiState.Loading },
                onComplete = { getWidgets() },
                onError = { message -> uiState.value = MainUiState.Error("다시 저장해주세요")
                    Log.d("MainViewModel", "saveWidget: $message")
                }
            ).collect()
        }
    }

    fun updateWidget(widget: CustomWidget){
        viewModelScope.launch {
            repository.updateWidget(
                widget = widget,
                onStart = { uiState.value = MainUiState.Loading },
                onComplete = { getWidgets() },
                onError = { message -> uiState.value = MainUiState.Error("다시 수정해주세요")
                    Log.d("MainViewModel", "updateWidget: $message")
                }
            ).collect()
        }
    }

    fun deleteWidget(id: Long){
        viewModelScope.launch {
            repository.deleteWidget(
                id = id,
                onStart = { uiState.value = MainUiState.Loading },
                onComplete = { getWidgets() },
                onError = { message -> uiState.value = MainUiState.Error("다시 삭제해주세요")
                    Log.d("MainViewModel", "deleteWidget: $message")
                }
            ).collect()
        }
    }

    fun changeMode(mode: Int){
        this.mode = mode
    }
}

@Stable
sealed interface MainUiState {

    data object Idle : MainUiState

    data object Loading : MainUiState

    data class Error(val message: String?) : MainUiState
}

