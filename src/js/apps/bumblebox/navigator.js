/**
* The main 'navigation' endpoints (the part executed inside
* the application) - this is a companion to the 'router'
*/

define([
  'jquery',
  'backbone',
  'underscore',
  'js/components/navigator'
],
  function(
    $,
    Backbone,
    _,
    Navigator
    ) {
    'use strict';
    var NavigatorService = Navigator.extend({

      start: function(app) {
        this.set('index-page', function() {
          app.getObject('MasterPageManager').show('PageManager', ['TargetWidget']);
          this.route = '';
        });
        this.set('404', function() {
          app.getObject('MasterPageManager').show(['ErrorWidget']);
          this.route = '#404';
        });
        this.set('505', function() {
          app.getObject('MasterPageManager').show(['ErrorWidget']);
          this.route = '#505';
        });
      }
    });
    return NavigatorService;
  });
