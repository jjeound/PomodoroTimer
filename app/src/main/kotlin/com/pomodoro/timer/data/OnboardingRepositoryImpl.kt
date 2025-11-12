package com.pomodoro.timer.data

import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.pomodoro.timer.data.PrefKeys.IS_FIRST_ENTER
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): OnboardingRepository {
    @WorkerThread
    override suspend fun isFirstEnter(): Boolean {
        return dataStore.data.map { pref ->
            pref[IS_FIRST_ENTER]
        }.firstOrNull() ?: true
    }

    @WorkerThread
    override suspend fun onFinishOnboarding() {
        dataStore.edit { prefs ->
            prefs[IS_FIRST_ENTER] = false
        }
    }
}