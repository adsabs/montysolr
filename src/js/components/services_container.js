/**
 * Created by rchyla on 3/18/14.
 */

define(['js/components/facade', 'js/components/generic_module', 'js/mixins/hardened', 'underscore'], function(Facade, GenericModule, Hardened, _) {
  var Services = GenericModule.extend({
    initialize: function(attrs, options) {
      this._services = {};
    },

    close: function() {
      for (var service in this._services) {
        this.removeService(service);
      }
    },

    addService: function(name, service) {
      if (this._services.hasOwnProperty(name)) {
        throw new Error('The service: ' + name + ' is already registered, remove it first!');
      }
      if (!(name && service) || !_.isString(name)) {
        throw new Error('The key must be a string and the service is an object');
      }
      this._services[name] = service;
    },

    removeService: function(name, service) {
      if (this._services.hasOwnProperty(name)) {
        var s = this._services[name];
        s.close();
        delete this._services[name];
        return s;
      }
      return null;
    },

    hasService: function(name) {
      return this._services.hasOwnProperty(name);
    }

  });

  _.extend(Services.prototype, Hardened, {
    /*
     * A simple facade, we'll expose only services that
     * have 'getHardenedMethod' (ie. they know to protect
     * themselves)
     */
    getHardenedInstance: function() {
      var iface = {}, s;
      for (service in this._services) {
        s = this._services[service];
        if (_.isObject(s) && 'getHardenedInstance' in s) {
          iface[service] = true;
        }
      }
      return this._getHardenedInstance(iface, this._services);
    }
  });

  return Services;
});
