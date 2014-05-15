define(['marionette', 'd3', 'hbs!./templates/item-checkbox', 'hbs!./templates/slider', 'hbs!./templates/graph', './collection', 'jquery-ui'
], function (Marionette, d3, FacetItemCheckboxTemplate, FacetSliderTemplate, FacetGraphTemplate, FacetCollection) {


  var BaseHierarchicalItemView = Marionette.CompositeView.extend({

    className: "item-view hide",

    itemViewContainer: ".child-facets",


    initialize: function (options) {

      //first, give the requesting facet a collection of its own
      this.collection = new FacetCollection([], {});

      this.events["click .facet-caret"] = "toggleChildren";
      this.events["click .show-more"] = "showExtraItems"

      this.on("itemview:selected", this.highlightParentFacet)
      this.on("itemview:unselected", this.unHighlightParentFacet)
    },

    highlightParentFacet: function () {
      this.$(".facet-caret").eq(0).addClass("active-style")
      this.trigger("selected")

    },

    unHighlightParentFacet: function () {
      //unhighlight the parent, if necessary
      if(this.$(".child-facets selected").length === 0 ){
        this.$(".facet-caret").eq(0).removeClass("active-style")
        this.trigger("unselected")
      }

    },

    defaultNumFacets: Marionette.getOption(this, "defaultNumFacets") || 5,

    onCompositeCollectionResetAdd: function () {

      if (this.collection.models.length) {
        //show initial number of facets
        var visible = 0;
        var $childFacets = this.$(".child-facets .item-view")
        if ($childFacets.length) {

          while (visible < this.defaultNumFacets) {

            $childFacets.eq(visible).removeClass("hide");
            visible++
          }
        }

        //unhide "show more" button if necessary
        if ($childFacets.filter(".hide").length) {
          //this confirms there are more facets to be shown, so present it as an option
          this.$(".child-facets + .show-more").removeClass("hide")
        }
      }
    },

    //adding the hier flag for hierarchical view
    serializeData : function () {
      var m = this.model.toJSON();
      m.hier = true;
      return m
    },

    //function to show or hide children
    toggleChildren : function (e) {

      e.stopPropagation();
      var $target = $(e.target)

      if ($target.hasClass("item-open")) {
        //just close the facet
        $target.removeClass("item-open").addClass("item-closed")
        //hiding .child-facets


      }
      else {
        if (this.collection && this.collection.models.length) {
          //reopening the toggle since the collection already has data
          $target.removeClass("item-closed").addClass("item-open")
          $($target.siblings()[1]).removeClass("hide");
          $($target.siblings()[2]).removeClass("hide")


        }
        else {
          //request facets
          this.trigger("requestChildData", this);

          $target.removeClass("item-closed").addClass("item-open");

          $($target.siblings()[1]).removeClass("hide")

        }
      }
    },

    showExtraItems: function (e) {
      e.stopPropagation();

      this.$(".item-view.hide").slice(0, this.defaultNumFacets).removeClass("hide");

      //because some facets are pruned, this might be triggered earlier than you would think
      if (!(this.$(".item-view.hide").length >= this.defaultNumFacets * 2)) {
        if (this.collection.moreFacets === true) {
          console.log("requesting more data")
          this.trigger("requestChildData")
        }
        else {
          console.log("NO MORE facets to show!")
          this.$("button.show-more").addClass("hide")
        }
      }

    }
  });

  return BaseHierarchicalItemView;

});

