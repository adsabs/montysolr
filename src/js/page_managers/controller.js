define([
    'underscore',
    "marionette",
    "hbs!./templates/results-page-layout",
    'hbs!./templates/results-control-row',
    'js/widgets/base/base_widget',
    './three_column_view',
    './view_mixin',
    'js/mixins/dependon'
  ],
  function (_,
            Marionette,
            pageTemplate,
            controlRowTemplate,
            BaseWidget,
            ThreeColumnView,
            PageManagerViewMixin,
            Dependon
            ) {

    var PageManagerController = BaseWidget.extend({

      initialize: function (options) {
        this.widgets = {};
        this.initialized = false;
        this.widgetId = null;
        this.assembled = false;
        _.extend(this, _.pick(options, ['debug', 'widgetId']));
      },


      /**
       * Necessary step: during activation we'll collect list of widgets
       * that were referenced by the template (and store them for future
       * reference)
       *
       * @param beehive
       */
      activate: function (beehive) {
        this.setBeeHive(beehive);
        this.debug = beehive.getDebug(); // XXX:rca - think of st better
        this.view = this.createView({debug : this.debug, widgets: this.widgets});
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
          if (!app.hasWidget(widgetName)) {
            delete that.widgets[widgetName];
            return;
          }

          var widget = app.getWidget(widgetName);
          if (widget) {
            // maybe it is a page-manager (this is a security hole though!)
            if (widget.assemble) {
              widget.assemble(app);
            }
            $(that.widgets[widgetName]).empty().append(widget.render().el);
            that.widgets[widgetName] = widget;
          }
        });
      },

      /**
       * Display the widgets that are under our control (and hide all the rest)
       *
       * @param pageName
       * @returns {exports.el|*|queryBuilder.el|p.el|AppView.el|view.el}
       */
      show: function(pageName){

        var self = this;

        if (!pageName) {
          this.showAll();
        }
        else {
          this.hideAll();

          // show just those that are requested + always show alerts widget
          var args = [].slice.apply(arguments);
          _.each(args, function(widgetName) {
            if (self.widgets[widgetName]) {
              var widget = self.widgets[widgetName];

              //don't call render each time or else we
              //would have to re-delegate widget events
              var $wcontainer = self.view.$el.find('[data-widget="' + widgetName + '"]');
              if ($wcontainer.length) {
                var d = $wcontainer.data('debug');
                if ( d !== undefined && d && !self.debug) {
                  return; // skip widgets that are there only for debugging
                }
                $wcontainer.append(widget.el ? widget.el : widget.view.el);
                try {
                  self.widgets[widgetName].triggerMethod('show');
                }
                catch (e) {
                  console.error('Error when displaying widget: ' + widgetName, e.message, e.stack);
                }

              }
              else {
                console.warn('Cannot insert widget: ' + widgetName + ' (no selector [data-widget="' + widgetName + '"])');
              }
            }
            else {
              if (self.debug)
                console.error("Cannot show widget: " + widgetName + "(because, frankly... there is no such widget there!)");
            }
          });
        }

        this.triggerMethod("show");

        return this.view;
      },

      hideAll: function() {
        // hide all widgets that are under our control
        _.each(this.widgets, function(w) {
          if (w.noDetach)
              return
          if ('detach' in w && _.isFunction(w.detach)) {
            w.detach();
          }
          else if (w.view && w.view.$el) {
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
      },

      /**
       * broadcast the event to all other managed widgets
       * (call trigger on them)
       */
      broadcast: function(){
        var args = arguments;
        _.each(this.widgets, function(widget, widgetName) {
          widget.trigger.apply(widget, args);
        }, this);
      }

    });

    _.extend(PageManagerController.prototype, PageManagerViewMixin, Dependon.BeeHive, {
      // override the pubsub - we give every child the same (hardened)
      // instance of pubsub
      getPubSub: function() {
        if (this._ps && this.hasPubSub())
          return this._ps;
        this._ps = this.getBeeHive().getHardenedInstance().getService('PubSub');
        return this._ps;
      }
    });
    return PageManagerController;
  });