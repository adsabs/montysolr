 define(["backbone"], function(Backbone) {

   IndividualFacetModel = Backbone.Model.extend({

     defaults: function() {
       return {
         title: undefined,
         value: undefined,
         //a facet will either change selected or newValue, not both
         selected: false,
         newValue: undefined
       }
     }
   });

   IndividualFacetCollection = Backbone.Collection.extend({

     model: IndividualFacetModel,

     initialize: function(models, options) {
       if (options && options.preprocess) {
         this.preprocess = options.preprocess
       }
     },

     titleCase: function(t) {
       var properI = t.replace(/\w\S*/g, function(txt) {
         return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
       });
       return properI
     },

     allCaps: function(t) {
       return t.toUpperCase();
     },

     removeSlash: function(t) {
      if(t.match(/\/([^\/]+)$/))
      {
       return t.match(/\/([^\/]+)$/)[1]
      }
      else {
        return t
      }
     },

   })


   return IndividualFacetCollection


 })
