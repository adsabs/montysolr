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

    registerModule: function(module) {
      if (!module instanceof GenericModule) {
        throw new Error('We can register only instances of GenericModule');
      }
      // listen to the events of the module
      this.listenTo(module, '[pubsub]', function() {this.trigger});
      // give the module a chance to call the pubsub (but we don't give it access to pubsub)
      var self = this;
      module.register({
        triggerPubSub: function() {
          if (self._listeners.hasOwnProperty(this.mid)) {
            return false; // pubsub has removed this module
          }
          self.trigger(arguments);
        },
        isRegistered: function() {
          if (self._listeners.hasOwnProperty(this.mid)) {
            return false; // pubsub has removed this module
          }
          return true;
        }
      });
      // save the module
      if (!this._providers.hasOwnProperty(module.mid)) {
        this._providers[module.mid] = module;
      }
    },

    unRegisterModule: function(module) {
      if (!module instanceof GenericModule) {
        throw new Error('We can register only instances of GenericModule');
      }
      // stop listening to events
      this.stopListening(module);
      // remove module from providers
      delete this._providers[module.mid];
    },

    /*
     * sends a signal 'onStart' to all listeners; the listeners
     * get a chance to return back a list of signal they are expecting
     * and we'll check whether this PubSub has implementations that
     * can serve them
     */
    onStart: function() {
      this.trigger('[pubsub]:starting');
    },

    /*
     * Sends a signal 'onClose' meaning that this PubSub is going to
     * be shut down and no further messages will be accepted
     */
    onClose: function() {
      this.trigger('[pubsub]:closing');
      for (var k in _.keys(this._providers)) {
        this.unRegisterModule(k);
      }
    },

    _checkCaller: function(key, name, callback) {
      if (_.isUndefined(key)) {
        throw new Error("Every request must be accompanied by PubSubKey");
      }
      if (!(key instanceof PubSubKey)) {
        throw new Error("Key must be instance of PubSubKey. " +
          "(If you are trying to pass context, you can't do that. Instead, " +
          "wrap your callback into: _.bind(callback, context))");
      }
      if (this.strict) {
        if (!this._issuedKeys.hasOwnProperty(key.getId())) {
          throw new Error("Your key is not known to us, sorry, you can't use this queue.");
        }
        if (this._issuedKeys[key.getId()] !== key.getCreator()) {
          throw new Error("Your key has wrong identity, sorry, you can't use this queue.");
        }
      }
    },

    subscribe: function(key, name, callback) {
      this._checkCaller(key, name, callback);
      this.on(name, callback, key); // the key becomes context
    },

    unsubscribe: function(key, name, callback) {
      this._checkCaller(key, name, callback);
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

    publish: function() {
      this.trigger(arguments);
    },

    getPubSubKey: function() {
      var k = PubSubKey.newInstance({creator: this});
      if (this.strict) {
        this._issuedKeys[k.getId()] = k.getCreator();
      }
      return k;
    }

  });


  return PubSub;
});
