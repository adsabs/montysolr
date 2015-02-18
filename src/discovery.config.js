// Main config file for the Discovery application
require.config({


  // Initialize the application with the main application file or if we run
  // as a test, then load the test unittests
  deps: window.bbbTest ? [window.bbbTest.testLoader ? window.bbbTest.testLoader : '../test/test-loader' ] : [ 'js/apps/discovery/main'],
  waitSeconds: 15,

  // Configuration we want to make available to modules of ths application
  // see: http://requirejs.org/docs/api.html#config-moduleconfig
  config: {
    'js/widgets/export/widget': {
      url: 'http://adsabs-classic-exports-service.elasticbeanstalk.com',
      target: '/'
    },

    'js/components/persistent_storage': {
      // the unique namespace under which the local storage will be created
      // so every new instance of the storage will be saving its data into
      // <namespace>[other-name]
      namespace: 'bumblebee'
    },

    'js/apps/discovery/main': {

      core: {
        controllers: {
          FeedbackMediator: 'js/wraps/discovery_mediator',
          QueryMediator: 'js/components/query_mediator',
          Diagnostics: 'js/bugutils/diagnostics',
          AlertsController: 'js/components/alerts_mediator',
          Orcid: 'js/modules/orcid/module'
        },
        services: {
          Api: 'js/services/api',
          PubSub: 'js/services/pubsub',
          Navigator: 'js/apps/discovery/navigator',
          PersistentStorage: 'js/services/storage'
        },
        objects: {
          User: 'js/components/user',
          DynamicConfig: 'discovery.vars',
          HistoryManager: 'js/components/history_manager',
          MasterPageManager: 'js/page_managers/master',
          AppStorage: 'js/components/app_storage'
        },
        modules: {
          FacetFactory: 'js/widgets/facet/factory'
        }
      },
      widgets: {
        LandingPage: 'js/wraps/landing_page_manager',
        SearchPage: 'js/wraps/results_page_manager',
        DetailsPage: 'js/wraps/details_page_manager',

        AlertsWidget: 'js/widgets/alerts/widget',
        SearchWidget: 'js/widgets/search_bar/search_bar_widget',
        Results: 'js/widgets/results/widget',
        QueryInfo: 'js/widgets/query_info/query_info_widget',
        QueryDebugInfo: 'js/widgets/api_query/widget',
        ExportWidget  : 'js/widgets/export/widget',
        Sort : 'js/widgets/sort/widget',
        ExportDropdown : 'js/wraps/export_dropdown',
        VisualizationDropdown : 'js/wraps/visualization_dropdown',
        AuthorNetwork : 'js/wraps/author_network',
        PaperNetwork : 'js/wraps/paper_network',
        WordCloud : 'js/widgets/wordcloud/widget',

        Metrics :  'js/widgets/metrics/widget',

        OrcidBigWidget: 'js/modules/orcid/widget/widget',

        AuthorFacet: 'js/wraps/author_facet',
        BibgroupFacet: 'js/wraps/bibgroup_facet',
        BibstemFacet: 'js/wraps/bibstem_facet',
        DataFacet: 'js/wraps/data_facet',
        DatabaseFacet: 'js/wraps/database_facet',
        GrantsFacet: 'js/wraps/grants_facet',
        KeywordFacet: 'js/wraps/keyword_facet',
        RefereedFacet: 'js/wraps/refereed_facet',
        VizierFacet: 'js/wraps/vizier_facet',
        GraphTabs : 'js/wraps/graph_tabs',

        ShowAbstract: 'js/widgets/abstract/widget',
        ShowReferences: 'js/wraps/references',
        ShowCitations : 'js/wraps/citations',
        ShowCoreads : 'js/wraps/coreads',
        ShowTableOfContents : 'js/wraps/table_of_contents',
        //ShowSimilar : 'js/widgets/similar/widget',
        ShowResources : 'js/widgets/resources/widget',
        //ShowRecommender : 'js/widgets/recommender/widget',
        ShowPaperMetrics: 'js/wraps/paper_metrics',

        TOCWidget: 'js/page_managers/toc_widget'
      },
      plugins: {}
      }
  },

  // Configuration for the facades (you can pick specific implementation, just for your
  // application) see http://requirejs.org/docs/api.html#config-map
  map: {
    '*': {
      'pubsub_service_impl': 'js/services/default_pubsub',
      'analytics_config': 'discovery.vars'
    }
  },

  paths: {

    //TODO: these libs will need manual optimization (they dont come with minified sources)
    //TODO: require-handlebars-js, d3-cloud, jquery-hoverIntent, dsjslib/cache, query-builder

    // bumblebee components (here we'll lists simple names), paths are relative
    // to the config (the module that bootstraps our application; look at the html)
    // as a convention, all modules should be loaded using 'symbolic' names
    'config': './discovery.config',
    'main': 'js/apps/discovery/main',
    'router': 'js/apps/discovery/router',
    'analytics': 'js/components/analytics',

    // Opt for Lo-Dash Underscore compatibility build over Underscore.
    "underscore": "libs/lodash/lodash.compat",

    // 3rd party dependencies
    'jquery': 'libs/jquery/jquery',
    'backbone': 'libs/backbone/backbone',
    'hbs': 'libs/require-handlebars-plugin/hbs',
    'marionette' : 'libs/marionette/backbone.marionette',
    'backbone.wreqr' : 'libs/backbone.wreqr/lib/backbone.wreqr',
    'backbone.eventbinder' : 'libs/backbone.eventbinder/backbone.eventbinder',
    'backbone.babysitter' : 'libs/backbone.babysitter/backbone.babysitter',
    'bootstrap': 'libs/bootstrap/bootstrap',
    'jquery-ui' : 'libs/jqueryui/jquery-ui',
    'd3':'libs/d3/d3',
    'd3-cloud' : 'libs/d3-cloud/d3.layout.cloud',
    'hoverIntent': 'libs/jquery-hoverIntent/jquery.hoverIntent',
    'cache': 'libs/dsjslib/lib/Cache',
    'jquery-querybuilder': 'libs/jQuery-QueryBuilder/query-builder',
    'nvd3' :  'libs/nvd3/nv.d3',
    // for development use
    //'google-analytics': "//www.google-analytics.com/analytics_debug",
    'google-analytics': "//www.google-analytics.com/analytics",
    'persist-js': 'libs/persist-js/src/persist',

    // only for diagnostics/debugging/testing - wont get loaded otherwise
    'sprintf': 'libs/sprintf/sprintf',
    'chai': '../bower_components/chai/chai',
    'sinon': '../bower_components/sinon/index'

  },

  hbs : {
    'templateExtension' : 'html'

  },

  shim: {
    'bootstrap' : {
      deps: ['jquery']
    },
    // This is required to ensure Backbone works as expected within the AMD
    // environment.
    'backbone': {
      // These are the two hard dependencies that will be loaded first.
      deps: ['jquery', 'underscore'],

      // This maps the global `Backbone` object to `require('backbone')`.
      exports: 'Backbone'
    },

    marionette : {
      deps : ['jquery', 'underscore', 'backbone'],
      exports : 'Marionette'
    },

    cache: {
      exports: 'Cache'
    },

    'jquery-querybuilder': {
      deps: ['jquery'],
      exports: 'QueryBuilder'
    },

    'd3-cloud' : {
      deps :['d3']
    },

    'nvd3' : {
      deps : ['d3']

    },

    'jquery-ui' : {
      deps: ['jquery', 'bootstrap']
    },

    'sprintf': {
      exports: 'sprintf'
    },

    'persist-js': {
      exports: 'Persist'
    }
  },

  callback: function() {
    require(['hbs/handlebars'], function(Handlebars) {
      // register system-wide helper for handlebars
      // http://doginthehat.com.au/2012/02/comparison-block-helper-for-handlebars-templates/#comment-44
      Handlebars.registerHelper('compare', function (lvalue, operator, rvalue, options) {
        var operators, result;
        if (arguments.length < 3) {
          throw new Error("Handlerbars Helper 'compare' needs 2 parameters");
        }
        if (options === undefined) {
          options = rvalue;
          rvalue = operator;
          operator = "===";
        }
        operators = {
          '==': function (l, r) { return l == r; },
          '===': function (l, r) { return l === r; },
          '!=': function (l, r) { return l != r; },
          '!==': function (l, r) { return l !== r; },
          '<': function (l, r) { return l < r; },
          '>': function (l, r) { return l > r; },
          '<=': function (l, r) { return l <= r; },
          '>=': function (l, r) { return l >= r; },
          'typeof': function (l, r) { return typeof l == r; }
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
