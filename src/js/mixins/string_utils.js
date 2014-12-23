define([],
  function () {

    function format() {
      var args = arguments;
      return args[0].replace(/\{\{|\}\}|\{(\d+)\}/g, function (m, n) {
        if (m == "{{") {
          return "{";
        }
        if (m == "}}") {
          return "}";
        }
        return args[parseInt(n) + 1];
      });
    }

  var Utils = {
    format: format
  };

  return Utils;

});
