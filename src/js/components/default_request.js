/**
 * Created by rchyla on 3/28/14.
 */

define([
    'underscore',
    'backbone',
    'js/components/api_query',
    'js/components/multi_params'
  ],
  function(
    _,
    Backbone,
    ApiQuery,
    MultiParams
    ) {

    var basicCheck = function(s) {
      if (_.isString(s)) {
        return true;
      }
      if (_.isArray(s)) {
        var l = s.length;
        for (var i=0; i<l; i++) {
          var x = s[i];
          if (!(_.isString(x) || _.isNumber(x))) {
            return false;
          }
        }
      }
      return true;
    };
    var allowedAttrs = {
      query: function(v) {return v instanceof ApiQuery},
      target: basicCheck,
      sender: basicCheck,
      options : basicCheck
    };

    var checker = {
      target: function(s) {
        if (s && s.substring(0,1) !== '/') {
          return '/' + s;
        }
      }
    };

    var Request = MultiParams.extend({
      /**
       * Internal method: we allow only certain keys
       *
       * @param attributes
       * @param options
       * @returns {boolean}
       * @private
       */
      _validate: function(attributes, options) {
        for (attr in attributes) {

          var tempVal = attributes[attr];

          if (!(attr in allowedAttrs)) {
            throw new Error('Invalid attr: '+ attr);
          }

          if (!allowedAttrs[attr].call(allowedAttrs, tempVal)) {
            throw new Error('Invalid value:key ' + attr  + tempVal);
          }

          //if (attr in checker) {
          //  attributes[attr] = checker[attr].call(checker, tempVal);
          //}
        }
        return true;
      },

      /**
       * Modified version of the multi-valued set(); we do not insist
       * on having the values in array
       *
       * @param key
       * @param val
       * @param options
       * @returns {Request}
       */
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

        Backbone.Model.prototype.set.call(this, attrs, options);
      },

      // for requests, we use all components: path, query, hash
      _checkParsed: function(attrs) {
        if (_.isObject(attrs)) {
          var ret = {};
          if ('#query' in attrs && !_.isEmpty(attrs['#query'])) {
            ret['query'] = new ApiQuery(attrs['#query']);
          }
          if ('#path' in attrs) {
            ret['target'] = attrs['#path'][0];
          }
          if ('#hash' in attrs) {
            _.extend(ret, _.each(attrs['#hash'], function(val, key, obj) {if (val.length == 1) {obj[key] = val[0]}}));
          }
          return ret;
        }
        return attrs;
      },

      /*
       * Return the url string encoding all parameters that made
       * this request. The parameters will be sorted alphabetically
       * by their keys and URL encoded so that they can be used
       * in requests.
       */
      url: function(whatToSort) {
        if (!whatToSort) {
          whatToSort = this.attributes;
        }

        var target = whatToSort['target'];

        var url = target ? (_.isArray(target) ? target.join('/') : target) : '';
        if ('query' in whatToSort) {
          url += '?' + whatToSort['query'].url();
        }
        if ('sender' in whatToSort) {
          url += '#' + MultiParams.prototype.url.call(this, {'sender': whatToSort['sender']});
        }
        return url;
      },

      /**
       * Re-constructs the query from the url string, returns the json attributes;
       * cannot be used it the instance is locked
       *
       * @param query (String)
       * @returns {Model}
       */
      load: function(query) {
        return MultiParams.prototype.load.call(this, query.indexOf('?') > -1 ? query : query + '?');
      }
    });


    return Request;
  });
