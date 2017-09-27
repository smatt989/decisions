package com.example.app.Routes

import com.example.app.models.{Choice, Question, Response, ResponseRequest}
import com.example.app.{AuthenticationSupport, SlickRoutes}
import com.example.app.contracts.ResponseContracts
import com.example.app.db.Tables.ResponsesRow

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by matt on 9/26/17.
  */
trait ResponseRoutes extends SlickRoutes with AuthenticationSupport {

  post("/questions/:id/responses/create") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId
    val questionId = params("id")

    var toCreate = parsedBody.extract[ResponseContracts.Input].response(questionId)

    if(Question.authorizedToEdit(userId, questionId)) {

      if(toCreate.responseRequestId.isEmpty) {
        val openResponseRequest = Await.result(ResponseRequest.lastActiveByQuestionId(questionId), Duration.Inf)
        if (openResponseRequest.isDefined)
          toCreate = toCreate.copy(responseRequestId = Some(openResponseRequest.get.responseRequestId))
      }

      val savedResponse = Await.result(Response.create(toCreate), Duration.Inf)

      if(savedResponse.responseRequestId.isDefined)
        ResponseRequest.scheduleNextRequest(questionId)

      val choice = Await.result(Choice.byId(savedResponse.choiceId), Duration.Inf)
      ResponseContracts.serialize(choice, savedResponse)
    } else
      throw new Exception("Not authorized to respond to this question")
  }

  get("/questions/:id/responses") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId
    val questionId = params("id")

    if(Question.authorizedToEdit(userId, questionId)) {
      val responses = Await.result(Response.byQuestionId(questionId), Duration.Inf)
      val choices = Await.result(Choice.byQuestionId(questionId), Duration.Inf)
      val choicesById = choices.map(c => c.choiceId -> c).toMap

      responses.sortBy(_.createdMillis).map(r => ResponseContracts.serialize(choicesById(r.choiceId), r))
    } else
      throw new Exception("Not authorized to see responses to this question")
  }
}
