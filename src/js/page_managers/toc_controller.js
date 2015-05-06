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

        _.each(_.keys(this.widgets), function(w) {
          this.listenTo(this.widgets[w], "page-manager-event", _.bind(this.onPageManagerEvent, this, this.widgets[w]));
          this.broadcast('page-manager-message', 'new-widget', w);
        }, this);
      },

      onDisplayDocuments : function(apiQuery){

        var bibcode = apiQuery.get('q');
        if (bibcode.length > 0 && bibcode[0].indexOf('bibcode:') > -1) {
          bibcode = bibcode[0].replace('bibcode:', '');
          this.widgets.TOCWidget.model.set("bibcode", bibcode);
          };
        },

      /**
       * Listens to and receives signals from managed widgets.
       * It will discover their 'widgetId' and broadcasts the
       * data via a page-manager-message to all widgets in the
       * collection.
       *
       * @param event
       * @param data
       */
      onPageManagerEvent: function(widget, event, data) {
        var sender = null; var widgetId = null;
        data = _.extend(data, {widget : widget });

        // try to find/identify sender
        if (data.widget) {
          _.each(_.pairs(this.widgets), function(w) {
            if (w[1] === data.widget) {
              widgetId = w[0];
              sender = w[1];
            }
          });
        }

        if (event == 'widget-ready' && sender !== null) {
          data["widgetId"] = widgetId;
          this.broadcast('page-manager-message', event, data);
        }
        else if (event == 'widget-selected') {
          this.pubsub.publish(this.pubsub.NAVIGATE, data.idAttribute, data.href);
        }
        else if (event == 'broadcast-payload'){
          this.broadcast('page-manager-message', event, data);
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