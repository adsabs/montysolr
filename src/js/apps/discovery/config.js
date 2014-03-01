// Main config file for the Discovery application; as a convention we'll
// list all libraries/modules/components, so that inside our code
// we don't need to care for their structure


// Path to the root of the project (to be used only for tests!)
var rootDir = '../../../../';

// Path to the src/ from here
var codeDir = '../../../';

require.config({



  // Initialize the application with the main application file or if we run
  // as a test, then load the test unittests
  deps: window.mocha ? [ rootDir + 'test/mocha/discovery.spec' ] : [ 'main' ],

  paths: {

    // bumblebee components
    'api_query': codeDir + 'js/components/api_query',

    // Almond is used to lighten the output filesize.
    "almond": codeDir + "libs/almond/almond",

    // Opt for Lo-Dash Underscore compatibility build over Underscore.
    "underscore": codeDir + "libs/lodash/lodash.compat",

    // 3rd party dependencies
    'jquery': codeDir + 'libs/jquery/jquery',
    'backbone': codeDir + 'libs/backbone/backbone'
  },

  shim: {
    // This is required to ensure Backbone works as expected within the AMD
    // environment.
    'backbone': {
      // These are the two hard dependencies that will be loaded first.
      deps: ['jquery', 'underscore'],

      // This maps the global `Backbone` object to `require('backbone')`.
      exports: 'Backbone'
    }
  }
});
