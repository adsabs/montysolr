/**
 * Created by rchyla on 3/14/14.
 */

/*
 * A simple, yet important, class - every subscriber
 * to the PubSub must contain one key. This class
 * should be instantiated in a safe manner. ie.
 *
 * PubSubKey.newInstance({creator: this});
 *
 * But beware that as long as the subscriber is alive
 * reference to the creator will be saved inside
 * the key! So choose carefully whether you use this
 * functionality
 */

define(['underscore'], function(_) {
  var PubSubKey = function(options) {
    _.extend(this,options);
  };

  _.extend(PubSubKey, {
    /*
     * Creates a new Instances of the PubSubKey
     * with a storage that cannot be changed.
     * To double sign the key, you can pass
     * an object that identifies creator of the
     * key and test identity, eg.
     *
     * var creator = {};
     * var k = PubSubKey(creator);
     * k.getCreator() === k;
     *
     */
    newInstance: function(options) {
      var private = {
        id: _.has(options, 'id') ? options.id : _.uniqueId('psk'),
        creator: _.has(options, 'creator') ? options.creator : null
      };
      return new PubSubKey({
        getId: function() {
          return private.id;
        },
        getCreator: function() {
          return private.creator;
        }
      });
    }
  });
  return PubSubKey;
})
