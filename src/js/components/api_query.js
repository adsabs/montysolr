/*
 A facade: api query exposing only the set of functions that we allow. This is
 the module that you want to load in the application (do not load the concrete
 implementaions, such as solr_params !)

 Put in your config:
 map: {
 'your/module': {
 'api_query_impl': 'js/components/specific_impl_of_the_api_query'
 }
 },

 */


define([
  'backbone',
  'underscore',
  'js/components/solr_params',
  'js/components/facade'
], function (
  Backbone,
  _,
  ApiQueryImplementation,
  Facade
  ) {


  var hardenedInterface = {
    add: 'add values',
    set: 'set (replace existing)',
    get: 'get values',
    has: 'has a key',
    url: 'url string of the params',
    load: 'loads query as a string',
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

  var ApiQuery = function (data, options) {

    // Facade pattern, we want to expose only limited API
    // despite the fact that the underlying instance has
    // all power of the Backbone.Model

    if (data instanceof ApiQueryImplementation) {
      this.innerQuery = new Facade(hardenedInterface, data);
    }
    else {
      this.innerQuery = new Facade(hardenedInterface, new ApiQueryImplementation(data, options));
    }
  };

  var toInsert = {};
  _.each(_.keys(hardenedInterface), function (element, index, list) {
    toInsert[element] = function () {
      return this.innerQuery[element].apply(this.innerQuery, arguments)
    };
  });
  _.extend(ApiQuery.prototype, toInsert, {
    clone: function () {
      var clone = this.innerQuery.clone.apply(this.innerQuery, arguments);
      return new ApiQuery(clone);
    },
    load: function () {
      var clone = this.innerQuery.load.apply(this.innerQuery, arguments);
      return new ApiQuery(clone);
    }
  });

  return ApiQuery;


});
