define([
  'js/widgets/facet/factory'
], function (
  FacetFactory
  ) {

  return function() {
    var widget = FacetFactory.makeHierarchicalCheckboxFacet({
      facetField: "author_facet_hier",
      facetTitle: "Authors",
      openByDefault: true,
      logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}
    });

    return widget;
  };

});
