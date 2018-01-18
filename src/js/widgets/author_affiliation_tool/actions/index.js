'use strict';
define([
  'underscore',
  'es6!../models/authorAffiliation',
  'es6!../constants/actionNames',
  'filesaver'
], function (_, authorAffiliation, ACTIONS) {

  /**
   * Format the data into something the server will accept
   *
   * @param {Array} arr
   * @returns {Array}
   */
  const createSelectedString = function (arr) {
    let out = [];
    _.forEach(arr, entry => {
      let val = [];
      if (!entry.selected) {
        return;
      }

      // get only the selected lastActiveDates
      let selectedDate = _.find(entry.lastActiveDates, { selected: true });
      selectedDate = selectedDate ? selectedDate.date : ' ';

      // get the selected affiliations
      let selectedAffiliations = _.filter(entry.affiliations, { selected: true });

      // if no affiliations, then just make a ' ' instead
      if (_.isEmpty(selectedAffiliations)) {
        out.push([entry.author, ' ', selectedDate].join('|'));
      }

      // pull everything together into a big pipe-delimited string
      _.forEach(selectedAffiliations, a => {
        if (!a.selected) { return; }
        val.push(entry.author);
        val.push(a.name);
        val.push(selectedDate);
        out.push(val.join('|'));
      });
    });
    return out;
  };

  /**
   * insert a value into an array at a certain index, without mutation,
   * returning a new array containing the new value.
   *
   * @param {Array} arr
   * @param {Number} idx
   * @param {*} val
   * @returns {Array} the new array
   */
  const swap = function (arr, idx, val) {
    return [
      ...arr.slice(0, idx), val, ...arr.slice(idx + 1)
    ];
  };

  /**
   * reset the current selected states of all the data entries
   *
   * Memoized so that it will only ever calculate the first time it is clicked,
   * after that the result should be cached.
   *
   * @param {Array} data - the current array of data points
   */
  const reset = _.memoize((data) => {

    // go through all the entries
    return data.map((d, i) => ({
      ...d,

      // set this author to be selected (that is default)
      selected: true,

      // for affiliations only the first element is selected
      affiliations: d.affiliations.map((a, i) => ({ ...a, selected: 0 === i })),

      // same for last active dates (only first is selected)
      lastActiveDates: d.lastActiveDates.map((l, i) => ({ ...l, selected: 0 === i}))
    }));
  }, () => 0);

  /**
   * Toggle all the current selected states
   *
   * @param {Array} data - the current array of data points
   * @param {boolean} toggle - flag to toggle entries
   */
  const toggleAll = _.memoize((data, toggle) => {
    return data.map(d => ({
      ...d,
      selected: toggle,
      affiliations: d.affiliations.map(a => ({ ...a, selected: toggle })),
      lastActiveDates: d.lastActiveDates.map((l, i) => ({
        ...l,
        selected: toggle && 0 === i
      }))
    }));
  }, (data, toggle) => toggle);

  /**
   * This will toggle the selected state the affiliation section of a particular
   * row
   *
   * @param {Array} data - full data set
   * @param {number} affIdx - the id of the affiliation section
   * @param {number} authorIdx - the id of the parent (author) row
   * @param {object} authorData - the parent row data
   * @param {object} innerData - the affiliation data
   * @returns {Array} the new data set with the affiliation section (at index) toggled
   */
  const toggleAffiliationRow = (data, affIdx, authorIdx, authorData, innerData) => {

    /*
      Take the current affiliations (at index) and swap out the selected one
      for a modified version (toggled).

      The resulting array will be used later as the set of affiliations for the
      new author row.
     */
    let affiliations = swap(data[authorIdx].affiliations, affIdx, {
      ...innerData,
      selected: !innerData.selected
    });

    /*
      Determine if the entire author row should be selected (far left toggle)

      If any of the affiliations or lastActiveDates are selected, it will
      set the author to be selected as well.
     */
    let selected =
      _.any(affiliations, { selected: true }) ||
      _.any(data[authorIdx].lastActiveDates, { selected: true });

    /*
      TODO: Find a more elegant solution, this is quite expensive
      Take the full data set, and swap out the current author row with a new set
      of data.  The new set will contain our modified (toggled) affiliation data.

      Returns a new (full) set of data
     */
    return swap(data, authorIdx, {
      ...authorData,
      selected: selected,
      affiliations: affiliations
    });
  };

  /**
   * This will toggle the lastActiveDate section of a particular row.
   *
   * If necessary it will also set the author to be selected/deselected.
   *
   * @param {Array} data - full data set
   * @param {number} dateIdx - the id of the lastActiveDate section
   * @param {number} authorIdx - the id of the parent (author) row
   * @param {object} authorData - the parent row data
   * @param {object} innerData - the affiliation data
   * @returns {Array} the new data set with the lastActiveDate section (at index) toggled
   */
  const toggleLastActiveDateRow = (data, authorIdx, dateIdx, innerData, authorData) => {

    // get the current ON date
    let currentSelectedIdx = _.findIndex(data[authorIdx].lastActiveDates, {
      selected: true
    });

    /*
      Create a new array of lastActiveDates based on the current one.
      Also, flip the one at `dateIdx`.
     */
    let lastActiveDates = swap(data[authorIdx].lastActiveDates, dateIdx, {
      ...innerData,
      selected: !innerData.selected
    });

    // Only allow one selected lastActiveDate (functions like radio buttons)
    if (currentSelectedIdx >= 0){
      lastActiveDates = swap(lastActiveDates, currentSelectedIdx, {
        ...lastActiveDates[currentSelectedIdx],
        selected: false
      });
    }

    // figure out if the whole row (author) should be selected
    let selected =
      _.any(lastActiveDates, { selected: true }) ||
      _.any(data[authorIdx].affiliations, { selected: true });

    /*
      Finally, return a new set of data that has all the old values except for
      any changes done at `authorIdx`.

      The author might have been selected/deselected and a new set of lastActiveDates
      provided.
     */
    return swap(data, authorIdx, {
      ...authorData,
      selected: selected,
      lastActiveDates: lastActiveDates
    });
  };

  /**
   * Returns a new array of data with the author row at a particular index
   * selected/deselected.
   *
   * It will also determine the selected states of the inner sections and
   * if the parent row is deselected, it will deselect them too.
   *
   * The main data array containing all the author/affiliation information is never
   * mutated.  So we have to swap out values and create new sub-arrays as necessary
   * when changes are needed.
   *
   * @param {Array} data - full data set
   * @param {number} authorIdx - the id of the parent (author) row
   * @param {number} authorData - the parent row data
   * @returns {Array} the new data set with the author row (at index) toggled
   */
  const toggleAuthorRow = (data, authorIdx, authorData) => {
    return swap(data, authorIdx, {
      ...authorData,

      selected: !authorData.selected,

      // flip all the affiliations to false if turning off author
      affiliations: (authorData.selected) ? data[authorIdx].affiliations.map(a => ({
        ...a,
        selected: false
      })) : data[authorIdx].affiliations,

      // same here with active dates (flip them all off)
      lastActiveDates: (authorData.selected) ? data[authorIdx].lastActiveDates.map(l => ({
        ...l,
        selected: false
      })) : data[authorIdx].lastActiveDates
    });
  };

  /**
   * Based on the format string, determine the file type
   *
   * @param {string} formatString - the format to be parsed
   * @returns {{ext:string, type:string}} extension and file type
   */
  const getFileType = (formatString) => {
    let type = /\[(.*)]/.exec(formatString);

    switch(type[1]) {
      case 'csv':
        return { ext: 'csv', type: 'text/csv' };
      case 'excel':
        return { ext: 'xls', type: 'application/vnd.ms-excel' };
      case 'browser':
        return { ext: 'browser', type: 'text/plain' };
      default:
        return { ext: 'txt', type: 'text/plain' }
    }
  };

  /**
   * Open the raw output in a new window/tab
   *
   * @param {string} data - raw data to be written
   */
  const openInNewTab = (data) => {
    let win = window.open();
    win.document.write(data);
    win.focus();
  };

  const actions = {

    /**
     * Reset the current data set
     */
    reset: () => (dispatch, getState) => {
      const { data } = getState();
      dispatch({ type: ACTIONS.setData, value: reset(data) });
    },

    /**
     * Toggle all of the current authors/affilations/lastActiveDates and update
     * the current data set
     */
    toggleAll: () => (dispatch, getState) => {
      const { data, toggle } = getState();
      dispatch({ type: ACTIONS.setData, value: toggleAll(data, toggle) });
      dispatch({ type: ACTIONS.setToggle });
    },

    /**
     * Normalizing the raw data from the server.  This will group the data by
     * authorName and the create a new row for each grouping.
     *
     * @param {array} data - the raw data from the server
     */
    setAffiliationData: (data) => (dispatch) => {
      data = _(data)
        .groupBy('authorName')
        .map((affs, author) => authorAffiliation.create(author, affs))
        .value();

      dispatch({ type: ACTIONS.setData, value: data });
    },

    /**
     * Close the widget
     */
    closeWidget: () => (dispatch, getState, widget) => {
      widget.closeWidget();
    },

    /**
     * Show a message with a timeout
     *
     * @param {string} [type='success'] - the type of alert
     * @param {string} [message=''] - the message
     * @param {number} [timeout=5000] - the timeout of the alert
     */
    showMessage: (type='success', message='', timeout=5000) => (dispatch) => {
      _.delay(() =>
        dispatch({ type: ACTIONS.setMessage, value: { show: false }})
      , timeout);
      dispatch({ type: ACTIONS.setMessage, value: {
        type: type,
        message: message,
        show: true
      }});
    }
  };

  /**
   * Toggle a particular selection.  This will be dispatched when a user selects
   * a checkbox/radio on the ui.  We need to figure out if the user is selecting
   * a parent element or a child element, in the latter case, we will need to do
   * additional work to make sure that the parent element is toggled accordingly.
   *
   * @param authorData
   * @param innerData
   */
  actions.toggleSelection = (authorData, innerData) => (dispatch, getState) => {
    let { data } = getState();
    let affIdx = -1, dateIdx = -1, newData = [];

    // find the author index
    let authorIdx = _.indexOf(data, authorData);

    // if true, we are inside a child element, update indexes
    if (innerData) {
      affIdx = _.indexOf(authorData.affiliations, innerData);
      dateIdx = _.indexOf(authorData.lastActiveDates, innerData);
    }

    // if an index is found for the innerData, run the toggle method for that section
    if (affIdx >= 0) {
      newData = toggleAffiliationRow(data, affIdx, authorIdx, authorData, innerData);
    } else if (dateIdx >= 0) {
      newData = toggleLastActiveDateRow(data, authorIdx, dateIdx, innerData, authorData);
    } else if (authorIdx >= 0) {
      newData = toggleAuthorRow(data, authorIdx, authorData);
    } else {
      newData = data;
    }

    // update the state with the new data set, which will update the table on the ui
    dispatch({ type: ACTIONS.setData, value: newData });
  };

  /**
   * Perform the export, this will depend on the export format that was chosen
   * by the user.
   */
  actions.doExport = () => (dispatch, getState, widget) => {
    const { data, format } = getState();

    // get the file extension and type
    let file = getFileType(format);

    // start export loading
    dispatch({ type: ACTIONS.setExporting, value: true });

    // do the actual export using the method on the widget
    widget.exportAffiliationData({
      selected: createSelectedString(data),
      format: format
    })

    // after the export, decide how to show/download the data
    .done((res) => {

      // if browser, then open a new tab and show message if it's blocked
      if (file.ext === 'browser') {
        openInNewTab(res);
        dispatch(actions.showMessage(
          'info',
          'If new tab doesn\'t appear, you will need to allow popups'
        ));
      } else {

        // otherwise, we can just create a file for download
        let blob = new Blob([res], { type: `${file.type};charset=utf=8` });
        saveAs(blob, `authorAffiliations.${file.ext}`);

        // show a message about successful download
        dispatch(actions.showMessage('success', 'Export Successful!'));
      }
    })

    // on failure, show error message
    .fail(() => {
      actions.showMessage('danger', 'Something happened with the request, please try again');
    })

    // always stop loading
    .always(() => dispatch({ type:ACTIONS.setExporting, value: false }));
    actions.showMessage('info', 'Exporting...');
  };

  return actions;
});
