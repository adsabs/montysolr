/**
 * Created by rchyla on 12/2/14.
 */

/**
 * Mediator to coordinate API-feedback exchange
 * and react on them
 */

define([
    'underscore',
    'jquery',
    'cache',
    'js/components/generic_module',
    'js/mixins/dependon',
    'js/components/api_request',
    'js/components/api_response',
    'js/components/api_query_updater',
    'js/components/api_feedback',
    'js/components/pubsub_key'
  ],
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
    PubSubKey
    ) {


  var ErrorMediator = GenericModule.extend({

    initialize: function(options) {
      this._cache = this._getNewCache(options.cache);
      this.debug = options.debug || false;
      this._handlers = {}; // TODO: expose api to register handlers
      this.app = null; // reference to the main application
    },


    _getNewCache: function(options) {
      return new Cache(_.extend({
        'maximumSize': 150,
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
      var pubsub = beehive.Services.get('PubSub');
      this.pubSubKey = pubsub.getPubSubKey();

      pubsub.subscribe(this.pubSubKey, pubsub.FEEDBACK, _.bind(this.receiveFeedback, this));

      this.app = app;
      this.pubsub = pubsub;
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
        console.log('[EM]: received feedback:', apiFeedback.toJSON(), senderKey ? senderKey.getId() : null);

      var componentKey = this._getCacheKey(apiFeedback, senderKey);
      var entry = this._retrieveCacheEntry(componentKey);
      if (!entry) {
        entry = this.createNewCacheEntry(componentKey);
        this._cache.put(componentKey, entry);
      }

      var handler = this.getFeedbackHandler(apiFeedback, entry);
      if (handler) {
        if (handler.execute && handler.execute(apiFeedback, entry)) {
          return;
        }
        else if (handler.call(this, apiFeedback, entry)) {
          return;
        }
      }

      this.handleFeedback(apiFeedback, entry);
    },

    removeFeedbackHandler: function(name) {
      if (name.toString() in this._handlers)
        delete this._handlers[name.toString()];
    },

    addFeedbackHandler: function(code, func) {
      if (!code && !_.isNumber(code))
        throw new Error('first argument must be code or code:string or string');
      if (!(_.isFunction(func)))
        throw new Error('second argument must be executable');
      this._handlers[code.toString()] = func;
    },

    getFeedbackHandler: function(apiFeedback, entry) {
      var keys = [apiFeedback.code + ':' + entry.id, entry.id, apiFeedback.code ? apiFeedback.code.toString() : Date.now()];
      for (var i=0; i<keys.length; i++) {
        if (keys[i] in this._handlers) {
          return this._handlers[keys[i]];
        }
      }
    },

    _retrieveCacheEntry: function(componentKey) {
      return this._cache.getSync(componentKey);
    },


    /**
     * Creates a unique, cleaned key from the request and the apiQuery
     * @param apiFeedback
     *    instance of {ApiFeedback}
     * @param senderKey
     *    string or instance of {PubSubKey}
     */
    _getCacheKey: function(apiFeedback, senderKey) {
      if (!apiFeedback)
        throw new Error('ApiFeedback cannot be empty');
      if (apiFeedback.getSenderKey())
        return apiFeedback.getSenderKey();
      if (senderKey) {
        if (senderKey instanceof PubSubKey) {
          return senderKey.getId();
        }
        else if (_.isString(senderKey)) {
          return senderKey;
        }
      }

      var req = apiFeedback.getApiRequest();
      if (req) {
        return req.url();
      }
      throw new Error('We cannot identify the origin (recipient) of this feedback');
    },


    createNewCacheEntry: function(componentKey) {
      return {
        waiting: 0,
        max: 0,
        errors: 0,
        counter: 0,
        created: Date.now(),
        id: componentKey
      }
    },

    handleFeedback: function(apiFeedback, entry) {
      var c = ApiFeedback.CODES;

      entry.counter += 1;

      switch(apiFeedback.code) {
        case c.ALL_FINE:
          entry.errors = 0;
          break;
        case c.KEEP_WAITING:
          entry.waiting += entry.started;
          break;
        case c.SERVER_ERROR:

          break;

        default:
      }
    }

  });

  _.extend(ErrorMediator.prototype, Mixins.BeeHive);
  return ErrorMediator;
});