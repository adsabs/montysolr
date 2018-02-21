define(['underscore', 'js/widgets/facet/factory' ], function (_, FacetFactory) {

  return function() {
    var widget = FacetFactory.makeBasicCheckboxFacet({
      facetField: "bibgroup_facet",
      facetTitle: "Bib Groups",
      logicOptions: {single: ['limit to', 'exclude'], multiple: ["and", "or", "exclude"]},
      preprocessors: [
        function (data) {

          // swap the value for the name
          // this makes sure that any entries with `/` are correctly shown
          return _.map(data, function (o) {
            if (o.value.indexOf('/') > -1) {
              return _.assign(o, {name: o.value});
            }
            return o;
          });
        }
      ]
    });

    return widget;
  };

});
