package com.example.app.db
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.PostgresDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(Choices.schema, DeviceTokens.schema, Migrations.schema, Questions.schema, ResponseRequests.schema, Responses.schema, Schedules.schema, UserAccounts.schema, UserConnections.schema, UserSessions.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Choices
   *  @param choiceId Database column CHOICE_ID SqlType(VARCHAR), PrimaryKey
   *  @param questionId Database column QUESTION_ID SqlType(VARCHAR)
   *  @param choiceText Database column CHOICE_TEXT SqlType(VARCHAR)
   *  @param choiceOrder Database column CHOICE_ORDER SqlType(INTEGER)
   *  @param choiceValue Database column CHOICE_VALUE SqlType(INTEGER) */
  case class ChoicesRow(choiceId: String, questionId: String, choiceText: String, choiceOrder: Int, choiceValue: Int)
  /** GetResult implicit for fetching ChoicesRow objects using plain SQL queries */
  implicit def GetResultChoicesRow(implicit e0: GR[String], e1: GR[Int]): GR[ChoicesRow] = GR{
    prs => import prs._
    ChoicesRow.tupled((<<[String], <<[String], <<[String], <<[Int], <<[Int]))
  }
  /** Table description of table CHOICES. Objects of this class serve as prototypes for rows in queries. */
  class Choices(_tableTag: Tag) extends Table[ChoicesRow](_tableTag, Some("DECISIONS"), "CHOICES") {
    def * = (choiceId, questionId, choiceText, choiceOrder, choiceValue) <> (ChoicesRow.tupled, ChoicesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(choiceId), Rep.Some(questionId), Rep.Some(choiceText), Rep.Some(choiceOrder), Rep.Some(choiceValue)).shaped.<>({r=>import r._; _1.map(_=> ChoicesRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column CHOICE_ID SqlType(VARCHAR), PrimaryKey */
    val choiceId: Rep[String] = column[String]("CHOICE_ID", O.PrimaryKey)
    /** Database column QUESTION_ID SqlType(VARCHAR) */
    val questionId: Rep[String] = column[String]("QUESTION_ID")
    /** Database column CHOICE_TEXT SqlType(VARCHAR) */
    val choiceText: Rep[String] = column[String]("CHOICE_TEXT")
    /** Database column CHOICE_ORDER SqlType(INTEGER) */
    val choiceOrder: Rep[Int] = column[Int]("CHOICE_ORDER")
    /** Database column CHOICE_VALUE SqlType(INTEGER) */
    val choiceValue: Rep[Int] = column[Int]("CHOICE_VALUE")

    /** Foreign key referencing Questions (database name CHOICES_TO_QUESTION_FK) */
    lazy val questionsFk = foreignKey("CHOICES_TO_QUESTION_FK", questionId, Questions)(r => r.questionId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Choices */
  lazy val Choices = new TableQuery(tag => new Choices(tag))

  /** Entity class storing rows of table DeviceTokens
   *  @param deviceTokenId Database column DEVICE_TOKEN_ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param userId Database column USER_ID SqlType(INTEGER)
   *  @param deviceToken Database column DEVICE_TOKEN SqlType(VARCHAR), Default(None) */
  case class DeviceTokensRow(deviceTokenId: Int, userId: Int, deviceToken: Option[String] = None)
  /** GetResult implicit for fetching DeviceTokensRow objects using plain SQL queries */
  implicit def GetResultDeviceTokensRow(implicit e0: GR[Int], e1: GR[Option[String]]): GR[DeviceTokensRow] = GR{
    prs => import prs._
    DeviceTokensRow.tupled((<<[Int], <<[Int], <<?[String]))
  }
  /** Table description of table DEVICE_TOKENS. Objects of this class serve as prototypes for rows in queries. */
  class DeviceTokens(_tableTag: Tag) extends Table[DeviceTokensRow](_tableTag, Some("DECISIONS"), "DEVICE_TOKENS") {
    def * = (deviceTokenId, userId, deviceToken) <> (DeviceTokensRow.tupled, DeviceTokensRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(deviceTokenId), Rep.Some(userId), deviceToken).shaped.<>({r=>import r._; _1.map(_=> DeviceTokensRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column DEVICE_TOKEN_ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val deviceTokenId: Rep[Int] = column[Int]("DEVICE_TOKEN_ID", O.AutoInc, O.PrimaryKey)
    /** Database column USER_ID SqlType(INTEGER) */
    val userId: Rep[Int] = column[Int]("USER_ID")
    /** Database column DEVICE_TOKEN SqlType(VARCHAR), Default(None) */
    val deviceToken: Rep[Option[String]] = column[Option[String]]("DEVICE_TOKEN", O.Default(None))

    /** Foreign key referencing UserAccounts (database name DEVICE_TOKENS_TO_USER_FK) */
    lazy val userAccountsFk = foreignKey("DEVICE_TOKENS_TO_USER_FK", userId, UserAccounts)(r => r.userAccountId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table DeviceTokens */
  lazy val DeviceTokens = new TableQuery(tag => new DeviceTokens(tag))

  /** Entity class storing rows of table Migrations
   *  @param migrationId Database column MIGRATION_ID SqlType(INTEGER), PrimaryKey */
  case class MigrationsRow(migrationId: Int)
  /** GetResult implicit for fetching MigrationsRow objects using plain SQL queries */
  implicit def GetResultMigrationsRow(implicit e0: GR[Int]): GR[MigrationsRow] = GR{
    prs => import prs._
    MigrationsRow(<<[Int])
  }
  /** Table description of table MIGRATIONS. Objects of this class serve as prototypes for rows in queries. */
  class Migrations(_tableTag: Tag) extends Table[MigrationsRow](_tableTag, Some("DECISIONS"), "MIGRATIONS") {
    def * = migrationId <> (MigrationsRow, MigrationsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = Rep.Some(migrationId).shaped.<>(r => r.map(_=> MigrationsRow(r.get)), (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column MIGRATION_ID SqlType(INTEGER), PrimaryKey */
    val migrationId: Rep[Int] = column[Int]("MIGRATION_ID", O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table Migrations */
  lazy val Migrations = new TableQuery(tag => new Migrations(tag))

  /** Entity class storing rows of table Questions
   *  @param questionId Database column QUESTION_ID SqlType(VARCHAR), PrimaryKey
   *  @param creatorId Database column CREATOR_ID SqlType(INTEGER)
   *  @param questionType Database column QUESTION_TYPE SqlType(VARCHAR)
   *  @param questionText Database column QUESTION_TEXT SqlType(VARCHAR)
   *  @param isActive Database column IS_ACTIVE SqlType(BOOLEAN)
   *  @param expirationMillis Database column EXPIRATION_MILLIS SqlType(BIGINT), Default(None)
   *  @param createdMillis Database column CREATED_MILLIS SqlType(BIGINT) */
  case class QuestionsRow(questionId: String, creatorId: Int, questionType: String, questionText: String, isActive: Boolean, expirationMillis: Option[Long] = None, createdMillis: Long)
  /** GetResult implicit for fetching QuestionsRow objects using plain SQL queries */
  implicit def GetResultQuestionsRow(implicit e0: GR[String], e1: GR[Int], e2: GR[Boolean], e3: GR[Option[Long]], e4: GR[Long]): GR[QuestionsRow] = GR{
    prs => import prs._
    QuestionsRow.tupled((<<[String], <<[Int], <<[String], <<[String], <<[Boolean], <<?[Long], <<[Long]))
  }
  /** Table description of table QUESTIONS. Objects of this class serve as prototypes for rows in queries. */
  class Questions(_tableTag: Tag) extends Table[QuestionsRow](_tableTag, Some("DECISIONS"), "QUESTIONS") {
    def * = (questionId, creatorId, questionType, questionText, isActive, expirationMillis, createdMillis) <> (QuestionsRow.tupled, QuestionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(questionId), Rep.Some(creatorId), Rep.Some(questionType), Rep.Some(questionText), Rep.Some(isActive), expirationMillis, Rep.Some(createdMillis)).shaped.<>({r=>import r._; _1.map(_=> QuestionsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column QUESTION_ID SqlType(VARCHAR), PrimaryKey */
    val questionId: Rep[String] = column[String]("QUESTION_ID", O.PrimaryKey)
    /** Database column CREATOR_ID SqlType(INTEGER) */
    val creatorId: Rep[Int] = column[Int]("CREATOR_ID")
    /** Database column QUESTION_TYPE SqlType(VARCHAR) */
    val questionType: Rep[String] = column[String]("QUESTION_TYPE")
    /** Database column QUESTION_TEXT SqlType(VARCHAR) */
    val questionText: Rep[String] = column[String]("QUESTION_TEXT")
    /** Database column IS_ACTIVE SqlType(BOOLEAN) */
    val isActive: Rep[Boolean] = column[Boolean]("IS_ACTIVE")
    /** Database column EXPIRATION_MILLIS SqlType(BIGINT), Default(None) */
    val expirationMillis: Rep[Option[Long]] = column[Option[Long]]("EXPIRATION_MILLIS", O.Default(None))
    /** Database column CREATED_MILLIS SqlType(BIGINT) */
    val createdMillis: Rep[Long] = column[Long]("CREATED_MILLIS")

    /** Foreign key referencing UserAccounts (database name QUESTIONS_TO_USERS_FK) */
    lazy val userAccountsFk = foreignKey("QUESTIONS_TO_USERS_FK", creatorId, UserAccounts)(r => r.userAccountId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Questions */
  lazy val Questions = new TableQuery(tag => new Questions(tag))

  /** Entity class storing rows of table ResponseRequests
   *  @param responseRequestId Database column RESPONSE_REQUEST_ID SqlType(VARCHAR), PrimaryKey
   *  @param questionId Database column QUESTION_ID SqlType(VARCHAR)
   *  @param scheduledForMillis Database column SCHEDULED_FOR_MILLIS SqlType(BIGINT)
   *  @param sentOutMillis Database column SENT_OUT_MILLIS SqlType(BIGINT), Default(None) */
  case class ResponseRequestsRow(responseRequestId: String, questionId: String, scheduledForMillis: Long, sentOutMillis: Option[Long] = None)
  /** GetResult implicit for fetching ResponseRequestsRow objects using plain SQL queries */
  implicit def GetResultResponseRequestsRow(implicit e0: GR[String], e1: GR[Long], e2: GR[Option[Long]]): GR[ResponseRequestsRow] = GR{
    prs => import prs._
    ResponseRequestsRow.tupled((<<[String], <<[String], <<[Long], <<?[Long]))
  }
  /** Table description of table RESPONSE_REQUESTS. Objects of this class serve as prototypes for rows in queries. */
  class ResponseRequests(_tableTag: Tag) extends Table[ResponseRequestsRow](_tableTag, Some("DECISIONS"), "RESPONSE_REQUESTS") {
    def * = (responseRequestId, questionId, scheduledForMillis, sentOutMillis) <> (ResponseRequestsRow.tupled, ResponseRequestsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(responseRequestId), Rep.Some(questionId), Rep.Some(scheduledForMillis), sentOutMillis).shaped.<>({r=>import r._; _1.map(_=> ResponseRequestsRow.tupled((_1.get, _2.get, _3.get, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column RESPONSE_REQUEST_ID SqlType(VARCHAR), PrimaryKey */
    val responseRequestId: Rep[String] = column[String]("RESPONSE_REQUEST_ID", O.PrimaryKey)
    /** Database column QUESTION_ID SqlType(VARCHAR) */
    val questionId: Rep[String] = column[String]("QUESTION_ID")
    /** Database column SCHEDULED_FOR_MILLIS SqlType(BIGINT) */
    val scheduledForMillis: Rep[Long] = column[Long]("SCHEDULED_FOR_MILLIS")
    /** Database column SENT_OUT_MILLIS SqlType(BIGINT), Default(None) */
    val sentOutMillis: Rep[Option[Long]] = column[Option[Long]]("SENT_OUT_MILLIS", O.Default(None))

    /** Foreign key referencing Questions (database name RESPONSE_REQUESTS_TO_QUESTIONS_FK) */
    lazy val questionsFk = foreignKey("RESPONSE_REQUESTS_TO_QUESTIONS_FK", questionId, Questions)(r => r.questionId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table ResponseRequests */
  lazy val ResponseRequests = new TableQuery(tag => new ResponseRequests(tag))

  /** Entity class storing rows of table Responses
   *  @param responseId Database column RESPONSE_ID SqlType(VARCHAR), PrimaryKey
   *  @param choiceId Database column CHOICE_ID SqlType(VARCHAR)
   *  @param questionId Database column QUESTION_ID SqlType(VARCHAR)
   *  @param responseRequestId Database column RESPONSE_REQUEST_ID SqlType(VARCHAR), Default(None)
   *  @param createdMillis Database column CREATED_MILLIS SqlType(BIGINT) */
  case class ResponsesRow(responseId: String, choiceId: String, questionId: String, responseRequestId: Option[String] = None, createdMillis: Long)
  /** GetResult implicit for fetching ResponsesRow objects using plain SQL queries */
  implicit def GetResultResponsesRow(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Long]): GR[ResponsesRow] = GR{
    prs => import prs._
    ResponsesRow.tupled((<<[String], <<[String], <<[String], <<?[String], <<[Long]))
  }
  /** Table description of table RESPONSES. Objects of this class serve as prototypes for rows in queries. */
  class Responses(_tableTag: Tag) extends Table[ResponsesRow](_tableTag, Some("DECISIONS"), "RESPONSES") {
    def * = (responseId, choiceId, questionId, responseRequestId, createdMillis) <> (ResponsesRow.tupled, ResponsesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(responseId), Rep.Some(choiceId), Rep.Some(questionId), responseRequestId, Rep.Some(createdMillis)).shaped.<>({r=>import r._; _1.map(_=> ResponsesRow.tupled((_1.get, _2.get, _3.get, _4, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column RESPONSE_ID SqlType(VARCHAR), PrimaryKey */
    val responseId: Rep[String] = column[String]("RESPONSE_ID", O.PrimaryKey)
    /** Database column CHOICE_ID SqlType(VARCHAR) */
    val choiceId: Rep[String] = column[String]("CHOICE_ID")
    /** Database column QUESTION_ID SqlType(VARCHAR) */
    val questionId: Rep[String] = column[String]("QUESTION_ID")
    /** Database column RESPONSE_REQUEST_ID SqlType(VARCHAR), Default(None) */
    val responseRequestId: Rep[Option[String]] = column[Option[String]]("RESPONSE_REQUEST_ID", O.Default(None))
    /** Database column CREATED_MILLIS SqlType(BIGINT) */
    val createdMillis: Rep[Long] = column[Long]("CREATED_MILLIS")

    /** Foreign key referencing Choices (database name RESPONSES_TO_CHOICE_FK) */
    lazy val choicesFk = foreignKey("RESPONSES_TO_CHOICE_FK", choiceId, Choices)(r => r.choiceId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing Questions (database name RESPONSES_TO_QUESTION_FK) */
    lazy val questionsFk = foreignKey("RESPONSES_TO_QUESTION_FK", questionId, Questions)(r => r.questionId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing ResponseRequests (database name RESPONSES_TO_RESPONSE_REQUEST_FK) */
    lazy val responseRequestsFk = foreignKey("RESPONSES_TO_RESPONSE_REQUEST_FK", responseRequestId, ResponseRequests)(r => Rep.Some(r.responseRequestId), onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Responses */
  lazy val Responses = new TableQuery(tag => new Responses(tag))

  /** Entity class storing rows of table Schedules
   *  @param scheduleId Database column SCHEDULE_ID SqlType(VARCHAR), PrimaryKey
   *  @param questionId Database column QUESTION_ID SqlType(VARCHAR)
   *  @param period Database column PERIOD SqlType(VARCHAR)
   *  @param frequency Database column FREQUENCY SqlType(INTEGER) */
  case class SchedulesRow(scheduleId: String, questionId: String, period: String, frequency: Int)
  /** GetResult implicit for fetching SchedulesRow objects using plain SQL queries */
  implicit def GetResultSchedulesRow(implicit e0: GR[String], e1: GR[Int]): GR[SchedulesRow] = GR{
    prs => import prs._
    SchedulesRow.tupled((<<[String], <<[String], <<[String], <<[Int]))
  }
  /** Table description of table SCHEDULES. Objects of this class serve as prototypes for rows in queries. */
  class Schedules(_tableTag: Tag) extends Table[SchedulesRow](_tableTag, Some("DECISIONS"), "SCHEDULES") {
    def * = (scheduleId, questionId, period, frequency) <> (SchedulesRow.tupled, SchedulesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(scheduleId), Rep.Some(questionId), Rep.Some(period), Rep.Some(frequency)).shaped.<>({r=>import r._; _1.map(_=> SchedulesRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column SCHEDULE_ID SqlType(VARCHAR), PrimaryKey */
    val scheduleId: Rep[String] = column[String]("SCHEDULE_ID", O.PrimaryKey)
    /** Database column QUESTION_ID SqlType(VARCHAR) */
    val questionId: Rep[String] = column[String]("QUESTION_ID")
    /** Database column PERIOD SqlType(VARCHAR) */
    val period: Rep[String] = column[String]("PERIOD")
    /** Database column FREQUENCY SqlType(INTEGER) */
    val frequency: Rep[Int] = column[Int]("FREQUENCY")

    /** Foreign key referencing Questions (database name SCHEDULES_TO_QUESTION_FK) */
    lazy val questionsFk = foreignKey("SCHEDULES_TO_QUESTION_FK", questionId, Questions)(r => r.questionId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Schedules */
  lazy val Schedules = new TableQuery(tag => new Schedules(tag))

  /** Entity class storing rows of table UserAccounts
   *  @param userAccountId Database column USER_ACCOUNT_ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param email Database column EMAIL SqlType(VARCHAR)
   *  @param hashedPassword Database column HASHED_PASSWORD SqlType(VARCHAR) */
  case class UserAccountsRow(userAccountId: Int, email: String, hashedPassword: String)
  /** GetResult implicit for fetching UserAccountsRow objects using plain SQL queries */
  implicit def GetResultUserAccountsRow(implicit e0: GR[Int], e1: GR[String]): GR[UserAccountsRow] = GR{
    prs => import prs._
    UserAccountsRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table USER_ACCOUNTS. Objects of this class serve as prototypes for rows in queries. */
  class UserAccounts(_tableTag: Tag) extends Table[UserAccountsRow](_tableTag, Some("DECISIONS"), "USER_ACCOUNTS") {
    def * = (userAccountId, email, hashedPassword) <> (UserAccountsRow.tupled, UserAccountsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userAccountId), Rep.Some(email), Rep.Some(hashedPassword)).shaped.<>({r=>import r._; _1.map(_=> UserAccountsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column USER_ACCOUNT_ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val userAccountId: Rep[Int] = column[Int]("USER_ACCOUNT_ID", O.AutoInc, O.PrimaryKey)
    /** Database column EMAIL SqlType(VARCHAR) */
    val email: Rep[String] = column[String]("EMAIL")
    /** Database column HASHED_PASSWORD SqlType(VARCHAR) */
    val hashedPassword: Rep[String] = column[String]("HASHED_PASSWORD")
  }
  /** Collection-like TableQuery object for table UserAccounts */
  lazy val UserAccounts = new TableQuery(tag => new UserAccounts(tag))

  /** Entity class storing rows of table UserConnections
   *  @param userConnectionId Database column USER_CONNECTION_ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param senderUserId Database column SENDER_USER_ID SqlType(INTEGER)
   *  @param receiverUserId Database column RECEIVER_USER_ID SqlType(INTEGER) */
  case class UserConnectionsRow(userConnectionId: Int, senderUserId: Int, receiverUserId: Int)
  /** GetResult implicit for fetching UserConnectionsRow objects using plain SQL queries */
  implicit def GetResultUserConnectionsRow(implicit e0: GR[Int]): GR[UserConnectionsRow] = GR{
    prs => import prs._
    UserConnectionsRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table USER_CONNECTIONS. Objects of this class serve as prototypes for rows in queries. */
  class UserConnections(_tableTag: Tag) extends Table[UserConnectionsRow](_tableTag, Some("DECISIONS"), "USER_CONNECTIONS") {
    def * = (userConnectionId, senderUserId, receiverUserId) <> (UserConnectionsRow.tupled, UserConnectionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userConnectionId), Rep.Some(senderUserId), Rep.Some(receiverUserId)).shaped.<>({r=>import r._; _1.map(_=> UserConnectionsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column USER_CONNECTION_ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val userConnectionId: Rep[Int] = column[Int]("USER_CONNECTION_ID", O.AutoInc, O.PrimaryKey)
    /** Database column SENDER_USER_ID SqlType(INTEGER) */
    val senderUserId: Rep[Int] = column[Int]("SENDER_USER_ID")
    /** Database column RECEIVER_USER_ID SqlType(INTEGER) */
    val receiverUserId: Rep[Int] = column[Int]("RECEIVER_USER_ID")

    /** Foreign key referencing UserAccounts (database name USER_CONNECTIONS_RECEIVER_TO_USERS_FK) */
    lazy val userAccountsFk1 = foreignKey("USER_CONNECTIONS_RECEIVER_TO_USERS_FK", receiverUserId, UserAccounts)(r => r.userAccountId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing UserAccounts (database name USER_CONNECTIONS_SENDER_TO_USERS_FK) */
    lazy val userAccountsFk2 = foreignKey("USER_CONNECTIONS_SENDER_TO_USERS_FK", senderUserId, UserAccounts)(r => r.userAccountId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table UserConnections */
  lazy val UserConnections = new TableQuery(tag => new UserConnections(tag))

  /** Entity class storing rows of table UserSessions
   *  @param userSessionId Database column USER_SESSION_ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param userId Database column USER_ID SqlType(INTEGER)
   *  @param hashString Database column HASH_STRING SqlType(VARCHAR) */
  case class UserSessionsRow(userSessionId: Int, userId: Int, hashString: String)
  /** GetResult implicit for fetching UserSessionsRow objects using plain SQL queries */
  implicit def GetResultUserSessionsRow(implicit e0: GR[Int], e1: GR[String]): GR[UserSessionsRow] = GR{
    prs => import prs._
    UserSessionsRow.tupled((<<[Int], <<[Int], <<[String]))
  }
  /** Table description of table USER_SESSIONS. Objects of this class serve as prototypes for rows in queries. */
  class UserSessions(_tableTag: Tag) extends Table[UserSessionsRow](_tableTag, Some("DECISIONS"), "USER_SESSIONS") {
    def * = (userSessionId, userId, hashString) <> (UserSessionsRow.tupled, UserSessionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userSessionId), Rep.Some(userId), Rep.Some(hashString)).shaped.<>({r=>import r._; _1.map(_=> UserSessionsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column USER_SESSION_ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val userSessionId: Rep[Int] = column[Int]("USER_SESSION_ID", O.AutoInc, O.PrimaryKey)
    /** Database column USER_ID SqlType(INTEGER) */
    val userId: Rep[Int] = column[Int]("USER_ID")
    /** Database column HASH_STRING SqlType(VARCHAR) */
    val hashString: Rep[String] = column[String]("HASH_STRING")

    /** Foreign key referencing UserAccounts (database name USER_SESSIONS_TO_USER_FK) */
    lazy val userAccountsFk = foreignKey("USER_SESSIONS_TO_USER_FK", userId, UserAccounts)(r => r.userAccountId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table UserSessions */
  lazy val UserSessions = new TableQuery(tag => new UserSessions(tag))
}
