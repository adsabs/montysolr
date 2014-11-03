
/*
 * This module contains a set of utilities to bootstrap Discovery app
 */
define([
    'underscore',
    'backbone',
    'js/page_managers/abstract_page_controller',
    'js/page_managers/results_page_controller',
    'js/page_managers/landing_page_controller',
    'js/page_managers/master_page_manager',
    'js/components/api_query',
    'js/components/api_request'
    ],
  function(
    _,
    Backbone,
    AbstractController,
    ResultsController,
    LandingPageController,
    MasterPageManager,
    ApiQuery,
    ApiRequest) {

  var Mixin = {

    configure: function() {

      var conf = this.getObject('DynamicConfig');

      if (conf) {

        var beehive = this.getBeeHive();
        var api = beehive.getService('Api');

        if (conf.root) {
          api.url = conf.root + "/" + api.url;
          this.root = conf.root;
        }
        if (conf.debug !== undefined) {
          beehive.debug = conf.debug;
          this.getObject('QueryMediator').debug = conf.debug;
        }

        if (conf.apiRoot) {
          api.url = conf.apiRoot;
        }

        this.bootstrapUrls = conf.bootstrapUrls;
      }

    },

    bootstrap: function() {

      var defer = $.Deferred();

      // this is the application dynamic config
      var api = this.getBeeHive().getService('Api');

      // load configuration from remote endpoints
      if (this.bootstrapUrls) {

        var pendingReqs = this.bootstrapUrls.length;
        var retVal = {};

        // harvest information from the remote urls and merge it into one object
        var opts = {
          done: function (data) {
            pendingReqs--;
            _.extend(retVal, data);
            if (pendingReqs <= 0) defer.resolve(retVal);
          },
          fail: function () {
            pendingReqs--;
            if (pendingReqs <= 0) defer.resolve(retVal);
          },
          type: 'GET'
        };
        var redirect_uri = location.origin + location.pathname;

        _.each(this.bootstrapUrls, function (url) {
          if (url.indexOf('http') > -1) {
            opts.u = url;
          }
          else {
            delete opts.u;
          }

          api.request(new ApiRequest({
              query: new ApiQuery({redirect_uri: redirect_uri}),
              target: '/bumblebee/bootstrap'}),
            opts);
        });

        setTimeout(function() {
            if (defer.state() == 'resolved')
              return;
            defer.reject();
          },
          3000);
      }
      else {
        setTimeout(function() {
          defer.resolve({}),
            1
        });
      }
      return defer;
    },

    /**
     * Reload the application - by simply changing the URL (append bbbRedirect=1)
     * If the url already contains 'bbbRedirect', redirect to the error page.
     * @param errorPage
     */
    reload: function(endPage) {
      if (location.search && location.search.indexOf('bbbRedirect=1') > -1) {
        return this.redirect(endPage);
      }
      location.search = location.search ? location.search + '&bbbRedirect=1' : 'bbbRedirect=1';
    },

    redirect: function(endPage) {
      if (this.router) {
        location.pathname = this.router.root + endPage;
      }
      // let's replace the last element from pathname - this code will run only when
      // router is not yet available; therefore it should hit situations when the app
      // was not loaded (but it is not bulletproof - the urls can vary greatly)
      // TODO: intelligently explore the rigth url (by sending HEAD requests)
      location.href = location.protocol + '//' + location.hostname + ':' + location.port +
        location.pathname.substring(0, location.pathname.lastIndexOf('/')) + '/' + endPage;
    },

    start: function(Router) {
      var app = this;
      var beehive = this.getBeeHive();
      var api = beehive.getService("Api");
      var FacetFactory = app.getModule("FacetFactory");
      var conf = this.getObject('DynamicConfig');


      var resultsWidgetDict = {};

      resultsWidgetDict.authorFacets = FacetFactory.makeHierarchicalCheckboxFacet({
        facetField: "author_facet_hier",
        facetTitle: "Authors",
        openByDefault: true,
        logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']},
        responseProcessors: [
          function (v) {
            var vv = v.split('/');
            return vv[vv.length - 1]
          }
        ]
      });

      // XXX:rca - another hack
      resultsWidgetDict.authorFacets.handleLogicalSelection = function (operator) {
        var q = this.getCurrentQuery();
        var paginator = this.findPaginator(q).paginator;
        var conditions = this.queryUpdater.removeTmpEntry(q, 'SelectedItems');

        //XXX:rca - hack ; this logic is triggerd multiple times
        // we need to prevent that

        var self = this;

        if (conditions && _.keys(conditions).length > 0) {


          conditions = _.values(conditions);
          _.each(conditions, function (c, i, l) {
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
            if (q.get(fieldName)) {
              this.queryUpdater.updateQuery(q, fieldName, 'exclude', conditions);
            }
            else {
              conditions.unshift('*:*');
              this.queryUpdater.updateQuery(q, fieldName, 'exclude', conditions);
            }

          }

          var fq = '{!type=aqp cache=false cost=150 v=$' + fieldName + '}';
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
          function (v) {
            var vv = v.split('/');
            return vv[vv.length - 1]
          }
        ]
      });


      resultsWidgetDict.grants.handleLogicalSelection = function (operator) {
        var q = this.getCurrentQuery();
        var paginator = this.findPaginator(q).paginator;
        var conditions = this.queryUpdater.removeTmpEntry(q, 'SelectedItems');

        //XXX:rca - hack ; this logic is triggerd multiple times
        // we need to prevent that

        var self = this;

        if (conditions && _.keys(conditions).length > 0) {


          conditions = _.values(conditions);
          _.each(conditions, function (c, i, l) {
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

          var fq = '{!type=aqp cache=false cost=150 v=$' + fieldName + '}';
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
        extractionProcessors: function (apiResponse) {
          var returnList = [];
          if (apiResponse.get('response.numFound') <= 0) {
            return returnList;
          }

          if (apiResponse.has('facet_counts.facet_queries')) {
            var queries = apiResponse.get('facet_counts.facet_queries');
            var v, found = 0;
            _.each(_.keys(queries), function (k) {
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

      resultsWidgetDict.refereed.handleLogicalSelection = function (operator) {
        var q = this.getCurrentQuery();
        var paginator = this.findPaginator(q).paginator;
        var conditions = this.queryUpdater.removeTmpEntry(q, 'SelectedItems');

        //XXX:rca - hack ; this logic is triggerd multiple times
        // we need to prevent that

        var self = this;

        if (conditions && _.keys(conditions).length > 0) {


          conditions = _.values(conditions);
          _.each(conditions, function (c, i, l) {
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

          var fq = '{!type=aqp v=$' + fieldName + '}';
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

      resultsWidgetDict.export = app.getWidget("Export");
      resultsWidgetDict.sort = app.getWidget('Sort');


      _.each(resultsWidgetDict.graphTabs.widgets, function (w) {
        w.activate(beehive.getHardenedInstance());
      });

      var abstract = app.getWidget('Abstract')
      abstract.activate(beehive.getHardenedInstance())

      var references = app.getWidget('References');
      references.activate(beehive.getHardenedInstance());

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
      pageControllers.results = new ResultsController({widgetDict: resultsWidgetDict});


      pageControllers.abstract = new AbstractController({widgetDict: {
        abstract: abstract,
        references: references,
        citations: citations,
        coreads: coreads,
        tableOfContents: tableOfContents,
        similar: similar,
        searchBar: resultsWidgetDict.searchBar,
        resources: resources
      }});

      pageControllers.index = new LandingPageController({widgetDict: {searchBar: resultsWidgetDict.searchBar}});

      _.each(pageControllers, function (v, k) {
        v.activate(beehive.getHardenedInstance())
      });

      var masterPageManager = new MasterPageManager({pageControllers: pageControllers, history: bumblebeeHistory});

      masterPageManager.activate(beehive.getHardenedInstance());

      app.router = new Router({pageManager: masterPageManager});
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

        if ($("#landing-page-layout").length > 0) {
          return
        }
        //navbar is currently 40 px height
        if ($(window).scrollTop() > 50) {
          $(".s-search-bar-full-width-container").addClass("s-search-bar-motion");
        }
        else {
          $(".s-search-bar-full-width-container").removeClass("s-search-bar-motion")
        }
      });


    }


  };

  return Mixin;
});
