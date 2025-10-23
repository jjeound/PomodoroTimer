package com.pomodoro.timer.database.entity.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pomodoro.timer.CustomWidget
import com.pomodoro.timer.database.entity.CustomWidgetEntity
import com.pomodoro.timer.presentation.common.ExpireMode

object CustomWidgetsEntityMapper : EntityMapper<List<CustomWidget>, List<CustomWidgetEntity>> {

  private val gson = Gson()

  override fun asEntity(domain: List<CustomWidget>): List<CustomWidgetEntity> {
    return domain.map { domain ->
      val backgroundJson = domain.backgroundImage?.let { gson.toJson(it) }
      val expireMode = when(domain.expireMode){
          ExpireMode.NO_SOUND -> 0
          ExpireMode.VIBRATE -> 1
          ExpireMode.SOUND -> 2
      }
      CustomWidgetEntity(
        fontSize = domain.fontStyle.fontSize.value,
        fontWeight = domain.fontStyle.fontWeight!!.weight,
        fontColor = domain.fontColor.toArgb().toString(16),
        backgroundImage = backgroundJson,
        mode = domain.mode,
        hour = domain.hour,
        minute = domain.minute,
        second = domain.second,
        gap = domain.gap,
        breakTime = domain.breakTime,
        startSound = domain.startSound,
        restartSound = domain.restartSound,
        expireMode = expireMode,
        repeat = domain.repeat,
        fgColor = domain.fgColor.toArgb().toString(16),
        bgColor = domain.bgColor.toArgb().toString(16)
      )
    }
  }

  override fun asDomain(entity: List<CustomWidgetEntity>): List<CustomWidget> {
    return entity.map { entity ->
      val backgroundList: List<String>? = entity.backgroundImage?.let {
        gson.fromJson(it, object : TypeToken<List<String>>() {}.type)
      }
      val textStyle = TextStyle(
        fontSize = entity.fontSize.sp,
        fontWeight = FontWeight(entity.fontWeight),
        color = Color(entity.fontColor.toLong(16))
      )
      val expireMode = when(entity.expireMode){
          0 -> ExpireMode.NO_SOUND
          1 -> ExpireMode.VIBRATE
          2 -> ExpireMode.SOUND
          else -> ExpireMode.NO_SOUND
      }

      CustomWidget(
        id = entity.id,
        fontStyle = textStyle,
        backgroundImage = backgroundList,
        mode = entity.mode,
        hour = entity.hour,
        minute = entity.minute,
        second = entity.second,
        gap = entity.gap,
        breakTime = entity.breakTime,
        startSound = entity.startSound,
        restartSound = entity.restartSound,
        expireMode = expireMode,
        repeat = entity.repeat,
        fgColor = Color(entity.fgColor.toLong(16)),
        bgColor = Color(entity.bgColor.toLong(16))
      )
    }
  }
}

fun List<CustomWidget>.asEntity(): List<CustomWidgetEntity> {
  return CustomWidgetsEntityMapper.asEntity(this)
}

fun List<CustomWidgetEntity>?.asDomain(): List<CustomWidget> {
  return CustomWidgetsEntityMapper.asDomain(this.orEmpty())
}
