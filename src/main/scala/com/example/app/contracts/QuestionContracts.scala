package com.example.app.contracts

import com.example.app.db.Tables.{ChoicesRow, QuestionsRow}
import com.example.app.models.QuestionType
import org.joda.time.DateTime

/**
  * Created by matt on 9/25/17.
  */
object QuestionContracts {

  case class Input(questionId: String = null, questionType: String, text: String, isActive: Boolean = true, expirationMillis: Option[Long] = None){
    require( QuestionType.valid(questionType), "must use a valid question type: "+QuestionType.all.map(_.name).mkString(", "))
    def question(creatorId: Int) = QuestionsRow(questionId, creatorId, questionType, text, isActive, expirationMillis, DateTime.now().getMillis)
  }

  case class Output(questionId: String, questionType: String, text: String, isActive: Boolean, expirationMillis: Option[Long], createdMillis: Long)

  def serialize(question: QuestionsRow) =
    Output(question.questionId, question.questionType, question.questionText, question.isActive, question.expirationMillis, question.createdMillis)

  case class QuestionWithChoices(question: Output, choices: Seq[ChoiceContracts.Output])

  def serializeQuestionWithChoices(question: QuestionsRow, choices: Seq[ChoicesRow]) =
    QuestionWithChoices(
      serialize(question),
      choices.map(ChoiceContracts.serialize)
    )
}
