/*
Example module that simply prints 'hello x'
as a main page
*/

define(['underscore', 'jquery'], function(_, $) {
  var showName = function(selector, n) {
    console.log(selector);
    console.log(n);
    var temp = _.template("Hello <%= name %>");
    $(selector).html(temp({name: n}));
  };
  return {
    showName: showName
  };
});
