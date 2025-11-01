package com.pomodoro.timer.database

import androidx.compose.ui.graphics.Color
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pomodoro.timer.database.entity.ColorEntity

@Dao
interface ColorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColor(colorEntity: ColorEntity)

    @Query("SELECT * FROM color_table")
    suspend fun getAllColors(): List<ColorEntity>

    @Query("DELETE FROM color_table WHERE color = :color")
    suspend fun deleteColor(color: Color)

    @Query("UPDATE color_table SET color = :newColor WHERE color = :oldColor")
    suspend fun updateColor(oldColor: Color, newColor: Color)
}