package com.pomodoro.timer.data

import androidx.annotation.WorkerThread
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.pomodoro.timer.data.model.CustomWidget
import com.pomodoro.timer.data.PrefKeys.WIDGET_ID
import com.pomodoro.timer.database.ColorDao
import com.pomodoro.timer.database.CustomWidgetDao
import com.pomodoro.timer.database.entity.ColorEntity
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
    private val colorDao: ColorDao,
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @WorkerThread
    override fun getAllCustomWidgets(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ): Flow<List<CustomWidget>> {
        return dataStore.data
            .map { pref -> pref[WIDGET_ID] }
            .distinctUntilChanged()
            .flatMapLatest { id ->
                flow {
                    val widgets = dao.getAllCustomWidgets().asDomain().sortedByDescending {
                        it.id == (id ?: 0L)
                    }
                    if(widgets.isEmpty()){
                        emit(emptyList())
                    } else {
                        emit(widgets)
                    }
                }
            }
            .onStart { onStart() }
            .onEach { onComplete() }
            .catch { throwable ->
                onError(throwable)
                emit(emptyList())
            }
        }

    @WorkerThread
    override fun updateWidget(
        widget: CustomWidget,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ): Flow<Unit> = flow {
        dataStore.edit { prefs ->
            prefs[WIDGET_ID] = widget.id
        }
        val entity = widget.asEntity()
        dao.updateCustomWidget(
            widget.id,
            entity.fontFamily,
            entity.fontSize,
            entity.fontColor,
            entity.backgroundImage,
            entity.mode,
            entity.hour,
            entity.minute,
            entity.second,
            entity.gap,
            entity.breakTime,
            entity.startSound,
            entity.breakTimeSound,
            entity.soundMode,
            entity.repeat,
            entity.fgColor,
            entity.bgColor,
            entity.handColor,
            entity.edgeColor,
            entity.bgMode
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
        val id = dao.insertCustomWidget(
            widget.asEntity()
        )
        dataStore.edit { prefs ->
            prefs[WIDGET_ID] = id
        }
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

    @WorkerThread
    override fun saveColor(
        color: Color,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ): Flow<Unit> = flow {
        colorDao.insertColor(
            ColorEntity(color)
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
    override fun getAllColors(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ): Flow<List<Color>> = flow {
        val colors = colorDao.getAllColors().map { it.color }
        emit(colors)
    }.onStart {
        onStart()
    }.onEach {
        onComplete()
    }.catch { throwable ->
        onError(throwable)
        emit(emptyList())
    }

    @WorkerThread
    override fun updateColor(
        oldColor: Color,
        newColor: Color,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ): Flow<Unit> = flow {
        colorDao.updateColor(oldColor, newColor)
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
    override fun deleteColor(
        color: Color,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ) : Flow<Unit> = flow {
        colorDao.deleteColor(color)
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