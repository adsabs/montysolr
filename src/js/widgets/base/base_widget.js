define(['backbone', 'marionette',
  'js/components/api_query', 'js/components/api_request',
  'js/mixins/widget_mixin_method'
], function (Backbone, Marionette, ApiQuery, ApiRequest, WidgetMixin) {

  /**
   * Default PubSub based widget; the main functionality is inside
   *
   *  dispatchRequest()
   *    - publishes ApiRequest object into PubSub (to initiate search)
   *
   *  processResponse()
   *    - receives ApiResponse object as a direct reply for the previous
   *      request
   *
   * You will want to override 'processResponse' method and possibly
   * some of the other methods like this;
   *
   * var newWidgetClass = BaseWidget.extend({
   *   composeRequest : function(){},
   *   processRequest : function(){}
   * });
   *
   * var newInstance = new newWidgetClass();
   *
   *
   * If you need to provide your own views, do initalization etc., override
   * initialize
   *
   * * var newWidgetClass = BaseWidget.extend({
   *   initialize: function() {
   *      // do something
   *      BaseWidget.prototype.apply(this, arguments);
   *   }
   * });
   *
   *
   * Some other options include:
   *
   *  defaultQueryArguments: this is a list of parameters added to each query
   *
   */


  var BaseWidget = Marionette.Controller.extend({

    initialize: function (options) {

      options = options || {};

      // these methods are called by PubSub as handlers so we bind them to 'this' object
      // and they will carry their own context 'this'
      _.bindAll(this, "dispatchRequest", "processResponse");

      this._currentQuery = new ApiQuery();
      this.defaultQueryArguments = this.defaultQueryArguments || options.defaultQueryArguments || {};

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
    activate: function (beehive) {
      this.pubsub = beehive.Services.get('PubSub');

      //custom dispatchRequest function goes here
      this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.dispatchRequest);

      //custom handleResponse function goes here
      this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
    },

    /**
     * Default callback to be called by PubSub on 'INVITING_REQUEST'
     */
    dispatchRequest: function (apiQuery) {

      var q = this.customizeQuery(apiQuery);
      if (q) {
        var req = this.composeRequest(q);
        if (req) {
          this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
        }
      }
    },


    /**
     * Default action to modify ApiQuery (called from inside dispatchRequest)
     *
     * @param apiQuery
     */
    customizeQuery: function (apiQuery) {
      var q = apiQuery.clone();
      q.unlock();
      if (this.defaultQueryArguments) {
        q = this.composeQuery(this.defaultQueryArguments, q);
      }
      return q;
    },

    /**
     * Default callback to be called by PubSub on 'DELIVERING_RESPONSE'
     * @param apiResponse
     */
    processResponse: function (apiResponse) {

      // in your widget, you should always set the current query like this
      var q = apiResponse.getApiQuery();
      this.setCurrentQuery(q);

      throw new Error("you need to customize this function");
    },

    /**
     * This function should return a request IFF we want to get some
     * data - it is called from inside 'dispatchRequest' event handler
     *
     * @param object with params to add
     * @returns {ApiRequest}
     */
    composeRequest: function (apiQuery) {
      return new ApiRequest({
        target: 'search',
        query: apiQuery
      });
    },

    /**
     * Will save a clone of the apiQuery into the widget (for future use and
     * reference)
     *
     * @param apiQuery
     */
    setCurrentQuery: function (apiQuery) {
      this._currentQuery = apiQuery;
    },

    /**
     * Returns the current ApiQuery
     *
     * @returns {ApiQuery|*}
     */
    getCurrentQuery: function () {
      return this._currentQuery;
    },

    /**
     * All purpose function for making a new query. It returns an apiQuery ready for
     * newQuery event or for insertion into  apiRequest.
     *
     * @param queryParams
     * @param apiQuery
     * @returns {*}
     */
    composeQuery: function (queryParams, apiQuery) {
      var query;
      if (!apiQuery) {
        query = this.getCurrentQuery();
        query = query.clone();
      }
      else {
        query = apiQuery;
      }

      if (queryParams) {
        _.each(queryParams, function (v, k) {
          query.set(k, v)
        });
      }
      ;
      return query;
    },

    onClose: function () {
      this.view.close();
    },

    getView: function () {
      if (!this.view) {
        throw new Error("This widget doesn't have a view");
      } else {
        return this.view
      }
    },

    render : function(){
      this.view.render();
      return this.view.el;
    }

  }, {mixin: WidgetMixin});

  return BaseWidget

});