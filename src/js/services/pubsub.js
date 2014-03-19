/**
 * Created by rchyla on 3/16/14.
 */

define(['backbone', 'underscore', 'js/mixins/hardened', 'pubsub_service_impl'], function(Backbone, _, Hardened, PubSubImplementation) {


  var PubSub = PubSubImplementation.extend({

    /*
     * Wraps itself into a Facade that can be shared with other modules
     * (it is read-only); absolutely non-modifiable and provides the
     * following callbacks:
     *  - publish
     *  - subscribe
     *  - unsubscribe
     *  - getPubSubKey
     */
    hardenedInterface:  {
      subscribe: 'register callback',
      unsubscribe: 'deregister callback',
      publish: 'send data to the queue',
      getPubSubKey: 'get secret key'
    }
  });

  _.extend(PubSub.prototype, Hardened);

  return PubSub;
});
