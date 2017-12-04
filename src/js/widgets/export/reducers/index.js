'use strict';

/**
 * Collects and combines all reducers
 */
define([
  'underscore',
  'js/components/api_targets',
  'es6!../actions/index',
  'redux'
], function (_, ApiTargets, actions, Redux) {

  const {
    SET_QUERY,
    SET_FORMAT,
    SET_FORMATS,
    SET_PROGRESS,
    SET_COUNT,
    SET_PAGE,
    SET_IGNORE,
    SET_HAS_ERROR,
    SET_SHOW_CLOSER,
    SET_ERROR_MSG,
    SET_MAX_COUNT,
    SET_TOTAL_RECS,
    SET_BATCH_SIZE,
    REQUEST_IDS,
    RECEIVE_IDS,
    REQUEST_EXPORT,
    RECEIVE_EXPORT,
    REQUEST_FAILED,
    REQUEST_CANCELLED,
    TAKE_SNAPSHOT,
    SET_ORIGIN,
    RESET,
    HARD_RESET
  } = actions;

  const format = (state = {
    label: 'BibTeX',
    value: 'bibtex',
    id: '0'
  }, action) => {
    switch (action.type) {
      case SET_FORMAT:
        return action.format;
      default:
        return state;
    }
  };

  const formats = (state = [
    { value: 'bibtex', id: '0', label: 'BibTeX' },
    { value: 'bibtexabs', id: '1', label: 'BibTeX ABS' },
    // { value: 'ads', id: '2', label: 'Classic ADS' },
    { value: 'endnote', id: '3', label: 'Endnote' },
    { value: 'procite', id: '4', label: 'Procite' },
    { value: 'ris', id: '5', label: 'RIS' },
    { value: 'refworks', id: '6', label: 'RefWorks' },
    { value: 'medlars', id: '7', label: 'MEDLARS' },
    { value: 'dcxml', id: '8', label: 'DC-XML' },
    { value: 'refxml', id: '9', label: 'REF-XML' },
    { value: 'refabsxml', id: '10', label: 'REFABS-XML' },
    { value: 'aastex', id: '11', label: 'AASTeX' },
    { value: 'icarus', id: '12', label: 'Icarus' },
    { value: 'mnras', id: '13', label: 'MNRAS' },
    { value: 'soph', id: '14', label: 'SOPH' }
  ], action) => {
    switch (action.type) {
      case SET_FORMATS:
        return action.formats;
      default:
        return state;
    }
  };

  const error = ( state = {
    hasError: false,
    errorMsg: 'Sorry, something happened during the request. Please try again'
  }, action) => {
    switch (action.type) {
      case SET_HAS_ERROR:
        return { ...state, hasError: action.hasError };
      case SET_ERROR_MSG:
        return { ...state, errorMsg: action.errorMsg };
      default:
        return state;
    }
  };

  const exports = (state = {
    isFetching: false,
    output: '',
    progress: 0,
    ids: [],
    count: 0,
    page: 0,
    maxCount: ApiTargets._limits.ExportWidget.default,
    batchSize: ApiTargets._limits.ExportWidget.default,
    ignore: false,
    totalRecs: 0,
    snapshot: {}
  }, action) => {
    switch(action.type) {
      case REQUEST_IDS:
        return { ...state, isFetching: true, progress: 0 };
      case RECEIVE_IDS:
        return { ...state, isFetching: false, progress: 100, ids: action.ids };
      case SET_TOTAL_RECS:
        return { ...state, totalRecs: action.totalRecs };
      case REQUEST_EXPORT:
        return { ...state, isFetching: true, progress: 0 };
      case RECEIVE_EXPORT:
        return {
          ...state,
          isFetching: false,
          progress: 100,
          output: action.exports,
          ignore: false
        };
      case REQUEST_FAILED:
        return {
          ...state,
          isFetching: false,
          progress: 0,

        };
      case REQUEST_CANCELLED:
        return {
          ...state,
          ignore: true,
          isFetching: false,
          progress: 0,
          output: ''
        };
      case SET_IGNORE:
        return { ...state, ignore: action.ignore };
      case SET_PROGRESS:
        return { ...state, progress: action.progress };
      case SET_BATCH_SIZE:
        return { ...state, batchSize: action.batchSize };
      case SET_COUNT:
        return {
          ...state,
          count: (action.count > state.maxCount) ? state.maxCount : action.count
        };
      case SET_MAX_COUNT:
        return {
          ...state,
          maxCount: action.maxCount > state.totalRecs ? state.totalRecs : action.maxCount
        };
      case SET_PAGE:
        return { ...state, page: action.page };
      case TAKE_SNAPSHOT:
        return { ...state, snapshot: action.snapshot };
      case RESET: {
        return { ...state.snapshot, output: '', snapshot: state.snapshot }
      }
      default:
        return state;
    }
  };

  const main = (state = {
    query: {},
    showCloser: true,
    showSlider: true,
    origin: 'results-page',
    showReset: true,
    splitCols: true
  }, action) => {
    switch (action.type) {
      case SET_SHOW_CLOSER:
        return { ...state, showCloser: action.showCloser };
      case SET_QUERY:
        return { ...state, query: action.query };
      case SET_ORIGIN:
        return {
          ...state,
          showCloser: action.origin === 'results-page',
          showSlider: action.origin === 'results-page',
          splitCols: action.origin === 'results-page',
          showReset: action.origin === 'results-page',
          origin: action.origin
        };
      default:
        return state;
    }
  };

  const appReducer = Redux.combineReducers({
    format, formats, error, exports, main
  });

  const rootReducer = (state, action) => {
    if (action.type === HARD_RESET) {
      state = undefined;
    }

    return appReducer(state, action);
  };

  return rootReducer;
});
