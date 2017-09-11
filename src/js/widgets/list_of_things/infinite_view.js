/**
 * Infinitely growing view of items
 *
 * ( **** NOT CURRENTLY USED ANYWHERE *** )
 *
 */

define([
    'marionette',
    'backbone',
    'hbs!js/widgets/list_of_things/templates/expanding-item-template',
    'hbs!js/widgets/list_of_things/templates/expanding-results-container-template',
  ],

  function (Marionette,
            Backbone,
            ItemTemplate,
            ResultsContainerTemplate
    ) {

    var ItemView = Marionette.ItemView.extend({

      //should it be hidden initially?
      className: function () {
        if (Marionette.getOption(this, "hide") === true) {
          return "hide row results-item"
        } else {
          return "row results-item"
        }
      },

      template: ItemTemplate,

      /**
       * This method prepares data for consumption by the template
       *
       * @returns {*}
       */
      serializeData: function () {
        var data = this.model.toJSON();
        return data;
      },

      events: {
        'change input[name=identifier]': 'toggleSelect'
      },

      toggleSelect: function () {
        this.$el.toggleClass("chosen");
      }

    });

    var ListView = Marionette.CompositeView.extend({

      initialize: function (options) {
        this.displayNum = options.displayNum;
        this.paginator = options.paginator;
      },


      className: "list-of-things",
      childView: ItemView,

      childViewOptions: function (model, index) {
        //if this is the initial round, hide fetchnum - displaynum
        if (this.paginator.getCycle() <= 1 && (index < this.displayNum)) {
          return {}
        }
        else {
          //otherwise, hide everything
          return {
            hide: true
          }
        }
      },

      childViewContainer: ".results-list",
      events: {
        "click .load-more-results": "fetchMore",
        "click .show-details": "showDetails"
      },

      template: ResultsContainerTemplate,

      fetchMore: function (ev) {
        if (ev)
          ev.stopPropagation();
        this.trigger('fetchMore', this.$(".results-item").filter(".hide").length);
      },

      /**
       * Displays loaded, but hidden items
       *
       * @param howMany
       */
      displayMore: function (howMany) {
        this.$(".results-item").filter(".hide").slice(0, howMany).removeClass("hide");
      },

      /**
       * Hides the area where 'show more' button lives; this is needed for
       * the pagination widged
       *
       * @param text
       */
      disableShowMore: function (text) {
        this.$('.load-more:first').addClass('hide');
      },

      /**
       * Displays the area where 'show more' button lives; this is needed for
       * the pagination widged
       *
       * @param text
       */
      enableShowMore: function (text) {
        this.$('.load-more:first').removeClass('hide');
      },

      /**
       * Un/Hides the area where details controls are
       *
       * @param text
       */
      toggleDetailsControls: function (visible) {
        if (visible) {
          this.$(".results-controls:first").removeClass('hide');
        }
        else {
          this.$(".results-controls:first").addClass('hide');
        }

      },

      /**
       * Displays the are inside of every item-view
       * with details (this place is normally hidden
       * by default)
       */
      showDetails: function (ev) {
        if (ev)
          ev.stopPropagation();
        this.$(".more-info").toggleClass("hide");
        if (this.$(".more-info").hasClass("hide")) {
          this.$(".show-details").text("Show details");
        } else {
          this.$(".show-details").text("Hide details");
        }
      }

    });
    return ListView;
  });
