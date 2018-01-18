'use strict';
define([
  'es6!../constants/actionNames'
], function (ACTIONS) {

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
    count: 0,
    message: { type: 'success', message: '', show: false },
    loading: false,
    exporting: false
  };

  const reducer = (state = initialState, action) => {
    switch(action.type) {
      case ACTIONS.setData:
        return { ...state, data: action.value, loading: false };
      case ACTIONS.setToggle:
        return { ...state, toggle: !state.toggle };
      case ACTIONS.setFormat:
        return { ...state, format: action.value };
      case ACTIONS.setCount:
        return { ...state, count: action.value };
      case ACTIONS.fetchData:
        return { ...state, loading: true };
      case ACTIONS.setMessage:
        return { ...state, message: { ...state.message, ...action.value } };
      case ACTIONS.setExporting:
        return { ...state, exporting: action.value };
      case ACTIONS.setLoading:
        return { ...state, loading: action.value };
      default: return state;
    }
  };

  return reducer;
});
