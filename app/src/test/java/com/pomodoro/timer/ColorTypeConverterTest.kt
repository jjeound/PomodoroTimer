package com.pomodoro.timer

import androidx.compose.ui.graphics.Color
import com.pomodoro.timer.database.typeConverter.ColorTypeConverter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ColorTypeConverterTest {

    private val converter = ColorTypeConverter()

    @Test
    fun `검정색 round-trip`() {
        val color = Color.Black
        assertEquals(color, converter.toColor(converter.fromColor(color)))
    }

    @Test
    fun `흰색 round-trip`() {
        val color = Color.White
        assertEquals(color, converter.toColor(converter.fromColor(color)))
    }

    @Test
    fun `커스텀 ARGB 컬러 round-trip`() {
        val color = Color(0xFFF94C5EL)
        assertEquals(color, converter.toColor(converter.fromColor(color)))
    }

    @Test
    fun `투명도 포함 컬러 round-trip`() {
        val color = Color(0x80FF0000.toInt())
        assertEquals(color, converter.toColor(converter.fromColor(color)))
    }

    @Test
    fun `fromColor는 비어있지 않은 hex 문자열 반환`() {
        val result = converter.fromColor(Color.Red)
        assertTrue(result.isNotEmpty())
    }
}
