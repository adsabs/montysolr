define([], function(){


var Utils = {};

// Helper method to extend an already existing method
  Utils.extendMethod = function(to, from, methodName) {

    // if the method is defined on from ...
    if (!_.isUndefined(from[methodName])) {
      var old = to[methodName];

      // ... we create a new function on to
      to[methodName] = function() {

        // wherein we first call the method which exists on `to`
        var oldReturn = old.apply(this, arguments);

        // and then call the method on `from`
        from[methodName].apply(this, arguments);

        // and then return the expected result,
        // i.e. what the method on `to` returns
        return oldReturn;

      };
    }

  };

return Utils

})
