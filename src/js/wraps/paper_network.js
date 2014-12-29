define([
    'marionette',
    'js/widgets/network_vis/network_widget',
    'js/components/api_query_updater',
    'hbs!./templates/paper-network-data',
    'bootstrap'
  ],
  function (
    Marionette,
    NetworkWidget,
    ApiQueryUpdater,
    DataTemplate,
    bs
    ) {

    var options = {};

    options.endpoint = "services/vis/paper-network";

    options.networkType = "paper";

    options.helpText = "<p>Papers are grouped by shared references, because " +
      " they are more likely to discuss similar topics.</p>" +
      " <p>If your search returned a large enough set of papers, you will see two views:" +
      " a <b>summary view</b>  with groups of tightly linked papers, and a <b>detail view</b> " +
      " with individual papers and their connections. </p><p>The size of the nodes corresponds " +
      " to cumulative number of shared citations.</p>";

    //these defaults won't typically change
    options.graphMixin = {};

    options.graphMixin.labelSpaceMultiplier = 1.5;

    options.graphMixin.drawSummaryGraph = function () {

      var d3Svg = d3.select(this.$("svg.summary-chart")[0]);

      var graphData = this.model.get("summaryGraph");

      var nodes = graphData.nodes;

      // get a  range for link weights
      var weights = [];

      for (var i = 0; i < graphData.links.length; i++) {
        weights.push(graphData.links[i].value);
      }

      weights.sort(function sortNumber(a, b) {
        return a - b;
      });

      var self = this;

      //show detail panel

      this.$(".detail-panel").removeClass("hidden");

      var paperNums = _.pluck(graphData.nodes, "paperCount");

      var pie = d3.layout.pie();

      // get sizes of each group for the outer radius scale
      var sizes = [];
      _.each(graphData.nodes, function (n) {
        if (n.size) {
          sizes.push(n.size / n.paperCount)
        }
      });

      sizes = _.sortBy(sizes, function (s) {
        return s
      });

      var width = 1000,
        height = 1000,
        radiusScale = d3.scale.linear().domain([sizes[0], sizes[sizes.length - 1]]).range([1.3, 2.5]);

        var innerRadius = Math.min(width, height) * .13;

      this.innerRadius = innerRadius;

      var calculateOuterRadius = function (d, i, j) {

        var size = graphData.nodes[i].size /graphData.nodes[i].paperCount;
        return innerRadius * radiusScale(size)
      };

      //give the labels a little inner padding
      var calculateLabelPosition = function (d, i, j) {

        var size = graphData.nodes[d.index].size;
        return innerRadius * 2;
      };


      var arc = d3.svg.arc().innerRadius(innerRadius).outerRadius(calculateOuterRadius);
      //ads colors
      var fill = d3.scale.ordinal().range(this.adsColors);

      var svg = d3Svg
        .attr("width", width)
        .attr("height", height)
        .append("g")
        .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

      var data = pie(paperNums);

      _.each(data, function (d, i) {

        var totalPadding = (2 * Math.PI / 22) / data.length;

        data[i].startAngle = data[i].startAngle + totalPadding / 2;

        data[i].endAngle = data[i].endAngle - totalPadding / 2;


      });

      var groups = svg.selectAll("path")
        .data(data)
        .enter()
        .append("path")
        .attr("fill", function (d, i) {

          return fill(i)
        })
        .attr("d", arc)
        .classed("summary-node-group", true)
        //for testing purposes, add the id
        .attr("id", function(d,i ){return "vis-group-" + graphData.nodes[i].id})
        .on("mouseover", fade("mouseenter"))
        .on("mouseout", fade("mouseleave"))
        //trigger group select events
        .on("click", function (d, i) {

          var groupId = graphData.nodes[i].id;
          self.trigger("group:selected", groupId);

        });

      // Returns an array of tick angles and labels, given a group.
      function groupTicks(d, i, j) {

        var name = graphData.nodes[i].nodeName;
        var k = (d.endAngle - d.startAngle) / d.value;
        return [
          {
            angle: d.value / 2 * k + d.startAngle,
            label: name
          }
        ];
      }

      var ticks = svg.append("g").selectAll("g")
        .data(data)
        .enter().append("g").selectAll("g")
        .data(groupTicks)
        .enter().append("g")
        .classed("groupLabel", true)
        .attr("transform", function (d, i, j) {
          return "rotate(" + (d.angle * 180 / Math.PI - 90) + ")" + "translate(" + calculateLabelPosition(_.extend(d, {
            index: j
          })) + ",0)";
        });

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
        .classed("text-labels", true)
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
          if (i <= 4) {
            return d[0];

          }
        });

      //show and fade labels
      text.on("mouseover", fadeText("mouseenter"))
        .on("mouseout", fadeText("mouseleave"));

      //trigger group select events
      text.on("click", function () {

        var groupIndex = graphData.nodes[this.parentNode.__data__.index].id;

        self.trigger("group:selected", groupIndex);

      });

      // Returns an event handler for fading a given chord group.
      function fade(event) {
        return function (g, i, j) {

          var textOpacity = event == "mouseenter" ? 1 : .25;

          //fade the text
          d3.selectAll(self.$(".summary-label-container"))
            .filter(function (d, i2) {
              return i2  == i
            })
            .selectAll("text")
            .transition()
            .style("opacity", textOpacity);

        };
      }

      //have to add this to the text or else it interferes with mouseover
      function fadeText(event) {
        return function (g, j, i) {

          var textOpacity = event == "mouseenter" ? 1 : .25;

          //fade the text
          d3.select(this.parentNode)
            .selectAll("text")
            .transition()
            .style("opacity", textOpacity);

        };
      }
    };

    options.graphMixin.events = {

      "change input[name=height-setting]" : "toggleHeightSetting"
    };

    options.graphMixin.toggleHeightSetting = function(e){

      var self = this;

      //val is either read_count vs "size" which is citations

      var val = $(e.target).val();

      var graphData  =this.model.get("summaryGraph");

      // get sizes of each group (average over total papers) for the outer radius scale
      var sizes = [];
      _.each(graphData.nodes, function (n) {

        var  individualSize = n[val] || 0 ;

          sizes.push(individualSize / n.paperCount);

      });

      sizes = _.sortBy(sizes, function (s) {
        return s
      });

      radiusScale = d3.scale.linear().domain([sizes[0], sizes[sizes.length - 1]]).range([1.3, 2.5]);

      var calculateOuterRadius = function (d, i, j) {

        var size = graphData.nodes[i][val] /graphData.nodes[i].paperCount;
        return self.innerRadius * radiusScale(size);
      };

      var arc = d3.svg.arc().innerRadius(this.innerRadius).outerRadius(calculateOuterRadius);

      d3.selectAll(".summary-node-group")
        .transition()
        .attr("d", arc);

      var fontScale = d3.scale.linear()
        .domain([sizes[0], sizes[sizes.length - 1]])
        .range([20, 40]);

      d3.selectAll(".summary-label-container")
        .attr("y", function (d, i) {
          var size = graphData.nodes[i][val]/graphData.nodes[i].paperCount;;
          return i * fontScale(size);
        })
        .selectAll("text")
        .attr("y", function (d, i, j) {
          var size = graphData.nodes[j][val]/graphData.nodes[j].paperCount;
          return i * fontScale(size);
        })
        .attr("font-size", function (d, i, j) {
          var size = graphData.nodes[j][val]/graphData.nodes[j].paperCount;
          return fontScale(size) + "px";
        })

    };

    options.showDetailGraphView = function () {

      var groupId = this.model.get("currentGroup");

      var groupIndex;

      _.each(this.model.get("data").summaryGraph.nodes, function (n, i) {

        if (n.id == groupId) {

          groupIndex = i;

        }

      });

      //get data
      var summaryData = _.filter(this.model.get("data").summaryGraph.nodes, function (n, i) {

        return ( n.id == groupId)

      })[0];

      //make a copy
      summaryData = $.extend({}, summaryData);

      summaryData.processedTopCommonReferences = [];

      _.each(summaryData.topCommonReferences, function (v, k) {

        summaryData.processedTopCommonReferences.push([k, (v * 100).toFixed(0)]);

      });

      summaryData.processedTopCommonReferences = _.sortBy(summaryData.processedTopCommonReferences,
        function(n){return n[1]}).reverse();

      var detailGraph = this.extractGraphData(groupId);

      var topNodes = _.sortBy(detailGraph.nodes, function (o) {

        return o.citation_count;

      }).reverse().slice(0, 5);

      var detailModel = new Backbone.Model();

      //if there are no citations for any paper, let the template know

      if (topNodes[0].citation_count === 0){

        detailModel.set("noCitations", true);

      }

      detailModel.set("summaryData", summaryData);

      if (this.model.get("currentlySelectedGroupIds").indexOf(groupId)!== -1){

        detailModel.set("currentlySelected", true);

      }
      else {

        detailModel.set("currentlySelected", false);

      }

      detailModel.set("topNodes", topNodes);

      detailModel.set("fullGraph", this.model.get("data").fullGraph);

      detailModel.set("groupId", groupId);

      detailModel.set("backgroundColor", this.adsColors[groupIndex]);

      //render view

      var detailView = new DetailView({model: detailModel});

      this.listenTo(detailView, "name:toggle", this.updateSingleName);
      this.listenTo(detailView, "names:toggle", this.updateGroupOfNames);

      this.listenTo(detailView, "close", function () {
        this.stopListening(detailView);
      });

      this.$(".info-region").empty().append(detailView.render().el);

      this.$(".info-region").fadeIn();

    };

    var DetailView = Marionette.ItemView.extend({

      template: DataTemplate,

      className : "paper-network-detail-view",

      events: {
        "click .update-all": "toggleAllNames"
      },

      toggleAllNames: function () {

        var names = [];

        _.each(this.model.get("fullGraph").nodes, function (n) {

          if (n.group ===this.model.get("groupId")) {

            names.push(n.nodeName);
          }

        }, this);

        var action = this.$(".update-all").hasClass("add-all") ? "add" : "remove";

        this.trigger("names:toggle", names, action);

        this.toggleUpdateButton();

      },

      toggleUpdateButton: function () {

        this.$(".update-all").toggleClass("add-all");

        if (this.$(".update-all").hasClass("add-all")) {

          this.$(".update-all").text("Add entire group to filter list.");

        }

        else {

          this.$(".update-all").text("Remove entire group from filter list.");

        }

      }


    });

    options.broadcastFilteredQuery = function (bibcodes) {

      if (!bibcodes.length) {
        return;
      }

      var updater = new ApiQueryUpdater("fq");
      bibcodes = "bibcode:(" + bibcodes.join(" OR ") + ")";
      var newQuery = this.getCurrentQuery().clone();
        newQuery.unlock();
      updater.updateQuery(newQuery, "fq", "limit", bibcodes);

      this.resetWidget();
      this.pubsub.publish(this.pubsub.START_SEARCH, newQuery);
    };

    return function () {
      return new NetworkWidget(options);
    };

  });