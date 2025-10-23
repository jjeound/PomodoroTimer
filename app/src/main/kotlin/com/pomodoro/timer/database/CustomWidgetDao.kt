package com.pomodoro.timer.database

import androidx.compose.ui.graphics.Color
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pomodoro.timer.database.entity.CustomWidgetEntity

@Dao
interface CustomWidgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomWidget(customWidgetEntity: CustomWidgetEntity)

    @Query("SELECT * FROM custom_widget")
    suspend fun getAllCustomWidgets(): List<CustomWidgetEntity>

    @Query("SELECT * FROM custom_widget WHERE id = :id")
    suspend fun getCustomWidget(id: Long): CustomWidgetEntity

    @Query("DELETE FROM custom_widget WHERE id = :id")
    suspend fun deleteCustomWidget(id: Long)

    @Query("""
        UPDATE custom_widget
        SET 
            fontSize = CASE WHEN :fontSize IS NOT NULL THEN :fontSize ELSE fontSize END,
            fontWeight = CASE WHEN :fontWeight IS NOT NULL THEN :fontWeight ELSE fontWeight END,
            fontColor = CASE WHEN :fontColor IS NOT NULL THEN :fontColor ELSE fontColor END,
            backgroundImage = CASE WHEN :backgroundImage IS NOT NULL THEN :backgroundImage ELSE backgroundImage END,
            mode = CASE WHEN :mode IS NOT NULL THEN :mode ELSE mode END,
            hour = CASE WHEN :hour IS NOT NULL THEN :hour ELSE hour END,
            minute = CASE WHEN :minute IS NOT NULL THEN :minute ELSE minute END,
            second = CASE WHEN :second IS NOT NULL THEN :second ELSE second END,
            gap = CASE WHEN :gap IS NOT NULL THEN :gap ELSE gap END,
            breakTime = CASE WHEN :breakTime IS NOT NULL THEN :breakTime ELSE breakTime END,
            startSound = CASE WHEN :startSound IS NOT NULL THEN :startSound ELSE startSound END,
            restartSound = CASE WHEN :restartSound IS NOT NULL THEN :restartSound ELSE restartSound END,
            expireMode = CASE WHEN :expireMode IS NOT NULL THEN :expireMode ELSE expireMode END,
            repeat = CASE WHEN :repeat IS NOT NULL THEN :repeat ELSE repeat END,
            fgColor = CASE WHEN :fgColor IS NOT NULL THEN :fgColor ELSE fgColor END,
            bgColor = CASE WHEN :bgColor IS NOT NULL THEN :bgColor ELSE bgColor END
        WHERE id = :id
    """)
    suspend fun updateCustomWidget(
        id: Long,
        fontSize: Float?,
        fontWeight: Int?,
        fontColor: String?,
        backgroundImage: String?,
        mode: Int?,
        hour: Int?,
        minute: Int?,
        second: Int?,
        gap: Int?,
        breakTime: Int?,
        startSound: String?,
        restartSound: String?,
        expireMode: Int?,
        repeat: Int?,
        fgColor: String?,
        bgColor: String?
    )
}