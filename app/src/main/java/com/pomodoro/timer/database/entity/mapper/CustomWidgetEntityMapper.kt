package com.pomodoro.timer.database.entity.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pomodoro.timer.CustomWidget
import com.pomodoro.timer.database.entity.CustomWidgetEntity

object CustomWidgetEntityMapper : EntityMapper<CustomWidget, CustomWidgetEntity> {

  private val gson = Gson()

  override fun asEntity(domain: CustomWidget): CustomWidgetEntity {
    val backgroundJson = domain.backgroundImage?.let { gson.toJson(it) }

    return CustomWidgetEntity(
      fontSize = domain.fontStyle?.fontSize?.value,
      fontWeight = domain.fontStyle?.fontWeight?.weight,
      fontColor = domain.fontStyle?.color?.value?.toLong(),
      backgroundImage = backgroundJson,
      mode = domain.mode,
      hour = domain.hour,
      minute = domain.minute,
      second = domain.second,
      interval = domain.interval,
      sound = domain.sound,
      vibration = domain.vibration,
      repeat = domain.repeat
    )
  }

  override fun asDomain(entity: CustomWidgetEntity): CustomWidget {
    val backgroundList: List<String>? = entity.backgroundImage?.let {
      gson.fromJson(it, object : TypeToken<List<String>>() {}.type)
    }
    val textStyle = if (entity.fontSize != null && entity.fontWeight != null && entity.fontColor != null) {
      TextStyle(
        fontSize = entity.fontSize.sp,
        fontWeight = FontWeight(entity.fontWeight),
        color = Color(entity.fontColor.toULong())
      )
    } else null

    return CustomWidget(
      fontStyle = textStyle,
      backgroundImage = backgroundList,
      mode = entity.mode,
      hour = entity.hour,
      minute = entity.minute,
      second = entity.second,
      interval = entity.interval,
      sound = entity.sound,
      vibration = entity.vibration,
      repeat = entity.repeat
    )
  }
}

fun CustomWidget.asEntity(): CustomWidgetEntity {
  return CustomWidgetEntityMapper.asEntity(this)
}

fun CustomWidgetEntity.asDomain(): CustomWidget {
  return CustomWidgetEntityMapper.asDomain(this)
}