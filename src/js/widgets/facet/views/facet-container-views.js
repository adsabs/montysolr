
//XXX:rca - if you think of facet containers as containers, then they are simply
// views that contain other 'arbitrary' views; then you can remove the imports hbs!../templates/logic-facet-container
// and make sure that the factory method is setting appropriate view *inside* container
// and remove the imports from here

define(['backbone', 'marionette',
     'hbs!./templates/base-facet-container', 'hbs!./templates/logic-facet-container',
     'hbs!./templates/facet-tooltip', 'hbs!./templates/empty-facet', 'bootstrap'
   ],
   function(Backbone, Marionette, baseFacetTemplate, logicFacetTemplate,
     facetTooltipTemplate, emptyFacetTemplate) {

     //if there is an error
     NoFacetsView = Backbone.Marionette.ItemView.extend({
       template: emptyFacetTemplate
     });

     //this holds the title of the facet as it should be shown in the ui
     BaseContainerModel = Backbone.Model.extend({
       defaults: function() {
         return {
           title: undefined
         }
       }
     });
     BaseContainer = Backbone.Marionette.CompositeView.extend({

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
             if (this.$(".selected").length === 0){
               this.$(".logic-dropdown i").addClass("inactive-style").removeClass("active-style");

               this.$(".facet-meta i").removeClass("active-style");

             }

           })
         });
       },

       emptyView: NoFacetsView,

       events: {
         "click .main-caret": "toggleFacet",
         "click .show-more" : "showExtraItems",
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
       template   : baseFacetTemplate,

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

     ChangeApplyContainer = BaseContainer.extend({

       initialize: function (options) {
         //a change in the "newValue" attribute for any model
         //in the collection will trigger an autosubmit
         this.listenTo(this.collection, "change:newValue", this.submitFacet);
         BaseContainer.prototype.initialize.call(this, options);
       },

       submitFacet: function () {
         this.trigger("changeApplySubmit")
       }

     }),

       SelectLogicModel = BaseContainerModel.extend({
         defaults: function () {
           return {
             singleLogic: [
               "limit to", "exclude", ],
             multiLogic : [
               "and", "or", "exclude"
             ],
             selected   : undefined,

             title: undefined,
           }
         }
       });

     SelectLogicContainer = BaseContainer.extend({

       initialize: function (options) {

         //clear out logic template when collection is reset
         this.listenTo(this.collection, "reset", function () {
           this.$(".dropdown-menu").html(facetTooltipTemplate({
             noneSelected: true
           }))
         });

         this.on("itemview:select", this.handleLogic);

         BaseContainer.prototype.initialize.call(this, options);

       },

       events: function () {
         var addEvents;
         addEvents = {
           "click .dropdown-toggle"       : "toggleLogic",
           "click .dropdown-menu .close"  : "closeLogic",
           //for this container, any click on a logic input emits an immediate
           //new query event
           "change .facet-item"           : "showLogic",
           "change .logic-container input": "changeLogicAndSubmit"

         };
         return _.extend(_.clone(BaseContainer.prototype.events), addEvents)
       },

       template: logicFacetTemplate,

       changeLogicAndSubmit: function (e) {
         //close the logic dropdown
         this.closeLogic();
         var val = $(e.target).val();
         this.model.set("selected", val)

         //because model is nested, the events won't work by themselves
         this.trigger("SelectLogicSubmit")
       },

       closeLogic: function () {
         this.$(".dropdown").removeClass("open")

       },
       showLogic : function () {
         var numSelected;
         numSelected = this.handleLogic();
         if (numSelected > 0) {
           this.$(".dropdown").addClass("open")
           this.$(".logic-dropdown i").removeClass("inactive-style")
         }
         else {
           this.$(".dropdown").removeClass("open")
         }
       },

       toggleLogic: function () {
         this.$(".dropdown").toggleClass("open")
       },

       handleLogic: function () {

         var selected = this.$("input:checked")
         var numSelected = selected.length;

         if (numSelected >= 1) {
           //highlight title
           this.trigger("itemview:facet:active")
         }
         ;

         //open the dropdown
         if (numSelected === 1) {
           this.$(".dropdown-menu").html(facetTooltipTemplate({
             single: true,
             logic : this.model.get("singleLogic")
           }));

           this.$(".dropdown").addClass("open");

         }
         else if (numSelected > 1) {
           var multiLogic = this.model.get("multiLogic");
           if (multiLogic === "fullSet") {
             /*any multiple selection automatically grabs the full set */
             this.$(".dropdown-menu").html(facetTooltipTemplate({
               fullSet: true,
             }))
           }
           else {
             this.$(".dropdown-menu").html(facetTooltipTemplate({
               multiLogic: true,
               logic     : multiLogic
             }))

           }
           this.$(".dropdown").addClass("open");

         }
         else {

           this.$(".dropdown-menu").html(facetTooltipTemplate({
             noneSelected: true
           }))
           this.$(".dropdown").removeClass("open");
           //deactivating styles
           this.trigger("itemview:facet:inactive")

         }
         return numSelected
       }

     });

     return {
       SelectLogicContainer: SelectLogicContainer,
       ChangeApplyContainer: ChangeApplyContainer,
       SelectLogicModel    : SelectLogicModel,
       BaseContainerModel  : BaseContainerModel
     }

})
