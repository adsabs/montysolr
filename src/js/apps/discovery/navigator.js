/**
 * The main 'navigation' endpoints (the part executed inside
 * the applicaiton) - this is a companion to the 'router'
 */

define([
    'jquery',
    'backbone',
    'underscore',
    'js/components/navigator',
    'js/components/api_feedback',
    'js/components/api_query_updater',
    'js/components/json_response',
    'js/components/api_query',
    'js/components/api_request',
    'js/components/api_targets'
  ],
  function (
    $,
    Backbone,
    _,
    Navigator,
    ApiFeedback,
    ApiQueryUpdater,
    JsonResponse,
    ApiQuery,
    ApiRequest,
    ApiTargets
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
        var queryUpdater = new ApiQueryUpdater('navigator');

        var publishFeedback = function(data) {
          self.pubsub.publish(self.pubSubKey, self.pubsub.FEEDBACK, new ApiFeedback(data))
        };

        var searchPageAlwaysVisible = [
          'Results', 'QueryInfo','AuthorFacet', 'DatabaseFacet', 'RefereedFacet',
          'KeywordFacet', 'BibstemFacet', 'BibgroupFacet', 'DataFacet',
          'VizierFacet', 'GrantsFacet', 'GraphTabs', 'QueryDebugInfo',
          'ExportDropdown', 'VisualizationDropdown', 'SearchWidget',
          'Sort', 'AlertsWidget'
        ];

        var detailsPageAlwaysVisible = [
          'TOCWidget', 'SearchWidget', 'ShowResources', 'ShowRecommender', 'AlertsWidget', 'ShowGraphicsSidebar'
        ];

        this.set('index-page', function() {
          app.getObject('MasterPageManager').show('LandingPage');
          var q = app.getObject('AppStorage').getCurrentQuery();
          this.route = '#index/' + queryUpdater.clean(q).url();
        });

        this.set('settings-page', function(page, data, key){
          var subView = data.subView;
          subView = subView ? subView : "preferences";
          var loggedIn = app.getBeeHive().getObject("User").isLoggedIn();

          if (!loggedIn){
            //redirect to authentication page
            app.getObject('MasterPageManager').show("AuthenticationPage",
              ['Authentication', 'AlertsWidget']);
            app.getWidget("Authentication").setSubView("login");
            this.route = "#user/account/login"
          }
          else {
            app.getWidget("UserSettings").setSubView(subView);
            this.route = "#user/settings/"+subView;
            app.getObject('MasterPageManager').show("SettingsPage",
              ['UserSettings', 'AlertsWidget']);
          }
        });

        this.set('authentication-page', function(page, data, key){
          var subView = data.subView;
          subView = subView ? subView : "login";
          var loggedIn = app.getBeeHive().getObject("User").isLoggedIn();

          if (loggedIn){
            //redirect to preferences
            app.getObject('MasterPageManager').show("SettingsPage",
              ['UserSettings', 'AlertsWidget']);
            app.getWidget("UserSettings").setSubView("preferences");
            this.route = "#user/settings/preferences"
          }
          else {
            app.getWidget("Authentication").setSubView(subView);
            this.route = "#user/account/"+subView;
            app.getObject('MasterPageManager').show("AuthenticationPage",
              ['Authentication', 'AlertsWidget']);
            }
        });

        this.set('results-page', function() {
          app.getObject('MasterPageManager').show('SearchPage',
            searchPageAlwaysVisible);
          var q = app.getObject('AppStorage').getCurrentQuery();
          this.route = '#search/' + queryUpdater.clean(q).url();
          publishFeedback({code: ApiFeedback.CODES.UNMAKE_SPACE});
        });

        this.set('export-bibtex', function(format, options) {
          publishFeedback({code: ApiFeedback.CODES.MAKE_SPACE});
          self.get('export-page').execute('bibtex', options);
        });
        this.set('export-aastex', function(format, options) {
          publishFeedback({code: ApiFeedback.CODES.MAKE_SPACE});
          self.get('export-page').execute('aastex', options);
        });
        this.set('export-endnote', function(format, options) {
          publishFeedback({code: ApiFeedback.CODES.MAKE_SPACE});
          self.get('export-page').execute('endnote', options);
        });
        this.set("export-classic", function(format, options){
          self.get('export-page').execute('classic', options);
        })
        this.set('export-page', function(format, options) {

          format = format || 'bibtex';
          var storage = app.getObject('AppStorage');
          var widget = app.getWidget('ExportWidget');

          //classic is a special case, it opens in a new tab
          if (format == "classic"){
            if (options.onlySelected && storage.hasSelectedPapers()) {
              widget.openClassicExports({bibcodes: storage.getSelectedPapers()});
            }
            else {
              widget.openClassicExports({currentQuery : storage.getCurrentQuery()});
            }
            return
          }

          // only selected records requested
          if (options.onlySelected && storage.hasSelectedPapers()) {
            widget.exportRecords(format, storage.getSelectedPapers());
          }
          //all records specifically requested
          else if (options.onlySelected === false && storage.hasCurrentQuery()){
            widget.exportQuery({format : format,  currentQuery: storage.getCurrentQuery(),  numFound : storage.get("numFound")});
          }
          // no request for selected or not selected, show selected
          else if (options.onlySelected === undefined && storage.hasSelectedPapers()){
            widget.exportRecords(format, storage.getSelectedPapers());
          }
          //no selected, show all papers
          else if(storage.hasCurrentQuery()) {
            widget.exportQuery({format : format,  currentQuery: storage.getCurrentQuery(),  numFound : storage.get("numFound")});
          }
          else {
            var alerts = app.getController('AlertsController');
            alerts.alert({msg: 'There are no records to export yet (please search or select some)'});
            this.get('results-page')();
            return;
          }

          app.getObject('MasterPageManager').show('SearchPage',
            ['ExportWidget'].concat(searchPageAlwaysVisible.slice(1)));
        });

        this.set('export-query', function() {
          var api = app.getService('Api');
          var q = app.getObject('AppStorage').getCurrentQuery();
          var alerter = app.getController('AlertsController');

          // TODO: modify filters (move them to the main q)
          q = new ApiQuery({query: q.url()});

          //save the query / obtain query id
          api.request(new ApiRequest({
            query: q,
            target: ApiTargets.MYADS_STORAGE + '/query',
            options: {
              done: function(){},
              type: 'POST',
              xhrFields: {
                withCredentials: false
              }
            }
          }))
            .done(function(data) {
              alerter.alert(new ApiFeedback({
                code: ApiFeedback.CODES.ALERT,
                msg: 'The query has been saved. You can insert the following snippet in a webpage: <br/>' + '<img src="' + ApiTargets.MYADS_STORAGE + '/query2svg/' + data.qid + '"></img><br/>' +
                  '<br/><textarea rows="10" cols="50">' +
                  '<a href="' + location.protocol + '//' + location.host + location.pathname +
                  '#execute-query/' + data.qid + '"><img src="' + ApiTargets.MYADS_STORAGE + '/query2svg/' + data.qid + '"></img>' +
                  '</a>' +
                  '</textarea>'
                ,
                modal: true
              }));
            });
        });

        this.set('execute-query', function(endPoint, queryId) {
          var api = app.getService('Api');
          api.request(new ApiRequest({
            target: ApiTargets.MYADS_STORAGE + '/query/' + queryId,
            options: {
              done: function(data){
                var q = new ApiQuery().load(JSON.parse(data.query).query);
                self.pubsub.publish(self.pubSubKey, self.pubsub.START_SEARCH, q);
              },
              fail: function() {
                var alerter = app.getController('AlertsController');
                alerter.alert(new ApiFeedback({
                  code: ApiFeedback.CODES.ERROR,
                  msg: 'The query with the given UUID cannot be found'
                }));
                self.get('index-page').execute();
              },
              type: 'GET',
              xhrFields: {
                withCredentials: false
              }
            }
          }))
        });

        this.set('orcid-page', function(view, targetRoute) {

          var orcidApi = app.getService('OrcidApi');

          // traffic from Orcid - user has authorized our access
          if (orcidApi.hasExchangeCode() && !orcidApi.hasAccess()) {
            orcidApi.getAccessData(orcidApi.getExchangeCode())
            .done(function(data) {
                orcidApi.saveAccessData(data);
                self.pubsub.publish(self.pubSubKey, self.pubsub.APP_EXIT, {url: window.location.pathname +
                  ((targetRoute && _.isString(targetRoute)) ? targetRoute : window.location.hash)});
            })
            .fail(function() {
                console.warn('Unsuccessful login to ORCID');
                self.get('index-page').execute();
                var alerter = app.getController('AlertsController');
                alerter.alert(new ApiFeedback({
                  code: ApiFeedback.CODES.ALERT,
                  msg: 'Error getting OAuth code to access ORCID',
                  modal: true
                }));
            });
            return;
          }

          this.route = '#user/orcid';

          var orcidWidget = app.getWidget('OrcidBigWidget');
          if (orcidWidget) {
              app.getObject('MasterPageManager').show('SearchPage',
                ['OrcidBigWidget', 'SearchWidget', 'AlertsWidget']);
          }
          else {
            self.pubsub.publish(self.pubSubKey, self.pubsub.NAVIGATE, 'index-page');
          }

        });

        this.set('show-author-network', function() {
          publishFeedback({code: ApiFeedback.CODES.MAKE_SPACE});
          app.getObject('MasterPageManager').show('SearchPage',
            ['AuthorNetwork'].concat(searchPageAlwaysVisible.slice(1)));
        });
        this.set('show-wordcloud', function() {
          publishFeedback({code: ApiFeedback.CODES.MAKE_SPACE});
          app.getObject('MasterPageManager').show('SearchPage',
            ['WordCloud'].concat(searchPageAlwaysVisible.slice(1)));

        });
        this.set('show-paper-network', function() {
          publishFeedback({code: ApiFeedback.CODES.MAKE_SPACE});
          app.getObject('MasterPageManager').show('SearchPage',
            ['PaperNetwork'].concat(searchPageAlwaysVisible.slice(1)));
        });
        this.set('show-bubble-chart', function() {
          publishFeedback({code: ApiFeedback.CODES.MAKE_SPACE});
          app.getObject('MasterPageManager').show('SearchPage',
            ['BubbleChart'].concat(searchPageAlwaysVisible.slice(1)));
        });
        this.set('show-metrics', function() {
          publishFeedback({code: ApiFeedback.CODES.MAKE_SPACE});
          app.getObject('MasterPageManager').show('SearchPage',
            ['Metrics'].concat(searchPageAlwaysVisible.slice(1)));
        });
        this.set("visualization-closed", this.get("results-page"));


        this.set('abstract-page', function(pageName, bibcode) {
          app.getWidget("TOCWidget").collection.selectOne("ShowAbstract");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowAbstract'].concat(detailsPageAlwaysVisible)
          );
          if (bibcode)
            this.route = '#abs/' + bibcode;
        });

        this.set('ShowAbstract', function(){
          self.get('abstract-page').execute();
          this.route = arguments[1];
        });
        this.set('ShowCitations', function() {
          app.getWidget("TOCWidget").collection.selectOne("ShowCitations");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowCitations'].concat(detailsPageAlwaysVisible));
          this.route = arguments[1];
        });
        this.set('ShowReferences', function() {
          app.getWidget("TOCWidget").collection.selectOne("ShowReferences");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowReferences'].concat(detailsPageAlwaysVisible));
          this.route = arguments[1];
        });
        this.set('ShowCoreads', function() {
          app.getWidget("TOCWidget").collection.selectOne("ShowCoreads");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowCoreads'].concat(detailsPageAlwaysVisible));
          this.route = arguments[1];
        });
        this.set('ShowTableOfContents', function() {
          app.getWidget("TOCWidget").collection.selectOne("ShowTableOfContents");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowTableOfContents'].concat(detailsPageAlwaysVisible));
          this.route = arguments[1];
        });
        this.set('ShowSimilar', function() {
          app.getWidget("TOCWidget").collection.selectOne("ShowSimilar");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowSimilar'].concat(detailsPageAlwaysVisible));
          this.route = arguments[1];
        });
        this.set('ShowPaperMetrics', function() {
          app.getWidget("TOCWidget").collection.selectOne("ShowPaperMetrics");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowPaperMetrics'].concat(detailsPageAlwaysVisible));
        });
        this.set('ShowGraphics', function() {
          app.getWidget("TOCWidget").collection.selectOne("ShowGraphics");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowGraphics'].concat(detailsPageAlwaysVisible));
        });

        this.set('abstract-page:bibtex', function() { app.getObject('MasterPageManager').show('DetailsPage')});
        this.set('abstract-page:endnote', function() { app.getObject('MasterPageManager').show('DetailsPage')});
        this.set('abstract-page:metrics', function() { app.getObject('MasterPageManager').show('DetailsPage')});
      }


    });

    return NavigatorService;

  });
