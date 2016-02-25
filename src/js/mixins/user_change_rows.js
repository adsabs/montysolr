define([
  "marionette",
  'js/components/api_targets'
], function(
  Marionette,
  ApiTargets

  ){

  /*
  * use this model as a base for any widget that needs to request
  * varying numbers of records from solr and visualize/export/etc them
  * in some way.
  *
  * Currently used by metrics and visualization widgets (not export for now)
  * they set the relevant solr vals into the model in the
  * processResponse function
  * */



  var mixin = {};

  mixin.Model = Backbone.Model.extend({

    initialize : function(options){
      this.on("change:numFound", this.updateMax);
      this.on("change:rows", this.updateCurrent);

      if (!options.widgetName){
        throw new Error("need to configure with widget name so we can get limit/default info from api_targets._limits");
      }

      var defaults = {
          // returned by solr query: rows requested
          rows : undefined,
          // returned by solr query: total available
          numFound : undefined,
          // the smaller of either rows or numFound
          current : undefined,
          // the smaller of either numFound or maxAllowed
          max : undefined,
          //records that user has requested
          userVal: undefined
        }

        _.extend(defaults, ApiTargets._limits[options.widgetName]);
        this.defaults = function(){ return defaults }
        this.set(this.defaults());

    },

    updateMax : function() {
      var m = _.min([this.get("limit"), this.get("numFound")]);
      this.set("max", m);
    },

    updateCurrent : function(){
      this.set("current", _.min([this.get("rows"), this.get("numFound")]));
    }

  });

  return mixin;

});