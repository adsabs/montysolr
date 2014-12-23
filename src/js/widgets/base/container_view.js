define([
    'backbone',
    'marionette',
    'hbs!./templates/widget-container',
    'hbs!./templates/empty'
  ],
  function(
    Backbone,
    Marionette,
    WidgetContainerTemplate,
    EmptyTemplate
    ) {

    //if there is an error, show this
    var EmptyView = Backbone.Marionette.ItemView.extend({
      template: EmptyTemplate
    });

    //this holds the title of the facet as it should be shown in the ui
    var BaseContainerModel = Backbone.Model.extend({
      defaults: function () {
        return {
          title: undefined
        }
      }
    });

    var BaseContainerView = Backbone.Marionette.CompositeView.extend({

      /**
       * Template for the container
       */
      template: WidgetContainerTemplate,

      /**
       * These will be the classes in which the view is going to be wrapped
       */
      className: "widget-container s-widget-container",

      /**
       * The container nested inside className object
       */
      itemViewContainer: ".widget-body",

      /**
       * What to use when there is no collection yet
       */
      emptyView: EmptyView,


      /**
       * events and callbacks this container provides
       */
      events: {

        "click .widget-name > h5": "toggleWidget",
        "click .widget-options.top": "onClickOptions",
        "click .widget-options.bottom": "onClickOptions",
        "click .widget-name .main-caret" : "toggleWidget",

      },

      // if we want to do some setup, ths is the way to go
      //constructor: function(){
      //  // do something here....
      //  BackBone.Marionette.CompositeView.prototype.constructor.apply(this, arguments);
      //},

      initialize: function (options) {

        options = options || {};

        //this.itemViewOptions = Marionette.getOption(this, "itemViewOptions") || {};
        this.openByDefault = Marionette.getOption(this, "openByDefault");
        this.showOptions = Marionette.getOption(this, "showOptions");

        this.displayNum = Marionette.getOption(this, "displayNum") || 5;
        this.maxDisplayNum = Marionette.getOption(this, "maxDisplayNum") || 200;

        this._states = [];
      },

      itemViewOptions: function (model, index) {
        var additionalOptions = Marionette.getOption(this, "additionalItemViewOptions") || {};
        //if this is the initial round, hide fetchnum - displaynum
        if (index < this.displayNum) {
          return _.extend({hide: false}, additionalOptions);
        }
        else {
          return _.extend({hide: true}, additionalOptions);
        }
      },

      /**
       * Called right after the view has been rendered
       */
      onRender: function() {
        this._onRender();
      },

      _onRender: function() {
        if (this.openByDefault) {
          this.toggleWidget();

          if (this.showOptions) {
            this.$(".widget-options:first").removeClass("hide");
            this.$(".widget-options.bottom:first").removeClass("hide");
          }
        }

      },

      /**
       * Opens/closes the facet body - revealing whatever is inside
       *
       * @param e
       */
      toggleWidget: function (e) {
        if (e){
          e.stopPropagation();
        }
        var $caret = this.$(".main-caret:first");
        if ($caret.hasClass("item-open")) {
          $caret.removeClass("item-open");
          $caret.addClass("item-closed");
          this.$(".widget-body:first").addClass("hide");
          this.$(".widget-options.bottom:first").addClass("hide");
          this.$(".widget-options.top:first").addClass("hide")

        }
        else {
          $caret.removeClass("item-closed");
          $caret.addClass("item-open");
          this.$(".widget-body:first").removeClass("hide");
          this.$(".widget-options.bottom:first").removeClass("hide");
          this.$(".widget-options.top:first").removeClass("hide");

        }
      },


      onClickOptions: function(ev) {
        if (ev && ev.target) {
          var $el = $(ev.target);
          var text = $el.text().trim();
          var tgt = $el.attr('wtarget');
          ev.preventDefault();
          if (tgt) {
            this.triggerMethod(tgt, ev);
          }
          else if (text) {
            this.triggerMethod(text.replace(' ', ''), ev);
          }
        }

      }

    });

    /**
     * This is our own convention to include the model class associated
     * with this container; it can be overriden and is used by factory
     * constructors only (ie. not inside Marionette)
     */
    BaseContainerView.ContainerModelClass = BaseContainerModel;

    return BaseContainerView;
  });