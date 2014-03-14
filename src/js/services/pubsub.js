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

define(['backbone', 'underscore', 'js/components/generic_module'], function(Backbone, _, GenericModule) {


  var PubSub = GenericModule.extend({

    className: 'PubSub',

    _signals: [],
    _providers: {},

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


    subscribe: function(obj, name, callback, context) {
      // I could use BB.listenTo() but
      // the big problem with the following call:
      //      obj.listenTo(this, name, callback);
      // is that we are giving up (exposing) our
      // pubsub; and coding defensively I DO NOT
      // want that to happen; the listenTo()
      // method is calling this.on(name, callback)
      // but also keeps track of the object to which
      // it is listening; so we can do that too

      var subscribers = this._subscribers || (this._subscribers = {});
      var id = obj._subscriberId || (obj._subscriberId = _.uniqueId('s'));
      subscribers[id] = obj;

      var events = this._events[name] || (this._events[name] = []);
      events.push({callback: callback, context: context, ctx: context || this});

      if (context) {
        callback = _.bind(callback, context);
      }
      else if (obj) {
        callback = _.bind(callback, obj);
      }

      this.on(name, callback);
    },

    unsubscribe: function(obj, name, callback) {
      obj.stopListening(this, name, callback);
    },

    publish: function() {
      this.trigger(arguments);
    }

  });


  return PubSub;
});
