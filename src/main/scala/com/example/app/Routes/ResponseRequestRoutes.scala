package com.example.app.Routes

import com.example.app.contracts.ResponseRequestContracts
import com.example.app.models.{Question, ResponseRequest}
import com.example.app.{AuthenticationSupport, SlickRoutes}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by matt on 9/26/17.
  */
trait ResponseRequestRoutes extends SlickRoutes with AuthenticationSupport {

  get("/questions/:id/responserequests/latest") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId
    val questionId = params("id")

    if(Question.authorizedToEdit(userId, questionId)) {
      val responseRequest = Await.result(ResponseRequest.lastActiveByQuestionId(questionId), Duration.Inf)
      responseRequest.map(ResponseRequestContracts.serialize)
    } else
      throw new Exception("Not authorized to respond to this question")
  }
}
