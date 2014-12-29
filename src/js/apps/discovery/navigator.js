/**
 * The main 'navigation' enpoints (the part executed inside
 * the applicaiton) - this is a companion to the 'router'
 */

define([
    'jquery',
    'backbone',
    'underscore',
    'js/components/navigator',
    'js/components/api_feedback',
    'js/components/api_query_updater'
  ],
  function (
    $,
    Backbone,
    _,
    Navigator,
    ApiFeedback,
    ApiQueryUpdater
    ) {

    "use strict";

    var NavigatorService = Navigator.extend({

      start: function(app) {
        /**
         * These 'transitions' are what happens inside 'discovery' application
         *
         * As a convention, navigation events are all lowercase, and widgets/page managers
         * are CamelCase (for example the table of contents menu on the left side of the
         * abstract/detail page is triggering navigation events using just the name of
         * the widget, e.g. ShowReferences - when References tab was selected)
         *
         */

        var self = this;
        var pubsub = this.pubsub;
        var self = this;
        var queryUpdater = new ApiQueryUpdater('navigator');

        var publishFeedback = function(data) {
          self.pubsub.publish(self.pubSubKey, self.pubsub.FEEDBACK, new ApiFeedback(data))
        };

        var searchPageAlwaysVisible = [
          'Results', 'QueryInfo','AuthorFacet', 'DatabaseFacet', 'RefereedFacet',
          'KeywordFacet', 'BibstemFacet', 'BibgroupFacet', 'DataFacet',
          'VizierFacet', 'GrantsFacet', 'GraphTabs', 'QueryDebugInfo',
          'VisualizationDropdown', 'SearchWidget',
          'Sort', 'AlertsWidget'
        ];

        var detailsPageAlwaysVisible = [
          'TOCWidget', 'SearchWidget', 'ShowResources', 'ShowRecommender', 'AlertsWidget'
        ];

        this.set('index-page', function() {
          app.getObject('MasterPageManager').show('LandingPage');
          var q = app.getObject('AppStorage').getCurrentQuery();
          this.route = '#index/' + queryUpdater.clean(q).url();
        });
        this.set('results-page', function() {
          app.getObject('MasterPageManager').show('SearchPage',
            searchPageAlwaysVisible);
          var q = app.getObject('AppStorage').getCurrentQuery();
          this.route = '#search/' + queryUpdater.clean(q).url();
        });
        this.set('export-page', function() {
          app.getObject('MasterPageManager').show('SearchPage',
            ['Export'].concat(searchPageAlwaysVisible.slice(1)));
        });

        this.set('show-author-network', function() {
          publishFeedback({code: ApiFeedback.CODES.MAKE_SPACE});
          app.getObject('MasterPageManager').show('SearchPage',
            ['AuthorNetwork'].concat(searchPageAlwaysVisible.slice(1)));
        });
        this.set('show-paper-network', function() {
          publishFeedback({code: ApiFeedback.CODES.MAKE_SPACE});
          app.getObject('MasterPageManager').show('SearchPage',
            ['PaperNetwork'].concat(searchPageAlwaysVisible.slice(1)));
        });
        this.set('show-metrics', function() {
          publishFeedback({code: ApiFeedback.CODES.MAKE_SPACE});
          app.getObject('MasterPageManager').show('SearchPage',
            ['Metrics'].concat(searchPageAlwaysVisible.slice(1)));
        });
        this.set("visualization-closed", this.get("results-page"));


        this.set('abstract-page', function() {
          app.getWidget("TOCWidget").collection.selectOne("ShowAbstract");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowAbstract'].concat(detailsPageAlwaysVisible)
          );
          //this.route = '#abs/' + app.getWidget('ShowAbstract').getCurrentQuery().url();
        });
        this.set('ShowAbstract', function(){self.get('abstract-page').execute()});
        this.set('ShowCitations', function() {
          app.getWidget("TOCWidget").collection.selectOne("ShowCitations");
          app.getObject('MasterPageManager').show('DetailsPage', ['ShowCitations'].concat(detailsPageAlwaysVisible));
        });
        this.set('ShowReferences', function() {
          app.getWidget("TOCWidget").collection.selectOne("ShowReferences");
          app.getObject('MasterPageManager').show('DetailsPage', ['ShowReferences'].concat(detailsPageAlwaysVisible));
        });
        this.set('ShowCoreads', function() {
          app.getWidget("TOCWidget").collection.selectOne("ShowCoreads");
          app.getObject('MasterPageManager').show('DetailsPage', ['ShowCoreads'].concat(detailsPageAlwaysVisible));
        });
        this.set('ShowTableOfContents', function() {
          app.getWidget("TOCWidget").collection.selectOne("ShowTableOfContents");
          app.getObject('MasterPageManager').show('DetailsPage', ['ShowTableOfContents'].concat(detailsPageAlwaysVisible));
        });
        this.set('ShowSimilar', function() {
          app.getWidget("TOCWidget").collection.selectOne("ShowSimilar").set("isActive", true);
          app.getObject('MasterPageManager').show('DetailsPage', ['TOCWidget', 'ShowSimilar', 'SearchWidget', 'ShowResources',  'ShowRecommender']);
        });
        this.set('abstract-page:bibtex', function() { app.getObject('MasterPageManager').show('DetailsPage')});
        this.set('abstract-page:endnote', function() { app.getObject('MasterPageManager').show('DetailsPage')});
        this.set('abstract-page:metrics', function() { app.getObject('MasterPageManager').show('DetailsPage')});
      }


    });

    return NavigatorService;

  });
