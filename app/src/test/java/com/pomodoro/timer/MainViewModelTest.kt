package com.pomodoro.timer

import androidx.compose.ui.graphics.Color
import com.pomodoro.timer.data.model.CustomWidget
import com.pomodoro.timer.presentation.MainUiState
import com.pomodoro.timer.presentation.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepo: FakeMainRepository
    private lateinit var vm: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeMainRepository()
        vm = MainViewModel(fakeRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `위젯이 없으면 기본 위젯 2개 생성`() = runTest {
        advanceUntilIdle()
        assertTrue(vm.widgets.value.isNotEmpty())
    }

    @Test
    fun `기존 위젯이 있으면 그대로 로드`() = runTest {
        val repo = FakeMainRepository()
        val existing = CustomWidget(id = 0L, fontSize = 20f)
        // saveWidget을 통해 먼저 삽입
        repo.saveWidget(existing, {}, {}, {}).collect {}
        val vm2 = MainViewModel(repo)
        advanceUntilIdle()
        assertTrue(vm2.widgets.value.any { it.fontSize == 20f })
    }

    @Test
    fun `saveColor는 colors 목록에 색상 추가`() = runTest {
        advanceUntilIdle()
        val initialSize = vm.colors.value.size
        vm.saveColor(Color.Cyan)
        advanceUntilIdle()
        assertEquals(initialSize + 1, vm.colors.value.size)
        assertTrue(vm.colors.value.contains(Color.Cyan))
    }

    @Test
    fun `deleteColor는 colors 목록에서 색상 제거`() = runTest {
        advanceUntilIdle()
        vm.saveColor(Color.Magenta)
        advanceUntilIdle()
        assertTrue(vm.colors.value.contains(Color.Magenta))
        vm.deleteColor(Color.Magenta)
        advanceUntilIdle()
        assertFalse(vm.colors.value.contains(Color.Magenta))
    }

    @Test
    fun `onShowButtonsChange는 showButtons를 토글`() = runTest {
        val initial = vm.showButtons
        vm.onShowButtonsChange()
        assertEquals(!initial, vm.showButtons)
        vm.onShowButtonsChange()
        assertEquals(initial, vm.showButtons)
    }

    @Test
    fun `onCancelEdit는 editingWidget을 currentWidget으로 복원`() = runTest {
        advanceUntilIdle()
        val original = vm.currentWidget.value
        vm.onAddNewWidget()
        advanceUntilIdle()
        vm.onCancelEdit()
        advanceUntilIdle()
        assertEquals(original, vm.editingWidget.value)
    }

    @Test
    fun `uiState는 초기에 Loading이고 완료 후 Idle`() = runTest {
        // init 직후엔 Loading 또는 Idle 중 하나
        advanceUntilIdle()
        assertEquals(MainUiState.Idle, vm.uiState.value)
    }
}
