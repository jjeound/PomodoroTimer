package com.pomodoro.timer.database.entity.mapper

import androidx.core.net.toUri
import com.pomodoro.timer.CustomWidget
import com.pomodoro.timer.database.entity.CustomWidgetEntity
import com.pomodoro.timer.presentation.common.SoundMode

object CustomWidgetEntityMapper : EntityMapper<CustomWidget, CustomWidgetEntity> {

  override fun asEntity(domain: CustomWidget): CustomWidgetEntity {
    val soundMode = when(domain.soundMode){
        SoundMode.NO_SOUND -> 0
        SoundMode.VIBRATE -> 1
        SoundMode.SOUND -> 2
    }

    return CustomWidgetEntity(
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
      soundMode = soundMode,
      repeat = domain.repeat,
      fgColor = domain.fgColor,
      bgColor = domain.bgColor,
      handColor = domain.handColor,
      edgeColor = domain.edgeColor
    )
  }

  override fun asDomain(entity: CustomWidgetEntity): CustomWidget {
    val soundMode = when(entity.soundMode){
        0 -> SoundMode.NO_SOUND
        1 -> SoundMode.VIBRATE
        2 -> SoundMode.SOUND
        else -> SoundMode.NO_SOUND
    }

    return CustomWidget(
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
      soundMode = soundMode,
      repeat = entity.repeat,
      fgColor = entity.fgColor,
      bgColor = entity.bgColor,
      handColor = entity.handColor,
      edgeColor = entity.edgeColor
    )
  }
}

fun CustomWidget.asEntity(): CustomWidgetEntity {
  return CustomWidgetEntityMapper.asEntity(this)
}

fun CustomWidgetEntity.asDomain(): CustomWidget {
  return CustomWidgetEntityMapper.asDomain(this)
}