package com.example.app.contracts

import com.example.app.db.Tables.{ChoicesRow, ResponsesRow}
import org.joda.time.DateTime

/**
  * Created by matt on 9/26/17.
  */
object ResponseContracts {

  case class Input(choiceId: String, responseRequestId: Option[String] = None) {
    def response(questionId: String) = ResponsesRow(null, choiceId, questionId, responseRequestId, new DateTime().getMillis)
  }

  case class Output(createdMillis: Long, choice: ChoiceContracts.Output)

  def serialize(choice: ChoicesRow, response: ResponsesRow) =
    Output(response.createdMillis, ChoiceContracts.serialize(choice))
}
