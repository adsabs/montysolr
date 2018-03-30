'use strict';
define([
  'underscore',
  'backbone',
  'analytics',
  'react',
  'react-redux',
  'react-dom',
  'redux',
  'es6!./redux/configure-store',
  'es6!./redux/modules/sort-app',
  'js/components/api_query',
  'js/widgets/base/base_widget',
  'js/components/api_feedback',
  'es6!./containers/sort-container'
], function (
  _, Backbone, analytics, React, ReactRedux, ReactDOM, Redux, configureStore,
  SortApp, ApiQuery, BaseWidget, ApiFeedback, SortContainer
) {

  /**
   * Main App View
   *
   * This view is the entry point of the app, it will pass the
   * store down using a <provider></provider> higher-order component.
   *
   * All sub-components will automatically have `store` available via context
   */
  const View = Backbone.View.extend({
    initialize: function (options) {
      _.assign(this, options);
    },
    render: function () {
      ReactDOM.render(
        <ReactRedux.Provider store={this.store}>
          <SortContainer/>
        </ReactRedux.Provider>,
        this.el
      );
      return this;
    },
    destroy: function () {
      ReactDOM.unmountComponentAtNode(this.el);
    }
  });

  /**
   * Backbone widget which does the wiring between the react view and
   * the application
   */
  const Widget = BaseWidget.extend({

    /**
     * Initialize the widget
     */
    initialize: function () {

      // create the store, using the configurator
      this.store = configureStore(this);

      // create the view, passing in store
      this.view = new View({ store: this.store });

      this.onSortChange = _.debounce(this.onSortChange, 500);
      this.handleFeedback = _.bind(this.handleFeedback, this);
    },

    /**
     * Activate the widget
     *
     * @param {object} beehive
     */
    activate: function (beehive) {
      this.setBeeHive(beehive);
      this.activateWidget();
      const pubsub = this.getPubSub();
      pubsub.subscribe(pubsub.FEEDBACK, this.handleFeedback);
    },

    /**
     * Handler that is called internally which will take the current state and
     * update the current query's sort parameter.
     *
     * It will only replace the first entry in the sort (up to the first `,`)
     *
     * Finally it will start a new search using the updated sort param and the
     * stored query.
     */
    onSortChange: function () {
      const pubsub = this.getPubSub();
      const app = this.store.getState().get('SortApp');
      const sort = app.get('sort').get('id');
      const dir = app.get('direction');
      let query = app.get('query');
      let newSort = sort + ' '  + dir;

      // try local app storage if we don't have one stored
      if (_.isNull(query)) {
        query = this.getBeeHive().getObject('AppStorage').getCurrentQuery();
      }

      // play nice with the sort, don't destroy what's there
      // only replace the first entry (primary sort)
      if (!_.isUndefined(query.sort) && !_.isEmpty(query.sort)) {
        let arr = query.sort[0].split(/,\s?/).slice(1);

        arr.unshift(newSort);
        newSort = arr.join(', ');
      }

      // don't do anything if we weren't able to find a query
      if (query) {
        query.sort = [newSort];
        pubsub.publish(pubsub.START_SEARCH, new ApiQuery(query));
        analytics('send', 'event', 'interaction', 'sort-applied', query.sort);
      }
    },

    /**
     * Called when the pubsub issues a feedback event.
     * Watch for search cycle events, and if a new one starts, grab the query
     * from it and disable the dropdown (prevents further updates, mid-cycle)
     *
     * Once the cycle is finished, for whatever reason, unlock the component.
     *
     * @param {ApiFeedback} feedback - the feedback object
     */
    handleFeedback: function (feedback) {
      const { dispatch } = this.store;
      const { setQuery, setSort, setDirection, setLocked } = SortApp;

      switch(feedback.code) {
        case ApiFeedback.CODES.SEARCH_CYCLE_STARTED: {
          let query = feedback && feedback.query && feedback.query.toJSON();
          if (query) {
            let sortStr = this.extractSort(query.sort[0]);
            dispatch(setQuery(query));
            dispatch(setSort(sortStr.sort, true));
            dispatch(setDirection(sortStr.direction, true));
            dispatch(setLocked(true));
          }
        } break;
        case ApiFeedback.CODES.SEARCH_CYCLE_PROGRESS: {
          dispatch(setLocked(true));
        } break;
        case ApiFeedback.CODES.SEARCH_CYCLE_FAILED_TO_START:
        case ApiFeedback.CODES.SEARCH_CYCLE_FINISHED: {
          dispatch(setLocked(false));
        }
      }
    },

    /**
     * Splits a sort string into parts and then returns the first entry
     * mapped to an object
     *
     * @param {string} sort - the string to break apart
     * @returns {{sort: string, direction: string}}
     */
    extractSort: function (sort) {

      // grab only the first sort and break it apart
      let sortStr = sort.split(/,\s?/)[0] || 'date desc';
      sortStr = sortStr.split(' ');
      return { sort: sortStr[0], direction: sortStr[1] };
    }
  });

  return Widget;
});
