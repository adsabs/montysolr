/**
 * The main 'navigation' enpoints (the part executed inside
 * the applicaiton) - this is a companion to the 'router'
 */

define([
    'jquery',
    'backbone',
    'js/components/navigator'],
  function ($, Backbone, Navigator) {

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
        pubsub.subscribe(this.pubSubKey, pubsub.START_SEARCH, function() {self.navigate('results-page')});

        this.set('index-page', function() {app.getObject('MasterPageManager').show('LandingPage')});
        this.set('results-page', function() { app.getObject('MasterPageManager').show('SearchPage',
          ['Results', 'QueryInfo','AuthorFacet', 'DatabaseFacet', 'RefereedFacet', 'KeywordFacet',
            'BibstemFacet', 'BibgroupFacet', 'DataFacet', 'VizierFacet', 'GrantsFacet', 'GraphTabs',
            'QueryDebugInfo', 'VisualizationDropdown', 'SearchWidget']);
          app.getWidget("SearchPage").view.returnColWidthsToDefault();

        });
        this.set('show-author-network', function() { app.getObject('MasterPageManager').show('SearchPage',
          ['AuthorNetwork','QueryInfo','AuthorFacet', 'DatabaseFacet', 'RefereedFacet', 'KeywordFacet',
            'BibstemFacet', 'BibgroupFacet', 'DataFacet', 'VizierFacet', 'GrantsFacet', 'GraphTabs',
            'QueryDebugInfo', 'VisualizationDropdown', 'SearchWidget']);
          app.getWidget("SearchPage").view.makeCenterFullWidth();
        });
        this.set("visualization-closed", this.get("results-page"));
        this.set('abstract-page', function() {
          app.getObject('MasterPageManager').show('DetailsPage', ['TOCWidget', 'ShowAbstract', 'SearchWidget', 'ShowResources']);
        });
        this.set('ShowAbstract', function() {
          app.getObject('MasterPageManager').show('DetailsPage', ['TOCWidget', 'ShowAbstract', 'SearchWidget', 'ShowResources']);
        });
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
