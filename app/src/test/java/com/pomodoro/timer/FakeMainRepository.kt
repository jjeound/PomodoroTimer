package com.pomodoro.timer

import androidx.compose.ui.graphics.Color
import com.pomodoro.timer.data.MainRepository
import com.pomodoro.timer.data.model.CustomWidget
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class FakeMainRepository : MainRepository {

    private val widgets = mutableListOf<CustomWidget>()
    private val colors = mutableListOf<Color>()
    private var nextId = 1L

    override fun getCustomWidget(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Flow<CustomWidget?> = flow {
        emit(widgets.firstOrNull())
    }.onStart { onStart() }.onEach { onComplete() }.catch { onError(it); emit(null) }

    override fun getAllCustomWidgets(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Flow<List<CustomWidget>> = flow {
        emit(widgets.toList())
    }.onStart { onStart() }.onEach { onComplete() }.catch { onError(it); emit(emptyList()) }

    override fun saveWidget(
        widget: CustomWidget,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Flow<Unit> = flow {
        val id = nextId++
        widgets.add(widget.copy(id = id))
        emit(Unit)
    }.onStart { onStart() }.onEach { onComplete() }.catch { onError(it); emit(Unit) }

    override fun updateWidget(
        widget: CustomWidget,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Flow<Unit> = flow {
        val idx = widgets.indexOfFirst { it.id == widget.id }
        if (idx >= 0) widgets[idx] = widget
        emit(Unit)
    }.onStart { onStart() }.onEach { onComplete() }.catch { onError(it); emit(Unit) }

    override fun deleteWidget(
        id: Long,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Flow<Unit> = flow {
        widgets.removeAll { it.id == id }
        emit(Unit)
    }.onStart { onStart() }.onEach { onComplete() }.catch { onError(it); emit(Unit) }

    override fun getAllColors(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Flow<List<Color>> = flow {
        emit(colors.toList())
    }.onStart { onStart() }.onEach { onComplete() }.catch { onError(it); emit(emptyList()) }

    override fun saveColor(
        color: Color,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Flow<Unit> = flow {
        colors.add(color)
        emit(Unit)
    }.onStart { onStart() }.onEach { onComplete() }.catch { onError(it); emit(Unit) }

    override fun updateColor(
        oldColor: Color,
        newColor: Color,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Flow<Unit> = flow {
        val idx = colors.indexOf(oldColor)
        if (idx >= 0) colors[idx] = newColor
        emit(Unit)
    }.onStart { onStart() }.onEach { onComplete() }.catch { onError(it); emit(Unit) }

    override fun deleteColor(
        color: Color,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Flow<Unit> = flow {
        colors.remove(color)
        emit(Unit)
    }.onStart { onStart() }.onEach { onComplete() }.catch { onError(it); emit(Unit) }
}
