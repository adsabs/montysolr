define([
   'underscore',
  'js/widgets/network_vis/network_widget',
  'js/components/api_query_updater',
  'bootstrap'
],
  function(
   _,
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

  options.summaryMixin.labelSpaceMultiplier = 1.5;

  options.summaryMixin.addLabels = function(options){

    var nodes = options.nodes;
    var ticks = options.ticks;

    // get sizes of each group for the outer radius scale
    var sizes = [];
    _.each(nodes, function (n) {
      if (n.size) {
        sizes.push(n.size)
      }
    });

    sizes = _.sortBy(sizes, function (s) {
      return s
    })

    var fontScale = d3.scale.linear()
      .domain([sizes[0], sizes[sizes.length - 1]])
      .range([20, 40]);

    //append labels
    var text = ticks.append("g")
      .attr("x", 0)
      .attr("dy", ".35em")
      .attr("transform", function (d, i) {
        return "rotate(" + -(d.angle * 180 / Math.PI - 90) + ")"
      })
      .classed("summary-label-container", true)
      .selectAll("text")
      .data(function (d, i) {
        return _.pairs(d.label);
      })
      .enter()
      .append("text")
      .attr("x", 0)
      .attr("text-anchor", "middle")
      .attr("y", function (d, i, j) {
        var size = nodes[j].size;
        return i * fontScale(size);
      })
      .attr("font-size", function (d, i, j) {
        var size = nodes[j].size;
        return fontScale(size) + "px";
      })
      .text(function (d, i) {
        if (i<=4){
          return d[0];

        }
      })

    return text;
  };

  options.detailMixin = {};

  options.detailMixin.renderNodes = function(options){

   var g2 = options.g2;

   var scalesDict = options.scalesDict;

   var authorRegex =/.+,\s*./;

   var node = g2.selectAll(".detail-node")
     .data(this.model.get("nodes"))
     .enter()
     .append("text")
     .text(function (d) {
       var normalizedName = d.first_author.match(authorRegex);
       return d.nodeName.slice(0,4) + "; " + normalizedName;
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
     }});

   return node

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