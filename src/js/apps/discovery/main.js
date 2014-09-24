
define(["config", 'module'], function(config, module) {

  // Kick off the application
  require(["router",
      'js/components/application',
      'js/page_managers/abstract_page_controller',
      'js/page_managers/results_page_controller',
      'js/page_managers/landing_page_controller',
      'js/page_managers/master_page_manager',
      'js/components/api_query',
      'js/components/api_request',
      'bootstrap'],
    function(Router,
      Application,
      AbstractController,
      ResultsController,
      LandingPageController,
      MasterPageManager,
      ApiQuery,
      ApiRequest) {

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

      if (conf) {
        if (conf.root) {
          api.url = conf.root + "/" + api.url;
          app.root = conf.root;
        }
        if (conf.debug !== undefined) {
          app.getObject('QueryMediator').debug = conf.debug;
        }

        if (conf.apiRoot) {
          api.url = conf.apiRoot;
        }

        // bootstrap application with remote configuration
        if (conf.bootstrapUrls) {

          var opts = {
            done: function (data) {
              if (data.access_token) {
                api.access_token = data.token_type + ':' + data.access_token;
                api.refresh_token = data.refresh_token;
                api.expires_in = data.expires_in;
              }
            },
            fail: function () {
              // ignore
            },
            type: 'GET'
          };
          var redirect_uri = window.location.origin + window.location.pathname;

          _.each(conf.bootstrapUrls, function (url) {
            if (url.indexOf('http') > -1) {
              opts.u = url;
            }
            else {
              delete opts.u;
            }

            api.request(new ApiRequest({query: new ApiQuery({redirect_uri: redirect_uri}),
                target: '/bumblebee/bootstrap'}),
              opts);
          })
        }
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


      resultsWidgetDict.searchBar = app.getWidget('SearchBar')

      resultsWidgetDict.queryInfo = app.getWidget('QueryInfo');
      resultsWidgetDict.graphTabs = app.getWidget('GraphTabs');
      resultsWidgetDict.queryDebugInfo = app.getWidget('QueryDebugInfo');

      resultsWidgetDict.sort = app.getWidget('Sort');



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


    //turn this into its own view?
    $(document).on("click", ".btn-expand", function(){

          var $t = $(this);
          var $leftCol =  $(".s-results-left-column");
          var $rightCol =  $(".s-results-right-column");

          if ($t.hasClass("btn-upside-down")){

            $t.removeClass("btn-upside-down");

            if ($t.hasClass("left-expand")){

              $leftCol.removeClass("hidden-col")
              $leftCol.find(".left-col-container").width('').fadeIn(500).children().show();

            }
            else {
              $rightCol.removeClass("hidden-col");

             $rightCol.find(".right-col-container").width('').fadeIn(500) ;

            }

            if (!$rightCol.hasClass("hidden-col") && !$leftCol.hasClass("hidden-col")){
              $("#results-middle-column")
                .css({"width": ""})

            }
            else if ($leftCol.hasClass("hidden-col")){
              $("#results-middle-column")
                .css({"width": "75%"})
            }
            else {
              $("#results-middle-column")
                .css({"width":  "83.33333333%"})

            }

          }
          else {
            $t.addClass("btn-upside-down");

            if ($t.hasClass("left-expand")){

              $leftCol.addClass("hidden-col")

              $leftCol.find(".left-col-container").width(0).fadeOut(500).children().hide();

            }
            else {
              //expand to the right

              $rightCol.addClass("hidden-col")

              $rightCol.find(".right-col-container").width(0).hide(500);

            }

            if ($rightCol.hasClass("hidden-col") && $leftCol.hasClass("hidden-col")){
              $("#results-middle-column")
                .css({"width": "100%"})

            }
            else if ($rightCol.hasClass("hidden-col")){
              $("#results-middle-column")
                // 58.33333 + 25
                .css("width", "83.33333333%")
            }
            else {
              //58.33333 + 16.666666
              $("#results-middle-column")
                .css("width", "75%")

            }

          }

        })

      });

    });
});



