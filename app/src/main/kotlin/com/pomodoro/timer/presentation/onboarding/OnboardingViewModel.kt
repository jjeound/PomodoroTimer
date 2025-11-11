package com.pomodoro.timer.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomodoro.timer.data.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val repository: OnboardingRepository
): ViewModel() {

    private val _isFirstEnter: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isFirstEnter = _isFirstEnter.asStateFlow()

    init {
        isFirstEnter()
    }

    fun isFirstEnter(){
        viewModelScope.launch {
            _isFirstEnter.value = repository.isFirstEnter()

        }
    }
}