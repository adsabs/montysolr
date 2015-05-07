
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

    //for an individual widget
    _makeWidgetSpin: function(id){

      // turn ids into a list of widgets
      var widget = this.getWidgets([id]);

      // activate loading state
      if (widget && widget.length > 0) {
        this.changeWidgetsState(widget, {state: WidgetStates.WAITING});
      }
      // register handlers which will remove the spinning wheel
      var self = this;
      var pubsub = this.getApp().getService('PubSub');

      pubsub.once(pubsub.DELIVERING_RESPONSE + id, function() {
          self.changeWidgetsState(self.getWidgets([id]), {state: WidgetStates.RESET});
        });
    },

    _makeWidgetsSpin: function(ids) {
      // turn ids into a list of widgets
      var widgets = this.getWidgets(ids);

      // activate loading state
      if (widgets && widgets.length > 0) {
        this.changeWidgetsState(widgets, {state: WidgetStates.WAITING});
      }

      // register handlers which will remove the spinning wheel
      var self = this;
      var pubsub = this.getApp().getService('PubSub');
      _.each(ids, function(k) {
        var key = k;
        pubsub.once(pubsub.DELIVERING_RESPONSE + k, function() {
          self.changeWidgetsState(self.getWidgets([key]), {state: WidgetStates.RESET});
        })
      });
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
