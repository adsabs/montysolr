define([
    'jquery',
    'backbone',
    'js/components/api_query',
    'js/mixins/dependon',
    'backbone-query-parameters',],
  function ($, Backbone, ApiQuery, Dependon) {

    "use strict";

    // Defining the application router.
    var Router = Backbone.Router.extend({

      initialize : function(options){
        options = options || {};

        _.bindAll(this, "navigateFromPubSub")
        this.pageControllers = options.pageControllers;
        this.history = options.history;

      },

      activate: function (beehive) {
        this.setBeeHive(beehive);
        this.pubsub = this.getBeeHive().Services.get('PubSub');

        this.pubsub.subscribe(this.pubsub.NAVIGATE, this.navigateFromPubSub);

      },

      navigateFromPubSub : function (data) {
        /*data should be a dict like {path: x, parameters : {y:1, z:2}}*/
        if (!data) {
          console.warn("can't navigate, no information given")
          return
        }
        else {
          var params = $.param(data.parameters);
          params = params? "/?" + params : "";
          this.navigate(data.path+ params)

        }


      },

      routes: {
        "": "index",
        "search/": 'search',
        'abs/:bibcode(/)(:subView)': 'viewAbstract'
      },


      index: function () {
        this.pageControllers.landingPage.showPage();
        this.history.addEntry({"landingPage": undefined})

      },

      search: function (query) {
        var serializedQuery = $.param(query);

        if (serializedQuery) {
          this.history.addEntry({"resultsPage": serializedQuery})
          var q= new ApiQuery().load(serializedQuery);
          this.pubsub.publish(this.pubsub.START_SEARCH, q);
        }
      },

      viewAbstract: function (bibcode, subView) {
        var fromWithinPage;

        if (!subView) {
          subView = "abstract"
          //"redirecting" to the abstract page
          this.navigate("/abs/"+bibcode+"/abstract", {replace : true})
        }
        //notifies manager if it is just a simple shift from abstract to cited list, for example
        if (this.history.getPriorPage() === "abstractPage" && this.history.getPriorPageVal().bibcode === bibcode){
          fromWithinPage = true;
        }
        else {
          fromWithinPage = false;
        }

        if (bibcode){
          //it's the default abstract view
          this.pageControllers.abstractPage.showPage(bibcode, subView, fromWithinPage);
          this.history.addEntry({"abstractPage": {bibcode: bibcode, subView : subView}})
        }

      }


    });

    _.extend(Router.prototype, Dependon.BeeHive);
    return Router;

  });