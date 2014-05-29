/**
 * Created by rchyla on 3/30/14.
 */

/**
 * Mediator to coordinate UI-query exchange
 */

define(['underscore',
    'jquery',
    'cache',
    'js/components/generic_module',
    'js/mixins/dependon',
    'js/components/api_response',
    'js/components/api_query_updater'],
  function(
    _,
    $,
    Cache,
    GenericModule,
    Mixins,
    ApiResponse,
    ApiQueryUpdater) {


  var QueryMediator = GenericModule.extend({
    debug: false,

    initialize: function(attrs, options) {
      if (options.cache) {
        this._cache = new Cache(_.extend({
          'maximumSize': 50,
          'expiresAfterWrite':600
        }, options.cache));
      }
      this.queryUpdater = new ApiQueryUpdater('QueryMediator');
    },

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

      pubsub.subscribe(this.mediatorPubSubKey, pubsub.NEW_QUERY, _.bind(this.startSearchCycle, this));
      pubsub.subscribe(this.mediatorPubSubKey, pubsub.DELIVERING_REQUEST, _.bind(this.receiveRequests, this));
    },

    /**
     * Happens at the beginnng of the new search cycle. This is the 'race started' signal
     */
    startSearchCycle: function(apiQuery) {
      if (this.debug)
        console.log('[QM]: received query:', apiQuery.url());

      if (apiQuery.keys().length <= 0) {
        console.warn('[QM] : received empty query (huh?!)');
        return;
      }
      var ps = this.getBeeHive().Services.get('PubSub');
      // we will protect the query -- in the future i can consider removing 'unlock' to really
      // cement the fact the query MUST NOT be changed (we want to receive a modified version)
      var q = apiQuery.clone();
      q.lock();
      ps.publish(this.mediatorPubSubKey, ps.INVITING_REQUEST, q);
    },

    receiveRequests: function(apiRequest, senderKey) {
      if (this.debug)
        console.log('[QM]: received request:', apiRequest.url(), senderKey.getId());

      var ps = this.getBeeHive().Services.get('PubSub');
      var api = this.getBeeHive().Services.get('Api');

      if (this._cache) {
        var requestKey = this.getCacheKey(apiRequest);
        var resp = this._cache.getSync(requestKey);
        var self = this;
        if (resp && resp.done) { // it is a promise object
          resp.done(function() {
            self.onApiResponse.apply({request:apiRequest, pubsub: ps, key: senderKey}, arguments);
          });
          resp.done(function(arguments) {
            self.onApiRequestFailure.apply({request:apiRequest, pubsub: ps, key: senderKey}, arguments);
          });
        }
        else if (resp) { // it is a data
          self.onApiResponse.apply({request:apiRequest, pubsub: ps, key: senderKey}, resp);
        }
        else { // new query
          var promise = api.request(apiRequest, {
            done: this.onApiResponse,
            fail: this.onApiRequestFailure,
            context: {request:apiRequest, pubsub: ps, key: senderKey}
          });
          promise.done(function() {
            self._cache.put(requestKey, arguments);
          });
        }
      }
      else {
        api.request(apiRequest, {
          done: this.onApiResponse,
          fail: this.onApiRequestFailure,
          context: {request:apiRequest, pubsub: ps, key: senderKey}
        });
      }

    },


    onApiResponse: function(data, textStatus, jqXHR ) {
      if (this.debug)
        console.log('[QM]: received response:', data);

      // TODO: check the status responses
      var response = new ApiResponse(data);
      response.setApiQuery(this.request.get('query'));

      if (this.debug)
        console.log('[QM]: sending response:', data);

      this.pubsub.publish(this.key, this.pubsub.DELIVERING_RESPONSE+this.key.getId(), response);
    },

    onApiRequestFailure: function( jqXHR, textStatus, errorThrown ) {
      var query = this.request.get('query');
      if (query) {
        console.warn(jqXHR, textStatus, errorThrown);
      }
    },

    /**
     * Creates a unique, cleaned key from the request and the apiQuery
     * @param apiRequest
     */
    getCacheKey: function(apiRequest) {
      var oldQ = apiRequest.get('query');
      var newQ = this.queryUpdater.clean(oldQ);
      apiRequest.set('query', newQ);
      var key = apiRequest.url();
      apiRequest.set('query', oldQ);
      return key;
    }

  });

  _.extend(QueryMediator.prototype, Mixins.BeeHive);
  return QueryMediator;
});