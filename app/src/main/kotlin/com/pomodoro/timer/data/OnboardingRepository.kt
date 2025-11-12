package com.pomodoro.timer.data

import androidx.annotation.WorkerThread

interface OnboardingRepository {
    @WorkerThread
    suspend fun isFirstEnter(): Boolean
    @WorkerThread
    suspend fun onFinishOnboarding()
}