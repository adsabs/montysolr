define(['js/widgets/facet/factory' ], function ( FacetFactory) {

  return function() {
    var widget = FacetFactory.makeBasicCheckboxFacet({
      facetField: "vizier_facet",
      facetTitle: "Vizier Tables",
      logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}

    });
    return widget;
  };

});