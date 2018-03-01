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

  // format reducer
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

  // format collection reducer
  const formats = (state = [{
      value: 'bibtex',
      id: '0',
      label: 'BibTeX',
      help: 'BibTeX format',
      ext: 'bbl'
    }, {
      value: 'bibtexabs',
      id: '1',
      label: 'BibTeX ABS',
      help: 'BibTeX with abstracts',
      ext: 'bbl'
    }, {
      value: 'endnote',
      id: '2',
      label: 'EndNote',
      help: 'EndNote format',
      ext: 'enw'
    }, {
      value: 'procite',
      id: '3',
      label: 'ProCite',
      help: 'ProCite format',
      ext: 'txt'
    }, {
      value: 'ris',
      id: '4',
      label: 'RIS',
      help: 'Research Information Systems (RIS) format',
      ext: 'txt'
    }, {
      value: 'refworks',
      id: '5',
      label: 'RefWorks',
      help: 'RefWorks format',
      ext: 'txt'
    }, {
      value: 'medlars',
      id: '6',
      label: 'MEDLARS',
      help: 'Medical Literature Analysis and Retrieval System (MEDLARS) format',
      ext: 'txt'
    }, {
      value: 'dcxml',
      id: '7',
      label: 'DC-XML',
      help: 'Dublin Core XML format',
      ext: 'xml'
    }, {
      value: 'refxml',
      id: '8',
      label: 'REF-XML',
      help: 'ADS link data in XML format',
      ext: 'xml'
    }, {
      value: 'refabsxml',
      id: '9',
      label: 'REFABS-XML',
      help: 'ADS records in XML format',
      ext: 'xml'
    }, {
      value: 'aastex',
      id: '10',
      label: 'AASTeX',
      help: 'LaTeX format for AAS journals',
      ext: 'txt'
    }, {
      value: 'icarus',
      id: '11',
      label: 'Icarus',
      help: 'LaTeX format for use in Icarus',
      ext: 'txt'
    }, {
      value: 'mnras',
      id: '12',
      label: 'MNRAS',
      help: 'LaTeX format for use in MNRAS',
      ext: 'txt'
    }, {
      value: 'soph',
      id: '13',
      label: 'Solar Physics',
      help: 'LaTeX format for use in Solar Physics',
      ext: 'txt'
    }
  ], action) => {
    switch (action.type) {
      case SET_FORMATS:
        return action.formats;
      default:
        return state;
    }
  };

  // error messages reducer
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

  // exports reducer (main export functionality)
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

  // main state reducer
  const main = (state = {
    query: {},
    showCloser: true,
    showSlider: true,
    origin: 'results-page',
    showReset: true,
    splitCols: true,
    autoSubmit: false
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
          autoSubmit: action.origin !== 'results-page',
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
