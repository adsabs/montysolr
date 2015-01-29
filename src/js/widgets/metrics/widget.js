define([
  'marionette',
  'nvd3',
  'js/widgets/base/base_widget',
  './extractor_functions',
  'js/components/api_response',
  'js/components/json_response',
  'js/components/api_request',
  'js/components/api_query',
  'hbs!./templates/metrics_metadata',
  'hbs!./templates/metrics_container',
  'hbs!./templates/graph_template',
  'hbs!./templates/paper_table',
  'hbs!./templates/citations_table',
  'hbs!./templates/indices_table',
  'hbs!./templates/reads_table',
  'bootstrap',
  'js/components/api_feedback',
  'js/components/api_targets'
], function (
  Marionette,
  nvd3,
  BaseWidget,
  DataExtractor,
  ApiResponse,
  JsonResponse,
  ApiRequest,
  ApiQuery,
  MetricsMetadataTemplate,
  MetricsContainer,
  GraphTemplate,
  PaperTableTemplate,
  CitationsTableTemplate,
  IndicesTableTemplate,
  ReadsTableTemplate,
  bs,
  ApiFeedback,
  ApiTargets
  ) {

  var TableModel = Backbone.Model.extend({

    defaults: function () {
      return {
        title: [],
        rows: []
      }
    }
  });

  var TableView = Marionette.ItemView.extend({

    className: "table table-hover s-metrics-table table-condensed",
    tagName: "table",
    onRender: function () {
      this.$('[data-toggle="popover"]').popover(
        {
          html: true,
          trigger: "hover"
        }
      )
    }
  });

  var GraphModel = Backbone.Model.extend({
    defaults: function () {
      return {
        //precompute this data to have on hand
        graphData: undefined,
        normalizedGraphData: undefined,
        //when this changes, graph view changes to normalized
        normalized: false,
        //tells graph which options to allow
        //right now we do "histogram" and "line"
        graphType: "histogram"
      }
    }
  });

  var GraphView = Marionette.ItemView.extend({

    className: "graph-view s-graph-view",
    initialize: function () {
      this.initial = true;
      this.listenTo(this.model, "change:normalized", this.drawGraph);
    },

    ui: {
      svg: "svg"
    },

    events: {
      "click .graph-tab": "updateTabValue"
    },

    updateTabValue: function (e) {
      var $e = $(e.currentTarget);
      this.$(".graph-tab").removeClass("active");
      $e.addClass("active");
      var val = $e.data("tab");
      if (val === "normalized") {
        this.model.set("normalized", true);
      }
      else {
        this.model.set("normalized", false)
      }
    },

    colors  : ["#5683e0", "#7ab889", "#ffb639", "#ed5e5b", "#ce5cff", "#1c459b"],

    drawHistogram: function () {

      var data, d3SVG;
      var that = this;
      d3SVG = d3.select(this.ui.svg[0]);

      //for the tooltip
      //tells you the total value of y for a given x value
      //e.g. adds refereed and non-refereed
      function countAll(data, year) {
        var total = 0;
        _.each(data, function (d) {
          var val = _.filter(d.values, function (v) {
            if (v.x == year) {
              return true
            }
          });
          total+= val[0].y;
        });
        return  total;
      }

      //get data
      data = this.model.get("normalized") ? this.model.get("normalizedGraphData") : this.model.get("graphData");

      //make a copy
      data =  $.extend(true, [], data);

      //figure out : is this a transition or a re-draw
      if (that.chart) {
        //it's a transition

       //overriding library tooltip function
        that.chart.tooltip(function (key, x, y, e, graph) {
          var total = countAll(data, x);
          return  '<h3>'+x+'</h3>'+
            '<p><b>' + key + "</b>: " +  y  + '</p>' +
            '<p><b> Total: </b>' + total.toFixed(1) + '</p>'
        });

        d3SVG
          .datum(data)
          .transition()
          .duration(500)
          .call(that.chart);
      }
      else {

        nv.addGraph(function () {
          that.chart = nv.models.multiBarChart();

          that.chart
            .color(that.colors);

          that.chart.yAxis
            //only touch values that are divisible by 1 to get rid of #s that look like "4.0"
            .tickFormat(function(d){
              if (d === 0 ){
                //sometimes -0.1 was showing on the y axis, I dont know why
                return 0
              }
              if (d % 1 !== 0){
                return d3.format(",.01f")(d)
              }
              else {
                return d3.format(",.0d")(d)
              }

            });

          /*
           * This is how you make the chart stacked by default
           * Not documented anywhere.
           * */
          that.chart.multibar.stacked(true);

          //overriding library tooltip function
          that.chart.tooltip(function (key, x, y, e, graph) {
            var total = countAll(data, x);
            return  '<h3>'+x+'</h3>'+
              '<p><b>' + key + "</b>: " +  y  + '</p>' +
              '<p><b> Total: </b>' +  total.toFixed(1) + '</p>'
          });

          d3SVG
            .datum(data)
            .call(that.chart);
        });
      }
    },

    drawLineGraph: function () {
      var data, d3SVG, options;
      var that = this;

      d3SVG = d3.select(this.ui.svg[0]);

      //get data
      data = this.model.get("normalized") ? this.model.get("normalizedGraphData") : this.model.get("graphData");

      //make a copy
      data =  $.extend(true, [], data);

      //figure out : is this a transition or a re-draw
      if (that.chart) {
        d3SVG
          .datum(data)
          .transition()
          .duration(500)
          .call(that.chart);
      }
      else {
        options = {
          showControls: false
        };

        nv.addGraph(function () {
          that.chart = nv.models.lineChart()
            .x(function (d) {
              return d.x
            })
            .y(function (d) {
              return d.y
            })
            .color(that.colors)
            .useInteractiveGuideline(true)
            .options(options);

          that.chart.yAxis
            .tickFormat(d3.format(",.0f"));

          d3SVG.datum(data)
            .call(that.chart);
        });
      }
    },

    //listens on change events
    drawGraph: function () {

      var type = this.model.get("graphType");
      //find correct function
      if (type === "histogram") {
        this.drawHistogram();
      }
      else if (type === "line") {
        this.drawLineGraph();
      }
    },

    template: GraphTemplate,
    onRender: function () {
      this.drawGraph();
    }
  });


  var ContainerModel = Backbone.Model.extend({

    initialize : function(){
      this.on("change:numFound", this.updateMax);
      this.on("change:rows", this.updateCurrent);
    },

    updateMax : function() {
      var m = _.min([300, this.get("numFound")]);
      if (m == 1)
        m = 0;
      this.set("max", m);
    },

    updateCurrent : function(){
      this.set("current", _.min([this.get("rows"), this.get("numFound")]));
    },

    defaults : function(){
      return {
        rows : undefined,
        numFound : undefined,
        current : undefined,
        max : undefined,
        userVal: undefined
      }
    }
  });

  var ContainerView = Marionette.Layout.extend({

    onRender : function(){
      this.renderMetadata();
    },

    //function to just re-render the metadata part at the bottom
    renderMetadata : function(){
      var data = {};
      data.max = this.model.get("max");
      data.current = this.model.get("current");
      this.$(".metrics-metadata").html(this.metadataTemplate(data));
    },

    template: MetricsContainer,
    metadataTemplate : MetricsMetadataTemplate,
    events : {
      "click .submit-rows" : "changeRows",
      "click .close-widget": "signalCloseWidget"
    },

    changeRows : function(e) {
      var num = parseInt(this.$(".metrics-rows").val());
      if (num){
        this.model.set("userVal", _.min([this.model.get("max"), num]));
      }
    },

    signalCloseWidget: function () {
      this.trigger('close-widget');
    },

    regions: {
      papersGraph: "#papers .metrics-graph",
      papersTable: "#papers .metrics-table",
      citationsGraph: "#citations .metrics-graph",
      citationsTable: "#citations .metrics-table",
      indicesGraph: "#indices .metrics-graph",
      indicesTable: "#indices .metrics-table",
      readsGraph: "#reads .metrics-graph",
      readsTable: "#reads .metrics-table"
    }
  });

  var MetricsWidget = BaseWidget.extend({

    viewEvents: {
      'close-widget': 'closeWidget'
    },

    initialize: function (options) {
      options = options || {};
      this.containerModel = new ContainerModel();
      this.listenTo(this.containerModel, "change:userVal", this.requestDifferentRows);
      this.view = new ContainerView({model : this.containerModel});
      this.childViews = {};
      Marionette.bindEntityEvents(this, this.view, Marionette.getOption(this, "viewEvents"));
    },

    closeWidget: function () {
      this.resetWidget();
      this.pubsub.publish(this.pubsub.NAVIGATE, "results-page");
    },

    resetWidget: function () {
      this.childViews = {};
      //empty the container view
      _.each(this.view.regions, function(v,k){
        if (this.view[k].currentView)
          this.view[k].currentView.close();
      }, this);
    },

    //when a user requests a different number of documents
    requestDifferentRows : function(model, rows){
      var query = this.getCurrentQuery().clone();
      query.unlock();
      query.set("rows", model.get("userVal"));
      query.set("fl", "bibcode");

      var request = new ApiRequest({
        target : ApiTargets.SEARCH,
        query : query
      });
      this.pubsub.publish(this.pubsub.EXECUTE_REQUEST, request);
    },

    processResponse: function (response) {
      if (response instanceof ApiResponse){
        var bibcodes = _.map(response.get("response.docs"), function(d){return d.bibcode});
        var options = {
          type : "POST",
          contentType : "application/json"
        };

        var request =  new ApiRequest({
          target: ApiTargets.SERVICE_METRICS,
          query: new ApiQuery({"bibcodes" : bibcodes}),
          options : options
        });

        // let container view know how many bibcodes we have
        this.view.model.set({"numFound": parseInt(response.get("response.numFound")),
                              "rows":  parseInt(response.get("responseHeader.params.rows"))});
        this.pubsub.publish(this.pubsub.EXECUTE_REQUEST, request);
      }
      //it's from the metrics endpoint
      else if (response instanceof JsonResponse ) {

        //is it a response with bibcodes from solr or a response with metrics?
        response = response.toJSON();

        //how is the json response formed? need to figure out why attributes is there
        response = response.attributes ? response.attributes : response;

        // for now, metrics api returns errors as 200 messages, so we have to detect it
        if (response.msg && response.msg.indexOf('Unable to get results') > -1) {
          this.pubsub.publish(this.pubsub.ALERT, new ApiFeedback({
            code: ApiFeedback.CODES.ALERT,
            msg: 'Unfortunately, the metrics service returned error (it affects only some queries). Please retry later.',
            modal: true
          }));
          return;
        }

        this.createTableViews(response);
        this.createGraphViews(response);
        this.insertViews();
      }
    },

    createTableData : function(response){
      var data = {};
      var generalData = {refereed: response["refereed stats"], total: response["all stats"]};
      var readsData = {refereed: response["refereed reads"], total: response["all reads"]};

      data.paperModelData = {
        totalNumberOfPapers: [generalData.total["Number of papers"], generalData.refereed["Number of papers"]],
        totalNormalizedPaperCount: [generalData.total["Normalized paper count"], generalData.refereed["Normalized paper count"]]
      };

      data.readsModelData = {
        totalNumberOfReads: [readsData.total["Total number of reads"], readsData.refereed["Total number of reads"]],
        averageNumberOfReads: [readsData.total["Average number of reads"], readsData.refereed["Average number of reads"]],
        medianNumberOfReads: [readsData.total["Median number of reads"], readsData.refereed["Median number of reads"]],
        totalNumberOfDownloads: [readsData.total["Total number of downloads"], readsData.refereed["Total number of downloads"] ],
        averageNumberOfDownloads: [readsData.total["Average number of downloads"], readsData.refereed["Average number of downloads"]],
        medianNumberOfDownloads: [readsData.total["Median number of reads"], readsData.total["Median number of reads"]]
      };

      data.citationsModelData = {
        numberOfCitingPapers: [generalData.total["Number of citing papers"], generalData.refereed["Number of citing papers"]],
        totalCitations: [generalData.total["Total citations"], generalData.refereed["Total citations"]],
        numberOfSelfCitations: [generalData.total["self-citations"], generalData.refereed["self-citations"]],
        averageCitations: [generalData.total["Average citations"], generalData.refereed["Average citations"]],
        medianCitations: [generalData.total["Median citations"], generalData.refereed["Median citations"]],
        normalizedCitations: [generalData.total["Normalized citations"], generalData.refereed["Normalized citations"]],
        refereedCitations: [generalData.total["Refereed citations"], generalData.refereed["Refereed citations"]],
        averageRefereedCitations: [generalData.total["Average refereed citations"], generalData.refereed["Average refereed citations"]],
        medianRefereedCitations: [generalData.total["Median refereed citations"], generalData.refereed["Median refereed citations"]],
        normalizedRefereedCitations: [generalData.total["Normalized refereed citations"], generalData.refereed["Normalized refereed citations"]]
      };

      data.indicesModelData = {
        hIndex: [generalData.total["H-index"], generalData.refereed["H-index"]],
        mIndex: [generalData.total["m-index"], generalData.refereed["m-index"]],
        gIndex: [generalData.total["g-index"], generalData.refereed["g-index"]],
        i10Index: [generalData.total["i10-index"], generalData.refereed["i10-index"]],
        i100Index: [generalData.total["i100-index"], generalData.refereed["i100-index"]],
        toriIndex: [generalData.total["tori index"], generalData.refereed["tori index"]],
        roqIndex: [generalData.total["roq index"], generalData.refereed["roq index"]],
        read10Index: [generalData.total["read10 index"], generalData.refereed["read10 index"]]
      };
      return data;
    },


    createTableViews: function (response) {

      var tableData = this.createTableData(response);

      this.childViews.papersTableView = new TableView({
        template: PaperTableTemplate,
        model: new TableModel(tableData.paperModelData)
      });

      this.childViews.readsTableView = new TableView({
        template: ReadsTableTemplate,
        model: new TableModel(tableData.readsModelData)
      });

      this.childViews.citationsTableView = new TableView({
        model: new TableModel(tableData.citationsModelData),
        template: CitationsTableTemplate
      });

      this.childViews.indicesTableView = new TableView({
        model: new TableModel(tableData.indicesModelData),
        template: IndicesTableTemplate
      });

    },

    createGraphViews: function (response) {
      //papers graph
      var papersModel = new GraphModel();
      this.childViews.papersGraphView = new GraphView({model: papersModel });
      this.childViews.papersGraphView.model.set("graphData", DataExtractor.plot_paperhist({norm: false, paperhist_data: response["paper histogram"]}));
      this.childViews.papersGraphView.model.set("normalizedGraphData", DataExtractor.plot_paperhist({norm: true, paperhist_data: response["paper histogram"]}));

      //citations graph
      var citationsModel = new GraphModel();
      this.childViews.citationsGraphView = new GraphView({model: citationsModel });
      this.childViews.citationsGraphView.model.set("graphData", DataExtractor.plot_citshist({norm: false, citshist_data: response["citation histogram"]}));
      this.childViews.citationsGraphView.model.set("normalizedGraphData", DataExtractor.plot_citshist({norm: true, citshist_data: response["citation histogram"]}));

      //indices graph
      var indicesModel = new GraphModel({graphType: "line"});
      this.childViews.indicesGraphView = new GraphView({model: indicesModel });
      this.childViews.indicesGraphView.model.set("graphData", DataExtractor.plot_series({series_data: response["metrics series"]}));

      //reads graph
      var readsModel = new GraphModel();
      this.childViews.readsGraphView = new GraphView({model: readsModel });
      this.childViews.readsGraphView.model.set("graphData", DataExtractor.plot_readshist({norm: false, readshist_data: response["reads histogram"]}));
      this.childViews.readsGraphView.model.set("normalizedGraphData", DataExtractor.plot_readshist({norm: true, readshist_data: response["reads histogram"]}));
    },

    insertViews: function () {

      //render the container view
      this.view.render();

      //attach table views
      this.view.papersTable.show(this.childViews.papersTableView);
      this.view.citationsTable.show(this.childViews.citationsTableView);
      this.view.indicesTable.show(this.childViews.indicesTableView);
      this.view.readsTable.show(this.childViews.readsTableView);

      //attach graph views

      this.view.papersGraph.show(this.childViews.papersGraphView);
      this.view.citationsGraph.show(this.childViews.citationsGraphView);
      this.view.indicesGraph.show(this.childViews.indicesGraphView);
      this.view.readsGraph.show(this.childViews.readsGraphView);
    },

    //so I can test these individually
    components: {

      TableModel: TableModel,
      TableView: TableView,
      GraphView: GraphView,
      GraphModel: GraphModel,
      ContainerView: ContainerView
    },

    defaultQueryArguments : {
      fl : "bibcode",
      rows : 100
    },


    activate : function(beehive){
      _.bindAll(this, "setCurrentQuery", "processResponse");
      this.pubsub = beehive.Services.get('PubSub');
      this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.setCurrentQuery);
      this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
    },

    //fetch data
    onShow : function(){
      this.resetWidget();
      this.dispatchRequest(this.getCurrentQuery());
    }
  });

  return MetricsWidget;
});