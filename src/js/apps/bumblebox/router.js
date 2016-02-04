define([
  'jquery',
  'underscore',
  'backbone',
  'js/components/api_query',
  'js/mixins/dependon',
  'js/components/api_feedback',
  'js/components/api_request',
  'js/components/api_targets',
  'js/components/api_query_updater'
  ],
  function(
    $,
    _,
    Backbone,
    ApiQuery,
    Dependon,
    ApiFeedback,
    ApiRequest,
    ApiTargets,
    ApiQueryUpdater
    ) {
    'use strict';

    var Router = Backbone.Router.extend({
      initialize: function(options) {
        options = options || {};
        this.queryUpdater = new ApiQueryUpdater('Router');
      },
      activate: function(beehive) {
        this.setBeeHive(beehive);
        if (!this.hasPubSub()) {
          throw new Error('Ooops! Who configured this #@$%! There is no PubSub service!');
        }
      },
      /*
      * if you don't want the navigator to duplicate the route in history,
      * use this function instead of pubsub.publish(pubsub.NAVIGATE ...)
      * */
      routerNavigate: function(route, options) {
        options = options || {};
        _.extend(options, {replace: true});
        this.getPubSub().publish(this.getPubSub().NAVIGATE, route, options);
      },

      routes: {
        '': 'index',
        '*invalidRoute': 'noPageFound'
      },

      index: function(query) {
        this.routerNavigate('index-page', query);
      },

      noPageFound: function() {
        this.getPubSub().publish(this.getPubSub().NAVIGATE, '404');
      }
    });

    _.extend(Router.prototype, Dependon.BeeHive);
    return Router;
  });
