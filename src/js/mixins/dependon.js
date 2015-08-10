/**
 * Created by rchyla on 3/13/14.
 */

/*
 * This module contains a set of utilities that can be added to classes
 * to give them certain functionality
 */
define([
  'underscore',
  'js/components/pubsub_events',
  'js/components/pubsub_key'
  ], function(
  _,
  PubSubEvents,
  PubSubKey
  ) {
  var Mixin = {

    /*
     * BeeHive is the object that allows modules to get access to objects
     * of the application (but we make sure these objects are protected
     * and only application can set/change them). This mixin gives objects
     * functions to query 'BeeHive'
     */
    BeeHive: {
      // called by parents (app) to give modules access
      setBeeHive: function(brundibar) {
        if (_.isEmpty(brundibar))
          throw new Error('Huh? Empty Beehive? Trying to be funny?');

        this.__beehive = brundibar;
      },
      getBeeHive: function() {
        if (!this.hasBeeHive())
          throw new Error('The BeeHive is inactivate (or dead :<})');
        return this.__beehive;
      },
      hasBeeHive: function() {
        if (this.__beehive && (this.__beehive.active || (this.__beehive.__facade__ && this.__beehive.getActive()))) {
          return true;
        }
        return false;
      },

      /**
       * Method which returns a masked instance of PubSub (unless the PubSub
       * is already a hardened instance; which carries its own key)
       *
       * You can call pubsub.publish() without having to supply the pubsub key
       * (which is what most controllers want to do; there are only some
       * exceptions to this rule; ie. query-mediator). If you need to get
       * access to the full PubSub (and you have it inside BeeHive) then do
       * this.getBeeHive().getService('PubSub')
       */
      getPubSub: function() {
        if (!this.hasBeeHive())
          throw new Error('The BeeHive is inactive (or dead >:})');

        if (!this.__ctx)
          this.__ctx = {};

        if (this.__ctx.pubsub)
          return this.__ctx.pubsub;

        var pubsub = this.__beehive.getService('PubSub');

        if (pubsub && pubsub.__facade__)
          return pubsub;

        // build a unique key for this instance
        this.__ctx.pubsub = {
          _key: pubsub.getPubSubKey(),
          _exec: function(name, args) {
            args = _.toArray(args);

            if (args[0] instanceof PubSubKey)
              throw Error("You have given us a PubSub key, this.publish() method does not need it.");

            args.unshift(this._key);
            pubsub[name].apply(pubsub, args);
          },
          publish: function() { this._exec('publish', arguments) },
          subscribe: function() { this._exec('subscribe', arguments) },
          subscribeOnce: function() { this._exec('subscribeOnce', arguments) },
          unpublish: function() { this._exec('unpublish', arguments) },
          getCurrentPubSubKey: function() {return this._key}
        };
        _.extend(this.__ctx.pubsub, PubSubEvents);

        return this.__ctx.pubsub;
      },

      hasPubSub: function() {
        if (this.hasBeeHive())
          return _.isObject(this.__beehive.getService('PubSub'));
        return false;
      }

    },

    App: {
      setApp: function(app) {
        if (_.isUndefined(app))
          throw new Error('App object cannot be empty');
        this.__app = app;
      },
      getApp: function() {
        return this.__app;
      },
      hasApp: function() {
        return _.isEmpty(this.__app) ? false : true;
      }
    }
  };

  return Mixin;
});
