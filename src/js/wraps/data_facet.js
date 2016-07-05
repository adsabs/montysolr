define(['js/widgets/facet/factory' ], function ( FacetFactory) {

  return function() {
    var widget = FacetFactory.makeBasicCheckboxFacet({
      facetField: "data_facet",
      facetTitle: "Data",
      logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}

    });
    return widget;
  };

});