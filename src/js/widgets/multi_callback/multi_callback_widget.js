/**
 * Created by alex on 4/23/14.
 */
define(['backbone', 'marionette', 'js/components/api_query', 
    'js/components/api_request', '../base/base_widget',
], function(
    Backbone, Marionette, ApiQuery, ApiRequest, BaseWidget) {

    var MultiCallbackWidget = BaseWidget.extend({

        /*Takes any additions to the query, a callback, and
         data for that callback. It then creates the apiQuery,
         registers the callback, and returns the apiRequest so
         that you can send it to pubsub in response to INVITING_REQUEST*/

        initialize : function(options){
            this._queriesInProgress = {};

            BaseWidget.prototype.initialize.call(this,options)
        },

        dispatchRequest : function(queryParams,callback,data){
            var apiRequest;
            apiRequest = this.composeRequest(queryParams, callback, data)
            this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, apiRequest)
        },

        composeRequest: function(queryParams, callback, data) {
            var query, apiRequest, id;

            if (!callback) {
                throw "No callback was passed"
            }

            query = this.customizeQuery(queryParams);

            apiRequest = new ApiRequest({
                query: query
            });

            id = apiRequest.get("query").url();

            this._queriesInProgress[id] = {
                "callback": callback,
                data: data
            };

            return apiRequest
        },

        /* Companion function to dispatchRequest. It will call the
         callback with the just-received data. This function
         is probably the only one the widget will need
         to register to DELIVERING_RESPONSE*/
        processResponse: function(apiResponse) {
            var id = apiResponse.getApiQuery().url();
            var parameters;

            //callback
            if (this._queriesInProgress[id]["data"]) {
                parameters = this._queriesInProgress[id]["data"];
            }

            callback = this._queriesInProgress[id].callback;

            //pop callback from this.queriesInProgress
            delete this._queriesInProgress[id];

            callback(apiResponse, parameters);
        }

    });

    return MultiCallbackWidget

})
