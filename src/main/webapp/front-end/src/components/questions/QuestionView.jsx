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
import { getQuestion, getQuestionSuccess, getQuestionError, getLatestResponseRequest, getLatestResponseRequestSuccess, getLatestResponseRequestError, cleanQuestionView } from '../../actions.js';
import {dispatchPattern} from '../../utilities.js';
import { Redirect, Link } from 'react-router-dom';
import NavBar from '../NavBar.jsx';
import {AnswerQuestionContainer} from './AnswerQuestion.jsx';
import {ResponsesContainer} from '../responses/Responses.jsx';

class QuestionView extends React.Component {

  constructor(props) {
    super(props)

    this.state = {viewing: null}

    this.resetState = this.resetState.bind(this)
    this.viewStats = this.viewStats.bind(this)
    this.viewChoices = this.viewChoices.bind(this)
  }

  resetState() {
    this.setState({viewing: null})
  }

  viewStats() {
    console.log('view stats?')
    this.setState({viewing: 'stats'})
  }

  viewChoices() {
    this.setState({viewing: 'answer'})
  }

  componentDidMount() {
    this.props.cleanQuestionView()
    const questionId = this.props.match.params.id
    console.log(questionId)
    this.props.getQuestion(questionId);
    this.props.getLatestResponseRequest(questionId);
  }

  render() {

    const question = this.props.currentQuestion.get('question') || Map({})
    const questionId = question.getIn(['question', 'questionId'], null)

    const responseRequest = this.props.currentResponseRequest

    var answerContainer = null
    var responseContainer = null
    var messageContainer = null

    var extraButton = null

    console.log(this.state.viewing)


    if(this.state.viewing == 'stats'){
            responseContainer = <Row>
                                    <div className='col-md-push-3 col-md-6 m-t-5'>
                                        <ResponsesContainer {...this.props} />
                                    </div>
                                </Row>
            extraButton = <Button bsStyle="link" style={{display:'inline'}} onClick={this.viewChoices}>answer</Button>
    } else if(responseRequest.get('request') || this.state.viewing == 'answer') {
                   answerContainer = <Row>
                                         <div className='col-md-push-4 col-md-4 m-t-5'>
                                             <AnswerQuestionContainer resetState={this.resetState} />
                                         </div>
                                     </Row>
                   extraButton = <Button bsStyle="link" style={{display:'inline'}} onClick={this.viewStats}>stats</Button>

    } else {
        messageContainer = <Row>
                               <div className='col-md-push-3 col-md-6 m-t-5 text-xs-center'>
                                   <h2>You're all set</h2>
                                   <p>We'll notify you when it's time to respond again.</p>
                                   <Button onClick={this.viewStats}>See Stats</Button>
                                   <br />
                                   <Button onClick={this.viewChoices} className='m-t-3'>Respond Now</Button>
                               </div>
                           </Row>
    }

    return <Grid>
        <NavBar inverse={false}/>
        <Row>
            <div className='col-md-push-3 col-md-6 text-xs-center'>
                <h2 style={{display:'inline'}}>{question.getIn(['question', 'text'], "bloop")}</h2>&nbsp;
                <Link style={{display:'inline'}} to={`${this.props.match.url}/edit`}>edit</Link>
                {extraButton}
            </div>
        </Row>
        {answerContainer}
        {responseContainer}
        {messageContainer}
    </Grid>
  }
}

const mapStateToProps = state => {
  return {
    currentQuestion: state.get('currentQuestion'),
    currentResponseRequest: state.get('currentResponseRequest')
  }
}

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    getQuestion: dispatchPattern(getQuestion, getQuestionSuccess, getQuestionError),
    getLatestResponseRequest: dispatchPattern(getLatestResponseRequest, getLatestResponseRequestSuccess, getLatestResponseRequestError),
    cleanQuestionView: () => dispatchPattern(cleanQuestionView())
  }
}

export const QuestionViewContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(QuestionView)
