import React from 'react';
import { connect } from 'react-redux';
import { Map, List } from 'immutable';
import {
  Table,
  Button,
  ButtonGroup
} from 'react-bootstrap';
import {QuestionFormContainer} from "./QuestionForm.jsx";
import { getQuestion, getQuestionSuccess, getQuestionError, getSchedule, getScheduleSuccess, getScheduleError } from '../../actions.js';
import {dispatchPattern} from '../../utilities.js';
import { setBuildingQuestionText, setBuildingQuestionType, setBuildingQuestionPeriod, setBuildingQuestionFrequency, setBuildingQuestionExpiration } from '../../actions.js';

class EditQuestion extends React.Component {
  componentDidMount() {
    const questionId = this.props.match.params.id
    console.log(questionId)
    this.props.getQuestion(questionId);
    this.props.getSchedule(questionId);
  }

  componentDidUpdate() {
    const defaultType = this.props.questionTypes ? this.props.questionTypes.get(0, '') : ''
    const defaultPeriod = this.props.periodTypes ? this.props.periodTypes.get(0, '') : ''

    const question = this.props.currentQuestion.get('question') ? this.props.currentQuestion.getIn(['question', 'question']) : Map({})
    const schedule = this.props.currentSchedule.get('schedule') ? this.props.currentSchedule.get('schedule') : Map({})

    this.props.setBuildingQuestionText(question.get('text', ''))
    this.props.setBuildingQuestionType(question.get('questionType', defaultType))
    this.props.setBuildingQuestionPeriod(schedule.get('period', defaultPeriod))
    this.props.setBuildingQuestionFrequency(schedule.get('frequency', 1))
    console.log(question.get('expirationMillis'))
    this.props.setBuildingQuestionExpiration(question.get('expirationMillis'), null)
  }

  render() {

    return <div className='col-md-push-4 col-md-4 m-t-5'>
                            <h1>Edit Question</h1>
                              <QuestionFormContainer questionId={this.props.currentQuestion.getIn(['question', 'question', 'questionId'], null)} />
                            </div>
  }
}

const mapStateToProps = state => {
  return {
    currentQuestion: state.get('currentQuestion'),
    currentSchedule: state.get('currentSchedule')
  }
}

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    getQuestion: dispatchPattern(getQuestion, getQuestionSuccess, getQuestionError),
    getSchedule: dispatchPattern(getSchedule, getScheduleSuccess, getScheduleError),
    setBuildingQuestionText: (questionText) => dispatch(setBuildingQuestionText(questionText)),
    setBuildingQuestionType: (questionType) => dispatch(setBuildingQuestionType(questionType)),
    setBuildingQuestionPeriod: (period) => dispatch(setBuildingQuestionPeriod(period)),
    setBuildingQuestionFrequency: (frequency) => dispatch(setBuildingQuestionFrequency(frequency)),
    setBuildingQuestionExpiration: (millis) => dispatch(setBuildingQuestionExpiration(millis))
  }
}

export const EditQuestionContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(EditQuestion)
