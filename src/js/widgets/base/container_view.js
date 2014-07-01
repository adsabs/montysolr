define(['backbone', 'marionette',
    'hbs!./templates/widget-container', 'hbs!./templates/empty'
  ],
  function(Backbone, Marionette, WidgetContainerTemplate, EmptyTemplate) {

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
       * This will be the class in which the view is going to be wrapped
       */
      className: "widget-container",

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
        "click .widget-name:first > h5": "toggleWidget",
        "click .widget-options.top:first": "onClickOptions",
        "click .widget-options.bottom:first": "onClickOptions"
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
        }
        if (this.showOptions) {
          this.$(".widget-options:first").removeClass("hide");
          this.$(".widget-options.bottom:first").removeClass("hide");
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
          this.$(".widget-options:first").addClass("hide")
        }
        else {
          $caret.removeClass("item-closed");
          $caret.addClass("item-open");
          this.$(".widget-body:first").removeClass("hide");
          this.$(".widget-options:first").removeClass("hide")

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

      },

      /**
       * This is the entry point for controllers to provide
       * feedback to the user; the container will receive a
       * {SanityMessage} and can act upon it
       */
      handleSanity: function(sanity) {
        if (sanity.ok) {
          if (this._states.length > 0) {
            var self = this;
            _.each(this._states, function(state) {
              self.revertState(state);
            });
            this._states = [];
          }
        }
        else if (sanity.error) {
          this.handleError(sanity.error);
        }
        else if (sanity.waiting) {
          this.handleWaiting();
        }
        else {
          throw new Error("Unknown sanity msg: ", sanity);
        }
      },

      /**
       * By default, this widget will indicate error by changing its
       * color
       *
       * @param err
       */
      handleError: function(err) {
        if (err && err.msg) {
          this.$('.widget-name:first > h5').attr('title', err.msg);
        }
        this.$('.widget-name:first').addClass('error');
        this._states.push(500);
      },

      /**
       * The widget has requested data and is waiting for them to
       * arrive (we should indicate it)
       */
      handleWaiting: function() {
        // XXX:alex - we should do something better (like go into background;
        // overlay it....)
        this.$('.widget-body').addClass('waiting');
        this._states.push(300);
      },

      revertState: function(state) {

        if (state == 300) {
          this.$('.widget-body:first').removeClass('waiting');
        }
        else if (state == 500) {
          this.$('.widget-name:first').removeClass('error');
          this.$('.widget-name:first > h5').attr('title', "");
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