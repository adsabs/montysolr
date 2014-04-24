define(['backbone', 'marionette',
  'js/components/api_query', 'js/components/api_request'
], function(
  Backbone, Marionette, ApiQuery, ApiRequest) {

  /**
   * A pubsub based widget that contains the basic functionality
   * you may want to override certain methods and pass your
   * own custom template:
   *
   * var newWidgetClass = BaseWidget.extend({
   *   activate: function() { .....}
   * });
   *
   * newWidgetInstance = newWidgetClass();
   */

  var BaseWidget = Marionette.Controller.extend({

    initialize: function(options) {
      // these methods are called by PubSub as handlers so we bind them to 'this' object
      // to avoid any confusion
      _.bindAll(this, "dispatchRequest", "processResponse")

      this._currentQuery = new ApiQuery();

      // XXX: here the widget should do something with the views/models/templates
      // to set everything up
    },


    /**
     * Called by Bumblebee application when a widget is about to be registered
     * it receives a BeeHive object, that contais methods/attributes that a
     * widget needs to communicate with the app
     *
     * This is the place where you want to subscribe to events
     *
     * @param beehive
     */
    activate: function(beehive) {
      this.pubsub = beehive.Services.get('PubSub');

      if (this.subscribeCustomHandlers) {
        this.subscribeCustomHandlers(this.pubsub)
      } else {
        //custom dispatchRequest function goes here
        this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.dispatchRequest);

        //custom handleResponse function goes here
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
      }

    },

    /**
     * Default callback to be called by PubSub on 'INVITING_REQUEST'
     */
    dispatchRequest: function(apiQuery) {
      var q = this.customizeQuery(apiQuery);
      if (q) {
        this.setCurrentQuery(q);
        var req = this.composeRequest(q);
        if (req) {
          this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
        }
      }
    },

    /**
     * Default callback to be called by PubSub on 'DELIVERING_RESPONSE'
     * @param apiResponse
     */
    processResponse: function(apiResponse) {
      throw new Error("you need to customize this function");
    },

    /**
     * This function should return a request IFF we want to get some
     * data - it is called from 'dispatchRequest' event handler
     *
     * @param apiQuery
     * @returns {ApiRequest}
     */
    composeRequest: function(apiQuery) {
      return new ApiRequest({
        target: 'search',
        query: apiQuery
      });
    },

    /**
     * Defaualt callback to be called by PubSub on 'INVITING_REQUEST'
     * XXX: seems like a problem to me (it should be called from inside
     * 'dispatchRequest' imo)
     *
     * @param apiQuery
     */
    setCurrentQuery: function(apiQuery) {
      var q = apiQuery.clone();
      q.unlock();
      this._currentQuery = q;
    },

    getCurrentQuery: function() {
      return this._currentQuery;
    },

    /**
     * all purpose function for making a new query
     * returns an apiQuery ready for newQuery event or
     * for insertion into  apiRequest
     * */
    customizeQuery: function(queryParams) {
      var query;
      if (queryParams instanceof  ApiQuery) {
        // do something here
        query = queryParams;
      }
      else {
        query = this.getCurrentQuery();
        if (queryParams) {
          _.each(queryParams, function(v, k) {
            query.set(k, v)
          });
        };
      }
      return query
    },

    onClose: function() {
      this.view.close();
    },

    getView: function() {
      if (!this.view) {
        throw new Error("This widget doesn't have a view");
      } else {
        return this.view
      }
    }

  });

  return BaseWidget

});