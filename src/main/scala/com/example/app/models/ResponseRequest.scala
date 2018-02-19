package com.example.app.models

import com.example.app.{AppGlobals, SlickUUIDObject, UpdatableUUIDObject}
import com.example.app.db.Tables.{ChoicesRow, QuestionsRow, ResponseRequests, ResponseRequestsRow}
import com.example.app.models.Question
import akka.actor.{Actor, ActorSystem, Props}
import org.joda.time.DateTime
import AppGlobals.dbConfig.driver.api._
import com.example.app.contracts.QuestionContracts
import com.example.app.contracts.QuestionContracts.QuestionWithChoices
import com.mailjet.client.resource.Email
import com.mailjet.client.{MailjetClient, MailjetRequest}
import org.json.{JSONArray, JSONObject}

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

  def checkRequestAlreadyHasResponse(requestId: String) =
    Await.result(db.run(Response.table.filter(_.responseRequestId === requestId).result), Duration.Inf).size > 0

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

  def toReschedule = {
    val now = DateTime.now().getMillis

    val unrespondedTo = Await.result(db.run(
      (for {
        (requests, responses) <- table.filter(rr => rr.sentOutMillis.isDefined) joinLeft Response.table on (_.responseRequestId === _.responseRequestId) if responses.isEmpty
      } yield (requests)).result
    ), Duration.Inf).groupBy(_.questionId).mapValues(_.sortBy(_.sentOutMillis).last).values.toSeq

    val questionIds = unrespondedTo.map(_.questionId)

    val qsAndSs = Await.result(db.run(
      (for {
        questions <- Question.table.filter(q => q.questionId.inSet(questionIds) && q.isActive && (q.expirationMillis.isEmpty || q.expirationMillis > now))
        schedules <- Schedule.table if questions.questionId === schedules.questionId
      } yield (questions, schedules) ).result
    ), Duration.Inf)

    val schedules = qsAndSs.map(_._2).map(s => s.questionId -> s).toMap

    unrespondedTo.filter(request => {
      schedules.get(request.questionId).isDefined && request.sentOutMillis.get < (now - (2 * Schedule.pureResponseRequest(schedules(request.questionId))))
    })
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
    system.scheduler.schedule(0 milliseconds, 30 seconds, myActor, "go")
    system.scheduler.schedule(0 milliseconds, 42 seconds, myActor, "remember")
  }
}

class ResponseRequestCreator extends Actor {

  val mailjetClient = new MailjetClient(System.getenv("MAIL_JET_PUBLIC_KEY"), System.getenv("MAIL_JET_SECRET_KEY"))

  def receive = {
    case "go" =>
      val requests = ResponseRequest.toFire
      println(requests.size + " requests...")
      requests.map(sendResponseRequest)
      println("requests saved")
    case "remember" =>
      val requests = ResponseRequest.toReschedule
      println(requests.size + " reminders...")
      requests.map(sendResponseRequest)
      println("requests saved")
    case _ => println("ouch!")
  }

  def sendResponseRequest(responseRequest: ResponseRequestsRow) {

    val question = Await.result(Question.byId(responseRequest.questionId), Duration.Inf)
    val choices = Await.result(Choice.byQuestionId(question.questionId), Duration.Inf)

    val user = User.makeJson(Await.result(User.byId(question.creatorId), Duration.Inf))

    ResponseRequest.sendResponseRequest(responseRequest)
    sendEmail(question, choices, user)
  }

  //def notify

  def sendEmail(question: QuestionsRow, choices: Seq[ChoicesRow], user: UserJson) = {


    val domain = sys.env.get("DOMAIN").getOrElse(s"http://localhost:9000/")

    val questionTemplate = {
      question.questionText+"\n"+
      choices.sortBy(_.choiceOrder).map(choice => {
        "<a href='"+domain+"#/questions/"+question.questionId+"/response/"+choice.choiceId+"'>"+choice.choiceText+"</a>"
      }).mkString("\n")
    }

    val request = new MailjetRequest(Email.resource)
      //.property(Email.RECIPIENTS, "matthew.slotkin@gmail.com")
      .property(Email.SUBJECT, "Answer a question!")
      .property(Email.HTMLPART, questionTemplate)
      .property(Email.FROMEMAIL, "scheduledq@gmail.com")
      .property(Email.FROMNAME, "ScheduledQ")
      .property(Email.RECIPIENTS, new JSONArray()
        .put(new JSONObject()
          .put("Email", user.email)));

    println("sending")
    val response = mailjetClient.post(request)
    System.out.println(response.getData());
    println("sent")
  }
}

