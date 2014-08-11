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

        _.bindAll(this, "navigateFromPubSub", "changeURLFromPubSub");
        this.pageControllers = options.pageControllers;
        this.history = options.history;

      },

      activate: function (beehive) {
        this.setBeeHive(beehive);
        this.pubsub = this.getBeeHive().Services.get('PubSub');

        this.pubsub.subscribe(this.pubsub.NAVIGATE_WITH_TRIGGER, this.navigateFromPubSub);

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

      navigateFromPubSub : function (data) {
        /*
        * data should be a dict like {path: x, parameters : {y:1, z:2}}
        * */

         if (!data) {
          console.warn("can't navigate, no information given")
          return
        }
        else {
          this.navigate(data.path, {trigger: true})

        }

      },

      routes: {
        "": "index",
        "search/:query": 'search',
        'abs/:bibcode(/)(:subView)': 'viewAbstract'
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
          this.pageControllers.resultsPage.showPage();
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
      }


    });

    _.extend(Router.prototype, Dependon.BeeHive);
    return Router;

  });