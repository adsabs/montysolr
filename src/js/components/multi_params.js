
define(['backbone', 'underscore', 'jquery'], function(Backbone, _, $) {


  var Model = Backbone.Model.extend({

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


    // adds values to existing (like set, but keeps the old vals)
    add: function(key, val, options) {
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

    // instead of url, we provide the complete url parameters that define
    // this query (by default, all params are used and sorted) but this
    // implementation doesn't do anything fancy with them
    url: function() {
      // sort keys alphabetically
      var sorted = _.pairs(this.attributes).sort(function(a,b) {return (a[0] > b[0]) ? 1 : (a[0] < b[0] ? -1 : 0)});
      // also sort values
      var s = {};
      sorted.map(function(item) { s[item[0]] = item[1].sort() });
      // use traditional encoding
      return $.param(s, true);
    },

    // re-construct the query from the url string, returns the json attributes
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
    }

  });

  return Model;
});
