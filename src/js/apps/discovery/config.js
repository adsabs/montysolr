// This is the runtime configuration file.  It complements the Gruntfile.js by
// supplementing shared properties.
console.log('loading config');

require.config({
  paths: {
    // Make vendor easier to access.
    "libs": "../../../libs",
    "js": "../../../js",

    // Almond is used to lighten the output filesize.
    "almond": "../../../libs/almond/almond",

    // Opt for Lo-Dash Underscore compatibility build over Underscore.
    "underscore": "../../../libs/lodash/lodash.compat",

    // Map remaining vendor dependencies.
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
