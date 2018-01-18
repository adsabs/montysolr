'use strict';
define([
  'underscore',
  'es6!../models/authorAffiliation',
  'es6!../constants/actionNames',
  'filesaver'
], function (_, authorAffiliation, ACTIONS) {

  const createSelectedString = function (arr) {
    let out = [];
    _.forEach(arr, entry => {
      let val = [];
      if (!entry.selected) {
        return;
      }

      let selectedDate = _.find(entry.lastActiveDates, { selected: true });
      selectedDate = selectedDate ? selectedDate.date : ' ';

      let selectedAffiliations = _.filter(entry.affiliations, { selected: true });

      if (_.isEmpty(selectedAffiliations)) {
        out.push([entry.author, ' ', selectedDate].join('|'));
      }

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

  const swap = function (arr, idx, val) {
    return [
      ...arr.slice(0, idx), val, ...arr.slice(idx + 1)
    ];
  };

  const reset = _.memoize((data) => {
    return data.map((d, i) => ({
      ...d,
      selected: true,
      affiliations: d.affiliations.map((a, i) => ({ ...a, selected: 0 === i })),
      lastActiveDates: d.lastActiveDates.map((l, i) => ({ ...l, selected: 0 === i}))
    }));
  }, () => 0);

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

  const toggleAffiliationRow = (data, affIdx, authorIdx, authorData, innerData) => {
    // an inner element was toggled
    let affiliations = swap(data[authorIdx].affiliations, affIdx, {
      ...innerData,
      selected: !innerData.selected
    });

    let selected =
      _.any(affiliations, { selected: true }) ||
      _.any(data[authorIdx].lastActiveDates, { selected: true });

    return swap(data, authorIdx, {
      ...authorData,
      selected: selected,
      affiliations: affiliations
    });
  };

  const toggleLastActiveDateRow = (data, authorIdx, dateIdx, innerData, authorData) => {
    // get the current ON date
    let currentSelectedIdx = _.findIndex(data[authorIdx].lastActiveDates, {
      selected: true
    });

    // an inner element was toggled
    let lastActiveDates = swap(data[authorIdx].lastActiveDates, dateIdx, {
      ...innerData,
      selected: !innerData.selected
    });

    // switch off any ON dates
    if (currentSelectedIdx >= 0){
      lastActiveDates = swap(lastActiveDates, currentSelectedIdx, {
        ...lastActiveDates[currentSelectedIdx],
        selected: false
      });
    }

    let selected =
      _.any(lastActiveDates, { selected: true }) ||
      _.any(data[authorIdx].affiliations, { selected: true });

    return swap(data, authorIdx, {
      ...authorData,
      selected: selected,
      lastActiveDates: lastActiveDates
    });
  };

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

  const openInNewTab = (data) => {
    let win = window.open();
    win.document.write(data);
    win.focus();
  };

  const actions = {
    reset: () => (dispatch, getState) => {
      const { data } = getState();
      dispatch({ type: ACTIONS.setData, value: reset(data) });
    },
    toggleAll: () => (dispatch, getState) => {
      const { data, toggle } = getState();
      dispatch({ type: ACTIONS.setData, value: toggleAll(data, toggle) });
      dispatch({ type: ACTIONS.setToggle });
    },
    setAffiliationData: (data) => (dispatch) => {
      data = _(data)
        .groupBy('authorName')
        .map((affs, author) => authorAffiliation.create(author, affs))
        .value();

      dispatch({ type: ACTIONS.setData, value: data });
    },
    closeWidget: () => (dispatch, getState, widget) => {
      widget.closeWidget();
    },
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

  actions.toggleSelection = (authorData, innerData) => (dispatch, getState) => {
    let { data } = getState();
    let affIdx = -1, dateIdx = -1, newData = [];

    let authorIdx = _.indexOf(data, authorData);
    if (innerData) {
      affIdx = _.indexOf(authorData.affiliations, innerData);
      dateIdx = _.indexOf(authorData.lastActiveDates, innerData);
    }

    if (affIdx >= 0) {
      newData = toggleAffiliationRow(data, affIdx, authorIdx, authorData, innerData);
    } else if (dateIdx >= 0) {
      newData = toggleLastActiveDateRow(data, authorIdx, dateIdx, innerData, authorData);
    } else if (authorIdx >= 0) {
      newData = toggleAuthorRow(data, authorIdx, authorData);
    } else {
      newData = data;
    }

    dispatch({ type: ACTIONS.setData, value: newData });
  };

  actions.doExport = () => (dispatch, getState, widget) => {
    const { data, format } = getState();

    let file = getFileType(format);

    dispatch({ type: ACTIONS.setExporting, value: true });

    widget.exportAffiliationData({
      selected: createSelectedString(data),
      format: format
    }).done((res) => {
      if (file.ext === 'browser') {
        openInNewTab(res);
        dispatch(actions.showMessage(
          'info',
          'If new tab doesn\'t appear, you will need to allow popups'
        ));

      } else {
        let blob = new Blob([res], { type: `${file.type};charset=utf=8` });
        saveAs(blob, `authorAffiliations.${file.ext}`);
        dispatch(actions.showMessage('success', 'Export Successful!'));
      }
    }).fail(() => {
      actions.showMessage('danger', 'Something happened with the request, please try again');
    }).always(() => dispatch({ type:ACTIONS.setExporting, value: false }));
    actions.showMessage('info', 'Exporting...');
  };

  return actions;
});
