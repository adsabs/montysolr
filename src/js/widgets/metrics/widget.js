define([
  'marionette',
  'nvd3',
  'js/widgets/base/base_widget',
  './extractor_functions',
  'js/components/api_response',
  'js/components/json_response',
  'js/components/api_request',
  'js/components/api_query',
  'js/mixins/dependon',
  'js/mixins/user_change_rows',
  'hbs!./templates/metrics_metadata',
  'hbs!./templates/metrics_container',
  'hbs!./templates/graph_template',
  'hbs!./templates/paper_table',
  'hbs!./templates/citations_table',
  'hbs!./templates/indices_table',
  'hbs!./templates/reads_table',
  'bootstrap',
  'js/components/api_feedback',
  'js/components/api_targets',
  'hbs!../network_vis/templates/loading-template'
], function (
  Marionette,
  nvd3,
  BaseWidget,
  DataExtractor,
  ApiResponse,
  JsonResponse,
  ApiRequest,
  ApiQuery,
  Dependon,
  UserChangeMixin,
  MetricsMetadataTemplate,
  MetricsContainer,
  GraphTemplate,
  PaperTableTemplate,
  CitationsTableTemplate,
  IndicesTableTemplate,
  ReadsTableTemplate,
  bs,
  ApiFeedback,
  ApiTargets,
  loadingTemplate
  ) {


  //used for processing data

  function limitPlaces(n){
    var stringNum = n.toString();
    if (stringNum.indexOf(".") > -1 && stringNum.split(".")[1]){
      return n.toFixed(1)
    }
    return n
  }


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
      var data, d3SVG, options, that = this;

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


  var ContainerModel = UserChangeMixin.Model;

  var ContainerView = Marionette.LayoutView.extend({

    onRender : function(){
      this.renderMetadata();
    },

    //function to just re-render the metadata part at the top
    renderMetadata : function(){
        var data = {};
        data.max = this.model.get("max");
        data.current = this.model.get("current");
        data.requestRowsAllowed = this.model.get("requestRowsAllowed");
        this.$(".metrics-metadata").html(this.metadataTemplate(data));
    },

    template: MetricsContainer,
    metadataTemplate : MetricsMetadataTemplate,
    events : {
      "click .submit-rows" : "changeRows",
      "click .close-widget": "signalCloseWidget"
    },

    changeRows : function(e) {
      e.preventDefault();
      var num = parseInt(this.$(".metrics-rows").val());
      //render the loading tempalte
      if (num){
        this.model.set("userVal", _.min([this.model.get("max"), num]));
        this.$(".s-metrics-metadata").html(loadingTemplate());
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

      this.containerModel = new ContainerModel({widgetName : "Metrics"});
      this.listenTo(this.containerModel, "change:userVal", this.requestDifferentRows);
      this.view = new ContainerView({model : this.containerModel});
      this.childViews = {};
      Marionette.bindEntityEvents(this, this.view, Marionette.getOption(this, "viewEvents"));

      //for widgets that extend the base MetricsWidget
      this.dataExtractor = DataExtractor;
      this.GraphModel = GraphModel;
      this.GraphView = GraphView;

      this.defaultQueryArguments =  {
          fl : "bibcode",
          rows : this.containerModel.get("default")
      };

    },

    activate : function(beehive){
      this.setBeeHive(beehive);
      _.bindAll(this, "setCurrentQuery", "processResponse");
      var pubsub = beehive.getService('PubSub');
      pubsub.subscribe(pubsub.INVITING_REQUEST, this.setCurrentQuery);
      pubsub.subscribe(pubsub.DELIVERING_RESPONSE, this.processResponse);

      //on initialization, store the current query
      if (this.getBeeHive().getObject("AppStorage")){
        this.setCurrentQuery(this.getBeeHive().getObject("AppStorage").getCurrentQuery());
      }
    },

    getMetrics : function(bibcodes){

      var d = $.Deferred(),
        pubsub = this.getPubSub(),
        options = {
          type : "POST",
          contentType : "application/json"
        };

      var request =  new ApiRequest({
        target: ApiTargets.SERVICE_METRICS,
        query: new ApiQuery({"bibcodes" : bibcodes}),
        options : options
      });
      // so promise can be resolved

      pubsub.subscribeOnce(pubsub.DELIVERING_RESPONSE, function(response){
        d.resolve(response.toJSON());
      });

      pubsub.publish(pubsub.EXECUTE_REQUEST, request);
      return d.promise();
    },

    processResponse: function (response) {

      //it's bibcodes from the search endpoint
      if (response instanceof ApiResponse){
        var bibcodes = _.map(response.get("response.docs"), function(d){return d.bibcode})
        // let container view know how many bibcodes we have
        this.view.model.set({"numFound": parseInt(response.get("response.numFound")),
          "rows":  parseInt(response.get("responseHeader.params.rows"))});

        //disable option to get more/fewer bibcodes if there's only 1
        if (parseInt(response.get("response.numFound")) === 1){
          this.containerModel.set("requestRowsAllowed", false);
        }

        this.getMetrics(bibcodes);
      }
      //it's from the metrics endpoint
      else if (response instanceof JsonResponse ) {
        this.processMetrics(response);
      }
    },

    closeWidget: function () {
      this.reset();
      this.getPubSub().publish(this.getPubSub().NAVIGATE, "results-page");
    },

    reset: function () {
      this.childViews = {};
      //empty the container view
      _.each(this.view.regions, function(v,k){
        if (this.view[k].currentView)
          this.view[k].currentView.destroy();
      }, this);

      this.containerModel.clear({silent : true}).set(this.containerModel.defaults(), {silent : true});
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
      this.getPubSub().publish(this.getPubSub().EXECUTE_REQUEST, request);
    },

    processMetrics : function (response){
        //how is the json response formed? need to figure out why attributes is there
        response = response.attributes ? response.attributes : response;
        // for now, metrics api returns errors as 200 messages, so we have to detect it
        if ((response.msg && response.msg.indexOf('Unable to get results') > -1) || (response.status == 500)) {
          this.closeWidget();
          this.getPubSub().publish(this.getPubSub().ALERT, new ApiFeedback({
            code: ApiFeedback.CODES.ALERT,
            msg: 'Unfortunately, the metrics service returned error (it affects only some queries). Please try with different search parameters.',
            modal: true
          }));
          return;
        }
      if (response["basic stats"]["number of papers"] === 1){
        this.createTableViews(response, 1);
        this.createGraphViewsForOnePaper(response);
        this.insertViewsForOnePaper(response);
      }
      else {
        this.createTableViews(response);
        this.createGraphViews(response);
        this.insertViews(response);
      }

    },

    createTableViews: function (response, num_bibcodes) {

      var tableData = num_bibcodes === 1 ?  this.createTableDataForOnePaper(response) : this.createTableData(response);

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

    /*
    * functions for >1 bibcode
    * */

    createTableData : function(response){
      var data = {};
      var generalData = {refereed: response["basic stats refereed"], total: response["basic stats"]};
      var citationData = {refereed: response["citation stats refereed"], total: response["citation stats"]};
      var indicesData = {refereed : response["indicators refereed"], total : response["indicators"]};

      data.paperModelData = {
        totalNumberOfPapers: [generalData.total["number of papers"], generalData.refereed["number of papers"]],
        totalNormalizedPaperCount: [generalData.total["normalized paper count"], generalData.refereed["normalized paper count"]]
      };

      data.readsModelData = {
        totalNumberOfReads: [generalData.total["total number of reads"], generalData.refereed["total number of reads"]],
        averageNumberOfReads: [generalData.total["average number of reads"], generalData.refereed["average number of reads"]],
        medianNumberOfReads: [generalData.total["median number of reads"], generalData.refereed["median number of reads"]],
        totalNumberOfDownloads: [generalData.total["total number of downloads"], generalData.refereed["total number of downloads"] ],
        averageNumberOfDownloads: [generalData.total["average number of downloads"], generalData.refereed["average number of downloads"]],
        medianNumberOfDownloads: [generalData.total["median number of downloads"], generalData.total["median number of downloads"]]
      };

      data.citationsModelData = {
        numberOfCitingPapers: [citationData.total["number of citing papers"], citationData.refereed["number of citing papers"]],
        totalCitations: [citationData.total["total number of citations"], citationData.refereed["total number of citations"]],
        numberOfSelfCitations: [citationData.total["number of self-citations"], citationData.refereed["number of self-citations"]],
        averageCitations: [citationData.total["average number of citations"], citationData.refereed["average number of citations"]],
        medianCitations: [citationData.total["median number of citations"],citationData.refereed["median number of citations"]],
        normalizedCitations: [citationData.total["normalized number of citations"], citationData.refereed["normalized number of citations"]],
        refereedCitations: [citationData.total["total number of refereed citations"], citationData.refereed["total number of refereed citations"]],
        averageRefereedCitations: [citationData.total["average number of refereed citations"],citationData.refereed["average number of refereed citations"]],
        medianRefereedCitations: [citationData.total["median number of refereed citations"], citationData.refereed["median number of refereed citations"]],
        normalizedRefereedCitations: [citationData.total["normalized number of refereed citations"], citationData.refereed["normalized number of refereed citations"]]
      };

        data.indicesModelData = {
          hIndex: [indicesData.total["h"], indicesData.refereed["h"]],
          mIndex: [indicesData.total["m"], indicesData.refereed["m"]],
          gIndex: [indicesData.total["g"], indicesData.refereed["g"]],
          i10Index: [indicesData.total["i10"], indicesData.refereed["i10"]],
          i100Index: [indicesData.total["i100"], indicesData.refereed["i100"]],
          toriIndex: [indicesData.total["tori"], indicesData.refereed["tori"]],
          riqIndex: [indicesData.total["riq"], indicesData.refereed["riq"]],
          read10Index: [indicesData.total["read10"], indicesData.refereed["read10"]]
      }


      //keep to 2 decimal places
      _.each(data, function(table,k){
        _.each(table, function(arr, name){
          table[name] = [limitPlaces(arr[0]), limitPlaces(arr[1])];
        });
      });

      return data;
    },


    createGraphViews: function (response) {

      var hist = response.histograms;
      //papers graph
      var papersModel = new GraphModel();
      this.childViews.papersGraphView = new GraphView({model: papersModel });
      this.childViews.papersGraphView.model.set("graphData", this.dataExtractor.plot_paperhist({norm: false, paperhist_data: hist["publications"]}));
      this.childViews.papersGraphView.model.set("normalizedGraphData", this.dataExtractor.plot_paperhist({norm: true, paperhist_data: hist["publications"]}));

      //citations graph
      var citationsModel = new GraphModel();
      this.childViews.citationsGraphView = new GraphView({model: citationsModel });
      this.childViews.citationsGraphView.model.set("graphData", this.dataExtractor.plot_citshist({norm: false, citshist_data: hist["citations"]}));
      this.childViews.citationsGraphView.model.set("normalizedGraphData", this.dataExtractor.plot_citshist({norm: true, citshist_data: hist["citations"]}));

      //indices graph
      var indicesModel = new GraphModel({graphType: "line"});
      this.childViews.indicesGraphView = new GraphView({model: indicesModel });
      //this isnt in histograms array
      this.childViews.indicesGraphView.model.set("graphData", this.dataExtractor.plot_series({series_data: response["time series"]}));

      //reads graph
      var readsModel = new GraphModel();
      this.childViews.readsGraphView = new GraphView({model: readsModel });
      this.childViews.readsGraphView.model.set("graphData", this.dataExtractor.plot_readshist({norm: false, readshist_data: hist["reads"]}));
      this.childViews.readsGraphView.model.set("normalizedGraphData", this.dataExtractor.plot_readshist({norm: true, readshist_data: hist["reads"]}));
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

    /*
    * functions for 1 bibcode
    * */


    insertViewsForOnePaper: function (data) {
      //render the container view
      this.view.render({title : this.containerModel.get("title")});
      this.view.$("#indices").hide();
      this.view.$("#papers").hide();
      this.view.$(".metrics-metadata").hide();

      //attach table views
      if (this.hasReads(data)){
        this.view.readsTable.show(this.childViews.readsTableView);
        this.view.readsGraph.show(this.childViews.readsGraphView);
      }
      else {
        this.view.$(this.view.readsTable.el).html("No reads found for this article.");
      }
      if (this.hasCitations(data)){
        this.view.citationsTable.show(this.childViews.citationsTableView);
        this.view.citationsGraph.show(this.childViews.citationsGraphView);
      }
      else {
        this.view.$(this.view.citationsTable.el).html("No citations found for this article.");
      }
      //some table rows need to be hidden
      this.view.$(".hidden-abstract-page").hide();
    },

    createGraphViewsForOnePaper: function (response) {

      var hist = response.histograms;

      //citations graph
      var citationsModel = new this.GraphModel();
      this.childViews.citationsGraphView = new this.GraphView({model: citationsModel });
      this.childViews.citationsGraphView.model.set("graphData", this.dataExtractor.plot_citshist({norm: false, citshist_data: hist["citations"]}));
      this.childViews.citationsGraphView.model.set("normalizedGraphData", this.dataExtractor.plot_citshist({norm: true, citshist_data: hist["citations"]}));

      //reads graph
      var readsModel = new this.GraphModel();
      this.childViews.readsGraphView = new this.GraphView({model: readsModel });
      this.childViews.readsGraphView.model.set("graphData", this.dataExtractor.plot_readshist({norm: false, readshist_data: hist["reads"]}));
      this.childViews.readsGraphView.model.set("normalizedGraphData", this.dataExtractor.plot_readshist({norm: true, readshist_data: hist["reads"]}));
    },

    createTableDataForOnePaper : function(response){
      var data = {};
      var generalData = {refereed: response["basic stats refereed"], total: response["basic stats"]};
      var citationData = {refereed: response["citation stats refereed"], total: response["citation stats"]};

      data.readsModelData = {
        totalNumberOfReads: [generalData.total["total number of reads"], generalData.refereed["total number of reads"]],
        averageNumberOfReads: [generalData.total["average number of reads"], generalData.refereed["average number of reads"]],
        medianNumberOfReads: [generalData.total["median number of reads"], generalData.refereed["median number of reads"]],
        totalNumberOfDownloads: [generalData.total["total number of downloads"], generalData.refereed["total number of downloads"] ],
        averageNumberOfDownloads: [generalData.total["average number of downloads"], generalData.refereed["average number of downloads"]],
        medianNumberOfDownloads: [generalData.total["median number of downloads"], generalData.total["median number of downloads"]]
      };

      data.citationsModelData = {
        numberOfCitingPapers: [citationData.total["number of citing papers"], citationData.refereed["number of citing papers"]],
        totalCitations: [citationData.total["total number of citations"], citationData.refereed["total number of citations"]],
        numberOfSelfCitations: [citationData.total["number of self-citations"], citationData.refereed["number of self-citations"]],
        averageCitations: [citationData.total["average number of citations"], citationData.refereed["average number of citations"]],
        medianCitations: [citationData.total["median number of citations"],citationData.refereed["median number of citations"]],
        normalizedCitations: [citationData.total["normalized number of citations"], citationData.refereed["normalized number of citations"]],
        refereedCitations: [citationData.total["total number of refereed citations"], citationData.refereed["total number of refereed citations"]],
        averageRefereedCitations: [citationData.total["average number of refereed citations"],citationData.refereed["average number of refereed citations"]],
        medianRefereedCitations: [citationData.total["median number of refereed citations"], citationData.refereed["median number of refereed citations"]],
        normalizedRefereedCitations: [citationData.total["normalized number of refereed citations"], citationData.refereed["normalized number of refereed citations"]]
      };

      //keep to 2 decimal places
      _.each(data, function(table,k){
        _.each(table, function(arr, name){
          table[name] = [limitPlaces(arr[0]), limitPlaces(arr[1])];
        });
      });

      return data;
    },

    hasCitations : function(data){
      return data["citation stats"]["total number of citations"] > 0;
    },

    hasReads : function(data){
      return  data["basic stats"]["total number of reads"] > 0;
    },

    /*
    * end functions for 1 paper
    * */

    //so I can test these individually
    components: {
      TableModel: TableModel,
      TableView: TableView,
      GraphView: GraphView,
      GraphModel: GraphModel,
      ContainerView: ContainerView
    },

    renderWidgetForCurrentQuery : function(){
      this.reset();
      this.containerModel.set("requestRowsAllowed", true);
      //for printing
      this.containerModel.set("url", window.location.href);
      this.dispatchRequest(this.getCurrentQuery());
    },

    renderWidgetForListOfBibcodes : function(bibcodes){
      this.reset();

      var request =  new ApiRequest({
        target: ApiTargets.SERVICE_METRICS,
        query: new ApiQuery({"bibcodes" : bibcodes}),
        options : {
          type : "POST",
          contentType : "application/json"
        }
      });

      //normally this info would come from apiquery
      // let container view know how many bibcodes we have
      this.view.model.set({
        numFound : bibcodes.length,
        rows : bibcodes.length
      });

      this.containerModel.set("requestRowsAllowed", false);

      this.getPubSub().publish(this.getPubSub().EXECUTE_REQUEST, request);
    }

  });

  _.extend(MetricsWidget.prototype, Dependon.BeeHive);

  return MetricsWidget;
});
