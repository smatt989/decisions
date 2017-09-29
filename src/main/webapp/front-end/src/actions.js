import axios from 'axios';
import {authenticatedSession, authenticationHeader, authenticate} from './utilities';

const domain = CONFIG ? CONFIG.frontServer ? 'http://localhost:8080' : '' : '';

/*
 * action types
 */

export const SIGNUP_EMAIL_CHANGED = 'SIGNUP_EMAIL_CHANGED';
export const SIGNUP_PASSWORD_CHANGED = 'SIGNUP_PASSWORD_CHANGED';
export const SIGNUP_CLEAR_INPUTS = 'SIGNUP_CLEAR_INPUTS';
export const LOGIN_EMAIL_CHANGED = 'LOGIN_EMAIL_CHANGED';
export const LOGIN_PASSWORD_CHANGED = 'LOGIN_PASSWORD_CHANGED';
export const LOGIN_CLEAR_INPUTS = 'LOGIN_CLEAR_INPUTS';

/*
 * action creators
 */

export function cleanState() {
  return {
    type: 'CLEAN_STATE'
  };
}

export function cleanEditPage(){
  return {
    type: 'CLEAN_EDIT_PAGE'
  }
}

export function cleanQuestionView() {
  return {
    type: 'CLEAN_QUESTION_VIEW'
  }
}

export function createUser(email, password) {
  const request = axios({
    method: 'post',
    url: `${domain}/users/create`,
    headers: {
      'email': email,
      'password': password
    }
  });

  return {
    type: 'CREATE_USER',
    payload: request
  };
}

export function createUserSuccess(loaded) {
  return {
    type: 'CREATE_USER_SUCCESS',
    payload: loaded
  };
}

export function createUserError(error) {
  return {
    type: 'CREATE_USER_ERROR',
    error: error
  };
}

export function login(email, password) {
  const request = axios({
    method: 'get',
    url: `${domain}/sessions/new`,
    headers: {
      'email': email,
      'password': password
    }
  });

  return {
    type: 'LOGIN',
    payload: request
  };
}

export function loginSuccess(loaded) {
  return {
    type: 'LOGIN_SUCCESS',
    payload: loaded
  };
}

export function loginError(error) {
  return {
    type: 'LOGIN_ERROR',
    error: error
  };
}

export function logout(session) {
  const request = axios({
    method: 'post',
    url: `${domain}/sessions/logout`,
    headers: authenticate()
  });

  return {
    type: 'LOGOUT',
    payload: request
  };
}

export function logoutSuccess(loaded) {
  return {
    type: 'LOGOUT_SUCCESS',
    payload: loaded
  };
}

export function logoutError(error) {
  return {
    type: 'LOGOUT_ERROR',
    error: error
  };
}

export function signupEmailChanged(email) {
  return {
    type: SIGNUP_EMAIL_CHANGED,
    email: email
  }
}

export function signupPasswordChanged(password) {
  return {
    type: SIGNUP_PASSWORD_CHANGED,
    password: password
  }
}

export function signupClearInputs() {
  return {
    type: SIGNUP_CLEAR_INPUTS
  }
}

export function loginEmailChanged(email) {
  return {
    type: LOGIN_EMAIL_CHANGED,
    email: email
  }
}

export function loginPasswordChanged(password) {
  return {
    type: LOGIN_PASSWORD_CHANGED,
    password: password
  }
}

export function loginClearInputs() {
  return {
    type: LOGIN_CLEAR_INPUTS
  }
}

export function getQuestions() {
  const request = axios({
    method: 'get',
    url: `${domain}/questions`,
    headers: authenticate()
  });

  return {
    type: 'GET_QUESTIONS',
    payload: request
  };
}

export function getQuestionsSuccess(loaded) {
  return {
    type: 'GET_QUESTIONS_SUCCESS',
    payload: loaded
  }
}

export function getQuestionsError(error) {
  return {
    type: 'GET_QUESTIONS_ERROR',
    error: error
  }
}

export function getQuestion(questionId) {
  const request = axios({
    method: 'get',
    url: `${domain}/questions/${questionId}`,
    headers: authenticate()
  });

  return {
    type: 'GET_QUESTION',
    payload: request
  };
}

export function getQuestionSuccess(loaded) {
  return {
    type: 'GET_QUESTION_SUCCESS',
    payload: loaded
  }
}

export function getQuestionError(error){
  return {
    type: 'GET_QUESTION_ERROR',
    error: error
  }
}

export function postResponse(questionId, choiceId) {
  const request = axios({
    method: 'post',
    url: `${domain}/questions/${questionId}/responses/create`,
    data: {choiceId: choiceId},
    headers: authenticate()
  });

  return {
    type: 'POST_RESPONSE',
    payload: request
  };
}

export function postResponseSuccess(loaded) {
  return {
    type: 'POST_RESPONSE_SUCCESS',
    payload: loaded
  }
}

export function postResponseError(error) {
  return {
    type: 'POST_RESPONSE_ERROR',
    error: error
  }
}

export function getQuestionTypes(){
  const request = axios({
    method: 'get',
    url: `${domain}/questiontypes`
  });

  return {
    type: 'GET_QUESTION_TYPES',
    payload: request
  };
}

export function getQuestionTypesSuccess(loaded) {
  return {
    type: 'GET_QUESTION_TYPES_SUCCESS',
    payload: loaded
  }
}

export function getQuestionTypesError(error) {
  return {
    type: 'GET_QUESTION_TYPES_ERROR',
    error: error
  }
}

export function getPeriodTypes(){
  const request = axios({
    method: 'get',
    url: `${domain}/periodtypes`
  });

  return {
    type: 'GET_PERIOD_TYPES',
    payload: request
  };
}

export function getPeriodTypesSuccess(loaded) {
  return {
    type: 'GET_PERIOD_TYPES_SUCCESS',
    payload: loaded
  }
}

export function getPeriodTypesError(error) {
  return {
    type: 'GET_PERIOD_TYPES_ERROR',
    error: error
  }
}

export function getSchedule(questionId) {
  const request = axios({
    method: 'get',
    url: `${domain}/questions/${questionId}/schedules`,
    headers: authenticate()
  });

  return {
    type: 'GET_SCHEDULE',
    payload: request
  };
}

export function getScheduleSuccess(loaded) {
  return {
    type: 'GET_SCHEDULE_SUCCESS',
    payload: loaded
  }
}

export function getScheduleError(error) {
  return {
    type: 'GET_SCHEDULE_ERROR',
    error: error
  }
}

export function setBuildingQuestionText(questionText) {
  return {
    type: 'SET_BUILDING_QUESTION_TEXT',
    questionText: questionText
  }
}

export function setBuildingQuestionType(questionType) {
  return {
    type: 'SET_BUILDING_QUESTION_TYPE',
    questionType: questionType
  }
}

export function setBuildingQuestionPeriod(period) {
  return {
    type: 'SET_BUILDING_QUESTION_PERIOD',
    period: period
  }
}

export function setBuildingQuestionFrequency(frequency) {
  return {
    type: 'SET_BUILDING_QUESTION_FREQUENCY',
    frequency: frequency
  }
}

export function setBuildingQuestionExpiration(millis){
  return {
    type: 'SET_BUILDING_QUESTION_EXPIRATION',
    millis: millis
  }
}

export function saveQuestion(questionId, questionText, questionType, millis){
  const request = axios({
    method: 'post',
    url: `${domain}/questions/save`,
    data: {questionId: questionId, text: questionText, questionType: questionType, expirationMillis: millis},
    headers: authenticate()
  });

  return {
    type: 'SAVE_QUESTION',
    payload: request
  };
}

export function saveQuestionSuccess(loaded) {
  return {
    type: 'SAVE_QUESTION_SUCCESS',
    payload: loaded
  }
}

export function saveQuestionError(error) {
  return {
    type: 'SAVE_QUESTION_ERROR',
    error: error
  }
}

export function saveSchedule(period, frequency, questionId) {
  const request = axios({
    method: 'post',
    url: `${domain}/questions/${questionId}/schedules/save`,
    data: {period: period, frequency: frequency},
    headers: authenticate()
  });

  return {
    type: 'SAVE_SCHEDULE',
    payload: request
  };
}

export function saveScheduleSuccess(loaded) {
  return {
    type: 'SAVE_SCHEDULE_SUCCESS',
    payload: loaded
  }
}

export function saveScheduleError(error) {
  return {
    type: 'SAVE_SCHEDULE_ERROR',
    error: error
  }
}

export function getResponses(questionId){
  const request = axios({
    method: 'get',
    url: `${domain}/questions/${questionId}/responses`,
    headers: authenticate()
  });

  return {
    type: 'GET_RESPONSES',
    payload: request
  };
}

export function getResponsesSuccess(loaded) {
  return {
    type: 'GET_RESPONSES_SUCCESS',
    payload: loaded
  }
}

export function getResponsesError(error) {
  return {
    type: 'GET_RESPONSES_ERROR',
    error: error
  }
}

export function getLatestResponseRequest(questionId) {
  const request = axios({
    method: 'get',
    url: `${domain}/questions/${questionId}/responserequests/latest`,
    headers: authenticate()
  });

  return {
    type: 'GET_LATEST_RESPONSE_REQUEST',
    payload: request
  };
}

export function getLatestResponseRequestSuccess(loaded) {
  return {
    type: 'GET_LATEST_RESPONSE_REQUEST_SUCCESS',
    payload: loaded
  }
}

export function getLatestResponseRequestError(error) {
  return {
    type: 'GET_LATEST_RESPONSE_REQUEST_ERROR',
    error: error
  }
}