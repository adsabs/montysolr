define([
  "marionette"
], function(
  Marionette
  ){

  var mixin = {};


  mixin.Model = Backbone.Model.extend({

    initialize : function(){
      this.on("change:numFound", this.updateMax);
      this.on("change:rows", this.updateCurrent);
    },

    updateMax : function() {
      var m = _.min([this.maxAllowed, this.get("numFound")]);
      this.set("max", m);
    },

    updateCurrent : function(){
      this.set("current", _.min([this.get("rows"), this.get("numFound")]));
    },

    /*config*/
    maxAllowed : 1000,

    defaults : function(){
      return {

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
    }
  });


  return mixin;


})