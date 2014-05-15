/**
 * Created by alex on 4/23/14.
 */
define(['backbone', 'marionette', 'js/components/api_query',
    'js/components/api_request', 'js/widgets/base/base_widget'
  ],
  function(
    Backbone, Marionette, ApiQuery, ApiRequest, BaseWidget) {

    /**
     * This widget is for situations when you want to register
     * a special function that handles the input (api-response)
     *
     * You call it in two steps:
     *
     *  1. registerCallback(apiQuery.url(), function() {....})
     *  2. dispatchRequest(apiQuery)
     */
    var MultiCallbackWidget = BaseWidget.extend({

      /*Takes any additions to the query, a callback, and
       data for that callback. It then creates the apiQuery,
       registers the callback, and returns the apiRequest so
       that you can send it to pubsub in response to INVITING_REQUEST*/

      initialize: function(options) {

        _.bindAll(this, "dispatchRequest", "callResponseCallback")

        this._queriesInProgress = {};
        BaseWidget.prototype.initialize.call(this, options);
      },

      /*function should not be overridden, can "subscribeCustomHandlers" in options to easily override*/
      activate: function(beehive) {
      this.pubsub = beehive.Services.get('PubSub');

      if (this.subscribeCustomHandlers) {
        this.subscribeCustomHandlers(this.pubsub)
      } else {
        //custom dispatchRequest function goes here
        this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.dispatchRequest);

        //custom handleResponse function goes here
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.callResponseCallback);
      }

    },

      /** utility function **/
      /**
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
        this._queriesInProgress[queryId] = {
          //calling the callback in the widget context
          callback: _.bind(callback, this),
          data: data
        };
      },

      /*a function to override or leave as is*/
      //default response to "INVITING_REQUEST"
      dispatchRequest: function(apiQuery) {
        var id, req;

        this.setCurrentQuery(apiQuery);

        id = apiQuery.url();

        //it's responding to INVITING_REQUEST, so just do default information request
        this.registerCallback(id, this.processResponse)

        req = this.composeRequest(apiQuery);
        if (req) {
          this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
        }

      },

      /*very simple for multi-callback, just returns an apirequest when you give
      it an apiquery
      */

      composeRequest : function(apiQuery){
        return new ApiRequest({
          target: 'search',
          query: apiQuery
        })
      },


      /*utility function*/
      //this can be called anywhere in your code to register your own callback

      dispatchSupplementalRequest: function(params, callback, data) {
        var newQuery, id, ApiRequest;

        newQuery = this.composeQuery(params);
        id = newQuery.url();

        this.registerCallback(id, callback, data);

        var req = this.composeRequest(newQuery);

        this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);

      },

      /*utility function*/
      /*
       Companion function to composeRequest. It will call the
       callback with the just-received data. This function
       is the only one the widget will need
       to register to DELIVERING_RESPONSE
       */

     callResponseCallback: function(apiResponse) {
        var id = apiResponse.getApiQuery().url();
        var parameters, callback;

        //find the callback based on the key of the query
        if (this._queriesInProgress[id]) {
          callback = this._queriesInProgress[id].callback;
        } else {
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
