package com.example.app.Routes

import com.example.app.{AuthenticationSupport, SlickRoutes}
import com.example.app.contracts.QuestionContracts
import com.example.app.models.{Question, ResponseRequest}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by matt on 9/25/17.
  */
trait QuestionRoutes extends SlickRoutes with AuthenticationSupport {

  post("/questions/save") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val inputQuestion = parsedBody.extract[QuestionContracts.Input]

    val toSave = inputQuestion.question(userId)

    if(!Question.existsInDb(toSave)) {
      val savedQuestion = Question.createWithChoiceCreation(toSave)
      ResponseRequest.scheduleNextRequest(savedQuestion.question.questionId)
      savedQuestion
    } else if(Question.authorizedToEdit(userId, toSave.questionId)) {
      val question = Await.result(Question.save(toSave), Duration.Inf)
      Question.questionWithChoices(question.questionId)
    } else
      throw new Exception("Not authorized to edit this question")
  }

  get("/questions") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val questions = Question.byCreatorId(userId)
    questions.map(_.map(QuestionContracts.serialize))
  }

  get("/questions/:id") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId
    val questionId = params("id")

    if(Question.authorizedToEdit(userId, questionId)) {
      Question.questionWithChoices(questionId)
    } else
      throw new Exception("No authorized to view this question")
  }
}
