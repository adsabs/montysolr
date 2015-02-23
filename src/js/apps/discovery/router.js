define([
    'jquery',
    'backbone',
    'js/components/api_query',
    'js/mixins/dependon',
    'hbs!./404',
    'js/components/api_feedback',
    'js/components/api_request'

  ],
  function (
    $,
    Backbone,
    ApiQuery,
    Dependon,
    ErrorTemplate,
    ApiFeedback,
    ApiRequest

    ) {

    "use strict";

    var Router = Backbone.Router.extend({

      initialize : function(options){
        options = options || {};
        this.history = options.history;
      },

      activate: function (beehive) {
        this.setBeeHive(beehive);
        this.pubsub = beehive.Services.get('PubSub');
        if (!this.pubsub) {
          throw new Exception("Ooops! Who configured this #@$%! There is no PubSub service!")
        }
      },

      routes: {
        "": "index",
        'index/(:query)': 'index',
        "search/(:query)": 'search',
        'abs/:bibcode(/)(:subView)': 'view',
        'user/orcid*(:subView)' : 'orcidPage',
        'user/account(/)(:subView)' : 'authenticationPage',
        'user/account/verify/(:subView)/(:token)' : 'routeToVerifyPage',
        'user/settings(/)(:subView)' : 'settingsPage',

        //"(:query)": 'index',
        '*invalidRoute': 'noPageFound'
      },

      index: function (query) {
        this.pubsub.publish(this.pubsub.NAVIGATE, 'index-page');
      },

      search: function (query) {
        if (query) {
          try {
            var q= new ApiQuery().load(query);
            this.pubsub.publish(this.pubsub.START_SEARCH, q);
          }
          catch (e) {
            console.error('Error parsing query from a string: ', query, e);
            this.pubsub.publish(this.pubsub.BIG_FIRE, new ApiFeedback({
              code: ApiFeedback.CODES.CANNOT_ROUTE,
              reason: 'Cannot parse query',
              query: query}));
          }
        }
        else {
          this.pubsub.publish(this.pubsub.NAVIGATE, 'index-page');
        }
      },

      view: function (bibcode, subPage) {
        if (bibcode){
          this.pubsub.publish(this.pubsub.DISPLAY_DOCUMENTS, new ApiQuery({'q': 'bibcode:' + bibcode}));

          if (!subPage) {
            return this.pubsub.publish(this.pubsub.NAVIGATE, 'abstract-page', bibcode);
          }
          else {
            var navigateString = "Show"+ subPage[0].toUpperCase() + subPage.slice(1);
            return this.pubsub.publish(this.pubsub.NAVIGATE, navigateString, bibcode);
          }
        }
        this.pubsub.publish(this.pubsub.NAVIGATE, 'abstract-page');
      },


      routeToVerifyPage : function(subView, token){

        var successMessage, failMessage, done, fail, request;

        if (subView === "register") {
          successMessage = "You have been successfully registered.",
              failMessage = "Registration failed.";
        }
        else if (subView === "email") {
         successMessage = "Email has been changed.",
            failMessage = "Attempt to change email failed";
        }
        else if (subView === "reset-password") {

          done = function() {
            //route to reset-password-2 form
            this.pubsub.publish(this.pubsub.NAVIGATE, 'settings-page', {subView: "reset-password-2"});
          };

         failMessage = "Reset password token was invalid."
        }

        done = done ? done : function() {
          //redirect to index page
          this.pubsub.publish(this.pubsub.NAVIGATE, 'index-page');
          //call alerts widget
          this.pubsub.publish(this.pubsub.ALERT, new ApiFeedback({code: 0, msg: successMessage, modal : true, type : "success"}));
        };

        fail = function() {
          //redirect to index page
          this.pubsub.publish(this.pubsub.NAVIGATE, 'index-page');
          //call alerts widget
          this.pubsub.publish(this.pubsub.ALERT, new ApiFeedback({code: 0, msg: failMessage, modal : true, type : "danger"}));

        };

         request = new ApiRequest({
            target : subView,
            type : "GET",
            context : this,
            done : done,
            fail : fail
          });

          this.getBeeHive().getService("Api").request(request);
        },

      orcidPage :function(){
        this.pubsub.publish(this.pubsub.NAVIGATE, 'orcid-page');
      },

      authenticationPage: function(subView){
        //possible subViews: "login", "register", "reset-password"
         this.pubsub.publish(this.pubsub.NAVIGATE, 'authentication-page', {subView: subView});
      },

      settingsPage : function(subView){
        //possible subViews: "token", "password", "email", "preferences"
        this.pubsub.publish(this.pubsub.NAVIGATE, 'settings-page', {subView: subView});
      },

      noPageFound : function() {
        //i will fix this later
        $("#body-template-container").html(ErrorTemplate())
      },

      // backbone default behaviour is to automatically decodeuri parameters
      // however this behaviour breaks our apiquery loading, so we'll detect
      // this situation and avoid decoding when there are multiple parameters
      _extractParameters: function(route, fragment) {
        var params = route.exec(fragment).slice(1);
        return _.map(params, function(param) {
          return param ? ((param.indexOf('%26C') > -1) ? param : decodeURIComponent(param)) : null;
        });
      }


    });

    _.extend(Router.prototype, Dependon.BeeHive);
    return Router;

  });