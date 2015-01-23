/**
 * Created by rchyla on 3/28/14.
 */

define([
    'underscore', 'backbone',
    'js/components/facade',
    'js/components/default_request'
  ],
  function(
    _,
    Backbone,
    Facade,
    ApiRequestImpl
    ) {

    var hardenedInterface =  {
      // add makes no sense with request
      get: 'get a key',
      set: 'set (replace existing)',
      url: 'url string defining this request',
      has: 'has a key',
      load: 'loads request as a string',
      clear: 'clears all values',
      unset: 'removes a key',
      toJSON: 'values back as JSON object',
      clone: 'make a copy',
      isLocked: true,
      lock: true,
      unlock: true,
      pairs: 'get all values as pairs',
      keys: 'as keys',
      values: 'only values',
      hasChanged: 'whether this object has modification (since its creation)',
      previousAttributes: 'get all changed attributes',
      previous: 'previous values for a given attribute'
    };

    var ApiRequest = function(data, options) {

      // Facade pattern, we want to expose only limited API
      // despite the fact that the underlying instance has
      // all power of the Backbone.Model

      if (data instanceof ApiRequestImpl) {
        this.innerRequest = new Facade(hardenedInterface, data);
      }
      else {
        this.innerRequest = new Facade(hardenedInterface, new ApiRequestImpl(data, options));
      }
    };

    var toInsert = {};
    _.each(_.keys(hardenedInterface), function(element, index, list) {
      toInsert[element] = function() {return this.innerRequest[element].apply(this.innerRequest, arguments)};
    });
    _.extend(ApiRequest.prototype, toInsert, {
      clone: function() {
        var clone = this.innerRequest.clone.apply(this.innerRequest, arguments);
        return new ApiRequest(clone);
      },
      load: function() {
        var clone = this.innerRequest.load.apply(this.innerRequest, arguments);
        return new ApiRequest(clone);
      }
    });

    return ApiRequest;

  });
