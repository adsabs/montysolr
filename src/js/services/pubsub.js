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
    _providers: [],

    /*
     * sends a signal 'onStart' to all listeners; the listeners
     * get a chance to return back a list of signal they are expecting
     * and we'll check whether this PubSub has implementations that
     * can serve them
     */
    onStart: function() {
      var self = this;
      this.trigger('onStart', function(attributes) {

      })
    },



    /*
     * Sends a signal 'onClose' meaning that this PubSub is going to
     * be shut down and no further messages will be accepted
     */
    onClose: function() {

    }

  });


  // Enhance the pubsub with events, but hide those that we don't
  // want to expose (they could be still accessed through the parent
  // but let's not bother with that for now)
  var error = _.bind(function() {throw new Error('PubSub forbids calling this method, use: trigger()/listenTo()')}, PubSub.prototype);

  _.extend(PubSub.prototype, Backbone.Events, {
    on: error,
    off: error,
    bind: error,
    unbind: error
  });


  return PubSub;
});
