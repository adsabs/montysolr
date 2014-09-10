define(["js/widgets/facet/factory",
     'js/components/api_response',
    'js/widgets/facet/graph-facet/year_graph'],
  function(FacetFactory, ApiResponse, YearGraphView){


    var testJSON = {
      "responseHeader": {
        "status": 0,
        "QTime": 58,
        "params": {
          "facet": "true",
          "fl": "id",
          "facet.mincount": "1",
          "q": "author:\"^accomazzi,a\"",
          "wt": "json",
          "facet.pivot": "property,year"
        }
      },
      "response": {
        "numFound": 34,
        "start": 0,
        "docs": [
          {
            "id": "4582438"
          },
          {
            "id": "4545442"
          },
          {
            "id": "4545606"
          },
          {
            "id": "9067423"
          },
          {
            "id": "8285512"
          },
          {
            "id": "8700936"
          },
          {
            "id": "3843891"
          },
          {
            "id": "3404318"
          },
          {
            "id": "3340879"
          },
          {
            "id": "3513629"
          }
        ]
      },
      "facet_counts": {
        "facet_queries": {},
        "facet_fields": {},
        "facet_dates": {},
        "facet_ranges": {},
        "facet_pivot": {
          "property,year": [
            {
              "field": "property",
              "value": "notrefereed",
              "count": 29,
              "pivot": [
                {
                  "field": "year",
                  "value": "2011",
                  "count": 4
                },
                {
                  "field": "year",
                  "value": "2007",
                  "count": 3
                },
                {
                  "field": "year",
                  "value": "2014",
                  "count": 3
                },
                {
                  "field": "year",
                  "value": "1992",
                  "count": 2
                },
                {
                  "field": "year",
                  "value": "2009",
                  "count": 2
                },
                {
                  "field": "year",
                  "value": "2012",
                  "count": 2
                },
                {
                  "field": "year",
                  "value": "1988",
                  "count": 1
                },
                {
                  "field": "year",
                  "value": "1989",
                  "count": 1
                },
                {
                  "field": "year",
                  "value": "1994",
                  "count": 1
                },
                {
                  "field": "year",
                  "value": "1995",
                  "count": 1
                },
                {
                  "field": "year",
                  "value": "1996",
                  "count": 1
                },
                {
                  "field": "year",
                  "value": "1997",
                  "count": 1
                },
                {
                  "field": "year",
                  "value": "1998",
                  "count": 1
                },
                {
                  "field": "year",
                  "value": "1999",
                  "count": 1
                },
                {
                  "field": "year",
                  "value": "2003",
                  "count": 1
                },
                {
                  "field": "year",
                  "value": "2004",
                  "count": 1
                },
                {
                  "field": "year",
                  "value": "2006",
                  "count": 1
                },
                {
                  "field": "year",
                  "value": "2010",
                  "count": 1
                },
                {
                  "field": "year",
                  "value": "2013",
                  "count": 1
                }
              ]
            },

            {
              "field": "property",
              "value": "refereed",
              "count": 5,
              "pivot": [
                {
                  "field": "year",
                  "value": "1995",
                  "count": 2
                },
                {
                  "field": "year",
                  "value": "1989",
                  "count": 1
                },
                {
                  "field": "year",
                  "value": "2000",
                  "count": 1
                },
                {
                  "field": "year",
                  "value": "2013",
                  "count": 1
                }
              ]
            }

          ]
        }
      }
      };



    describe("Graph for Publications Per Year", function(){

      var widget;

      beforeEach(function(){

        widget = FacetFactory.makeGraphFacet({
          graphView            : YearGraphView,
          facetField           : "year",
          defaultQueryArguments: {
            "facet.pivot"   : "property,year",
            "facet"         : "true",
            "facet.minCount": "1"
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

        widget.processResponse(new ApiResponse(testJSON));

      });


      it("should process refereed and refereed data into a single array, filling in missing years (with y values of 0), to be used by d3", function(){

        var graphData = widget.collection.models[0].attributes.graphData;

        var expectedResults  = [{"x":1988,"y":1,"refCount":0},{"x":1989,"y":2,"refCount":1},{"x":1990,"y":0,"refCount":0},{"x":1991,"y":0,"refCount":0},{"x":1992,"y":2,"refCount":0},{"x":1993,"y":0,"refCount":0},{"x":1994,"y":1,"refCount":0},{"x":1995,"y":3,"refCount":2},{"x":1996,"y":1,"refCount":0},{"x":1997,"y":1,"refCount":0},{"x":1998,"y":1,"refCount":0},{"x":1999,"y":1,"refCount":0},{"x":2000,"y":1,"refCount":1},{"x":2001,"y":0,"refCount":0},{"x":2002,"y":0,"refCount":0},{"x":2003,"y":1,"refCount":0},{"x":2004,"y":1,"refCount":0},{"x":2005,"y":0,"refCount":0},{"x":2006,"y":1,"refCount":0},{"x":2007,"y":3,"refCount":0},{"x":2008,"y":0,"refCount":0},{"x":2009,"y":2,"refCount":0},{"x":2010,"y":1,"refCount":0},{"x":2011,"y":4,"refCount":0},{"x":2012,"y":2,"refCount":0},{"x":2013,"y":2,"refCount":1},{"x":2014,"y":3,"refCount":0}];

          _.each(expectedResults, function(d,i){
            expect(d).to.eql(graphData[i])
          });

      })




    })


});