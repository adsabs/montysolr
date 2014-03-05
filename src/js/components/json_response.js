/*
 * A simple wrapper around the API response for ADS
 * This class is extended/enhanced by other implementations
 * (e.g. solr_response)
 */

define(['underscore', 'backbone'], function(_, Backbone) {

  var JSONResponse = function(attributes, options) {
    var defaults;
    var attrs = attributes || {};
    options || (options = {});
    this.rid = _.uniqueId('r');
    this.readOnly = options.hasOwnProperty('readOnly') ? options.readOnly : true;
    if (options.parse) attrs = this.parse(attrs, options) || {};
    if (defaults = _.result(this, 'defaults')) {
      attrs = _.defaults({}, attrs, defaults);
    }

    this.attributes = attrs;

    this.initialize.apply(this, arguments);
  };

  _.extend(JSONResponse.prototype, {
    // Initialize is an empty function by default. Override it with your own
    // initialization logic.
    initialize: function(){},

    // Return a copy of the model's `attributes` object.
    toJSON: function(options) {
      return this._clone(this.attributes);
    },

    set: function(key, val, options) {
      if (this.readOnly) {
        throw Error("You can't change read-only response object");
      }
      var parts = this._split(key);
      if (parts.length == 1) {
        this.attributes[parts[0]] = val;
      }
      else {
        var pointer = this.get(key);
        pointer = val;
      }
    },

    _split: function(key) {
      var parts = [];
      var i = 0, l=key.length, start= 0, quotes = [];
      while (i < l) {
        if (key[i] == quotes[quotes.length -1]) {
          quotes.pop();
        }
        else if (key[i] == '"' || key[i] == "'") {
          quotes.push(key[i]);
        }
        else if (key[i] == '.' && quotes.length == 0) {
          parts.push(key.substring(start, i));
          start = i+1;
        }
        else if (key[i] == '[' && quotes.length == 0) {
          parts.push(key.substring(start, i));
          parts.push(key.substring(i, key.indexOf(']', i+1)+1));
          start = i = key.indexOf(']', i+1)+1;
        }
        i += 1;
      }
      if (start < l) {
        parts.push(key.substring(start));
      }
      //console.log(key, parts);
      return parts;
    },

    get: function(key, lastElem) {
      // if key empty, return everything
      if (!key) {
        return this._clone(this.attributes);
      }
      var parts = this._split(key), found = [],
        pointer = this.attributes;

      while (parts.length > 0) {
        var k = parts.shift();
        if (pointer.hasOwnProperty(k)) {
          pointer = pointer[k];
          found.push(k);
        }
        else if (k.indexOf('[') > -1) { // foo['something'] or foo[0]

          var m = k.trim().substring(1, k.length - 1);
          if ((m.indexOf('"') > -1 || m.indexOf("'") > -1) && pointer.hasOwnProperty(m.substring(1, m.length - 1))) { // object property access

            pointer = pointer[m.substring(1, m.length - 1)];
            found.push(m);
          }
          else if (_.isArray(pointer)) {
            var ix = null;
            try {
              ix = parseInt(m);
              if (_.isNaN(ix) || pointer.length <= ix || ix < 0) {
                throw new Error();
              }
              pointer = pointer[ix];
              found.push(m);
            }
            catch (e) {
              throw new Error("Can't find: " + key + (found.length > 0 ? " (worked up to: " + found.join('.') + ")" : ""));
            }

          }
          else {
            throw new Error("Can't find: " + key + (found.length > 0 ? " (worked up to: " + found.join('.') + ")" : ""));
          }
        }
        else {
          throw new Error("Can't find: " + key + (found.length > 0 ? " (worked up to: " + found.join('.') + ")" : ""));
        }
      }


      return this._clone(pointer);

    },

    clone: function() {
      return new this.constructor(this.attributes);
    },


    // creates a copy of the requested elements
    _clone: function(elem) {
      if (!this.readOnly || !_.isObject(elem)) {
        return elem;
      }
      
      if (_.cloneDeep) { // lodash
        return _.cloneDeep(elem);
      }
      else {
        return JSON.parse(JSON.stringify(elem));
      }
    },

    isReadOnly: function() {
      return this.readOnly;
    }

  });

  // use the bb extend function for classes hierarchy
  JSONResponse.extend = Backbone.Model.extend;

  return JSONResponse;

});
/**
 * Created by rchyla on 3/3/14.
 */
