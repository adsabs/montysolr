'use strict';
define([
  'es6!../constants/actionNames',
  'moment'
], function (ACTIONS, Moment) {

  const currentYear = Number((new Moment()).year());

  // Initial state
  const initialState = {
    data: [],
    formats: [
      '| Lastname, Firstname | Affiliation | Last Active Date | [csv]',
      '| Lastname | Firstname | Affiliation | Last Active Date | [csv]',
      '| Lastname, Firstname | Affiliation | Last Active Date | [excel]',
      '| Lastname | Firstname | Affiliation | Last Active Date | [excel]',
      'Lastname, Firstname(Affiliation)Last Active Date[text]',
      'Lastname, Firstname(Affiliation)Last Active Date[browser]'
    ],
    format: '| Lastname, Firstname | Affiliation | Last Active Date | [csv]',
    toggle: false,
    year: 4,
    currentYear: currentYear,
    message: { type: 'success', message: '', show: false },
    loading: false,
    exporting: false,
    author: 3,
    ids: []
  };

  // Reducers
  const reducer = (state = initialState, action) => {
    switch(action.type) {

      // Set the current data and stop any loading
      case ACTIONS.setData:
        return { ...state, data: action.value, loading: false };

      // Flip the current toggle
      case ACTIONS.setToggle:
        return { ...state, toggle: !state.toggle };

      // Reset the current format
      case ACTIONS.setFormat:
        return { ...state, format: action.value };

      // Reset the current year
      case ACTIONS.setYear:
        return { ...state, year: action.value };

      // updates the current number of authors
      case ACTIONS.setAuthor:
        return { ...state, author: action.value };

      // Start loading
      case ACTIONS.fetchData:
        return { ...state, loading: true, ids: action.value };

      // set the current message
      case ACTIONS.setMessage:
        return { ...state, message: { ...state.message, ...action.value } };

      // Set exporting flag
      case ACTIONS.setExporting:
        return { ...state, exporting: action.value };

      // set the current loading value
      case ACTIONS.setLoading:
        return { ...state, loading: action.value };

      case ACTIONS.appReset:
        return initialState;

      // return the current state
      default: return state;
    }
  };

  return reducer;
});
