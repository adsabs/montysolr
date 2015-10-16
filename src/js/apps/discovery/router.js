define([
    'jquery',
    'backbone',
    'js/components/api_query',
    'js/mixins/dependon',
    'js/components/api_feedback',
    'js/components/api_request',
    'js/components/api_targets',
    'js/mixins/api_access',
    'js/components/api_query_updater'

  ],
  function (
    $,
    Backbone,
    ApiQuery,
    Dependon,
    ApiFeedback,
    ApiRequest,
    ApiTargets,
    ApiAccessMixin,
    ApiQueryUpdater

    ) {

    "use strict";

    var Router = Backbone.Router.extend({

      initialize : function(options){
        options = options || {};
        this.history = options.history;
        this.queryUpdater = new ApiQueryUpdater('Router');
      },

      activate: function (beehive) {
        this.setBeeHive(beehive);
        if (!this.hasPubSub()) {
          throw new Exception("Ooops! Who configured this #@$%! There is no PubSub service!")
        }
      },

      /*
      * if you don't want the navigator to duplicate the route in history,
      * use this function instead of pubsub.publish(pubsub.NAVIGATE ...)
      * */

      routerNavigate: function (route, options) {

        var options = options || {};
        //this tells navigator not to create 2 history entries, which causes
        //problems with the back button
        _.extend(options, {replace : true});
       this.getPubSub().publish(this.getPubSub().NAVIGATE, route, options);

      },

      routes: {
        "": "index",
        'classic-form' : 'classicForm',
        'paper-form' : 'paperForm',
        'index/(:query)': 'index',
        'search/(:query)/(:widgetName)': 'search',
        'execute-query/(:query)': 'executeQuery',
        'abs/:bibcode(/)(:subView)': 'view',
        'user/orcid*(:subView)' : 'orcidPage',

        'user/account(/)(:subView)' : 'authenticationPage',
        'user/account/verify/(:subView)/(:token)' : 'routeToVerifyPage',
        'user/settings(/)(:subView)(/)' : 'settingsPage',

        'user/libraries(/)(:id)(/)(:subView)(/)(:subData)(/)' : 'librariesPage',
        'user/home' : 'homePage',
        'public-libraries/(:id)(/)' : 'publicLibraryPage',
        //"(:query)": 'index',

        '*invalidRoute': 'noPageFound'
      },

      index: function (query) {
        this.routerNavigate('index-page');
      },

      classicForm : function(){
        this.routerNavigate('ClassicSearchForm');
      },

      paperForm : function(){
        this.routerNavigate('PaperSearchForm');

      },

      search: function (query, widgetName) {
        var possibleSearchSubPages = ["metrics", "author-network", "paper-network",
                                      "concept-cloud", "bubble-chart"];
        if (query) {
          try {
            var q= new ApiQuery().load(query);
            this.getPubSub().publish(this.getPubSub().START_SEARCH, q);
          }
          catch (e) {
            console.error('Error parsing query from a string: ', query, e);
            this.getPubSub().publish(this.getPubSub().BIG_FIRE, new ApiFeedback({
              code: ApiFeedback.CODES.CANNOT_ROUTE,
              reason: 'Cannot parse query',
              query: query}));
          }
        }
        else {
          this.getPubSub().publish(this.getPubSub().NAVIGATE, 'index-page');
        }

        if (widgetName && possibleSearchSubPages.indexOf(widgetName) > -1){
          // convention is that a navigate command for search page widget starts with "show-"
          // waits for the navigate to results page emitted by the discovery_mediator
          // once the solr search has been received
          this.getPubSub().subscribeOnce(this.getPubSub().NAVIGATE, _.bind(function(page){
              if (page == "results-page"){
                this.getPubSub().publish(this.getPubSub().NAVIGATE, "show-" + widgetName);
              }
          }, this));
        }
        else if (widgetName && possibleSearchSubPages.indexOf(widgetName) == -1){
          console.error("Results page subpage not recognized:", widgetName);
        }
      },


      executeQuery: function(queryId) {
        this.getPubSub().publish(this.getPubSub().NAVIGATE, 'execute-query', queryId);
      },

      view: function (bibcode, subPage) {

        // check we are using the canonical bibcode and redirect to it if necessary
        var q, req, self;
        self = this;
        q = new ApiQuery({'q': 'identifier:' + this.queryUpdater.quoteIfNecessary(bibcode), 'fl': 'bibcode'});
        req = new ApiRequest({query: q, target: ApiTargets.SEARCH, options: {
          done: function(resp) {
            var navigateString, href;

            if (!subPage) {
              navigateString = 'ShowAbstract';
            }
            else {
              navigateString = "Show"+ subPage[0].toUpperCase() + subPage.slice(1);
              href =  "#abs/" + bibcode + "/" + subPage;
            }
            self.routerNavigate(navigateString, {href : href});

            if (resp.response && resp.response.docs && resp.response.docs[0]) {
              bibcode = resp.response.docs[0].bibcode;
              self.getPubSub().publish(self.getPubSub().DISPLAY_DOCUMENTS, new ApiQuery({'q': 'bibcode:' + bibcode}));
            }
          },
          fail: function() {
            console.log('Cannot identify page to load, bibcode: ' + bibcode);
            self.getPubSub().publish(this.getPubSub().NAVIGATE, 'index-page');
          }
        }});

        this.getPubSub().publish(this.getPubSub().EXECUTE_REQUEST, req);

      },


      routeToVerifyPage : function(subView, token){

        var failMessage, failTitle, route, done, request, type, that = this;

        if (subView == "register") {
          failTitle = "Registration failed.";
          failMessage = "<p>Please try again, or contact <b> adshelp@cfa.harvard.edu for support </b></p>";
          route = ApiTargets.VERIFY + "/" + token;

          done = function(reply) {
            //user has been logged in already by server
            //request bootstrap
            this.getApiAccess({reconnect : true}).done(function(){
              //redirect to index page
              that.getPubSub().publish(that.getPubSub().NAVIGATE, 'index-page');
              //call alerts widget
              var title = "Welcome to ADS";
              var msg = "<p>You have been successfully registered with the username</p> <p><b>"+ reply.email +"</b></p>";
              that.getPubSub().publish(that.getPubSub().ALERT, new ApiFeedback({code: 0, title : title, msg: msg, modal : true, type : "success"}));
            }).fail(function(){
              //fail function defined below
              fail();
            });

          };
        }
        else if (subView == "change-email") {
          failTitle = "Attempt to change email failed";
          failMessage = "Please try again, or contact adshelp@cfa.harvard.edu for support";
          route = ApiTargets.VERIFY + "/" + token;

          done = function(reply) {
            //user has been logged in already
            //request bootstrap
            this.getApiAccess({reconnect : true}).done(function(){
              //redirect to index page
              that.getPubSub().publish(that.getPubSub().NAVIGATE, 'index-page');
              //call alerts widget
              var title = "Email has been changed.";
              var msg = "Your new ADS email is <b>" + reply.email + "</b>";
              that.getPubSub().publish(that.getPubSub().ALERT, new ApiFeedback({code: 0, title : title, msg: msg, modal : true, type : "success"}));
            }).fail(function(){
              //fail function defined below
              this.apply(fail, arguments);
            });
          };
        }
        else if (subView == "reset-password") {

          done = function() {
            //route to reset-password-2 form
            //set the token so that session can use it in the put request with the new password
            this.getBeeHive().getObject("Session").setChangeToken(token);
            this.getPubSub().publish(this.getPubSub().NAVIGATE, 'authentication-page', {subView: "reset-password-2"});
          };

          failMessage = "Reset password token was invalid.";
          route = ApiTargets["RESET_PASSWORD"] + "/" + token;
          type = "GET";

        }
        function fail(jqXHR, status, errorThrown) {
          //redirect to index page
          this.getPubSub().publish(this.getPubSub().NAVIGATE, 'index-page');
          var error = (jqXHR.responseJSON && jqXHR.responseJSON.error) ? jqXHR.responseJSON.error : "error unknown";
          //call alerts widget
          this.getPubSub().publish(this.getPubSub().ALERT, new ApiFeedback({code: 0, title: failTitle, msg: " <b>" + error + "</b> <br/>" + failMessage, modal : true, type : "danger"}));
        };

        request = new ApiRequest({
          target : route,
          options : {
            type : type || "GET",
            context : this,
            done : done,
            fail : fail
          }
        });

        this.getBeeHive().getService("Api").request(request);
      },

      orcidPage :function(){
        this.getPubSub().publish(this.getPubSub().NAVIGATE, 'orcid-page');
      },

      authenticationPage: function(subView){
        //possible subViews: "login", "register", "reset-password"
        if (subView && !_.contains(["login", "register", "reset-password-1", "reset-password-2"], subView)){
          throw new Error("that isn't a subview that the authentication page knows about")
        }
        this.routerNavigate('authentication-page', {subView: subView});
      },

      settingsPage : function(subView){
        //possible subViews: "token", "password", "email", "preferences"
        if (_.contains(["token", "password", "email", "delete"], subView)){
          this.routerNavigate('UserSettings', {subView: subView});
        }
        else if ("preferences" == subView || !subView){
          //show preferences if no subview provided
          this.routerNavigate('UserPreferences');
        }
        else {
          throw new Error("did not recognize user page");
        }
      },

      librariesPage : function(id, subView, subData){

        if (id){
          //individual libraries view
          var subView = subView || "library";
          if (_.contains(["library", "admin"], subView )){

            this.routerNavigate('IndividualLibraryWidget', {sub : subView, id : id});
          }
          else if(_.contains(["export", "metrics", "visualization"], subView)) {

            subView = "library-" + subView;

            if (subView == "library-export"){
              this.routerNavigate(subView, {sub : subData || "bibtex", id : id});
            }
            else if (subView == "library-metrics"){
              this.routerNavigate(subView, { id : id});

            }
            else if (subView == "library-visualization"){
              //not implemented yet
            }

          }
          else {
            throw new Error("did not recognize subview for library view");
          }
        }
        else {
          //main libraries view
          this.routerNavigate("AllLibrariesWidget", "libraries");
        }
      },

      publicLibraryPage : function (id){
        //main libraries view
        this.getPubSub().publish(this.getPubSub().NAVIGATE, "IndividualLibraryWidget", {id : id, publicView : true, sub : "library"});
      },

      homePage : function(subView){
        this.routerNavigate('home-page', {subView: subView});
      },

      noPageFound : function() {
        this.getPubSub().publish(this.getPubSub().NAVIGATE, "404")
      },

      // backbone default behaviour is to automatically decodeuri parameters
      // however this behaviour breaks our apiquery loading, so we'll detect
      // this situation and avoid decoding when there are multiple parameters
      _extractParameters: function(route, fragment) {
        var params = route.exec(fragment).slice(1);
        return _.map(params, function(param) {
          return param ? ((param.indexOf('%26') > -1 && param.indexOf('&') > -1 ) ? param : decodeURIComponent(param)) : null;
        });
      }


    });

    _.extend(Router.prototype, Dependon.BeeHive, ApiAccessMixin);

    return Router;

  });
