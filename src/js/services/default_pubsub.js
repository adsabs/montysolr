/**
 * Provides the nervous system of the application. There may exist
 * different breeds of this animal and application can choose
 * which ones to use.
 *
 * The intended usage is this:
 *
 *   1. Applicaiton creates a PubSub [any implementation]
 *   2. Application loads modules/views and gives them this PubSub
 *      - modules/views can subscribe to the PubSub (ie. view.listenTo(PubSub)
 *      - modules can trigger events through the PubSub (ie. pubSub.trigger('name', ....))
 *   3. Application activates the PubSub and calls 'self-check'
 *      - the pubsub will check whether it contains providers that can handle
 *        all signals
 *
 *
 * All topics subscribed to are broadcasted using Backbone.Event. The modules/view have
 * no access to the Application object. The PubSub serves as a 'middleman'
 *
 * For the moment, this object is not protected (you should implement) the facade
 * that hides it. But it provides all necessary functionality to make it robust and
 * secured. Ie. only modules with approprite keys can subscribe/publish/unscubscribe
 *
 * Also, errors are caught and counted. Actions can be fired to treat offending
 * callbacks.
 **/

define(['backbone', 'underscore', 'js/components/generic_module', 'js/components/pubsub_key'],
  function(Backbone, _, GenericModule, PubSubKey) {

    // unfortunately, these methods are not part of the BB.Events class
    // so we have to duplicate them iff we want to provide a queue which
    // handles failed callbacks

    // ------------------------ copied from BB ------------------------------------------
    // Regular expression used to split event strings.
    var eventSplitter = /\s+/;

    // Implement fancy features of the Events API such as multiple event
    // names `"change blur"` and jQuery-style event maps `{change: action}`
    // in terms of the existing API.
    var eventsApi = function(obj, action, name, rest) {
      if (!name) return true;

      // Handle event maps.
      if (typeof name === 'object') {
        for (var key in name) {
          obj[action].apply(obj, [key, name[key]].concat(rest));
        }
        return false;
      }

      // Handle space separated event names.
      if (eventSplitter.test(name)) {
        var names = name.split(eventSplitter);
        for (var i = 0, l = names.length; i < l; i++) {
          obj[action].apply(obj, [names[i]].concat(rest));
        }
        return false;
      }

      return true;
    };
    // ------------------------ /copied from BB ------------------------------------------

    var PubSub = GenericModule.extend({

    className: 'PubSub',

    initialize: function(options) {
      this._issuedKeys = {};
      this.strict = true;
      this.handleErrors = true;
      this._errors = {};
      this.errWarningCount = 10; // this many errors trigger warning
      _.extend(this, _.pick(options, ['strict', 'handleErrors', 'errWarningCount']));
      this.pubSubKey = PubSubKey.newInstance({creator: {}}); // this.getPubSubKey(); // the key the pubsub uses for itself
      this._issuedKeys[this.pubSubKey.getId()] = this.pubSubKey.getCreator();
      this.running = true;
      this.debug = false;
    },


    /*
     * when pubsub is activated it will issue signal 'pubsub.starting'
     * and it will check whether there are events that cannot possibly
     * by handled by some listeners
     */
    start: function() {
      this.publish(this.pubSubKey, this.OPENING_GATES);
      this.publish(this.pubSubKey, this.OPEN_FOR_BUSINESS);
      this.running = true;
    },

    /*
     * Sends a signal 'pubsub.closing' to all listeners and then
     * immediately shuts down the queue and removes any keys
     */
    close: function() {
      this.publish(this.pubSubKey, this.CLOSING_GATES);
      this.off();
      this.publish(this.pubSubKey, this.CLOSED_FOR_BUSINESS);
      this.running = false;
      this._issuedKeys = {};
    },



    /*
     * subscribe() -> undefined or error
     *
     *  - key: instance of PubSubKey (it must be known
     *         to this PubSub - so typically it is a key
     *         issued by this PubSub)
     *  - name: string, name of the event (can be name
     *         accepted by Backbone)
     *  - callback: a function to call (you cannot supply
     *         context, so if the callback needs to be bound
     *         use: _.bind(callback, context)
     *
     */
    subscribe: function(key, name, callback) {
      if (!this.isRunning()) {
        throw new Error('PubSub has been closed, ignoring futher requests');
      }
      this._checkKey(key, name, callback);
      if (_.isUndefined(name)) {
        throw new Error("You tried to subscribe to undefined event. Error between chair and keyboard?");
      }
      this.on(name, callback, key); // the key becomes context
      this.on(name+key.getId(), callback, key); // this is for individual responses
    },

    /*
     * subscribeOnce() -> undefined or error
     *
     *  Just like subscribe, but it will be removed by PubSub once
     *  it has been called
     *
     *  - key: instance of PubSubKey (it must be known
     *         to this PubSub - so typically it is a key
     *         issued by this PubSub)
     *  - name: string, name of the event (can be name
     *         accepted by Backbone)
     *  - callback: a function to call (you cannot supply
     *         context, so if the callback needs to be bound
     *         use: _.bind(callback, context)
     *
     */
    subscribeOnce: function(key, name, callback) {
      this._checkKey(key, name, callback);
      if (_.isUndefined(name)) {
        throw new Error("You tried to subscribe to undefined event. Error between chair and keyboard?");
      }
      this.once(name, callback, key); // the key becomes context
      this.once(name+key.getId(), callback, key); // this is for individual responses
    },

    /*
     * unsubscribe() -> undefined or error
     *
     *  - key: instance of PubSubKey (it must be known
     *         to this PubSub - so typically it is a key
     *         issued by this PubSub)
     *  - name: string, name of the event (can be name
     *         accepted by Backbone)
     *  - callback: a function to call (you cannot supply
     *         context, so if the callback needs to be bound
     *         use: _.bind(callback, context)
     *
     *  When you supply only:
     *   - key: all callbacks registered under this key will
     *          be removed
     *   - key+name: all callbacks for this key (module) and
     *         event will be removed
     *   - key+name+callback: the most specific call, it will
     *         remove only one callback (if it is there)
     *
     */
    unsubscribe: function(key, name, callback) {
      this._checkKey(key, name, callback);
      var context = key;
      if (name && callback) {
        this.off(name, callback, context);
      }
      else if (name || callback) {
        this.off(name, callback, context);
      }
      else { // remove all events of this subscriber
        var names = _.keys(this._events), name, events,ev, i, l, k, j;
        for (i = 0, l = names.length; i < l; i++) {
          name = names[i];
          if (events = this._events[name]) {
            var toRemove = [];
            for (j = 0, k = events.length; j < k; j++) {
              ev = events[j];
              if (ev.context === context) {
                toRemove.push(ev);
              }
            }
            for (i=0, l = toRemove.length; i < l; i++) {
              this.off(name, toRemove[i].callback, context);
            }
          }
        }
      }

    },

    /*
     * publish(key, event-name, arguments...) -> undef
     *
     * Publish the message with any set of arguments
     * into the queue. No checking is done whether there
     * are callbacks that can handle the event
     */
    publish: function() {

      if (!this.isRunning()) {
        console.error('PubSub has been closed, ignoring futher requests');
        return;
      }

      this._checkKey(arguments[0]);
      var args = Array.prototype.slice.call(arguments, 1);
      
      if (args.length == 0 || _.isUndefined(args[0])) {
        throw new Error("You tried to trigger undefined event. Error between chair and keyboard?");
      }

      // push the key back into the arguments (it identifies the sender)
      args.push(arguments[0]);

      //console.log('publishing', arguments, args);

      // this is faster, default BB implementation
      if (!this.handleErrors) {
        return this.trigger.apply(this, args);
      }
      else { // safer, default
        return this.triggerHandleErrors.apply(this, args);
      }

    },

    /*
     * getPubSubKey() -> PubSubKey
     *
     * Returns a new instance of PubSubKey - every
     * subscriber must obtain one instance of the key
     * and use it for all calls to publish/(un)subscribe
     *
     * If this queue is running in a strict mode, the
     * keys will be remembered and they will be checked
     * during the calls.
     */
    getPubSubKey: function() {
      var k = PubSubKey.newInstance({creator: this.pubSubKey}); // creator identifies issuer of the key
      if (this.strict) {
        if (this._issuedKeys[k.getId()]) {
          throw Error("The key with id", k.getId(), "has been already registered!");
        }
        this._issuedKeys[k.getId()] = k.getCreator();
      }
      return k;
    },

    /*
     * Says whether this PubSub is running in a strict mode
     */
    isStrict: function() {
      return this.strict;
    },

    /*
     * Checks the key - subscriber must supply it when calling
     */
    _checkKey: function(key, name, callback) {
      if (this.strict) {

        if (_.isUndefined(key)) {
          throw new Error("Every request must be accompanied by PubSubKey");
        }
        if (!(key instanceof PubSubKey)) {
          throw new Error("Key must be instance of PubSubKey. " +
            "(If you are trying to pass context, you can't do that. Instead, " +
            "wrap your callback into: _.bind(callback, context))" + "\n" +
            "Perhaps the PubSub you are using is the non-protected version?");
        }

        if (!this._issuedKeys.hasOwnProperty(key.getId())) {
          throw new Error("Your key is not known to us, sorry, you can't use this queue.");
        }
        if (this._issuedKeys[key.getId()] !== key.getCreator()) {
          throw new Error("Your key has wrong identity, sorry, you can't use this queue.");
        }
      }
    },

    // Copied and modified version of the BB trigger - we deal with errors
    // and optionally execute stuff asynchronously
    triggerHandleErrors: function(name) {

      // almost the same as BB impl, but we call local triggerEvents
      // that do error handling
      if (!this._events) return this;
      var args = Array.prototype.slice.call(arguments, 1);
      if (!eventsApi(this, 'trigger', name, args)) return this;
      var events = this._events[name];
      var allEvents = this._events.all;

      if (events) this.triggerEvents(events, args);
      if (allEvents) this.triggerEvents(allEvents, arguments);

      return this;
    },

    // A modified version of BB - errors will not disrupt the queue
    triggerEvents: function(events, args) {
      var ev, i = -1, l = events.length, a1 = args[0], a2 = args[1], a3 = args[2];
      switch (args.length) {
        case 0: while (++i < l) try{(ev = events[i]).callback.call(ev.ctx)} catch(e) {this.handleCallbackError(e, ev, args)}; return;
        case 1: while (++i < l) try{(ev = events[i]).callback.call(ev.ctx, a1)} catch(e) {this.handleCallbackError(e, ev, args)}; return;
        case 2: while (++i < l) try{(ev = events[i]).callback.call(ev.ctx, a1, a2)} catch(e) {this.handleCallbackError(e, ev, args)}; return;
        case 3: while (++i < l) try{(ev = events[i]).callback.call(ev.ctx, a1, a2, a3)} catch(e) {this.handleCallbackError(e, ev, args)}; return;
        default: while (++i < l) try{(ev = events[i]).callback.apply(ev.ctx, args)} catch(e) {this.handleCallbackError(e, ev, args)};
      }
    },

    // the default implementation just counts the number of errors per module (key) and
    // triggers pubsub.many_errors
    handleCallbackError: function(e, event, args) {
      console.warn('[PubSub] Error: ', event, args);
      if (this.debug) {
        throw e;
      }
      else {
        console.warn(e.stack);
      }

      var kid = event.ctx.getId();
      var nerr = (this._errors[kid] = (this._errors[kid] || 0) + 1);
      if (nerr % this.errWarningCount == 0) {
        this.publish(this.pubSubKey, this.BIG_FIRE, nerr, e, event, args);
      }
    },

    isRunning: function() {
      return this.running;
    }

  });


  return PubSub;
});
