/**
 * Created by alex on 4/23/14.
 */
define(['backbone', 'marionette', 'js/components/api_query',
    'js/components/api_request', 'js/widgets/base/base_widget'],
  function(
    Backbone, Marionette, ApiQuery, ApiRequest, BaseWidget) {

    /**
     * This widget is for situations when you want to register
     * a special function that handles the input (api-response)
     *
     * You call processing in two steps:
     *
     *  1. registerCallback(apiQuery.url(), function() {....})
     *  2. dispatchRequest(apiQuery)
     *
     *
     * Or, alternatively, in one step:
     *
     *   dispatch(apiQuery, function(){....});
     */
    var MultiCallbackWidget = BaseWidget.extend({

      initialize : function(options){
        this._queriesInProgress = {};
        BaseWidget.prototype.initialize.call(this,options);
      },

      /**
       * Utility method that 1. registers callback and 2. sends data
       * to Pubsub.
       *
       * @param apiQuery
       * @param callback
       * @param data
       */
      dispatch: function(apiQuery, callback, data) {
        var queryId = apiQuery.url();
        if (this.registerCallback(queryId, callback, data)) {
          this.dispatchRequest(apiQuery);
        }
      },

      /**
       * Here you register callbacks that should receive response
       * and handle it.
       *
       * It returns true if the callback was successfully registered
       * false otherwise
       *
       * @param queryId
       * @param callback
       * @param data
       */
      registerCallback: function(queryId, callback, data) {
        if (!_.isFunction(callback)) {
          throw new Error("Callback must be a function");
        }
        if (this._queriesInProgress[queryId]) {
          console.warn("There already exists a callback for: " + queryId);
          return false;
        }
        this._queriesInProgress[queryId] = {callback: callback, data: data};
        return true;
      },


      /**
       * Checks the register of callbqacks and returns the one that was registered
       * for this query
       *
       * @param queryId
       * @returns {*}
       */
      getCallback: function(queryId) {
        if (this._queriesInProgress[queryId]) {
          return this._queriesInProgress[queryId];
        }
        if (this.defaultCallback) {
          return {callback: this.defaultCallback, data: undefined};
        }
      },

      removeCallback: function(queryId) {
        delete this._queriesInProgress[queryId];
      },

      /**
       * Listens to INVITING_REQUEST signals from PubSub
       * (but unless there is a callback registered under ApiQuery.url()
       * the signal will be ignored)
       *
       * @see dispatch()
       * @param apiQuery
       * @returns {ApiRequest}
       */
      composeRequest: function(apiQuery) {
        var apiRequest, queryId, callback;

        queryId = apiQuery.url();
        callback = this.getCallback(queryId);

        if (!callback) {
          console.warn("We have no callback, ignoring query: ", apiQuery);
          return;
        }

        return new ApiRequest({
          target: 'search',
          query: apiQuery
        });
      },

      /**
       * Listens to DELIVERING_RESPONSE signal from PubSub
       * it gets the response and routes it to the appropriate
       * callback (which was registered before)
       *
       * WARNING: the widget is responsible for calling
       * setCurrentQuery() inside his callback
       *
       * @see dispatch()
       * @param apiResponse
       */
      processResponse: function(apiResponse) {
        var q = apiResponse.getApiQuery();
        var id = q.url();
        var parameters, callback;

        //find the callback based on the key of the query
        var call = this.getCallback(id);
        if (call) {
          callback = call.callback;
        }
        else {
          console.warn("Widget received a response for which it has no callback: " + id);
          return;
        }


        callback.call(this, apiResponse, call.data);

        //remove the callback from this.queriesInProgress
        this.removeCallback(id);

      }

    });

    return MultiCallbackWidget;

  });
