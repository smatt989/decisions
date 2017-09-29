import { Map, List, is } from 'immutable';
import Immutable from 'immutable';
import { getSession, setSession } from './utilities';
import {
  SIGNUP_EMAIL_CHANGED, SIGNUP_PASSWORD_CHANGED, SIGNUP_CLEAR_INPUTS,
  LOGIN_EMAIL_CHANGED, LOGIN_PASSWORD_CHANGED, LOGIN_CLEAR_INPUTS
} from './actions.js';

const cleanBuildingQuestion = Map({questionText: '', questionType: '', period: '', frequency: null, expiration: null});
const cleanSavingQuestion = Map({saved: null, loading: false, error: null});
const cleanSavingSchedule = Map({saved: null, loading: false, error: null});

const cleanCurrentResponseRequest = Map({request: null, loading: false, error: null});
const cleanCurrentResponses = Map({responses: List.of(), loading: false, error: null});
const cleanCurrentSchedule = Map({schedule: null, loading: false, error: null});
const cleanCurrentQuestion =  Map({question: null, loading: false, error: null});

function cleanState() {
  const sessionKey = getSession();
  setSession(sessionKey); // refresh session key

  const cleanState = Map({
    createUser: Map({loading: false, error: null}),
    login: Map({session: sessionKey, error: null, loading: false}),
    user: Map({email: null, id: null}),
    logout: Map({error: null, loading: false}),
    signupEmail: Map({ email: '' }),
    signupPassword: Map({ password: '' }),
    loginEmail: Map({ email: '' }),
    loginPassword: Map({ password: '' }),
    questionsList: Map({questions: List.of(), loading: false, error: null}),
    currentQuestion: cleanCurrentQuestion,
    postResponse: Map({response: null, loading: false, error: null}),
    questionTypes: Map({types: List.of(), loading: false, error: null}),
    periodTypes: Map({types: List.of(), loading: false, error: null}),
    currentSchedule: cleanCurrentSchedule,
    buildingQuestion: cleanBuildingQuestion,
    savingQuestion: cleanSavingQuestion,
    savingSchedule: cleanSavingSchedule,
    currentResponses: cleanCurrentResponses,
    currentResponseRequest: cleanCurrentResponseRequest
  });

  return cleanState;
}

function cleanEditPage(state) {
  const cleanBuildingQuestionState = state.set('buildingQuestion', cleanBuildingQuestion)
  const cleanSavingQuestionState = cleanBuildingQuestionState.set('savingQuestion', cleanSavingQuestion)
  const cleanSavingScheduleState = cleanSavingQuestionState.set('savingSchedule', cleanSavingSchedule)

  return cleanSavingScheduleState
}

function cleanQuestionView(state) {
  const cleanCurrentQuestionState = state.set('currentQuestion', cleanCurrentQuestion)
  const cleanCurrentScheduleState = cleanCurrentQuestionState.set('currentSchedule', cleanCurrentSchedule)
  const cleanCurrentResponsesState = cleanCurrentScheduleState.set('currentResponses', cleanCurrentResponses)
  const cleanCurrentResponseRequestState = cleanCurrentResponsesState.set('currentResponseRequest', cleanCurrentResponseRequest)

  return cleanCurrentResponseRequestState
}

function createUser(state) {
  return state.set('createUser', Map({loading: true, error: null}));
}

function createUserSuccess(state, user) {
  return state.set('createUser', Map({loading: false, error: null}));
}

function createUserError(state, error) {
  return state.set('createUser', Map({loading: false, error: Immutable.fromJS(error)}));
}

function login(state) {
  return state.set('login', Map({session: null, error: null, loading: true}));
}

function loginSuccess(state, session) {
  setSession(session);
  return state.set('login', Map({session: session, error: null, loading: false}));
}

function loginError(state, error) {
  return state.set('login', Map({session: null, error: error, loading: false}));
}

function logout(state) {
  return state.set('logout', Map({error: null, loading: true}));
}

function logoutSuccess(state, payload) {
  setSession(null);
  const newState = state.set('login', Map({session: null, error: null, loading: false}));
  return newState.set('logout', Map({error: null, loading: false}));
}

function logoutError(state, error) {
  return state.set('logout', Map({error: error, loading: false}));
}

function signupEmailChanged(state, email) {
  return state.set('signupEmail', Map({ email: email }));
}

function signupPasswordChanged(state, password) {
  return state.set('signupPassword', Map({ password: password }));
}

function signupClearInputs(state) {
  const newState = state.set('signupEmail', Map({ email: '' }));
  return newState.set('signupPassword', Map({ password: '' }));
}

function loginEmailChanged(state, email) {
  return state.set('loginEmail', Map({ email: email }));
}

function loginPasswordChanged(state, password) {
  return state.set('loginPassword', Map({ password: password }));
}

function loginClearInputs(state) {
  const newState = state.set('loginEmail', Map({ email: '' }));
  return newState.set('loginPassword', Map({ password: '' }));
}

function getQuestions(state) {
  return state.set('questionsList', Map({questions: List.of(), loading: true, error: null}));
}

function getQuestionsSuccess(state, questions) {
  return state.set('questionsList', Map({questions: Immutable.fromJS(questions), loading: false, error: null}));
}

function getQuestionsError(state, error) {
  return state.set('questionsList', Map({questions: List.of(), loading: false, error: Immutable.fromJS(error)}));
}

function getQuestion(state) {
  return state.set('currentQuestion', Map({question: null, loading: true, error: null}));
}

function getQuestionSuccess(state, question) {
  return state.set('currentQuestion', Map({question: Immutable.fromJS(question), loading: false, error: null}));
}

function getQuestionError(state, error) {
  return state.set('currentQuestion', Map({question: null, loading: false, error: Immutable.fromJS(error)}));
}

function postResponse(state) {
  return state.set('postResponse', Map({response: null, loading: true, error: null}));
}

function postResponseSuccess(state, response) {
  const newState = state.set('currentResponseRequest', cleanCurrentResponseRequest)
  return newState.set('postResponse', Map({response: Immutable.fromJS(response), loading: false, error: null}));
}

function postResponseError(state, error) {
  return state.set('postResponse', Map({response: null, loading: false, error: Immutable.fromJS(error)}));
}

function getQuestionTypes(state) {
  return state.set('questionTypes', Map({types: List.of(), loading: true, error: null}));
}

function getQuestionTypesSuccess(state, questionTypes) {
  return state.set('questionTypes', Map({types: Immutable.fromJS(questionTypes), loading: false, error: null}));
}

function getQuestionTypesError(state, error) {
  return state.set('questionTypes', Map({types: List.of(), loading: false, error: Immutable.fromJS(error)}))
}

function getPeriodTypes(state) {
  return state.set('periodTypes', Map({types: List.of(), loading: true, error: null}));
}

function getPeriodTypesSuccess(state, periodTypes) {
  return state.set('periodTypes', Map({types: Immutable.fromJS(periodTypes), loading: false, error: null}));
}

function getPeriodTypesError(state, error) {
  return state.set('periodTypes', Map({types: List.of(), loading: false, error: Immutable.fromJS(error)}));
}

function getSchedule(state) {
  return state.set('currentSchedule', Map({schedule: null, loading: true, error: null}));
}

function getScheduleSuccess(state, schedule) {
  return state.set('currentSchedule', Map({schedule: Immutable.fromJS(schedule), loading: false, error: null}));
}

function getScheduleError(state, error) {
  return state.set('currentSchedule', Map({schedule: null, loading: false, error: Immutable.fromJS(error)}));
}

function setBuildingQuestionText(state, questionText){
  return state.setIn(['buildingQuestion', 'questionText'], questionText);
}

function setBuildingQuestionType(state, questionType){
  return state.setIn(['buildingQuestion', 'questionType'], questionType);
}

function setBuildingQuestionPeriod(state, period) {
  return state.setIn(['buildingQuestion', 'period'], period);
}

function setBuildingQuestionFrequency(state, frequency) {
  return state.setIn(['buildingQuestion', 'frequency'], frequency);
}

function setBuildingQuestionExpiration(state, millis) {
  return state.setIn(['buildingQuestion', 'expiration'], millis);
}

function saveQuestion(state) {
  return state.set('savingQuestion', Map({saved: null, loading: true, error: null}));
}

function saveQuestionSuccess(state, saved) {
  return state.set('savingQuestion', Map({saved: Immutable.fromJS(saved), loading: false, error: null}));
}

function saveQuestionError(state, error) {
  return state.set('savingQuestion', Map({saved: null, loading: false, error: Immutable.fromJS(error)}));
}

function saveSchedule(state) {
  return state.set('savingSchedule', Map({saved: null, loading: true, error: null}));
}

function saveScheduleSuccess(state, saved) {
  return state.set('savingSchedule', Map({saved: Immutable.fromJS(saved), loading: false, error: null}));
}

function saveScheduleError(state, error) {
  return state.set('savingSchedule', Map({saved: null, loading: false, error: Immutable.fromJS(error)}));
}

function getResponses(state) {
  return state.set('currentResponses', Map({responses: List.of(), loading: true, error: null}));
}

function getResponsesSuccess(state, responses) {
  return state.set('currentResponses', Map({responses: Immutable.fromJS(responses), loading: false, error: null}));
}

function getResponsesError(state, error) {
  return state.set('currentResponses', Map({responses: List.of(), loading: false, error: Immutable.fromJS(error)}));
}

function getLatestResponseRequest(state) {
  return state.set('currentResponseRequest', Map({request: null, loading: true, error: null}));
}

function getLatestResponseRequestSuccess(state, responseRequest) {
  return state.set('currentResponseRequest', Map({request: Immutable.fromJS(responseRequest), loading: false, error: null}));
}

function getLatestResponseRequestError(state, error) {
  return state.set('currentResponseRequest', Map({request: null, loading: false, error: Immutable.fromJS(error)}));
}

export default function reducer(state = Map(), action) {
  switch (action.type) {
    case 'CLEAN_STATE':
      return cleanState();
    case 'CLEAN_EDIT_PAGE':
      return cleanEditPage(state);
    case 'CLEAN_QUESTION_VIEW':
      return cleanQuestionView(state);
    case 'CREATE_USER':
      return createUser(state);
    case 'CREATE_USER_SUCCESS':
      return createUserSuccess(state, action.email);
    case 'CREATE_USER_ERROR':
      return createUserError(state, action.error);
    case 'LOGIN':
      return login(state);
    case 'LOGIN_SUCCESS':
      return loginSuccess(state, action.payload);
    case 'LOGIN_ERROR':
      return loginError(state, action.error);
    case 'LOGOUT':
      return logout(state);
    case 'LOGOUT_SUCCESS':
      return logoutSuccess(state, action.payload);
    case 'LOGOUT_ERROR':
      return logoutError(state, action.error);
    case SIGNUP_EMAIL_CHANGED:
      return signupEmailChanged(state, action.email);
    case SIGNUP_PASSWORD_CHANGED:
      return signupPasswordChanged(state, action.password);
    case SIGNUP_CLEAR_INPUTS:
      return signupClearInputs(state);
    case LOGIN_EMAIL_CHANGED:
      return loginEmailChanged(state, action.email);
    case LOGIN_PASSWORD_CHANGED:
      return loginPasswordChanged(state, action.password);
    case LOGIN_CLEAR_INPUTS:
      return loginClearInputs(state);
    case 'GET_QUESTIONS':
      return getQuestions(state);
    case 'GET_QUESTIONS_SUCCESS':
      return getQuestionsSuccess(state, action.payload);
    case 'GET_QUESTIONS_ERROR':
      return getQuestionsError(state, action.error);
    case 'GET_QUESTION':
      return getQuestion(state);
    case 'GET_QUESTION_SUCCESS':
      return getQuestionSuccess(state, action.payload);
    case 'GET_QUESTION_ERROR':
      return getQuestionError(state, action.error);
    case 'POST_RESPONSE':
      return postResponse(state);
    case 'POST_RESPONSE_SUCCESS':
      return postResponseSuccess(state, action.payload);
    case 'POST_RESPONSE_ERROR':
      return postResponseError(state, action.error);
    case 'GET_QUESTION_TYPES':
      return getQuestionTypes(state);
    case 'GET_QUESTION_TYPES_SUCCESS':
      return getQuestionTypesSuccess(state, action.payload);
    case 'GET_QUESTION_TYPES_ERROR':
      return getQuestionTypesError(state, action.error);
    case 'GET_PERIOD_TYPES':
      return getPeriodTypes(state);
    case 'GET_PERIOD_TYPES_SUCCESS':
      return getPeriodTypesSuccess(state, action.payload);
    case 'GET_PERIOD_TYPES_ERROR':
      return getPeriodTypesError(state, action.error);
    case 'GET_SCHEDULE':
      return getSchedule(state);
    case 'GET_SCHEDULE_SUCCESS':
      return getScheduleSuccess(state, action.payload);
    case 'GET_SCHEDULE_ERROR':
      return getScheduleError(state, action.error);
    case 'SET_BUILDING_QUESTION_TEXT':
      return setBuildingQuestionText(state, action.questionText);
    case 'SET_BUILDING_QUESTION_TYPE':
      return setBuildingQuestionType(state, action.questionType);
    case 'SET_BUILDING_QUESTION_PERIOD':
      return setBuildingQuestionPeriod(state, action.period);
    case 'SET_BUILDING_QUESTION_FREQUENCY':
      return setBuildingQuestionFrequency(state, action.frequency);
    case 'SET_BUILDING_QUESTION_EXPIRATION':
      return setBuildingQuestionExpiration(state, action.millis);
    case 'SAVE_QUESTION':
      return saveQuestion(state);
    case 'SAVE_QUESTION_SUCCESS':
      return saveQuestionSuccess(state, action.payload);
    case 'SAVE_QUESTION_ERROR':
      return saveQuestionError(state, action.error);
    case 'SAVE_SCHEDULE':
      return saveSchedule(state);
    case 'SAVE_SCHEDULE_SUCCESS':
      return saveScheduleSuccess(state, action.payload);
    case 'SAVE_SCHEDULE_ERROR':
      return saveScheduleError(state, action.error);
    case 'GET_RESPONSES':
      return getResponses(state);
    case 'GET_RESPONSES_SUCCESS':
      return getResponsesSuccess(state, action.payload);
    case 'GET_RESPONSES_ERROR':
      return getResponsesError(state, action.error);
    case 'GET_LATEST_RESPONSE_REQUEST':
      return getLatestResponseRequest(state);
    case 'GET_LATEST_RESPONSE_REQUEST_SUCCESS':
      return getLatestResponseRequestSuccess(state, action.payload);
    case 'GET_LATEST_RESPONSE_REQUEST_ERROR':
      return getLatestResponseRequestError(state, action.error);
    default:
      return state;
  }
};
