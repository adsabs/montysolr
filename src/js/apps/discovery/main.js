/**
 * Discovery application: main bootstrapping routine
 *
 * Here we will bring up to life the discovery application,
 * all configuration is provided through the discovery.config.js
 *
 * Inside the config, there are sections for:
 *
 *  - where to find js libraries
 *  - which widgets to load (for this application)
 *  - which environmental variables are used
 *        (and how to bootstrap run-time values)
 */

define(["config", 'module'], function(config, module) {

  require(["router",
      'js/components/application',
      'js/mixins/discovery_bootstrap'
      ],
    function(Router,
             Application,
             DiscoveryBootstrap
             ) {


      var app = new (Application.extend(DiscoveryBootstrap))();

      // load the objects/widgets/modules (using discovery.config.js)
      var defer = app.loadModules(module.config());

      // after they are loaded; we'll kick off the application
      defer.done(function() {

        // this will activate all loaded modules
        app.activate();

        // set some important urls, parameters before doing anything
        app.configure();

        var bootPromise = app.bootstrap();

        bootPromise.done(function (data) {

          // set the API key
          if (data.access_token) {
            var api = app.getBeeHive().getService('Api');
            api.access_token = data.token_type + ':' + data.access_token;
            api.refresh_token = data.refresh_token;
            api.expires_in = data.expires_in;
          }

          app.start(Router);

        }).fail(function () {
          app.redirect('/505.html');
        });

      }).fail(function() {
        // if we failed loading, retry *once again* (and give up eventually)
        app.reload('/404.html');
          this.dispatchNewQuery(paginator.cleanQuery(q));
        }
      };

      resultsWidgetDict.authorFacets.activate(beehive.getHardenedInstance());
      resultsWidgetDict.database.activate(beehive.getHardenedInstance());
      resultsWidgetDict.keywords.activate(beehive.getHardenedInstance());
      resultsWidgetDict.pub.activate(beehive.getHardenedInstance());
      resultsWidgetDict.bibgroup.activate(beehive.getHardenedInstance());
      resultsWidgetDict.data.activate(beehive.getHardenedInstance());
      resultsWidgetDict.vizier.activate(beehive.getHardenedInstance());
      resultsWidgetDict.grants.activate(beehive.getHardenedInstance());
      resultsWidgetDict.refereed.activate(beehive.getHardenedInstance());



      resultsWidgetDict.searchBar = app.getWidget('SearchBar')

      resultsWidgetDict.queryInfo = app.getWidget('QueryInfo');
      resultsWidgetDict.graphTabs = app.getWidget('GraphTabs');
      resultsWidgetDict.queryDebugInfo = app.getWidget('QueryDebugInfo');

      resultsWidgetDict.results = app.getWidget('Results');

      //is there a nicer way to do this?
      resultsWidgetDict.results.view.sortView = app.getWidget('Sort').view;



      _.each(resultsWidgetDict.graphTabs.widgets, function(w){
        w.activate(beehive.getHardenedInstance());
      });

      var abstract = app.getWidget('Abstract')

      var references = app.getWidget('References');

      var citations = app.getWidget('Citations')

      var coreads = app.getWidget('Coreads')

      var tableOfContents = app.getWidget('TableOfContents')

      var similar = app.getWidget('Similar')

      var resources = app.getWidget('Resources');

      var pageControllers = {};
      var bumblebeeHistory = app.getObject("HistoryManager");

      //     all sub-views have their own controllers
     pageControllers.results= new ResultsController({widgetDict : resultsWidgetDict});


     pageControllers.abstract = new AbstractController({widgetDict :
       {
            abstract : abstract,
            references :references,
            citations : citations,
            coreads : coreads,
            tableOfContents : tableOfContents,
            similar : similar,
            searchBar : resultsWidgetDict.searchBar,
            resources : resources
          }});

     pageControllers.index  = new LandingPageController({widgetDict : {searchBar: resultsWidgetDict.searchBar}});

      _.each(pageControllers, function(v, k){
        v.activate(beehive.getHardenedInstance())
      });

      var masterPageManager = new MasterPageManager({pageControllers: pageControllers, history: bumblebeeHistory});

      masterPageManager.activate(beehive.getHardenedInstance());

      app.router = new Router({pageManager : masterPageManager});
      app.router.activate(beehive.getHardenedInstance());

      // Trigger the initial route and enable HTML5 History API support


      Backbone.history.start(conf.routerConf);


      // All navigation that is relative should be passed through the navigate
      // method, to be processed by the router. If the link has a `data-bypass`
       // attribute, bypass the delegation completely.
  $(document).on("click", "a[href]:not([data-bypass])", function (evt) {

    var attr = $(this).attr("href");

    //getting rid of first character so router.routes can easily do regex matches
    var withoutSlashOrHash = attr.match(/^[#/]*(.*)/);
    withoutSlashOrHash = withoutSlashOrHash.length === 2 ? withoutSlashOrHash[1] : attr;

    var route = _.find(Backbone.history.handlers, function (h) {
      //testing to see if it matches any router route other than the "catchall" 404 route
      if (h.route.test(withoutSlashOrHash) && h.route.toString() !== /^(.*?)$/.toString()) {

        return true
      }
    });

    if (route !== undefined) {

      evt.preventDefault();
      Backbone.history.navigate(attr, true);
    }
  });

  $(document).on("scroll", function () {

          if ($("#landing-page-layout").length > 0){
            return
          }
          //navbar is currently 40 px height
          if ($(window).scrollTop() > 70) {
            $(".s-search-bar-full-width-container").addClass("s-search-bar-motion");
            $("#field-options").hide()
          }
          else {
            $(".s-search-bar-full-width-container").removeClass("s-search-bar-motion")
            $("#field-options").fadeIn()
          }
        });

      });

    });
});



