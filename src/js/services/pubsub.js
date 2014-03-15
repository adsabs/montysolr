/**
 * Provides the nervous system of the application. There may exist
 * different breads of this animal and application can choose
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
 * For the moment, this object is not protected, but it allows only to send
 * signals and to subscribe to the pubsub. In the future
 * we may consider just exposing API calls to send (trigger) events
 * throug this PubSub (i.e. hide the possibility to unsubscribe)
 **/

define(['backbone', 'underscore', 'js/components/generic_module', 'js/components/pubsub_key'],
  function(Backbone, _, GenericModule, PubSubKey) {


  var PubSub = GenericModule.extend({

    className: 'PubSub',

    initialize: function(attributes, options) {
      this._issuedKeys = {};
      this.strict = true;
      _.extend(this, _.pick(options || attributes, ['strict']));
    },


    /*
     * when pubsub is activated it will issue signal 'pubsub.starting'
     * and it will check whether there are events that cannot possibly
     * by handled by some listeners
     */
    start: function() {
      this.trigger('pubsub.starting');
    },

    /*
     * Sends a signal 'pubsub.closing' to all listeners and then
     * immediately shuts down the queue and removes any keys
     */
    close: function() {
      this.trigger('pubsub:closing');
      this.off();
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
      this._checkKey(key, name, callback);
      this.on(name, callback, key); // the key becomes context
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
      this._checkKey(arguments[0]);
      var args = Array.prototype.slice.call(arguments, 1);
      this.trigger.apply(this, args);
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
      var k = PubSubKey.newInstance({creator: this});
      if (this.strict) {
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
            "wrap your callback into: _.bind(callback, context))");
        }

        if (!this._issuedKeys.hasOwnProperty(key.getId())) {
          throw new Error("Your key is not known to us, sorry, you can't use this queue.");
        }
        if (this._issuedKeys[key.getId()] !== key.getCreator()) {
          throw new Error("Your key has wrong identity, sorry, you can't use this queue.");
        }
      }
    }

  });


  return PubSub;
});
