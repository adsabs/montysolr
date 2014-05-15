require(['js/components/beehive', 'js/services/pubsub', 'js/components/query_mediator', 'js/services/api',
  'jquery', 'underscore',
  'js/widgets/search_bar/search_bar_widget', 'js/widgets/results_render/results_render_widget',
  'js/widgets/facet/factory', 'js/widgets/query_info/query_info_widget'
], function(BeeHive, PubSub, QueryMediator, Api, $, _, SearchBar, ResultsRender, FacetFactory, QueryInfoWidget) {


  var beehive = new BeeHive();
  beehive.addService('PubSub', new PubSub());
  beehive.addService('Api', new Api());
  var queryMediator = new QueryMediator();
  queryMediator.activate(beehive);

  var queryInfo = new QueryInfoWidget();

  var s = new SearchBar();
  var r = new ResultsRender({pagination: {rows: 40, start:0}});
  var c = FacetFactory.makeGraphFacet({
    facetName: "citation_count",
    userFacingName: "Citations",
    xAxisTitle: "Citation Count",
    openByDefault: true,
  });

  var a = FacetFactory.makeHierarchicalCheckboxFacet({
    facetName: "author",
    userFacingName: "Authors",
    openByDefault: true,
    preprocess: ["titleCase", "removeSlash"]
  })
  var keywords = FacetFactory.makeBasicCheckboxFacet({
    facetName: "keyword",
    userFacingName: "Keywords",
    openByDefault: false,
    preprocess: ["titleCase", "removeSlash"]
  })

  var database = FacetFactory.makeBasicCheckboxFacet({
    facetName: "database",
    userFacingName: "Databases",
    openByDefault: true,
    preprocess: "titleCase",
  })
  var data = FacetFactory.makeBasicCheckboxFacet({
    facetName: "data",
    userFacingName: "Data",
    openByDefault: false,
    preprocess: "allCaps",
  })

  var vizier = FacetFactory.makeBasicCheckboxFacet({
    facetName: "vizier",
    userFacingName: "Vizier Tables",
    openByDefault: false,
    preprocess: "allCaps",
  })

  var pub = FacetFactory.makeBasicCheckboxFacet({
    facetName: "bibstem",
    userFacingName: "Publications",
    openByDefault: false,
    preprocess: "allCaps",
    multiLogic: ["or", "exclude"]
  })
  var bibgroup = FacetFactory.makeBasicCheckboxFacet({
    facetName: "bibgroup",
    userFacingName: "Bib Groups",
    openByDefault: false,
    preprocess: "allCaps",
    multiLogic: ["or", "exclude"]
  })

  var grants = FacetFactory.makeHierarchicalCheckboxFacet({
    facetName: "grant",
    userFacingName: "Grants",
    openByDefault: false,
    preprocess: ["allCaps", "removeSlash"],
    multiLogic: ["or", "exclude"]
  })

  var refereed = FacetFactory.makeBasicCheckboxFacet({
    facetName: "property",
    userFacingName: "Refereed Status",
    openByDefault: true,
    multiLogic: "fullSet",
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
  })

  var y = FacetFactory.makeGraphFacet({
    facetName: "year",
    userFacingName: "Year",
    xAxisTitle: "Year",
    openByDefault: true,
  });

  queryInfo.activate(beehive.getHardenedInstance())
  s.activate(beehive.getHardenedInstance())
  r.activate(beehive.getHardenedInstance())
  c.activate(beehive.getHardenedInstance())
  a.activate(beehive.getHardenedInstance())
  y.activate(beehive.getHardenedInstance())
  database.activate(beehive.getHardenedInstance())
  keywords.activate(beehive.getHardenedInstance())
  pub.activate(beehive.getHardenedInstance())
  bibgroup.activate(beehive.getHardenedInstance())
  data.activate(beehive.getHardenedInstance())
  vizier.activate(beehive.getHardenedInstance())
  grants.activate(beehive.getHardenedInstance())
  refereed.activate(beehive.getHardenedInstance())



  $("#top").append(s.getView().render().el)

  $("#middle").append(r.getView().render().el)

  $("#left").append(a.getView().render().el)
  $("#left").append(database.getView().render().el)
  $("#left").append(refereed.getView().render().el)
  $("#left").append(keywords.getView().render().el)
  $("#left").append(pub.getView().render().el)
  $("#left").append(bibgroup.getView().render().el)
  $("#left").append(data.getView().render().el)
  $("#left").append(vizier.getView().render().el)
  $("#left").append(grants.getView().render().el)




  $("#right").append(queryInfo.getView().render().el)
  $("#right").append(y.getView().render().el)
  $("#right").append(c.getView().render().el)





})
