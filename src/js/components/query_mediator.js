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
      this.mediatorPubSubKey = pubsub.getPubSubKey();

      pubsub.subscribe(this.mediatorPubSubKey, pubsub.NEW_QUERY, _.bind(this.start_searching, this));
      pubsub.subscribe(this.mediatorPubSubKey, pubsub.DELIVERING_REQUEST, _.bind(this.get_requests, this));
    },

    /**
     * Happens at the beginnng of the new search cycle. This is the 'race started' signal
     */
    start_searching: function(apiQuery) {
      console.log('QM: received query:', apiQuery.url());
      if (apiQuery.keys().length <= 0) {
        console.warn('[QueryMediator] : received empty query (huh?!)');
        return;
      }
      var ps = this.getBeeHive().Services.get('PubSub');
      // we will protect the query -- in the future i can consider removing 'unlock' to really
      // cement the fact the query MUST NOT be changed (we want to receive a modified version)
      var q = apiQuery.clone();
      q.lock();
      ps.publish(this.mediatorPubSubKey, ps.INVITING_REQUEST, q);
    },
    get_requests: function(apiRequest, senderKey) {
      console.log('QM: received request:', apiRequest.url(), senderKey.getId());
      var ps = this.getBeeHive().Services.get('PubSub');
      var api = this.getBeeHive().Services.get('Api');

      api.request(apiRequest,
        {done: this.deliver_response, context: {request:apiRequest, pubsub: ps, key: senderKey}});
    },
    deliver_response: function(data, textStatus, jqXHR ) {
      console.log('QM: received response:', data);
      // TODO: check the status responses
      var response = new ApiResponse(data);
      response.setApiQuery(this.request.get('query'));
      console.log('QM: sending response:', data);
      this.pubsub.publish(this.key, this.pubsub.DELIVERING_RESPONSE+this.key.getId(), response);
    }

  });

  _.extend(QueryMediator.prototype, Mixins.BeeHive);
  return QueryMediator;
});