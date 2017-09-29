import React from 'react';
import { connect } from 'react-redux';
import { Map, List } from 'immutable';
import { Redirect, Link } from 'react-router-dom';
import {
  Table,
  Button,
  ButtonGroup,
  FormGroup,
  ControlLabel,
  FormControl,
  Form,
  Radio
} from 'react-bootstrap';
import FormGroupBase from '../shared/FormGroupBase.jsx';
import {dispatchPattern} from '../../utilities.js';
import { setBuildingQuestionText, setBuildingQuestionType, setBuildingQuestionPeriod, setBuildingQuestionFrequency, setBuildingQuestionExpiration } from '../../actions.js';
import { saveQuestion, saveQuestionSuccess, saveQuestionError, saveSchedule, saveScheduleSuccess, saveScheduleError, cleanEditPage } from '../../actions.js';

var DatePicker = require("react-bootstrap-date-picker");

class QuestionForm extends React.Component {

  render() {

    const defaultDate = new Date(new Date().getTime() + 1000 * 60 * 60 * 24 * 30).toISOString()

    const buildingQuestion = this.props.buildingQuestion

    const datePickerValue = buildingQuestion.get('expiration') ? new Date(buildingQuestion.get('expiration')).toISOString() : defaultDate

    const questionTextOnChange = (e) => this.props.setBuildingQuestionText(e.target.value)
    const questionTypeOnChange = (e) => this.props.setBuildingQuestionType(e.target.value)
    const frequencyTextOnChange = (e) => this.props.setBuildingQuestionFrequency(Number(e.target.value))
    const periodOnChange = (e) => this.props.setBuildingQuestionPeriod(e.target.value)
    const addExpiration = (value) => this.props.setBuildingQuestionExpiration(new Date(value).getTime())
    const turnExpirationOn = () => this.props.setBuildingQuestionExpiration(new Date(datePickerValue).getTime())
    const removeExpiration = (e) => this.props.setBuildingQuestionExpiration(null)


    const onSave = () => {
        this.props.saveQuestion(
            this.props.questionId,
            buildingQuestion.get('questionText'),
            buildingQuestion.get('questionType'),
            buildingQuestion.get('expiration'),
            buildingQuestion.get('period'),
            buildingQuestion.get('frequency')

        )
    }

    const typeEditingDisabled = this.props.questionId ? true : false

    const questionTextFormProps = {
      type: 'label',
      label: 'Question:',
      placeholder: 'How happy are you?, How is your job?, ...',
      onChange: questionTextOnChange,
      value: buildingQuestion.get('questionText')
    }

    const frequencyTextFormProps = {
      type: 'text',
      placeholder: "1, 2, 3, 5, 8...",
      value: Number(buildingQuestion.get('frequency')),
      onChange: frequencyTextOnChange,
      className: 'input-sm'
    }

    const questionTypes = this.props.questionTypes.get('types')
    const periodTypes = this.props.periodTypes.get('types')

    const pluralTimes = buildingQuestion.get('frequency') == 1 ? 'time' : 'times'

    const returnDestination = this.props.questionId ? "/questions/"+this.props.questionId : "/questions"

    if(this.props.savingSchedule.get('saved', null)){
        const qId = this.props.savingQuestion.getIn(['saved', 'question', 'questionId'])
        this.props.cleanEditPage()
        return <Redirect to={"/questions/"+qId}></Redirect>
    }

    return <div><Form>
                     <FormGroupBase baseProps={questionTextFormProps}/>
                     <FormGroup>
                       <ControlLabel>Question Type</ControlLabel>
                       <FormControl disabled={typeEditingDisabled} value={buildingQuestion.get('questionType')} onChange={questionTypeOnChange} componentClass="select" placeholder="select">
                         { questionTypes ? questionTypes.map(o =>
                                   <option key={o.get('name')} value={o.get('name')}>{o.get('name')}</option>)
                                   : null }
                       </FormControl>
                     </FormGroup>
                     </Form>

                     <Form inline>
                       <ControlLabel>Ask me &nbsp;</ControlLabel>
                       <FormControl type="text" placeholder="1, 2, 3, 5, 8..." value={Number(buildingQuestion.get('frequency'))} onChange={frequencyTextOnChange} style={{width:'100px'}} />
                       &nbsp;{pluralTimes} every&nbsp;
                       <FormControl value={buildingQuestion.get('period')} onChange={periodOnChange} componentClass="select" placeholder="select">
                         { periodTypes ? periodTypes.map(o =>
                                   <option key={o.get('name')} value={o.get('name')}>{o.get('name')}</option>)
                                   : null }
                       </FormControl>
                     </Form>
                     <br />
                     <Form inline>

                        <FormGroup onChange={this.onLimitChange}>
                           <br />
                           <Radio name="radioGroup" inline value={false} onChange={removeExpiration} checked={!buildingQuestion.get('expiration')}>
                             Forever
                           </Radio>
                           {' '}
                           <Radio name="radioGroup" inline value={true} onChange={turnExpirationOn} checked={buildingQuestion.get('expiration') != null}>
                             Until
                           </Radio>
                         </FormGroup>

                        <DatePicker disabled={buildingQuestion.get('expiration') == null} value={datePickerValue} onChange={(value) => addExpiration(new Date(value).getTime())} />
                     </Form>

                     <br />
                     <div className="text-xs-center">
                         <Button
                           bsStyle="primary"
                           onClick={onSave}
                           type="submit">
                           Save Question
                         </Button>
                         <p className='m-t-1'><Link to={{ pathname: returnDestination }}>cancel</Link></p>
                     </div>
                   </div>
  }
}

const mapStateToProps = state => {
  return {
    questionTypes: state.get('questionTypes'),
    periodTypes: state.get('periodTypes'),
    buildingQuestion: state.get('buildingQuestion'),
    savingQuestion: state.get('savingQuestion'),
    savingSchedule: state.get('savingSchedule')
  }
}

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    setBuildingQuestionText: (questionText) => dispatch(setBuildingQuestionText(questionText)),
    setBuildingQuestionType: (questionType) => dispatch(setBuildingQuestionType(questionType)),
    setBuildingQuestionPeriod: (period) => dispatch(setBuildingQuestionPeriod(period)),
    setBuildingQuestionFrequency: (frequency) => dispatch(setBuildingQuestionFrequency(frequency)),
    setBuildingQuestionExpiration: (millis) => dispatch(setBuildingQuestionExpiration(millis)),
    saveQuestion: (questionId, questionText, questionType, expiration, period, frequency) => {
        const saveFunc = dispatchPattern(saveQuestion, saveQuestionSuccess, saveQuestionError, function(data) {
            const qId = data.question.questionId
            dispatchPattern(saveSchedule, saveScheduleSuccess, saveScheduleError)(period, frequency, qId)
        })
        saveFunc(questionId, questionText, questionType, expiration)
        },
    saveSchedule: dispatchPattern(saveSchedule, saveScheduleSuccess, saveScheduleError),
    cleanEditPage: () => dispatch(cleanEditPage())
  }
}

export const QuestionFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(QuestionForm)
