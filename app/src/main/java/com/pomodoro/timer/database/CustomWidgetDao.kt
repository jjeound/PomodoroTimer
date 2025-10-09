package com.pomodoro.timer.database

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
}