import React from 'react';
import {
  Grid,
  PageHeader,
  Button
} from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import {QuestionTableContainer} from './QuestionTable.jsx';
import NavBar from '../NavBar.jsx';

const Questions = (props) => {
  return <Grid>
    <NavBar inverse={false}/>
    <div className='container'>
      <PageHeader>
        Questions
        <LinkContainer className="pull-right" to={`${props.match.url}/new`}>
          <Button
            className="new-tbl-item-btn"
            bsStyle="primary"
            type="button">
            New Question
          </Button>
        </LinkContainer>
      </PageHeader>
      <QuestionTableContainer {...props} />
    </div>
  </Grid>;
};

export default Questions;
