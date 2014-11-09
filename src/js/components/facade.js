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
    var property, propertyValue;

    facade = facade || this;

    for (property in description) {

      propertyValue = description[property];

      if (property in objectIn) {

        var p = objectIn[property];

        if (typeof propertyValue == 'function') {  // redefining the method
          facade[property] = _.bind(propertyValue, objectIn);
        }
        else if (typeof p == 'function') { // exposing the method
          facade[property] = _.bind(p, objectIn);
        }
        else if (_.isUndefined(p)) {
          //pass
        }
        else if (_.isString(p) || _.isNumber(p) || _.isBoolean(p) || _.isDate(p) || _.isNull(p) || _.isRegExp(p)) { // build getter method
          facade['get' + property.substring(0,1).toUpperCase() + property.substring(1)] = _.bind(function() {return this.ctx[this.name]}, {ctx:objectIn, name:property});
        }
        else if (p.hasOwnProperty('__facade__') && p.__facade__) { // exposing internal facade
          facade[property] = p;
        }
        else if (_.isObject(p) && 'getHardenedInstance' in p) { // builds a facade
          facade[property] = p.getHardenedInstance();
        }
        else {
          throw new Error("Sorry, you can't wrap '" + property + "': " + p);
        }

      }
      else {
        if (typeof propertyValue == 'function') {
          facade[property] = _.bind(propertyValue, objectIn);
        }
        else {
          throw new Error("Unknown key: " + property + "(" + propertyValue + ")");
        }
      }
    }

    if (objectIn) {
      // .name is not supported in IE
      // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Function/name
      facade.__facade__ = objectIn.constructor ? (objectIn.constructor.name ? objectIn.constructor.name : true) : true;
    }
    else {
      facade.__facade__ = true;
    }
    return facade;
  };

  return Facade;
});




