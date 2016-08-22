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
  'hbs!../network_vis/templates/loading-template',
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
  loadingTemplate,
  d3Tip
  ) {

    /*
    NOTE: importing d3-tip from the metrics folder bc it is a modified file made to
    work AMD
     */


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

          var color = d3.scale.ordinal()
              .domain([0, n - 1])
              .range(this.colors);

          var layer =  graphContainerG
               .selectAll(".layer")
               .data(layers)
               .enter().append("g")
               .attr("class", "layer")
               .style("fill", function(d, i) { return color(i); });

           var rect = layer.selectAll("rect")
               .data(function(d) { return d; })
             .enter()
             .append("rect")
             .attr("width", x.rangeBand())
             .attr("x", function(d) { return x(d.x) })
             .on('mouseover', this.tip.show)
             .on('mouseout', this.tip.hide);


        var xTickValues = years.length > 5 ? years.filter(function(y, i){
          if (i % 3 === 0) return true
        }) : years;

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

      //parse the years
      graphData.forEach(function(d){
        d.values.forEach(function(v){
          v.x = parseInt(v.x)
        })
      })

      var years = graphData[0].values.map(function(o){return o.x});

      var svg = d3.select(this.$("svg")[0]),
        margin = {top: 20, right: 80, bottom: 30, left: 50},
        width = 480 - margin.left - margin.right,
        height = 250 - margin.top - margin.bottom;
        g = svg.append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");

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
      var citationsModel = new GraphModel({yAxisLabel : 'Citations'});
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
