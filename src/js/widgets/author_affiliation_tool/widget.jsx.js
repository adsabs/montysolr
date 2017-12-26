'use strict';
define([
  'jquery',
  'underscore',
  'backbone',
  'react',
  'react-redux',
  'react-dom',
  'redux',
  'redux-thunk',
  'js/components/api_targets',
  'js/components/api_query',
  'js/components/api_request',
  'js/widgets/base/base_widget',
  'es6!./containers/App.jsx',
  'es6!./constants/actionNames',
  'es6!./actions/index',
  'es6!./reducers/index'
], function (
  $, _, Backbone, React, ReactRedux, ReactDOM, Redux, ReduxThunk,
  ApiTargets, ApiQuery, ApiRequest, BaseWidget, App, ACTIONS, actions, reducers
) {

  var View = Backbone.View.extend({
    initialize: function (options) {
      _.assign(this, options);
    },
    render: function () {
      console.log('RENDER');
      ReactDOM.render(
        <ReactRedux.Provider store={this.store}>
          <App/>
        </ReactRedux.Provider>,
        this.el
      );
      return this;
    },
    destroy: function () {
      ReactDOM.unmountComponentAtNode(this.el);
    }
  });

  var Widget = BaseWidget.extend({
    initialize: function (options) {
      options = options || {};
      var middleware = Redux.applyMiddleware(
        ReduxThunk.default.withExtraArgument(this)
      );
      const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__
        || compose;
      this.store = Redux.createStore(reducers, composeEnhancers(middleware));
      this.view = new View({ store: this.store });
    },
    activate: function (beehive) {
      this.setBeeHive(beehive);
      this.activateWidget();
    },
    renderWidgetForCurrentQuery: function ({ currentQuery }) {
      const pubsub = this.getPubSub();
      if (currentQuery) {
        const q = currentQuery.clone();
        q.unlock();
        q.set('rows', 1000);
        q.set('fl', 'bibcode');
        const req = this.composeRequest(q);
        req.set('options', {
          done: res => {
            const ids = _.map(res.response.docs, 'bibcode');
            this.fetchAffiliationData(ids);
          },
          fail: () => this.onError(),
          always: () => {
            this.store.dispatch({ type: ACTIONS.setLoading, value: false });
          }
        });

        pubsub.publish(pubsub.EXECUTE_REQUEST, req);
        this.store.dispatch({ type: ACTIONS.fetchData });
      }
    },
    renderWidgetForListOfBibcodes: function (ids) {
      this.fetchAffiliationData(ids);
    },
    fetchAffiliationData: function (ids) {
      const pubsub = this.getPubSub();
      const $dd = $.Deferred();

      this.store.dispatch({ type: ACTIONS.setCount, value: ids.length });
      this.store.dispatch({ type: ACTIONS.fetchData });

      const req = new ApiRequest({
        target: ApiTargets.AUTHOR_AFFILIATION_SEARCH,
        query: new ApiQuery({ bibcode: ids }),
        options : {
          type : 'POST',
          contentType : 'application/json',
          done: (...args) => $dd.resolve(...args),
          fail: (...args) => $dd.reject(...args),
          always: (...args) => $dd.always(...args)
        }
      });

      $dd.done(data => {
        try {
          data = JSON.parse(data).data;
          this.store.dispatch(actions.setAffiliationData(data));
        } catch (e) {
          console.error(e);
        }
      });

      $dd.fail(() => this.onError());

      $dd.always(() => this.store.dispatch({
        type: ACTIONS.setLoading, value: false
      }));

      pubsub.publish(pubsub.EXECUTE_REQUEST, req);
      return $dd.promise();
    },
    exportAffiliationData: function (data) {
      const pubsub = this.getPubSub();
      const $dd = $.Deferred();

      const req = new ApiRequest({
        target: ApiTargets.AUTHOR_AFFILIATION_EXPORT,
        query: new ApiQuery(data),
        options : {
          type : 'POST',
          contentType : 'application/json',
          dataType: 'text',
          done: (...args) => $dd.resolve(...args),
          fail: (...args) => $dd.reject(...args),
          always: (...args) => $dd.always(...args)
        }
      });

      $dd.fail(() => this.onError());

      $dd.always(() => this.store.dispatch({
        type: ACTIONS.setLoading, value: false
      }));

      pubsub.publish(pubsub.EXECUTE_REQUEST, req);
      return $dd.promise();
    },
    closeWidget: function () {
      const pubsub = this.getPubSub();
      pubsub.publish(pubsub.NAVIGATE, "results-page");
    },
    onError: function () {
      const { dispatch } = this.store;
      dispatch(actions.showMessage(
        'danger',
        'Something happened during the request, please try again',
        10000
      ));
    }
  });

  return Widget;
});
