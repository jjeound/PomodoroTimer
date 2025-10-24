package com.pomodoro.timer.database.entity.mapper

import androidx.core.net.toUri
import com.pomodoro.timer.CustomWidget
import com.pomodoro.timer.database.entity.CustomWidgetEntity
import com.pomodoro.timer.presentation.common.ExpireMode

object CustomWidgetEntityMapper : EntityMapper<CustomWidget, CustomWidgetEntity> {

  override fun asEntity(domain: CustomWidget): CustomWidgetEntity {
    val expireMode = when(domain.expireMode){
        ExpireMode.NO_SOUND -> 0
        ExpireMode.VIBRATE -> 1
        ExpireMode.SOUND -> 2
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
      restartSound = domain.restartSound,
      expireMode = expireMode,
      repeat = domain.repeat,
      fgColor = domain.fgColor,
      bgColor = domain.bgColor
    )
  }

  override fun asDomain(entity: CustomWidgetEntity): CustomWidget {
    val expireMode = when(entity.expireMode){
        0 -> ExpireMode.NO_SOUND
        1 -> ExpireMode.VIBRATE
        2 -> ExpireMode.SOUND
        else -> ExpireMode.NO_SOUND
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
      restartSound = entity.restartSound,
      expireMode = expireMode,
      repeat = entity.repeat,
      fgColor = entity.fgColor,
      bgColor = entity.bgColor
    )
  }
}

fun CustomWidget.asEntity(): CustomWidgetEntity {
  return CustomWidgetEntityMapper.asEntity(this)
}

fun CustomWidgetEntity.asDomain(): CustomWidget {
  return CustomWidgetEntityMapper.asDomain(this)
}