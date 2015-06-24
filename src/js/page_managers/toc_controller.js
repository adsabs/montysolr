define([
    "marionette",
    './controller',
    './toc_widget'
  ],
  function (Marionette,
            BasicPageManagerController,
            TOCWidget
    ) {

    /*
     * need to provide a toc template for the toc view when you inherit from this
     * */

    var PageManagerController = BasicPageManagerController.extend({

      assemble: function() {
        BasicPageManagerController.prototype.assemble.apply(this, arguments);

        if (this.TOCEvents){
          //initiate the TOC view
          this.widgets.tocWidget = new TOCWidget(
            {template : Marionette.getOption(this, "TOCTemplate"),
            events : Marionette.getOption(this, "TOCEvents") });
        }
        else {
          //initiate the TOC view
          this.widgets.tocWidget = new TOCWidget({
            template : Marionette.getOption(this, "TOCTemplate")
          });
        }

        //insert the TOC nav view into its slot
        this.view.$(".nav-container").append(this.widgets.tocWidget.render().el);

        _.each(_.keys(this.widgets), function(w) {
          this.listenTo(this.widgets[w], "page-manager-event", _.bind(this.onPageManagerEvent, this, this.widgets[w]));
          this.broadcast('page-manager-message', 'new-widget', w);
        }, this);

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
        
        // try to find/identify sender
        if (widget) {
          _.each(_.pairs(this.widgets), function(w) {
            if (w[1] === widget) {
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
          this.pubsub.publish(this.pubsub.NAVIGATE, data.idAttribute, data);
        }
        else if (event == 'broadcast-payload'){
          this.broadcast('page-manager-message', event, data);
        }

        else if (event == "navigate"){
          this.pubsub.publish(this.pubsub.NAVIGATE, data.navCommand, data.sub);
        }

        else if (event == "apply-function"){
          data.func.apply(this);
        }

      },

      setActive : function(widgetName, subView){
        //now inform the widget of the subView to show
        if (subView && this.widgets[widgetName].setSubView instanceof Function){
          this.widgets[widgetName].setSubView(subView);
        }
        if (subView){
          widgetName = widgetName + "__" + subView;
        }
        this.widgets.tocWidget.collection.selectOne(widgetName);
      },

      onDestroy: function () {
        this.stopListening();
        this.widgets = {};
        this.view.destroy();
      }

    });

    return PageManagerController;
  });