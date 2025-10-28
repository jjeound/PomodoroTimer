package com.pomodoro.timer.database.entity.mapper

import androidx.core.net.toUri
import com.pomodoro.timer.data.model.CustomWidget
import com.pomodoro.timer.database.entity.CustomWidgetEntity

object CustomWidgetsEntityMapper : EntityMapper<List<CustomWidget>, List<CustomWidgetEntity>> {


  override fun asEntity(domain: List<CustomWidget>): List<CustomWidgetEntity> {
    return domain.map { domain ->
      CustomWidgetEntity(
        textStyle = domain.textStyle,
        fontColor = domain.fontColor,
        backgroundImage = domain.backgroundImage.toString(),
        mode = domain.mode,
        hour = domain.hour,
        minute = domain.minute,
        second = domain.second,
        gap = domain.gap,
        breakTime = domain.breakTime,
        startSound = domain.startSound,
        breakTimeSound = domain.breakTimeSound,
        soundMode = domain.soundMode,
        repeat = domain.repeat,
        fgColor = domain.fgColor,
        bgColor = domain.bgColor,
        handColor = domain.handColor,
        edgeColor = domain.edgeColor,
        bgMode = domain.bgMode
      )
    }
  }

  override fun asDomain(entity: List<CustomWidgetEntity>): List<CustomWidget> {
    return entity.map { entity ->
      CustomWidget(
        id = entity.id,
        textStyle = entity.textStyle,
        fontColor = entity.fontColor,
        backgroundImage = entity.backgroundImage?.toUri(),
        mode = entity.mode,
        hour = entity.hour,
        minute = entity.minute,
        second = entity.second,
        gap = entity.gap,
        breakTime = entity.breakTime,
        startSound = entity.startSound,
        breakTimeSound = entity.breakTimeSound,
        soundMode = entity.soundMode,
        repeat = entity.repeat,
        fgColor = entity.fgColor,
        bgColor = entity.bgColor,
        handColor = entity.handColor,
        edgeColor = entity.edgeColor,
        bgMode = entity.bgMode
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
