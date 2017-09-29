package com.example.app.models

import com.example.app.{AppGlobals, UpdatableUUIDObject}
import com.example.app.db.Tables._
import AppGlobals.dbConfig.driver.api._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by matt on 9/25/17.
  */
object Response extends UpdatableUUIDObject[ResponsesRow, Responses] {
  def updateQuery(a: _root_.com.example.app.db.Tables.ResponsesRow) = table.filter(t => idColumnFromTable(t) === idFromRow(a))
    .map(x => (x.choiceId, x.createdMillis))
    .update((a.choiceId, a.createdMillis))

  def table = Responses

  def idFromRow(a: _root_.com.example.app.db.Tables.ResponsesRow) =
    a.responseId

  def updateId(a: _root_.com.example.app.db.Tables.ResponsesRow, id: String) =
    a.copy(responseId = id)

  def idColumnFromTable(a: _root_.com.example.app.db.Tables.Responses) =
    a.responseId

  def byQuestionId(questionId: String) =
    db.run(table.filter(_.questionId === questionId).result)
}
