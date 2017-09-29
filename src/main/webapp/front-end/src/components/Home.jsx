import React from 'react';
import { Grid, Jumbotron, Button, Glyphicon } from 'react-bootstrap';
import { Link, Redirect } from 'react-router-dom';
import BeeLabel from './BeeLabel.jsx';
import NavBar from './NavBar.jsx';

export default class Home extends React.Component {
  render() {
    return <div className="landing full-screen-page">
        <NavBar inverse={false} />
        <div className='container'>
          <div className="jumbotron jumbotron-home text-xs-center">
              <div className='row'>
                <div className='col-md-12'>
                  <h1>See Yourself From Above</h1>
                  <h3>Get out of your head and understand yourself</h3>
                </div>
              </div>
              <div className='row m-t-2'>
                <div className='col-md-12'>
                  <Link to="/register">
                    <Button bsStyle="success">Create Account</Button>
                  </Link>
                </div>
              </div>
              <br />
              <br />
              <br />
              <div className='m-t-5'>
                <div className='col-md-4'>
                    <h3>1. Submit a question with a schedule</h3>
                </div>
                <div className='col-md-4'>
                    <h3>2. Emails notify you when it's time to respond</h3>
                </div>
                <div className='col-md-4'>
                    <h3>3. See how you feel over time</h3>
                </div>
              </div>
          </div>
        </div>
      </div>;
  }
};
