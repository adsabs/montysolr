'use strict';
define([
  'underscore',
  'backbone',
  'react',
  'react-redux',
  'react-dom',
  'redux',
  'es6!./redux/configure-store',
  'es6!./redux/modules/orcid-selector-app',
  'js/components/api_query',
  'js/widgets/base/base_widget',
  'es6!./containers/orcid-selector-container'
], function (
  _, Backbone, React, ReactRedux, ReactDOM, Redux, configureStore,
  OrcidSelectorApp, ApiQuery, BaseWidget, OrcidSelectorContainer
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
          <OrcidSelectorContainer/>
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

      // grab the current mode while activating, in case we should render
      const mode = beehive.hasObject('User') && beehive.getObject('User').isOrcidModeOn();
      this.store.dispatch(OrcidSelectorApp.updateMode(mode));

      pubsub.subscribe(pubsub.STORAGE_PAPER_UPDATE,
        _.bind(this.onStoragePaperChange, this));
      pubsub.subscribe(pubsub.USER_ANNOUNCEMENT,
        _.bind(this.onUserAnnouncement, this));
    },

    onStoragePaperChange: function () {
      const beehive = this.getBeeHive();
      let selected = beehive.getObject('AppStorage').getSelectedPapers();
      this.store.dispatch(OrcidSelectorApp.updateSelected(selected));
    },

    onUserAnnouncement: function (msg, data) {

      // watch for orcid mode change
      if (_.has(data, 'isOrcidModeOn')) {
        this.store.dispatch(OrcidSelectorApp.updateMode(data.isOrcidModeOn));
      }
    },

    fireOrcidEvent: function (event, bibcodes) {
      const pubsub = this.getPubSub();
      pubsub.publish(pubsub.CUSTOM_EVENT, event, bibcodes);
    }
  });

  return Widget;
});
