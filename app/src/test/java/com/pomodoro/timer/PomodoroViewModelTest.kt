package com.pomodoro.timer

import app.cash.turbine.test
import com.pomodoro.timer.data.GoalRepository
import com.pomodoro.timer.presentation.pomodoro.PomodoroViewModel
import com.pomodoro.timer.presentation.pomodoro.TimerState
import com.pomodoro.timer.presentation.pomodoro.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
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
class PomodoroViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeGoalRepo: FakeGoalRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeGoalRepo = FakeGoalRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createVm() = PomodoroViewModel(fakeGoalRepo)

    // ── Goal tests ──────────────────────────────────────────────────────────

    @Test
    fun `setGoal은 goalText를 업데이트`() = runTest {
        val vm = createVm()
        vm.setGoal("Chapter 3 공부하기")
        advanceUntilIdle()
        assertEquals("Chapter 3 공부하기", vm.goalText.value)
    }

    @Test
    fun `onGoalComplete는 isGoalCompleted를 true로`() = runTest {
        val vm = createVm()
        vm.onGoalComplete()
        assertTrue(vm.isGoalCompleted)
    }

    @Test
    fun `onGoalComplete 두 번 호출하면 토글`() = runTest {
        val vm = createVm()
        vm.onGoalComplete()
        vm.onGoalComplete()
        assertFalse(vm.isGoalCompleted)
    }

    @Test
    fun `onReset은 isGoalCompleted를 false로 초기화`() = runTest {
        val vm = createVm()
        vm.onGoalComplete()
        vm.onReset()
        assertFalse(vm.isGoalCompleted)
    }

    @Test
    fun `onStart는 isGoalCompleted를 false로 초기화`() = runTest {
        val vm = createVm()
        vm.onGoalComplete()
        vm.onStart()
        testDispatcher.scheduler.runCurrent()
        assertFalse(vm.isGoalCompleted)
    }

    // ── 기존 테스트 ─────────────────────────────────────────────────────────

    @Test
    fun `초기 상태는 IDLE이고 remainingTime은 3600`() {
        val vm = createVm()
        assertEquals(TimerState.IDLE, vm.state)
        assertEquals(60 * 60, vm.remainingTime)
        assertEquals(3, vm.repeat)
    }

    @Test
    fun `onStart는 RUNNING 상태로 변경하고 tempRepeat을 1 감소`() = runTest {
        val vm = createVm()
        val initialRepeat = vm.repeat
        vm.onStart()
        testDispatcher.scheduler.runCurrent()
        assertEquals(TimerState.RUNNING, vm.state)
        assertEquals(initialRepeat - 1, vm.tempRepeat)
    }

    @Test
    fun `onPause는 PAUSED 상태로 변경`() = runTest {
        val vm = createVm()
        vm.onStart()
        testDispatcher.scheduler.runCurrent()
        vm.onPause()
        assertEquals(TimerState.PAUSED, vm.state)
    }

    @Test
    fun `onReset은 IDLE로 복귀하고 remainingTime을 3600으로 초기화`() = runTest {
        val vm = createVm()
        vm.onStart()
        testDispatcher.scheduler.runCurrent()
        vm.onReset()
        assertEquals(TimerState.IDLE, vm.state)
        assertEquals(60 * 60, vm.remainingTime)
        assertEquals(vm.repeat, vm.tempRepeat)
    }

    @Test
    fun `tempRepeat이 0이면 onStart 호출 시 리셋`() = runTest {
        val vm = createVm()
        vm.setRP(0)
        vm.onStart()
        testDispatcher.scheduler.runCurrent()
        assertEquals(TimerState.IDLE, vm.state)
    }

    @Test
    fun `setBT는 breakTime을 업데이트`() {
        val vm = createVm()
        vm.setBT(300)
        assertEquals(300, vm.breakTime)
    }

    @Test
    fun `setRP는 repeat과 tempRepeat을 함께 업데이트`() {
        val vm = createVm()
        vm.setRP(5)
        assertEquals(5, vm.repeat)
        assertEquals(5, vm.tempRepeat)
    }

    @Test
    fun `onStart는 PlayStartSound 이벤트 발행`() = runTest {
        val vm = createVm()
        vm.eventFlow.test {
            vm.onStart()
            testDispatcher.scheduler.runCurrent()
            assertEquals(UiEvent.PlayStartSound, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `1초 경과 시 remainingTime 1 감소`() = runTest {
        val vm = createVm()
        vm.onStart()
        testDispatcher.scheduler.runCurrent()
        val before = vm.remainingTime
        advanceTimeBy(1001)
        assertTrue(vm.remainingTime < before)
    }

    @Test
    fun `breakTime 도달 시 PlayBreakSound 이벤트 발행`() = runTest {
        val vm = createVm()
        vm.setBT(2)
        vm.eventFlow.test {
            vm.onStart()
            testDispatcher.scheduler.runCurrent()
            // PlayStartSound 소비
            assertEquals(UiEvent.PlayStartSound, awaitItem())
            // remainingTime이 2가 될 때까지 진행
            advanceTimeBy((60 * 60 - 2 + 1) * 1000L)
            assertEquals(UiEvent.PlayBreakSound, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
