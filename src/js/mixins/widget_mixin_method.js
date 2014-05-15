define(['js/mixins/widget_utility'], function (Utils) {


  var mixin = function (from, methods) {
    var to = this.prototype;
    // we add those methods which exists on `from` but not on `to` to the latter
    _.extend(from, to);
    // … and we do the same for events
    _.extend(from.events, to.events);

    _.each(Array.prototype.slice.call(arguments, 1), function (m) {
      Utils.extendMethod(to, from, m)
    })
  };

  return mixin

})

