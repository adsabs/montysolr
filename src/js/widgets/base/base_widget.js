define(['backbone', 'marionette',
  'js/components/api_query', 'js/components/api_request'
], function(
  Backbone, Marionette, ApiQuery, ApiRequest) {

  //a pub-sub enabled widget that will need to be overridden

  var BaseWidget = Backbone.Marionette.Controller.extend({

    initialize: function() {
      _.bindAll(this, "updateCurrentQuery", "dispatchRequest", "processResponse")
      this._currentQuery = new ApiQuery();
    },

    getCurrentQuery: function() {
      return this._currentQuery.clone()
    },

    //will need to override this method to add custom event listeners
    activate: function(beehive) {
      this.pubsub = beehive.Services.get('PubSub');
      this.pubSubKey = this.pubsub.getPubSubKey();

      //always need to keep currentQuery updated, so don't change this
      this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.updateCurrentQuery);

      if (this.subscribeCustomHandlers) {
        this.subscribeCustomHandlers()
      } else {
        //custom dispatchRequest function goes here
        this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.dispatchRequest);

        //custom handleResponse function goes here
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
      }

    },

    dispatchRequest: function() {
      var apiRequest;
      apiRequest = new ApiRequest({
        query: this.customizeQuery()
      });
      this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, apiRequest)
    },

    processResponse: function(apiResponse) {
      throw "you need to customize this widget "
      console.log(apiResponse)

    },

    updateCurrentQuery: function(ApiQuery) {
      this._currentQuery = ApiQuery;
    },

    /*all purpose function for making a new query
  returns an apiQuery ready for newQuery event or 
  for insertion into  apiRequest */
    customizeQuery: function(queryParams) {
      var query = this.getCurrentQuery();

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
        throw "This widget doesn't have a view"
      } else {
        return this.view
      }
    }

  });

  return BaseWidget

})
