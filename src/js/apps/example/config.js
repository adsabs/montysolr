// Application specific config (for require.js) it is also used
// by Grunt when we build the app from command line.

console.log('loading config');

require.config({
  paths: {
    // it is a good practice to set paths in the config, because
    // we can swap/change location of the libraries for this application
    // without changing the code. You can then say: require('foo')
    // and the library 'foo' will be loaded for you by require.js
    
    // generic paths
    "libs": "../../../libs",
    "js": "../../../js",

    // Almond is used to lighten the output filesize.
    "almond": "../../../libs/almond/almond",

    // We are using Lo-Dash instead of Underscore (tests indicate it is much
    // faster; for some frequent operations, such as list recognition, it is
    // significant)
    "underscore": "../../../libs/lodash/lodash.compat",

    // Other dependencies of our application
    "jquery": "../../../libs/jquery/jquery",
    "backbone": "../../../libs/backbone/backbone"
  },

  shim: {
    // This is required to ensure Backbone works as expected within the AMD
    // environment.
    "backbone": {
      // These are the two hard dependencies that will be loaded first.
      deps: ["jquery", "underscore"],

      // This maps the global `Backbone` object to `require("backbone")`.
      exports: "Backbone"
    }
  }
});

console.log(require.config);
