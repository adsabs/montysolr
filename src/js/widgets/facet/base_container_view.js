define(['backbone', 'marionette',
    'hbs!./templates/base-container', 'hbs!./templates/empty', 'bootstrap'
  ],
  function(Backbone, Marionette, BaseFacetContainer, EmptyFacetTemplate) {

    //if there is an error
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

      ModelClass: BaseContainerModel,

      initialize: function (options) {

        options = options || {};

        this.itemViewOptions = options.itemViewOptions || {};

        this.defaultNumFacets = options.defaultNumFacets || 5;

        this.itemViewOptions.defaultNumFacets = this.defaultNumFacets;

        if (options.openByDefault == true) {
          //open up the body of the facet and point caret down
          this.onRender = function () {
            this.toggleFacet();
          }
        }

        //for hierarchical child views
        this.on("all", function (e) {
          //  somewhere in the hierarchy of children views, a new view
          //  with a new collection has been added
          if (e.match("requestChildData")) {
            var view = arguments[arguments.length - 1];
            this.trigger("hierarchicalDataRequest", view);
          }

          //so that there is special styling when a facet might be applied
          this.on("itemview:selected", function () {
            this.$(".facet-meta i").addClass("active-style")

          });

          this.on("itemview:unselected", function () {
            //specifically for logic dropdowns
            //only remove if there is no more selected
            if (this.$(".selected").length === 0) {
              this.$(".logic-dropdown i").addClass("inactive-style").removeClass("active-style");

              this.$(".facet-meta i").removeClass("active-style");

            }

          })
        });
      },

      emptyView: NoFacetsView,

      events: {
        "click .main-caret": "toggleFacet",
        "click .show-more": "showExtraItems",
      },

      showExtraItems: function () {
        this.$(".item-view.hide").slice(0, this.defaultNumFacets).removeClass("hide");

        //because some facets are pruned, this might be triggered earlier than you would think
        if (!(this.$(".item-view.hide").length >= this.defaultNumFacets * 2)) {
          this.trigger("moreDataRequested")
        }

      },

      toggleFacet: function (e) {

        $caret = this.$(".main-caret");

        if ($caret.hasClass("item-open")) {
          $caret.removeClass("item-open");
          $caret.addClass("item-closed");
          this.$(".logic-dropdown").addClass("hide");
          this.$(".facet-body").addClass("hide");

        }
        else {
          $caret.removeClass("item-closed");
          $caret.addClass("item-open");
          this.$(".logic-dropdown").removeClass("hide");
          this.$(".facet-body").removeClass("hide");

        }
      },
      template: BaseFacetContainer,

      className: "facet-widget",

      itemViewContainer: ".facet-items",

      onCompositeCollectionResetAdd: function () {

        if (this.collection.models.length) {
          //show initial number of facets
          var visible = 0;
          var $childFacets = this.$(".item-view")
          if ($childFacets.length) {

            while (visible < this.defaultNumFacets) {

              $childFacets.eq(visible).removeClass("hide");
              visible++
            }

          }
          //unhide "show more" button if necessary
          if ($childFacets.filter(".hide").length) {
            //this confirms there are more facets to be shown, so present it as an option
            this.$(".facet-items + .show-more").removeClass("hide")
          }
        }

      }


    });

    return BaseContainerView;
});