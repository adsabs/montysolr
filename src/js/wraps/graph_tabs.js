define(['js/widgets/tabs/tabs_widget', 'js/widgets/facet/factory'], function (TabsWidget, FacetFactory) {


  var yearGraphWidget = FacetFactory.makeGraphFacet("year")

  var citationGraphWidget = FacetFactory.makeGraphFacet("citation")


  var graphTabWidget = new TabsWidget(
    {tabs: [
      {title: "Years", widget: yearGraphWidget, id: "year-facet", default: true},
      {title: "Citations", widget: citationGraphWidget, id: "citations-facet"}
    ]
    });

  return graphTabWidget


});