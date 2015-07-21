define([
  'js/widgets/metrics/widget',
  'js/widgets/metrics/extractor_functions',
  'js/components/json_response',
  'js/bugutils/minimal_pubsub',
  'js/components/api_query',
  'js/components/api_response',
  'js/components/api_targets'

], function(
  MetricsWidget,
  DataExtractor,
  JsonResponse,
  MinimalPubSub,
  ApiQuery,
  ApiResponse,
  ApiTargets
  ){

  describe("Metrics Widget (metrics_widget.spec.js)", function(){

//query : {"bibcodes":["1980ApJS...44..137K","1980ApJS...44..489B"]}'

    //this is the large version straight from Edwin's api
    var testData = {
      "basic stats": {
        "average number of downloads": 19.275862068965516,
        "average number of reads": 63.86206896551724,
        "median number of downloads": 3.0,
        "median number of reads": 52.0,
        "normalized paper count": 7.291269841269841,
        "number of papers": 30,
        "recent number of downloads": 78,
        "recent number of reads": 204,
        "total number of downloads": 559,
        "total number of reads": 1852
      },
      "basic stats refereed": {
        "average number of downloads": 62.0,
        "average number of reads": 148.0,
        "median number of downloads": 62.0,
        "median number of reads": 148.0,
        "normalized paper count": 0.125,
        "number of papers": 1,
        "recent number of downloads": 2,
        "recent number of reads": 5,
        "total number of downloads": 62,
        "total number of reads": 148
      },
      "citation stats": {
        "average number of citations": 2.125,
        "average number of refereed citations": 0.375,
        "median number of citations": 2.0,
        "median number of refereed citations": 0.0,
        "normalized number of citations": 6.430555555555555,
        "normalized number of refereed citations": 1.5833333333333333,
        "number of citing papers": 15,
        "number of self-citations": 5,
        "total number of citations": 17,
        "total number of refereed citations": 3
      },
      "citation stats refereed": {
        "average number of citations": 1.0,
        "average number of refereed citations": 0.0,
        "median number of citations": 1.0,
        "median number of refereed citations": 0.0,
        "normalized number of citations": 0.125,
        "normalized number of refereed citations": 0.0,
        "number of citing papers": 1,
        "number of self-citations": 0,
        "total number of citations": 1,
        "total number of refereed citations": 0
      },
      "histograms": {
        "citations": {
          "nonrefereed to nonrefereed": {
            "2011": 1,
            "2012": 5,
            "2013": 3,
            "2014": 2,
            "2015": 2
          },
          "nonrefereed to nonrefereed normalized": {
            "2011": 0.1111111111111111,
            "2012": 2.861111111111111,
            "2013": 0.9444444444444444,
            "2014": 0.4444444444444444,
            "2015": 0.3611111111111111
          },
          "nonrefereed to refereed": {
            "2011": 0,
            "2012": 0,
            "2013": 0,
            "2014": 1,
            "2015": 0
          },
          "nonrefereed to refereed normalized": {
            "2011": 0,
            "2012": 0,
            "2013": 0,
            "2014": 0.125,
            "2015": 0
          },
          "refereed to nonrefereed": {
            "2011": 1,
            "2012": 1,
            "2013": 0,
            "2014": 1,
            "2015": 0
          },
          "refereed to nonrefereed normalized": {
            "2011": 0.25,
            "2012": 0.3333333333333333,
            "2013": 0,
            "2014": 1.0,
            "2015": 0
          },
          "refereed to refereed": {
            "2011": 0,
            "2012": 0,
            "2013": 0,
            "2014": 0,
            "2015": 0
          },
          "refereed to refereed normalized": {
            "2011": 0,
            "2012": 0,
            "2013": 0,
            "2014": 0,
            "2015": 0
          }
        },
        "downloads": {
          "all downloads": {

            "2010": 16,
            "2011": 85,
            "2012": 145,
            "2013": 80,
            "2014": 155,
            "2015": 78
          },
          "all downloads normalized": {

            "2010": 1.7777777777777777,
            "2011": 46.75,
            "2012": 46.94444444444444,
            "2013": 23.069444444444443,
            "2014": 37.361111111111114,
            "2015": 14.890079365079362
          },
          "refereed downloads": {

            "2012": 26,
            "2013": 18,
            "2014": 16,
            "2015": 2
          },
          "refereed downloads normalized": {

            "2012": 3.25,
            "2013": 2.25,
            "2014": 2.0,
            "2015": 0.25
          }
        },
        "publications": {
          "all publications": {
            "2011": 8,
            "2012": 6,
            "2013": 4,
            "2014": 3,
            "2015": 9
          },
          "all publications normalized": {
            "2011": 3.166666666666667,
            "2012": 1.9444444444444444,
            "2013": 0.5178571428571428,
            "2014": 0.375,
            "2015": 1.2873015873015876
          },
          "refereed publications": {
            "2011": 0,
            "2012": 0,
            "2013": 1,
            "2014": 0,
            "2015": 0
          },
          "refereed publications normalized": {
            "2011": 0,
            "2012": 0,
            "2013": 0.125,
            "2014": 0,
            "2015": 0
          }
        },
        "reads": {
          "all reads": {

            "2010": 37,
            "2011": 368,
            "2012": 428,
            "2013": 371,
            "2014": 444,
            "2015": 204
          },
          "all reads normalized": {

            "2010": 4.111111111111111,
            "2011": 137.16666666666666,
            "2012": 126.66666666666666,
            "2013": 96.9107142857143,
            "2014": 87.63492063492063,
            "2015": 37.39801587301587
          },
          "refereed reads": {

            "2012": 56,
            "2013": 57,
            "2014": 30,
            "2015": 5
          },
          "refereed reads normalized": {

            "2012": 7.0,
            "2013": 7.125,
            "2014": 3.75,
            "2015": 0.625
          }
        }
      },
      "indicators": {
        "g": 3,
        "h": 3,
        "i10": 0,
        "i100": 0,
        "m": 0.6,
        "read10": 37.39801587301587,
        "riq": 165,
        "tori": 0.6887780112044819
      },
      "indicators refereed": {
        "g": 1,
        "h": 1,
        "i10": 0,
        "i100": 0,
        "m": 0.3333333333333333,
        "read10": 0.625,
        "riq": 34,
        "tori": 0.010416666666666666
      },
      "skipped bibcodes": [],
      "time series": {
        "g": {
          "2011": 1,
          "2012": 2,
          "2013": 3,
          "2014": 3,
          "2015": 3
        },
        "h": {
          "2011": 1,
          "2012": 1,
          "2013": 2,
          "2014": 2,
          "2015": 2
        },
        "i10": {
          "2011": 0,
          "2012": 0,
          "2013": 0,
          "2014": 0,
          "2015": 0
        },
        "i100": {
          "2011": 0,
          "2012": 0,
          "2013": 0,
          "2014": 0,
          "2015": 0
        },
        "read10": {
          "2011": 89.5,
          "2012": 119.66666666666666,
          "2013": 96.9107142857143,
          "2014": 82.19047619047619,
          "2015": 37.39801587301587
        },
        "tori": {
          "2011": 0.04820261437908496,
          "2012": 0.15058356676003734,
          "2013": 0.3394724556489262,
          "2014": 0.6387780112044817,
          "2015": 0.6887780112044819
        }
      }
    }

    afterEach(function(){
      $("#test").empty();
    });

    //first, test Edwin's functions

    it("should have a data extractor object that takes metrics data and prepares json for the nvd3 graph", function(){

      var hist = testData.histograms;

      var citshist = DataExtractor.plot_citshist({norm : false, citshist_data : hist["citations"]});
      var norm_citshist =  DataExtractor.plot_citshist({norm : true, citshist_data : hist["citations"]});
      var readshist = DataExtractor.plot_readshist({norm: false, readshist_data : hist["reads"]});
      var norm_readshist  = DataExtractor.plot_readshist({norm: true, readshist_data : hist["reads"]});
      var paperhist = DataExtractor.plot_paperhist({norm : true, paperhist_data : hist["publications"]});
      var norm_paperhist = DataExtractor.plot_paperhist({norm : false, paperhist_data : hist["publications"]});
      var indexes_data = DataExtractor.plot_series({series_data : testData["time series"]});

      expect(citshist).to.eql([
        {
          "key": "Ref. citations to ref. papers",
          "values": [
            {
              "x": "2011",
              "y": 0
            },
            {
              "x": "2012",
              "y": 0
            },
            {
              "x": "2013",
              "y": 0
            },
            {
              "x": "2014",
              "y": 0
            },
            {
              "x": "2015",
              "y": 0
            }
          ]
        },
        {
          "key": "Ref. citations to non ref. papers",
          "values": [
            {
              "x": "2011",
              "y": 1
            },
            {
              "x": "2012",
              "y": 1
            },
            {
              "x": "2013",
              "y": 0
            },
            {
              "x": "2014",
              "y": 1
            },
            {
              "x": "2015",
              "y": 0
            }
          ]
        },
        {
          "key": "Non ref. citations to ref. papers",
          "values": [
            {
              "x": "2011",
              "y": 0
            },
            {
              "x": "2012",
              "y": 0
            },
            {
              "x": "2013",
              "y": 0
            },
            {
              "x": "2014",
              "y": 1
            },
            {
              "x": "2015",
              "y": 0
            }
          ]
        },
        {
          "key": "Non ref. citations to non ref. papers",
          "values": [
            {
              "x": "2011",
              "y": 1
            },
            {
              "x": "2012",
              "y": 5
            },
            {
              "x": "2013",
              "y": 3
            },
            {
              "x": "2014",
              "y": 2
            },
            {
              "x": "2015",
              "y": 2
            }
          ]
        }
      ]);

      expect(norm_citshist).to.eql([
        {
          "key": "Ref. citations to ref. papers",
          "values": [
            {
              "x": "2011",
              "y": 0
            },
            {
              "x": "2012",
              "y": 0
            },
            {
              "x": "2013",
              "y": 0
            },
            {
              "x": "2014",
              "y": 0
            },
            {
              "x": "2015",
              "y": 0
            }
          ]
        },
        {
          "key": "Ref. citations to non ref. papers",
          "values": [
            {
              "x": "2011",
              "y": 0.25
            },
            {
              "x": "2012",
              "y": 0.3333333333333333
            },
            {
              "x": "2013",
              "y": 0
            },
            {
              "x": "2014",
              "y": 1
            },
            {
              "x": "2015",
              "y": 0
            }
          ]
        },
        {
          "key": "Non ref. citations to ref. papers",
          "values": [
            {
              "x": "2011",
              "y": 0
            },
            {
              "x": "2012",
              "y": 0
            },
            {
              "x": "2013",
              "y": 0
            },
            {
              "x": "2014",
              "y": 0.125
            },
            {
              "x": "2015",
              "y": 0
            }
          ]
        },
        {
          "key": "Non ref. citations to non ref. papers",
          "values": [
            {
              "x": "2011",
              "y": 0.1111111111111111
            },
            {
              "x": "2012",
              "y": 2.861111111111111
            },
            {
              "x": "2013",
              "y": 0.9444444444444444
            },
            {
              "x": "2014",
              "y": 0.4444444444444444
            },
            {
              "x": "2015",
              "y": 0.3611111111111111
            }
          ]
        }
      ]);

      expect(readshist).to.eql([
        {
          "key": "Refereed",
          "values": [
            {
              "x": "2012",
              "y": 56
            },
            {
              "x": "2013",
              "y": 57
            },
            {
              "x": "2014",
              "y": 30
            },
            {
              "x": "2015",
              "y": 5
            }
          ]
        },
        {
          "key": "Non-refereed",
          "values": [
            {
              "x": "2010",
              "y": 37
            },
            {
              "x": "2011",
              "y": 368
            },
            {
              "x": "2012",
              "y": 372
            },
            {
              "x": "2013",
              "y": 314
            },
            {
              "x": "2014",
              "y": 414
            },
            {
              "x": "2015",
              "y": 199
            }
          ]
        }
      ]);

      expect(norm_readshist).to.eql([{"key":"Refereed","values":[{"x":"2012","y":7},{"x":"2013","y":7.125},{"x":"2014","y":3.75},{"x":"2015","y":0.625}]},{"key":"Non-refereed","values":[{"x":"2010","y":4.111111111111111},{"x":"2011","y":137.16666666666666},{"x":"2012","y":119.66666666666666},{"x":"2013","y":89.7857142857143},{"x":"2014","y":83.88492063492063},{"x":"2015","y":36.77301587301587}]}]);

      expect(paperhist).to.eql([
        {
          "key": "Refereed",
          "values": [
            {
              "x": "2011",
              "y": 0
            },
            {
              "x": "2012",
              "y": 0
            },
            {
              "x": "2013",
              "y": 0.125
            },
            {
              "x": "2014",
              "y": 0
            },
            {
              "x": "2015",
              "y": 0
            }
          ]
        },
        {
          "key": "Non-refereed",
          "values": [
            {
              "x": "2011",
              "y": 3.166666666666667
            },
            {
              "x": "2012",
              "y": 1.9444444444444444
            },
            {
              "x": "2013",
              "y": 0.3928571428571428
            },
            {
              "x": "2014",
              "y": 0.375
            },
            {
              "x": "2015",
              "y": 1.2873015873015876
            }
          ]
        }
      ]);

      expect(norm_paperhist).to.eql([
        {
          "key": "Refereed",
          "values": [
            {
              "x": "2011",
              "y": 0
            },
            {
              "x": "2012",
              "y": 0
            },
            {
              "x": "2013",
              "y": 1
            },
            {
              "x": "2014",
              "y": 0
            },
            {
              "x": "2015",
              "y": 0
            }
          ]
        },
        {
          "key": "Non-refereed",
          "values": [
            {
              "x": "2011",
              "y": 8
            },
            {
              "x": "2012",
              "y": 6
            },
            {
              "x": "2013",
              "y": 3
            },
            {
              "x": "2014",
              "y": 3
            },
            {
              "x": "2015",
              "y": 9
            }
          ]
        }
      ]);

      //checking parsing
      expect(_.findWhere(indexes_data, {key : "h Index"}).values[1].y).to.eql(1);
      expect(_.findWhere(indexes_data, {key : "g Index"}).values[1].y).to.eql(2);
      expect(_.findWhere(indexes_data, {key : "i10 Index"}).values[1].y).to.eql(0);
      expect(_.findWhere(indexes_data, {key : "tori Index"}).values[1].y).to.eql(0.15058356676003734);
      expect(_.findWhere(indexes_data, {key : "i100 Index"}).values[1].y).to.eql(0);
      expect(_.findWhere(indexes_data, {key : "read10 Index"}).values[1].y).to.eql(11.966666666666665);

      expect(indexes_data).to.eql([
          {
            "key": "h Index",
            "values": [
              {
                "x": "2011",
                "y": 1
              },
              {
                "x": "2012",
                "y": 1
              },
              {
                "x": "2013",
                "y": 2
              },
              {
                "x": "2014",
                "y": 2
              },
              {
                "x": "2015",
                "y": 2
              }
            ]
          },
          {
            "key": "g Index",
            "values": [
              {
                "x": "2011",
                "y": 1
              },
              {
                "x": "2012",
                "y": 2
              },
              {
                "x": "2013",
                "y": 3
              },
              {
                "x": "2014",
                "y": 3
              },
              {
                "x": "2015",
                "y": 3
              }
            ]
          },
          {
            "key": "i10 Index",
            "values": [
              {
                "x": "2011",
                "y": 0
              },
              {
                "x": "2012",
                "y": 0
              },
              {
                "x": "2013",
                "y": 0
              },
              {
                "x": "2014",
                "y": 0
              },
              {
                "x": "2015",
                "y": 0
              }
            ]
          },
          {
            "key": "tori Index",
            "values": [
              {
                "x": "2011",
                "y": 0.04820261437908496
              },
              {
                "x": "2012",
                "y": 0.15058356676003734
              },
              {
                "x": "2013",
                "y": 0.3394724556489262
              },
              {
                "x": "2014",
                "y": 0.6387780112044817
              },
              {
                "x": "2015",
                "y": 0.6887780112044819
              }
            ]
          },
          {
            "key": "i100 Index",
            "values": [
              {
                "x": "2011",
                "y": 0
              },
              {
                "x": "2012",
                "y": 0
              },
              {
                "x": "2013",
                "y": 0
              },
              {
                "x": "2014",
                "y": 0
              },
              {
                "x": "2015",
                "y": 0
              }
            ]
          },
          {
            "key": "read10 Index",
            "values": [
              {
                "x": "2011",
                "y": 8.95
              },
              {
                "x": "2012",
                "y": 11.966666666666665
              },
              {
                "x": "2013",
                "y": 9.69107142857143
              },
              {
                "x": "2014",
                "y": 8.219047619047618
              },
              {
                "x": "2015",
                "y": 3.7398015873015873
              }
            ]
          }
        ]
      );

    });


    it("should have a function that empties the main view", function(){

      var metricsWidget = new MetricsWidget();
      metricsWidget.processResponse(new JsonResponse(testData));
      $("#test").append(metricsWidget.view.el);
      metricsWidget.resetWidget();

      //check to see that the rendered views are inserted
      expect($("#test").find((".metrics-graph *")).length).to.eql(0);
      expect($("#test").find((".metrics-table *")).length).to.eql(0);


    });


    it("should have a configurable graph view that can show a bar chart", function(done){

      this.timeout(3000);
      var metricsWidget = new MetricsWidget();
      var gModel = new metricsWidget.components.GraphModel();

      var graphView = new metricsWidget.components.GraphView({model : gModel });

      graphView.model.set("graphData", DataExtractor.plot_citshist({norm : false, citshist_data : testData["histograms"]["citations"]}));
      graphView.model.set("normalizedGraphData", DataExtractor.plot_citshist({norm : true, citshist_data : testData["histograms"]["citations"]}));

      $("#test").append(graphView.render().el);

      //need to wait for animation to complete

      setTimeout(function(){

        expect(d3.selectAll("#test g.nv-series-0 rect")[0].length).to.eql(5);
        $("#test").empty();
        done();

      }, 2000);


    })

    // this isn't working, the setimeout is running before anything is in the dom...

    it("should have a configurable graph view that can show a line chart", function(done){

      this.timeout(3000);
      var metricsWidget = new MetricsWidget();
      var gModel = new metricsWidget.components.GraphModel({graphType : "line"});
      var graphView = new metricsWidget.components.GraphView({model : gModel });

      graphView.model.set("graphData", DataExtractor.plot_series({series_data : testData["time series"]}));

      $("#test").empty().append(graphView.render().el);

      setTimeout(function(){
        //should show 6 different lines, so 12 items (nvd3 shows a separate group with a path and a group of circles)
        expect($(".nv-group").length).to.eql(12)

        done();

      }, 2500)

    })

    it("should have a function that creates table views from the raw api response", function(){

      var metricsWidget = new MetricsWidget();

      metricsWidget.processResponse(new JsonResponse(testData));
      //checking a single row from each template
      //would there be a way to check the entire rendered html in a non-messy way?
      expect(metricsWidget.childViews.papersTableView.render().$("td:contains(Number of papers)~td").eq(1).text().trim()).to.eql("30");
      expect(metricsWidget.childViews.papersTableView.render().$("td:contains(Number of papers)~td").eq(2).text().trim()).to.eql("1");

      expect(metricsWidget.childViews.readsTableView.render().$("td:contains(Total number of downloads)~td").eq(1).text().trim()).to.eql("559");
      expect(metricsWidget.childViews.readsTableView.render().$("td:contains(Total number of downloads)~td").eq(2).text().trim()).to.eql("62");

      expect(metricsWidget.childViews.citationsTableView.render().$("td:contains(Average refereed citations)~td").eq(1).text().trim()).to.eql("0.4");
      expect(metricsWidget.childViews.citationsTableView.render().$("td:contains(Average refereed citations)~td").eq(2).text().trim()).to.eql("0");

      expect(metricsWidget.childViews.indicesTableView.render().$("td:contains(i10-index)~td").eq(1).text().trim()).to.eql("0");
      expect(metricsWidget.childViews.indicesTableView.render().$("td:contains(i10-index)~td").eq(2).text().trim()).to.eql("0");

    })

    it("should have a function that creates graph views out of the raw api response", function(){

      var metricsWidget = new MetricsWidget();

      metricsWidget.createGraphViews(testData);

      //should have 4 graph views
      expect(_.keys(metricsWidget.childViews)).to.eql(["papersGraphView", "citationsGraphView", "indicesGraphView", "readsGraphView"]);

      //they should be instances of GraphView
      expect(metricsWidget.childViews.papersGraphView).to.be.instanceof(metricsWidget.components.GraphView);

      //they should have the proper data in their models
      expect(metricsWidget.childViews.citationsGraphView.model.get("graphData")).to.eql(DataExtractor.plot_citshist({norm: false, citshist_data: testData["histograms"]["citations"]}));
      expect(metricsWidget.childViews.citationsGraphView.model.get("normalizedGraphData")).to.eql(DataExtractor.plot_citshist({norm: true, citshist_data: testData["histograms"]["citations"]}));

    })

    it("should have a container view (marionette layout) that arranges the child views", function(done){

      var metricsWidget = new MetricsWidget();

      metricsWidget.processResponse(new JsonResponse(testData));

      //check to see that the rendered views are inserted

      $("#test").append(metricsWidget.view.el);

      expect($("#test").find((".metrics-graph")).length).to.eql(4);
      expect($("#test").find((".metrics-table")).length).to.eql(4);

      done();


    })


    it("should request data from pubsub, then send that data to the metrics endpoint, then render the graph", function(done){


      var metricsWidget = new MetricsWidget();

      var minsub = new (MinimalPubSub.extend({
        request: function(apiRequest) {
          if (apiRequest.toJSON().target === ApiTargets.SEARCH){
            return {
              "responseHeader":{
                "status":0,
                "QTime":1,
                "params":{
                  "fl":"bibcode",
                  "indent":"true",
                  "rows": 200,
                  "wt":"json",
                  "q":"bibcode:(\"1980ApJS...44..137K\" OR \"1980ApJS...44..489B\")\n"}},
              "response":{"numFound":2,"start":0,"docs":[
                {
                  "bibcode":"1980ApJS...44..489B"},
                {
                  "bibcode":"1980ApJS...44..137K"}]
              }};
          }
          //just to be explicit
          else if (apiRequest.toJSON().target === ApiTargets.SERVICE_METRICS){
            return testData;
          }
        }
      }))({verbose: false});

      metricsWidget.activate(minsub.beehive.getHardenedInstance());

      expect(metricsWidget.childViews.citationsTableView).to.be.undefined;

      //provide widget with current query
      minsub.publish(minsub.START_SEARCH, new ApiQuery({q : "star"}));

      //trigger show event, should prompt dispatchRequest
      metricsWidget.showMetricsForCurrentQuery();

      setTimeout(function() {
        //if the views received the data, the 2 step request process worked
        expect(metricsWidget.childViews.citationsTableView.model.attributes.medianCitations).to.eql([ 2, 1 ]);
        done();
      }, 5);
    });


    it("should allow the user to request a different number of documents", function(done){

      var minsub = new (MinimalPubSub.extend({
        request: function (apiRequest) {
          this.counter = this.counter || 0;
          if (apiRequest.toJSON().target === ApiTargets.SEARCH && this.counter == 0) {
            this.counter++;
            return {
              "responseHeader": {
                "status": 0,
                "QTime": 1,
                "params": {
                  "fl": "bibcode",
                  "indent": "true",
                  "wt": "json",
                  "rows": 200,
                  "q": "bibcode:(\"1980ApJS...44..137K\" OR \"1980ApJS...44..489B\")\n"}},
              "response": {"numFound": 2, "start": 0, "docs": [
                {
                  "bibcode": "1980ApJS...44..489B"},
                {
                  "bibcode": "1980ApJS...44..137K"}
              ]
              }}
          }
          else if (apiRequest.toJSON().target === ApiTargets.SEARCH && this.counter > 1){
            return {
              "responseHeader": {
                "status": 0,
                "QTime": 1,
                "params": {
                  "fl": "bibcode",
                  "indent": "true",
                  "wt": "json",
                  "rows": 1,
                  "q": "bibcode:(\"1980ApJS...44..137K\" OR \"1980ApJS...44..489B\")\n"}},
              "response": {"numFound": 2, "start": 0, "docs": [
                {
                  "bibcode": "1980ApJS...44..489B"},
                {
                  "bibcode": "1980ApJS...44..137K"}
              ]
              }}
          }
          //just to be explicit
          else if (apiRequest.toJSON().target === ApiTargets.SERVICE_METRICS ) {
            this.counter++;
            return testData;
          }
        }
      }))({verbose: false});

      var metricsWidget = new MetricsWidget();
      metricsWidget.activate(minsub.beehive.getHardenedInstance());

      $("#test").append(metricsWidget.view.render().el);

      //provide widget with current query
      minsub.publish(minsub.START_SEARCH, new ApiQuery({q : "star"}));

      metricsWidget.showMetricsForCurrentQuery();
      expect($("#test").find(".metrics-metadata").text().trim()).to.eql('Currently viewing metrics for 2\n    \n     papers.\n    \n \nChange to first  paper(s) (max is 2).\n Submit');


        sinon.spy(metricsWidget.getPubSub(), "publish");

        $("#test").find(".metrics-metadata input").val("1");
        $("#test").find(".metrics-metadata button.submit-rows").trigger("click");

        setTimeout(function(){
          expect(metricsWidget.getPubSub().publish.args[0][0]).to.eql(minsub.EXECUTE_REQUEST);
          expect(metricsWidget.getPubSub().publish.args[0][1].get("query").toJSON().rows).to.eql([1]);
          expect($("#test").find(".metrics-metadata").text().trim()).to.eql("Loading data...");
          done();
        }, 1000)


    });
  })
});