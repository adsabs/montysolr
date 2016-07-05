define([
  'js/widgets/facet/widget',
  'js/widgets/facet/graph-facet/widget'
], function (
  FacetWidget,
  BaseGraphWidget
  ) {

  var FacetFactory = {

    makeBasicCheckboxFacet: function (options) {
      var defaultOptions = {
        facetTitle : options.facetTitle,
        displayNum: 5,
        maxDisplayNum: 100,
        facetField : options.facetField,
        openByDefault: false,
        showOptions: true,
        hierMaxLevels: 1
      };
      return new FacetWidget( _.extend(defaultOptions, options) );
    },

    makeHierarchicalCheckboxFacet: function (options) {
      options.hierMaxLevels = 2;
      options.defaultQueryArguments = options.defaultQueryArguments || {};
      options.defaultQueryArguments["facet.prefix"] =  "0/";
      return this.makeBasicCheckboxFacet.apply(this, arguments);
    },

    makeGraphFacet: function (options) {
      options = _.extend({openByDefault: true}, options);
      return new BaseGraphWidget(options);
    }
  };
  return FacetFactory
});
