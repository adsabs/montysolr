// Main config file for the Discovery application
require.config({



  // Initialize the application with the main application file or if we run
  // as a test, then load the test unittests
  deps: (function(){

    if (typeof window !== "undefined" && window.bbbTest){
      return  [window.bbbTest.testLoader ? window.bbbTest.testLoader : '../test/test-loader' ];
    }
    else {
      return [ 'js/apps/discovery/main'];
    }

  }()),


  //this will be overridden in the compiled file
  waitSeconds: 30,

  // Configuration we want to make available to modules of ths application
  // see: http://requirejs.org/docs/api.html#config-moduleconfig
  config: {

  'es6': {
              'modules': undefined,
      },

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
          AlertsController: 'js/wraps/alerts_mediator',
          Orcid: 'js/modules/orcid/module'
        },
        services: {
          Api: 'js/services/api',
          PubSub: 'js/services/pubsub',
          Navigator: 'js/apps/discovery/navigator',
          PersistentStorage: 'js/services/storage',
          HistoryManager: 'js/components/history_manager'
        },
        objects: {
          User: 'js/components/user',
          Session: 'js/components/session',
          DynamicConfig: 'discovery.vars',
          MasterPageManager: 'js/page_managers/master',
          AppStorage: 'js/components/app_storage',
          RecaptchaManager : 'js/components/recaptcha_manager',
          CSRFManager : "js/components/csrf_manager",
          LibraryController : 'js/components/library_controller',
          DocStashController : 'js/components/doc_stash_controller'
        },
        modules: {
          FacetFactory: 'js/widgets/facet/factory'
        }
      },
      widgets: {
        LandingPage: 'js/wraps/landing_page_manager/landing_page_manager',
        SearchPage: 'js/wraps/results_page_manager',
        DetailsPage: 'js/wraps/abstract_page_manager/abstract_page_manager',
        AuthenticationPage: 'js/wraps/authentication_page_manager',
        SettingsPage: 'js/wraps/user_settings_page_manager/user_page_manager',
        OrcidPage: 'js/wraps/orcid_page_manager/orcid_page_manager',
        OrcidInstructionsPage : 'js/wraps/orcid-instructions-page-manager/manager',

        LibrariesPage: 'js/wraps/libraries_page_manager/libraries_page_manager',
        HomePage: 'js/wraps/home_page_manager/home_page_manager',
        PublicLibrariesPage : 'js/wraps/public_libraries_page_manager/public_libraries_manager',
        ErrorPage : 'js/wraps/error_page_manager/error_page_manager',

        Authentication: 'js/widgets/authentication/widget',
        UserSettings: 'js/widgets/user_settings/widget',
        UserPreferences: 'js/widgets/preferences/widget',
        LibraryImport : 'js/widgets/library_import/widget',
        BreadcrumbsWidget: 'js/widgets/filter_visualizer/widget',
        NavbarWidget: 'js/widgets/navbar/widget',
        UserNavbarWidget: 'js/widgets/user_navbar/widget',
        AlertsWidget: 'js/widgets/alerts/widget',
        ClassicSearchForm: 'js/widgets/classic_form/widget',
        SearchWidget: 'js/widgets/search_bar/search_bar_widget',
        PaperSearchForm:'js/widgets/paper_search_form/widget',
        Results: 'js/widgets/results/widget',
        QueryInfo: 'js/widgets/query_info/query_info_widget',
        QueryDebugInfo: 'js/widgets/api_query/widget',
        ExportWidget  : 'js/widgets/export/widget',
        Sort : 'js/widgets/sort/widget',
        ExportDropdown : 'js/wraps/export_dropdown',
        VisualizationDropdown : 'js/wraps/visualization_dropdown',
        AuthorNetwork : 'js/wraps/author_network',
        PaperNetwork : 'js/wraps/paper_network',
        ConceptCloud : 'js/widgets/wordcloud/widget',
        BubbleChart : 'js/widgets/bubble_chart/widget',

        Metrics :  'js/widgets/metrics/widget',
        OrcidBigWidget: 'js/modules/orcid/widget/widget',

        AuthorFacet: 'js/wraps/author_facet',
        BibgroupFacet: 'js/wraps/bibgroup_facet',
        BibstemFacet: 'js/wraps/bibstem_facet',
        DataFacet: 'js/wraps/data_facet',
        DatabaseFacet: 'js/wraps/database_facet',
        GrantsFacet: 'js/wraps/grants_facet',
        KeywordFacet: 'js/wraps/keyword_facet',
		    ObjectFacet: 'js/wraps/object_facet',
        RefereedFacet: 'js/wraps/refereed_facet',
        VizierFacet: 'js/wraps/vizier_facet',
        GraphTabs : 'js/wraps/graph_tabs',
        FooterWidget : 'js/widgets/footer/widget',

        ShowAbstract: 'js/widgets/abstract/widget',
        ShowGraphics: 'js/widgets/graphics/widget',
        ShowGraphicsSidebar: 'js/wraps/sidebar-graphics-widget',
        ShowReferences: 'js/wraps/references',
        ShowCitations : 'js/wraps/citations',
        ShowCoreads : 'js/wraps/coreads',
        //can't camel case because router only capitalizes first letter
        ShowTableofcontents : 'js/wraps/table_of_contents',
        ShowResources : 'js/widgets/resources/widget',
        ShowRecommender : 'js/widgets/recommender/widget',
        ShowMetrics: 'js/wraps/paper_metrics',
        ShowPaperExport : 'js/wraps/paper_export',
        ShowLibraryAdd : 'js/wraps/abstract_page_library_add/widget',

        IndividualLibraryWidget : 'js/widgets/library_individual/widget',
        AllLibrariesWidget : 'js/widgets/libraries_all/widget',
        LibraryListWidget : 'js/widgets/library_list/widget'
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
    'react' : 'libs/react/react-with-addons',
    'react-dom' : 'libs/react/react-dom',
     es6: 'libs/requirejs-babel-plugin/es6',
     babel: 'libs/requirejs-babel-plugin/babel-5.8.34.min',
    'redux' : 'libs/redux/index',
    'react-redux' : 'libs/react-redux/index',
    'redux-thunk' : 'libs/redux-thunk/index',
    'backbone': 'libs/backbone/backbone',
    'hbs': 'libs/require-handlebars-plugin/hbs',
    'async': 'libs/requirejs-plugins/async',
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
    'google-recaptcha' : '//www.google.com/recaptcha/api.js?&render=explicit&onload=onRecaptchaLoad',
    'persist-js': 'libs/persist-js/src/persist',
    'backbone-validation': 'libs/backbone-validation/backbone-validation',
    'backbone.stickit' : 'libs/backbone.stickit/backbone.stickit',
    // only for diagnostics/debugging/testing - wont get loaded otherwise
    'sprintf': 'libs/sprintf/sprintf',
    'chai': '../bower_components/chai/chai',
    'sinon': '../bower_components/sinon/index',
    'filesaver' : 'libs/FileSaver/FileSaver',
    'select2' : 'libs/select2/select2',
    'squire': '../bower_components/squire/src/Squire',
    'clipboard': 'libs/clipboard/clipboard',
    'es5-shim' : 'libs/es5-shim/es5-shim',
    'mathjax' : '//cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS_HTML&amp;delayStartupUntil=configured',
    'moment' : 'libs/momentjs/moment'

  },

  hbs : {
    'templateExtension' : 'html',
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
        init: function () {
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

    require([
      'hbs/handlebars'
              ], function(
        Handlebars
    ) {

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

    //set validation callbacks used by authentication and user settings widgets
    require(['backbone-validation'], function(){

      //this allows for instant validation of form fields using the backbone-validation plugin
      _.extend(Backbone.Validation.callbacks, {
        valid: function (view, attr, selector) {
          var $el = view.$('input[name=' + attr + ']');

           $el.closest('.form-group')
              .removeClass('has-error')
              .find('.help-block')
              .html('')
              .addClass('no-show');

        },
        invalid: function (view, attr, error, selector) {
          var $el = view.$('[name=' + attr + ']');
          $group = $el.closest('.form-group');

          if (view.submit === true){
            //only show error states if there has been a submit event
            $group.addClass('has-error');
            $group.find('.help-block').html(error).removeClass('no-show');
          }
        }
      });
    });

  }
});
