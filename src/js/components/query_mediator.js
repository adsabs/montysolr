/**
 * Created by rchyla on 3/30/14.
 */

/**
 * Mediator to coordinate UI-query exchange
 */

define(['underscore', 'jquery', 'js/components/generic_module', 'js/mixins/dependon'],
  function(_, $, GenericModule, Mixins) {

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

      pubsub.subscribe(this.pubSubKey, pubsub.NEW_QUERY, this._new_query);
      pubsub.subscribe(this.pubSubKey, pubsub.NEW_REQUEST, this._new_request);
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

      api.request(apiRequest, {done: this._new_response});
      //ps.publish(this.pubSubKey, ps.WANTING_RESPONSE, apiRequest);
    },
    _new_response: function(apiResponse) {
      var ps = this.getBeeHive().Services.get('PubSub');
      ps.publish(this.pubSubKey, ps.SENDING_RESPONSE, apiResponse);
    }

  });

  _.extend(QueryMediator.prototype, Mixins.BeeHive);
  return QueryMediator;
});