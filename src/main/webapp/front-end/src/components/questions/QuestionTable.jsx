import React from 'react';
import { connect } from 'react-redux';
import {
  Table,
  Button,
  ButtonGroup
} from 'react-bootstrap';
import { getQuestions, getQuestionsSuccess, getQuestionsError } from '../../actions.js';
import QuestionTableEntry from './QuestionTableEntry.jsx';
import {dispatchPattern} from '../../utilities.js';

class QuestionTable extends React.Component {
  componentDidMount() {
    this.props.getQuestions();
  }

  buildContent() {
    const { questions, loading, error } = this.props;
    if (error) {
      return <div>Error</div>;
    } else if (loading) {
      return <div>Loading</div>;
    } else if (!questions) {
      return null;
    };

    return <div className='container'>
        <h3>{this.props.tableHeader}</h3>
				<Table className="task-tbl" responsive striped hover>
		      <thead>
		        <tr>
		          <th>Name</th>
		          <th>Expires</th>
		        </tr>
		      </thead>
		      <tbody>
		        { questions
              ? questions.map(o =>
		            <QuestionTableEntry key={o.questionId} data={o} {...this.props} />)
		          : null
		        }
	      </tbody>
	    </Table>
      { questions.length > 0 ? null : <div /> }
		</div>;
  }

  render() {
    return this.buildContent();
  }
}

const mapStateToProps = state => {
  return {
    questions: state.getIn(['questionsList', 'questions']).toJS(),
    loading: state.getIn(['questionsList', 'loading']),
    error: state.getIn(['questionsList', 'error'])
  }
}

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    getQuestions: dispatchPattern(getQuestions, getQuestionsSuccess, getQuestionsError)
  }
}

export const QuestionTableContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(QuestionTable)
