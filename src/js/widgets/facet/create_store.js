define(
    [
      'underscore',
      'redux',
      'redux-thunk',
      'js/widgets/facet/reducers'
    ],
  function(
    _,
    Redux,
    Thunk,
    Reducer
  ) {

    return function createStore(config) {
      // pass in specific default config vars (e.g. preprocessors)
      // these come from the facet widget's initialize method
      var config = {
        config: _.assign({}, Reducer.defaultState.config, config),
      };
      var store = _.assign({}, _.cloneDeep(Reducer.defaultState), config);
      return Redux.createStore(Reducer,
        store,
        Redux.applyMiddleware(Thunk.default)
      );
    }
    return createStore;

  });
