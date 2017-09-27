package com.example.app.contracts

import com.example.app.db.Tables.SchedulesRow
import com.example.app.models.PeriodType

/**
  * Created by matt on 9/26/17.
  */
object ScheduleContracts {
  case class Input(period: String, frequency: Int) {
    require( PeriodType.valid(period), "must use a valid period type: "+PeriodType.all.map(_.name).mkString(", "))
    require( frequency > 0, "frequency must be greater than 0" )
    def schedule(questionId: String, scheduleId: String) = SchedulesRow(scheduleId, questionId, period, frequency)
  }

  case class Output(period: String, frequency: Int)

  def serialize(schedule: SchedulesRow) =
    Output(schedule.period, schedule.frequency)
}
