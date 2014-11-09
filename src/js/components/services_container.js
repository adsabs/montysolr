/**
 * Created by rchyla on 3/18/14.
 */

define(['js/components/facade', 'js/components/generic_module', 'js/mixins/hardened', 'underscore'], function(Facade, GenericModule, Hardened, _) {
  var Services = GenericModule.extend({
    initialize: function(options) {
      this._services = _.has(options, 'services') ? options['services'] : {};
    },

    activate: function() {
      var args = arguments;
      _.each(_.values(this._services), function(service) {// _.keys() preserves access order
        if ('activate' in service) {
          service.activate.apply(service, args);
        }
      });
    },

    close: function() {
      for (var service in this._services) {
        this.remove(service);
      }
    },

    add: function(name, service) {
      if (this._services.hasOwnProperty(name)) {
        throw new Error('The service: ' + name + ' is already registered, remove it first!');
      }
      if (!(name && service) || !_.isString(name)) {
        throw new Error('The key must be a string and the service is an object');
      }
      this._services[name] = service;
    },

    remove: function(name, service) {
      if (this._services.hasOwnProperty(name)) {
        var s = this._services[name];
        if ('close' in s) {
          s.close();
        }
        delete this._services[name];
        return s;
      }
      return null;
    },

    has: function(name) {
      return this._services.hasOwnProperty(name);
    },

    get: function(name) {
      return this._services[name];
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
      var newContainer = new this.constructor({'services': this._getHardenedInstance(iface, this._services)});
      return this._getHardenedInstance({'get':true, 'has':true}, newContainer);

    }
  });

  return Services;
});
