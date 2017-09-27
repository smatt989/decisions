package com.example.app.contracts

import com.example.app.db.Tables.ChoicesRow

/**
  * Created by matt on 9/25/17.
  */
object ChoiceContracts {

  case class Output(choiceId: String, text: String, order: Int, value: Int)

  def serialize(choice: ChoicesRow) =
    Output(choice.choiceId, choice.choiceText, choice.choiceOrder, choice.choiceValue)
}
