({
  baseUrl: './src',
  //appDir: './',
  //mainConfigFile: './src/js/apps/discovery/main.js',
  generateSourceMaps: false,
  removeCombined: false,

  optimize: 'uglify',
  optimizeCss: 'standard',

  // Since we bootstrap with nested `require` calls this option allows
  // R.js to find them.
  findNestedDependencies: true,

  // Include a minimal AMD implementation shim.
  //name: '../libs/almond/almond',

  // Wrap everything in an IIFE.
  wrap: true,

  // Do not preserve any license comments when working with source
  // maps.  These options are incompatible.
  preserveLicenseComments: false,

  //dir: 'dist/',
  //cssIn: 'styles/css/styles.css',
  //out: 'syles/css/styles.optimized.css',

  //modules: [
  //  {name: 'js/components/application'}
  //],

  paths: {

    // bumblebee components (here we'll lists simple names), paths are relative
    // to the config (the module that bootstraps our application; look at the html)
    // as a convention, all modules should be loaded using 'symbolic' names
    'config': './discovery.config',
    'main': 'js/apps/discovery/main',
    'router': 'js/apps/discovery/router',


    // Almond is used to lighten the output filesize.
    "almond": "libs/almond/almond",

    // Opt for Lo-Dash Underscore compatibility build over Underscore.
    "underscore": "libs/lodash/lodash.compat",

    // 3rd party dependencies
    'jquery': 'empty:',
    'backbone': 'empty:',
    'hbs': 'libs/require-handlebars-plugin/hbs',
    'marionette': 'libs/marionette/backbone.marionette',
    'backbone.wreqr': 'libs/backbone.wreqr/lib/backbone.wreqr',
    'backbone.eventbinder': 'libs/backbone.eventbinder/backbone.eventbinder',
    'backbone.babysitter': 'libs/backbone.babysitter/backbone.babysitter',
    'bootstrap': 'libs/bootstrap/bootstrap',
    'jquery-ui': 'libs/jqueryui/jquery-ui',
    'd3': 'libs/d3/d3',
    'hoverIntent': 'libs/jquery-hoverIntent/jquery.hoverIntent',
    'cache': 'libs/dsjslib/lib/Cache',
    'jquery-querybuilder': 'libs/jQuery-QueryBuilder/query-builder',
    'd3-cloud': 'libs/d3-cloud/d3.layout.cloud',
    'nvd3': 'libs/nvd3/nv.d3',

    // only for testing (won't get loaded otherwise)
    'chai': '../bower_components/chai/chai',
    'sinon': '../bower_components/sinon/index'
  },

  map: {
    '*': {
      'api_query_impl': 'js/components/solr_params',
      'api_response_impl': 'js/components/solr_response',
      'api_request_impl': 'js/components/default_request',
      'pubsub_service_impl': 'js/services/default_pubsub'
    }
  },

  hbs : {
    'templateExtension' : 'html'

  },

  name: "js/apps/discovery/main",
  include: [

    "js/components/application",
    "js/wraps/author_facet"
  ],
  out: "dist/js/apps/discovery/main.js"

  })