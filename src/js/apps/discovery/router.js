define([
    'jquery',
    'backbone',
    'js/components/api_query',
    'js/mixins/dependon'],
  function ($, Backbone, ApiQuery, Dependon) {

    "use strict";


    // Defining the application router.
    var Router = Backbone.Router.extend({
      routes: {
        "": "index",
        "search/*query": 'search',
        'abs/:bibcode': 'viewAbstract'
      },

      index: function () {
        this.switchViews();
        this.navigate('search/');
      },

      search: function (query) {
        this.switchViews('search');
        if (query) {
          if (_.isString(query))
            query = new ApiQuery().load(query);
          var pubsub = this.getBeeHive().Services.get('PubSub');
          pubsub.publish(pubsub.NEW_QUERY, query);
        }
      },

      viewAbstract: function (bibcode) {
        this.switchViews('abs');
        if (bibcode) {
          var pubsub = this.getBeeHive().Services.get('PubSub');
          pubsub.publish(pubsub.DISPLAY_DOCUMENTS, bibcode);
        }
      },

      onNewQuery: function(apiQuery) {
        this.switchViews('search');
        this.navigate('search/' + encodeURI(apiQuery.url()));
      },

      switchViews: function (name) {
        switch(name) {
          case 'abs':
            $('#middle-column #search-results').addClass('hide');
            $('#middle-column .multi-view').removeClass('hide');
            break;
          case 'search':
          default:
            $('#middle-column #search-results').removeClass('hide');
            $('#middle-column .multi-view').addClass('hide');
            break;
        }

      },

      activate: function (beehive) {
        this.setBeeHive(beehive);
        var pubsub = this.getBeeHive().Services.get('PubSub');
        pubsub.subscribe(pubsub.NEW_QUERY, _.bind(this.onNewQuery, this));
      }

    });

    _.extend(Router.prototype, Dependon.BeeHive);
    return Router;

  });