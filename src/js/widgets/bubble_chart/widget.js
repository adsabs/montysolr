define([
  'd3',
  'marionette',
  'js/widgets/base/base_widget',
  'js/components/api_request',
  'hbs!./templates/container',
  'hbs!./templates/tooltip',
  'js/components/api_targets',
  "js/components/api_query_updater"
], function (
  d3,
  Marionette,
  BaseWidget,
  ApiRequest,
  ContainerTemplate,
  TooltipTemplate,
  ApiTargets,
  ApiQueryUpdater
  ) {

  var BubbleModel = Backbone.Model.extend({

    initialize: function (options) {
      this.on("change:solrData", this.modifyData);
    },

    modifyData: function () {

      var bibs, journalNames;

      if (_.isEmpty(this.get("solrData"))) {
        return
      }
      //always do this when the data is reset
      var data = [];

      _.each(this.get("solrData"), function (d, i) {
        try{
          //turn 00s into 01s (ok maybe this is not the ideal solution)
          d.pubdate = d.pubdate.replace(/(\D)00/g, function(p1,p2){return p2 +"01"});
          d.date = new Date(d.pubdate);
          //for use by citation radius
          d.year = parseInt(d.bibcode.slice(0,4));
          d.citation_count = d.citation_count ? d.citation_count : 0;
          d.read_count = d.read_count ? d.read_count : 0;
          d.title = d.title ? d.title[0] : "";
          d.pub = d.bibcode.slice(4, 9).replace(/\./g, '');
          data.push(d);
        }
        catch (e){
          console.warn(d, "didn't get added to bibcode list for some reason")
        }
      });

      //check if there are enough important journals to highlight circles
      bibs = _.chain(data).pluck("pub").countBy(function (pub) {
        return pub
      }).pairs().sortBy(function (a) {
        return a[1];
      }).value().reverse();

      var totalTop = 0;
      _.each(bibs.slice(0, 5), function (t) {
        totalTop += t[1];
      });

      if (totalTop / data.length >= .25) {
        journalNames  =_.map(bibs, function (b) {
          return b[0]
        }).slice(0, 5);
        //show "other" option as long as there are other journals
        if (totalTop / data.length < 1)
        journalNames.push("other");
        this.set("journalNames", journalNames);
      }
      else {
        this.set("journalNames", []);
      }
      //now set the data, initiates the graph drawing
      this.set("modifiedSolrData", data);
    },

    reset : function(){
      this.set(this.defaults(), {silent : true});
    },

    toggleTracked : function(bibcode){

      var tracked = this.get("trackingBibs");

      if (_.contains(tracked, bibcode)){
        this.set("trackingBibs", _.without(tracked, bibcode));
      }
      else {
        tracked.push(bibcode);
        this.set("trackingBibs", tracked);
      }
    },

    defaults: function () {
      return {
        //straight from solr
        solrData: {},
        //processed solrData, only happens 1x per new solrData
        modifiedSolrData: [],
        //for each graph, changes when user changes x and y vals
        currentGraphData: {},
        //a change in scale will call "rearrangeGraph"
        yScale: "log",
        xScale : "linear",
        yLogPossible: true,
        xLogPossible: false,
        //changes in these values won't call rearrangeGraph, you
        //have to do it manually
        xValue: "date",
        yValue: "read_count",
        radius: "citation_count",
        // to show as a key to the right
        journalNames: [],
        //just allows the user to select a bibcode, change its appearance
        //and track it through different graphs
        trackingBibs : [],
        //to filter on
        selectedBibs : [],
        timeRange : "year",
        currentPub : undefined

      }
    }
  });

  var BubbleView = Marionette.ItemView.extend({

    className: "bubble-chart s-bubble-chart",

    template: ContainerTemplate,

    reset : function(){
      this.scales = {};
      this.cache = {};
    },

    modelEvents: {
      "change:modifiedSolrData": "render",
      "change:currentPub": "rearrangeGraph",
      "change:yScale" : "rearrangeGraph",
      "change:xScale" : "rearrangeGraph",
      "change:selectedBibs": "manageSubmitButton"
    },

    events: {
      //here we toggle between the three graph types
      "click .graph-select": function (e) {
        var $target = $(e.currentTarget);

        this.$(".graph-select").removeClass("active");

        if ($target.hasClass("reads-vs-time")) {

          this.model.set({
            radius: "citation_count",
            xScale: "linear",
            xValue: "date",
            yValue: "read_count",
            yScale: "log"
          }, {silent : true});
        }
        else if ($target.hasClass("citations-vs-time")) {

          this.model.set({
            radius: "read_count",
            xValue: "date",
            xScale: "linear",
            yValue: "citation_count",
            yScale: "log"
          }, {silent : true});
        }
        else if ($target.hasClass("reads-vs-citations")) {

          this.model.set({
            radius: "year",
            yValue: "read_count",
            xValue: "citation_count",
            xScale: "log"
          }, {silent : true});

        }
        this.rearrangeGraph();
        $target.addClass("active");
      },
      "click .submit" : function(){
        this.trigger("filterBibs", this.model.get("selectedBibs"));
      },

      "click .close" : function(){
          this.trigger("close");
        }
    },

    getConfig: function () {

      this.config = {}
      this.config.margin =  {
          left: 80,
          bottom: 40,
          right: 20,
          top: 60
        },

      this.config.height = 500 - (this.config.margin.top + this.config.margin.bottom);
      this.config.width = 1000 - (this.config.margin.left + this.config.margin.right);
      this.config.animationLength = Marionette.getOption(this, "testing") ? 0 : 500;
    },

    cacheVals: function () {
      this.cache = {};
      this.cache.svg = d3.select(this.$("svg.bubble-chart-svg")[0])
        .append("g")
        .attr("transform", "translate("+this.config.margin.left+ "," + this.config.margin.top + ")");
      this.cache.realSvg = d3.select(this.$("svg.bubble-chart-svg")[0]);
      this.cache.tooltip = d3.select(this.$(".d3-tooltip")[0]);
    },

    //this function is run every time the graph is rendered or changes
    setScales: function () {

      this.scales = {};

      var data = this.model.get("currentGraphData"),
        that = this,
        dateRange,
        yExtent, xExtent,
        newDateRange = [];

      //journal scale
      if (this.model.get("journalNames").length) {

        this.scales.journalScale = d3.scale.ordinal()
          .domain(this.model.get("journalNames"))
          .range(["hsla(282, 80%, 52%, 1)", "hsla(1, 80%, 51%, 1)", "hsla(152, 80%, 40%, 1)", "hsla(193, 80%, 48%, 1)", "hsla(220, 80%, 56%, 1)", "hsla(0, 0%, 20%, 1)"]);
      }

      //x scale
      if (that.model.get("xValue") === "date"){

        dateRange = d3.extent(data, function (d) {
          return d.date
        });

        //adding some padding to the date ranges so they are more attractive
        //there is probably an easier way to do this but how???

        if (dateRange[1].getFullYear() - dateRange[0].getFullYear() > 2){
          this.model.set("timeRange", "year");
          var newDate = new Date(dateRange[0]);
          newDate.setMonth(newDate.getMonth() - 12);
          newDateRange.push(newDate);
          newDate = new Date(dateRange[1]);
          newDate.setMonth(newDate.getMonth() + 12);
          newDateRange.push(newDate);
        }
        else {
          this.model.set("timeRange", "month");
          var newDate = new Date(dateRange[0]);
          newDate.setMonth(newDate.getMonth() - 1);
          newDateRange.push(newDate);
          newDate = new Date(dateRange[1]);
          newDate.setMonth(newDate.getMonth() + 1);
          newDateRange.push(newDate);
        }

        this.scales.xScale = d3.time.scale()
          .domain(newDateRange)
          .range([0, that.config.width]);
      }

      else if (that.model.get("xValue") == "citation_count"){

        xExtent = d3.extent(data, function (d) {
          return d[that.model.get("xValue")]
        });

        if (that.model.get("xScale") === "log"){

          this.scales.xScale = d3.scale.log()
            .domain(xExtent)
            .range([0, that.config.width]);
        }
        else {

          this.scales.xScale = d3.scale.linear()
            .domain(xExtent)
            .range([0, that.config.width]);
        }
      }

      yExtent = d3.extent(data, function (d) {
        return d[that.model.get("yValue")]
      });

      //y scale
      if (this.model.get("yScale") === "log") {

        this.scales.yScale = d3.scale.log()
          .domain(yExtent)
          .range([that.config.height, 0]);
      }
      else {

        this.scales.yScale = d3.scale.linear()
          .domain(yExtent)
          .range([that.config.height, 0]);
      }

      //radius scale
      if (that.model.get("radius") == "year"){
        this.scales.radiusScale = d3.scale.linear()
          .domain(d3.extent(data, function (d) {
            return d[that.model.get("radius")]
          }))
          .range([2, 14]);
      }
      else {
        this.scales.radiusScale = d3.scale.linear()
          .domain(d3.extent(data, function (d) {
            return d[that.model.get("radius")]
          }))
          .range([4, 26]);
      }
    },

    finalProcess: function () {

      var that = this,
        data = this.model.get("modifiedSolrData"),
        yExtent, xExtent;

      //do last minute scale adjustments if necessary

      yExtent = d3.sum(_.pluck(data, that.model.get("yValue")));
      xExtent = d3.sum(_.pluck(data, that.model.get("xValue")));

      if (!yExtent){
        this.model.set("yScale", "linear", {silent : true});
        this.model.set("yLogPossible", false);
      }
      else {
        this.model.set("yLogPossible", true);
      }
      if (!xExtent){
        this.model.set("xScale", "linear", {silent : true});
        this.model.set("xLogPossible", false);
      }
      else {
        this.model.set("xLogPossible", true);
      }

      //make sure that any log scales only get data > 0
      data = _.chain(data).filter(function (d) {
        if (that.model.get("yScale") === "linear") {
          return true
        }
        //if it's a log scale, make sure all y vals are at least 1
        else if (d[that.model.get("yValue")]) {
          return true
        }
      }).filter(function (d) {
          if (that.model.get("xScale") === "linear") {
            return true
          }
          //if it's a log scale, make sure all y vals are at least 1
          else if (d[that.model.get("xValue")]) {
            return true
          }
        })
        //sorting is important for mouseover interactions,
        //you don't want the circles hidden behind larger ones
        .sortBy(function (d) {
        return -d[that.model.get("radius")]
      }).value();

      this.model.set("currentGraphData", data);

      this.setScales();

    },

    resetSelection : function(){
      this.model.set("selectedBibs", []);
      this.cache.svg.selectAll(".paper-circle.selected").classed("selected", false);
      this.cache.svg.select("rect.selection").remove();
    },

    rearrangeGraph: function () {


      this.finalProcess();
      this.resetSelection();

      var svg = this.cache.svg,
        realSvg = this.cache.realSvg,
        data,
        yAxis,
        circles,
        journalNames = this.model.get("journalNames"),
        pubName,
        xTickFormat,
        that = this;

      realSvg.select("rect.selection").remove();

      data = this.model.get("currentGraphData");

      //finally, limiting to only certain publication if necessary
      pubName = that.model.get("currentPub");

      if (pubName) {
        data = _.filter(data, function (d) {
          //we want to show all gray circles
          if ( pubName === "other"){
            return !_.contains(that.model.get("journalNames"), d.pub);
          }
          else {
            return (d.pub === pubName);
          }
        })
      }

      yAxis = d3.svg.axis().scale(this.scales.yScale).orient("left").ticks(10).tickFormat(d3.format("s"));

      // X Axis
      if (this.model.get("xValue") === "date" && this.model.get("timeRange") == "year"){
        xTickFormat = d3.time.format("%Y");
      }
      else if (this.model.get("xValue") === "date" && this.model.get("timeRange") == "month"){
        //the time range is 2 years or less
       xTickFormat = d3.time.format("%b-%Y");
      }
      else {
        //it's not a time scale
        xTickFormat = d3.format("");
      }
      xAxis = d3.svg.axis().scale(this.scales.xScale).orient("bottom").tickFormat(xTickFormat);

      svg.select(".y-axis")
        .transition()
        .duration(that.config.animationLength)
        .call(yAxis);

      svg.select(".x-axis")
        .transition()
        .duration(that.config.animationLength)
        .call(xAxis);

      this.renderLabels(realSvg.select(".y-label"), realSvg.select(".x-label"));

      circles = svg.selectAll(".paper-circle")
        .data(data, function (d) {
          return d.bibcode
        });

      //exit selection
      circles.exit()
        .remove();

    //update selection
      circles
        .transition()
        .duration(that.config.animationLength)
        .attr("r", function (d) {
          return that.scales.radiusScale(d[that.model.get("radius")]) + "px";
        })
        .attr("cy", function (d) {
          return that.scales.yScale(d[that.model.get("yValue")]);
        })
        .attr("cx", function (d) {
          return that.scales.xScale(d[that.model.get("xValue")]);
        });

      //enter selection
      circles
        .enter()
        .append("circle")
        .classed("paper-circle", true)
        .classed("tracked", function(d){
          // should the bubble get a nice tracking outline?
          if ( _.contains(that.model.get("trackingBibs"), d.bibcode)){
            return true
          }
          else {
            return false
          }
        })
        .attr("cx", function (d) {
          return that.scales.xScale(d[that.model.get("xValue")]);
        })
        .style("fill", function (d) {
          if (journalNames && _.contains(journalNames, d.pub)) {
            return that.scales.journalScale(d.pub);
          }
        })
        .attr("cy", function (d) {
          return that.scales.yScale(d[that.model.get("yValue")]);
        })
        .attr("r", function (d) {
          return that.scales.radiusScale(d[that.model.get("radius")]) + "px";
        });

      //sort
      circles.order();

    },

    onRender : function(){
      if (!_.isEmpty(this.model.get("modifiedSolrData"))){
        this.renderGraph();
      }
      else {
        this.$el.html("Not enough data to form the citations/reads graphs");
      }
    },

    //where yLabel and xLabel are d3 selections
    renderLabels : function(yLabel, xLabel){

      var that = this;

      //a function to attach log/linear options
      function appendLogOption(labelGroup, scaleName){
        labelGroup.append("circle")
          .classed("scale-choice", true)
          .classed("log", true)
          .classed("selected", function () {
            if (that.model.get(scaleName) === "log") {
              return true
            }
          })
          .attr("r", "6px")
          .attr("cx", 200)
          .attr("cy", -10);

        labelGroup.append("text")
          .attr("x", 210)
          .attr("y", -5)
          .text("log")
          .classed("log", true);

        labelGroup.append("circle")
          .classed("scale-choice", true)
          .classed("linear", true)
          .classed("selected", function () {
            if (that.model.get(scaleName) === "linear") {
              return true
            }
          })
          .attr("r", "6px")
          .attr("cx", 250)
          .attr("cy", -10);

        labelGroup
          .append("text")
          .attr("x", 260)
          .attr("y", -5)
          .text("linear");

      }

      yLabel.selectAll("*").remove();

      yLabel.append("text")
        .text(function () {
          if (that.model.get("yValue") === "citation_count") {
            return "Citation Count";
          }
          else if (that.model.get("yValue") === "read_count") {
            return "90 Day Read Count";
          }
        })
        .classed("axis-title", true);

      yLabel.call(appendLogOption, "yScale");

      xLabel.selectAll("*").remove();
      xLabel.append("text")
        .text(function () {
          if (that.model.get("xValue") === "citation_count") {
            return "Citation Count";
          }
          else if (that.model.get("xValue") === "date") {
            return "Date";
          }
        })
        .classed("axis-title", true);

      //no log option if x scale is date
      if (that.model.get("xValue")!== "date"){
        xLabel.call(appendLogOption, "xScale");
      }

      this.attachLogLinearEventListeners();

      //finally, hide log options if they are  impossible
      if (!this.model.get("yLogPossible")){
        yLabel.selectAll(".scale-choice").classed("hidden", true);
        yLabel.selectAll("text.log").classed("hidden", true);
      }

      if (!this.model.get("xLogPossible")){
        xLabel.selectAll(".scale-choice").classed("hidden", true);
        xLabel.selectAll("text.log").classed("hidden", true);
      }

    },

    //so that the fake radio buttons work
    attachLogLinearEventListeners : function(){
      var realSvg = this.cache.realSvg,
          that = this;

      var config = [{ container: ".y-label", scale: "yScale"},{container: ".x-label", scale: "xScale"}];

      _.each(config, function(c){

        realSvg.selectAll(c.container + " .scale-choice").on("click", function (e) {

          d3.selectAll(c.container + " .scale-choice").classed("selected", false);
          var d3this = d3.select(this);

          if (d3this.classed("linear")) {
            that.model.set(c.scale, "linear");
            d3this.classed("selected", true);
          }
          else {
            that.model.set(c.scale, "log");
            d3this.classed("selected", true);
          }
        }, this);
      });
    },

    renderGraph: function () {

      this.getConfig();
      this.cacheVals();
      this.finalProcess();

      var svg = this.cache.svg,
        realSvg = this.cache.realSvg,
        data,
        that = this,
        journalNames = that.model.get("journalNames"),
        isMousePressed = false,
        xLabel, xAxis, yLabel, yAxis;

      data = this.model.get("currentGraphData");

      //attaching axes
      if (this.model.get("timeRange") == "year"){
        xAxis = d3.svg.axis().scale(this.scales.xScale).orient("bottom").tickFormat(d3.time.format("%Y"))
      }
      else {
        //the time range is 2 years or less
        xAxis = d3.svg.axis().scale(this.scales.xScale).orient("bottom").tickFormat(d3.time.format("%b-%Y"))

      }
      yAxis = d3.svg.axis().scale(this.scales.yScale).orient("left").tickFormat(d3.format("s"));

      svg.append("g")
        .attr("class", "x-axis")
        .attr("transform", "translate(0," + that.config.height + ")")
        .call(xAxis);

      svg.append("g")
        .attr("class", "y-axis")
        .call(yAxis);

      //This is the part where we attach labels and their options

      yLabel = realSvg.append("g")
        .attr("transform", "translate(35, 400) rotate(-90)")
        .classed("y-label", true);

      xLabel = realSvg.append("g")
        .attr("transform", "translate("+that.config.width/2 + "," + (function(){return that.config.height + that.config.margin.top + that.config.margin.bottom + 10 }())  +")")
        .classed("x-label", true);

      //this function adds the labels and also attaches the event listeners
      this.renderLabels(yLabel, xLabel);

     svg.selectAll(".paper-circle")
        .data(data)
        .enter()
        .append("circle")
        .classed("paper-circle", true)
        .attr("r", function (d) {
          return that.scales.radiusScale(d.citation_count) + "px";
        })
        .attr("cx", function (d) {
          return that.scales.xScale(d.date);
        })
        .attr("cy", function (d) {
          return that.scales.yScale(d.read_count);
        })
        .style("fill", function (d) {
          if (journalNames && _.contains(journalNames, d.pub)) {
            return that.scales.journalScale(d.pub);
          }
        });

      //journal name key
      var key = realSvg.append("g")
        .classed("journal-name-key", true)
        .attr("transform", "translate(" +(function(){return that.config.width + that.config.margin.right + that.config.margin.left}()) + "," +
          (function(){return that.config.height/2 - that.config.margin.top}()) + ")");

      key
        .selectAll("rect")
        .data(journalNames)
        .enter()
        .append("rect")
        .attr("width", 13)
        .attr("height", 13)
        .attr("y", function (d, i) {
          return i * 22;
        })
        .attr("fill", function (d) {
          if (d === "other"){
            return "hsla(0, 0%, 20%, 1)";
          } else {
            return that.scales.journalScale(d);
          }
        })
      key.selectAll("text")
        .data(journalNames)
        .enter()
        .append("text")
        .attr("x", 15)
        .attr("y", function (d, i) {
          return i * 22+ 10;
        })
        .text(function (d) {
          return d
        })
       .on("click",  isolateCirclesByPub)

     function isolateCirclesByPub (pubName){

       var d3this = d3.select(this);

       if (d3this.classed("journal-selected") == true){
         d3this.classed("journal-selected", false);
         that.model.set("currentPub", undefined);
       }
       else {
         realSvg.selectAll(".journal-selected").classed("journal-selected", false);
         d3this.classed("journal-selected", true);
         that.model.set("currentPub", pubName);
       }
     };

      //adding the "trackingBibs" bubble select functionality
      //a delegated event

      realSvg.on("click.tracking", function(d,i){
          if (!d3.select(d3.event.target).classed("paper-circle")){
            return
          }

       var bibcode = d3.event.target.__data__.bibcode;

       that.model.toggleTracked(bibcode);

        if (_.contains(that.model.get("trackingBibs"), bibcode)){
          d3.select(d3.event.target).classed("tracked", true);
        }
        else {
          d3.select(d3.event.target).classed("tracked", false);

        }

      });
      //adding delegated tooltip events
      realSvg.on("mouseover.tooltip", function (e) {

        if (d3.select(d3.event.target).classed("paper-circle") && !isMousePressed) {

          var yVal = that.model.get("yValue"),
            xVal = that.model.get("xValue"),
            radius = that.model.get("radius"),
            xOffset = d3.mouse(this.parentElement)[0],
            yOffset = d3.mouse(this.parentElement)[1],
            toReturn = {},
            allData, height, outerD = d3.event.target.__data__,
            xPadding, xPosition;

          xPosition = d3.mouse(this.parentElement)[0]-that.config.margin.left > 500 ? "right" : "left";

          allData = d3.selectAll(".paper-circle").filter(function (d) {
            if (d[yVal] == outerD[yVal] && d[xVal] == outerD[xVal]) {
              return true
            }
          }).sort(function (a, b) {
            return b[radius] - a[radius]
          }).data();

          //show only first 3
          if (allData.length > 3){
            toReturn.extra = allData.length -3;
            allData = allData.slice(0,3);
          }

          toReturn.bibs = allData;

          that.cache.tooltip
            .html(TooltipTemplate(toReturn));

          that.cache.tooltip.style({visibility: "hidden", display: "block"});
          height = d3.select(".d3-tooltip")[0][0].clientHeight;

          xPadding= d3.max([parseInt(d3.select(d3.event.target).attr("r"))*2.5, 10]);

          if (xPosition === "left"){

            that.cache.tooltip
              .style("left", xOffset + xPadding + "px")
              .style("top", yOffset - height/2 + "px")
              .style("visibility", "visible");

          }
          else {
            //tooltip is 240px, so shift it that far to the left
            that.cache.tooltip
              .style("left", xOffset - xPadding  - 240 + "px")
              .style("top", yOffset - height/2 + "px")
              .style("visibility", "visible");
          }
        }
      });

      realSvg.on("mousemove.tooltip", function (e) {
        if (!d3.event.target.classList.contains("paper-circle")) {
          that.cache.tooltip
            .style({display: "none"});
        }
      });

      //taking care of selection box
      realSvg.on("mousedown.select", function() {

        isMousePressed = true;

        svg.selectAll('.paper-circle.selected').classed( "selected", false);
        realSvg.select("rect.selection").remove();

          var p = d3.mouse(this);
          realSvg.append( "rect")
            .attr({
              rx      : 6,
              ry      : 6,
              class   : "selection",
              x       : p[0],
              y       : p[1],
              width   : 0,
              height  : 0
            })
        })
        .on( "mousemove.select", function() {
          if (!isMousePressed){
            return
          }
          var s = realSvg.select( "rect.selection");

          if( !s.empty()) {
            var p = d3.mouse(this),
              d = {
                x       : parseInt( s.attr( "x"), 10),
                y       : parseInt( s.attr( "y"), 10),
                width   : parseInt( s.attr( "width"), 10),
                height  : parseInt( s.attr( "height"), 10)
              },
              move = {
                x : p[0] - d.x,
                y : p[1] - d.y
              }

            d.width = move.x;
            d.height = move.y

            if (move.x < 0 || move.y < 0 ){
              //it's going backwards, ignore it
              return
            }

            //code that allows you to reverse direction of the rect,
            //commented for now since it doesn't work perfectly

//            if( move.x < 1 || (move.x*2<d.width)) {
//              d.x = p[0];
//              d.width -= move.x;
//            } else {
//              d.width = move.x;
//            }
//
//            if( move.y < 1 || (move.y*2<d.height)) {
//              d.y = p[1];
//              d.height -= move.y;
//            } else {
//              d.height = move.y;
//            }

            s.attr(d);

            realSvg.selectAll('.paper-circle').each( function(paperD, i) {
              var d3this = d3.select(this),
                  cx = parseInt(d3this.attr("cx")),
                  cy = parseInt(d3this.attr("cy"));

              if( cx + that.config.margin.left >= d.x && cx + that.config.margin.left <= d.x+d.width
                && cy + that.config.margin.top >= d.y && cy + that.config.margin.top <= d.y+d.height ) {
                d3this.classed( "selected", true);
              }
              else {
                d3this.classed( "selected", false);
              }
            });
          }
        })
        .on("mouseup.select", function() {
          isMousePressed = false;
          var bibs = [];
          svg.selectAll(".paper-circle.selected").each(function(d){
            if (this.style.display !== "none"){
              bibs.push(d.bibcode);
            }
          })
          that.model.set("selectedBibs", bibs);
        });
    },

    manageSubmitButton : function(){

      if (this.model.get("selectedBibs").length === 0){
        this.$(".submit").addClass("disabled");
      }
      else {
        this.$(".submit").removeClass("disabled");
      }
    }

  });

  var BubbleChart = BaseWidget.extend({

    initialize: function (options) {
      options = options || {};
      this.model = new BubbleModel();
      //testing reduces animations to 0
      this.view = new BubbleView({model: this.model, testing: options.testing});
      this.listenTo(this.view, "filterBibs", this.onFilterBibs);
      this.listenTo(this.view, "destroy", this.broadcastClose);
      this.widgetName = 'bubble_chart';
      this.queryUpdater = new ApiQueryUpdater(this.widgetName);
    },

    activate: function (beehive) {
      this.setBeeHive(beehive);
      _.bindAll(this, "setCurrentQuery", "processResponse");
      var pubsub = this.getPubSub();
      //custom dispatchRequest function goes here
      pubsub.subscribe(pubsub.INVITING_REQUEST, this.setCurrentQuery);
      //custom handleResponse function goes here
      pubsub.subscribe(pubsub.DELIVERING_RESPONSE, this.processResponse);
    },

    //fetch data
    onShow: function () {

      var query = this.getCurrentQuery().clone();
      query.unlock();
      query.set("rows", 1000);
      query.set("fl", "title,bibcode,citation_count,read_count,pubdate");
      query.unset("hl");
      query.unset("hl.fl");

      var request = new ApiRequest({
        target : ApiTargets.SEARCH,
        query: query
      });

      this.getPubSub().publish(this.getPubSub().DELIVERING_REQUEST, request);
    },


    processResponse: function (apiResponse) {
      this.model.reset();
      this.view.reset();
      this.model.set("solrData", apiResponse.get("response.docs"));

    },

    onFilterBibs: function(){
      var bibs = this.model.get("selectedBibs"),
        fqString = "",
        newQuery = this.getCurrentQuery().clone();

      if (!bibs.length){
        return
      }

      var qu = this.queryUpdater;
      var newQ = 'bibcode:(' + bibs.map(function(x) {return qu.quoteIfNecessary(x)}).join(" OR ") + ')';

      newQuery.unlock();
      this._updateFq(newQuery, newQ);
      this.getPubSub().publish(this.getPubSub().START_SEARCH, newQuery);
    },

    _updateFq: function(q, value) {

      var filterName = 'fq_' + this.widgetName;

      // uncomment if we need adding to the existing conditions
      q.unset(filterName);
      this.queryUpdater.updateQuery(q, filterName, 'limit', value);

      var fq = '{!type=aqp v=$' + filterName + '}';
      var fqs = q.get('fq');
      if (!fqs) {
        q.set('fq', [fq]);
      }
      else {
        var i = _.indexOf(fqs, fq);
        if (i == -1) {
          fqs.push(fq);
        }
        q.set('fq', fqs);
      }
    },


    broadcastClose: function () {
      this.getPubSub().publish(this.getPubSub().NAVIGATE, "results-page");
    }

  });

  return BubbleChart

});