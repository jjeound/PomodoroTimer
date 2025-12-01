package com.pomodoro.timer.database.entity.mapper

import com.pomodoro.timer.data.model.CustomWidget
import com.pomodoro.timer.database.entity.CustomWidgetEntity

object CustomWidgetsEntityMapper : EntityMapper<List<CustomWidget>, List<CustomWidgetEntity>> {


  override fun asEntity(domain: List<CustomWidget>): List<CustomWidgetEntity> {
    return domain.map { domain ->
      CustomWidgetEntity(
        fontFamily = domain.fontFamily,
        fontSize = domain.fontSize,
        fontColor = domain.fontColor,
        backgroundImage = domain.backgroundImage,
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
        screenColor = domain.screenColor,
        fgColor = domain.fgColor,
        bgColor = domain.bgColor,
        handColor = domain.handColor,
        edgeColor = domain.edgeColor,
        bgMode = domain.bgMode,
          pattern = domain.pattern,
      )
    }
  }

  override fun asDomain(entity: List<CustomWidgetEntity>): List<CustomWidget> {
    return entity.map { entity ->
      CustomWidget(
        id = entity.id,
        fontFamily = entity.fontFamily,
        fontSize = entity.fontSize,
        fontColor = entity.fontColor,
        backgroundImage = entity.backgroundImage,
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
        screenColor = entity.screenColor,
        fgColor = entity.fgColor,
        bgColor = entity.bgColor,
        handColor = entity.handColor,
        edgeColor = entity.edgeColor,
        bgMode = entity.bgMode,
          pattern = entity.pattern
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
