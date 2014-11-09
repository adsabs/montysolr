define([
    'jquery',
    'backbone',
    'js/components/api_query',
    'js/mixins/dependon'],
  function ($, Backbone, ApiQuery, Dependon) {

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
        "search/(:query)": 'search',
        'abs/:bibcode(/)(:subView)': 'view',
        '*invalidRoute': 'noPageFound'
      },


      index: function () {
        this.pubsub.publish(this.pubsub.NAVIGATE, 'index-page');
      },

      search: function (query) {
        if (query) {
          var q= new ApiQuery().load(query);
          this.pubsub.publish(this.pubsub.START_SEARCH, q);
        }
        this.pubsub.publish(this.pubsub.NAVIGATE, 'results-page');
      },

      view: function (bibcode, subPage) {
        if (bibcode){
          this.pubsub.publish(this.pubsub.DISPLAY_DOCUMENTS, new ApiQuery({'q': 'bibcode:' + bibcode}));

          if (!subPage) {
            return this.pubsub.publish(this.pubsub.NAVIGATE, 'abstract-page', bibcode);
          }
          else {
            return this.pubsub.publish(this.pubsub.NAVIGATE, 'abstract-page:' + subPage, bibcode);
          }
        }
        this.pubsub.publish(this.pubsub.NAVIGATE, 'abstract-page');
      },

      noPageFound : function() {
        //i will fix this later
        $("#body-template-container").html("<div>You have broken bumblebee. (404)</div><img src=\"http://imgur.com/EMJhzmL.png\" alt=\"sad-bee\">")
      }


    });

    _.extend(Router.prototype, Dependon.BeeHive);
    return Router;

  });