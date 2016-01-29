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

        tempVal = _.without(_.flatten(tempVal), "", false, null, undefined, NaN);

        if (!_.isArray(tempVal)) {
          throw new Error('Values were not converted to an Array');
        }

        if (_.isEmpty(tempVal) && options.unset !== true) {
          throw new Error('Empty values not allowed');
        }

        if (!(_.every(tempVal, function(v) {
          return _.isString(v) || (_.isNumber(v) && !_.isNaN(v))}))) {
          throw new Error('Invalid value (not a string or number): ' + tempVal);
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
          attrs[attr] = _.flatten([tempVal]);
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
          tempVal = _.flatten([tempVal]);
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

      // June1:rca - I need to preserve order of values (becuaes of the query modifications/updates) the logic
      // just requires us to be careful and we need order to be preserved when the query is cloned

      // also sort values
      // var s = {};
      // sorted.map(function(item) { s[item[0]] = (_.isArray(item[1]) ? item[1].sort() : item[1]) });

      // we have to double encode certain elements
      //sorted = _.map(sorted, function(pair) { return [pair[0], _.map(pair[1], function(v) {return (v.indexOf && v.indexOf('=') > -1) ? encodeURIComponent(v) : v })]});

      // use traditional encoding
      //this is going to be used for a url, not a form --> use %20 instead of +
      var encoded =  $.param(_.object(sorted), true);
          encoded = encoded.replace("+", "%20");

      return encoded;
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
        resp = decodeURI(resp);
        if (resp.indexOf('?') > -1) {
          attrs['#path'] = [resp.slice(0, resp.indexOf('?'))];
          resp = resp.slice(resp.indexOf('?')+1);
        }
        if (resp.indexOf('#') > -1) {
          attrs['#hash'] = this._parse(resp.slice(resp.indexOf('#')+1));
          resp = resp.slice(0, resp.indexOf('#'));
        }
        attrs['#query'] = this._parse(resp);

        return this._checkParsed(attrs);
      }
      return this._checkParsed(resp); // else return resp object
    },

    _parse: function(resp) {
      var attrs = {}, hash;
      if (!resp.trim()) {
        return attrs;
      }
      var hashes = resp.slice(resp.indexOf('?') + 1).split('&');

      //resp = decodeURIComponent(resp);
      var key,value;
      for (var i = 0; i < hashes.length; i++) {
        hash = hashes[i].split('=');
        key = decodeURIComponent(hash[0].split('+').join(' ')); // optimized: .replace(/\+/g, " ")

        var vall = hash[1];
        if (hash.length > 2) {
          hash.shift()
          vall = hash.join('=');
        }

        value = decodeURIComponent(vall.split('+').join(' '));
        if (attrs[key] !== undefined) {
          attrs[key].push(value);
        }
        else {
          attrs[key] = [ value ];
        }
      }
      return attrs;
    },

    // default behaviour is just to keep the query parameters
    // after the string was parsed, you can override it to suit other needs
    _checkParsed: function(attrs) {
      if (_.isObject(attrs)) {
        if ('#query' in attrs) {
          return attrs['#query'];
        }
      }
      return attrs;
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
