package com.example.app.Routes

import com.example.app.contracts.ScheduleContracts
import com.example.app.models.{PeriodType, Question, ResponseRequest, Schedule}
import com.example.app.{AuthenticationSupport, SlickRoutes}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by matt on 9/26/17.
  */
trait ScheduleRoutes extends SlickRoutes with AuthenticationSupport {

  post("/questions/:id/schedules/save") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId
    val questionId = params("id")

    val scheduleInput = parsedBody.extract[ScheduleContracts.Input]

    if(Question.authorizedToEdit(userId, questionId)) {
      val preexistingSchedule = Schedule.byQuestionId(questionId)
      val schedule = scheduleInput.schedule(questionId, preexistingSchedule.map(_.scheduleId).getOrElse(null))
      val saved = Await.result(Schedule.save(schedule), Duration.Inf)
      ResponseRequest.scheduleNextRequest(questionId)
      ScheduleContracts.serialize(saved)
    } else
      throw new Exception("Not authorized to edit this question")
  }

  get("/questions/:id/schedules") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId
    val questionId = params("id")

    if(Question.authorizedToEdit(userId, questionId)) {
      val schedule = Schedule.byQuestionId(questionId)
      schedule.map(ScheduleContracts.serialize)
    } else
      throw new Exception("Not authorized to view this question")
  }

  get("/periodtypes") {
    contentType = formats("json")

    PeriodType.all
  }
}
