package com.pomodoro.timer.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PrefKeys {
    val WIDGET_ID = longPreferencesKey("widget_id")
    val IS_FIRST_ENTER = booleanPreferencesKey("is_first_enter")
    val GOAL_TEXT = stringPreferencesKey("goal_text")
}
