/**
 * Created by alex on 4/23/14.
 */
define(['backbone', 'marionette', 'js/components/api_query',
    'js/components/api_request', 'js/widgets/base/base_widget'],
  function(
    Backbone, Marionette, ApiQuery, ApiRequest, BaseWidget) {

    var MultiCallbackWidget = BaseWidget.extend({

      /*Takes any additions to the query, a callback, and
       data for that callback. It then creates the apiQuery,
       registers the callback, and returns the apiRequest so
       that you can send it to pubsub in response to INVITING_REQUEST*/

      initialize : function(options){
        this._queriesInProgress = {};
        BaseWidget.prototype.initialize.call(this,options);
      },

      /**\
       * Here you register callbacks that should receive response
       * and handle it
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
          throw new Error("There is already a callback for: " + queryId);
        }
        this._queriesInProgress[queryId] = {callback: callback, data: data};
      },

      composeRequest: function(apiQuery) {
        var apiRequest, queryId, callback;

        queryId = apiQuery.url();
        callback = this._queriesInProgress[queryId];

        if (!callback) {
          console.warn("We have no callback, ignoring query: " + apiQuery);
          return;
        }

        return new ApiRequest({
          target: 'search',
          query: apiQuery
        });
      },

      /*
       Companion function to composeRequest. It will call the
       callback with the just-received data. This function
       is probably the only one the widget will need
       to register to DELIVERING_RESPONSE
       */
      processResponse: function(apiResponse) {
        var id = apiResponse.getApiQuery().url();
        var parameters, callback;

        //find the callback based on the key of the query
        if (this._queriesInProgress[id]) {
          callback = this._queriesInProgress[id].callback;
        }
        else {
          console.warn("Widget received a response for which it has no callback: " + id);
          return;
        }

        if (this._queriesInProgress[id] && this._queriesInProgress[id]["data"]) {
          parameters = this._queriesInProgress[id]["data"];
        }

        //remove the callback from this.queriesInProgress
        delete this._queriesInProgress[id];

        callback(apiResponse, parameters);
      }

    });

    return MultiCallbackWidget

  })
