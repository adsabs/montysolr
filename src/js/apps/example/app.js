// app.js is the main backbone application
// representing the logic


console.log('loading: app.js');


define(function(require, exports, module) {
  "use strict";

  // External dependencies.
  var _ = require("underscore");
  var $ = require("jquery");
  var Backbone = require("backbone");
  
  // Alias the module for easier identification.
  var app = module.exports;

  // The root path to run the application through.
  app.root = "/js/apps/example";
  
  var hello = require('js/modules/hello');
  
  console.log('App is running!');
  
  app.hello = function(name) {
      hello.showName('#example-main', name);
    };
  
  app.hello('world!');
  
});
