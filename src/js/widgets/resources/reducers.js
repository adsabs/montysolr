'use strict';
define([], function () {

  var initialState = function () {
    return {
      apiResponse: null,
      bibcode: null,
      dataProducts: [],
      error: null,
      fullTextSources: [],
      isLoading: true,
      query: null
    };
  };

  /**
   * Depending on the action type, perform an update to the state
   *
   * @param state
   * @param action
   * @returns {{dataProducts, fullTextSources, isLoading}|*}
   */
  var exports = function reducers (state, action) {
    state = state ? state : initialState();

    switch(action.type) {
      case 'IS_LOADING':
        return Object.assign({}, state, {
          isLoading: action.value
        });
      case 'UPDATE_RESOURCES':
        return Object.assign({}, state, {
          dataProducts: action.dataProducts,
          fullTextSources: action.fullTextSources
        });
      case 'IS_ERRORED':
        return Object.assign({}, state, {
          error: {
            e: action.value,
            msg: action.message
          }
        });
      case 'UPDATE_API_RESPONSE':
        return Object.assign({}, state, {
          apiResponse: action.value
        });
      case 'UPDATE_QUERY':
        return Object.assign({}, state, {
          query: action.value
        });
      case 'UPDATE_BIBCODE':
        return Object.assign({}, state, {
          bibcode: action.value
        });
      default:
        return state;
    }
  };

  return exports;
});
