/*
 * A simple wrapper around the API response for ADS
 * This class is extended/enhanced by other implementations
 * (e.g. solr_response)
 */

define(['underscore', 'backbone'], function(_, Backbone) {

  var ApiResponse = function(attributes, options) {
    var defaults;
    var attrs = attributes || {};
    options || (options = {});
    this.cid = _.uniqueId('r');
    this.attributes = {};
    if (options.parse) attrs = this.parse(attrs, options) || {};
    if (defaults = _.result(this, 'defaults')) {
      attrs = _.defaults({}, attrs, defaults);
    }
    this.set(attrs, options);
    this.initialize.apply(this, arguments);
  };

  _.extend(ApiResponse.prototype, {
    // Initialize is an empty function by default. Override it with your own
    // initialization logic.
    initialize: function(){},

    // Return a copy of the model's `attributes` object.
    toJSON: function(options) {
      return _.clone(this.attributes);
    },

    set: function(key, val, options) {
      console.log(key, val, options);
    }

  });

  // use the bb extend function for classes hierarchy
  ApiResponse.extend = Backbone.Model.extend;

  return ApiResponse;

});
/**
 * Created by rchyla on 3/3/14.
 */
