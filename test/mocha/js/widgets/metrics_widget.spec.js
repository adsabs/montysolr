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
      "all reads": {
        "Average number of downloads": 51.0,
        "Average number of reads": 75.0,
        "Median number of downloads": 51.0,
        "Median number of reads": 75.0,
        "Normalized number of downloads": 90.0,
        "Normalized number of reads": 129.0,
        "Total number of downloads": 102,
        "Total number of reads": 150
      },
      "all stats": {
        "Average citations": 38.5,
        "Average refereed citations": 37.0,
        "H-index": 2,
        "Median citations": 38.5,
        "Median refereed citations": 37.0,
        "Normalized citations": 71.5,
        "Normalized paper count": 1.5,
        "Normalized refereed citations": 69.0,
        "Number of citing papers": 77,
        "Number of papers": 2,
        "Refereed citations": 74,
        "Total citations": 77,
        "e-index": 8.5,
        "g-index": 2,
        "i10-index": 2,
        "i100-index": 0,
        "m-index": 2.0,
        "read10 index": 0,
        "roq index": 1792.0,
        "self-citations": 0,
        "tori index": 3.2
      },
      "citation histogram": {
        "1980": "0:0:0:0:0.0:0.0:0.0:0.0",
        "1981": "4:4:4:4:4.0:4.0:4.0:4.0",
        "1982": "5:5:5:5:4.0:4.0:4.0:4.0",
        "1983": "5:4:5:4:4.0:3.5:4.0:3.5",
        "1984": "3:3:3:3:3.0:3.0:3.0:3.0",
        "1985": "7:7:7:7:7.0:7.0:7.0:7.0",
        "1986": "4:4:4:4:4.0:4.0:4.0:4.0",
        "1987": "8:7:8:7:7.5:6.5:7.5:6.5",
        "1988": "7:7:7:7:7.0:7.0:7.0:7.0",
        "1989": "1:1:1:1:1.0:1.0:1.0:1.0",
        "1990": "5:5:5:5:4.5:4.5:4.5:4.5",
        "1991": "2:2:2:2:2.0:2.0:2.0:2.0",
        "1992": "2:2:2:2:1.5:1.5:1.5:1.5",
        "1993": "1:1:1:1:1.0:1.0:1.0:1.0",
        "1994": "5:5:5:5:4.5:4.5:4.5:4.5",
        "1995": "0:0:0:0:0.0:0.0:0.0:0.0",
        "1996": "3:3:3:3:2.5:2.5:2.5:2.5",
        "1997": "1:1:1:1:1.0:1.0:1.0:1.0",
        "1998": "2:2:2:2:2.0:2.0:2.0:2.0",
        "1999": "3:3:3:3:3.0:3.0:3.0:3.0",
        "2000": "2:1:2:1:2.0:1.0:2.0:1.0",
        "2001": "1:1:1:1:1.0:1.0:1.0:1.0",
        "2002": "2:2:2:2:1.5:1.5:1.5:1.5",
        "2003": "0:0:0:0:0.0:0.0:0.0:0.0",
        "2004": "0:0:0:0:0.0:0.0:0.0:0.0",
        "2005": "0:0:0:0:0.0:0.0:0.0:0.0",
        "2006": "1:1:1:1:1.0:1.0:1.0:1.0",
        "2007": "1:1:1:1:0.5:0.5:0.5:0.5",
        "2008": "0:0:0:0:0.0:0.0:0.0:0.0",
        "2009": "1:1:1:1:1.0:1.0:1.0:1.0",
        "2010": "0:0:0:0:0.0:0.0:0.0:0.0",
        "2011": "0:0:0:0:0.0:0.0:0.0:0.0",
        "2012": "1:1:1:1:1.0:1.0:1.0:1.0",
        "2013": "0:0:0:0:0.0:0.0:0.0:0.0",
        "2014": "0:0:0:0:0.0:0.0:0.0:0.0",
        "type": "citation_histogram"
      },
      "metrics series": {
        "1980": "0:0:0:0:0.0:0:0:0",
        "1981": "1:2:0:0.472023809524:0.5:343:0:0",
        "1982": "2:2:0:0.645851606393:0.666666666667:267:0:0",
        "1983": "2:2:1:1.10189227625:0.5:262:0:0",
        "1984": "2:2:1:1.22747569666:0.4:221:0:0",
        "1985": "2:2:1:1.43852512908:0.333333333333:199:0:0",
        "1986": "2:2:1:1.59290955994:0.285714285714:180:0:0",
        "1987": "2:2:1:2.00479397183:0.25:176:0:0",
        "1988": "2:2:1:2.24667667969:0.222222222222:166:0:0",
        "1989": "2:2:1:2.28667667969:0.2:151:0:0",
        "1990": "2:2:1:2.59707762952:0.181818181818:146:0:0",
        "1991": "2:2:1:2.63106455763:0.166666666667:135:0:0",
        "1992": "2:2:1:2.67899484085:0.153846153846:125:0:0",
        "1993": "2:2:1:2.7316264198:0.142857142857:118:0:0",
        "1994": "2:2:1:2.83935637802:0.133333333333:112:0:0",
        "1995": "2:2:1:2.83935637802:0.125:105:0:0",
        "1996": "2:2:1:2.88540869527:0.117647058824:99:0:0",
        "1997": "2:2:1:2.89969440955:0.111111111111:94:0:0",
        "1998": "2:2:1:2.95326583812:0.105263157895:90:0:0",
        "1999": "2:2:1:2.98340195637:0.1:86:0:0",
        "2000": "2:2:1:3.1525748887:0.0952380952381:84:0:0",
        "2001": "2:2:1:3.16003757527:0.0909090909091:80:0:0",
        "2002": "2:2:2:3.1837423651:0.0869565217391:77:0:0",
        "2003": "2:2:2:3.1837423651:0.0833333333333:74:0:0",
        "2004": "2:2:2:3.1837423651:0.08:71:0:0",
        "2005": "2:2:2:3.1837423651:0.0769230769231:68:0:0",
        "2006": "2:2:2:3.19088522224:0.0740740740741:66:0:0",
        "2007": "2:2:2:3.20199633335:0.0714285714286:63:0:0",
        "2008": "2:2:2:3.20199633335:0.0689655172414:61:0:0",
        "2009": "2:2:2:3.21084589088:0.0666666666667:59:0:0",
        "2010": "2:2:2:3.21084589088:0.0645161290323:57:0:0",
        "2011": "2:2:2:3.21084589088:0.0625:55:0:0",
        "2012": "2:2:2:3.21246663642:0.0606060606061:54:0:0",
        "2013": "2:2:2:3.21246663642:0.0588235294118:52:0:0",
        "2014": "2:2:2:3.21246663642:0.0571428571429:51:0:0",
        "type": "metrics_series"
      },
      "paper histogram": {
        "1980": "2:2:1.5:1.5",
        "type": "publication_histogram"
      },
      "reads histogram": {
        "1996": "0:0:0.0:0.0",
        "1997": "0:0:0.0:0.0",
        "1998": "2:2:2.0:2.0",
        "1999": "7:7:6.0:6.0",
        "2000": "4:4:4.0:4.0",
        "2001": "6:6:5.0:5.0",
        "2002": "15:15:14.5:14.5",
        "2003": "5:5:5.0:5.0",
        "2004": "20:20:17.0:17.0",
        "2005": "4:4:4.0:4.0",
        "2006": "10:10:9.0:9.0",
        "2007": "7:7:6.0:6.0",
        "2008": "15:15:14.0:14.0",
        "2009": "12:12:9.5:9.5",
        "2010": "7:7:5.0:5.0",
        "2011": "12:12:10.5:10.5",
        "2012": "13:13:9.0:9.0",
        "2013": "4:4:3.5:3.5",
        "2014": "7:7:5.0:5.0",
        "type": "reads_histogram"
      },
      "refereed reads": {
        "Average number of downloads": 51.0,
        "Average number of reads": 75.0,
        "Median number of downloads": 51.0,
        "Median number of reads": 75.0,
        "Normalized number of downloads": 90.0,
        "Normalized number of reads": 129.0,
        "Total number of downloads": 102,
        "Total number of reads": 150
      },
      "refereed stats": {
        "Average citations": 38.5,
        "Average refereed citations": 37.0,
        "H-index": 2,
        "Median citations": 38.5,
        "Median refereed citations": 37.0,
        "Normalized citations": 71.5,
        "Normalized paper count": 1.5,
        "Normalized refereed citations": 69.0,
        "Number of citing papers": 77,
        "Number of papers": 2,
        "Refereed citations": 74,
        "Total citations": 77,
        "e-index": 8.5,
        "g-index": 2,
        "i10-index": 2,
        "i100-index": 0,
        "m-index": 2.0,
        "read10 index": 0,
        "roq index": 1792.0,
        "self-citations": 0,
        "tori index": 3.2
      }
    };

    //the smaller more manageable version
    var testDataSmall = {
      "all reads": {
        "Average number of downloads": 51.0,
        "Average number of reads": 75.0,
        "Median number of downloads": 51.0,
        "Median number of reads": 75.0,
        "Normalized number of downloads": 90.0,
        "Normalized number of reads": 129.0,
        "Total number of downloads": 102,
        "Total number of reads": 150
      },
      "all stats": {
        "Average citations": 38.5,
        "Average refereed citations": 37.0,
        "H-index": 2,
        "Median citations": 38.5,
        "Median refereed citations": 37.0,
        "Normalized citations": 71.5,
        "Normalized paper count": 1.5,
        "Normalized refereed citations": 69.0,
        "Number of citing papers": 77,
        "Number of papers": 2,
        "Refereed citations": 74,
        "Total citations": 77,
        "e-index": 8.5,
        "g-index": 2,
        "i10-index": 2,
        "i100-index": 0,
        "m-index": 2.0,
        "read10 index": 0,
        "roq index": 1792.0,
        "self-citations": 0,
        "tori index": 3.2
      },
      "citation histogram": {
        "1980": "0:0:0:0:0.0:0.0:0.0:0.0",
        "1981": "4:4:4:4:4.0:4.0:4.0:4.0",
        "type": "citation_histogram"
      },
      "metrics series": {
        "1980": "0:0:0:0:0.0:0:0:0",
        "1981": "1:2:0:0.472023809524:0.5:343:0:17",
        "type": "metrics_series"
      },
      "paper histogram": {
        "1980": "2:2:1.5:1.5",
        "type": "publication_histogram"
      },
      "reads histogram": {
        "1996": "0:0:0.0:0.0",
        "1997": "0:0:0.0:0.0",
        "type": "reads_histogram"
      },
      "refereed reads": {
        "Average number of downloads": 51.0,
        "Average number of reads": 75.0,
        "Median number of downloads": 51.0,
        "Median number of reads": 75.0,
        "Normalized number of downloads": 90.0,
        "Normalized number of reads": 129.0,
        "Total number of downloads": 102,
        "Total number of reads": 150
      },
      "refereed stats": {
        "Average citations": 38.5,
        "Average refereed citations": 37.0,
        "H-index": 2,
        "Median citations": 38.5,
        "Median refereed citations": 37.0,
        "Normalized citations": 71.5,
        "Normalized paper count": 1.5,
        "Normalized refereed citations": 69.0,
        "Number of citing papers": 77,
        "Number of papers": 2,
        "Refereed citations": 74,
        "Total citations": 77,
        "e-index": 8.5,
        "g-index": 2,
        "i10-index": 2,
        "i100-index": 0,
        "m-index": 2.0,
        "read10 index": 0,
        "roq index": 1792.0,
        "self-citations": 0,
        "tori index": 3.2
      }
    };


    afterEach(function(){

      $("#test").empty();

    })

    //first, test Edwin's functions

    it("should have a data extractor object that takes metrics data and prepares json for the nvd3 graph", function(){

      var citshist = DataExtractor.plot_citshist({norm : false, citshist_data : testDataSmall["citation histogram"]});

      var norm_citshist =  DataExtractor.plot_citshist({norm : true, citshist_data : testDataSmall["citation histogram"]});

      var readshist = DataExtractor.plot_readshist({norm: false, readshist_data : testDataSmall["reads histogram"]});

      var norm_readshist  = DataExtractor.plot_readshist({norm: true, readshist_data : testDataSmall["reads histogram"]});

      var paperhist = DataExtractor.plot_paperhist({norm : true, paperhist_data : testDataSmall["paper histogram"]});

      var norm_paperhist = DataExtractor.plot_paperhist({norm : false, paperhist_data : testDataSmall["paper histogram"]});

      var indexes_data = DataExtractor.plot_series({series_data : testDataSmall["metrics series"]});

      expect(citshist).to.eql([{
        "key": "Ref. citations to ref. papers",
        "values": [
          {
            "x": "1980",
            "y": 0
          },
          {
            "x": "1981",
            "y": 4
          }
        ]
      },
        {
          "key": "Ref. citations to non ref. papers",
          "values": [
            {
              "x": "1980",
              "y": 0
            },
            {
              "x": "1981",
              "y": 0
            }
          ]
        },
        {
          "key": "Non ref. citations to ref. papers",
          "values": [
            {
              "x": "1980",
              "y": 0
            },
            {
              "x": "1981",
              "y": 0
            }
          ]
        },
        {
          "key": "Non ref. citations to non ref. papers",
          "values": [
            {
              "x": "1980",
              "y": 0
            },
            {
              "x": "1981",
              "y": 0
            }
          ]
        }
      ]);

      expect(norm_citshist).to.eql([
        {
          "key": "Ref. citations to ref. papers",
          "values": [
            {
              "x": "1980",
              "y": 0
            },
            {
              "x": "1981",
              "y": 4
            }
          ]
        },
        {
          "key": "Ref. citations to non ref. papers",
          "values": [
            {
              "x": "1980",
              "y": 0
            },
            {
              "x": "1981",
              "y": 0
            }
          ]
        },
        {
          "key": "Non ref. citations to ref. papers",
          "values": [
            {
              "x": "1980",
              "y": 0
            },
            {
              "x": "1981",
              "y": 0
            }
          ]
        },
        {
          "key": "Non ref. citations to non ref. papers",
          "values": [
            {
              "x": "1980",
              "y": 0
            },
            {
              "x": "1981",
              "y": 0
            }
          ]
        }
      ]);

      expect(readshist).to.eql([
        {
          "key": "Refereed",
          "values": [
            {
              "x": 1996,
              "y": 0
            },
            {
              "x": 1997,
              "y": 0
            }
          ]
        },
        {
          "key": "Non-refereed",
          "values": [
            {
              "x": 1996,
              "y": 0
            },
            {
              "x": 1997,
              "y": 0
            }
          ]
        }
      ]);

      expect(norm_readshist).to.eql([
        {
          "key": "Refereed",
          "values": [
            {
              "x": 1996,
              "y": 0
            },
            {
              "x": 1997,
              "y": 0
            }
          ]
        },
        {
          "key": "Non-refereed",
          "values": [
            {
              "x": 1996,
              "y": 0
            },
            {
              "x": 1997,
              "y": 0
            }
          ]
        }
      ]);

      expect(paperhist).to.eql([
        {
          "key": "Refereed",
          "values": [
            {
              "x": 1980,
              "y": 1.5
            }
          ]
        },
        {
          "key": "Non-refereed",
          "values": [
            {
              "x": 1980,
              "y": 0
            }
          ]
        }
      ]);

      expect(norm_paperhist).to.eql([
        {
          "key": "Refereed",
          "values": [
            {
              "x": 1980,
              "y": 2
            }
          ]
        },
        {
          "key": "Non-refereed",
          "values": [
            {
              "x": 1980,
              "y": 0
            }
          ]
        }
      ]);

    //  "metrics series": {
     //   "1980": "0:0:0:0:0.0:0:0:0",
     //     "1981": "1:2:0:0.472023809524:0.5:343:0:17",
     //     "type": "metrics_series"

      //checking parsing
      expect(_.findWhere(indexes_data, {key : "h Index"}).values[1].y).to.eql(1);
      expect(_.findWhere(indexes_data, {key : "g Index"}).values[1].y).to.eql(2);
      expect(_.findWhere(indexes_data, {key : "i10 Index"}).values[1].y).to.eql(0)
      expect(_.findWhere(indexes_data, {key : "tori Index"}).values[1].y).to.eql(0.5);
      expect(_.findWhere(indexes_data, {key : "i100 Index"}).values[1].y).to.eql(0)
      expect(_.findWhere(indexes_data, {key : "read10 Index"}).values[1].y).to.eql(17)



      expect(indexes_data).to.eql([
          {
            "key": "h Index",
            "values": [
              {
                "x": "1980",
                "y": 0
              },
              {
                "x": "1981",
                "y": 1
              }
            ]
          },
          {
            "key": "g Index",
            "values": [
              {
                "x": "1980",
                "y": 0
              },
              {
                "x": "1981",
                "y": 2
              }
            ]
          },
          {
            "key": "i10 Index",
            "values": [
              {
                "x": "1980",
                "y": 0
              },
              {
                "x": "1981",
                "y": 0
              }
            ]
          },
          {
            "key": "tori Index",
            "values": [
              {
                "x": "1980",
                "y": 0
              },
              {
                "x": "1981",
                "y": 0.5
              }
            ]
          },
          {
            "key": "i100 Index",
            "values": [
              {
                "x": "1980",
                "y": 0
              },
              {
                "x": "1981",
                "y": 0
              }
            ]
          },
          {
            "key": "read10 Index",
            "values": [
              {
                "x": "1980",
                "y": 0
              },
              {
                "x": "1981",
                "y": 17
              }
            ]
          }
        ]

      );

    })


    it("should have a function that empties the main view", function(){

      metricsWidget = new MetricsWidget();

      metricsWidget.processResponse(new JsonResponse(testData));

      $("#test").append(metricsWidget.view.el);

      metricsWidget.resetWidget();

      //check to see that the rendered views are inserted

      expect($("#test").find((".metrics-graph *")).length).to.eql(0);
      expect($("#test").find((".metrics-table *")).length).to.eql(0);


    })


    it("should have a configurable graph view that can show a bar chart", function(done){

      this.timeout(3000);


      var metricsWidget = new MetricsWidget();

      var gModel = new metricsWidget.components.GraphModel();

      var graphView = new metricsWidget.components.GraphView({model : gModel });

      graphView.model.set("graphData", DataExtractor.plot_citshist({norm : false, citshist_data : testData["citation histogram"]}));
      graphView.model.set("normalizedGraphData", DataExtractor.plot_citshist({norm : true, citshist_data : testData["citation histogram"]}));

      $("#test").append(graphView.render().el);

      //need to wait for animation to complete

      setTimeout(function(){

        //should show 25 bars representing ref to ref citations
        expect(d3.selectAll("#test g.nv-series-0 rect").filter(function(d){if (d3.select(this).attr("height") > 1 ){return true}})[0].length).to.eql(25);

        //the third of these bars should be shorter than the second
        expect(parseInt(d3.select("#test g.nv-series-0 rect:nth-of-type(3)").attr("y"))).to.be.lessThan(d3.select("#test g.nv-series-0 rect:nth-of-type(2)").attr("y"))

        //all bars from the third series (non ref to ref) should have a height of 0
        expect(d3.selectAll("#test g.nv-series-3 rect").filter(function(){if (d3.select(this).attr("height") == 1){return true}})[0].length).to.eql(35);

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

      graphView.model.set("graphData", DataExtractor.plot_series({series_data : testData["metrics series"]}));

      $("#test").empty();

      $("#test").append(graphView.render().el);

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

      expect(metricsWidget.childViews.papersTableView.render().$("td:contains(Number of papers)~td").eq(1).text().trim()).to.eql("2");
      expect(metricsWidget.childViews.papersTableView.render().$("td:contains(Number of papers)~td").eq(2).text().trim()).to.eql("2");

      expect(metricsWidget.childViews.readsTableView.render().$("td:contains(Total number of downloads)~td").eq(1).text().trim()).to.eql("102");
      expect(metricsWidget.childViews.readsTableView.render().$("td:contains(Total number of downloads)~td").eq(2).text().trim()).to.eql("102");

      expect(metricsWidget.childViews.citationsTableView.render().$("td:contains(Average refereed citations)~td").eq(1).text().trim()).to.eql("37");
      expect(metricsWidget.childViews.citationsTableView.render().$("td:contains(Average refereed citations)~td").eq(2).text().trim()).to.eql("37");

      expect(metricsWidget.childViews.indicesTableView.render().$("td:contains(i10-index)~td").eq(1).text().trim()).to.eql("2");
      expect(metricsWidget.childViews.indicesTableView.render().$("td:contains(i10-index)~td").eq(2).text().trim()).to.eql("2");


    })

    it("should have a function that creates graph views out of the raw api response", function(){

      var metricsWidget = new MetricsWidget();

      metricsWidget.createGraphViews(testData);

      //should have 4 graph views
      expect(_.keys(metricsWidget.childViews)).to.eql(["papersGraphView", "citationsGraphView", "indicesGraphView", "readsGraphView"]);

      //they should be instances of GraphView
      expect(metricsWidget.childViews.papersGraphView).to.be.instanceof(metricsWidget.components.GraphView);

      //they should have the proper data in their models
      expect(metricsWidget.childViews.citationsGraphView.model.get("graphData")).to.eql(DataExtractor.plot_citshist({norm: false, citshist_data: testData["citation histogram"]}));
      expect(metricsWidget.childViews.citationsGraphView.model.get("normalizedGraphData")).to.eql(DataExtractor.plot_citshist({norm: true, citshist_data: testData["citation histogram"]}));


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
      metricsWidget.onShow();

      setTimeout(function() {
        //if the views received the data, the 2 step request process worked
        expect(metricsWidget.childViews.citationsTableView.model.attributes.medianCitations).to.eql([38.5, 38.5]);
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

      //trigger show event, should prompt dispatchRequest
      metricsWidget.onShow();

      setTimeout(function() {
        expect($("#test").find(".metrics-metadata").text().trim()).to.eql('You are viewing metrics for 2 paper(s).\nChange to first  paper(s) (max is 2).\n Submit');

        sinon.spy(metricsWidget.pubsub, "publish");

        $("#test").find(".metrics-metadata input").val("1");
        $("#test").find(".metrics-metadata button.submit-rows").trigger("click");

        setTimeout(function(){
          expect(metricsWidget.pubsub.publish.args[0][0]).to.eql(minsub.EXECUTE_REQUEST);
          expect(metricsWidget.pubsub.publish.args[0][1].get("query").toJSON().rows).to.eql([1]);
          expect($("#test").find(".metrics-metadata").text().trim()).to.eql('You are viewing metrics for 1 paper(s).\nChange to first  paper(s) (max is 2).\n Submit');
          done();
        }, 1000)
      }, 500);


    });
  })
});