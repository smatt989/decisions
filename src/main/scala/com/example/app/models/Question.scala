package com.example.app.models

import com.example.app.{AppGlobals, UpdatableUUIDObject}
import com.example.app.db.Tables._
import AppGlobals.dbConfig.driver.api._
import com.example.app.contracts.{ChoiceContracts, QuestionContracts}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration
/**
  * Created by matt on 9/25/17.
  */
object Question extends UpdatableUUIDObject[QuestionsRow, Questions] {
  def updateQuery(a: _root_.com.example.app.db.Tables.QuestionsRow) = table.filter(t => idColumnFromTable(t) === idFromRow(a))
    .map(x => (x.questionText, x.isActive, x.expirationMillis))
    .update((a.questionText, a.isActive, a.expirationMillis))

  def table = Questions

  def idFromRow(a: _root_.com.example.app.db.Tables.QuestionsRow) =
    a.questionId

  def updateId(a: _root_.com.example.app.db.Tables.QuestionsRow, id: String) =
    a.copy(questionId = id)

  def idColumnFromTable(a: _root_.com.example.app.db.Tables.Questions) =
    a.questionId

  def byCreatorId(userId: Int) =
    db.run(table.filter(_.creatorId === userId).result)

  def createWithChoiceCreation(question: QuestionsRow) = {
    val createdQuestion = Await.result(create(question), Duration.Inf)
    val questionType = QuestionType.byName(createdQuestion.questionType)
    val questionId = createdQuestion.questionId
    val choices = QuestionType.choices(questionType, questionId)

    val createdChoices = Await.result(Choice.createMany(choices), Duration.Inf)

    QuestionContracts.serializeQuestionWithChoices(createdQuestion, createdChoices)
  }

  def questionWithChoices(questionId: String) = {
    val question = Await.result(byId(questionId), Duration.Inf)
    val choices = Await.result(Choice.byQuestionId(question.questionId), Duration.Inf)
    QuestionContracts.serializeQuestionWithChoices(question, choices)
  }

  def authorizedToEdit(userId: Int, questionId: String) =
    isCreatorOfQuestion(userId, questionId)

  def isCreatorOfQuestion(userId: Int, questionId: String) =
    Await.result(byId(questionId).map(_.creatorId == userId), Duration.Inf)

}
