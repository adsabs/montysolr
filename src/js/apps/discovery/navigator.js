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

        var self = this;
        var queryUpdater = new ApiQueryUpdater('navigator');

        var publishFeedback = function(data) {
          self.pubsub.publish(self.pubSubKey, self.pubsub.FEEDBACK, new ApiFeedback(data))
        };

        //right now, user navbar widget depends on this to show the correct highlighted pill
        var publishPageChange = function(pageName){
          self.pubsub.publish(self.pubSubKey, self.pubsub.PAGE_CHANGE, pageName);
        };

        var searchPageAlwaysVisible = [
          'Results', 'QueryInfo','AuthorFacet', 'DatabaseFacet', 'RefereedFacet',
          'KeywordFacet', 'BibstemFacet', 'BibgroupFacet', 'DataFacet',
          'VizierFacet', 'GrantsFacet', 'GraphTabs', 'QueryDebugInfo',
          'ExportDropdown', 'VisualizationDropdown', 'SearchWidget',
          'Sort'
        ];

        var detailsPageAlwaysVisible = [
          'TOCWidget', 'SearchWidget', 'ShowResources', 'ShowRecommender', 'ShowGraphicsSidebar'
        ];

        function redirectIfNotSignedIn(){
          var loggedIn = app.getBeeHive().getObject("User").isLoggedIn();
          if (!loggedIn){
            //redirect to authentication page
            app.getService("Navigator").navigate("authentication-page");
            return true;
          }
          else {
            return false;
          }
        };

        this.set('index-page', function() {
          app.getObject('MasterPageManager').show('LandingPage');
          var q = app.getObject('AppStorage').getCurrentQuery();
          this.route = '#index/' + queryUpdater.clean(q).url();
        });


        //request for the widget
        this.set("UserSettings", function(page, data){
          if (redirectIfNotSignedIn())
            return;
          var subView = data.subView || "token";
          //set left hand nav panel correctly and tell the view what to show
          app.getWidget("SettingsPage").setActive("UserSettings",  subView);
          app.getObject('MasterPageManager').show("SettingsPage",
            ['UserSettings', "UserNavbarWidget"]);

          this.route = "#user/settings/"+subView;
          publishPageChange("settings-page");

        });

        //request for the widget
        this.set("UserPreferences", function(page, data){
          if (redirectIfNotSignedIn())
            return;
          //set left hand nav panel correctly and tell the view what to show
          app.getWidget("SettingsPage").setActive("UserPreferences");
          app.getObject('MasterPageManager').show("SettingsPage",
            ['UserPreferences', "UserNavbarWidget"]);

          this.route = "#user/settings/preferences";
          publishPageChange("settings-page");

        });

        this.set("AllLibrariesWidget", function(widget, subView){

          var subView = subView || "libraries";

          app.getWidget("AllLibrariesWidget").setSubView(subView);
          app.getObject('MasterPageManager').show("LibrariesPage",
            ["AllLibrariesWidget", "UserNavbarWidget"]);

          this.route = "#user/libraries/";
          publishPageChange("libraries-page");

        });

        this.set("IndividualLibraryWidget", function(widget, data){

          var sub = data.sub, id = data.id;

          if (!_.contains(["library", "admin", "metrics", "export", "visualization"], sub)){
            throw new Error("no valid subview provided to individual library widget");
          }

          app.getWidget("IndividualLibraryWidget").setSubView(sub, id);
          app.getObject('MasterPageManager').show("LibrariesPage",
            ["IndividualLibraryWidget", "UserNavbarWidget"]);

          var lastSlash = (sub == "library") ? "" : "/admin";

          this.route = "#user/libraries/" + id + lastSlash;
          publishPageChange("libraries-page");

        });

        this.set("library-export", function(widget, data){

        //first, tell export widget what to show
        if (data.bibcodes && data.bibcodes.length) {

          app.getWidget("ExportWidget").exportRecords(data.sub, data.bibcodes);
          //then, set library tab to proper field
          app.getWidget("IndividualLibraryWidget").setSubView("export");

        }
        else if (data.id){

          app.getObject("LibraryController").getLibraryRecords(data.id).done(function(bibcodes){

            bibcodes = bibcodes.documents;
            app.getWidget("ExportWidget").exportRecords(data.sub, bibcodes);
            //then, set library tab to proper field
            app.getWidget("IndividualLibraryWidget").setSubView("export", data.id);

          });

        }
        else {
          throw new Error("neither an identifying id for library nor the bibcodes themselves were provided to export widget");
          return
        }

        //then, show library page manager
          app.getObject('MasterPageManager').show("LibrariesPage",
            ["IndividualLibraryWidget", "UserNavbarWidget", "ExportWidget"]);

          publishPageChange("libraries-page");

          this.route = "#user/libraries/" + data.id + "/export/" + data.sub;

        });

        this.set("library-metrics", function(widget, data){

          //first, tell export widget what to show
          if (data.bibcodes && data.bibcodes.length) {

            app.getWidget("Metrics").showMetricsForListOfBibcodes(data.bibcodes);
            //then, set library tab to proper field
            app.getWidget("IndividualLibraryWidget").setSubView("metrics");
          }
          else if (data.id){

            app.getObject("LibraryController").getLibraryRecords(data.id).done(function(bibcodes){
              bibcodes = bibcodes.documents;
              app.getWidget("Metrics").showMetricsForListOfBibcodes(bibcodes);
              //then, set library tab to proper field
              app.getWidget("IndividualLibraryWidget").setSubView("metrics", data.id);
            });

          }
          else {
            throw new Error("neither an identifying id for library nor the bibcodes themselves were provided to export widget");
            return
          }

          //then, show library page manager
          app.getObject('MasterPageManager').show("LibrariesPage",
            ["IndividualLibraryWidget", "UserNavbarWidget", "Metrics"]);

          this.route = "#user/libraries/" + data.id + "/metrics";

          publishPageChange("libraries-page");

        });

        this.set("home-page", function(){
          app.getObject('MasterPageManager').show("HomePage",
            []);
          publishPageChange("home-page");

        });

        this.set('authentication-page', function(page, data){
          var subView = data.subView;
          subView = subView ? subView : "login";
          var loggedIn = app.getBeeHive().getObject("User").isLoggedIn();

          if (loggedIn){
            //redirect to preferences
            app.getObject('MasterPageManager').show("SettingsPage",
              ['UserSettings']);
            app.getWidget("UserSettings").setSubView("preferences");
            this.route = "#user/settings/preferences"
          }
          else {
            app.getWidget("Authentication").setSubView(subView);
            this.route = "#user/account/" + subView;
            app.getObject('MasterPageManager').show("AuthenticationPage",
              ['Authentication']);
          }
        });

        this.set('results-page', function() {

          app.getObject('MasterPageManager').show('SearchPage',
            searchPageAlwaysVisible);
          var q = app.getObject('AppStorage').getCurrentQuery();
          this.route = '#search/' + queryUpdater.clean(q).url();
          publishFeedback({code: ApiFeedback.CODES.UNMAKE_SPACE});
        });

        this.set('export', function(nav, options) {

          var format = options.format || 'bibtex';
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

          //first, open central panel
          publishFeedback({code: ApiFeedback.CODES.MAKE_SPACE});

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
              ['OrcidBigWidget', 'SearchWidget']);
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

          app.getWidget("Metrics").showMetricsForCurrentQuery();
          app.getObject('MasterPageManager').show('SearchPage',
            ['Metrics'].concat(searchPageAlwaysVisible.slice(1)));
        });
        this.set("visualization-closed", this.get("results-page"));


        this.set('ShowAbstract', function(id, data){
          app.getWidget("DetailsPage").setActive("ShowAbstract");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowAbstract'].concat(detailsPageAlwaysVisible));
          this.route = data.href;
        });
        this.set('ShowCitations', function(id, data) {
          //set left hand nav panel correctly
          app.getWidget("DetailsPage").setActive("ShowCitations");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowCitations'].concat(detailsPageAlwaysVisible));
          this.route = data.href;
        });
        this.set('ShowReferences', function(id, data ) {
          //set left hand nav panel correctly
          app.getWidget("DetailsPage").setActive("ShowReferences");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowReferences'].concat(detailsPageAlwaysVisible));
          this.route = data.href;
        });
        this.set('ShowCoreads', function(id, data) {
          //set left hand nav panel correctly
          app.getWidget("DetailsPage").setActive("ShowCoreads");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowCoreads'].concat(detailsPageAlwaysVisible));
          this.route = data.href;
        });
        this.set('ShowTableOfContents', function(id, data) {
          //set left hand nav panel correctly
          app.getWidget("DetailsPage").setActive("ShowTableOfContents");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowTableOfContents'].concat(detailsPageAlwaysVisible));
          this.route = data.href;
        });
        this.set('ShowSimilar', function(id, data) {
          //set left hand nav panel correctly
          app.getWidget("DetailsPage").setActive("ShowSimilar");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowSimilar'].concat(detailsPageAlwaysVisible));
          this.route = data.href;
        });
        this.set('ShowPaperMetrics', function() {
          //set left hand nav panel correctly
          app.getWidget("DetailsPage").setActive("ShowPaperMetrics");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowPaperMetrics'].concat(detailsPageAlwaysVisible));
        });
        this.set("ShowPaperExport", function(funcName, data){
          var format = data.subView;
          //set left hand nav panel correctly
          app.getWidget("DetailsPage").setActive("ShowPaperExport", format);
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowPaperExport'].concat(detailsPageAlwaysVisible));
//          this.route = data.href;
        });
        this.set('ShowGraphics', function() {
          app.getWidget("DetailsPage").setActive("ShowGraphics");
          app.getObject('MasterPageManager').show('DetailsPage',
            ['ShowGraphics'].concat(detailsPageAlwaysVisible));
        });
      }


    });

    return NavigatorService;

  });
