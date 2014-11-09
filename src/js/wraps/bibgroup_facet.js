define(['js/widgets/facet/factory' ], function ( FacetFactory) {

  return function() {
    var widget = FacetFactory.makeBasicCheckboxFacet({
      facetField: "bibgroup_facet",
      facetTitle: "Bib Groups",
      openByDefault: false,
      logicOptions: {single: ['limit to', 'exclude'], multiple: ["or", "exclude"]}
    });

    return widget;
  };

});