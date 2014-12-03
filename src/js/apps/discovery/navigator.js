/**
 * The main 'navigation' enpoints (the part executed inside
 * the applicaiton) - this is a companion to the 'router'
 */

define([
    'jquery',
    'backbone',
    'underscore',
    'js/components/navigator',
    'js/components/api_feedback'
  ],
  function (
    $,
    Backbone,
    _,
    Navigator,
    ApiFeedback
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
        var pubsub = this.pubsub;
        var self = this;
        pubsub.subscribe(this.pubSubKey, pubsub.START_SEARCH, function() {
          self.navigate('results-page')
        });

        var publishFeedback = function(data) {
          self.pubsub.publish(self.pubSubKey, self.pubsub.FEEDBACK, new ApiFeedback(data))
        };

        var searchPageAlwaysVisible = [
          'Results', 'QueryInfo','AuthorFacet', 'DatabaseFacet', 'RefereedFacet',
          'KeywordFacet', 'BibstemFacet', 'BibgroupFacet', 'DataFacet',
          'VizierFacet', 'GrantsFacet', 'GraphTabs', 'QueryDebugInfo',
          'VisualizationDropdown', 'SearchWidget'];

        this.set('index-page', function() {
          app.getObject('MasterPageManager').show('LandingPage');
          this.route = '#search/' + app.getWidget('SearchWidget').getCurrentQuery().url();
        });
        this.set('results-page', function() {
          app.getObject('MasterPageManager').show('SearchPage',
          searchPageAlwaysVisible);
          this.route = '#search/' + app.getWidget('SearchWidget').getCurrentQuery().url();
        });

        this.set('show-author-network', function() {
          publishFeedback({code: ApiFeedback.CODES.MAKE_SPACE});
          app.getObject('MasterPageManager').show('SearchPage',
            ['AuthorNetwork'].concat(searchPageAlwaysVisible.slice(1)));

        });
        this.set("visualization-closed", this.get("results-page"));
        this.set('abstract-page', function() {
          app.getObject('MasterPageManager').show('DetailsPage',
            ['TOCWidget', 'ShowAbstract', 'SearchWidget', 'ShowResources']);
          //this.route = '#abs/' + app.getWidget('ShowAbstract').getCurrentQuery().url();
        });
        this.set('ShowAbstract', this.get('abstract-page'));
        this.set('ShowCitations', function() {
          app.getObject('MasterPageManager').show('DetailsPage', ['TOCWidget', 'ShowCitations', 'SearchWidget', 'ShowResources']);
        });
        this.set('ShowReferences', function() {
          app.getObject('MasterPageManager').show('DetailsPage', ['TOCWidget', 'ShowReferences', 'SearchWidget', 'ShowResources']);
        });
        this.set('ShowCoreads', function() {
          app.getObject('MasterPageManager').show('DetailsPage', ['TOCWidget', 'ShowCoreads', 'SearchWidget', 'ShowResources']);
        });
        this.set('ShowTableOfContents', function() {
          app.getObject('MasterPageManager').show('DetailsPage', ['TOCWidget', 'ShowTableOfContents', 'SearchWidget', 'ShowResources']);
        });
        this.set('ShowSimilar', function() {
          app.getObject('MasterPageManager').show('DetailsPage', ['TOCWidget', 'ShowSimilar', 'SearchWidget', 'ShowResources']);
        });
        this.set('abstract-page:bibtex', function() { app.getObject('MasterPageManager').show('DetailsPage')});
        this.set('abstract-page:endnote', function() { app.getObject('MasterPageManager').show('DetailsPage')});
        this.set('abstract-page:metrics', function() { app.getObject('MasterPageManager').show('DetailsPage')});
      }


    });

    return NavigatorService;

  });
