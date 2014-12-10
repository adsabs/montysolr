define(['js/widgets/tabs/tabs_widget',
    'js/widgets/facet/factory',
    'js/widgets/facet/graph-facet/year_graph',
    'js/widgets/facet/graph-facet/h_index_graph'],
  function (TabsWidget, FacetFactory, YearGraphView, HIndexGraph) {

    return function() {

      var yearGraphWidget = FacetFactory.makeGraphFacet({
        graphView            : YearGraphView,
        facetField           : "year",
        defaultQueryArguments: {
          "facet.pivot"   : "property,year",
          "facet"         : "true",
          "facet.minCount": "1"
        },

        graphViewOptions : {
          yAxisTitle :  "article count",
          xAxisTitle : "years",
          graphTitle: "Years"
        },

        processResponse      : function (apiResponse) {

          this.setCurrentQuery(apiResponse.getApiQuery());

          var data = apiResponse.get("facet_counts.facet_pivot.property,year");

          if (apiResponse.get("response.numFound") < 2) {
            this.collection.reset({graphData: []});
            return
          }

          var refData = _.findWhere(data, {value: "refereed"});

          if (refData) {
            refData = refData.pivot;
          }

          var nonRefData = _.findWhere(data, {value: "notrefereed"});

          if (nonRefData) {
            nonRefData = nonRefData.pivot;
          }

          var maxVal, minVal;

          _.each(refData, function (d) {
            var val = parseInt(d.value);
            if (maxVal === undefined) {
              maxVal = val;
            }
            else if (val > maxVal) {
              maxVal = val
            }
            if (minVal === undefined) {
              minVal = val;
            }
            else if (parseInt(d.value) < minVal) {
              minVal = parseInt(d.value);
            }
          });

          _.each(nonRefData, function (d) {
            var val = parseInt(d.value);
            if (maxVal === undefined) {
              maxVal = val;
            }
            else if (val > maxVal) {
              maxVal = val
            }
            if (minVal === undefined) {
              minVal = val;
            }
            else if (parseInt(d.value) < minVal) {
              minVal = parseInt(d.value);
            }
          });

          var yearRange = _.range(minVal, maxVal + 1);

          var finalData = [];

          _.each(yearRange, function (year) {
            var stringYear = year + "";
            refCount = _.filter(refData, function (d) {
              return d.value === stringYear
            })[0];
            refCount = refCount ? refCount.count : 0;
            nonRefCount = _.filter(nonRefData, function (d) {
              return d.value === stringYear
            })[0];
            nonRefCount = nonRefCount ? nonRefCount.count : 0;

            finalData.push({x: year, y: refCount + nonRefCount, refCount: refCount})

          })

          if (finalData.length < 2) {
            this.collection.reset({graphData: []});
            return
          }
          this.collection.reset([
            {graphData: finalData}
          ]);
        }});

      var citationGraphWidget = FacetFactory.makeGraphFacet({
        graphView            : HIndexGraph,
        facetField           : "citation_count",
        defaultQueryArguments: {
          "facet.pivot": "property,citation_count",
          "facet"      : "true",
          "facet.limit": "-1"
        },
        graphViewOptions : {
          yAxisTitle :  "citations",
          xAxisTitle : "number of records",
          graphTitle: "Citation",
          pastTenseTitle : "cited"
        },
        processResponse      : function (apiResponse) {
          this.setCurrentQuery(apiResponse.getApiQuery());

          var data = apiResponse.get("facet_counts.facet_pivot.property,citation_count");

          if (apiResponse.get("response.numFound") < 2) {
            this.collection.reset({graphData: []});
            return
          }

          var refData = _.findWhere(data, {value: "refereed"});

          if (refData) {
            refData = refData.pivot;
          }

          var nonRefData = _.findWhere(data, {value: "notrefereed"});

          if (nonRefData) {
            nonRefData = nonRefData.pivot;
          }

          var finalData = [];

          _.each(refData, function (d) {
            var val = d.value, count = d.count;
            _.each(_.range(count), function () {
              finalData.push({refereed: true, x: undefined, y: val})
            })
          })

          _.each(nonRefData, function (d) {
            var val = d.value, count = d.count;
            _.each(_.range(count), function () {
              finalData.push({refereed: false, x: undefined, y: val})
            })
          })

          if (finalData.length < 2) {
            this.collection.reset({graphData: []});
            return
          }

          finalData = finalData.sort(function (a, b) {
            return b.y - a.y;
          });

          //a cut off of 2000
          finalData = _.first(finalData, 2000);

          finalData = _.map(finalData, function (d, i) {
            d.x = i + 1
            return d
          });

          this.collection.reset([
            {graphData: finalData}
          ]);

        }

      });

      var readsGraphWidget = FacetFactory.makeGraphFacet({
        graphView            : HIndexGraph,
        facetField           : "read_count",
        defaultQueryArguments: {
          "facet.pivot": "property,read_count",
          "facet"      : "true",
          "facet.limit": "-1"
        },
        graphViewOptions : {
          yAxisTitle :  "reads",
          xAxisTitle : "number of records",
          graphTitle: "Reads",
          pastTenseTitle : "read"
        },
        processResponse      : function (apiResponse) {
          this.setCurrentQuery(apiResponse.getApiQuery());

          var data = apiResponse.get("facet_counts.facet_pivot.property,read_count");

          if (apiResponse.get("response.numFound") < 2) {
            this.collection.reset({graphData: []});
            return
          }

          var refData = _.findWhere(data, {value: "refereed"});

          if (refData) {
            refData = refData.pivot;
          }

          var nonRefData = _.findWhere(data, {value: "notrefereed"});

          if (nonRefData) {
            nonRefData = nonRefData.pivot;
          }

          var finalData = [];

          _.each(refData, function (d) {
            var val = d.value, count = d.count;
            _.each(_.range(count), function () {
              finalData.push({refereed: true, x: undefined, y: val})
            })
          })

          _.each(nonRefData, function (d) {
            var val = d.value, count = d.count;
            _.each(_.range(count), function () {
              finalData.push({refereed: false, x: undefined, y: val})
            })
          })

          if (finalData.length < 2) {
            this.collection.reset({graphData: []});
            return
          }

          finalData = finalData.sort(function (a, b) {
            return b.y - a.y;
          });

          //a cut off of 2000
          finalData = _.first(finalData, 2000);

          finalData = _.map(finalData, function (d, i) {
            d.x = i + 1;
            return d
          });

          this.collection.reset([
            {graphData: finalData}
          ]);

        }

      });

      var graphTabWidget = new TabsWidget({tabs: [
        {title: "Years", widget: yearGraphWidget, id: "year-facet", default: true},
        {title: "Citations", widget: citationGraphWidget, id: "citations-facet"},
        {title: "Reads", widget: readsGraphWidget, id: "reads-facet"}
      ]
      });

      return graphTabWidget;
    }

  });