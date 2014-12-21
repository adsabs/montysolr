/**
 * Created by rchyla on 3/16/14.
 */

define(['backbone', 'underscore', 'js/mixins/hardened', 'pubsub_service_impl', 'js/components/pubsub_events'],
  function(Backbone, _, Hardened, PubSubImplementation, PubSubEvents) {


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

  _.extend(PubSub.prototype, Hardened, {
    /**
     * The PubSub hardened instance will expose different
     * api - it doesn't allow modules to pass the PubSubKey
     *
     * @param iface
     * @returns {*}
     */
    getHardenedInstance: function(iface) {
      iface = _.clone(iface || this.hardenedInterface);

      // build a unique key for this instance
      var ctx = {
        key: this.getPubSubKey()
      };
      var self = this;
      // purpose of these functions is to expose simplified
      // api (without need to pass pubsubkey explicitly)
      iface['publish'] = function() {
        arguments = _.toArray(arguments); arguments.unshift(ctx.key);
        self.publish.apply(self, arguments);
      };
      iface['subscribe'] = function() {
        arguments = _.toArray(arguments); arguments.unshift(ctx.key);
        self.subscribe.apply(self, arguments);
      };
      iface['unsubscribe'] = function() {
        arguments = _.toArray(arguments); arguments.unshift(ctx.key);
        self.unsubscribe.apply(self, arguments);
      };
      iface['subscribeOnce'] = function() {
        arguments = _.toArray(arguments); arguments.unshift(ctx.key);
        self.subscribeOnce.apply(self, arguments);
      };
      iface['getCurrentPubSubKey'] = function() {
        return ctx.key;
      };
      var hardened = this._getHardenedInstance(iface, this);
      _.extend(hardened, PubSubEvents);

      return hardened;
    }
  });
  _.extend(PubSub.prototype, PubSubEvents);

  return PubSub;
});
