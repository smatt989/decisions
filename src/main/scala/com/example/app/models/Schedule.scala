package com.example.app.models

import com.example.app.{AppGlobals, UpdatableUUIDObject}
import com.example.app.db.Tables._
import AppGlobals.dbConfig.driver.api._
import org.joda.time.DateTime

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
/**
  * Created by matt on 9/25/17.
  */
object Schedule extends UpdatableUUIDObject[SchedulesRow, Schedules] {
  def updateQuery(a: _root_.com.example.app.db.Tables.SchedulesRow) = table.filter(t => idColumnFromTable(t) === idFromRow(a))
    .map(x => (x.period, x.frequency))
    .update((a.period, a.frequency))

  def table = Schedules

  def idFromRow(a: _root_.com.example.app.db.Tables.SchedulesRow) =
    a.scheduleId

  def updateId(a: _root_.com.example.app.db.Tables.SchedulesRow, id: String) =
    a.copy(scheduleId = id)

  def idColumnFromTable(a: _root_.com.example.app.db.Tables.Schedules) =
    a.scheduleId

  def byQuestionId(questionId: String) =
    Await.result(db.run(table.filter(_.questionId === questionId).result), Duration.Inf).headOption

  def nextResponseRequest(schedule: SchedulesRow) = {
    val periodMillis = PeriodType.periodMillis(PeriodType.byName(schedule.period))
    val averageInterval = periodMillis.toDouble / schedule.frequency

    val noiseMultiplier = math.min(
      math.max(
        scala.util.Random.nextGaussian() * 0.25 + 1,
        0.25),
      1.75)

    println("noise multiplier: "+noiseMultiplier)
    println("average interval: "+averageInterval)

    val lastCompletedRequestResponse = Await.result(ResponseRequest.lastCompletedRequestResponse(schedule.questionId), Duration.Inf)

    lastCompletedRequestResponse.map(_.createdMillis).getOrElse(new DateTime().getMillis) + (averageInterval * noiseMultiplier)
  }
}
