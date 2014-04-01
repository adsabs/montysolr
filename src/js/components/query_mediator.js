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

      pubsub.subscribe(this.pubSubKey, pubsub.NEW_QUERY, _.bind(this.start_searching, this));
      pubsub.subscribe(this.pubSubKey, pubsub.UPDATED_QUERY, _.bind(this.receive_modified_queries, this));
      pubsub.subscribe(this.pubSubKey, pubsub.NEW_REQUEST, _.bind(this.get_requests, this));
    },

    /**
     * Responds to the events arriving from PubSub
     */
    start_searching: function(apiQuery) {
      console.log('QM: received query:', apiQuery.url());
      if (apiQuery.keys().length <= 0) {
        console.warn('[QueryMediator] : received empty query (huh?!)');
        return;
      }
      var ps = this.getBeeHive().Services.get('PubSub');
      ps.publish(this.pubSubKey, ps.WANTING_QUERY, apiQuery.clone());
    },
    /**
     * Receives queries from the OC's - these are the guys that should
     * be searched. We can veto them here; or simply go ahead and
     * ask for requests
     *
     * @param apiQuery
     */
    receive_modified_queries: function(apiQuery, senderKey) {
      console.log('QM: received modified query:', apiQuery.url(), senderKey.getId());
      if (apiQuery.keys().length <= 0) {
        console.warn('[QueryMediator] : received empty query (huh?!)');
        return;
      }
      var ps = this.getBeeHive().Services.get('PubSub');
      ps.publish(this.pubSubKey, ps.WANTING_REQUEST+senderKey.getId(), apiQuery.clone());
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
      this.pubsub.publish(this.key, this.pubsub.NEW_RESPONSE+this.key.getId(), response);
    }

  });

  _.extend(QueryMediator.prototype, Mixins.BeeHive);
  return QueryMediator;
});