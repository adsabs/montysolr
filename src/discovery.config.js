// Main config file for the Discovery application


require.config({


  // Initialize the application with the main application file or if we run
  // as a test, then load the test unittests
  deps: window.mocha
    ? [ window.mocha.testLoader ? window.mocha.testLoader : '../test/test-loader' ]
    : [ 'js/apps/discovery/main' ],

  // Configuration we want to make available to modules of ths application
  // see: http://requirejs.org/docs/api.html#config-moduleconfig
  config: {
    'js/page_managers/controller': {
      'LandingPageManager': {

      }
    },
    'js/apps/discovery/main': {
      core: {
        controllers: {
          FeedbackMediator: 'js/wraps/discovery_mediator'
        },
        services: {
          'Api': 'js/services/api',
          'PubSub': 'js/services/pubsub',
          'Navigator': 'js/apps/discovery/navigator'
        },
        objects: {
          User: 'js/components/user',
          DynamicConfig: 'discovery.vars',
          QueryMediator: 'js/components/query_mediator',
          HistoryManager: 'js/components/history_manager',
          MasterPageManager: 'js/page_managers/master'
        },
        modules: {
          FacetFactory: 'js/widgets/facet/factory'
        }
      },
      widgets: {
        LandingPage: 'js/wraps/landing_page_manager',
        SearchPage: 'js/wraps/results_page_manager',
        DetailsPage: 'js/wraps/details_page_manager',

        SearchWidget: 'js/widgets/search_bar/search_bar_widget',
        Results: 'js/widgets/results/widget',
        QueryInfo: 'js/widgets/query_info/query_info_widget',
        QueryDebugInfo: 'js/widgets/api_query/widget',
        Export  : 'js/widgets/export/widget',
        Sort : 'js/widgets/sort/widget',
        VisualizationDropdown : 'js/wraps/visualization_dropdown',
        AuthorNetwork : 'js/wraps/author_network',

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
        ShowSimilar : 'js/widgets/similar/widget',
        ShowResources : 'js/widgets/resources/widget',

        TOCWidget: 'js/page_managers/toc_widget'

      },
      plugins: {
      }
    }
  },

  // Configuration for the facades (you can pick specific implementation, just for your
  // application) see http://requirejs.org/docs/api.html#config-map
  map: {
    '*': {
      'api_query_impl': 'js/components/solr_params',
      'api_response_impl': 'js/components/solr_response',
      'api_request_impl': 'js/components/default_request',
      'pubsub_service_impl': 'js/services/default_pubsub'
    }
  },

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
    'hoverIntent': 'libs/jquery-hoverIntent/jquery.hoverIntent',
    'cache': 'libs/dsjslib/lib/Cache',
    'jquery-querybuilder': 'libs/jQuery-QueryBuilder/query-builder',
    'd3-cloud' : 'libs/d3-cloud/d3.layout.cloud',

    // only for testing (won't get loaded otherwise)
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

    'jquery-ui' : {
      deps: ['jquery']
    }
  }
});
