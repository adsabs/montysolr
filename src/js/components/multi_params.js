/*
 multi_params is a generic class to store any parameters;
 it is backed by BB.Model and has all the functionality
 the values are always stored as an array of values; so
 even if you try to set strings, you will always have
 list of strings
 */
define(['backbone', 'underscore', 'jquery'], function(Backbone, _, $) {


  var Model = Backbone.Model.extend({
    locked: false,
    _checkLock: function() {
      if (this.locked === true) {
        throw Error("Object locked for modifications");
      }
    },
    isLocked: function() {
      return this.locked;
    },
    lock: function() {
      this.locked = true;
    },
    unlock: function() {
      this.locked = false;
    },
    clone: function() {
      if (this.isLocked()) {
        var c = new this.constructor(this.attributes);
        c.lock();
        return c;
      }
      return new this.constructor(this.attributes);
    },

    // we allow only strings and numbers; instead of sending
    // signal we throw a direct error
    _validate: function(attributes, options) {
      // check we have only numbers and/or finite numbers
      for (attr in attributes) {
        if (!_.isString(attr)) {
          throw new Error('Keys must be strings, not: ' + attr);
        }
        // remove empty strings
        var tempVal = attributes[attr];

        if (!_.isArray(tempVal)) {
          throw new Error('Values were not converted to an Array');
        }

        tempVal =_.compact(tempVal);
        if (_.isEmpty(tempVal) && options.unset !== true) {
          throw new Error('Empty values not allowed');
        }

        if (!(_.every(tempVal, function(v) {
          return _.isString(v) || (_.isNumber(v) && !_.isNaN(v))}))) {
          throw new Error('Invalid value: ' + tempVal);
        }

        attributes[attr] = tempVal;
      }
      return true;
    },


    // Every value is going to be multi-valued by default
    // in this way we can treat all objects in the same way
    set: function(key, val, options) {
      this._checkLock();
      var attrs;

      if (key == null) return this;

      // Handle both `"key", value` and `{key: value}` -style arguments.
      if (typeof key === 'object') {
        attrs = key;
        options = val;
      } else {
        (attrs = {})[key] = val;
      }

      for (attr in attrs) {
        var tempVal = attrs[attr];

        // convert to array if necessary
        if (!(_.isArray(tempVal))) {
          attrs[attr] = _.compact([tempVal]);
        }
      }

      Backbone.Model.prototype.set.call(this, attrs, options);
    },

    unset: function() {
      this._checkLock();
      Backbone.Model.prototype.unset.apply(this, arguments);
    },


    // adds values to existing (like set, but keeps the old vals)
    add: function(key, val, options) {
      this._checkLock();
      var attrs;

      if (key == null) return this;

      // Handle both `"key", value` and `{key: value}` -style arguments.
      if (typeof key === 'object') {
        attrs = key;
        options = val;
      } else {
        (attrs = {})[key] = val;
      }

      for (attr in attrs) {
        var tempVal = attrs[attr];

        // convert to array if necessary
        if (!(_.isArray(tempVal))) {
          tempVal = _.compact([tempVal]);
        }
        if (this.has(attr)) {
          tempVal = _.clone(this.get(attr)).concat(tempVal);
        }

        attrs[attr] = tempVal;
      }

      Backbone.Model.prototype.set.call(this, attrs, options);
    },

    // synchronization is disabled
    sync: function() {
      throw Error("MultiParams cannot be saved to server");
    },

    /*
     * Return the url string encoding all parameters that made
     * this query. The parameters will be sorted alphabetically
     * by their keys and URL encoded so that they can be used
     * in requests.
     */
    url: function(whatToSort) {
      if (!whatToSort) {
        whatToSort = this.attributes;
      }
      // sort keys alphabetically
      var sorted = _.pairs(whatToSort).sort(function(a,b) {return (a[0] > b[0]) ? 1 : (a[0] < b[0] ? -1 : 0)});
      // also sort values
      var s = {};
      sorted.map(function(item) { s[item[0]] = item[1].sort() });
      // use traditional encoding
      return $.param(s, true);
    },

    /**
     * Parses string (urlparams) and returns it as an object
     * @param resp
     * @param options
     * @returns {*}
     */
    parse: function(resp, options) {
      if (_.isString(resp)) {
        var attrs  = {};
        resp = decodeURIComponent(resp);
        var hashes = resp.slice(resp.indexOf('?') + 1).split('&');
        for (var i = 0; i < hashes.length; i++) {
          hash = hashes[i].split('=');
          if (attrs[hash[0]] !== undefined) {
            attrs[hash[0]].push(hash[1]);
          }
          else {
            attrs[hash[0]] = [ hash[1] ];
          }
        }
        return attrs;
      }
      return resp; // else return resp object
    },

    /**
     * Re-constructs the query from the url string, returns the json attributes;
     * cannot be used it the instance is locked
     *
     * @param query (String)
     * @returns {Model}
     */
    load: function(query) {
      this._checkLock();
      var vals = this.parse(query);
      this.clear();
      this.set(vals);
      return this;
    }

  });


  return Model;
});
