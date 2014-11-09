define([
    "marionette",
    "hbs!./templates/toc-page-layout",
    './controller',
    './three_column_view'
  ],
  function (Marionette,
            pageTemplate,
            BasicPageManagerController
    ) {

    var PageManagerController = BasicPageManagerController.extend({


      assemble: function() {
        BasicPageManagerController.prototype.assemble.apply(this, arguments);
        var self = this;

        // listen to every widget we manage
        var listener = _.bind(self.onPageManagerEvent, self);

        _.each(_.keys(self.widgets), function(w) {
          self.listenTo(self.widgets[w], "page-manager-event", listener);
          self.broadcast('page-manager-message', 'new-widget', w);
        });
      },

      /**
       * broadcast the event to all other managed widgets
       */
      broadcast: function(){
        var args = arguments;
        var self = this;
        _.each(_.keys(self.widgets), function(w) {
          var widget = self.widgets[w];
          widget.trigger.apply(widget, args);
        });
      },


      /**
       * Listens to and receives signals from managed widgets.
       * It will discover their 'widgetId' and broadcasts the
       * data to all widgets int he collection.
       *
       * @param event
       * @param data
       */
      onPageManagerEvent: function(event, data) {
        var self = this;
        var sender = null; var widgetId = null;

        // try to find/identify sender
        if (data.widget) {
          _.each(_.pairs(self.widgets), function(w) {
            if (w[1] === data.widget) {
              widgetId = w[0];
              sender = w[1];
            }
          });
          delete data.widget;
        }

        if (event == 'widget-ready' && sender !== null) {
          data["widgetId"] = widgetId;
          self.broadcast('page-manager-message', event, data);
        }
        else if (event == 'widget-selected') {
          this.pubsub.publish(this.pubsub.NAVIGATE, this.widgetId ? this.widgetId + ':' + widgetId : widgetId);
        }

      },

      onClose: function () {
        this.stopListening();
        this.widgets = {};
        this.view.close();
      }

    });

    return PageManagerController;
  });