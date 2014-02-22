
console.log('loading: routerj.js');

define(function(require, exports, module) {
  "use strict";

  // External dependencies.
  var Backbone = require("backbone");
  var app = require("app");
  
  // Defining the application router.
  module.exports = Backbone.Router.extend({
    routes: {
      "": "index",
      "hello": "hello"
    },

    index: function() {
      console.log("Welcome to your / route.");
    },
    
    hello: function() {
      app.hello('world!');
    }
  });
});