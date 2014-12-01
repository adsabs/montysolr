define([
  'marionette',
  'nvd3',
  'js/widgets/base/base_widget',
  'hbs!./templates/metrics_container',
  'hbs!./templates/graph_template',
  'hbs!./templates/paper_table',
  'hbs!./templates/citations_table',
  'hbs!./templates/indices_table',
  'hbs!./templates/reads_table',
  'bootstrap'
], function (
  Marionette,
  nvd3,
  BaseWidget,
  MetricsContainer,
  GraphTemplate,
  PaperTableTemplate,
  CitationsTableTemplate,
  IndicesTableTemplate,
  ReadsTableTemplate
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

    className: "table table-hover",

    tagName: "table"

  });

  var GraphModel = Backbone.Model.extend({

    initialize : function(){

      //set current graph based on the graphOptions configuration
      var current = _.filter(this.get("graphOptions"), function(item){
        if (item.default === true){
          return true
        }
      })[0].value;
      this.set("currentGraph", current);

    },

    defaults : function(){
      return {
        //precompute this data to have on hand
        graphData : undefined,
        normalizedGraphData : undefined,
        //when this changes, graph view changes to normalized
        normalized : false,
        //tells graph which options to allow
        graphOptions : [
          {value : "line", name : "Line Graph"},
          {value : "stacked", name : "Stacked Graph"},
          {value : "histogram", default : true, name : "Histogram"}],
        priorGraph : undefined,
        currentGraph : undefined
      }

    }
  });


  var GraphView = Marionette.ItemView.extend({

    className : "graph-view s-graph-view",

    initialize : function(){

      this.initial = true;

      this.listenTo(this.model, "change:currentGraph", this.drawGraph);
      this.listenTo(this.model, "change:normalized", this.drawGraph);

    },

    ui : {
      svg : "svg"
    },

    events : {

      "click input[name=graph-type]" : "updateGraphType",
      "click .graph-tab" : "updateTabValue"

    },

    updateTabValue : function(e){

      var $e = $(e.currentTarget);

      this.$(".graph-tab").removeClass("active");

      $e.addClass("active");

      var val = $e.data("tab");

      if (val = "normalized"){
        this.model.set("normalized", true);
      }
      else {
        this.model.set("normalized", false)
      }
    },

    updateGraphType: function(e){

      var val = $(e.target).attr("value");
      this.model.set("priorGraph", this.model.get("currentGraph"));
      this.model.set("currentGraph", val);

    },

    drawHistogram : function(){

      var data, d3SVG;

      d3SVG = d3.select(this.ui.svg[0]);

    //get data
     data = this.model.get("normalized") ? this.model.get("normalizedGraphData") : this.model.get("graphData");

    //figure out : is this a transition or a re-draw
      if (!this.initial && this.model.get("priorGraph") == "histogram"){
        //it's a transition

      }
      else {
        // just draw the graph from scratch

        this.ui.svg.empty();

        nv.addGraph(function() {
          var chart = nv.models.multiBarChart();

          chart.xAxis
            .tickFormat(d3.format());

          chart.yAxis
            .tickFormat(d3.format());

          d3SVG
            .datum(data)
            .call(chart);

          return chart;
        });

      }

    },

    drawLineGraph : function(){

      var data, d3SVG;

      d3SVG = d3.select(this.ui.svg[0]);

      //get data
      data = this.model.get("normalized") ? this.model.get("normalizedGraphData") : this.model.get("graphData");

      //figure out : is this a transition or a re-draw
      if (!this.initial && this.model.get("priorGraph") == "line"){

      }
      else {

        this.ui.svg.empty();

        nv.addGraph(function() {
          var chart = nv.models.cumulativeLineChart()
              .x(function(d) { return d.x })
              .y(function(d) { return d.y })
              .color(d3.scale.category10().range())
              .useInteractiveGuideline(true);

          chart.xAxis
            .tickFormat();

          chart.yAxis.tickFormat();

          d3SVG.datum(data)
            .call(chart);

          return chart;
        });

      }

    },

    drawStackedGraph : function(){

      var data, d3SVG;

      d3SVG = d3.select(this.ui.svg[0]);

      //get data
      data = this.model.get("normalized") ? this.model.get("normalizedGraphData") : this.model.get("graphData");


      //figure out : is this a transition or a re-draw
      if (!this.initial && this.model.get("priorGraph") == "stacked"){

      }
      else {
        this.ui.svg.empty();


        nv.addGraph(function() {
          var chart = nv.models.stackedAreaChart()
              .x(function(d) { return d.x })
              .y(function(d) { return d.y })
              .clipEdge(true)
              .useInteractiveGuideline(true)
            ;

          chart.xAxis
            .showMaxMin(false)
            .tickFormat();

          chart.yAxis
            .tickFormat();

          d3SVG.datum(data)
            .call(chart);

          return chart;
        });

      }


    },


    //listens on change events
    drawGraph : function(){

      var current = this.model.get("currentGraph");

      //find correct function

      if (current === "histogram"){

        this.drawHistogram();

      }
      else if ( current === "line") {

        this.drawLineGraph();

      }
      else if (current === "stacked") {

        this.drawStackedGraph();

      }

      this.initial = false;

    },

    template : GraphTemplate,

    onRender: function(){

      this.drawGraph();

    }

  })


  var ContainerView = Marionette.Layout.extend({

    template: MetricsContainer,

    regions: {
      papersGraph: "#papers .metrics-graph",
      papersTable: "#papers .metrics-table",
      citationsGraph: "#citations .metrics-graph",
      citationsTable: "#citations .metrics-table",
      indicesGraph: "#indeices .metrics-graph",
      indicesTable: "#indices .metrics-table",
      readsGraph: "#reads .metrics-graph",
      readsTable: "#reads .metrics-table"

    }




  })


  var MetricsWidget = BaseWidget.extend({


    initialize: function (options) {

      this.view = new ContainerView();

    },

    resetWidget: function () {
      this.views = {};
      //empty the container view
//        this.containerView.emptyCon

    },

    processResponse: function (response) {

      this.childViews = {};

      response = response.toJSON();

      this.createTableViews(response);

      this.createGraphViews(response);

      this.insertViews();

    },

    createTableViews: function (response) {

      var generalData = {refereed: response["refereed stats"], total: response["all stats"]};
      var readsData = {refereed: response["refereed reads"], total: response["all reads"]};

      var paperModelData = {
        totalNumberOfPapers: [generalData.total["Number of papers"], generalData.refereed["Number of papers"]],
        totalNormalizedPaperCount: [generalData.total["Normalized paper count"], generalData.refereed["Normalized paper count"]]
      }

      this.childViews.papersTableView = new TableView({
        template: PaperTableTemplate,
        model: new TableModel(paperModelData)
      });

      var readsModelData = {
        totalNumberOfReads: [readsData.total["Total number of reads"], readsData.refereed["Total number of reads"]],
        averageNumberOfReads: [readsData.total["Average number of reads"], readsData.refereed["Average number of reads"]],
        medianNumberOfReads: [readsData.total["Median number of reads"], readsData.refereed["Median number of reads"]],
        totalNumberOfDownloads: [readsData.total["Total number of downloads"], readsData.refereed["Total number of downloads"] ],
        averageNumberOfDownloads: [readsData.total["Average number of downloads"], readsData.refereed["Average number of downloads"]],
        medianNumberOfDownloads: [readsData.total["Median number of reads"], readsData.total["Median number of reads"]]
      };


      this.childViews.readsTableView = new TableView({
        template: ReadsTableTemplate,
        model: new TableModel(readsModelData)
      });

      var citationsModelData = {
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

      }

      this.childViews.citationsTableView = new TableView({
        model: new TableModel(citationsModelData),
        template: CitationsTableTemplate
      });

      var indicesModelData = {
        hIndex: [generalData.total["H-index"], generalData.refereed["H-index"]],
        mIndex: [generalData.total["m-index"], generalData.refereed["m-index"]],
        gIndex: [generalData.total["g-index"], generalData.refereed["g-index"]],
        i10Index: [generalData.total["i10-index"], generalData.refereed["i10-index"]],
        i100Index: [generalData.total["i100-index"], generalData.refereed["i100-index"]],
        toriIndex: [generalData.total["tori index"], generalData.refereed["tori index"]],
        roqIndex: [generalData.total["roq index"], generalData.refereed["roq index"]],
        read10Index: [generalData.total["read10 index"], generalData.refereed["read10 index"]]

      }

      this.childViews.indicesTableView = new TableView({
        model: new TableModel(indicesModelData),
        template: IndicesTableTemplate
      });

    },

    createGraphViews : function(response){

//      this.childViews.


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
    },



//so I can test these individually
    components: {

      TableModel: TableModel,

      TableView: TableView,

      GraphView: GraphView,

      GraphModel : GraphModel,

      ContainerView: ContainerView

    }


  });


  return MetricsWidget;


})