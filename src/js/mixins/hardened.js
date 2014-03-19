/**
 * Created by rchyla on 3/18/14.
 */

define(['underscore', 'js/components/facade'], function(_, Facade) {
  var Mixin = {
    /*
     * Creates a hardened instance of itself, it uses
     * interface description from 'hardenedInterface'
     * Implementations need to populate 'hardenedInterface'
     * with list of properties and methods that should be exposed
     * through the Facade
     */
    _getHardenedInstance: function(iface, objectIn) {
      if (!('hardenedInterface' in this) && !iface) {
        throw Error("Error: this.hardenedInterface is not defined");
      }
      return new Facade(iface || ('hardenedInterface' in this ? this['hardenedInterface'] : {}), objectIn);
    },
    getHardenedInstance: function(iface) {
      return this._getHardenedInstance(iface, this);
    }
  };

  return Mixin;
});

