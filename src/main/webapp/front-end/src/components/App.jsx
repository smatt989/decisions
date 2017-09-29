import '../styles/app.less';
import React from 'react';
import {
  HashRouter as Router,
  Route,
  Switch
} from 'react-router-dom';
import { Grid, Row, Col } from 'react-bootstrap';
import NavBarContainer from './NavBar.jsx';
import Home from './Home.jsx';
import LoginContainer from './account_forms/Login.jsx';
import RegisterContainer from './account_forms/Register.jsx';
import Questions from './questions/Questions.jsx'
import { PrivateRouteContainer, HomeRouteContainer } from './PrivateRoute.jsx';
import Err from './Error.jsx';
import {QuestionViewContainer} from './questions/QuestionView.jsx';
import {NewQuestionContainer} from './questions/NewQuestion.jsx';
import {EditQuestionContainer} from './questions/EditQuestion.jsx';
import {ResponsePageContainer} from './responses/ResponsePage.jsx';

export default class App extends React.Component {
  render() {
    return <Router>
      <div>
        <Switch>
          <HomeRouteContainer exact path="/" component={Home}/>
          <Route exact path="/login" component={LoginContainer}/>
          <Route exact path="/register" component={RegisterContainer}/>
          <Route exact path="/recover" component={LoginContainer}/>
          <Route exact path="/questions" component={Questions}/>
          <Route exact path="/questions/new" component={NewQuestionContainer} />
          <PrivateRouteContainer path="/questions/:id/response/:choice" component={ResponsePageContainer} />
          <PrivateRouteContainer path="/questions/:id/edit" component={EditQuestionContainer} />
          <PrivateRouteContainer path="/questions/:id" component={QuestionViewContainer} />


          <Route component={Err}/>
        </Switch>
      </div>
    </Router>;
  }
}
