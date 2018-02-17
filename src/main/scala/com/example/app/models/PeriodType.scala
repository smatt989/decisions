package com.example.app.models

/**
  * Created by matt on 9/26/17.
  */
case class PeriodType(name: String)

object PeriodType {

  val day = PeriodType("day")
  val week = PeriodType("week")
  val month = PeriodType("month")
  val year = PeriodType("year")

  val all = Seq(day, week, month, year)

  def valid(name: String) =
    all.map(_.name).contains(name)

  val byName = all.map(a => a.name -> a).toMap

  val dayMillis = 1000 * 60 * 60 * 24
  val weekMillis = 7 * dayMillis
  val monthMillis = 30 * dayMillis
  val yearMillis = 12 * monthMillis

  def periodMillis(periodType: PeriodType) =
    periodType match {
      case PeriodType.day   =>  dayMillis
      case PeriodType.week  =>  weekMillis
      case PeriodType.month =>  monthMillis
      case PeriodType.year  =>  yearMillis
    }
}
