define([
  'js/widgets/network_vis/network_widget',
  'js/components/api_query_updater',
  'bootstrap'
],
  function(
  NetworkWidget,
  ApiQueryUpdater
  ) {

  var options = {};

  options.endpoint = "services/vis/paper-network";

  options.networkType = "paper";

  options.helpText = "<p>The paper network groups papers from your search results based on how many" +
    " references they have in common. Papers with many references in common are more likely to discuss" +
    " similar topics.</p><p>If your search results returned a large enough set of papers, you will see two views:" +
    " a summary view, which shows groups of tightly linked papers, and a detail view " +
    " which shows you the individual papers from a group and how they are connected. </p><p>The size of the circles in the summary node graph" +
    " are based on the cumulative number of citations shared by the group, and the titles of the summary nodes are small" +
    " word clouds based on the words from the titles of the papers in the group.</p>";

  //these defaults won't typically change
  options.summaryMixin = {};

  options.summaryMixin.computeScales = function () {

    var currentData, scalesDict, groupWeights, linkValues;

    currentData = this.model.get("summaryData");

    scalesDict = this.model.get("scales");

    groupWeights = _.pluck(currentData.nodes, "size");

    linkValues = _.pluck(currentData.links, "weight");

    scalesDict.lineScale = d3.scale.linear().domain([d3.min(linkValues), d3.max(linkValues)]).range([1, 12]);
    scalesDict.linkScale = d3.scale.linear().domain([d3.min(linkValues), d3.max(linkValues)]).range([.1, .3]);
    scalesDict.radiusScale = d3.scale.linear().domain([d3.min(groupWeights), d3.max(groupWeights)]).range([8, 18]);

  };

  options.summaryMixin.initializeGraph = function () {

    var force, linkDistance, charge;

    var svg, g1, g2, link, node;

    var width, height;

    var scalesDict, lineScale, nodeScale, radiusScale, linkScale;

    var linksData, nodesData;

    var nodeColor, z;

    linkDistance = this.styleModel.get("linkDistance");

    charge = this.styleModel.get("charge");

    var gravity = this.styleModel.get("gravity");

    linksData = this.model.get("summaryData").links;

    nodesData = this.model.get("summaryData").nodes;

    svg = d3.select(this.$("svg")[0]);

    scalesDict = this.model.get("scales");

    lineScale = scalesDict["lineScale"];

    nodeScale = scalesDict["nodeScale"];

    linkScale = scalesDict["linkScale"];

    radiusScale = scalesDict["radiusScale"];

    nodeColor = this.styleModel.get("nodeColor");

    width = this.styleModel.get("width");

    height = this.styleModel.get("height");

    force = d3.layout
      .force()
      .size([width, height])
      .distance(function () {
       return 50
      })
      .charge(charge);

    force
      .nodes(nodesData)
      .links(linksData)
      .start();

    this.model.set("force", force);

    //need two gs because of weird panning requirement
    g1 = svg.append("g");

    this.model.set("g1", g1);

    g2 = g1.append("g");

    g2.append("rect")
      .attr("width", width)
      .attr("height", height)
      .style("fill", "none")
      .style("pointer-events", "all");


    //improving mouseover interaction (svg doesn't use z index)
    link = g2.selectAll(".network-link")
      .data(linksData)
      .enter()
      .append("line")
      .classed({"network-link": true, "connector-link": function (d) {
        if (d.source.connector || d.target.connector) {
          return true
        }
      }})
      .style({"stroke-width": function (d) {
        if (d.source.connector || d.target.connector) {
          return null
        } else {
          return lineScale(d.weight)

        }
      }

      });

    node = g2.selectAll(".summary-node-group")
      .data(nodesData)
      .enter()
      .append("g")
      .classed("summary-node-group", true)
      .call(force.drag);

    node.append("circle")
      .classed({"network-node": true })
      .attr("r", function (d) {
        return d.size ? radiusScale(d.size) : 10
      })
      .each(function (d) {

        var d3Group = d3.select(this.parentElement);

        wordValues = [];
        _.each(d.nodeName, function(v){
          wordValues.push(v);
        });

        var size = d.size;

        fontScale = d3.scale.linear().domain([d3.min(wordValues), d3.max(wordValues)]).range([radiusScale(d.size)/5, radiusScale(d.size)/4]);

        var i = 0;


        _.each(d.nodeName, function (v,k) {

          i++;

          d3Group.append("text")
            .text(k)
            .attr("font-size", fontScale(v))
            .attr("text-anchor", "middle")
            .classed("group-paper-name", true)
            .attr("y", function () {
             var p = i % 2 == 1? -i : i;
              if (i == 1){
                return -1
              }
              else {
                return p * radiusScale(size)/7.3

              }
            })

        })

      });

    force.on("tick", function () {

      node.attr("x", function(d) {

        var r = d3.select(this.childNodes[0]).attr("r");
        return d.x = Math.max(r, Math.min(width - r, d.x));
      })
        .attr("y", function(d) {
          var r = d3.select(this.childNodes[0]).attr("r");
          return d.y = Math.max(r, Math.min(height - r, d.y));
        });

      node.attr("transform", function (d) {

        return "translate(" + d.x + "," + d.y + ")"
      });

      link.attr("x1", function (d) {
        return d.source.x;
      })
        .attr("y1", function (d) {
          return d.source.y;
        })
        .attr("x2", function (d) {
          return d.target.x;
        })
        .attr("y2", function (d) {
          return d.target.y;
        });

    });

    z = d3.behavior.zoom().scaleExtent([1, 1]);

    g1.call(z)

  };

  options.detailMixin = {};

  options.detailMixin.drawGraph = function () {

    var svg, width, height, g1, g2, z;

    var scalesDict;

    var node, link;

    var numTicks;

    var self = this;

    width = this.styleModel.get("width");
    height = this.styleModel.get("height");

    scalesDict = this.model.get("scales");

    svg = d3.select(this.$("svg")[0]);

    this.model.set("svg", svg);

    var force = d3.layout.force()
      .size([width, height])
      .linkDistance(this.styleModel.get("linkDistance"));

    svg
      .attr("width", width)
      .attr("height", height);

    this.model.set("svg", svg);

    //container for network
    //need two gs because of weird panning requirement
    g1 = svg.append("g");

    g2 = g1.append("g");

    g2.append("rect")
      .attr("width", width)
      .attr("height", height)
      .style("fill", "none")
      .style("pointer-events", "all");

    this.model.set("g2", g2);

    force
      .nodes(this.model.get("nodes"))
      .links(this.model.get("links"))
      .start();

    this.model.set("force", force);

    link = g2.selectAll(".detail-link")
      .data(this.model.get("links"))
      .enter().append("line")
      .attr("class", "detail-link")
      .style("stroke-width", function (d) {
        return scalesDict.lineScale(d.weight);
      });

    node = g2.selectAll(".detail-node")
      .data(this.model.get("nodes"))
      .enter()
      .append("text")
      .text(function (d) {
        return d.nodeName
      })
      //adding popovers
      .attr("data-toggle", "popover")
      .attr("title", function(d){return "<b>"+d.nodeName +"</b>"})
      .attr("data-content", function(d){
        return "<b>Title: </b>"+ d.title+"<br/><b>First Author: </b>" + d.first_author+"<br/><b>Citation Count: </b>"+ d.citation_count
      })
      .attr("font-size", function (d) {
        return scalesDict.fontScale(d.nodeWeight) + "px"
      })
      .classed({"detail-node": true, "selected-node": function (d) {
        return d.currentlySelected ? true : false
      }})
      .style("fill", function (d, i) {
        return scalesDict.colorScale(i);
      });

    numTicks = 0;

    force.on("tick", function () {

      numTicks++;

      //check to make sure we are still in the dom
      //this caused errors during testing
      if (numTicks === 40) {

        zoomToFit(self)
      }

      node.attr("x", function (d) {
        return d.x
      })
        .attr("y", function (d) {
          return d.y
        });

      link.attr("x1", function (d) {
        return d.source.x;
      })
        .attr("y1", function (d) {
          return d.source.y;
        })
        .attr("x2", function (d) {
          return d.target.x;
        })
        .attr("y2", function (d) {
          return d.target.y;
        });

    });

    function zoomToFit(self) {

      z = d3.behavior.zoom().on("zoom", null);

      var center, largestDistance;

      var maxX, maxY, minX, minY;

      var buffer;

      var xList = [], yList = [];

      this.$(".detail-node").each(function () {

        xList.push(this.__data__.x);
        yList.push(this.__data__.y);

      });

      maxX = _.max(xList);
      minX = _.min(xList);

      maxY = _.max(yList);
      minY = _.min(yList);

      buffer = 30;

      largestDistance = _.max([(maxX - minX), (maxY - minY)]) + buffer;

      center = [(maxX + minX) / 2, (maxY + minY) / 2];

      var newScale = 100 / largestDistance;

      var oldScale = z.scale();

      var translateX = -newScale * ((center[0]) / oldScale) + width / 2;
      var translateY = -newScale * ((center[1]) / oldScale) + height / 2;

      z.scale(newScale);
      z.translate([translateX, translateY]);

      g2.transition().duration(3000)
        .attr('transform', 'translate(' + translateX + ', ' + translateY + ')' + 'scale(' + z.scale() + ')');

      self.model.set("z", z);
      self.model.set("g2", g2);

      setTimeout(zoomCallback, 3000);

      //now initializing all zoom behaviors, including button zoom
      //will be called after 40 ticks
      function zoomCallback() {

        var drag = d3.behavior.drag()
          .on("drag", function () {
            var x = this.transform.animVal[0].matrix.e + d3.event.dx;
            var y = this.transform.animVal[0].matrix.f + d3.event.dy;
            d3.select(this).attr("transform", function () {
              return "translate(" + [ x, y ] + ")"
                + " scale(" + z.scale() + ")";
            });
            z.translate([x,y]);
          });

        g2.call(drag);

      }
    }

  };


  options.detailMixin.addExtraListeners = function(){

    //initialize popovers
    this.$('[data-toggle="popover"]').popover(
      {
        trigger: "hover",
        container: $("body"),
        html : true,
        placement : "left"
      }
    )

  };

  options.broadcastFilteredQuery = function(bibcodes){

    if (!bibcodes.length) {
      return;
    }

    var updater = new ApiQueryUpdater("fq");
    bibcodes = "bibcode:(" + bibcodes.join(" OR ") +")";
    var newQuery = this.getCurrentQuery().clone();
    updater.updateQuery(newQuery, "fq", "limit", bibcodes);

    this.resetWidget();
    this.pubsub.publish(this.pubsub.START_SEARCH, newQuery);
  };


  return function(){
    return new NetworkWidget(options);
  };


});