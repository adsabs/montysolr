define(['backbone', 'marionette',
    'hbs!./templates/item-checkbox'
  ],
  function(Backbone, Marionette, ItemCheckBoxTemplate) {
    var FacetCollectionView = Marionette.CollectionView.extend({

      /**
       * The view will be inside div.[className]
       */
      className: "item-view",

      /**
       * You will need to provide the template of your choice
       * for the view to work
       */
      template: ItemCheckBoxTemplate,

      /**
       * The container nested inside className object
       */
      itemViewContainer: ".facet-items"

      /*
      // XXX:rca this was inside base-contair: initialize

      this.on("all", function (e) {
        //  somewhere in the hierarchy of children views, a new view
        //  with a new collection has been added
        if (e.match("requestChildData")) {
          var view = arguments[arguments.length - 1];
          this.trigger("hierarchicalDataRequest", view);
        }

        // XXX:rca - does this keep adding listeners multiple
        // times?

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

    showExtraItems: function () {
      this.$(".item-view.hide").slice(0, this.defaultNumFacets).removeClass("hide");

      //because some facets are pruned, this might be triggered earlier than you would think
      if (!(this.$(".item-view.hide").length >= this.defaultNumFacets * 2)) {
        this.trigger("moreDataRequested")
      }

    },

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
    */

    });

    return FacetCollectionView;
  });