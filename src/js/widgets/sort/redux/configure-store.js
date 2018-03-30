'use strict';
define([
  'redux',
  'redux-thunk',
  'es6!./modules/sort-app',
  'redux-immutable'
], function (Redux, ReduxThunk, SortApp, ReduxImmutable) {

  const { createStore, applyMiddleware } = Redux;
  const { combineReducers } = ReduxImmutable;

  return function configureStore (context) {
    const middleware = applyMiddleware(ReduxThunk.default.withExtraArgument(context));
    const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || Redux.compose;
    const reducer = combineReducers({
      SortApp: SortApp.reducer
    });
    return createStore(reducer, composeEnhancers(middleware));
  };
});
