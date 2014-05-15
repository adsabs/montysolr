define(["backbone"], function (Backbone) {

  var FacetModel = Backbone.Model.extend({

    defaults: function () {
      return {
        title: undefined,
        value: undefined,
        //a facet will either change selected or newValue, not both
        selected: false,
        newValue: undefined
      }
    }
  });

  return FacetModel;
});