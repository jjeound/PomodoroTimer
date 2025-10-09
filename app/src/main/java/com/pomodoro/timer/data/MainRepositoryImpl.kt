package com.pomodoro.timer.data

import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pomodoro.timer.CustomWidget
import com.pomodoro.timer.data.PrefKeys.WIDGET_ID
import com.pomodoro.timer.database.CustomWidgetDao
import com.pomodoro.timer.database.entity.mapper.asDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val dao: CustomWidgetDao,
    private val dataStore: DataStore<Preferences>
): MainRepository {

    @WorkerThread
    override fun getCustomWidget(): Flow<CustomWidget?> = flow {
        val id = dataStore.data.map { pref ->
            pref[WIDGET_ID]
        }.first()
        if (id != null){
            emit(dao.getCustomWidget(id).asDomain())
        } else {
            emit(null)
        }
    }

    @WorkerThread
    override fun getAllCustomWidgets(): Flow<List<CustomWidget>?> = flow {
        emit(dao.getAllCustomWidgets().asDomain())
    }

}