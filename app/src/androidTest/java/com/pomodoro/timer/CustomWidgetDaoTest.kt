package com.pomodoro.timer

import androidx.compose.ui.graphics.Color
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pomodoro.timer.data.model.BgMode
import com.pomodoro.timer.data.model.Mode
import com.pomodoro.timer.data.model.SoundMode
import com.pomodoro.timer.database.PomodoroDatabase
import com.pomodoro.timer.database.entity.CustomWidgetEntity
import com.pomodoro.timer.database.typeConverter.BgModeTypeConverter
import com.pomodoro.timer.database.typeConverter.ColorTypeConverter
import com.pomodoro.timer.database.typeConverter.FontFamilyTypeConverter
import com.pomodoro.timer.database.typeConverter.ModeTypeConverter
import com.pomodoro.timer.database.typeConverter.SoundModeTypeConverter
import com.pomodoro.timer.ui.theme.pretendard
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CustomWidgetDaoTest {

    private lateinit var db: PomodoroDatabase

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, PomodoroDatabase::class.java)
            .addTypeConverter(ColorTypeConverter())
            .addTypeConverter(FontFamilyTypeConverter())
            .addTypeConverter(ModeTypeConverter())
            .addTypeConverter(SoundModeTypeConverter())
            .addTypeConverter(BgModeTypeConverter())
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    private fun sampleEntity(
        mode: Mode = Mode.POMODORO,
        fontSize: Float = 30f,
    ) = CustomWidgetEntity(
        fontFamily = pretendard,
        fontSize = fontSize,
        fontColor = Color.Black,
        mode = mode,
        hour = 1,
        minute = 0,
        second = 0,
        gap = 5,
        breakTime = 5,
        startSound = 0,
        breakTimeSound = 0,
        soundMode = SoundMode.NO_SOUND,
        repeat = 1,
        screenColor = Color.White,
        fgColor = Color.Red,
        bgColor = Color.White,
        handColor = Color.Black,
        edgeColor = Color.Black,
        bgMode = BgMode.IDLE,
        pattern = 0,
    )

    @Test
    fun insertAndGetAll() = runBlocking {
        val dao = db.customWidgetDao()
        dao.insertCustomWidget(sampleEntity())
        val result = dao.getAllCustomWidgets()
        assertEquals(1, result.size)
    }

    @Test
    fun insertReturnsGeneratedId() = runBlocking {
        val dao = db.customWidgetDao()
        val id = dao.insertCustomWidget(sampleEntity())
        assertTrue(id > 0)
    }

    @Test
    fun getCustomWidgetById() = runBlocking {
        val dao = db.customWidgetDao()
        val id = dao.insertCustomWidget(sampleEntity(fontSize = 24f))
        val fetched = dao.getCustomWidget(id)
        assertEquals(24f, fetched.fontSize)
    }

    @Test
    fun deleteWidget() = runBlocking {
        val dao = db.customWidgetDao()
        val id = dao.insertCustomWidget(sampleEntity())
        dao.deleteCustomWidget(id)
        val result = dao.getAllCustomWidgets()
        assertTrue(result.isEmpty())
    }

    @Test
    fun updateWidget() = runBlocking {
        val dao = db.customWidgetDao()
        val id = dao.insertCustomWidget(sampleEntity())
        dao.updateCustomWidget(
            id = id,
            fontFamily = null,
            fontSize = 48f,
            fontColor = null,
            backgroundImage = null,
            mode = Mode.DIGITAL,
            hour = null,
            minute = null,
            second = null,
            gap = null,
            breakTime = null,
            startSound = null,
            breakTimeSound = null,
            soundMode = null,
            repeat = null,
            screenColor = null,
            fgColor = null,
            bgColor = null,
            handColor = null,
            edgeColor = null,
            bgMode = null,
            pattern = null,
        )
        val updated = dao.getCustomWidget(id)
        assertEquals(48f, updated.fontSize)
        assertEquals(Mode.DIGITAL, updated.mode)
    }

    @Test
    fun insertMultipleWidgetsWithDifferentModes() = runBlocking {
        val dao = db.customWidgetDao()
        dao.insertCustomWidget(sampleEntity(mode = Mode.POMODORO))
        dao.insertCustomWidget(sampleEntity(mode = Mode.DIGITAL))
        dao.insertCustomWidget(sampleEntity(mode = Mode.DESK))
        val all = dao.getAllCustomWidgets()
        assertEquals(3, all.size)
        assertTrue(all.any { it.mode == Mode.DIGITAL })
        assertTrue(all.any { it.mode == Mode.DESK })
    }
}
