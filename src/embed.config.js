// Main config file for the BumbleBox application
require.config({

  // Initialize the application with the main application file or if we run
  // as a test, then load the test unittests
  deps: (function(){
    if (typeof window !== "undefined" && window.bbbTest){
      return [window.bbbTest.testLoader ? window.bbbTest.testLoader : '../test/test-loader'];
    }
    else {
      return ['js/apps/bumblebox/main'];
    }
  }()),

  waitSeconds: 30,

  // Configuration we want to make available to modules of ths application
  // see: http://requirejs.org/docs/api.html#config-moduleconfig
  config: {
    'js/components/persistent_storage': {
      // the unique namespace under which the local storage will be created
      // so every new instance of the storage will be saving its data into
      // <namespace>[other-name]
      namespace: 'bumblebox'
    },

    'js/apps/bumblebox/main': {
      core: {
        controllers: {
          FeedbackMediator: 'js/components/feedback_mediator',
          QueryMediator: 'js/components/query_mediator',
          Diagnostics: 'js/bugutils/diagnostics',
          AlertsController: 'js/components/alerts_mediator'
        },
        services: {
          Api: 'js/services/api',
          PubSub: 'js/services/pubsub',
          Navigator: 'js/apps/bumblebox/navigator',
          PersistentStorage: 'js/services/storage'
        },
        objects: {
          DynamicConfig: 'embed.vars',
          MasterPageManager: 'js/page_managers/master',
          AppStorage: 'js/components/app_storage'
        },
        modules: {
          FacetFactory: 'js/widgets/facet/factory'
        }
      },
      widgets: {
        AlertsWidget: 'js/widgets/alerts/widget',
        PageManager: 'js/apps/bumblebox/page_manager'
      },
      plugins: {}
    }
  },

  // Configuration for the facades (you can pick specific implementation, just for your
  // application) see http://requirejs.org/docs/api.html#config-map
  map: {
    '*': {
      'pubsub_service_impl': 'js/services/default_pubsub',
      'analytics_config': 'embed.vars',
      'dynamic_config': 'embed.vars'
    }
  },

  paths: {

    // bumblebee components (here we'll lists simple names), paths are relative
    // to the config (the module that bootstraps our application; look at the html)
    // as a convention, all modules should be loaded using 'symbolic' names
    'config': 'js/apps/bumblebox/config',
    'main': 'js/apps/bumblebox/main',
    'router': 'js/apps/bumblebox/router',
    'bootstrap': 'js/apps/bumblebox/bootstrap',

    // the rest is libraries that are provided by BBB (and if you use BBB components
    // they can import js libraries using symbolic names)
    'analytics': 'js/components/analytics',
    'underscore': 'libs/lodash/lodash.compat',
    'jquery': 'libs/jquery/jquery',
    'backbone': 'libs/backbone/backbone',
    'hbs': 'libs/require-handlebars-plugin/hbs',
    'async': 'libs/requirejs-plugins/async',
    'marionette': 'libs/marionette/backbone.marionette',
    'backbone.wreqr': 'libs/backbone.wreqr/lib/backbone.wreqr',
    'backbone.eventbinder': 'libs/backbone.eventbinder/backbone.eventbinder',
    'backbone.babysitter': 'libs/backbone.babysitter/backbone.babysitter',
    'bootstrap': 'libs/bootstrap/bootstrap',
    'jquery-ui': 'libs/jqueryui/jquery-ui',
    'd3': 'libs/d3/d3',
    'd3-cloud': 'libs/d3-cloud/d3.layout.cloud',
    'hoverIntent': 'libs/jquery-hoverIntent/jquery.hoverIntent',
    'cache': 'libs/dsjslib/lib/Cache',
    'jquery-querybuilder': 'libs/jQuery-QueryBuilder/query-builder',
    'filesaver': 'libs/FileSaver/FileSaver',
    'select2': 'libs/select2/select2',
    'squire': '../bower_components/squire/src/Squire',
    'clipboard': 'libs/clipboard/clipboard',
    'es5-shim': 'libs/es5-shim/es5-shim',
    'mathjax': '//cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS_HTML&amp;delayStartupUntil=configured',

    // for development use
    // 'google-analytics': "//www.google-analytics.com/analytics_debug",
    'google-analytics': '//www.google-analytics.com/analytics',
    'google-recaptcha' : '//www.google.com/recaptcha/api.js?&render=explicit&onload=onRecaptchaLoad',
    'persist-js': 'libs/persist-js/src/persist',
    'backbone-validation': 'libs/backbone-validation/backbone-validation',
    'backbone.stickit' : 'libs/backbone.stickit/backbone.stickit',

    // only for diagnostics/debugging/testing - wont get loaded otherwise
    'sprintf': 'libs/sprintf/sprintf',
    'chai': '../bower_components/chai/chai',
    'sinon': '../bower_components/sinon/index'
  },

  hbs: {
    templateExtension: 'html',
    helpers: false
  },

  shim: {
    "Backbone": {
      deps: ["backbone"],
      exports: "Backbone"
    },
    'backbone.stickit' : {
      deps : ['backbone']
    },
    'backbone-validation' : {
      deps : ['backbone']
    },
    'bootstrap' : {
      deps: ['jquery', 'jquery-ui']
    },
    // This is required to ensure Backbone works as expected within the AMD
    // environment.
    'backbone': {
      // These are the two hard dependencies that will be loaded first.
      deps: ['jquery', 'underscore']
    },
    marionette : {
      deps : ['jquery', 'underscore', 'backbone'],
      exports : 'Marionette'
    },
    cache: {
      exports: 'Cache'
    },
    'jquery-querybuilder': {
      deps: ['jquery']
    },
    'd3-cloud' : {
      deps :['d3']
    },
    'nvd3' : {
      deps : ['d3']
    },
    'jquery-ui' : {
      deps: ['jquery']
    },
    'sprintf': {
      exports: 'sprintf'
    },
    'persist-js': {
      exports: 'Persist'
    },
    mathjax: {
      exports: "MathJax",
      init: function() {
        MathJax.Hub.Config({
          HTML: ["input/TeX","output/HTML-CSS"],
          TeX: { extensions: ["AMSmath.js","AMSsymbols.js"],
            equationNumbers: { autoNumber: "AMS" } },
          extensions: ["tex2jax.js"],
          jax: ["input/TeX","output/HTML-CSS"],
          tex2jax: { inlineMath: [ ['$','$'], ["\\(","\\)"] ],
            displayMath: [ ['$$','$$'], ["\\[","\\]"] ],
            processEscapes: true },
          "HTML-CSS": { availableFonts: ["TeX"],
            linebreaks: { automatic: true } }
        });
        MathJax.Hub.Startup.onload();
        return MathJax;
      }
    }
  },

  callback: function() {
    require(['hbs/handlebars'], function(Handlebars) {
      // register helpers
      // http://doginthehat.com.au/2012/02/comparison-block-helper-for-handlebars-templates/#comment-44
      //eg  (where current is a variable): {{#compare current 1 operator=">"}}

      Handlebars.registerHelper('compare', function (lvalue, rvalue, options) {
        var operators, result, operator;
        if (arguments.length < 3) {
          throw new Error("Handlerbars Helper 'compare' needs 2 parameters");
        }
        if (options === undefined || !options.hash || !options.hash.operator) {
          operator = "===";
        }
        else {
          operator = options.hash.operator;
        }

        operators = {
          '==': function(l, r) { return l == r; },
          '===': function(l, r) { return l === r; },
          '!=': function(l, r) { return l != r; },
          '!==': function(l, r) { return l !== r; },
          '<': function(l, r) { return l < r; },
          '>': function(l, r) { return l > r; },
          '<=': function(l, r) { return l <= r; },
          '>=': function(l, r) { return l >= r; },
          'typeof': function(l, r) { return typeof l == r; }
        };
        if (!operators[operator]) {
          throw new Error("Handlerbars Helper 'compare' doesn't know the operator " + operator);
        }
        result = operators[operator](lvalue, rvalue);
        if (result) {
          return options.fn(this);
        } else {
          return options.inverse(this);
        }
      });
    });
  }
});
