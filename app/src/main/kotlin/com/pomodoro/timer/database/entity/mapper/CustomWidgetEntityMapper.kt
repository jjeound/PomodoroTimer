package com.pomodoro.timer.database.entity.mapper

import com.pomodoro.timer.data.model.CustomWidget
import com.pomodoro.timer.database.entity.CustomWidgetEntity

object CustomWidgetEntityMapper : EntityMapper<CustomWidget, CustomWidgetEntity> {

  override fun asEntity(domain: CustomWidget): CustomWidgetEntity {

    return CustomWidgetEntity(
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
      screenColor =  domain.screenColor,
      fgColor = domain.fgColor,
      bgColor = domain.bgColor,
      handColor = domain.handColor,
      edgeColor = domain.edgeColor,
      bgMode = domain.bgMode
    )
  }

  override fun asDomain(entity: CustomWidgetEntity): CustomWidget {
    return CustomWidget(
      id = entity.id,
      fontFamily = entity.fontFamily,
      fontSize =  entity.fontSize,
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
      bgMode = entity.bgMode
    )
  }
}

fun CustomWidget.asEntity(): CustomWidgetEntity {
  return CustomWidgetEntityMapper.asEntity(this)
}

fun CustomWidgetEntity.asDomain(): CustomWidget {
  return CustomWidgetEntityMapper.asDomain(this)
}