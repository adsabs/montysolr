/**
 * Created by rchyla on 3/16/14. inspiration: http://jsfiddle.net/pajtai/GD5qR/35/
 */

/*
 * // Interface
 *  var remoteInterface = {
 *    on: 'turn on'
 *  };
 *  // Implementation
 *  var htmlRemote = {
 *    on: function() { console.log("remote on"); return this; }
 *  };
 *  // Protecting the implementation
 *  var htmlInterface = new Facade(remoteInterface, htmlRemote);
 *
 */
define(['underscore', 'js/components/facade'], function(_, Facade) {
  'use strict';

  // The Facade encapsulates objectIn according to the description
  // The exposed facade is guaranteed to have exactly the functions described in description.
  var Facade = function(description, objectIn) {

    var facade;

    // TODO: add enforce of "new"

    facade = {};

    this.mixIn(description, objectIn, facade);

    // TODO: check that "mixIn" is not taken
    facade.mixIn = this.mixIn;

    return facade;
  };

  Facade.prototype.mixIn = function(description, objectIn, facade) {
    var method;

    facade = facade || this;

    for (method in description) {
      if (description.hasOwnProperty(method)) {
        if (! objectIn[method]) {
          throw new Error(methodName + " not imlemented for this facade");
        }

        var p = objectIn[method];

        if (typeof p == 'function') {
          // Must be a function - bind is needed to enable use of methods other than those on the interface
          facade[method] = _.bind(p, objectIn);
        }
        else if (p.hasOwnProperty('__facade__') && p.__facade__) {
          facade[method] = p;
        }
        else if (_.isUndefined(p)) {
          //pass
        }
        else if (_.isString(p) || _.isNumber(p) || _.isBoolean(p) || _.isDate(p) || _.isNull(p) || _.isRegExp(p)) {
          facade['get' + method.substring(0,1).toUpperCase() + method.substring(1)] = _.bind(function() {return this.ctx[this.name]}, {ctx:objectIn, name:method});
        }
        else {
          throw new Error("Sorry, you can't wrap '" + method + "': " + p);
        }

      }
    }

    facade.__facade__ = true;
    return facade;
  }

  return Facade;
});




