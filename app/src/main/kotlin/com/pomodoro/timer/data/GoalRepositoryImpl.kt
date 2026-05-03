package com.pomodoro.timer.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.pomodoro.timer.data.PrefKeys.GOAL_TEXT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : GoalRepository {

    override fun getGoal(): Flow<String> =
        dataStore.data.map { it[GOAL_TEXT] ?: "" }

    override suspend fun saveGoal(text: String) {
        dataStore.edit { it[GOAL_TEXT] = text }
    }
}
