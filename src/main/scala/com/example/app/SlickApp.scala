package com.example.app

import com.example.app.Routes._
import com.example.app.models.ResponseRequest
import org.scalatra.{FutureSupport, ScalatraServlet}


class SlickApp() extends ScalatraServlet with FutureSupport
  with UserRoutes
  with SessionRoutes
  with AppRoutes
  with QuestionRoutes
  with ResponseRequestRoutes
  with ResponseRoutes
  with ScheduleRoutes {

  def db = AppGlobals.db

  lazy val realm = "rekki"

  protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

  ResponseRequest.startupResponseRequestCreator
}