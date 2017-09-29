import React from 'react';
import {
  Button,
  ButtonGroup
} from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import { Redirect, Link } from 'react-router-dom';

const QuestionTableEntry = ({ data, match, leave }) => {

 const date = data.expirationMillis ? new Date(data.expirationMillis).toDateString() : ''
    console.log(date)

  return (
    <tr>
      <td><Link to={{ pathname: "/questions/"+data.questionId }}>{data.text}</Link></td>
      <td>{date}</td>
    </tr>)
  ;
};

export default QuestionTableEntry;
