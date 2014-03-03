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


define(['backbone', 'underscore', 'api_query_impl'], function(Backbone, _, ApiQueryImplementation) {



  var ApiQuery = function(innerQ) {
    var innerQuery = innerQ;
    var locked = false, _checkLock = function() {
      if (locked === true) {
        throw Error("Query locked for modifications");
      }
    };

    return {
      // This is our only extra method
      add: function(key, val, options) {
        _checkLock();
        innerQuery.add(key, val, options);
        return this;
      },
      set: function(key, val, options) {
        _checkLock();
        innerQuery.set(key, val, options);
        return this;
      },
      get: function(key) {
        return innerQuery.get(key);
      },
      url: function() {
        return innerQuery.url();
      },
      clone: function() {
        var q = new ApiQuery(new ApiQueryImplementation(innerQuery.toJSON()));
        if (this.isLocked()) {
          q.lock();
        }
        return q;
      },
      load: function(query) {
        _checkLock();
        var vals = innerQuery.parse(query);
        innerQuery.clear();
        innerQuery.set(vals);
        return this;
      },
      clear: function() {
        _checkLock();
        return innerQuery.clear();
      },
      has: function(key) {
        return innerQuery.has(key);
      },
      unset: function(key) {
        _checkLock();
        innerQuery.unset(key);
      },
      toJSON: function() {
        return innerQuery.toJSON();
      },
      isLocked: function() {
        return locked;
      },
      lock: function() {
        locked = true;
      },
      unlock: function() {
        locked = false;
      },
      pairs: function() {
        return innerQuery.pairs();
      },
      keys: function() {
        return innerQuery.keys();
      },
      values: function() {
        return innerQuery.values();
      },
      hasChanged: function(key) {
        return innerQuery.hasChanged(key);
      },
      previousAttributes: function() {
        return innerQuery.previousAttributes();
      },
      previous: function(attr) {
        return innerQuery.previous(attr);
      }

    };
  };



  // Facade pattern, we want to expose only limited API
  // despite the fact that the underlying instance has
  // all power of the Backbone.Model

  return function(data) {
    return new ApiQuery(new ApiQueryImplementation(data));
  }
});
