
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

       initialize: function(options) {

         if (options && options.itemViewOptions) {
           this.itemViewOptions = options.itemViewOptions
         };

         if (options && options.defaultNumFacets) {
           this.defaultNumFacets = options.defaultNumFacets
         };

         if (options && options.openByDefault == true) {
           //open up the body of the facet and point caret down
           this.onRender = function() {
             this.toggleFacet();
           }
         };

         //for hierarchical child views
         this.on("all", function(e) {
           //  somewhere in the hierarchy of children views, a new view
           //  with a new collection has been added
           if (e.match("requestChildData")) {
             var view = arguments[arguments.length - 1];
             this.trigger("hierarchicalDataRequest", view);
           }
         });
       },

       emptyView: NoFacetsView,

       events: {
         "click .main-caret": "toggleFacet",
         "click .show-more": "showExtraItems"
       },

       showExtraItems: function() {
         this.$(".item-view.hide").slice(0, this.defaultNumFacets ).removeClass("hide");

         //because some facets are pruned, this might be triggered earlier than you would think
         if(!(this.$(".item-view.hide").length >= this.defaultNumFacets * 2)){
           this.trigger("moreDataRequested")
         }

       },

       toggleFacet: function(e) {

         $caret = this.$(".main-caret");

         if ($caret.hasClass("item-open")) {
           $caret.removeClass("item-open");
           $caret.addClass("item-closed");
           this.$(".logic-dropdown").addClass("hide");
           this.$(".facet-body").addClass("hide");


         } else {
           $caret.removeClass("item-closed");
           $caret.addClass("item-open");
           this.$(".logic-dropdown").removeClass("hide");
           this.$(".facet-body").removeClass("hide");

         }
       },
       template: baseFacetTemplate,

       className: "facet-widget",

       itemViewContainer: ".facet-items",

       defaultNumFacets: 5,

       onCompositeCollectionRendered : function(){
       //show initial number of facets
         var visible = 0;
         var $childFacets = this.$(".item-view")
         if($childFacets.length){

           while (visible < this.defaultNumFacets){

             $childFacets.eq(visible).removeClass("hide");
             visible++
           }

         }
         console.log(this, this.collection.moreFacets, $childFacets.length, this.defaultNumFacets )
       //unhide "show more" button if necessary
         if(this.collection.moreFacets && !($childFacets.length < this.defaultNumFacets) ){
           //this confirms there are more facets to be shown, so present it as an option
           this.$(".facet-items + .show-more").removeClass("hide")
         }
       }


     });

     ChangeApplyContainer = BaseContainer.extend({

       initialize: function(options) {
         //a change in the "newValue" attribute for any model
         //in the collection will trigger an autosubmit
         this.listenTo(this.collection, "change:newValue", this.submitFacet);
         BaseContainer.prototype.initialize.call(this, options);
       },

       submitFacet: function() {
         this.trigger("changeApplySubmit")
       }

     }),

     SelectLogicModel = BaseContainerModel.extend({
       defaults: function() {
         return {
           singleLogic: [
             "limit to",
             "exclude"
           ],
           multiLogic: [
             "and",
             "or",
             "exclude"
           ],
           selected: undefined,

           title: undefined
         }
       }
     });

     SelectLogicContainer = BaseContainer.extend({

       initialize: function(options) {

         //clear out logic template when collection is reset
         this.listenTo(this.collection, "reset", function() {
           this.$(".dropdown-menu").html(facetTooltipTemplate({
             noneSelected: true
           }))
         });

         this.on("itemview:select", this.handleLogic);

         BaseContainer.prototype.initialize.call(this, options);

       },

       events: function() {
         var addEvents;
         addEvents = {
           "click .dropdown-toggle": "toggleLogic",
           "click .dropdown-menu .close": "closeLogic",
           //for this container, any click on a logic input emits an immediate
           //new query event
           "change .facet-item": "showLogic",
           "change .logic-container input": "changeLogicAndSubmit"

         };
         return _.extend(_.clone(BaseContainer.prototype.events), addEvents)
       },

       template: logicFacetTemplate,

       changeLogicAndSubmit: function(e) {
         //close the logic dropdown
         this.closeLogic();
         var val = $(e.target).val();
         this.model.set("selected", val)

         //because model is nested, the events won't work by themselves
         this.trigger("SelectLogicSubmit")
       },

       closeLogic: function() {
         this.$(".dropdown").removeClass("open")

       },
       showLogic: function() {
         var numSelected;
         numSelected = this.handleLogic();
         if (numSelected > 0) {
           this.$(".dropdown").addClass("open")
         } else {
           this.$(".dropdown").removeClass("open")
         }
       },

       toggleLogic: function() {
         this.$(".dropdown").toggleClass("open")
       },

       handleLogic: function(model) {

         var selected = this.$("input:checked")
         var numSelected = selected.length;

         //open the dropdown
         if (numSelected === 1) {
           this.$(".dropdown-menu").html(facetTooltipTemplate({
             single: true,
             logic: this.model.get("singleLogic")
           }));

           this.$(".dropdown").addClass("open");

         } else if (numSelected > 1) {
          var multiLogic = this.model.get("multiLogic");
          if(multiLogic === "fullSet"){
            /*any multiple selection automatically grabs the full set */
            this.$(".dropdown-menu").html(facetTooltipTemplate({
             fullSet: true
           }))
          }
          else {
            this.$(".dropdown-menu").html(facetTooltipTemplate({
             multiLogic: true,
             logic: multiLogic
           }))

          }  
          this.$(".dropdown").addClass("open");

         } else {

           this.$(".dropdown-menu").html(facetTooltipTemplate({
             noneSelected: true
           }))
           this.$(".dropdown").removeClass("open")

         }
         return numSelected
       }

     });


     return {
       SelectLogicContainer: SelectLogicContainer,
       ChangeApplyContainer: ChangeApplyContainer,
       SelectLogicModel: SelectLogicModel,
       BaseContainerModel: BaseContainerModel
     }

   })
