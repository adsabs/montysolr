/*
 * A simple wrapper around the API response for ADS. The underlying
 * implementation of the Response object can provide a specific
 * logic, so don't be surprised if you see a different behaviour.
 * But the API remains the same!
 *
 * To configure what implementation you want for your module, use this
 * in the app config:
 *
 * map: {
 *  'your/module': {
 *    'api_response_impl': 'js/components/specific_impl_of_the_api_response'
 *  }
 * },
 */

define([
  'underscore',
  'backbone',
  'js/components/solr_response',
  'js/components/facade'
], function (
  _,
  Backbone,
  ApiResponseImplementation,
  Facade
  ) {

  var hardenedInterface = {
    set: 'set (replace existing)',
    get: 'get values',
    has: 'has a key',
    toJSON: 'values back as JSON object',
    clone: 'make a copy',
    url: 'url string of the params',
    isLocked: true,
    lock: true,
    unlock: true,
    setApiQuery: 'sets the ApiQuery',
    getApiQuery: 'gets the query'
  };

  var ApiResponse = function (data, options) {

    // Facade pattern, we want to expose only limited API
    // despite the fact that the underlying instance has
    // all power of the Backbone.Model

    if (data instanceof ApiResponseImplementation) {
      this.innerResponse = new Facade(hardenedInterface, data);
    }
    else {
      this.innerResponse = new Facade(hardenedInterface, new ApiResponseImplementation(data, options));
    }
  };

  var toInsert = {};
  _.each(_.keys(hardenedInterface), function (element, index, list) {
    toInsert[element] = function () {
      return this.innerResponse[element].apply(this.innerResponse, arguments)
    };
  });
  _.extend(ApiResponse.prototype, toInsert, {
    clone: function () {
      var clone = this.innerResponse.clone.apply(this.innerResponse, arguments);
      return new ApiResponse(clone);
    }
  });

  return ApiResponse;


});
/**
 * Created by rchyla on 3/3/14.
 */
