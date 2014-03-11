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

define(['underscore', 'backbone', 'api_response_impl'], function(_, Backbone, ApiResponseImplementation) {

  var ApiResponse = function(data) {
    var innerResponse = data;

    return {
      has: function(key) {
        return innerResponse.has(key);
      },
      get: function(key) {
        return innerResponse.get(key);
      },
      set: function(key, val, options) {
        return innerResponse.set(key, val, options);
      },
      clone: function() {
        return new ApiResponse(innerResponse.clone());
      },
      toJSON: function() {
        return innerResponse.toJSON();
      },
      isLocked: function() {
        return innerResponse.isLocked();
      },
      lock: function() {
        return innerResponse.lock();
      },
      unlock: function() {
        return innerResponse.unlock();
      },
      url: function() {
        return innerResponse.url();
      }

    };
  };



  // Facade pattern, we want to expose only limited API
  // despite the fact that the underlying instance has
  // all power of the Backbone.Model

  return function(data, options) {
    return new ApiResponse(new ApiResponseImplementation(data, options));
  }

});
/**
 * Created by rchyla on 3/3/14.
 */
