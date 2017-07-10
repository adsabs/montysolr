'use strict';
define([
  'underscore',
  'backbone',
  'react',
  'react-dom',
  'redux',
  'react-redux',
  'redux-thunk',
  'js/widgets/base/base_widget',
  'js/mixins/link_generator_mixin',
  './reducers',
  './actions',
  'es6!./components/app.jsx'
], function (
  _, Backbone, React, ReactDOM, Redux, ReactRedux,
  ReduxThunk, BaseWidget, LinkGenerator, reducers, actions, App) {

  var View = Backbone.View.extend({
    initialize: function (options) {

      // provide this with all the options passed in
      _.assign(this, options);
    },
    render: function () {

      // create provider component, that passes the store to <App>
      ReactDOM.render(
        <ReactRedux.Provider store={this.store}>
          <App/>
        </ReactRedux.Provider>,
        this.el
      );
      return this;
    },
    destroy: function () {

      // on destroy, make sure the React DOM is unmounted
      ReactDOM.unmountComponentAtNode(this.el);
    }
  });

  var Widget = BaseWidget.extend({
    initialize: function (options) {
      this.options = options || {};
      this.NAME = 'ShowResources';

      // create thunk middleware, passing in `this` as extra argument
      var middleware = Redux.applyMiddleware(
        ReduxThunk.default.withExtraArgument(this));

      // create the redux store using reducers and applying middleware
      this.store = Redux.createStore(reducers, middleware);

      // create the view passing the store as the only property
      this.view = new View({
        store: this.store
      });
    },
    activate: function (beehive) {
      this.setBeeHive(beehive);
      var pubsub = beehive.getService('PubSub');
      _.bindAll(this, [
        'processResponse',
        'onDisplayDocuments',
        'handleFeedback'
      ]);

      pubsub.subscribe(pubsub.DISPLAY_DOCUMENTS, this.onDisplayDocuments);
      pubsub.subscribe(pubsub.DELIVERING_RESPONSE, this.processResponse);
      pubsub.subscribe(pubsub.FEEDBACK, this.handleFeedback);
    },
    processResponse: function (apiResponse) {
      this.store.dispatch(actions.processResponse(apiResponse));
    },
    onDisplayDocuments: function (apiQuery) {
      this.store.dispatch(actions.displayDocuments(apiQuery));
    },
    handleFeedback: function (feedback) {
      this.store.dispatch(actions.handleFeedback(feedback));
    }
  });

  _.extend(Widget.prototype, LinkGenerator);
  return Widget;
});
