'use strict';
define([], function () {

  // set of targets corresponding to actions (helps with dispatching)
  let target = {
    fetchData: 'FETCHING_DATA',
    setLoading: 'SET_LOADING',
    setData: 'SET_DATA',
    setToggle: 'SET_TOGGLE',
    setFormat: 'SET_FORMAT',
    setCount: 'SET_COUNT',
    setMessage: 'SET_MESSAGE',
    setExporting: 'SET_EXPORTING'
  };

  return target;
});
