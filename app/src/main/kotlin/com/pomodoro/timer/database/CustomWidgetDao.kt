package com.pomodoro.timer.database

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pomodoro.timer.data.model.BgMode
import com.pomodoro.timer.data.model.Mode
import com.pomodoro.timer.data.model.SoundMode
import com.pomodoro.timer.database.entity.CustomWidgetEntity

@Dao
interface CustomWidgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomWidget(customWidgetEntity: CustomWidgetEntity): Long

    @Query("SELECT * FROM custom_widget")
    suspend fun getAllCustomWidgets(): List<CustomWidgetEntity>

    @Query("SELECT * FROM custom_widget WHERE id = :id")
    suspend fun getCustomWidget(id: Long): CustomWidgetEntity

    @Query("DELETE FROM custom_widget WHERE id = :id")
    suspend fun deleteCustomWidget(id: Long)

    @Query("""
        UPDATE custom_widget
        SET 
            fontFamily = CASE WHEN :fontFamily IS NOT NULL THEN :fontFamily ELSE fontFamily END,
            fontSize = CASE WHEN :fontSize IS NOT NULL THEN :fontSize ELSE fontSize END,
            fontColor = CASE WHEN :fontColor IS NOT NULL THEN :fontColor ELSE fontColor END,
            backgroundImage = CASE WHEN :backgroundImage IS NOT NULL THEN :backgroundImage ELSE backgroundImage END,
            mode = CASE WHEN :mode IS NOT NULL THEN :mode ELSE mode END,
            hour = CASE WHEN :hour IS NOT NULL THEN :hour ELSE hour END,
            minute = CASE WHEN :minute IS NOT NULL THEN :minute ELSE minute END,
            second = CASE WHEN :second IS NOT NULL THEN :second ELSE second END,
            gap = CASE WHEN :gap IS NOT NULL THEN :gap ELSE gap END,
            breakTime = CASE WHEN :breakTime IS NOT NULL THEN :breakTime ELSE breakTime END,
            startSound = CASE WHEN :startSound IS NOT NULL THEN :startSound ELSE startSound END,
            breakTimeSound = CASE WHEN :breakTimeSound IS NOT NULL THEN :breakTimeSound ELSE breakTimeSound END,
            soundMode = CASE WHEN :soundMode IS NOT NULL THEN :soundMode ELSE soundMode END,
            repeat = CASE WHEN :repeat IS NOT NULL THEN :repeat ELSE repeat END,
            screenColor = CASE WHEN :screenColor IS NOT NULL THEN :screenColor ELSE screenColor END,
            fgColor = CASE WHEN :fgColor IS NOT NULL THEN :fgColor ELSE fgColor END,
            bgColor = CASE WHEN :bgColor IS NOT NULL THEN :bgColor ELSE bgColor END,
            handColor = CASE WHEN :handColor IS NOT NULL THEN :handColor ELSE handColor END,
            edgeColor = CASE WHEN :edgeColor IS NOT NULL THEN :edgeColor ELSE edgeColor END,
            bgMode = CASE WHEN :bgMode IS NOT NULL THEN :bgMode ELSE bgMode END,
            pattern = CASE WHEN :pattern IS NOT NULL THEN :pattern ELSE pattern END
        WHERE id = :id
    """)
    suspend fun updateCustomWidget(
        id: Long,
        fontFamily: FontFamily?,
        fontSize: Float?,
        fontColor: Color?,
        backgroundImage: String?,
        mode: Mode?,
        hour: Int?,
        minute: Int?,
        second: Int?,
        gap: Int?,
        breakTime: Int?,
        startSound: Int?,
        breakTimeSound: Int?,
        soundMode: SoundMode?,
        repeat: Int?,
        screenColor: Color?,
        fgColor: Color?,
        bgColor: Color?,
        handColor: Color?,
        edgeColor: Color?,
        bgMode: BgMode?,
        pattern: Int?
    )
}