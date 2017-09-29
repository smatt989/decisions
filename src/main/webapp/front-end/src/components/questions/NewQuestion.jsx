import React from 'react';
import { connect } from 'react-redux';
import { Map, List } from 'immutable';
import {
  Table,
  Button,
  ButtonGroup
} from 'react-bootstrap';
import {QuestionFormContainer} from "./QuestionForm.jsx";
import { setBuildingQuestionText, setBuildingQuestionType, setBuildingQuestionPeriod, setBuildingQuestionFrequency, setBuildingQuestionExpiration } from '../../actions.js';

//import {  } from '../../actions.js';

class NewQuestion extends React.Component {

  constructor(props) {
    super(props)

    this.state = {viewing: null}

    this.setInitialQuestion = this.setInitialQuestion.bind(this)
  }

  componentDidUpdate() {
    this.setInitialQuestion()
  }

  componentDidMount() {
    this.setInitialQuestion()
  }

  setInitialQuestion() {
      this.props.setBuildingQuestionText('')
      this.props.setBuildingQuestionType(this.props.questionTypes.getIn(['types', 0, 'name'], ''))
      this.props.setBuildingQuestionPeriod(this.props.periodTypes.getIn(['types', 0, 'name'], ''))
      this.props.setBuildingQuestionFrequency(1)
      this.props.setBuildingQuestionExpiration(null)
  }



  render() {

    return <div className='col-md-push-4 col-md-4 m-t-5'>
                 <h1>New Question</h1>
                   <QuestionFormContainer />
                 </div>
  }
}

const mapStateToProps = state => {
  return {
    questionTypes: state.get('questionTypes'),
    periodTypes: state.get('periodTypes')
  }
}

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    setBuildingQuestionText: (questionText) => dispatch(setBuildingQuestionText(questionText)),
    setBuildingQuestionType: (questionType) => dispatch(setBuildingQuestionType(questionType)),
    setBuildingQuestionPeriod: (period) => dispatch(setBuildingQuestionPeriod(period)),
    setBuildingQuestionFrequency: (frequency) => dispatch(setBuildingQuestionFrequency(frequency)),
    setBuildingQuestionExpiration: (millis) => dispatch(setBuildingQuestionExpiration(millis))
  }
}

export const NewQuestionContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(NewQuestion)
