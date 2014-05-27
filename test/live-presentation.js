require(['js/components/beehive', 'js/services/pubsub', 'js/components/query_mediator', 'js/services/api',
  'jquery', 'underscore',
  'js/widgets/search_bar/search_bar_widget', 'js/widgets/results/widget',
  'js/widgets/facet/factory', 'js/widgets/query_info/query_info_widget'
], function(BeeHive, PubSub, QueryMediator, Api, $, _, SearchBar, ResultsWidget, FacetFactory, QueryInfoWidget) {


  var beehive = new BeeHive();
  beehive.addService('PubSub', new PubSub());
  beehive.addService('Api', new Api());
  var queryMediator = new QueryMediator();
  queryMediator.activate(beehive);

  var queryInfo = new QueryInfoWidget();

  var searchBar = new SearchBar();
  var results = new ResultsWidget({pagination: {rows: 40, start:0}});
  var citationsGraphWidget = FacetFactory.makeGraphFacet({
    facetField: "citation_count",
    facetTitle: "Citations",
    xAxisTitle: "Citation Count",
    openByDefault: true
  });

  var authorFacets = FacetFactory.makeHierarchicalCheckboxFacet({
    facetField: "author_facet_hier",
    facetTitle: "Authors",
    openByDefault: true,
    logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}
  });
  var keywords = FacetFactory.makeBasicCheckboxFacet({
    facetField: "keyword_facet",
    facetTitle: "Keywords",
    openByDefault: false,
    logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}
  });

  var database = FacetFactory.makeBasicCheckboxFacet({
    facetField: "database",
    facetTitle: "Databases",
    openByDefault: true
  });
  var data = FacetFactory.makeBasicCheckboxFacet({
    facetField: "data_facet",
    facetTitle: "Data",
    openByDefault: false
  });

  var vizier = FacetFactory.makeBasicCheckboxFacet({
    facetField: "vizier_facet",
    facetTitle: "Vizier Tables",
    openByDefault: false
  });

  var pub = FacetFactory.makeBasicCheckboxFacet({
    facetField: "bibstem_facet",
    facetTitle: "Publications",
    openByDefault: false,
    logicOptions: {single: null, multiple: ["or", "exclude"]}
  });
  var bibgroup = FacetFactory.makeBasicCheckboxFacet({
    facetField: "bibgroup_facet",
    facetTitle: "Bib Groups",
    openByDefault: false,
    logicOptions: {single: null, multiple: ["or", "exclude"]}
  });

  var grants = FacetFactory.makeHierarchicalCheckboxFacet({
    facetField: "grant_facet_hier",
    facetTitle: "Grants",
    openByDefault: false,
    logicOptions: {single: null, multiple: ["or", "exclude"]}
  });

  var refereed = FacetFactory.makeBasicCheckboxFacet({
    facetField: "property",
    facetTitle: "Refereed Status",
    openByDefault: true,
    extractFacets: function(facets) {
      var returnList = [];
      _.each(facets, function(d,i ){
        if (d ==="refereed"){
          returnList.push("Refereed", facets[i+1])
        }
        else if (d ==="notrefereed"){
           returnList.push("Non-Refereed", facets[i+1])
        }
      })
      return returnList
    }
  });

  var yearGraph = FacetFactory.makeGraphFacet({
    facetField: "year",
    facetTitle: "Year",
    xAxisTitle: "Year",
    openByDefault: true
  });

  queryInfo.activate(beehive.getHardenedInstance());
  searchBar.activate(beehive.getHardenedInstance());
  results.activate(beehive.getHardenedInstance());
  citationsGraphWidget.activate(beehive.getHardenedInstance());
  authorFacets.activate(beehive.getHardenedInstance());
  yearGraph.activate(beehive.getHardenedInstance());
  database.activate(beehive.getHardenedInstance());
  keywords.activate(beehive.getHardenedInstance());
  pub.activate(beehive.getHardenedInstance());
  bibgroup.activate(beehive.getHardenedInstance());
  data.activate(beehive.getHardenedInstance());
  vizier.activate(beehive.getHardenedInstance());
  grants.activate(beehive.getHardenedInstance());
  refereed.activate(beehive.getHardenedInstance());



  $("#top").append(searchBar.render().el)

  $("#middle").append(results.render().el)

  $("#left").append(authorFacets.render().el);
  $("#left").append(database.render().el);
  $("#left").append(refereed.render().el);
  $("#left").append(keywords.render().el);
  $("#left").append(pub.render().el);
  $("#left").append(bibgroup.render().el);
  $("#left").append(data.render().el);
  $("#left").append(vizier.render().el);
  $("#left").append(grants.render().el);




  $("#right").append(queryInfo.render().el);
  $("#right").append(yearGraph.render().el);
  $("#right").append(citationsGraphWidget.render().el);

});
