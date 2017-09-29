import React from 'react';
import { connect } from 'react-redux';
import { Map, List } from 'immutable';
import {
  Table,
  Button,
  ButtonGroup,
  Grid,
  Row
} from 'react-bootstrap';
import { postResponse, postResponseSuccess, postResponseError } from '../../actions.js';
import {dispatchPattern} from '../../utilities.js';
import { Redirect, Link } from 'react-router-dom';

class AnswerQuestion extends React.Component {
  render() {

    const question = this.props.currentQuestion.get('question') || Map({})
    const questionId = question.getIn(['question', 'questionId'], null)

    const postResponse = this.props.postResponse

    return <div>
                {question.get('choices', List.of()).map(choice =>
                    <Button key={choice.get('choiceId')} onClick={() => postResponse(questionId, choice.get('choiceId'), this.props.resetState) } block>{choice.get('text')}</Button>
                )}
            </div>
  }
}

const mapStateToProps = state => {
  return {
    currentQuestion: state.get('currentQuestion')
  }
}

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    postResponse: (questionId, choiceId, resetState) => {
        const saveFunc = dispatchPattern(postResponse, postResponseSuccess, postResponseError, function(a){
            resetState()
        })
        saveFunc(questionId, choiceId)
    }
  }
}

export const AnswerQuestionContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(AnswerQuestion)
