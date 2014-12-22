//BC:rca - not used anywwhere, to remove?
// besides, you could use underscore: http://underscorejs.org/#chaining
define([], function() {
  var Mixin = {
    flatMap: function(callback) {
      return [].concat.apply([], this.map(callback));
    }
  };

  return Mixin;
});
