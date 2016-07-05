define([
    'underscore'
], function(
  _
) {

  var actions = {};

  actions.data_requested = function(id) {
    return {
      type: 'DATA_REQUESTED',
      id: id
    }
  };

  /*
   * resets visible variable to 5
   * */
  actions.reset_visible = function(id) {
    return {
      type: 'RESET_VISIBLE',
      id: id
    };
  };

  actions.increase_visible = function(id) {

    return function(dispatch, getState) {

      var num = 5;
      //check to see if more need to be requested
      var numLoadedRecords = id ? getState().facets[id].children.length : getState().children.length;
      var numVisible = id ? getState().facets[id].state.visible : getState().state.visible;
      // want to make sure there is always an extra cycle available to
      // minimize impression of loading
      if (numLoadedRecords - (numVisible + num) <= num) {
        dispatch(this.fetch_data(id));
      }

      dispatch({
        type: 'INCREASE_VISIBLE',
        id: id,
        num: num
      });

    }.bind(this);

  };

  actions.data_received = function(data, id) {
    return {
      type: 'DATA_RECEIVED',
      data: data,
      id: id
    }
  };

  actions.toggle_facet = function(id, open) {

    return function(dispatch, getState) {

      var currentOpen = id ? getState().facets[id].state.open : getState().state.open;
      //if open was not supplied, just toggle the facet
      open = _.isBoolean(open) ? open : !currentOpen;
      var hasData = id ? getState().facets[id].children.length : getState().children.length;
      //fetch the data now
      if (open && !hasData) dispatch(this.fetch_data(id));

      dispatch({
        type: 'FACET_TOGGLED',
        open: open,
        id: id
      });

    }.bind(this);

  }

  actions.fetch_data = function(id, offset) {
    //must be overridden by widget
    throw new Error('this was supposed to be overridden!');
  }

  actions.select_facet = function(id) {
    return {
      type: 'FACET_SELECTED',
      id: id
    }
  };

  actions.unselect_facet = function(id) {
    return {
      type: 'FACET_UNSELECTED',
      id: id
    }
  };

  actions.submit_filter = function(logicOption) {
    //must be overridden by widget
    throw new Error('this was supposed to be overridden!');
  };

  actions.reset_state = function() {
    return {
      type: 'RESET_STATE'
    }
  };

  return function() {
    return Object.create(actions);
  };

})
