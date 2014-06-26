require([
  'jquery',
  'underscore',
  'js/components/beehive',
  'js/services/pubsub',
  'js/components/query_mediator',
  'js/services/api',
  'js/widgets/search_bar/search_bar_widget',
  'js/widgets/results/widget',
  'js/widgets/facet/factory',
  'js/widgets/query_info/query_info_widget',
  'js/widgets/abstract/widget',
  'js/widgets/contents_manager/widget'

], function(
  $,
  _,
  BeeHive,
  PubSub,
  QueryMediator,
  Api,
  SearchBar,
  ResultsWidget,
  FacetFactory,
  QueryInfoWidget,
  AbstractWidget,
  LayoutWidget
  ) {


  var beehive = new BeeHive();
  beehive.addService('PubSub', new PubSub());
  beehive.addService('Api', new Api());
  var queryMediator = new QueryMediator({cache:true, debug:true});
  queryMediator.activate(beehive);

  var queryInfo = new QueryInfoWidget();

  var abstract = new AbstractWidget();
  var layout = new LayoutWidget({widgetTitleMapping : {'abstract' : {widget: abstract, default : true}}});

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
    logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']},
    responseProcessors: [
      function(v) {var vv = v.split('/'); return vv[vv.length-1]}
    ]
  });
  var keywords = FacetFactory.makeBasicCheckboxFacet({
    facetField: "keyword_facet",
    facetTitle: "Keywords",
    openByDefault: false,
    logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}
  });

  var database = FacetFactory.makeBasicCheckboxFacet({
    facetField: "database",
    facetTitle: "Collections",
    openByDefault: true,
    logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}

  });
  var data = FacetFactory.makeBasicCheckboxFacet({
    facetField: "data_facet",
    facetTitle: "Data",
    openByDefault: false,
    logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}

  });

  var vizier = FacetFactory.makeBasicCheckboxFacet({
    facetField: "vizier_facet",
    facetTitle: "Vizier Tables",
    openByDefault: false,
    logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}

  });

  var pub = FacetFactory.makeBasicCheckboxFacet({
    facetField: "bibstem_facet",
    facetTitle: "Publications",
    openByDefault: false,
    logicOptions: {single: ['limit to', 'exclude'], multiple: ["or", "exclude"]}
  });
  var bibgroup = FacetFactory.makeBasicCheckboxFacet({
    facetField: "bibgroup_facet",
    facetTitle: "Bib Groups",
    openByDefault: false,
    logicOptions: {single: ['limit to', 'exclude'], multiple: ["or", "exclude"]}
  });

  var grants = FacetFactory.makeHierarchicalCheckboxFacet({
    facetField: "grant_facet_hier",
    facetTitle: "Grants",
    openByDefault: false,
    logicOptions: {single: ['limit to', 'exclude'], multiple: ["or", "exclude"]},
    responseProcessors: [
      function(v) {var vv = v.split('/'); return vv[vv.length-1]}
    ]
  });

  var refereed = FacetFactory.makeBasicCheckboxFacet({
    facetField: "property",
    facetTitle: "Refereed Status",
    openByDefault: true,
    defaultQueryArguments: {
      "facet": "true",
      "facet.mincount": "1",
      "fl": "id",
      "facet.query": 'property:refereed'
    },
    // this is optimization, we'll execute only one query (we don't even facet on
    // other values). There is a possibility is is OK (but could also be wrong;
    // need to check)
    extractionProcessors:
      function(apiResponse) {
      var returnList = [];
      if (apiResponse.has('facet_counts.facet_queries')) {
        var queries = apiResponse.get('facet_counts.facet_queries');
        var v, found = 0;
        _.each(_.keys(queries), function(k) {
          v = queries[k];
          if (k.indexOf(':refereed') > -1) {
            found = v;
            returnList.push("refereed", v);
          }
        });

        returnList.push('notrefereed', apiResponse.get('response.numFound') - found);
        return returnList;
      }
    },
    logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['invalid choice']}

  });

  var yearGraph = FacetFactory.makeGraphFacet({
    facetField: "year",
    facetTitle: "Year",
    xAxisTitle: "Year",
    openByDefault: true
  });

  abstract.activate(beehive.getHardenedInstance());
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



  $("#top").append(searchBar.render().el);

  $("#s-middle-col-container").append(results.render().el).append(layout.render().el)

  $("#s-facet-container")
    .append(authorFacets.render().el)
    .append(database.render().el)
    .append(refereed.render().el)
    .append(keywords.render().el)
    .append(pub.render().el)
    .append(bibgroup.render().el)
    .append(data.render().el)
    .append(vizier.render().el)
    .append(grants.render().el)


  $("#s-right-col-container")
    .append(queryInfo.render().el)
    .append(yearGraph.render().el)
    .append(citationsGraphWidget.render().el);

});
