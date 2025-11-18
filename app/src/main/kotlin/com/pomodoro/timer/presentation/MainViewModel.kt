package com.pomodoro.timer.presentation

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomodoro.timer.data.model.CustomWidget
import com.pomodoro.timer.data.MainRepository
import com.pomodoro.timer.data.model.Mode
import com.pomodoro.timer.ui.theme.Black
import com.pomodoro.timer.ui.theme.White
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
): ViewModel() {

    val uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Loading)

    private val _widgets: MutableStateFlow<List<CustomWidget>> = MutableStateFlow(emptyList())
    val widgets = _widgets.asStateFlow()

    private val _widgetsByMode: MutableStateFlow<List<CustomWidget>> = MutableStateFlow(emptyList())
    val widgetsByMode = _widgetsByMode.asStateFlow()

    private val _currentWidget: MutableStateFlow<CustomWidget?> = MutableStateFlow(null)
    val currentWidget = _currentWidget.asStateFlow()

    private val _editingWidgets: MutableStateFlow<List<CustomWidget>> = MutableStateFlow(emptyList())
    val editingWidgets = _editingWidgets.asStateFlow()

    private val _editingWidget: MutableStateFlow<CustomWidget?> = MutableStateFlow(null)
    val editingWidget = _editingWidget.asStateFlow()

    private val _colors: MutableStateFlow<List<Color>> = MutableStateFlow(listOf(
        Color(0xFFF94C5E), Color(0xFF9AC1F0), Color(0xFF72FA93), Color(0xFFF6C445)
    ))
    val colors = _colors.asStateFlow()

    var mode by mutableStateOf(Mode.POMODORO)
        private set
    var showButtons by mutableStateOf(true)
        private set

    init {
        getColors()
    }

    fun getWidgets(isSystemInDarkTheme: Boolean? = null){
        viewModelScope.launch {
            repository.getAllCustomWidgets(
                onStart = { uiState.value = MainUiState.Loading },
                onComplete = { uiState.value = MainUiState.Idle },
                onError = { message -> uiState.value = MainUiState.Error("다시 실행해주세요")
                    Log.d("MainViewModel", "getWidgets: $message")
                }
            ).collect {
                if (it.isEmpty()){
                    if(isSystemInDarkTheme != null){
                        if(isSystemInDarkTheme) {
                            _widgets.value = listOf(CustomWidget(
                                fontColor = White,
                                edgeColor = White,
                                bgColor = Black
                            ))
                        } else {
                            _widgets.value = listOf(CustomWidget())
                        }
                    } else _widgets.value = listOf(CustomWidget())
                } else {
                    _widgets.value = it
                }
                _widgetsByMode.value = _widgets.value.filter { widget ->
                    widget.mode == mode
                }
                _currentWidget.value = _widgetsByMode.value.first()
                _editingWidgets.value = _widgetsByMode.value
                _editingWidget.value = _currentWidget.value
            }
        }
    }

    fun editWidget(
        widget: CustomWidget
    ){
        _editingWidget.value = widget
    }

    fun onCancelEdit(){
        _editingWidgets.value = _widgetsByMode.value
        _editingWidget.value = _currentWidget.value
    }

    fun onDoneEdit(){
        if(_editingWidget.value!!.id != 0L)
        updateWidget(_editingWidget.value!!)
        else saveWidget(_editingWidget.value!!)
    }

    fun onAddNewWidget(isSystemInDarkTheme: Boolean){
        if(isSystemInDarkTheme){
            _editingWidgets.value += CustomWidget(
                mode = this.mode,
                fontColor = White,
                edgeColor = White,
                bgColor = Black
            )
        } else {
            _editingWidgets.value += CustomWidget(
                mode = this.mode
            )
        }
        _editingWidget.value = _editingWidgets.value.last()
    }

    fun onNextWidget(index: Int){
        _editingWidget.value = _editingWidgets.value[index]
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

    fun deleteWidget(){
        viewModelScope.launch {
            repository.deleteWidget(
                id = _editingWidget.value!!.id,
                onStart = { uiState.value = MainUiState.Loading },
                onComplete = { getWidgets() },
                onError = { message -> uiState.value = MainUiState.Error("다시 삭제해주세요")
                    Log.d("MainViewModel", "deleteWidget: $message")
                }
            ).collect()
        }
    }

    fun getColors(){
        viewModelScope.launch {
            repository.getAllColors(
                onStart = { uiState.value = MainUiState.Loading },
                onComplete = {  uiState.value = MainUiState.Idle},
                onError = { message -> uiState.value = MainUiState.Error("다시 실행해주세요")
                    Log.d("MainViewModel", "getColors: $message") }
            ).collect {
                _colors.value += it
            }
        }
    }

    fun saveColor(color: Color){
        viewModelScope.launch {
            repository.saveColor(
                color = color,
                onStart = {  },
                onComplete = {
                    _colors.value += color
                },
                onError = { message -> uiState.value = MainUiState.Error("다시 저장해주세요")
                    Log.d("MainViewModel", "saveColor: $message") }
            ).collect()
        }
    }

    fun updateColor(oldColor: Color, newColor: Color){
        viewModelScope.launch {
            repository.updateColor(
                oldColor = oldColor,
                newColor = newColor,
                onStart = {  },
                onComplete = {
                    _colors.value = _colors.value.map {
                        if(it == oldColor) newColor else it
                    }
                },
                onError = { message -> uiState.value = MainUiState.Error("다시 수정해주세요")
                    Log.d("MainViewModel", "updateColor: $message") }
            ).collect()
        }
    }

    fun deleteColor(color: Color){
        viewModelScope.launch {
            repository.deleteColor(
                color = color,
                onStart = {  },
                onComplete = {
                    _colors.value = _colors.value.filterNot { it == color }
                },
                onError = { message -> uiState.value = MainUiState.Error("다시 삭제해주세요")
                    Log.d("MainViewModel", "deleteColor: $message") }
            ).collect()
        }
    }

    fun onShowButtonsChange(){
        showButtons = !showButtons
    }
}

@Stable
sealed interface MainUiState {

    data object Idle : MainUiState

    data object Loading : MainUiState

    data class Error(val message: String?) : MainUiState
}

