define(['js/widgets/facet/factory' ], function ( FacetFactory) {

  return function() {
    var widget = FacetFactory.makeBasicCheckboxFacet({
      facetField: "doctype",
      facetTitle: "Publication Type",
      logicOptions: {single: ['limit to', 'exclude'], multiple: ["and", "or", "exclude"]}
    });

    return widget;
  };

});
