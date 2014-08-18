
define(["config", 'module'], function(config, module) {

  // Kick off the application
  require(["router", 'js/components/application',
      'js/page_managers/abstract_page_controller', 'js/page_managers/results_page_controller',
      'js/page_managers/landing_page_controller', 'bootstrap'],
    function(Router, Application, AbstractController, ResultsController, LandingPageController) {

    // load the objects/widgets/modules (as specified inside the main config
    // in the section config.main
    var app = new Application();
    var defer = app.loadModules(module.config());

    // after they are loaded; let's start the application
    defer.done(function() {

      // this will activate all loaded modules
      app.activate();

      // this is the application dynamic config
      var conf = app.getObject('DynamicConfig');

      // the central component
      var beehive = app.getBeeHive();
      var api = beehive.getService('Api');

      if (conf.root) {
        api.url = conf.root + "/" + api.url;
        app.root = conf.root;
      }
      if (conf.debug !== undefined) {
        app.getObject('QueryMediator').debug = conf.debug;
      }

      // XXX:rca - this will need to be moved somewhere else (it is getting confusing -- to long)

      // create composite widgets

      var FacetFactory = app.getModule("FacetFactory")

      resultsWidgetDict = {}

      resultsWidgetDict.authorFacets = FacetFactory.makeHierarchicalCheckboxFacet({
        facetField: "author_facet_hier",
        facetTitle: "Authors",
        openByDefault: true,
        logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']},
        responseProcessors: [
          function(v) {var vv = v.split('/'); return vv[vv.length-1]}
        ]
      });

      // XXX:rca - another hack
      resultsWidgetDict.authorFacets.handleLogicalSelection = function(operator) {
        var q = this.getCurrentQuery();
        var paginator = this.findPaginator(q).paginator;
        var conditions = this.queryUpdater.removeTmpEntry(q, 'SelectedItems');

        //XXX:rca - hack ; this logic is triggerd multiple times
        // we need to prevent that

        var self = this;

        if (conditions && _.keys(conditions).length > 0) {


          conditions = _.values(conditions);
          _.each(conditions, function(c, i, l) {
            l[i] = "author:\"" + c.title + "\"";
          });

          q = q.clone();

          var fieldName = 'fq_author';

          if (operator == 'and' || operator == 'limit to') {
            this.queryUpdater.updateQuery(q, fieldName, 'limit', conditions);
          }
          else if (operator == 'or') {
            this.queryUpdater.updateQuery(q, fieldName, 'expand', conditions);
          }
          else if (operator == 'exclude' || operator == 'not') {
            this.queryUpdater.updateQuery(q, fieldName, 'exclude', conditions);
          }

          var fq = '{!type=aqp cache=false cost=150 v=$' + fieldName +'}';
          var fqs = q.get('fq');
          if (!fqs) {
            q.set('fq', [fq]);
          }
          else {
            var i = _.indexOf(fqs, fq);
            if (i == -1) {
              fqs.push(fq);
            }
            q.set('fq', fqs);
          }
          q.unset('facet.prefix');
          q.unset('facet');
          this.dispatchNewQuery(paginator.cleanQuery(q));
        }
      };


      resultsWidgetDict.keywords = FacetFactory.makeBasicCheckboxFacet({
        facetField: "keyword_facet",
        facetTitle: "Keywords",
        openByDefault: false,
        logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}
      });

      resultsWidgetDict.database = FacetFactory.makeBasicCheckboxFacet({
        facetField: "database",
        facetTitle: "Collections",
        openByDefault: true,
        logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}

      });
      resultsWidgetDict.data = FacetFactory.makeBasicCheckboxFacet({
        facetField: "data_facet",
        facetTitle: "Data",
        openByDefault: false,
        logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}

      });

      resultsWidgetDict.vizier = FacetFactory.makeBasicCheckboxFacet({
        facetField: "vizier_facet",
        facetTitle: "Vizier Tables",
        openByDefault: false,
        logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}

      });

      resultsWidgetDict.pub = FacetFactory.makeBasicCheckboxFacet({
        facetField: "bibstem_facet",
        facetTitle: "Publications",
        openByDefault: false,
        logicOptions: {single: ['limit to', 'exclude'], multiple: ["or", "exclude"]}
      });

      resultsWidgetDict.bibgroup = FacetFactory.makeBasicCheckboxFacet({
        facetField: "bibgroup_facet",
        facetTitle: "Bib Groups",
        openByDefault: false,
        logicOptions: {single: ['limit to', 'exclude'], multiple: ["or", "exclude"]}
      });

      resultsWidgetDict.grants = FacetFactory.makeHierarchicalCheckboxFacet({
        facetField: "grant_facet_hier",
        facetTitle: "Grants",
        openByDefault: false,
        logicOptions: {single: ['limit to', 'exclude'], multiple: ["or", "exclude"]},
        responseProcessors: [
          function(v) {var vv = v.split('/'); return vv[vv.length-1]}
        ]
      });


      resultsWidgetDict.grants.handleLogicalSelection = function(operator) {
        var q = this.getCurrentQuery();
        var paginator = this.findPaginator(q).paginator;
        var conditions = this.queryUpdater.removeTmpEntry(q, 'SelectedItems');

        //XXX:rca - hack ; this logic is triggerd multiple times
        // we need to prevent that

        var self = this;

        if (conditions && _.keys(conditions).length > 0) {


          conditions = _.values(conditions);
          _.each(conditions, function(c, i, l) {
            l[i] = "grant:\"" + c.title + "\"";
          });

          q = q.clone();

          var fieldName = 'fq_grant';

          if (operator == 'and' || operator == 'limit to') {
            this.queryUpdater.updateQuery(q, fieldName, 'limit', conditions);
          }
          else if (operator == 'or') {
            this.queryUpdater.updateQuery(q, fieldName, 'expand', conditions);
          }
          else if (operator == 'exclude' || operator == 'not') {
            this.queryUpdater.updateQuery(q, fieldName, 'exclude', conditions);
          }

          var fq = '{!type=aqp cache=false cost=150 v=$' + fieldName +'}';
          var fqs = q.get('fq');
          if (!fqs) {
            q.set('fq', [fq]);
          }
          else {
            var i = _.indexOf(fqs, fq);
            if (i == -1) {
              fqs.push(fq);
            }
            q.set('fq', fqs);
          }
          q.unset('facet.prefix');
          q.unset('facet');
          this.dispatchNewQuery(paginator.cleanQuery(q));
        }
      };

      resultsWidgetDict.refereed = FacetFactory.makeBasicCheckboxFacet({
        facetField: "property",
        facetTitle: "Refereed Status",
        openByDefault: true,
        defaultQueryArguments: {
          "facet": "true",
          "facet.mincount": "1",
          "fl": "id",
          "facet.query": 'property:refereed'
        },
        // this is optimization, we'll execute only one query (we don't even facet on
        // other values). There is a possibility is is OK (but could also be wrong;
        // need to check)
        extractionProcessors:
          function(apiResponse) {
            var returnList = [];
            if (apiResponse.has('facet_counts.facet_queries')) {
              var queries = apiResponse.get('facet_counts.facet_queries');
              var v, found = 0;
              _.each(_.keys(queries), function(k) {
                v = queries[k];
                if (k.indexOf(':refereed') > -1) {
                  found = v;
                  returnList.push("refereed", v);
                }
              });

              returnList.push('notrefereed', apiResponse.get('response.numFound') - found);
              return returnList;
            }
          },
        logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['invalid choice']}

      });

      resultsWidgetDict.refereed.handleLogicalSelection = function(operator) {
        var q = this.getCurrentQuery();
        var paginator = this.findPaginator(q).paginator;
        var conditions = this.queryUpdater.removeTmpEntry(q, 'SelectedItems');

        //XXX:rca - hack ; this logic is triggerd multiple times
        // we need to prevent that

        var self = this;

        if (conditions && _.keys(conditions).length > 0) {


          conditions = _.values(conditions);
          _.each(conditions, function(c, i, l) {
            l[i] = 'property:' + self.queryUpdater.escapeInclWhitespace(c.value);
          });

          q = q.clone();

          var fieldName = 'fq_' + this.facetField;

          if (operator == 'and' || operator == 'limit to') {
            this.queryUpdater.updateQuery(q, fieldName, 'limit', conditions);
          }
          else if (operator == 'or') {
            this.queryUpdater.updateQuery(q, fieldName, 'expand', conditions);
          }
          else if (operator == 'exclude' || operator == 'not') {
            this.queryUpdater.updateQuery(q, fieldName, 'exclude', conditions);
          }

          var fq = '{!type=aqp v=$' + fieldName +'}';
          var fqs = q.get('fq');
          if (!fqs) {
            q.set('fq', [fq]);
          }
          else {
            var i = _.indexOf(fqs, fq);
            if (i == -1) {
              fqs.push(fq);
            }
            q.set('fq', fqs);
          }

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

      resultsWidgetDict.results = app.getWidget('Results')

      resultsWidgetDict.graphTabs = app.getWidget('GraphTabs')

      resultsWidgetDict.searchBar = app.getWidget('SearchBar')

      resultsWidgetDict.queryInfo = app.getWidget('QueryInfo');
      resultsWidgetDict.queryDebugInfo = app.getWidget('QueryDebugInfo');



      _.each(resultsWidgetDict.graphTabs.widgets, function(w){
        w.activate(beehive.getHardenedInstance());
      });

      var abstract = app.getWidget('Abstract')
      abstract.activate(beehive.getHardenedInstance())
      var references = app.getWidget('References');
      references.activate(beehive.getHardenedInstance())

      var citations = app.getWidget('Citations')
      citations.activate(beehive.getHardenedInstance())

      var coreads = app.getWidget('Coreads')
      coreads.activate(beehive.getHardenedInstance())

      var tableOfContents = app.getWidget('TableOfContents')
      tableOfContents.activate(beehive.getHardenedInstance())

      var similar = app.getWidget('Similar')
      similar.activate(beehive.getHardenedInstance());

      var resources = app.getWidget('Resources');
      resources.activate(beehive.getHardenedInstance());

      var pageControllers = {};
      var bumblebeeHistory = app.getObject("HistoryManager");

      //     all sub-views have their own controllers
     pageControllers.resultsPage = new ResultsController({widgetDict : resultsWidgetDict, history : bumblebeeHistory});


     pageControllers.abstractPage = new AbstractController({widgetDict :
          {abstract : abstract,
            references :references,
            citations : citations,
            coreads : coreads,
            tableOfContents : tableOfContents,
            similar : similar,
            searchBar : resultsWidgetDict.searchBar,
            resources : resources
          },
     history : bumblebeeHistory});

     pageControllers.landingPage  = new LandingPageController({widgetDict : {searchBar: resultsWidgetDict.searchBar},
     history: bumblebeeHistory});

      _.each(pageControllers, function(v, k){
        v.activate(beehive.getHardenedInstance())
      });

      app.router = new Router({pageControllers : pageControllers, history : bumblebeeHistory});
      app.router.activate(beehive.getHardenedInstance());

      // Trigger the initial route and enable HTML5 History API support


      Backbone.history.start(conf.routerConf);

      // All navigation that is relative should be passed through the navigate
      // method, to be processed by the router. If the link has a `data-bypass`
      // attribute, bypass the delegation completely.
      $(document).on("click", "a[href]:not([data-bypass])", function(evt) {

        // Get the absolute anchor href.
        var href = { prop: $(this).prop("href"), attr: $(this).attr("href") };
        // Get the absolute root.
        var root = location.protocol + "//" + location.host + app.root;

        // Ensure the root is part of the anchor href, meaning it's relative.
//        if (href.prop.slice(0, root.length) === root) {

        //alex changed the logic to check for a relative link, possibly incorrectly??
        if(href.attr[0] === "/"){
          // Stop the default event to ensure the link will not cause a page
          // refresh.
          evt.preventDefault();

          // `Backbone.history.navigate` is sufficient for all Routers and will
          // trigger the correct events. The Router's internal `navigate` method
          // calls this anyways.  The fragment is sliced from the root.
          Backbone.history.navigate(href.attr, true);
        }
      });

      $(document).on("scroll", function(){
        //navbar is currently 40 px height
        if ($(window).scrollTop() > 70) {
          $(".s-search-bar-row").addClass("s-search-bar-motion");
          $("#field-options").hide()
        }
        else {
          $(".s-search-bar-row").removeClass("s-search-bar-motion")
          $("#field-options").fadeIn()
        }
      })




    });


  });
});
