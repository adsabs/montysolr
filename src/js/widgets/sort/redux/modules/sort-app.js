'use strict';
define([
  'underscore',
  'immutable'
], function (_, Immutable) {

  // Action Constants
  const SET_DIRECTION = 'SET_DIRECTION';
  const SET_SORT = 'SET_SORT';
  const SET_QUERY = 'SET_QUERY';
  const SET_LOCKED = 'SET_LOCKED';

  // Action Creators

  /**
   * Takes a new sort object and dispatches the update to the state.  If only a
   * string is passed, then it will attempt to find the value in the current
   * list of options.
   *
   * @param {string|object} value - The new selected sort
   * @param {boolean} silent - if true, a change event will not be triggered
   */
  const setSort = (value, silent) => (dispatch, getState, widget) => {

    // if passed a string, convert it to one of our pre-defined options
    if (_.isString(value)) {
      const options = getState().get('SortApp').get('options');

      // attempt to find something, if not just provide the first option
      value = options.find(i => i.get('id') === value) || options[0];
    }

    dispatch({ type: SET_SORT, value });

    // only fire if we aren't being silent
    !silent && widget.onSortChange();
  };

  /**
   * Takes in a direction and dispatches a request
   * if no direction is passed, it will toggle the current direction between
   * `asc` and `desc`
   *
   * @param {string} [value] - The new direction
   * @param {boolean} silent - if true, a change event will not be triggered
   */
  const setDirection = (value, silent) => (dispatch, getState, widget) => {

    // if a direction isn't passed, then just toggle
    if (_.isUndefined(value)) {
      const direction = getState().get('SortApp').get('direction');
      value = (direction === 'asc') ? 'desc' : 'asc';
    }
    dispatch({ type: SET_DIRECTION, value });

    // only fire if we aren't being silent
    !silent && widget.onSortChange();
  };

  /**
   * Set the current query
   *
   * @param {object} value - the new query
   */
  const setQuery = (value) => ({ type: SET_QUERY, value });

  /**
   * Set the current locked value
   *
   * @param {boolean} value - the new locked value
   */
  const setLocked = (value) => (dispatch, getState) => {

    // grab the current timer (if exists)
    let timeout = getState().get('SortApp').get('lockTimer');
    if (timeout) {
      clearTimeout(timeout);
    }

    // create a new timer, which resets the locked state after a period of time
    let timer = setTimeout(() =>
      dispatch({ type: SET_LOCKED, value: false }), 30000);

    // dispatch the new state
    dispatch({ type: SET_LOCKED, value, timer: timer });
  };

  // initial state
  const initialState = Immutable.fromJS({
    options: [
      {id: 'author_count', text: 'Author Count', desc: 'sort by number of authors'},
      {id: 'bibcode', text: 'Bibcode', desc: 'sort by bibcode'},
      {id: 'citation_count', text: 'Citation Count', desc: 'sort by number of citations'},
      {id: 'classic_factor', text: 'Classic Factor', desc: 'sort using classical score'},
      {id: 'date', text: 'Date', desc: 'sort by publication date'},
      {id: 'entry_date', text: 'Entry Date', desc: 'sort by date work entered the database'},
      {id: 'read_count', text: 'Read Count', desc: 'sort by number of reads'},
      {id: 'score', text: 'Score', desc: 'sort by the relative score'}
    ],
    sort: { id: 'date', text: 'Date' },
    direction: 'asc',
    query: null,
    locked: false,
    lockTimer: null
  });

  // reducer
  const reducer = (state = initialState, action) => {
    switch (action.type) {
      case SET_SORT:
        return state.set('sort', action.value);
      case SET_DIRECTION:
        return state.set('direction', action.value);
      case SET_QUERY:
        return state.set('query', action.value);
      case SET_LOCKED:
        return state.merge({
          locked: action.value,
          lockTimer: action.timer
        });
      default: return initialState;
    }
  };

  return {
    setSort: setSort,
    setDirection: setDirection,
    setQuery: setQuery,
    setLocked: setLocked,
    initialState: initialState,
    reducer: reducer
  };
});
