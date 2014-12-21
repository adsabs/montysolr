define([], function() {
  var Mixin = {
    flatMap: function(callback) {
      return [].concat.apply([], this.map(callback));
    }
  };

  return Mixin;
});
