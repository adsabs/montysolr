'use strict';
define([], function () {

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

  let handler = {
    get: (target, key) => {
      if (target.hasOwnProperty(key)) {
        return target[key];
      } else {
        throw new Error(`Fired a wrong actionname: ${key}. Available Actions: ${Object.keys(target)}`);
      }
    }
  };

  return new Proxy(target, handler);
});
