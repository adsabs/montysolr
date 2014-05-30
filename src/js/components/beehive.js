/**
 * Created by rchyla on 3/16/14.
 */

define(['backbone', 'underscore',
  'js/components/generic_module', 'js/mixins/dependon', 'js/mixins/hardened', 'js/components/services_container'],
  function(Backbone, _, GenericModule, Dependon, Hardened, ServicesContainer) {

  var hiveOptions = [];
  var BeeHive = GenericModule.extend({
    initialize: function(options) {
      _.extend(this, _.pick(options, hiveOptions));
      this.Services = new ServicesContainer();
      this.Objects = new ServicesContainer();
    },

    activate: function() {
      this.Services.activate(arguments);
      this.Objects.activate(this);
    },

    close: function() {
      this.Services.close(arguments);
      this.Objects.close(arguments);
    },

    getService: function(name) {
      return this.Services.get(name);
    },

    hasService: function(name) {
      return this.Services.has(name);
    },

    addService: function(name, service) {
      return this.Services.add(name, service);
    },

    removeService: function(name) {
      return this.Services.remove(name);
    },

    getObject: function(name) {
      return this.Objects.get(name);
    },

    hasObject: function(name) {
      return this.Objects.has(name);
    },

    addObject: function(name, service) {
      return this.Objects.add(name, service);
    },

    removeObject: function(name) {
      return this.Objects.remove(name);
    },


    /*
     * Wraps itself into a Facade that can be shared with other modules
     * (it is read-only); absolutely non-modifiable and provides the
     * following callbacks and properties:
     *  - Services
     */
    hardenedInterface:  {
      Services: 'services container',
      Objects: 'objects container'
    }



  });

  _.extend(BeeHive.prototype, Hardened);

  return BeeHive;
});
