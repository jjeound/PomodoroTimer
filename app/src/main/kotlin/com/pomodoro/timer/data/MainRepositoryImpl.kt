package com.pomodoro.timer.data

import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pomodoro.timer.CustomWidget
import com.pomodoro.timer.data.PrefKeys.WIDGET_ID
import com.pomodoro.timer.database.CustomWidgetDao
import com.pomodoro.timer.database.entity.mapper.asDomain
import com.pomodoro.timer.database.entity.mapper.asEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val dao: CustomWidgetDao,
    private val dataStore: DataStore<Preferences>
): MainRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    @WorkerThread
    override fun getCustomWidget(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ): Flow<CustomWidget?> {
        return dataStore.data
            .map { pref -> pref[WIDGET_ID] }
            .distinctUntilChanged()
            .flatMapLatest { id ->
                flow {
                    val widget = id?.let { dao.getCustomWidget(it).asDomain() }
                    emit(widget)
                }
            }
            .onStart { onStart() }
            .onEach { onComplete() }
            .catch { throwable ->
                onError(throwable)
                emit(null)
            }
    }

    @WorkerThread
    override fun getAllCustomWidgets(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ): Flow<List<CustomWidget>?> = flow {
        val widgets = dao.getAllCustomWidgets().asDomain()
        if(widgets.isEmpty()){
            emit(null)
        } else {
            emit(widgets)
        }
    }.onStart {
        onStart()
    }.onEach {
        onComplete()
    }.catch { throwable ->
        onError(throwable)
        emit(emptyList())
    }

    @WorkerThread
    override fun updateWidget(
        widget: CustomWidget,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ): Flow<Unit> = flow {
        val entity = widget.asEntity()
        dao.updateCustomWidget(
            entity.id,
            entity.fontSize,
            entity.fontWeight,
            entity.fontColor,
            entity.backgroundImage,
            entity.mode,
            entity.hour,
            entity.minute,
            entity.second,
            entity.interval,
            entity.breakTime,
            entity.startSound,
            entity.restartSound,
            entity.expireMode,
            entity.repeat,
            entity.fgColor,
            entity.bgColor
        )
        emit(Unit)
    }.onStart {
        onStart()
    }.onEach {
        onComplete()
    }.catch { throwable ->
        onError(throwable)
        emit(Unit)
    }

    @WorkerThread
    override fun saveWidget(
        widget: CustomWidget,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ): Flow<Unit> = flow {
        dao.insertCustomWidget(
            widget.asEntity()
        )
        emit(Unit)
    }.onStart {
        onStart()
    }.onEach {
        onComplete()
    }.catch { throwable ->
        onError(throwable)
        emit(Unit)
    }

    @WorkerThread
    override fun deleteWidget(
        id: Long,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ): Flow<Unit> = flow {
        dao.deleteCustomWidget(id)
        emit(Unit)
    }.onStart {
        onStart()
    }.onEach {
        onComplete()
    }.catch { throwable ->
        onError(throwable)
        emit(Unit)
    }
}