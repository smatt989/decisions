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

class ResponsePage extends React.Component {

  componentDidMount() {
    const questionId = this.props.match.params.id
    const choiceId = this.props.match.params.choice
    this.props.postResponse(questionId, choiceId)
  }

  render() {

    const postResponse = this.props.postingResponse

    console.log()

    if(postResponse.get('response')) {
        return <Redirect to={"/questions/"+this.props.match.params.id}></Redirect>
    } else if (postResponse.get('loading')) {
        return <div><h3>Loading...</h3></div>
    } else {
        return <div><h3>Yikes error...</h3></div>
    }
  }
}

const mapStateToProps = state => {
  return {
    postingResponse: state.get('postResponse')
  }
}

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    postResponse: dispatchPattern(postResponse, postResponseSuccess, postResponseError)
  }
}

export const ResponsePageContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(ResponsePage)
