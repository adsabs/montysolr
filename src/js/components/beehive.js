/**
 * Created by rchyla on 3/16/14.
 */

define(['backbone', 'underscore',
  'js/components/generic_module', 'js/mixins/dependon', 'js/components/facade'],
  function(Backbone, _, GenericModule, Dependon, Facade) {

  var hiveOptions = [];
  var BeeHive = GenericModule.extend({

    initialize: function(attrs, options) {
      _.extend(this, _.pick(options, hiveOptions));
    },
    // wraps BeeHive into a facade so that it can be shared with other
    // components (without fear they will have access to private methods
    // or will be changing something)
    getProtectedInstance: function() {
      var pubsubIface = {
        publish: 'publishes message to the pubsub',
        subscribe: 'attaches callbacks from the queue',
        unsubscribe: 'detaches callbacks from the queue'
      };
      var Bee = new {};
      Bee.Services = {
        PubSub: new Facade(pubsubIface, this.PubSub)
      };

    }
  });

  _.extend(BeeHive.prototype, Dependon.Beehive, Dependon.PubSub);

  return BeeHive;
});
