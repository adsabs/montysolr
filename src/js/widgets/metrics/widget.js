define([
  'marionette',
  'd3',
  'js/widgets/base/base_widget',
  './extractor_functions',
  'js/components/api_response',
  'js/components/json_response',
  'js/components/api_request',
  'js/components/api_query',
  'js/mixins/dependon',
  'hbs!./templates/metrics_container',
  'hbs!./templates/graph_template',
  'hbs!./templates/paper_table',
  'hbs!./templates/citations_table',
  'hbs!./templates/indices_table',
  'hbs!./templates/reads_table',
  'bootstrap',
  'js/components/api_feedback',
  'js/components/api_targets',
  './d3-tip'
], function (
  Marionette,
  d3,
  BaseWidget,
  DataExtractor,
  ApiResponse,
  JsonResponse,
  ApiRequest,
  ApiQuery,
  Dependon,
  MetricsContainer,
  GraphTemplate,
  PaperTableTemplate,
  CitationsTableTemplate,
  IndicesTableTemplate,
  ReadsTableTemplate,
  bs,
  ApiFeedback,
  ApiTargets,
  d3Tip
  ) {

    /*
    NOTE: importing d3-tip from the metrics folder bc it is a modified file made to
    work with AMD
     */

  //used for processing data

  function limitPlaces(n){
    if (!n) return n;
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
        graphType: "histogram",
        barArrangement : 'stacked',
        yAxisLabel : 'Papers'
      }
    }
  });

  var GraphView = Marionette.ItemView.extend({

    className: "graph-view s-graph-view",
    initialize: function () {
      this.initial = true;
    },

    modelEvents : {
      'change:normalized' : 'render'
    },

    ui: {
      svg: "svg"
    },

    events: {
      "click .graph-tab": "updateTabValue",
    },

    updateTabValue: function (e) {
      var $e = $(e.currentTarget);
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

      var that = this;

      //modified from https://bl.ocks.org/mbostock/3943967

        var graphData = this.model.get("normalized") ?
        this.model.get("normalizedGraphData") :
        this.model.get("graphData");

        var n = graphData.length,
        stack = d3.layout.stack(),
        layers = stack(graphData.map(function(g){ return g.values })),
        yGroupMax = d3.max(layers, function(layer) { return d3.max(layer, function(d) { return d.y; }); }),
        yStackMax = d3.max(layers, function(layer) { return d3.max(layer, function(d) { return d.y0 + d.y; }); });

        var svg = d3.select(this.$("svg")[0]);
        var that = this;

        var margin = {top: 20, right: 10, bottom: 20, left: 50},
            width = 480 - margin.left - margin.right,
            height = 250 - margin.top - margin.bottom;

        var years = this.model.get('graphData')[0].values.map(function(o){return o.x});

        var x = d3.scale.ordinal()
            .domain(years)
            .rangeRoundBands([0, width], .08);

        var y = d3.scale.linear()
            .domain([0, yStackMax])
            .range([height, 0]);

        this.$("svg").empty();

        var graphContainerG =  svg.append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
            .classed("graph-container-g", true);

          //create the key
          if (!this.$(".graph-key").children().length){
            var keyDivs = d3.select(this.$(".graph-key")[0])
            .selectAll("div")
            .data(graphData.map(function(o){return o.key}))
              .enter()
              .append("div");

            keyDivs
              .append("div")
              .classed("key-block", true)
              .style("background-color", function(d,i){
                return that.colors[i]
              });
            keyDivs
              .append("span")
              .text(function(d){
                return d;
              });
          }

          //cache it so it can be applied to the rects later
          if (!this.tip) {

            this.tip = d3.tip()
            .attr('class', 'd3-tip')
            .offset([-10, 0])
            .html(function(d) {
              var data = that.model.get("graphData");
              return data.map(function(obj){
                var title = obj.key;
                var val = _.findWhere(obj.values, { x : d.x }).y;
                return "<b>" + title + "</b>:&nbsp;" + val;
              }).join("<br/>")
            });
          }

          graphContainerG.call(this.tip);

          graphContainerG.append("g")
                .attr("class", "x axis")
                .attr("transform", "translate(0," + height + ")");

          graphContainerG.append("g")
                .attr("class", "y axis")
                .attr("transform", "translate(0,0)");

          graphContainerG.append("text")
              .attr("text-anchor", "middle")  // this makes it easy to centre the text as the transform is applied to the anchor
              .attr("transform", "translate("+  -40 +","+(height/2)+")rotate(-90)")  // text is drawn off the screen top left, move down and out and rotate
              .classed("graph-label", true)
              .text(this.model.get("yAxisLabel"));

          this.$("input[name=bar-arrangement]").on("change", changeBarArrangement);

          var layer =  graphContainerG
               .selectAll(".layer")
               .data(layers)
               .enter().append("g")
               .attr("class", "layer")
               .style("fill", function(d, i) { return that.colors[i]; });

           var rect = layer.selectAll("rect")
               .data(function(d) { return d; })
             .enter()
             .append("rect")
             .attr("width", x.rangeBand())
             .attr("x", function(d) { return x(d.x) })
             .on('mouseover', this.tip.show)
             .on('mouseout', this.tip.hide);

        var xTickValues;

        //avoid having too many ticks
        if (years.length > 20) {
          var interval = Math.floor(years.length/5);
          xTickValues = years.filter(function(y){
            return y % interval === 0
          });

        }
        else if (years.length > 8){
          xTickValues = years.filter(function(y){
            return y % 2 === 0
          });
        }
        else {
          xTickValues = years;
        }

         var xAxis = d3.svg.axis()
             .scale(x)
             .tickSize(0)
             .tickValues(xTickValues)
             .tickPadding(6)
             .orient("bottom");

         var yTickCount =  yStackMax > 1 ? 5 : 0;

         var yAxis = d3.svg.axis()
             .scale(y)
             .tickSize(0)
             .ticks(yTickCount)
             .tickFormat(function(d){
               if (d > 1) {
                 return d3.format("s")(d)
               } else {
                 return  d3.format(".1n")(d)
               }
             })
             .orient("left");

        graphContainerG.select(".x.axis")
            .call(xAxis);

        graphContainerG.select(".y.axis")
            .transition()
            .call(yAxis);

        graphContainerG.selectAll("rect")
        .transition()
        .delay(function(d, i) { return i * 10; })
        .attr("y", function(d) { return y(d.y0 + d.y); })
        .attr("height", function(d) { return y(d.y0) - y(d.y0 + d.y); });


      /*
        stacked/grouped
       */
       function changeBarArrangement(e) {
         if (this.value === "grouped") transitionGrouped();
         else transitionStacked();
       }

       function transitionGrouped() {
         y.domain([0, yGroupMax]);

         rect.transition()
             .duration(500)
             .delay(function(d, i) { return i * 10; })
             .attr("x", function(d, i, j) { return x(d.x) + x.rangeBand() / n * j; })
             .attr("width", x.rangeBand() / n)
           .transition()
             .attr("y", function(d) { return y(d.y); })
             .attr("height", function(d) { return height - y(d.y); });
       }

       function transitionStacked() {
         y.domain([0, yStackMax]);

         rect.transition()
             .duration(500)
             .delay(function(d, i) { return i * 10; })
             .attr("y", function(d) { return y(d.y0 + d.y); })
             .attr("height", function(d) { return y(d.y0) - y(d.y0 + d.y); })
           .transition()
             .attr("x", function(d) { return x(d.x); })
             .attr("width", x.rangeBand());
           }


    },

    drawLineGraph: function () {

      var that = this;

      //dont need the radio buttons
      this.$(".stacked-form").remove();

      //cache it so it can be applied to the rects later
      var tip = d3.tip()
      .attr('class', 'd3-tip')
      .offset([-10, 0])
      .html(function(d) {
        return d.key
      });

      var graphData = this.model.get("graphData");

      if (!graphData) return;

      //parse the years
      graphData.forEach(function(d){
        d.values.forEach(function(v){
          v.x = parseInt(v.x)
        })
      });

      var years = graphData[0].values.map(function(o){return o.x});

      var svg = d3.select(this.$("svg")[0]),
        margin = {top: 20, right: 80, bottom: 30, left: 50},
        width = 480 - margin.left - margin.right,
        height = 250 - margin.top - margin.bottom;
        g = svg.append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");

      //it's only one year, show a message
      if (years.length === 1){
        g.append("text")
        .attr("x", 10)
        .attr("y", 100)
        .text("time period is less than a year, refer to table to the left for values");

        return
      }

    var x = d3.scale.linear()
        .domain(d3.extent(years))
        .range([0, width]);

    var y = d3.scale.linear()
        .range([height, 0]);

      y.domain([
        0,
        d3.max(
          _.flatten(graphData.map(function(g){return g.values.map(function(v){return v.y})}))
        )
      ]);

      var xAxis = d3.svg.axis()
          .scale(x)
          .tickSize(0)
          .ticks(5)
          .tickFormat(d3.format())
          .tickPadding(6)
          .orient("bottom");

      var yAxis = d3.svg.axis()
          .scale(y)
          .tickSize(0)
          .ticks(5)
          .tickFormat(function(d){
            if (d > 1) {
              return d3.format("s")(d)
            } else {
              return  d3.format(".1n")(d)
            }
          })
          .orient("left");

      var line = d3.svg.line()
          .x(function(d) { return x(d.x); })
          .y(function(d) { return y(d.y); });

      g.call(tip);

      g.append("g")
    		.attr("class", "x axis")
    		.attr("transform", "translate(0," + height + ")")
    		.call(xAxis);

    	g.append("g")
    		.attr("class", "y axis")
    		.call(yAxis);

      var index = g.selectAll(".index")
        .data(graphData)
        .enter().append("g")
          .attr("class", "index");

      index.append("path")
          .attr("class", "line")
          .attr("d", function(d) {
            return line(d.values); })
          .style("stroke", function(d, i) { return that.colors[i]; })
          .on('mouseover', tip.show)
          .on('mouseout', tip.hide);

        // append key
        var keyDivs = d3.select(this.$(".graph-key")[0])
        .style({position : 'absolute', right : 0 })
        .selectAll("div")
        .data(graphData.map(function(o){return o.key}))
          .enter()
          .append("div");

        keyDivs
          .append("div")
          .classed("key-block", true)
          .style("background-color", function(d,i){
            return that.colors[i]
          });
        keyDivs
          .append("span")
          .text(function(d){
            return d;
          });

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

    defaults : function(){
      return {
        widgetName : 'Metrics'
      }
    }
  });

  var ContainerView = Marionette.LayoutView.extend({

    template: MetricsContainer,

    events : {
      "click .close-widget": "signalCloseWidget",
      "click .show-all" : "signalShowAll"
    },

    signalCloseWidget: function () {
      //kill the d3 tip if it's still there
      $(".d3-tip").remove();
      this.trigger('close-widget');
    },

    signalShowAll: function () {
      this.$(".show-all").html('<i class="icon-loading"/>&nbsp;&nbsp;' + this.$(".show-all").html());
      this.trigger('show-all');
      },

      modelEvents : {
        change : 'render'
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
      'close-widget': 'closeWidget',
      'show-all' : 'showAll'
    },

    initialize: function (options) {

      options = options || {};

      this.containerModel = new ContainerModel();
      this.view = new ContainerView({model : this.containerModel});
      this.childViews = {};
      Marionette.bindEntityEvents(this, this.view, Marionette.getOption(this, "viewEvents"));

      //for widgets that extend the base MetricsWidget, so they can access this stuff
      this.dataExtractor = DataExtractor;
      this.GraphModel = GraphModel;
      this.GraphView = GraphView;

    },

    defaultQueryArguments : {
      fl : ['bibcode']
    },

    //so I can test these individually
    components: {
      TableModel: TableModel,
      TableView: TableView,
      GraphView: GraphView,
      GraphModel: GraphModel,
      ContainerView: ContainerView
    },

    activate : function(beehive){
      this.setBeeHive(beehive);
      _.bindAll(this, "setCurrentQuery", "processMetrics", "checkIfSimpleRequired");
      var pubsub = beehive.getService('PubSub');
      pubsub.subscribe(pubsub.INVITING_REQUEST, this.setCurrentQuery);
    },

    closeWidget: function () {
      this.reset();
      this.getPubSub().publish(this.getPubSub().NAVIGATE, "results-page");
    },

    //load a large query including RIQ/indices/Tori
    showAll : function(){
      this.getIndicators();
    },

    reset: function () {
      this.childViews = {};
      //empty the container view
      _.each(this.view.regions, function(v,k){
        if (this.view[k].currentView)
          this.view[k].currentView.destroy();
      }, this);

      this.containerModel.clear({silent : true});
    },

    createTableViews: function (response, num_bibcodes) {

      var tableData = num_bibcodes === 1 ? this.createTableDataForOnePaper(response)
      : this.createTableData(response);

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
      var citationsModel = new GraphModel({yAxisLabel : 'Citations'});
      this.childViews.citationsGraphView = new GraphView({model: citationsModel });
      this.childViews.citationsGraphView.model.set("graphData", this.dataExtractor.plot_citshist({norm: false, citshist_data: hist["citations"]}));
      this.childViews.citationsGraphView.model.set("normalizedGraphData", this.dataExtractor.plot_citshist({norm: true, citshist_data: hist["citations"]}));

      //reads graph
      var readsModel = new GraphModel();
      this.childViews.readsGraphView = new GraphView({model: readsModel });
      this.childViews.readsGraphView.model.set("graphData", this.dataExtractor.plot_readshist({norm: false, readshist_data: hist["reads"]}));
      this.childViews.readsGraphView.model.set("normalizedGraphData", this.dataExtractor.plot_readshist({norm: true, readshist_data: hist["reads"]}));

      //indices graph
      var indicesModel = new GraphModel({graphType: "line"});
      this.childViews.indicesGraphView = new GraphView({model: indicesModel });

      //might not be there (for 'simple' variant)
     if (response["time series"]) {
      this.childViews.indicesGraphView.model.set("graphData", this.dataExtractor.plot_series({series_data: response["time series"]}));
    } else {
      this.childViews.indicesGraphView.model.set({showingSimple : true})
    }

    },

    insertViews: function () {
      //render the container view
      this.view.render();
      //attach table and graph views
      ['papers', 'citations', 'indices', 'reads'].forEach(function(name){
          this.view[name + 'Table'].show(this.childViews[name + 'TableView']);
          this.view[name + 'Graph'].show(this.childViews[name + 'GraphView']);
        }, this);

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

    renderWidgetForCurrentQuery : function(options){
      //{ simple : true/false}
      options = options || {};

      this.reset();
      //for printing
      this.containerModel.set({
        url : window.location.href,
        loading : true
      });

      var q = this.customizeQuery(this.getCurrentQuery());

      var that = this;

      var allBibcodes = [],
          rows = 1000,
          start = 0,
          numFound = undefined,
          limit = ApiTargets._limits.Metrics.default;

      q.set("rows", rows);

      var sendRepeatingRequest = function (start){

        q.set({start : start});

        this._executeSearch(q).done(function(request){

          allBibcodes = allBibcodes.concat(
            request.get("response.docs").map(function(o){return o.bibcode})
          );

          numFound = numFound || request.get("response").numFound;
          start += rows;
          if (start >= numFound || start >= limit){
            //if more than 6000 records, automatically request the simple version
            if (numFound > limit && options.simple === undefined) options.simple = true;

            //set numFound  + bibcodes into the view Model
            that.containerModel.set({numFound : numFound > limit ? limit : numFound,
              bibcodes : allBibcodes
             });

            that.getMetrics(allBibcodes, options);
          } else {
            sendRepeatingRequest(start);
          }
        });
      }.bind(this);

      sendRepeatingRequest(start);

    },

    /**
     * requires a current apiQuery
     */
    checkIfSimpleRequired : function(){
      //bibcodes are being provided rather than query, don't show the simple version
      if (!this._currentQuery) return new $.Deferred().resolve(false);

      var query = this._currentQuery.clone();
      query.unlock();
      query.set({
         stats : 'true',
        'stats.field' : 'citation_count',
        fl: 'id'
      });

      return this._executeSearch(query).then(function(apiResponse){
         var citationCount = apiResponse.get('stats.stats_fields.citation_count.sum');
         var simple = citationCount > 50000 ? true : false;
         return simple
      });

    },

    //just get indicators
    getIndicators : function(){

      var pubsub = this.getPubSub(),
        options = {
          type: "POST",
          contentType: "application/json"
        };

      var query = new ApiQuery({
        bibcodes : this.containerModel.get("bibcodes"),
        types : ['indicators', 'timeseries']
      });

      var request = new ApiRequest({
        target: ApiTargets.SERVICE_METRICS,
        query: query,
        options: options
      });

      function onResponse(jsonResponse) {
        var response = jsonResponse.toJSON();
        
        this.childViews.indicesGraphView.model.set({
          graphData: this.dataExtractor.plot_series({series_data: response["time series"]}),
          showingSimple : false
        }
      );

        var indicesData = {refereed : response["indicators refereed"], total : response["indicators"]};

        var indicesModelData = {
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
          _.each(indicesModelData, function(arr, name){
            indicesModelData[name] = [limitPlaces(arr[0]), limitPlaces(arr[1])];
          });

        this.childViews.indicesTableView.model.set(indicesModelData);

        this.childViews.indicesTableView.render();
        this.childViews.indicesGraphView.render();
      }

      onResponse = onResponse.bind(this);

      pubsub.subscribeOnce(pubsub.DELIVERING_RESPONSE, onResponse);
      pubsub.publish(pubsub.EXECUTE_REQUEST, request);
    },

    getMetrics: function(bibcodes, options) {
      options = options || {};
     
    /*
            this function returns a promise
            at the moment the promise is only used by
            the abstract page metrics widget that shows metrics
            for 1 paper
          */

    var d = $.Deferred();

    function _getMetrics(simple) {

      var pubsub = this.getPubSub(),
        options = {
          type: "POST",
          contentType: "application/json"
        };

      var query = new ApiQuery({
        bibcodes : bibcodes,
      });

      if (simple) query.set({
        types: ['simple']
      });

      var request = new ApiRequest({
        target: ApiTargets.SERVICE_METRICS,
        query: query,
        options: options
      });

      function onResponse() {
        //the promise is used by paper metrics widget
        d.resolve();
        this.processMetrics.apply(this, arguments);
      }

      onResponse = onResponse.bind(this);

      pubsub.subscribeOnce(pubsub.DELIVERING_RESPONSE, onResponse);
      pubsub.publish(pubsub.EXECUTE_REQUEST, request);

    }

    _getMetrics = _getMetrics.bind(this);

    if (options.simple !== undefined) {
      _getMetrics(options.simple);
    } else {
      this.checkIfSimpleRequired().done(_getMetrics);
    }

    return d.promise();

  },


    processMetrics : function (response){

      this.containerModel.set("loading", false);

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

    _executeSearch: function(apiQuery){

      var pubsub = this.getPubSub();

      var req = new ApiRequest({
        target: ApiTargets.SEARCH,
        query: apiQuery
      });
      var defer = $.Deferred();

      pubsub.subscribeOnce(pubsub.DELIVERING_RESPONSE, _.bind(function(data) {
        defer.resolve(data);
      }), this);

      pubsub.publish(pubsub.EXECUTE_REQUEST, req);

      return defer;
    },

    renderWidgetForListOfBibcodes : function(bibcodes){
      this.reset();

      // let container view know how many bibcodes we have
      this.containerModel.set({
        numFound : bibcodes.length,
        loading : true
      });
      /*
        for now, library metrics always show everything (simple = false).
        If this changed in the future, the checkIfSimpleRequired function would need to
        be refactored to create a bigquery, then send the qid to execute_query
       */
      this.getMetrics(bibcodes, {simple : false});
    }

  });

  _.extend(MetricsWidget.prototype, Dependon.BeeHive);

  return MetricsWidget;
});
