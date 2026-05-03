package com.pomodoro.timer

import com.pomodoro.timer.data.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeGoalRepository : GoalRepository {
    private val _goal = MutableStateFlow("")

    override fun getGoal(): Flow<String> = _goal

    override suspend fun saveGoal(text: String) {
        _goal.value = text
    }
}
