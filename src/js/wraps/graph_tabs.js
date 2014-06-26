define(['js/widgets/tabs/tabs_widget', 'js/widgets/facet/factory'], function (TabsWidget, FacetFactory) {


  var citationsGraphWidget = FacetFactory.makeGraphFacet({
    facetField: "citation_count",
    facetTitle: "Citations",
    xAxisTitle: "Citation Count",
    openByDefault: true
  });

  var yearGraphWidget = FacetFactory.makeGraphFacet({
    facetField: "year",
    facetTitle: "Articles per year",
    xAxisTitle: "Year",
    openByDefault: true
  });


  var graphTabWidget = new TabsWidget(
    {tabs: [
      {title: "Years", widget: yearGraphWidget, id: "year-facet", default: true},
      {title: "Citations", widget: citationsGraphWidget, id: "citations-facet"}
    ]
    });

  return graphTabWidget


});