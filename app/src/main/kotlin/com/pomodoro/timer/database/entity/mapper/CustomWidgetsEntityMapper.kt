package com.pomodoro.timer.database.entity.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pomodoro.timer.CustomWidget
import com.pomodoro.timer.database.entity.CustomWidgetEntity

object CustomWidgetsEntityMapper : EntityMapper<List<CustomWidget>, List<CustomWidgetEntity>> {

  private val gson = Gson()

  override fun asEntity(domain: List<CustomWidget>): List<CustomWidgetEntity> {
    return domain.map { domain ->
      val backgroundJson = domain.backgroundImage?.let { gson.toJson(it) }
      CustomWidgetEntity(
        fontSize = domain.fontStyle.fontSize.value,
        fontWeight = domain.fontStyle.fontWeight!!.weight,
        fontColor = domain.fontColor.value.toLong(),
        backgroundImage = backgroundJson,
        mode = domain.mode,
        hour = domain.hour,
        minute = domain.minute,
        second = domain.second,
        interval = domain.interval,
        breakTime = domain.breakTime,
        startSound = domain.startSound,
        restartSound = domain.restartSound,
        expireMode = domain.expireMode,
        repeat = domain.repeat,
        fgColor = domain.fgColor.value.toLong(),
        bgColor = domain.bgColor.value.toLong()
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
        color = Color(entity.fontColor.toULong())
      )

      CustomWidget(
        id = entity.id,
        fontStyle = textStyle,
        backgroundImage = backgroundList,
        mode = entity.mode,
        hour = entity.hour,
        minute = entity.minute,
        second = entity.second,
        interval = entity.interval,
        breakTime = entity.breakTime,
        startSound = entity.startSound,
        restartSound = entity.restartSound,
        expireMode = entity.expireMode,
        repeat = entity.repeat,
        fgColor = Color(entity.fgColor),
        bgColor = Color(entity.bgColor)
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
