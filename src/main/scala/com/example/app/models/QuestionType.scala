package com.example.app.models

import com.example.app.db.Tables.ChoicesRow

/**
  * Created by matt on 9/25/17.
  */
case class QuestionType(name: String)

object QuestionType {

  val yesNo = QuestionType("Yes / No")
  val oneToTen = QuestionType("1-10")
  val oneToFive = QuestionType("1-5")

  val all = Seq(yesNo, oneToTen, oneToFive)

  def valid(name: String) =
    all.map(_.name).contains(name)

  val byName = all.map(a => a.name -> a).toMap

  def choices(questionType: QuestionType, questionId: String) = questionType match {
    case QuestionType.yesNo => Seq(
      ChoicesRow(null, questionId, "yes", 1, 1),
      ChoicesRow(null, questionId, "no", 0, 0)
    )
    case QuestionType.oneToFive => Seq(
      ChoicesRow(null, questionId, "1", 1, 1),
      ChoicesRow(null, questionId, "2", 2, 2),
      ChoicesRow(null, questionId, "3", 3, 3),
      ChoicesRow(null, questionId, "4", 4, 4),
      ChoicesRow(null, questionId, "5", 5, 5)
    )
    case QuestionType.oneToTen => Seq(
      ChoicesRow(null, questionId, "1", 1, 1),
      ChoicesRow(null, questionId, "2", 2, 2),
      ChoicesRow(null, questionId, "3", 3, 3),
      ChoicesRow(null, questionId, "4", 4, 4),
      ChoicesRow(null, questionId, "5", 5, 5),
      ChoicesRow(null, questionId, "6", 6, 6),
      ChoicesRow(null, questionId, "7", 7, 7),
      ChoicesRow(null, questionId, "8", 8, 8),
      ChoicesRow(null, questionId, "9", 9, 9),
      ChoicesRow(null, questionId, "10", 10, 10)
    )
  }
}
