define(['underscore', 'jquery', 'js/widgets/facet/factory'],
  function(
    _,
    $,
    FacetFactory) {

    var authorFacets = FacetFactory.makeHierarchicalCheckboxFacet({
      facetField: "author_facet_hier",
      facetTitle: "Authors",
      openByDefault: true,
      logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']},
      responseProcessors: [
        function(v) {var vv = v.split('/'); return vv[vv.length-1]}
      ]
    });

    return authorFacets;

});