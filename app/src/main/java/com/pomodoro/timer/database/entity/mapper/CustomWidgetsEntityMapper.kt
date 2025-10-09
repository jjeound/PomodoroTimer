package com.pomodoro.timer.database.entity.mapper

import com.pomodoro.timer.CustomWidget
import com.pomodoro.timer.database.entity.CustomWidgetEntity

object CustomWidgetsEntityMapper : EntityMapper<List<CustomWidget>, List<CustomWidgetEntity>> {

  override fun asEntity(domain: List<CustomWidget>): List<CustomWidgetEntity> {
    return domain.map { customWidget ->
      CustomWidgetEntity()
    }
  }

  override fun asDomain(entity: List<CustomWidgetEntity>): List<CustomWidget> {
    return entity.map { customWidgetEntity ->
      CustomWidget()
    }
  }
}

fun List<CustomWidget>.asEntity(): List<CustomWidgetEntity> {
  return CustomWidgetsEntityMapper.asEntity(this)
}

fun List<CustomWidgetEntity>?.asDomain(): List<CustomWidget> {
  return CustomWidgetsEntityMapper.asDomain(this.orEmpty())
}
