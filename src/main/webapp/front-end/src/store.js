import {createStore, applyMiddleware} from 'redux';
import { cleanState, getQuestionTypes, getQuestionTypesSuccess, getQuestionTypesError, getPeriodTypes, getPeriodTypesSuccess, getPeriodTypesError } from './actions';
import reducer from './reducer';
import promise from 'redux-promise';
import {dispatchPattern} from './utilities.js'

function initStore() {
  const createStoreWithMiddleware = applyMiddleware(
    promise
  )(createStore);
  const store = createStoreWithMiddleware(reducer, window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__());
  store.dispatch(cleanState());
  fetchReferenceData(store);
  return store;
}

const store = initStore();
export default store;

function fetchReferenceData(store) {
    store.dispatch(getQuestionTypes())
                .then(response => {
                    if(response.error) {
                        store.dispatch(getQuestionTypesError(response.error));
                        return false;
                    }

                    store.dispatch(getQuestionTypesSuccess(response.payload.data));
                    return true;
                });

    store.dispatch(getPeriodTypes())
                .then(response => {
                    if(response.error) {
                        store.dispatch(getPeriodTypesError(response.error));
                        return false;
                    }

                    store.dispatch(getPeriodTypesSuccess(response.payload.data));
                    return true;
                });
}
