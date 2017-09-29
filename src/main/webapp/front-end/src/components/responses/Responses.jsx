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
import { getResponses, getResponsesSuccess, getResponsesError } from '../../actions.js';
import {dispatchPattern} from '../../utilities.js';
import { Redirect, Link } from 'react-router-dom';
import {HighchartsChart, Chart, Title, Subtitle, Legend, XAxis, YAxis, LineSeries} from 'react-jsx-highcharts';
import Highcharts from 'highcharts';

class Responses extends React.Component {

  componentDidMount() {
     const questionId = this.props.match.params.id
     this.props.getResponses(questionId)
  }

  render() {

    const responses = this.props.currentResponses.get('responses').map(response => {
        const millis = new Date(response.get('createdMillis'))
        const value = response.getIn(['choice', 'value'])
        return {x: millis, y: value}
    })

    const plotOptions = {
                            series: {
                                marker: {
                                    enabled: true,
                                    symbol: 'circle',
                                    radius: 3
                                }
                            }
                        }

    var yMin = -1
    var yMax = 11

    const questionType = this.props.currentQuestion.getIn(['question', 'question', 'questionType'], '')

    if(questionType == "1-5") {
        yMin = 1
        yMax = 5
    } else if (questionType == "1-10") {
        yMin = 1
        yMax = 10
    } else if (questionType == "Yes / No") {
        yMin = -1
        yMax = 2
    }

    Highcharts.setOptions({global: {useUTC: false}});

    return <div>
                <HighchartsChart plotOptions={plotOptions} useUTC={false}>
                  <Chart setOptions={{global: {useUTC: false}}} />
                  <XAxis type="datetime" >
                    <XAxis.Title >Time</XAxis.Title>
                  </XAxis>
                  <YAxis id="number" min={yMin} max={yMax}>
                    <YAxis.Title>Response</YAxis.Title>
                    <LineSeries id="installation" name="Installation" data={responses}/>
                  </YAxis>
                </HighchartsChart>
            </div>
  }
}

const mapStateToProps = state => {
  return {
    currentResponses: state.get('currentResponses'),
    currentQuestion: state.get('currentQuestion')
  }
}

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    getResponses: dispatchPattern(getResponses, getResponsesSuccess, getResponsesError)
  }
}

export const ResponsesContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(Responses)
