package com.example.app.contracts

import com.example.app.db.Tables.ResponseRequestsRow

/**
  * Created by matt on 9/26/17.
  */
object ResponseRequestContracts {

  case class Output(responseRequestId: String, scheduledForMillis: Long)

  def serialize(responseRequest: ResponseRequestsRow) =
    Output(responseRequest.responseRequestId, responseRequest.scheduledForMillis)
}
