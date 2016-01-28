/**
* The main 'navigation' endpoints (the part executed inside
* the application) - this is a companion to the 'router'
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
    'js/components/api_targets',
    'hbs!./../../../404',
    'hbs!./templates/orcid-modal-template'
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
    ApiTargets,
    ErrorTemplate,
    OrcidModalTemplate

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
          self.getPubSub().publish(self.getPubSub().FEEDBACK, new ApiFeedback(data))
        };

        //right now, user navbar widget depends on this to show the correct highlighted pill
        var publishPageChange = function(pageName){
          self.getPubSub().publish(self.getPubSub().PAGE_CHANGE, pageName);
        };

        var searchPageAlwaysVisible = [
          'Results', 'QueryInfo','AuthorFacet', 'DatabaseFacet', 'RefereedFacet',
          'KeywordFacet', 'BibstemFacet', 'BibgroupFacet', 'DataFacet',
          'VizierFacet', 'GrantsFacet', 'GraphTabs', 'QueryDebugInfo',
          'ExportDropdown', 'VisualizationDropdown', 'SearchWidget',
          'Sort', 'BreadcrumbsWidget'
        ];

        var detailsPageAlwaysVisible = [
          'TOCWidget', 'SearchWidget', 'ShowResources', 'ShowRecommender', 'ShowGraphicsSidebar', 'ShowLibraryAdd'
        ];

        function redirectIfNotSignedIn(){
          var loggedIn = app.getBeeHive().getObject("User").isLoggedIn();
          if (!loggedIn){
            //redirect to authentication page
            app.getService("Navigator").navigate("authentication-page", {subView : "login"});
            return true;
          }
          else {
            return false;
          }
        };

        this.set('index-page', function() {

          app.getObject('MasterPageManager').show('LandingPage', ["SearchWidget"]);
          app.getWidget("LandingPage").done(function(widget) {
            widget.setActive("SearchWidget");
          });
          this.route = "";
        });

        this.set('SearchWidget', function() {
          //you must set a route within the function, even if you are calling
          //another function that sets a route
          this.route = "";
          self.get('index-page').execute();
        });


        this.set("404", function(){
          $("#body-template-container").html(ErrorTemplate());
          this.route = '#404';
        });


        this.set('ClassicSearchForm', function() {
          app.getObject('MasterPageManager').show('LandingPage', ["ClassicSearchForm"]);
          app.getWidget("LandingPage").done(function(widget){widget.setActive("ClassicSearchForm")});
          this.route = "#classic-form";

        });

        this.set('PaperSearchForm', function() {
          app.getObject('MasterPageManager').show('LandingPage', ['PaperSearchForm']);
          app.getWidget("LandingPage").done(function(widget){widget.setActive("PaperSearchForm");});
          this.route = "#paper-form";
        });


        //request for the widget
        this.set("UserSettings", function(page, data){
          if (redirectIfNotSignedIn())
            return;
          var subView = data.subView || "token";
          //set left hand nav panel correctly and tell the view what to show
          app.getObject('MasterPageManager').show("SettingsPage",
            ['UserSettings', "UserNavbarWidget"]);
          app.getWidget("SettingsPage")
           .done(function(widget) {
             widget.setActive("UserSettings",  subView);
           });

          this.route = "#user/settings/"+subView;
          publishPageChange("settings-page");

        });

        //request for the widget
        this.set("UserPreferences", function(page, data){
          if (redirectIfNotSignedIn())
            return;
          var subView = data.subView || "librarylink";
          //set left hand nav panel correctly and tell the view what to show
          app.getObject('MasterPageManager').show("SettingsPage",
            ['UserPreferences', "UserNavbarWidget"]);
          app.getWidget("SettingsPage").done(function(widget) {
              widget.setActive("UserPreferences", subView);
            });

          this.route = "#user/settings/" + subView;

          publishPageChange("settings-page");

        });

        this.set("AllLibrariesWidget", function(widget, subView){

          var subView = subView || "libraries";
          app.getObject('MasterPageManager').show("LibrariesPage",
            ["AllLibrariesWidget", "UserNavbarWidget"]);
          app.getWidget("AllLibrariesWidget").done(function(widget) {
            widget.setSubView({view : subView});
          });

          this.route = "#user/libraries/";
          publishPageChange("libraries-page");

        });

        this.set("IndividualLibraryWidget", function(widget, data) {
          
          //where view is an object in the form
          // {subView: sub, id: id, publicView : false}

          data.publicView = data.publicView ? data.publicView : false;

          if (!_.contains(["library", "admin", "metrics", "export", "visualization"], data.subView )){
            throw new Error("no valid subview provided to individual library widget");
          }

          if ( data.publicView ){
            app.getWidget("IndividualLibraryWidget").done(function(widget) {
              widget.setSubView(data);
              //then, show library page manager
              app.getObject('MasterPageManager').show("PublicLibrariesPage",
                ["IndividualLibraryWidget"]);
            });
          }
          //make sure user is signed in
          else if (!redirectIfNotSignedIn()){

            app.getWidget("IndividualLibraryWidget").done(function(widget) {
              widget.setSubView(data);
              app.getObject('MasterPageManager').show("LibrariesPage",
                ["IndividualLibraryWidget", "UserNavbarWidget"]);
              publishPageChange("libraries-page");
            });

          }

          if (data.publicView) {
            this.route = "#/public-libraries/" + data.id ;
          }
          else {
            this.route = "#user/libraries/" + data.id;
          }

        });


        this.set("library-export", function(widget, data){

          if (!(data.bibcodes && data.bibcodes.length || data.id)) {
            throw new Error("neither an identifying id for library nor the bibcodes themselves were provided to export widget");
          }

          app.getWidget("ExportWidget").done(function(exportWidget) {

            //classic is a special case, it opens in a new tab
            if (data.subView == "classic") {
              if (data.bibcodes && data.bibcodes.length) {
                exportWidget.openClassicExports({bibcodes: data.bibcodes});
              }
              else if (data.id) {
                app.getObject("LibraryController").getLibraryData(data.id).done(function (bibcodes) {
                  exportWidget.openClassicExports({currentQuery: bibcodes});
                });
              }
              //show library list view (since there is nothing else to show in this tab
              self.navigate("IndividualLibraryWidget", {sub: "library", id: data.id});
              return;
            }
            // if it was a regular export:
            //first, tell export widget what to show
            if (data.bibcodes && data.bibcodes.length) {

              exportWidget.exportRecords(data.sub, data.bibcodes);
              //then, set library tab to proper field
              app.getWidget("IndividualLibraryWidget").done(function(widget) {
                widget.setSubView({subView: "export", publicView: data.publicView});
              });

            }
            //no bibcodes provided (coming from router)
            else if (data.id) {
              app.getObject("LibraryController").getLibraryData(data.id).done(function (bibcodes) {
                bibcodes = bibcodes.documents;
                exportWidget.exportRecords(data.sub, bibcodes);
                //then, set library tab to proper field
                app.getWidget("IndividualLibraryWidget").done(function(widget) {
                  widget.setSubView({
                    subView: "export",
                    id: data.id,
                    publicView: data.publicView
                  });
                });
              });
            }

            if (data.publicView) {
              app.getObject('MasterPageManager').show("PublicLibrariesPage",
                ["IndividualLibraryWidget", "ExportWidget"]);

              this.route = "#/public-libraries/" + data.id ;

            }
            else {
              //show library page manager
              app.getObject('MasterPageManager').show("LibrariesPage",
                ["IndividualLibraryWidget", "UserNavbarWidget", "ExportWidget"]);
              publishPageChange("libraries-page");

              this.route = "#user/libraries/" + data.id;

            }
          });

        });

        this.set("library-metrics", function(widget, data){

          function renderMetrics(bibcodes){
            app.getWidget("Metrics").done(function(widget){
              widget.renderWidgetForListOfBibcodes(bibcodes);
            });
            //then, set library tab to proper field
            app.getWidget("IndividualLibraryWidget").done(function(widget){
              widget.setSubView({ subView : "metrics", publicView : data.publicView, id : data.id });
            })
          }

            //first, tell export widget what to show
            if (data.bibcodes && data.bibcodes.length) {
              renderMetrics(data.bibcodes);
            }

            else if (data.id){
              app.getObject("LibraryController").getLibraryData(data.id).done(function(bibcodes){
               renderMetrics(bibcodes.documents);
              });
            }

            else {
              throw new Error("neither an identifying id for library nor the bibcodes themselves were provided to export widget");
              return
            }

            if (data.publicView) {

              app.getObject('MasterPageManager').show("PublicLibrariesPage",
                ["IndividualLibraryWidget", "Metrics"]);
              this.route = "#/public-libraries/" + data.id ;

            }
            else {

              app.getObject('MasterPageManager').show("LibrariesPage",
                ["IndividualLibraryWidget", "UserNavbarWidget", "Metrics"]);
              this.route = "#user/libraries/" + data.id;
              publishPageChange("libraries-page");

            }

        });

        this.set("library-visualization", function(widget, data){

          var widgetName = arguments[1].subView;

          function renderVis(bibcodes){

            app.getWidget(widgetName).done(function(widget){
              widget.renderWidgetForListOfBibcodes(bibcodes);
            });
            //then, set library tab to proper field
            app.getWidget("IndividualLibraryWidget").done(function(widget){
              widget.setSubView({ subView : "visualization", publicView : data.publicView, id : data.id });
            })

          }
          //first, tell export widget what to show
          if (data.bibcodes && data.bibcodes.length) {
            renderVis(data.bibcodes);
          }
          else if (data.id){
            app.getObject("LibraryController").getLibraryData(data.id).done(function(bibcodes){
            renderVis(bibcodes.documents);
            });
          }
          else {
            throw new Error("neither an identifying id for library nor the bibcodes themselves were provided to export widget");
            return
          }

          if (data.publicView){
            app.getObject('MasterPageManager').show("PublicLibrariesPage",
              ["IndividualLibraryWidget", widgetName]);
            this.route = "#/public-libraries/" + data.id ;
          }
          else {
            app.getObject('MasterPageManager').show("LibrariesPage",
              ["IndividualLibraryWidget", "UserNavbarWidget", widgetName]);
            this.route = "#user/libraries/" + data.id;
            publishPageChange("libraries-page");
          }

        });


        this.set("home-page", function(){
          app.getObject('MasterPageManager').show("HomePage",
            []);
          publishPageChange("home-page");

        });

        this.set('authentication-page', function(page, data){
          var data = data || {},
              subView = data.subView || "login",
              loggedIn = app.getBeeHive().getObject("User").isLoggedIn();

          if (loggedIn){
            //redirect to index
            self.get('index-page').execute();
          }
          else {
            this.route = "#user/account/" + subView;
            app.getObject('MasterPageManager').show("AuthenticationPage",
              ['Authentication']);
            app.getWidget("Authentication").done(function(w) {
              w.setSubView(subView);
            });
          }
        });

        this.set('results-page', function(widget, args) {

          app.getObject('MasterPageManager').show('SearchPage',
            searchPageAlwaysVisible);
          //allowing widgets to override appstorage query (so far only used for orcid redirect)
          var q = app.getObject('AppStorage').getCurrentQuery();
          var route = '#search/' + queryUpdater.clean(q).url();

          //taking care of inserting bigquery key here, not sure if right place
          //clean(q) above got rid of qid key, reinsert it
          if (q && q.get("__qid")){
            route += ("&__qid=" + q.get("__qid")[0]);
          }

          this.route = route;
          publishFeedback({code: ApiFeedback.CODES.UNMAKE_SPACE});
        });

        this.set('export', function(nav, options) {

          var format = options.format || 'bibtex';
          var storage = app.getObject('AppStorage');

          app.getObject('MasterPageManager').show('SearchPage',
            ['ExportWidget'].concat(searchPageAlwaysVisible.slice(1)));

          app.getWidget('ExportWidget').done(function(widget) {

            //classic is a special case, it opens in a new tab
            if (format == "classic") {
              if (options.onlySelected && storage.hasSelectedPapers()) {
                widget.openClassicExports({bibcodes: storage.getSelectedPapers()});
              }
              else {
                widget.openClassicExports({currentQuery: storage.getCurrentQuery()});
              }
              return;
            }

            //first, open central panel
            publishFeedback({code: ApiFeedback.CODES.MAKE_SPACE});

            // is a special case, it opens in a new tab
            if (options.onlySelected && storage.hasSelectedPapers()) {
              widget.exportRecords(format, storage.getSelectedPapers());
            }
            //all records specifically requested
            else if (options.onlySelected === false && storage.hasCurrentQuery()) {
              widget.exportQuery({
                format: format,
                currentQuery: storage.getCurrentQuery(),
                numFound: storage.get("numFound")
              });
            }
            // no request for selected or not selected, show selected
            else if (options.onlySelected === undefined && storage.hasSelectedPapers()) {
              widget.exportRecords(format, storage.getSelectedPapers());
            }
            //no selected, show all papers
            else if (storage.hasCurrentQuery()) {
              widget.exportQuery({
                format: format,
                currentQuery: storage.getCurrentQuery(),
                numFound: storage.get("numFound")
              });
            }
            else {
              var alerts = app.getController('AlertsController');
              alerts.alert({msg: 'There are no records to export yet (please search or select some)'});
              this.get('results-page')();
              return;
            }
          });

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
                self.getPubSub().publish(self.getPubSub().START_SEARCH, q);
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

        this.set('orcid-instructions', function(){

          this.route = "#orcid-instructions";
          app.getObject('MasterPageManager').show('OrcidInstructionsPage');
        });

        this.set('orcid-page', function(view, targetRoute) {

          var orcidApi = app.getService('OrcidApi');
          var persistentStorage  = app.getService('PersistentStorage');
          var appStorage = app.getObject('AppStorage');

          // traffic from Orcid - user has authorized our access
          if (!orcidApi.hasAccess() && orcidApi.hasExchangeCode()) {
            //since app will exit, store the information that we're authenticating
            if (persistentStorage){
              persistentStorage.set("orcidAuthenticating", true);
            }
            else {
              console.warn("no persistent storage service available");
            }
            orcidApi.getAccessData(orcidApi.getExchangeCode())
                .done(function (data) {
                  orcidApi.saveAccessData(data);
                  self.getPubSub().publish(self.getPubSub().APP_EXIT, {
                    url: window.location.pathname +
                    ((targetRoute && _.isString(targetRoute)) ? targetRoute : window.location.hash)
                  });
                })
                .fail(function () {
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

          if (orcidApi.hasAccess()) {

            if (persistentStorage.get("orcidAuthenticating")){

              persistentStorage.remove("orcidAuthenticating");
              // check if we need to trigger modal alert to ask user to fill in necessary data
              //we only want to show this immediately after user has authenticated with orcid
              orcidApi.getADSUserData().done(function (data) {
                //don't show modal if we're just going to redirect to the ads-orcid form anyway
                    if (!data.hasOwnProperty("authorizedUser") && JSON.stringify(appStorage.get("stashedNav")) !== '["UserPreferences",{"subView":"orcid"}]') {
                      //the form has yet to be filled out by the user
                      //now tailor the message depending on whether they are signed in to ADS or not
                      var alerter = app.getController('AlertsController');
                      alerter.alert(new ApiFeedback({
                        code: ApiFeedback.CODES.ALERT,
                        msg: OrcidModalTemplate({adsLoggedIn: app.getObject("User").isLoggedIn()}),
                        type: "success",
                        title: "You are now logged in to ORCID",
                        modal: true
                      }));
                    }// end check if user has already provided data
                  })
                  .fail(function(error){
                    console.warn(error);
                  });
            }
            //should we redirect back to a certain page now that orcid is authenticated?
            //the stashed nav in question currently would only belong to orcid form on user page
            //calling this function executes it
            if (!appStorage.executeStashedNav()) {
              //go to the orcidbigwidget
              this.route = '#user/orcid';
                app.getObject('MasterPageManager').show('OrcidPage',
                    ['OrcidBigWidget', 'SearchWidget']);
            }
          }
          else {
            //just redirect to index page, no orcid access
            this.route = "";
            self.get('index-page').execute();
          }
        });

        /*
        * functions for showing "explore" widgets on results page
        * */

        function showResultsPageWidgetWithUniqueUrl (command, options){

          var q = app.getObject('AppStorage').getCurrentQuery();
          publishFeedback({code: ApiFeedback.CODES.MAKE_SPACE});
          var widgetName = _.map(command.split("-").slice(1), function(w){return w[0].toUpperCase() + w.slice(1)}).join("");
          app.getObject('MasterPageManager').show('SearchPage',
            [widgetName].concat(searchPageAlwaysVisible.slice(1)));
          this.route =  '#search/' + queryUpdater.clean(q.clone()).url() +
                        "/" + command.split("-").slice(1).join("-");

          //show selected, need to explicitly tell widget to show bibcodes
          if (options && options.onlySelected){
            app.getWidget(widgetName).done(function(w){
              var selected =  app.getObject('AppStorage').getSelectedPapers();
              w.renderWidgetForListOfBibcodes(selected);
            });
          }
          else {
            app.getWidget(widgetName).done(function(w){
              w.renderWidgetForCurrentQuery();
            });
          }
        }

        this.set('show-author-network', function(command, options) {
          showResultsPageWidgetWithUniqueUrl.apply(this, arguments);
        });

        this.set('show-concept-cloud', function(command, options) {
          showResultsPageWidgetWithUniqueUrl.apply(this, arguments);
        });

        this.set('show-paper-network', function(command, options) {
          showResultsPageWidgetWithUniqueUrl.apply(this, arguments);
        });

        this.set('show-bubble-chart', function(command, options) {
          showResultsPageWidgetWithUniqueUrl.apply(this, arguments);
        });
        this.set('show-metrics', function(command, options) {
          showResultsPageWidgetWithUniqueUrl.apply(this, arguments);
        });

        this.set("visualization-closed", this.get("results-page"));

        /*
        * Below are functions for abstract pages
        */

        var showDetail = function(pages, toActivate) {
          app.getObject('MasterPageManager').show('DetailsPage',
            pages);
          app.getWidget("DetailsPage").done(function(w) {
            w.setActive(toActivate);
          });
        };

        this.set('ShowAbstract', function(id, data){
          showDetail([id].concat(detailsPageAlwaysVisible), id);
          this.route = data.href;
        });
        this.set('ShowCitations', function(id, data) {
          showDetail([id].concat(detailsPageAlwaysVisible), id);
          this.route = data.href;
        });
        this.set('ShowReferences', function(id, data ) {
          showDetail([id].concat(detailsPageAlwaysVisible), id);
          this.route = data.href;
        });
        this.set('ShowCoreads', function(id, data) {
          showDetail([id].concat(detailsPageAlwaysVisible), id);
          this.route = data.href;
        });
        this.set('ShowTableOfContents', function(id, data) {
          showDetail([id].concat(detailsPageAlwaysVisible), id);
          this.route = data.href;
        });
        this.set('ShowSimilar', function(id, data) {
          showDetail([id].concat(detailsPageAlwaysVisible), id);
          this.route = data.href;
        });
        this.set('ShowMetrics', function(id, data) {
          showDetail([id].concat(detailsPageAlwaysVisible), id);
          this.route = data.href;
        });
        this.set("ShowPaperExport", function(id, data){
          var format = data.subView;
          app.getObject('MasterPageManager').show('DetailsPage',
            [id].concat(detailsPageAlwaysVisible));
          app.getWidget("DetailsPage").done(function(w) {
            w.setActive(id, format);
          });
        });
        this.set('ShowGraphics', function(id, data) {
          showDetail([id].concat(detailsPageAlwaysVisible), id);
          this.route = data.href;

        });
      }


    });

    return NavigatorService;

  });
