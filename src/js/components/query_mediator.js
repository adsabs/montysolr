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
    'js/components/api_request',
    'js/components/api_response',
    'js/components/api_query_updater',
    'js/components/api_feedback',
    'js/components/json_response'],
  function(
    _,
    $,
    Cache,
    GenericModule,
    Mixins,
    ApiRequest,
    ApiResponse,
    ApiQueryUpdater,
    ApiFeedback,
    JsonResponse) {


  var QueryMediator = GenericModule.extend({

    initialize: function(options) {

      if (options.cache) {
        this._cache = this._getNewCache(options.cache);
      }
      this.debug = options.debug || false;
      this.queryUpdater = new ApiQueryUpdater('QueryMediator');
      this.failedRequestsCache = this._getNewCache();
      this.maxRetries = options.maxRetries || 3;
      this.recoveryDelayInMs = options.recoveryDelayInMs || 700;
    },

    activateCache: function() {
      if (!this._cache)
        this._cache = this._getNewCache();
    },

    _getNewCache: function(options) {
      return new Cache(_.extend({
        'maximumSize': 100,
        'expiresAfterWrite':60*30 // 30 mins
      }, _.isObject(options) ? options : {}));
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
      this.pubSubKey = pubsub.getPubSubKey();

      pubsub.subscribe(this.pubSubKey, pubsub.START_SEARCH, _.bind(this.startSearchCycle, this));
      pubsub.subscribe(this.pubSubKey, pubsub.DELIVERING_REQUEST, _.bind(this.receiveRequests, this));
      pubsub.subscribe(this.pubSubKey, pubsub.GET_QTREE, _.bind(this.getQTree, this));
    },


    getQTree: function(apiQuery, senderKey) {
      var apiRequest = new ApiRequest({'query': apiQuery, 'target': 'qtree'});
      var api = this.getBeeHive().Services.get('Api');
      api.request(apiRequest, {
        done: this.onApiResponse,
        fail: this.onApiRequestFailure,
        context: {request:apiRequest, key: senderKey, requestKey:apiRequest.url(), qm: this }
      });
    },

    /**
     * Happens at the beginnng of the new search cycle. This is the 'race started' signal
     */
    startSearchCycle: function(apiQuery, senderKey) {
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

      // since widgets are dirty bastards, we will remove the fl parameter (to avoid cross-
      // contamination)
      q.unset('fl');

      q.lock();
      ps.publish(this.pubSubKey, ps.INVITING_REQUEST, q);
    },


      /**
     * This method harvest requests from the PubSub and passes them to the Api. We do check
     * the local cache and also prepare context for the done/fail callbacks
     *
     * @param apiRequest
     * @param senderKey
     */
    receiveRequests: function(apiRequest, senderKey) {
      if (this.debug)
        console.log('[QM]: received request:', apiRequest.url(), senderKey.getId());

      var ps = this.getBeeHive().Services.get('PubSub');
      var api = this.getBeeHive().Services.get('Api');

      var requestKey = this._getCacheKey(apiRequest);
      var maxTry = this.failedRequestsCache.getSync(requestKey) || 0;

      if (maxTry >= this.maxRetries) {
        this.onApiRequestFailure.apply({request:apiRequest, key: senderKey, requestKey:requestKey, qm: this},
          [{status: ApiFeedback.CODES.TOO_MANY_FAILURES}, 'Error', 'This request has reached maximum number of failures (wait before retrying)']);
        return;
      }


      if (this._cache) {

        var resp = this._cache.getSync(requestKey);
        var self = this;

        if (resp && resp.promise) { // it is a promise object
          //console.log('promise1', resp);
          resp.done(function() {
            //console.log('promise1:done', resp);
            self._cache.put(requestKey, arguments);
            self.onApiResponse.apply(
              {request:apiRequest, key: senderKey, requestKey:requestKey, qm: self }, arguments);
          });
          resp.fail(function() {
            //console.log('promise1:fail', resp);
            self._cache.invalidate(requestKey);
            self.onApiRequestFailure.apply(
              {request:apiRequest, pubsub: ps, key: senderKey, requestKey:requestKey,
                qm: self }, arguments);
          });
        }
        else if (resp) { // we already have data
          //console.log('cached2', resp);
          self.onApiResponse.apply(
            {request:apiRequest, key: senderKey, requestKey:requestKey, qm: self }, resp);
        }
        else { // new query
          //console.log('newQ3');
          var promise = api.request(apiRequest, {
            done: function() {
              self._cache.put(requestKey, arguments);
              //console.log('done3', 'set', requestKey, this._cache);
              self.onApiResponse.apply(this, arguments);
            },
            fail: function() {
              self._cache.invalidate(requestKey);
              //console.log('fail3', 'invalidate', requestKey, this._cache);
              self.onApiRequestFailure.apply(this, arguments);
            },
            context: {request:apiRequest, key: senderKey, requestKey:requestKey, qm: self }
          });
          this._cache.put(requestKey, promise);
        }
      }
      else {
        api.request(apiRequest, {
          done: this.onApiResponse,
          fail: this.onApiRequestFailure,
          context: {request:apiRequest, key: senderKey, requestKey:requestKey, qm: this }
        });
      }

    },


    onApiResponse: function(data, textStatus, jqXHR ) {
      var qm = this.qm;
      if (qm.debug)
        console.log('[QM]: received response:', JSON.stringify(data).substring(0, 1000));

      // TODO: check the status responses

     var response = (data.responseHeader && data.responseHeader.QTime) ? new ApiResponse(data) : new JsonResponse(data);

      response.setApiQuery(this.request.get('query'));

      if (qm.debug)
        console.log('[QM]: sending response:', this.key.getId());

      var pubsub = qm.getBeeHive().Services.get('PubSub');

      if (pubsub)
        pubsub.publish(this.key, pubsub.DELIVERING_RESPONSE+this.key.getId(), response);
        
      if (qm.failedRequestsCache.getIfPresent(this.requestKey)) {
        qm.failedRequestsCache.invalidate(this.requestKey);
      }

    },

    onApiRequestFailure: function( jqXHR, textStatus, errorThrown ) {
      var qm = this.qm;
      var query = this.request.get('query');
      if (qm.debug) {
        console.warn('[QM]: request failed', jqXHR, textStatus, errorThrown);
      }

      var errCount = qm.failedRequestsCache.getSync(this.requestKey) || 0;
      qm.failedRequestsCache.put(this.requestKey, errCount+1);

      if (qm.tryToRecover.apply(this, arguments)) {
        console.warn("[QM]: attempting recovery");
      }
      else {
        return;
      }

      var feedback = new ApiFeedback({code:503, msg:textStatus});
      try {
        feedback.setCode(jqXHR.status);
      }
      catch(e) {
        console.error(e.stack);
      }

      if (this.request) {
        feedback.setApiRequest(this.request);
      }

      var pubsub = qm.getBeeHive().Services.get('PubSub');
      if (pubsub)
        pubsub.publish(this.key, pubsub.FEEDBACK+this.key.getId(), feedback);

    },

    /**
     * Method that receives the same arguments as the error callback. It can try to
     * recover (re-issue) the request. Note: it doesn't need to check whether the
     * recovery is needed - if we are here, it means 'do what you can to recover'
     *
     * This method MUST return 'true' when the request was resent. If it doesn't
     * return 'true' the sender will be notified about the error.
     *
     * If it returns a Feedback object, the sender will be notified using it
     *
     * @param jqXHR
     * @param textStatus
     * @param errorThrown
     */
    tryToRecover: function(jqXHR, textStatus, errorThrown) {
      var qm = this.qm; // QueryMediator
      var senderKey = this.key;
      var request = this.request;
      var requestKey = this.requestKey;

      var status = jqXHR.status;
      if (status) {
        switch(status) {
          case 408: // proxy timeout
          case 504: // gateway timeout
          case 503: // service unavailable
            setTimeout(function() {
              // we can remove the entry from the cache, because
              // if they eventually succeed, sender will receive
              // its data (because the promise object inside the
              // cache contains the function to call delivery
              if (qm._cache) {
                var resp = qm._cache.getSync(requestKey);
                if (resp && resp.promise) {
                  qm._cache.invalidate(requestKey);
                }
                else if (resp) {
                  // it must have succeeded, good!
                  return;
                }
              }
              // re-send the query
              qm.receiveRequests.call(qm, request, senderKey);
            }, qm.recoveryDelayInMs);
            return true;
            break;

          default:
            //TBD
        }
      }
    },

    /**
     * Creates a unique, cleaned key from the request and the apiQuery
     * @param apiRequest
     */
    _getCacheKey: function(apiRequest) {
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