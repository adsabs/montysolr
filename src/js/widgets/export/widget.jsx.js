'use strict';
define([
  'jquery',
  'underscore',
  'backbone',
  'react',
  'react-dom',
  'redux',
  'react-redux',
  'redux-thunk',
  'js/widgets/base/base_widget',
  'es6!./reducers/index',
  'es6!./actions/index',
  'es6!./containers/App.jsx',
  'js/components/api_query',
  'js/components/api_targets',
  'js/components/api_feedback',
  'hbs!js/widgets/export/templates/classic_submit_form'
], function ($, _, Backbone, React, ReactDOM, Redux, ReactRedux,
             ReduxThunk, BaseWidget, reducers, actions, App, ApiQuery, ApiTargets,
             ApiFeedback, ClassicFormTemplate) {

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

      // create thunk middleware, passing in `this` as extra argument
      var middleware = Redux.applyMiddleware(
        ReduxThunk.default.withExtraArgument(this));

      // create the redux store using reducers and applying middleware
      const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || Redux.compose;
      this.store = Redux.createStore(reducers, composeEnhancers(middleware));

      // create the view passing the store as the only property
      this.view = new View({ store: this.store });
    },
    activate: function (beehive) {
      this.setBeeHive(beehive);
      const pubsub = this.getPubSub();
      const { dispatch } = this.store;
      const { setQuery } = actions;
      this.activateWidget();

      pubsub.subscribe(pubsub.INVITING_REQUEST, (query) => (
        dispatch(setQuery(query.toJSON()))));
      this.attachGeneralHandler(this.onApiFeedback);
    },
    onApiFeedback: function (feedback) {
      this.store.dispatch(actions.requestFailed(feedback));
    },

    /**
     * Called from navigator when the widget is needed to get the bibcodes to
     * use for the export.  This will be used as an interaction point to the
     * redux store only.
     *
     * @param {object} info
     */
    renderWidgetForCurrentQuery: function ({ currentQuery, numFound, format }) {
      const { dispatch } = this.store;
      const {
        fetchUsingQuery, fetchUsingIds, findAndSetFormat, hardReset,
        setCount, setQuery, setTotalRecs, takeSnapshot, setOrigin
      } = actions;

      dispatch(hardReset());
      dispatch(setOrigin(this.componentParams && this.componentParams.origin));
      dispatch(setQuery(currentQuery.toJSON()));
      dispatch(findAndSetFormat(format));
      dispatch(setCount(numFound));
      dispatch(setTotalRecs(numFound));
      if (format !== 'other') {
        dispatch(takeSnapshot());
        dispatch(fetchUsingQuery())
          .then(() => dispatch(fetchUsingIds())
            .always(() => dispatch(takeSnapshot()))
          );
      } else {
        dispatch(takeSnapshot());
      }
    },
    closeWidget: function () {
      const pubsub = this.getPubSub();
      pubsub.publish(pubsub.NAVIGATE, "results-page");
    },
    _executeApiRequest: function (req) {
      const $dd = $.Deferred();

      const pubsub = this.getPubSub();
      pubsub.subscribeOnce(pubsub.DELIVERING_RESPONSE, (res) => $dd.resolve(res));
      pubsub.publish(pubsub.EXECUTE_REQUEST, req);
      return $dd.promise();
    },
    renderWidgetForListOfBibcodes : function (recs, data) {
      const { dispatch } = this.store;
      const {
        receiveIds, findAndSetFormat, fetchUsingIds, hardReset,
        setCount, setTotalRecs, takeSnapshot, setOrigin
      } = actions;

      dispatch(hardReset());
      dispatch(setOrigin(this.componentParams && this.componentParams.origin));
      dispatch(receiveIds(recs));
      dispatch(findAndSetFormat(data.format));
      dispatch(setCount(recs.length));
      dispatch(setTotalRecs(recs.length));
      if (data.format !== 'other') {
        dispatch(fetchUsingIds()).done(() => dispatch(takeSnapshot()));
      } else {
        dispatch(takeSnapshot());
      }
    },
    openClassicExports : function(options){
      if (options.bibcodes){
        var $form =  $(ClassicFormTemplate({
          bibcode: options.bibcodes,
          exportLimit : ApiTargets._limits.ExportWidget.limit
        }));
        $("body").append($form);
        $form.submit();
        $form.remove();
      }
      else if (options.currentQuery ) {
        var q = options.currentQuery.clone();
        q.set("rows", ApiTargets._limits.ExportWidget.limit);
        q.set("fl", "bibcode");
        var req = this.composeRequest(q);

        this._executeApiRequest(req)
          .done(function(apiResponse) {
            // export documents by their ids
            var ids = _.map(apiResponse.get('response.docs'), function(d) {
              return d.bibcode
            });
            var $form =  $(ClassicFormTemplate({
              bibcode: ids,
              exportLimit: ids.length }));
            //firefox requires form to actually be in the dom when it is submitted
            $("body").append($form);
            $form.submit();
            $form.remove();
          });
      }
      else {
        throw new Error("can't export with no bibcodes or query");
      }

      //finally, close export widget and return to results page

      if (options.libid) {
        // We are in an ADS library: the contents of the library need to stay visible and the highlight
        // has to go back to the "View Library" menu item
        this.getPubSub().publish(this.getPubSub().NAVIGATE, "IndividualLibraryWidget", {subView: "library", id : options.libid});
      } else {
        this.getPubSub().publish(this.getPubSub().NAVIGATE, "results-page");
      }
    }
  });

  return Widget;
});
