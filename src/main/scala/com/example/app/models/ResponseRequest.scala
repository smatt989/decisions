package com.example.app.models

import com.example.app.{AppGlobals, SlickUUIDObject, UpdatableUUIDObject}
import com.example.app.db.Tables.{ResponseRequests, ResponseRequestsRow}
import com.example.app.models.Question
import akka.actor.{Actor, ActorSystem, Props}
import org.joda.time.DateTime
import AppGlobals.dbConfig.driver.api._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by matt on 9/25/17.
  */
object ResponseRequest extends UpdatableUUIDObject[ResponseRequestsRow, ResponseRequests] {

  def updateQuery(a: _root_.com.example.app.db.Tables.ResponseRequestsRow) = table.filter(t => idColumnFromTable(t) === idFromRow(a))
    .map(x => (x.sentOutMillis))
    .update((a.sentOutMillis))


  def table = ResponseRequests

  def idFromRow(a: _root_.com.example.app.db.Tables.ResponseRequestsRow) =
    a.responseRequestId

  def updateId(a: _root_.com.example.app.db.Tables.ResponseRequestsRow, id: String) =
    a.copy(responseRequestId = id)

  def idColumnFromTable(a: _root_.com.example.app.db.Tables.ResponseRequests) =
    a.responseRequestId

  def lastActiveByQuestionId(questionId: String)  = {
    db.run(
      (for {
        (requests, responses) <- table.filter(rr => rr.sentOutMillis.isDefined && rr.questionId === questionId) joinLeft Response.table on (_.responseRequestId === _.responseRequestId) if responses.isEmpty
      } yield (requests)).take(1).result.headOption
    )
  }

  def lastCompletedRequestResponse(questionId: String) =
    db.run(
      (for {
        responseRequests <- table.filter(_.questionId === questionId)
        responses <- Response.table if responses.responseRequestId === responseRequests.responseRequestId
      } yield (responses)).sortBy(_.createdMillis.desc).take(1).result.headOption
    )

  def toFire = {
    println("firing...?")
    val nowMillis = new DateTime().getMillis

    val requests = Await.result(db.run(
      (for {
        questions <- Question.table.filter(q => q.isActive && (q.expirationMillis.isEmpty || q.expirationMillis > nowMillis))
        requestResponses <- table.filter(r => r.sentOutMillis.isEmpty && r.scheduledForMillis <= nowMillis) if requestResponses.questionId === questions.questionId
      } yield requestResponses).result
    ), Duration.Inf)

    println("this many requests: "+requests.size)

    requests.groupBy(_.questionId).mapValues(_.sortBy(_.scheduledForMillis).last).values.toSeq
  }

  def sendResponseRequest(responseRequest: ResponseRequestsRow) = {
    val updated = responseRequest.copy(sentOutMillis = Some(new DateTime().getMillis))
    save(updated)
  }

  def scheduleNextRequest(questionId: String) = {
    val schedule = Schedule.byQuestionId(questionId)
    if(schedule.isDefined){
      val nextScheduledTime = Schedule.nextResponseRequest(schedule.get)
      val timeMillis = math.max(nextScheduledTime, new DateTime().getMillis)
      val millisFromNow = timeMillis - new DateTime().getMillis
      println("response request scheduled for "+millisFromNow.toLong+" millis ("+(millisFromNow / (1000 * 60 * 60))+" hours)")

      scheduleResponseRequest(timeMillis.toLong, questionId)
    }
  }

  def scheduleResponseRequest(timeMillis: Long, questionId: String) = {
    val toCreate = ResponseRequestsRow(null, questionId, timeMillis)
    Await.result(db.run(removeUnsentResponsesQuery(questionId)), Duration.Inf)
    create(toCreate)
    //db.run(DBIO.seq(removeUnsentResponsesQuery(questionId), createQuery(Seq(toCreate))).transactionally)
  }

  def removeUnsentResponsesQuery(questionId: String) =
    table.filter(r => r.questionId === questionId && r.sentOutMillis.isEmpty).delete

  def createResponseRequestLater(millisFromNow: Long, questionId: String) = {
    system.scheduler.scheduleOnce(millisFromNow milliseconds) {
      myActor ! questionId
    }
  }

  val system = ActorSystem()
  val myActor = system.actorOf(Props[ResponseRequestCreator])

  def startupResponseRequestCreator = {
    system.scheduler.schedule(0 milliseconds, 1000 * 5 milliseconds, myActor, "go")
  }
}

class ResponseRequestCreator extends Actor {

  def receive = {
    case "go" =>
      println("pinging db...")
      val requests = ResponseRequest.toFire
      println(requests.size + " requests...")
      requests.map(ResponseRequest.sendResponseRequest)
      println("requests saved")
    case _ => println("ouch!")
  }

}

