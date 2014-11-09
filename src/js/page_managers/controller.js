define([
    "marionette",
    "hbs!./templates/results-page-layout",
    'hbs!./templates/results-control-row',
    'js/widgets/base/base_widget',
    './three_column_view',
    './view_mixin'
  ],
  function (Marionette,
            pageTemplate,
            controlRowTemplate,
            BaseWidget,
            ThreeColumnView,
            PageManagerViewMixin
            ) {

    var PageManagerController = BaseWidget.extend({

      initialize: function (options) {
        this.widgets = {};
        this.initialized = false;
        this.widgetId = null;
        this.assembled = false;
        _.extend(this, _.pick(options, ['debug', 'widgetId']));
      },

      setWidgetId: function(n) {
        this.widgetId = n;
      },

      /**
       * Creates the view: the pagemanger view (from the template
       * that references widgets)
       *
       * @param options
       * @returns {ThreeColumnView}
       */
      createView: function(options) {
        return new ThreeColumnView(options);
      },

      /**
       * Necessary step: during activation we'll collect list of widgets
       * that were referenced by the template (and store them for future
       * reference)
       *
       * @param beehive
       */
      activate: function (beehive) {
        this.pubsub = beehive.getHardenedInstance().Services.get('PubSub');
        this.debug = beehive.getDebug(); // XXX:rca - think of st better
        this.view = this.createView({debug : this.debug, widgets: this.widgets});
      },

      /**
       * This is the necessary step to activate the page manager; we'll render
       * the widgets and append them inside the appropriate places inside the
       * template
       *
       * @param app
       */
      assemble: function(app) {
        if (this.assembled)
          return this.view.el;

        this.assembled = true;
        this.view.render();

        var that = this;
        _.extend(that.widgets, that.getWidgetsFromTemplate(that.view.$el));

        _.each(_.keys(that.widgets), function(widgetName) {
          var widget = app.getWidget(widgetName);
          if (widget) {
            // maybe it is a page-manager (this is a security hole though!)
            if (widget.assemble) {
              widget.assemble(app);
            }
            $(that.widgets[widgetName]).empty().append(widget.render().el);
            that.widgets[widgetName] = widget;
          }
          else {
            console.warn("Widget " + widgetName + " is not available (ignoring it)");
            delete that.widgets[widgetName];
          }
        });
      },

      /**
       * Display the widgets that are under our control (and hide all the rest)
       *
       * @param pageName
       * @returns {exports.el|*|queryBuilder.el|p.el|AppView.el|view.el}
       */
      show: function(pageName) {

        var self = this;

        if (!pageName) {
          this.showAll();
        }
        else {
          this.hideAll();
          // show just those that are requested
          _.each(arguments, function(widgetName) {
            if (self.widgets[widgetName]) {
              var widget = self.widgets[widgetName];

              //don't call render each time or else we
              //would have to re-delegate widget events
              self.view.$el.find('[data-widget="' + widgetName + '"]').append(widget.el ? widget.el : widget.view.el);
              self.widgets[widgetName].triggerMethod('show');
            }
            else {
              console.error("Cannot show widget: " + widgetName + "(because, frankly... there is no such widget there!)");
            }
          });
        }

        return this.view;
      },

      hideAll: function() {
        // hide all widgets that are under our control
        _.each(this.widgets, function(w) {
          if (w.view && w.view.$el) {
            w.view.$el.detach();
          }
          else if (w.$el) {
            w.$el.detach();
          }
        });
        return this.view;
      },

      showAll: function() {
        var self = this;
        // show just those that are requested
        _.each(_.keys(self.widgets), function(widgetName) {
            var widget = self.widgets[widgetName];
            //don't call render each time or else we
            //would have to re-delegate widget events
            self.view.$el.find('[data-widget="' + widgetName + '"]').append(widget.el ? widget.el : widget.view.el);
            self.widgets[widgetName].triggerMethod('show');
        });
        return this.view;
      }

    });

    _.extend(PageManagerController.prototype, PageManagerViewMixin);
    return PageManagerController;
  });