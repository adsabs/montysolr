
/*
 * This module contains a set of utilities that can be added to feedback
 * handlers; the feedback handler must have access to the application
 * object (this is different from widget feedback handling; see
 * widget_state_handling)
 */
define([
  'underscore',
  'js/widgets/widget_states'
  ], function(
  _,
  WidgetStates
  ) {

  var Mixin = {

    getApp: function() {
      return this.app;
    },

    changeWidgetsState: function(widgets, state) {
      var app = this.getApp();
      app.triggerMethod(widgets, "Putting widgets into new state", 'changeState', state);
    },

    getWidgets: function(pubsubKeys) {
      var app = this.getApp();
      var widgets = _.map(pubsubKeys, function(psk) {
        var n = app.getPluginOrWidgetName(psk);
        if (n && n.indexOf('widget:') > -1) {
          try {
            var w = app.getPluginOrWidgetByPubSubKey(psk);
            if (w)
              return [psk, w];
          }
          catch (e) {
            if (e.message && e.message.indexOf('Cant find barbarian with ID') > -1) return;
            throw e;
          }
        }
      });
      return _.filter(widgets);
    }

  };

  return Mixin;
});
