/**
 * Created by rchyla on 12/2/14.
 */

/**
 * Mediator to coordinate API-feedback exchange
 * and react on them
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


  var ErrorMediator = GenericModule.extend({

    initialize: function(options) {
      this._cache = this._getNewCache(options.cache);
      this.debug = options.debug || false;
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

      pubsub.subscribe(this.pubSubKey, pubsub.FEEDBACK, _.bind(this.receiveFeedback, this));
    },



    /**
     * This method receives ApiFeedback objects (usually from the API)
     * and decides what to do with them.
     *
     * @param apiFeedback
     * @param senderKey - if present, identifies the widget that made
     *                    the request
     */
    receiveFeedback: function(apiFeedback, senderKey) {
      if (this.debug)
        console.log('[EM]: received feedback:', apiFeedback.toJSON(), senderKey.getId());

      var ps = this.getBeeHive().Services.get('PubSub');

      var componentKey = this.getCacheKey(apiFeedback, senderKey);
      var entry = this._cache.getSync(componentKey);
      if (!entry) {
        entry = this.createNewCacheEntry();
        this._cache.put(componentKey, entry);
      }

      if (this.handleBenignFeedback(entry, apiFeedback))
        return;

      var c = ApiFeedback.CODES;
      switch(apiFeedback.code) {
        case c.ALL_FINE:
          entry.errors = 0;
          break;
        case c.KEEP_WAITING:
          entry.waiting += entry.started
          break;
        case c.SERVER_ERROR:

          break;

        default:
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

  _.extend(ErrorMediator.prototype, Mixins.BeeHive);
  return ErrorMediator;
});