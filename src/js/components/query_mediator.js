/**
 * Created by rchyla on 3/30/14.
 */

/**
 * Mediator to coordinate UI-query exchange
 */

define(['underscore', 'jquery', 'js/components/generic_module', 'js/mixins/dependon', 'js/components/api_response'],
  function(_, $, GenericModule, Mixins, ApiResponse) {

  var QueryMediator = GenericModule.extend({
    /**
     * Starts listening on the PubSub
     *
     * @param beehive - the full access instance; we excpect PubSub to be
     *    present
     */
    activate: function(beehive) {
      this.setBeeHive(beehive);
      var pubsub = beehive.Services.get('PubSub');
      this.pubSubKey = pubsub.getPubSubKey();

      pubsub.subscribe(this.pubSubKey, pubsub.NEW_QUERY, _.bind(this._new_query, this));
      pubsub.subscribe(this.pubSubKey, pubsub.NEW_REQUEST, _.bind(this._new_request, this));
    },

    /**
     * Responds to the events arriving from PubSub
     */
    _new_query: function(apiQuery) {
      if (apiQuery.keys().length <= 0) {
        console.warn('[QueryMediator] : received empty query (huh?!)');
        return;
      }
      var ps = this.getBeeHive().Services.get('PubSub');
      ps.publish(this.pubSubKey, ps.WANTING_REQUEST, apiQuery.clone());
    },
    _new_request: function(apiRequest) {
      var ps = this.getBeeHive().Services.get('PubSub');
      var api = this.getBeeHive().Services.get('Api');

      api.request(apiRequest,
        {done: this._new_response, context: {request:apiRequest, pubsub: ps, key: this.pubSubKey}});
    },
    _new_response: function(data, textStatus, jqXHR ) {
      // TODO: check the status responses
      var response = new ApiResponse(data);
      response.setApiQuery(this.request.get('query'));
      this.pubsub.publish(this.key, this.pubsub.NEW_RESPONSE, response);
    }

  });

  _.extend(QueryMediator.prototype, Mixins.BeeHive);
  return QueryMediator;
});