package com.example.app.models

import com.example.app.{AppGlobals, UpdatableUUIDObject}
import com.example.app.db.Tables._
import AppGlobals.dbConfig.driver.api._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by matt on 9/25/17.
  */
object Choice extends UpdatableUUIDObject[ChoicesRow, Choices] {
  def updateQuery(a: _root_.com.example.app.db.Tables.ChoicesRow) = table.filter(t => idColumnFromTable(t) === idFromRow(a))
    .map(x => (x.choiceText))
    .update((a.choiceText))

  def table = Choices

  def idFromRow(a: _root_.com.example.app.db.Tables.ChoicesRow) =
    a.choiceId

  def updateId(a: _root_.com.example.app.db.Tables.ChoicesRow, id: String) =
    a.copy(choiceId = id)

  def idColumnFromTable(a: _root_.com.example.app.db.Tables.Choices) =
    a.choiceId

  def byQuestionId(questionId: String) =
    db.run(table.filter(_.questionId === questionId).result)
}
