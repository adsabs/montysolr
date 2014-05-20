define(['backbone', 'marionette',
    'hbs!./templates/base-container', 'hbs!./templates/empty', 'bootstrap'
  ],
  function(Backbone, Marionette, BaseFacetContainer, EmptyFacetTemplate) {

    //if there is an error, show this
    var NoFacetsView = Backbone.Marionette.ItemView.extend({
      template: EmptyFacetTemplate
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
      template: BaseFacetContainer,

      /**
       * This will be the class in which the view is going to be wrapped
       */
      className: "facet-widget",

      /**
       * The container nested inside className object
       */
      itemViewContainer: ".facet-items",

      /**
       * What to use when there is no collection yet
       */
      emptyView: NoFacetsView,


      /**
       * events and callbacks this container provides
       */
      events: {
        //"click .main-caret": "toggleFacet",
        "click .facet-name > h5": "toggleFacet",
        "click .show-more": "showExtraItems"
      },

      // if we want to do some setup, ths is the way to go
      //constructor: function(){
      //  // do something here....
      //  BackBone.Marionette.CompositeView.prototype.constructor.apply(this, arguments);
      //},

      initialize: function (options) {

        options = options || {};

        this.itemViewOptions = Marionette.getOption(this, "itemViewOptions") || {};
        this.openByDefault = Marionette.getOption(this, "openByDefault");
        this.showOptions = Marionette.getOption(this, "showOptions");

        this._states = [];
      },


      /**
       * Called right after the view has been rendered
       */
      onRender: function() {
        if (this.openByDefault) {
          this.toggleFacet();
        }
        if (this.showOptions) {
          this.$(".facet-options").removeClass("hide");
        }
      },

      /**
       * Opens/closes the facet body - revealing whatever is inside
       *
       * @param e
       */
      toggleFacet: function (e) {
        var $caret = this.$(".main-caret");
        if ($caret.hasClass("item-open")) {
          $caret.removeClass("item-open");
          $caret.addClass("item-closed");
          this.$(".facet-body").addClass("hide");
        }
        else {
          $caret.removeClass("item-closed");
          $caret.addClass("item-open");
          this.$(".facet-body").removeClass("hide");
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
          this.$('.facet-name > h5').attr('title', err.msg);
        }
        this.$('.facet-name').addClass('error');
        this._states.push(500);
      },

      /**
       * The widget has requested data and is waiting for them to
       * arrive (we should indicate it)
       */
      handleWaiting: function() {
        // XXX:alex - we should do something better (like go into background;
        // overlay it....)
        this.$('.facet-body').addClass('waiting');
        this._states.push(300);
      },

      revertState: function(state) {

        if (state == 300) {
          this.$('.facet-body').removeClass('waiting');
        }
        else if (state == 500) {
          this.$('.facet-name').removeClass('error');
          this.$('.facet-name > h5').attr('title', "");
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