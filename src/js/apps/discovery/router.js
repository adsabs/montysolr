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
        this.pageControllers = options.pageControllers;
        this.history = options.history;

      },

      activate: function (beehive) {
        this.setBeeHive(beehive);
        this.pubsub = this.getBeeHive().Services.get('PubSub');

        this.pubsub.subscribe(this.pubsub.NAVIGATE_WITHOUT_TRIGGER, this.changeURLFromPubSub)

      },

      changeURLFromPubSub : function(d){

        if (!d) {
          console.warn("can't navigate, no information given")
          return
        }
        else {
          this.history.addEntry({page: d.page, subPage:d.subPage, data : d.data })
          this.navigate(d.path)
        }

      },

      routes: {
        "": "index",
        "search/(:query)": 'search',
        'abs/:bibcode(/)(:subView)': 'viewAbstract',
        '*invalidRoute': 'noPageFound'
      },


      index: function () {

        this.history.addEntry({page : "landingPage", subPage : undefined, data : undefined})

        this.pageControllers.landingPage.showPage();

      },

      search: function (query) {

        if (query) {
          var q= new ApiQuery().load(query);

          this.history.addEntry({page : "resultsPage", subPage: undefined, data: q.toJSON()})
          this.pubsub.publish(this.pubsub.START_SEARCH, q);
        }
        else {
          this.history.addEntry({page : "resultsPage", subPage: undefined, data: undefined})
          this.pageControllers.resultsPage.showPage(false);

        }
      },

      viewAbstract: function (bibcode, subView) {

        if (bibcode){

          this.history.addEntry({page: "abstractPage", data:  bibcode, subPage: subView})

          if (!subView) {
            subView = "abstract"
            //"redirecting" to the abstract page
            this.navigate("/abs/" + bibcode + "/abstract", {replace: true})
          }

         this.pageControllers.abstractPage.showPage(bibcode, subView);

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