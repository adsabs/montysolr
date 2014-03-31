/**
 * Created by rchyla on 3/16/14.
 */

define(['backbone', 'underscore',
  'js/components/generic_module', 'js/mixins/dependon', 'js/mixins/hardened', 'js/components/services_container'],
  function(Backbone, _, GenericModule, Dependon, Hardened, ServicesContainer) {

  var hiveOptions = [];
  var BeeHive = GenericModule.extend({

    initialize: function(attrs, options) {
      _.extend(this, _.pick(options, hiveOptions));
      this.Services = new ServicesContainer();
    },

    activate: function() {
      this.Services.activate(arguments);
    },

    close: function() {
      this.Services.close(arguments);
    },

    addService: function(name, service) {
      return this.Services.add(name, service);
    },

    removeService: function(name) {
      return this.Services.remove(name);
    },


    /*
     * Wraps itself into a Facade that can be shared with other modules
     * (it is read-only); absolutely non-modifiable and provides the
     * following callbacks and properties:
     *  - Services
     */
    hardenedInterface:  {
      Services: 'services container'
    }



  });

  _.extend(BeeHive.prototype, Hardened, {
    getHardenedInstance: function() {
      var iface = _.clone(this.hardenedInterface);
      if (this.Services.has('PubSub')) {

        var ctx = {
          key: this.Services.get('PubSub').getPubSubKey()
        };
        // purpose of these functions is to expose simplified
        // api directly at the root of beehive; the key is
        // is
        iface['publish'] = function() {
          var s = this.Services.get('PubSub');
          arguments = _.toArray(arguments); arguments.unshift(ctx.key);
          s.publish.apply(s, arguments);
        };
        iface['subscribe'] = function() {
          var s = this.Services.get('PubSub');
          arguments = _.toArray(arguments); arguments.unshift(ctx.key);
          s.subscribe.apply(s, arguments);
        };
        iface['unsubscribe'] = function() {
          var s = this.Services.get('PubSub');
          arguments = _.toArray(arguments); arguments.unshift(ctx.key);
          s.unsubscribe.apply(s, arguments);
        };
      }
      return this._getHardenedInstance(iface, this);

    }
  });

  return BeeHive;
});
