
define(['backbone', 'underscore', 'api_query'], function(Backbone, _, ApiQuery) {



  var SolrQuery = function(innerQ) {
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
        var q = new SolrQuery(new ApiQuery(innerQuery.toJSON()));
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
    return new SolrQuery(new ApiQuery(data));
  }
});
