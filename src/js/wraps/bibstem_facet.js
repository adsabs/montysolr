define(['js/widgets/facet/factory' ], function ( FacetFactory) {

  return function() {
    var widget = FacetFactory.makeBasicCheckboxFacet({
      facetField: "bibstem_facet",
      facetTitle: "Publications",
      openByDefault: false,
      logicOptions: {single: ['limit to', 'exclude'], multiple: ["or", "exclude"]}
    });
    return widget;
  };

});