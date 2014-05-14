define(['backbone', 'marionette',
  'js/components/api_query', 'js/components/api_request',
  'js/mixins/widget_mixin_method'
], function(
  Backbone, Marionette, ApiQuery, ApiRequest, WidgetMixin) {

  /**
   * A pubsub based widget with basic functionality. For
   *  a basic widget, you would only need to customize the composeRequest
   *  and processResponse functions for pubsub functionality. If you pass in a "defaultFields"
   *  object to the constructor, e.g.:
   *  defaultFields = {fl:"title,author", sort:"ascending"}
   *
   *  you don't even need to customize composeRequest for basic functionality.
   *  You would also probably want to write your own initialize method that creates
   *  instances of any views, collections or models your widget will need, e.g.:
   *
   * var newWidgetClass = BaseWidget.extend({
   *   initialize : function(options){
   *      BaseWidget.prototype.initialize.call(this, options)
   *    },
   *   composeRequest : function(){},
   *   processRequest : function(){}
   * });
   *
   * newWidgetInstance = newWidgetClass();
   */


  var BaseWidget = Marionette.Controller.extend({

    initialize: function(options) {

      options = options ||  {};

      // these methods are called by PubSub as handlers so we bind them to 'this' object
      // to avoid any confusion
      _.bindAll(this, "dispatchRequest", "processResponse")

      this._currentQuery = new ApiQuery();
      if (options.defaultFields){
        this.defaultFields = options.defaultFields
      }

      if (options.subscribeCustomHandlers){
        this.subscribeCustomHandlers = options.subscribeCustomHandlers
      };

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

      //XXX:rca: IFF st changed query before calling 'dispatch' then this
      // would be OK; but if st is changing query inside 'composeRequest'
      // then this usage is not OK. Because it is setting query and then
      // refusing to act on it (it is possible)

      this.setCurrentQuery(apiQuery);
      var req = this.composeRequest();
      if (req) {
        this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
      }
    },

    /**
     * Default callback to be called by PubSub on 'DELIVERING_RESPONSE'
     * @param apiResponse
     */
    processResponse: function(apiResponse) {
      //need to put some parsing logic here
      // reset collection: 
      //this.collection.reset(apiResponse)
      throw new Error("you need to customize this function");
    },

    /**
     * This function should return a request IFF we want to get some
     * data - it is called from 'dispatchRequest' event handler
     *
     * @param object with params to add
     * @returns {ApiRequest}
     */
    composeRequest: function(params) {
      /*
      XXX:rca: i have objections to this (api inconsistency) - the previous signature was
       apiQuery; which is a clear what it contains, but params could
       be anything. I'd like to see composeRequest to receive ApiQuery
       and do whatever is necessary - the query can be changed before
       composeRequest is called. What was the reason for reverting
       this functionality?

       */
      if (params){
          var q = this.composeQuery(params);
      }
      else if (this.defaultFields){
         var q = this.composeQuery(this.defaultFields);
      }
      else {
         var q = this.composeQuery();
      };

      if (q) {
        return new ApiRequest({
          target: 'search',
          query: q
        });
      }
    },

    /**
     *
     * @param apiQuery
     */
    setCurrentQuery: function(apiQuery) {
      var q = apiQuery.clone();
      q.unlock();
      this._currentQuery = q;
    },

    getCurrentQuery: function() {
      return this._currentQuery.clone(); // XXX:rca: this closing seems excessive
    },

    /**
     * All purpose function for making a new query. It returns an apiQuery ready for
     * newQuery event or  for insertion into  apiRequest.
     * */
    composeQuery: function(queryParams) {
      var query;

      query = this.getCurrentQuery();
        if (queryParams) {
          _.each(queryParams, function(v, k) {
            query.set(k, v)
          });
        };
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

  }, {mixin : WidgetMixin});

  return BaseWidget

});