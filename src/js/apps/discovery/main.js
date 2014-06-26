
define(["config", 'module'], function(config, module) {

  // Kick off the application
  require(["router", 'js/components/application'], function(Router, Application) {

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
        api.url = conf.root + api.url;
        app.root = conf.root;
      }
      if (conf.debug !== undefined) {
        app.getObject('QueryMediator').debug = conf.debug;
      }

      // XXX:rca - this will need to be moved somewhere else (it is getting confusing -- to long)

      // create composite widgets
      var LayoutBuilder = app.getModule('LayoutBuilder');
      var displayDocs = new LayoutBuilder({widgetTitleMapping : {'abstract' : {widget: app.getWidget('Abstract'), default : true}}});

      var FacetFactory = app.getModule('FacetFactory');


      var authorFacets = FacetFactory.makeHierarchicalCheckboxFacet({
        facetField: "author_facet_hier",
        facetTitle: "Authors",
        openByDefault: true,
        logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']},
        responseProcessors: [
          function(v) {var vv = v.split('/'); return vv[vv.length-1]}
        ]
      });

      // XXX:rca - another hack
      authorFacets.handleLogicalSelection = function(operator) {
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

      var keywords = FacetFactory.makeBasicCheckboxFacet({
        facetField: "keyword_facet",
        facetTitle: "Keywords",
        openByDefault: false,
        logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}
      });

      var database = FacetFactory.makeBasicCheckboxFacet({
        facetField: "database",
        facetTitle: "Collections",
        openByDefault: true,
        logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}

      });
      var data = FacetFactory.makeBasicCheckboxFacet({
        facetField: "data_facet",
        facetTitle: "Data",
        openByDefault: false,
        logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}

      });

      var vizier = FacetFactory.makeBasicCheckboxFacet({
        facetField: "vizier_facet",
        facetTitle: "Vizier Tables",
        openByDefault: false,
        logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}

      });

      var pub = FacetFactory.makeBasicCheckboxFacet({
        facetField: "bibstem_facet",
        facetTitle: "Publications",
        openByDefault: false,
        logicOptions: {single: ['limit to', 'exclude'], multiple: ["or", "exclude"]}
      });
      var bibgroup = FacetFactory.makeBasicCheckboxFacet({
        facetField: "bibgroup_facet",
        facetTitle: "Bib Groups",
        openByDefault: false,
        logicOptions: {single: ['limit to', 'exclude'], multiple: ["or", "exclude"]}
      });

      var grants = FacetFactory.makeHierarchicalCheckboxFacet({
        facetField: "grant_facet_hier",
        facetTitle: "Grants",
        openByDefault: false,
        logicOptions: {single: ['limit to', 'exclude'], multiple: ["or", "exclude"]},
        responseProcessors: [
          function(v) {var vv = v.split('/'); return vv[vv.length-1]}
        ]
      });

      grants.handleLogicalSelection = function(operator) {
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


      var refereed = FacetFactory.makeBasicCheckboxFacet({
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

      refereed.handleLogicalSelection = function(operator) {
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


      _.each(app.getWidget('GraphTabs').widgets, function(w){
        w.activate(beehive.getHardenedInstance());
      });
      authorFacets.activate(beehive.getHardenedInstance());
      database.activate(beehive.getHardenedInstance());
      keywords.activate(beehive.getHardenedInstance());
      pub.activate(beehive.getHardenedInstance());
      bibgroup.activate(beehive.getHardenedInstance());
      data.activate(beehive.getHardenedInstance());
      vizier.activate(beehive.getHardenedInstance());
      grants.activate(beehive.getHardenedInstance());
      refereed.activate(beehive.getHardenedInstance());

      authorFacets.activate(beehive.getHardenedInstance());
      database.activate(beehive.getHardenedInstance());

      $("#top")
        .append(app.getWidget('SearchBar').render().el);

      $("#s-middle-col-container")
        .append(app.getWidget('Results').render().el)
        .append(displayDocs.render().el);

      $("#s-facet-container")
        .append(authorFacets.render().el)
        .append(database.render().el)
        .append(refereed.render().el)
        .append(keywords.render().el)
        .append(pub.render().el)
        .append(bibgroup.render().el)
        .append(data.render().el)
        .append(vizier.render().el)
        .append(grants.render().el);


      $("#s-right-col-container")
        .append(app.getWidget('QueryInfo').render().el)
        .append(app.getWidget('GraphTabs').render().el)
        .append(app.getWidget('QueryDebugInfo').render().el);

      app.router = new Router();
      app.router.activate(beehive.getHardenedInstance());

      // Trigger the initial route and enable HTML5 History API support, set the
      // root folder to '/' by default.  Change in app.js.
      var histOpts = {}
      if (conf.root) {
        histOpts['root'] = conf.root;
      }
      Backbone.history.start(histOpts);

    });


  });
});
