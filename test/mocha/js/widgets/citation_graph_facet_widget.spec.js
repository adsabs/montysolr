define(["js/widgets/facet/factory",
    'js/components/api_response',
    'js/wraps/graph_tabs'
  ],
  function(FacetFactory, ApiResponse, GraphTabs) {

    var testJSON = {
      "responseHeader": {
        "status": 0,
        "QTime": 7,
        "params": {
          "facet": "true",
          "fl": "id",
          "indent": "true",
          "q": "author:^accomazzi,a",
          "wt": "json",
          "facet.pivot": "property,citation_count"
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
          "property,citation_count": [
            {
              "field": "property",
              "value": "notrefereed",
              "count": 29,
              "pivot": [
                {
                  "field": "citation_count",
                  "value": 0,
                  "count": 7
                },
                {
                  "field": "citation_count",
                  "value": 1,
                  "count": 3
                },
                {
                  "field": "citation_count",
                  "value": 2,
                  "count": 3
                },
                {
                  "field": "citation_count",
                  "value": 3,
                  "count": 3
                },
                {
                  "field": "citation_count",
                  "value": 6,
                  "count": 3
                },
                {
                  "field": "citation_count",
                  "value": 4,
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
                  "field": "citation_count",
                  "value": 0,
                  "count": 3
                },
                {
                  "field": "citation_count",
                  "value": 1,
                  "count": 1
                },
                {
                  "field": "citation_count",
                  "value": 20,
                  "count": 1
                }
              ]
            }

          ]
        }
      }
    };

    describe("Graph for Citation Distribution in a List of Results", function() {

      var widget;

      beforeEach(function() {

        widget = GraphTabs().citationGraphWidget;
        widget.processResponse(new ApiResponse(testJSON));

      });

      it("should have a processResponse function that unspools a facet pivot query into a single, ordered array usable by d3", function(done) {

        var graphData = widget.model.attributes.graphData;
        //where y is # citations
        var expectedResults = [
            {
              "refereed": true,
              "x": 1,
              "y": 20
            },
            {
              "refereed": false,
              "x": 2,
              "y": 6
            },
            {
              "refereed": false,
              "x": 3,
              "y": 6
            },
            {
              "refereed": false,
              "x": 4,
              "y": 6
            },
            {
              "refereed": false,
              "x": 5,
              "y": 4
            },
            {
              "refereed": false,
              "x": 6,
              "y": 3
            },
            {
              "refereed": false,
              "x": 7,
              "y": 3
            },
            {
              "refereed": false,
              "x": 8,
              "y": 3
            },
            {
              "refereed": false,
              "x": 9,
              "y": 2
            },
            {
              "refereed": false,
              "x": 10,
              "y": 2
            },
            {
              "refereed": false,
              "x": 11,
              "y": 2
            },
            {
              "refereed": true,
              "x": 12,
              "y": 1
            },
            {
              "refereed": false,
              "x": 13,
              "y": 1
            },
            {
              "refereed": false,
              "x": 14,
              "y": 1
            },
            {
              "refereed": false,
              "x": 15,
              "y": 1
            },
            {
              "refereed": true,
              "x": 16,
              "y": 0
            },
            {
              "refereed": false,
              "x": 17,
              "y": 0
            },
            {
              "refereed": false,
              "x": 18,
              "y": 0
            },
            {
              "refereed": false,
              "x": 19,
              "y": 0
            },
            {
              "refereed": false,
              "x": 20,
              "y": 0
            },
            {
              "refereed": false,
              "x": 21,
              "y": 0
            },
            {
              "refereed": false,
              "x": 22,
              "y": 0
            },
            {
              "refereed": false,
              "x": 23,
              "y": 0
            },
            {
              "refereed": true,
              "x": 24,
              "y": 0
            },
            {
              "refereed": true,
              "x": 25,
              "y": 0
            }
          ];

        expect(JSON.stringify(graphData.slice(0,14))).to.eql(JSON.stringify(expectedResults.slice(0,14)));

        done();
      });
    });
  });
