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

  _.extend(BeeHive.prototype, Hardened);

  return BeeHive;
});
