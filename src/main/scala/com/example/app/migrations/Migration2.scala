package com.example.app.migrations

import com.example.app.AppGlobals
import AppGlobals.dbConfig.driver.api._
import com.example.app.db.Tables
object Migration2 extends Migration {

  val id = 2

  class Questions(tag: Tag) extends Table[(String, Int, String, String, Boolean, Option[Long], Long)](tag, Some(InitDB.SCHEMA_NAME), "QUESTIONS") {
    def id = column[String]("QUESTION_ID", O.PrimaryKey)
    def creatorId = column[Int]("CREATOR_ID")
    def questionType = column[String]("QUESTION_TYPE")
    def text = column[String]("QUESTION_TEXT")
    def isActive = column[Boolean]("IS_ACTIVE")
    def expirationMillis = column[Option[Long]]("EXPIRATION_MILLIS")
    def createdMillis = column[Long]("CREATED_MILLIS")

    def * = (id, creatorId, questionType, text, isActive, expirationMillis, createdMillis)

    def creator = foreignKey("QUESTIONS_TO_USERS_FK", creatorId,  Migration1.users)(_.id)
  }

  class Choices(tag: Tag) extends Table[(String, String, String, Int, Int)](tag, Some(InitDB.SCHEMA_NAME), "CHOICES") {
    def id = column[String]("CHOICE_ID", O.PrimaryKey)
    def questionId = column[String]("QUESTION_ID")
    def text = column[String]("CHOICE_TEXT")
    def order = column[Int]("CHOICE_ORDER")
    def value = column[Int]("CHOICE_VALUE")

    def * = (id, questionId, text, order, value)

    def question = foreignKey("CHOICES_TO_QUESTION_FK", questionId, questions)(_.id)
  }

  class ResponseRequests(tag: Tag) extends Table[(String, String, Long, Option[Long])](tag, Some(InitDB.SCHEMA_NAME), "RESPONSE_REQUESTS") {
    def id = column[String]("RESPONSE_REQUEST_ID", O.PrimaryKey)
    def questionId = column[String]("QUESTION_ID")
    def scheduledForMillis = column[Long]("SCHEDULED_FOR_MILLIS")
    def sentOutMillis = column[Option[Long]]("SENT_OUT_MILLIS")

    def * = (id, questionId, scheduledForMillis, sentOutMillis)

    def question = foreignKey("RESPONSE_REQUESTS_TO_QUESTIONS_FK", questionId, questions)(_.id)
  }

  class Responses(tag: Tag) extends Table[(String, String, String, Option[String], Long)](tag, Some(InitDB.SCHEMA_NAME), "RESPONSES") {
    def id = column[String]("RESPONSE_ID", O.PrimaryKey)
    def choiceId = column[String]("CHOICE_ID")
    def questionId = column[String]("QUESTION_ID")
    def responseRequestId = column[Option[String]]("RESPONSE_REQUEST_ID")
    def createdMillis = column[Long]("CREATED_MILLIS")

    def * = (id, choiceId, questionId, responseRequestId, createdMillis)

    def choice = foreignKey("RESPONSES_TO_CHOICE_FK", choiceId, choices)(_.id)
    def responseRequest = foreignKey("RESPONSES_TO_RESPONSE_REQUEST_FK", responseRequestId, responseRequests)(_.id)
    def question = foreignKey("RESPONSES_TO_QUESTION_FK", questionId, questions)(_.id)
  }

  class Schedules(tag: Tag) extends Table[(String, String, String, Int)](tag, Some(InitDB.SCHEMA_NAME), "SCHEDULES") {
    def id = column[String]("SCHEDULE_ID", O.PrimaryKey)
    def questionId = column[String]("QUESTION_ID")
    def period = column[String]("PERIOD")
    def frequency = column[Int]("FREQUENCY")

    def * = (id, questionId, period, frequency)

    def question = foreignKey("SCHEDULES_TO_QUESTION_FK", questionId, questions)(_.id)
  }


  val questions = TableQuery[Questions]
  val choices = TableQuery[Choices]
  val responses = TableQuery[Responses]
  val responseRequests = TableQuery[ResponseRequests]
  val schedules = TableQuery[Schedules]

  def query = (questions.schema ++ choices.schema ++ responses.schema ++ responseRequests.schema ++ schedules.schema).create
}