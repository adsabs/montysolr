define([
    'jquery',
    'backbone',
    'js/components/api_query',
    'js/mixins/dependon'],
  function ($, Backbone, ApiQuery, Dependon) {

    "use strict";

    // Defining the application router.
    var Router = Backbone.Router.extend({

      initialize : function(options){
        options = options || {};

        _.bindAll(this, "changeURLFromPubSub");
        this.pageManager  = options.pageManager;
        this.history = options.history;

      },

      activate: function (beehive) {
        this.setBeeHive(beehive);
        this.pubsub = this.getBeeHive().Services.get('PubSub');

        this.pubsub.subscribe(this.pubsub.NAVIGATE_WITHOUT_TRIGGER, this.changeURLFromPubSub)

      },

      changeURLFromPubSub : function(options, pubsubkey){

        var path = options.path;

        //rewrite this to check to make sure its a valid path
        if (!path) {
          console.warn("can't navigate, no information given")
          return
        }
        else {

          var skipHistory = options.skipHistory || false;

          this.navigate(path, {replace : skipHistory})
        }

      },

      routes: {
        "": "index",
        "search/(:query)": 'search',
        'abs/:bibcode(/)(:subView)': 'viewAbstract',
        '*invalidRoute': 'noPageFound'
      },


      index: function () {

        this.pageManager.showPage("index");

      },

      search: function (query) {

        if (query) {
          var q= new ApiQuery().load(query);

          this.pubsub.publish(this.pubsub.START_SEARCH, q);
        }
        else {
          this.pageManager.showPage("results", {triggerNav: false});

        }
      },

      viewAbstract: function (bibcode, subPage) {

        if (bibcode){

          if (!subPage) {
            subPage = "abstract"
            //"redirecting" to the abstract page
            this.navigate("/abs/" + bibcode + "/abstract", {replace: true})
          }

         this.pageManager.showPage("abstract", {bibcode: bibcode, subPage : subPage});

        }
      },

      noPageFound : function() {
       //i will fix this later

        $("#body-template-container").html("<div>You have broken bumblebee. (404)</div><img src=\"http://imgur.com/EMJhzmL.png\" alt=\"sad-bee\">")


    }


    });

    _.extend(Router.prototype, Dependon.BeeHive);
    return Router;

  });