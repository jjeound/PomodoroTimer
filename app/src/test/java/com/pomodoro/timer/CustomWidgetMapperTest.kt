package com.pomodoro.timer

import androidx.compose.ui.graphics.Color
import com.pomodoro.timer.data.model.BgMode
import com.pomodoro.timer.data.model.CustomWidget
import com.pomodoro.timer.data.model.Mode
import com.pomodoro.timer.data.model.SoundMode
import com.pomodoro.timer.database.entity.mapper.asDomain
import com.pomodoro.timer.database.entity.mapper.asEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class CustomWidgetMapperTest {

    private val sampleWidget = CustomWidget(
        id = 1L,
        fontSize = 24f,
        mode = Mode.DIGITAL,
        hour = 0,
        minute = 25,
        second = 0,
        gap = 3,
        breakTime = 5,
        repeat = 4,
        fontColor = Color(0xFF000000L),
        fgColor = Color(0xFFFF0000L),
        bgColor = Color(0xFFFFFFFF.toInt()),
        soundMode = SoundMode.VIBRATE,
        bgMode = BgMode.SNOW,
        pattern = 2,
    )

    @Test
    fun `asEntity는 모든 필드를 보존`() {
        val entity = sampleWidget.asEntity()
        assertEquals(sampleWidget.fontSize, entity.fontSize)
        assertEquals(sampleWidget.mode, entity.mode)
        assertEquals(sampleWidget.hour, entity.hour)
        assertEquals(sampleWidget.minute, entity.minute)
        assertEquals(sampleWidget.repeat, entity.repeat)
        assertEquals(sampleWidget.soundMode, entity.soundMode)
        assertEquals(sampleWidget.bgMode, entity.bgMode)
        assertEquals(sampleWidget.pattern, entity.pattern)
    }

    @Test
    fun `asEntity는 id를 포함하지 않음 (autoGenerate)`() {
        val entity = sampleWidget.asEntity()
        assertEquals(0L, entity.id)
    }

    @Test
    fun `asDomain round-trip은 id를 포함한 모든 필드 보존`() {
        val entity = sampleWidget.asEntity().copy(id = sampleWidget.id)
        val result = entity.asDomain()
        assertEquals(sampleWidget.id, result.id)
        assertEquals(sampleWidget.fontSize, result.fontSize)
        assertEquals(sampleWidget.mode, result.mode)
        assertEquals(sampleWidget.minute, result.minute)
        assertEquals(sampleWidget.soundMode, result.soundMode)
        assertEquals(sampleWidget.bgMode, result.bgMode)
        assertEquals(sampleWidget.pattern, result.pattern)
    }

    @Test
    fun `Mode 전체 값 매핑`() {
        Mode.entries.forEach { mode ->
            val widget = CustomWidget(mode = mode)
            assertEquals(mode, widget.asEntity().asDomain().mode)
        }
    }

    @Test
    fun `SoundMode 전체 값 매핑`() {
        SoundMode.entries.forEach { soundMode ->
            val widget = CustomWidget(soundMode = soundMode)
            assertEquals(soundMode, widget.asEntity().asDomain().soundMode)
        }
    }

    @Test
    fun `BgMode 전체 값 매핑`() {
        BgMode.entries.forEach { bgMode ->
            val widget = CustomWidget(bgMode = bgMode)
            assertEquals(bgMode, widget.asEntity().asDomain().bgMode)
        }
    }
}
