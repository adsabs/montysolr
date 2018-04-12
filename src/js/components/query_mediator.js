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
    'js/mixins/add_secondary_sort',
    'js/components/api_request',
    'js/components/api_response',
    'js/components/api_query_updater',
    'js/components/api_feedback',
    'js/components/json_response',
    'js/components/api_targets',
    'js/components/api_query',
    'analytics'
  ],
  function(
    _,
    $,
    Cache,
    GenericModule,
    Dependon,
    SecondarySort,
    ApiRequest,
    ApiResponse,
    ApiQueryUpdater,
    ApiFeedback,
    JsonResponse,
    ApiTargets,
    ApiQuery,
    analytics
    ) {


    var QueryMediator = GenericModule.extend({

      initialize: function(options) {
        options = options || {};
        this._cache = null;
        this.debug = options.debug || false;
        this.queryUpdater = new ApiQueryUpdater('QueryMediator');
        this.failedRequestsCache = this._getNewCache({
          'expiresAfterWrite': 5
        });
        this.maxRetries = options.maxRetries || 3;
        this.recoveryDelayInMs = _.isNumber(options.recoveryDelayInMs) ? options.recoveryDelayInMs : 700;
        this.__searchCycle = {waiting:{}, inprogress: {}, done: {}, failed: {}};
        this.shortDelayInMs = _.isNumber(options.shortDelayInMs) ? options.shortDelayInMs : 10;
        this.longDelayInMs = _.isNumber(options.longDelayInMs) ? options.longDelayInMs: 100;
        this.monitoringDelayInMs = _.isNumber(options.monitoringDelayInMs) ? options.monitoringDelayInMs : 200;
        this.mostRecentQuery = new ApiQuery();
      },

      activateCache: function(options) {
        this._cache = this._getNewCache((options || {}).cache);
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
      activate: function(beehive, app) {
        this.setBeeHive(beehive);
        this.setApp(app);

        var pubsub = this.getPubSub();

        // if you run discovery-mediator; this signal may be removed from the
        // queue (and instead, the discovery mediator will serve the request)
        pubsub.subscribe(pubsub.START_SEARCH, _.bind(this.getQueryAndStartSearchCycle, this));
        pubsub.subscribe(pubsub.DELIVERING_REQUEST, _.bind(this.receiveRequests, this));
        pubsub.subscribe(pubsub.EXECUTE_REQUEST, _.bind(this.executeRequest, this));
        pubsub.subscribe(pubsub.GET_QTREE, _.bind(this.getQTree, this));
      },


      getQTree: function(apiQuery, senderKey) {
        var apiRequest = new ApiRequest({'query': apiQuery, 'target': ApiTargets.QTREE});
        this._executeRequest(apiRequest, senderKey);
      },

      doQueryTranslation: function(q) {
        var d = $.Deferred(),
          pubsub = this.getPubSub(),
          options = {
            type : "POST",
            contentType : "application/json"
          };

        // Send the entire query to the "query" endpoint of the "object_service" micro service
        // this endpoint will grab the "object:<expression>" from the query string and send back
        // a translated query string which will have "simbid:<expression with SIMBAD identifiers>"
        // instead of the "object:" part
        var request =  new ApiRequest({
          target: ApiTargets.SERVICE_OBJECTS_QUERY,
          query: new ApiQuery({
            'query': q
          }),
          options : options
        });
        // when the promise gets resolved, it will have the JSON response of the micro service
        // which will have the translated object query
        pubsub.subscribeOnce(pubsub.DELIVERING_RESPONSE, function(response){
          d.resolve(response.toJSON());
        });

        pubsub.publish(pubsub.EXECUTE_REQUEST, request);
        return d.promise();
      },

      getQueryAndStartSearchCycle : function(apiQuery, senderKey){

        var that = this,
            ps = this.getPubSub();

        //modifies apiQuery in place
        SecondarySort.addSecondarySort(apiQuery);

        // Watch for simbid references and use a masked version of the
        // query to generate the url if any are found.
        if (this.original_url && apiQuery.get('q') && apiQuery.get('q')[0].indexOf('simbid') !== -1) {
          var newQ = apiQuery.clone();
          var origQ = apiQuery.get('q')[0];

          if (apiQuery.has('__original_query')) {
            origQ = apiQuery.get('__original_query')[0];
          }

          // Use the old query for the clone's query string, then get url
          newQ.set('q', origQ);
          this.original_url = this.queryUpdater.clean(newQ).url();
        } else {
          this.original_url = this.queryUpdater.clean(apiQuery).url();
        }

        this.original_query = apiQuery.get('q');

        // checking if it's a new big query
        if (apiQuery.get("__bigquery")) {

          apiQuery.set("bigquery","bibcode\n" + apiQuery.get("__bigquery").join("\n") );
          //don't need this anymore
          apiQuery.unset("__bigquery");
          //query might have a q, otherwise q is everything
          if (!apiQuery.get("q")) apiQuery.set("q", "*:*");
          //bigquery endpoint needs this value set
          apiQuery.add("fq", "{!bitset}");

          var request = new ApiRequest({
            target : ApiTargets.MYADS_STORAGE + "/query",
            query : apiQuery,
            options : {
              type : "POST",
              contentType : "application/json",
              done : function(response){

                var newQuery = new ApiQuery({q : apiQuery.get("q"),
                 "__qid" : response.qid,
               });

               var bigquerySource = apiQuery.get("__bigquerySource");
               if ( bigquerySource ){
                 newQuery.set('__bigquerySource', bigquerySource[0])
               }
               if ( apiQuery.get('sort') ){
                 newQuery.set('sort', apiQuery.get('sort') )
               }

                that.startSearchCycle( newQuery, senderKey )
              },
              fail : function(jqXHR, textStatus, errorThrown){
                console.warn("bigquery failed:", [].slice.apply(arguments).join(","));

                ps.publish(ps.FEEDBACK, new ApiFeedback({
                  code: ApiFeedback.CODES.SEARCH_CYCLE_FAILED_TO_START,
                  error: {jqXHR: jqXHR, textStatus: textStatus, errorThrown: errorThrown}
                }));
              }
            }
          });

          //it needs to be a bigquery, we need to formulate the query
          this.getBeeHive().getService("Api").request(request);

        }

        // check if this is an "object:" query
        else if (apiQuery.get("q")[0].indexOf("object:") > -1) {
          // we have an "object:" query as part of the query
          // first define a callback function to process the response of the micro service
          // and bind it to "this" so that we can use the trigger
          var callback = function(v){
            apiQuery.set({
              q : v.query
            });
            this.startSearchCycle(apiQuery, senderKey);
          }.bind(this);
          // call the callback function when the promise has been resolved
          this.doQueryTranslation(apiQuery.get("q")[0]).done(callback);
        }
        else {
          this.startSearchCycle.apply(this, arguments);
        }

       },

      /**
       * Happens at the beginning of the new search cycle. This is the 'race started' signal
       */
      startSearchCycle: function(apiQuery, senderKey) {

        //we have to clear selected records in app storage here too
        if ( this.getBeeHive().getObject("AppStorage")){
          this.getBeeHive().getObject("AppStorage").clearSelectedPapers();
        }

        /*
          if it's a pre-existing bigquery NOT supplanted by another bigquery,
          and not forcibly clearing the current bigquery, then
          keep it and just augment it rather than losing the bigquery
          this is so that users can add q parameters to bigqueries, e.g. library exports
         */
        if (this.mostRecentQuery.get('__qid') &&
         !apiQuery.get('__qid') &&
         !apiQuery.get('__clearBiqQuery')
       ){
          this.mostRecentQuery.set('q', apiQuery.get('q'));
          apiQuery = this.mostRecentQuery;
        }

        this.mostRecentQuery = apiQuery;

        if (this.debug) {
          console.log('[QM]: received query:',
            this.hasApp() ? (this.getApp().getPluginOrWidgetName(senderKey.getId()) || senderKey.getId()) : senderKey.getId(),
            apiQuery.url()
          );
        }

        if (apiQuery.keys().length <= 0) {
          console.error('[QM] : received empty query (huh?!)');
          return;
        }

        if (apiQuery.get("q")[0].indexOf("simbid") > -1) {
          apiQuery.add("__original_url", this.original_url);
          apiQuery.add("__original_query", this.original_query);
        }
        var ps = this.getPubSub();

        if (this.__searchCycle.running && this.__searchCycle.waiting && _.keys(this.__searchCycle.waiting)) {
          console.error('The previous search cycle did not finish, and there already comes the next!');
        }

        this.reset();
        this.__searchCycle.initiator = senderKey.getId();
        this.__searchCycle.collectingRequests = true;
        this.__searchCycle.query = apiQuery.clone();

        // we will protect the query -- in the future i can consider removing 'unlock' to really
        // cement the fact the query MUST NOT be changed (we want to receive a modified version)
        var q = apiQuery.clone();
        //q = this.queryUpdater.clean(q);

        // since widgets are dirty bastards, we will remove the fl parameter (to avoid cross-
        // contamination)
        q.unset('fl');

        q.lock();
        ps.publish(ps.INVITING_REQUEST, q);

        // give widgets some time to submit their requests
        var self = this;

        if (this.shortDelayInMs) {
          setTimeout(function() {
            self.__searchCycle.collectingRequests = false;
            if (self.startExecutingQueries()) {
              self.monitorExecution();
            }
          }, this.shortDelayInMs);
        }
        else {
          this.__searchCycle.collectingRequests = false;
          if (self.startExecutingQueries()) {
            setTimeout(function() {
              self.monitorExecution();
            }, this.shortDelayInMs);
          }
        }
      },


      /**
       * Starts executing queries from the search cycle
       *
       * Return value indicates whether the process starter;
       * if 'true', then you can start monitoring
       *
       * @param force
       */
      startExecutingQueries: function(force) {

        var self = this;
        var cycle = this.__searchCycle;
        if (cycle.running) return; // safety barrier
        if (_.isEmpty(cycle.waiting)) return;
        if (!this.hasBeeHive()) return;

        cycle.running = true;

        var data;
        var beehive = this.getBeeHive();
        var api = beehive.getService('Api');
        var ps = this.getPubSub();

        if (!(ps && api)) return; // application is gone

        var app = this.getApp();
        var pskToExecuteFirst;
        if (pskToExecuteFirst = app.getPskOfPluginOrWidget('widget:Results')) { // pick a request that will be executed first
          if (cycle.waiting[pskToExecuteFirst]) {
            data = cycle.waiting[pskToExecuteFirst];
            delete cycle.waiting[pskToExecuteFirst];
          }
        }
        if (!data && cycle.waiting[cycle.initiator]) { // grab the query/request which started the cycle
          data = cycle.waiting[cycle.initiator];
          delete cycle.waiting[cycle.initiator];
        }
        if (!data) {
          if (this.debug)
            console.warn('DynamicConfig does not tell us which request to execute first (grabbing random one).');

          var kx;
          data = cycle.waiting[(kx=_.keys(cycle.waiting)[0])];
          delete cycle.waiting[kx]
        }

        // execute the first search (if it succeeds, fire the rest)
        var firstReqKey = data.key.getId();
        cycle.inprogress[firstReqKey] = data;

        this._executeRequest(data.request, data.key)
          .done(function(response, textStatus, jqXHR) {
            cycle.done[firstReqKey] = data;
            delete cycle.inprogress[firstReqKey];

            var numFound;
            if (response.response && response.response.numFound) {
              numFound = response.response.numFound;
            }

            ps.publish(ps.FEEDBACK, new ApiFeedback({
              code: ApiFeedback.CODES.SEARCH_CYCLE_STARTED,
              query: cycle.query,
              request: data.request,
              numFound: numFound,
              cycle: cycle,
              response: response // this is a raw response (and it is save to send, cause it was already copied by the first 'done' callback
            }));

            // after we are done with the first query, start executing other queries
            var f = function() {
              _.each(_.keys(cycle.waiting), function (k) {
                data = cycle.waiting[k];
                delete cycle.waiting[k];
                cycle.inprogress[k] = data;
                var psk = k;

                self._executeRequest.call(self, data.request, data.key)
                  .done(function() {
                    cycle.done[psk] = cycle.inprogress[psk];
                    delete cycle.inprogress[psk];
                  })
                  .fail(function() {
                    cycle.failed[psk] = cycle.inprogress[psk];
                    delete cycle.inprogress[psk];
                  })
                  .always(function() {
                    if (cycle.finished) return;

                    if (_.isEmpty(cycle.inprogress)) {
                      ps.publish(ps.FEEDBACK, new ApiFeedback({
                        code: ApiFeedback.CODES.SEARCH_CYCLE_FINISHED,
                        cycle: cycle
                      }));
                      cycle.finished = true;
                    }
                  })
              });
            };

            // for the display experience, it is better to introduce delays
            if (self.longDelayInMs && self.longDelayInMs > 0) {
              setTimeout(function() {
                f();
              }, self.longDelayInMs);
            }
            else {
              f();
            }
          })
          .fail(function(jqXHR, textStatus, errorThrown) {
            self.__searchCycle.error = true;
            ps.publish(ps.FEEDBACK, new ApiFeedback({
              code: ApiFeedback.CODES.SEARCH_CYCLE_FAILED_TO_START,
              cycle: cycle,
              request: this.request,
              error: {jqXHR: jqXHR, textStatus: textStatus, errorThrown: errorThrown}
            }));
          });

        return true; // means that the process can be monitored
      },

      monitorExecution: function() {

        if (!this.hasBeeHive()) return; // app is closed

        var self = this;
        var ps = this.getPubSub();
        if (!ps) return; // application is gone

        this.__searchCycle.monitor += 1;

        if (this.__searchCycle.monitor > 100) {
          console.warn('Stopping monitoring of queries, it is running too long');
          ps.publish(ps.FEEDBACK, new ApiFeedback({
            code: ApiFeedback.CODES.SEARCH_CYCLE_STOP_MONITORING,
            cycle: this.__searchCycle
          }));
          return;
        }

        if (this.__searchCycle.inprogress && _.isEmpty(this.__searchCycle.inprogress)) {

          if (this.__searchCycle.finished) return; // it was already signalled

          ps.publish(ps.FEEDBACK, new ApiFeedback({
            code: ApiFeedback.CODES.SEARCH_CYCLE_FINISHED,
            cycle: this.__searchCycle
          }));
          return;
        }

        var lenToDo = _.keys(this.__searchCycle.waiting).length;
        var lenDone = _.keys(this.__searchCycle.done).length;
        var lenInProgress = _.keys(this.__searchCycle.inprogress).length;
        var lenFailed = _.keys(this.__searchCycle.failed).length;

        var total = lenToDo + lenDone + lenInProgress + lenFailed;

        ps.publish(ps.FEEDBACK, new ApiFeedback({
          code: ApiFeedback.CODES.SEARCH_CYCLE_PROGRESS,
          msg: (lenToDo / total),
          total: total,
          todo: lenToDo,
          cycle: this.__searchCycle
        }));

        setTimeout(function() {
          self.monitorExecution();
        }, self.monitoringDelayInMs);
      },

      /**
       * This method harvest requests from the PubSub and stores them inside internal
       * datastruct
       *
       * @param apiRequest
       * @param senderKey
       */
      receiveRequests: function(apiRequest, senderKey) {
        if (this.debug)
          console.log('[QM]: received request:',
            this.hasApp() ? (this.getApp().getPluginOrWidgetName(senderKey.getId()) || senderKey.getId()) : senderKey.getId(),
            apiRequest.url()
          );

        if (this.__searchCycle.collectingRequests) {
          this.__searchCycle.waiting[senderKey.getId()] = {request: apiRequest, key: senderKey};
        }
        else {
          this.executeRequest(apiRequest, senderKey);
        }
      },

      /**
       * This method executes a request, we check
       * the local cache and also prepare context for the done/fail callbacks
       *
       * @param apiRequest
       * @param senderKey
       */
      executeRequest: function(apiRequest, senderKey) {
        if (!(apiRequest instanceof ApiRequest)) {
          throw new Error('Sir, I belive you forgot to send me a valid ApiRequest!');
        }
        else if (!senderKey){
          throw new Error("Request executed, but no widget id provided!");
        }

        return this._executeRequest(apiRequest, senderKey);
      },

      _executeRequest: function(apiRequest, senderKey) {

        // for altering widget queries
        // from regular solr requests to execute_query requests
        // if bigquery is being used

        //it's a bigquery
        if (apiRequest.get("query") && apiRequest.get("query").get("__qid")){
          var qid = apiRequest.get("query").get("__qid")[0];
          apiRequest.set("target", ApiTargets.MYADS_STORAGE + "/execute_query/" + qid);
        }

        var ps = this.getPubSub();
        var api = this.getBeeHive().getService('Api');

        var requestKey = this._getCacheKey(apiRequest);
        var maxTry = this.failedRequestsCache.getSync(requestKey) || 0;

        if (maxTry >= this.maxRetries) {
          this.onApiRequestFailure.apply({request:apiRequest, key: senderKey, requestKey:requestKey, qm: this},
            [{status: ApiFeedback.CODES.TOO_MANY_FAILURES}, 'Error', 'This request has reached maximum number of failures (wait before retrying)']);
          var d = $.Deferred();
          return d.reject();
        }


        if (this._cache) {

          var resp = this._cache.getSync(requestKey);
          var self = this;

          if (resp && resp.promise) { // we have already created ajax request

            resp.done(function() {
              self._cache.put(requestKey, arguments);
              self.onApiResponse.apply(
                {request:apiRequest, key: senderKey, requestKey:requestKey, qm: self }, arguments);
            });
            resp.fail(function() {
              self._cache.invalidate(requestKey);
              self.onApiRequestFailure.apply(
                {request:apiRequest, pubsub: ps, key: senderKey, requestKey:requestKey,
                  qm: self }, arguments);
            });
            return resp;
          }
          else if (resp) { // we already have data (in the cache)
            var defer = $.Deferred();
            defer.done(function() {
              self.onApiResponse.apply(
                {request: apiRequest, key: senderKey, requestKey: requestKey, qm: self}, resp)
            });
            defer.resolve();
            return defer.promise();
          }
          else { // create a new query

            var promise = api.request(apiRequest, {
                done: function() {
                self._cache.put(requestKey, arguments);
                self.onApiResponse.apply(this, arguments);
              },
              fail: function() {
                self._cache.invalidate(requestKey);
                self.onApiRequestFailure.apply(this, arguments);
              },
              context: {request:apiRequest, key: senderKey, requestKey:requestKey, qm: self }
            });
            this._cache.put(requestKey, promise);
            return promise;
          }
        }
        else {
          return api.request(apiRequest, {
            done: this.onApiResponse,
            fail: this.onApiRequestFailure,
            context: {request:apiRequest, key: senderKey, requestKey:requestKey, qm: this }
          });
        }

      },

      onApiResponse: function(data, textStatus, jqXHR ) {
        var qm = this.qm;

        // TODO: check the status responses

        var response = (data.responseHeader && data.responseHeader.params) ? new ApiResponse(data) : new JsonResponse(data);

        response.setApiQuery(this.request.get('query'));

        if (qm.debug)
          console.log('[QM]: sending response:',
            qm.hasApp() ? (qm.getApp().getPluginOrWidgetName(this.key.getId()) || this.key.getId()) : this.key.getId(),
            data
          );

        var pubsub = qm.getBeeHive().getService('PubSub'); // we cant use getPubSub() as we are sending the key

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
          return true; // means we are trying to recover
        }

        var feedback = new ApiFeedback({
          code:ApiFeedback.CODES.API_REQUEST_ERROR,
          msg:textStatus,
          request: this.request,
          error: jqXHR,
          psk: this.key,
          errorThrown: errorThrown,
          text: textStatus
        });

        var pubsub = qm.getBeeHive().getService('PubSub');
        if (pubsub)
          pubsub.publish(this.key, pubsub.FEEDBACK, feedback);

        return false; // means: we gave up
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
            case 409: // conflict
            case 500: // server error
            case 502: // bad gateway
            case 503: // service unavailable
            case 504: // gateway timeout
              analytics('send', 'event', 'introspection', 'retrying', status); 
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
                qm._executeRequest.call(qm, request, senderKey);
              }, qm.recoveryDelayInMs);
              return true;
              break;

            default:
              analytics('send', 'event', 'introspection', 'not-retrying', status); 
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
      },

      reset: function() {
        this.__searchCycle = {waiting:{}, inprogress: {}, done:{}, failed:{}}; //reset the datastruct
        if (this._cache) {
          this._cache.invalidateAll();
        }
      },

      resetFailures: function() {
        this.failedRequestsCache.invalidateAll();
      }

    });

    _.extend(QueryMediator.prototype, Dependon.BeeHive, Dependon.App);
    return QueryMediator;
  });
