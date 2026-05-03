package com.pomodoro.timer.data

import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    fun getGoal(): Flow<String>
    suspend fun saveGoal(text: String)
}
