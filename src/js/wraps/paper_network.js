define([
    'marionette',
    'js/widgets/network_vis/network_widget',
    'js/components/api_query_updater',
    'hbs!./templates/paper-network-data',
    'hbs!./templates/paper-network-container',
    'hbs!../widgets/network_vis/templates/not-enough-data-template',
    'hbs!./templates/paper-network-link-data',
    'js/components/api_targets',
    'bootstrap'
  ],
  function (
    Marionette,
    NetworkWidget,
    ApiQueryUpdater,
    GroupDataTemplate,
    ContainerTemplate,
    NotEnoughDataTemplate,
    LinkDataTemplate,
    ApiTargets,
    bs) {

    var options = {};

    options.endpoint = ApiTargets.SERVICE_PAPER_NETWORK;

    options.networkType = "paper";

    options.helpText = "<p>Papers are grouped by shared references, because " +
      " they are more likely to discuss similar topics.</p>" +
      " <p>If your search returned a large enough set of papers, you will see two views:" +
      " a <b>summary view</b>  with groups of tightly linked papers, and a <b>detail view</b> " +
      " that gives you more information about the group </p>";

    var GraphModel = Backbone.Model.extend({
      defaults: function () {
        return  {
          //data from the api
          graphData: {},
          //keep track of selection state
          selectedEntity: undefined,
          cachedEntity: undefined,
          linkLayer: false,
          mode: "occurrences"
        }
      }
    });

    options.graphView = Marionette.ItemView.extend({

      template: ContainerTemplate,

      className: "graph-view",

      events: {
        "change input[name=mode]": function (e) {
          this.model.set("mode", e.target.value);
        },
        "change input[name=show-link]": function (e) {
          this.model.set("linkLayer", e.target.checked);
        },
        "click .filter-remove": function (e) {
          var id = this.model.get("selectedEntity");
          var displayName = _.findWhere(this.model.get("graphData").summaryGraph.nodes, {id: id}).node_name
          //showing display name
          Marionette.getOption(this, "filterCollection").remove({id: displayName});
          //re-render detail sub view
          this.showSelectedEntity();
        },
        "click .filter-add": function (e) {
          var id = this.model.get("selectedEntity");
          var displayName = _.findWhere(this.model.get("graphData").summaryGraph.nodes, {id: id}).node_name
          Marionette.getOption(this, "filterCollection").add({name: displayName});
          //re-render detail sub view
          this.showSelectedEntity();
        }
      },

      modelEvents: {
        "change:graphData": "renderGraph",
        "change:selectedEntity": "showSelectedEntity",
        "change:mode": "changeMode"
      },

      onRender: function () {
        if (this.model.get("graphData")) {
          this.renderGraph();
        }
      },

      /* functions to render the graph and overlay */

      getConfig: function () {
        //hold static config variables here
        this.config = {
          width: 400,
          height: 400,
          noGroupColor: "#a6a6a6"
        };
        this.config.radius = Math.min(this.config.width, this.config.height) / 2;
        this.config.outerRadius = Math.min(this.config.width, this.config.height) * .23;
        this.config.innerRadius = Math.min(this.config.width, this.config.height) * .13
      },

      generateCachedVals: function () {
        var that = this;

        //these are d3 layouts/objects we cache for multiple functions
        this.cachedVals = {
          line: undefined,
          bundle: undefined,
          svg: undefined,
          arc: undefined
        };

        this.cachedVals.bundle = d3.layout.bundle();

        this.cachedVals.line = d3.svg.line.radial()
          .interpolate("bundle")
          .tension(.3)
          .radius(function (d) {
            if (d.startAngle) {
              return that.config.innerRadius + 10;
            }
            //it's the center node
            else {
              return 0
            }
          })
          .angle(function (d) {
            if (d.startAngle) {
              return (d.startAngle + d.endAngle) / 2
            }
            else {
              return 0
            }
          });

        this.cachedVals.svg = d3.select(this.$("svg.network-chart")[0])
          .attr("width", that.config.width)
          .attr("height", that.config.height)
          .append("g")
          .attr("transform", "translate(" + that.config.width / 2 + "," + that.config.height / 2.5 + ")");

        this.cachedVals.arc = d3.svg.arc()
          .innerRadius(that.config.innerRadius)
          .outerRadius(that.config.outerRadius);

        this.cachedVals.pie = d3.layout.pie()
          .value(function (d) {
            return d.paper_count;
          })
          .sort(function (a, b) {
            return a.node_name - b.node_name;
          });

        that.cachedVals.groupTicks = function (d) {
          return  {
            angle: (d.startAngle + d.endAngle) / 2,
            label: d.data.node_label,
            data: d
          };
        }
      },

      computeScales: function () {

        var graphData = this.model.get("graphData").summaryGraph;
        this.scales = {};

        // get a  range for link weights
        var weights = _.map(graphData.links, function (l) {
          if (l.source !== l.target) {
            return l.weight;
          }
        });

        this.scales.tensionScale = d3.scale.linear()
          .domain([d3.min(weights), d3.max(weights)])
          .range([0, 1]);

        this.scales.linkScale = d3.scale.linear()
          .domain([d3.min(weights), d3.max(weights)])
          .range([3, 22]);

        var paperCounts = _.pluck(graphData.nodes, "paper_count")
        //set up initial font scale
        this.scales.initialFontScale = d3.scale.linear()
          .domain([d3.min(paperCounts), d3.max(paperCounts)])
          .range([7, 17]);

        this.scales.fill = d3.scale.ordinal()
          .domain([1, 2, 3, 4, 5, 6])
          .range(["hsla(282, 80%, 52%, 1)", "hsla(1, 80%, 51%, 1)", "hsla(42, 97%, 48%, 1)", "hsla(152, 80%, 40%, 1)", "hsla(193, 80%, 48%, 1)", "hsla(220, 80%, 56%, 1)", "hsla(250, 69%, 47%, 1)"]);
      },

      renderGraph: function () {

        this.getConfig();
        this.computeScales();
        this.generateCachedVals();

        var svg = this.cachedVals.svg,
          graphData = this.model.get("graphData").summaryGraph,
          labelSum = [],
          that = this;

        if (!graphData) {
          this.$el.html(NotEnoughDataTemplate())
        }

        var pie = this.cachedVals.pie;
        var arc = this.cachedVals.arc;
        var data = pie(graphData.nodes);

        //can't be equal to zero or else the lines barf
        _.findWhere(data, {startAngle: 0 }).startAngle = 0.001;

        svg.selectAll("rect")
          .data(data)
          .enter()
          .append("rect")
          .classed("node-path", true);

        //do this first so that the path data can layer over it slightly
        // and make it look better
        this.renderLinkLayer();

        svg.selectAll("rect").remove();

        svg.selectAll(".node-path")
          .data(data)
          .enter()
          .append("path")
          .classed("node-path", true)
          .attr("fill", function (d, i) {
            if (d.data.node_name > 7) {
              return that.config.noGroupColor;
            } else {
              return that.scales.fill(d.data.node_name)
            }
          })
          .attr("d", arc)
          //for testing purposes, add the id
          .attr("id", function (d, i) {
            return "vis-group-" + d.data.node_name
          })
          .on("mouseover", fade("mouseenter"))
          .on("mouseout", fade("mouseleave"))
          //trigger group select events
          .on("click", function (d, i) {
            var groupId = d.data.id;
            that.model.set("selectedEntity", groupId);
          });

        var ticks = svg.selectAll(".groupLabel")
          .data(function () {
            return _.map(data, function (d, i) {
              return that.cachedVals.groupTicks(d)
            });
          })
          .enter()
          .append("g")
          .each(function (d) {
            labelSum.push(d.data.value)
          })
          .classed("groupLabel", true)
          .attr("transform", function (d) {
            return "rotate(" + (d.angle * 180 / Math.PI - 90) + ")" + "translate(" + that.config.outerRadius + ",0)";
          });

        labelSum = d3.sum(labelSum);

        //append labels
        var text = ticks.append("g")
          .attr("x", 0)
          .attr("dy", ".5em")
          .attr("transform", function (d, i) {
            return "rotate(" + -(d.angle * 180 / Math.PI - 90) + ")"
          })
          .classed("summary-label-container", true)
          .classed("hidden", function (d) {
            if (d.data.value / labelSum < .08) {
              return true
            }
          })
          .selectAll("text")
          .data(function (d, i) {
            var data = _.pairs(d.label);
            return _.map(data, function (wordList) {
              wordList[2] = d.data.data.id;
              return wordList;
            });
          })
          .enter()
          .append("text")
          .attr("x", 0)
          .classed("paper-network-labels", true)
          .attr("text-anchor", "middle")
          .attr("y", function (d, i, j) {
            var size = _.findWhere(graphData.nodes, {id: d[2]}).paper_count;
            return i * that.scales.initialFontScale(size) - 30;
          })
          .attr("font-size", function (d, i, j) {
            var size = _.findWhere(graphData.nodes, {id: d[2]}).paper_count;
            return that.scales.initialFontScale(size) + "px";
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
        text.on("click", function (d) {
          var groupId = d[2];
          that.model.set("selectedEntity", groupId);
        });

        // Returns an event handler for fading a given chord group.
        function fade(event) {
          return function (g, i, j) {
            var textOpacity = event == "mouseenter" ? 1 : .8;
            //fade the text
            d3.selectAll(self.$(".summary-label-container"))
              .filter(function (d, i2) {
                return i2 == i
              })
              .selectAll("text")
              .transition()
              .style("opacity", textOpacity);
          };
        };

        //have to add this to the text or else it interferes with mouseover
        function fadeText(event) {
          return function (g, j, i) {
            var textOpacity = event == "mouseenter" ? 1 : .8;
            //fade the text
            d3.select(this.parentNode)
              .selectAll("text")
              .transition()
              .style("opacity", textOpacity);
          };
        }
      },

      parseLinks: function (links) {

        var svg = this.cachedVals.svg,
          parentInCommon = {name: "fake"};

        links = _.chain(links).map(function (l) {
          var linkData = {};
          linkData.source = svg.selectAll(".node-path").filter(function (d, i) {
            return d.data.stable_index == l.source;
          })[0][0].__data__;
          linkData.target = svg.selectAll(".node-path").filter(function (d, i) {
            return d.data.stable_index == l.target;
          })[0][0].__data__;
          //ignore self links
          if (linkData.source === linkData.target) {
            return undefined
          }
          else {
            linkData.weight = l.weight;
            linkData.source.parent = parentInCommon;
            linkData.target.parent = parentInCommon;
            return linkData
          }
        }).filter(function (l) {
          if (l) {
            return true
          }
        }).value();
        return links;
      },

      updateLinkLayer: function () {

        var that = this,
          svg = this.cachedVals.svg,
          links = that.model.get("graphData").summaryGraph.links,
          line;

        links = this.parseLinks(links);
        line = this.cachedVals.line;

        svg.selectAll(".link")
          .data(that.cachedVals.bundle(links))
          .transition()
          .duration(1000)
          .attr("d", function (d) {
            return line(d)
          });
      },

      renderLinkLayer: function () {

        var that = this,
          svg = this.cachedVals.svg,
          links = that.model.get("graphData").summaryGraph.links,
          line, linkContainer;

        links = this.parseLinks(links)
        line = this.cachedVals.line;

        linkContainer = svg.append("g")
          .classed("link-container", true);

        linkContainer.append("circle")
          .attr("r", that.config.radius)
          .classed("link-background", true);

        linkContainer.selectAll(".link")
          .data(that.cachedVals.bundle(links))
          .enter().append("path")
          .attr("class", "link")
          .attr("d", function (d) {
            return line(d)
          })
          .attr("stroke-width", function (d) {
            // get link weight
            var weight = _.findWhere(links, function (l) {
              return (l.source == d[0] && l.target == d[d.length - 1] || l.target == d[0] && l.source == d[d.length - 1])
            }).weight;
            return that.scales.linkScale(weight);
          }).on("click", function (d) {
            //set the selected Entity to be the link
            that.model.set("selectedEntity", [d[0], d[2]])
          });

      },

      showSelectedEntity: function () {

        var that = this,
          data = {},
          topNodes;

        var entity = this.model.get("selectedEntity");

        // it's a link
        if (_.isArray(entity)) {

          var id1, id2, links1, links2, allReferences1, allReferences2;
          var shared = [];
          //find references in common
          id1 = entity[0].data.id;
          id2 = entity[1].data.id;

          links1 = that.getAllLinks(id1);
          links2 = that.getAllLinks(id2);

          allReferences1 = _.flatten(_.pluck(links1, "overlap"));
          allReferences2 = _.flatten(_.pluck(links2, "overlap"));

          _.each(_.intersection(allReferences1, allReferences2), function (s) {
            percent1 = _.filter(allReferences1, function (b) {
              return b == s
            }).length / allReferences1.length;

            percent2 = _.filter(allReferences2, function (b) {
              return b == s
            }).length / allReferences2.length;

            shared.push({name: s, percentOne: percent1 * 100, percentTwo: percent2 * 100});

          });

          shared = _.sortBy(shared, function (s) {
            return (s.percentOne * s.percentTwo);
          }).reverse();
          data.shared = shared.slice(0, 10);
          _.each(data.shared, function (s, i) {
            data.shared[i].percentOne = data.shared[i].percentOne.toFixed(2);
            data.shared[i].percentTwo = data.shared[i].percentTwo.toFixed(2);
          });

          data.group1 = {name: _.findWhere(this.model.get("graphData").summaryGraph.nodes, {id: id1}).node_name};
          data.group1.color = data.group1.name < 8 ? that.scales.fill(data.group1.name) : that.config.noGroupColor;

          data.group2 = {name: _.findWhere(this.model.get("graphData").summaryGraph.nodes, {id: id2}).node_name};
          data.group2.color = data.group1.name < 8 ? that.scales.fill(data.group2.name) : that.config.noGroupColor;

          data.referencesLength = data.shared.length;

          this.$(".network-detail-area").html(LinkDataTemplate(data));

        }
        //it's a node id
        else {

          var groupData = _.findWhere(that.model.get("graphData").summaryGraph.nodes, function (g) {
            return ( g.id == entity);
          });

          //make a copy
          summaryData = $.extend({}, groupData);
          summaryData.processedTopCommonReferences = [];
          summaryData.titleWords = _.keys(groupData.node_label);

          var groupBibs = _.pluck(that.getAllNodes(entity), "node_name");

          _.each(summaryData.top_common_references, function (v, k) {
            var inGroup = _.contains(groupBibs, k) ? true : false;
            summaryData.processedTopCommonReferences.push({bibcode: k, percent: (v * 100).toFixed(0), inGroup: inGroup});
          });

          summaryData.processedTopCommonReferences = _.sortBy(summaryData.processedTopCommonReferences,
            function (n) {
              return n[1]
            }).reverse();
          //using OLD ID because it is the one that joins the group to the detail nodes
          filteredNodes = this.getAllNodes(groupData.id);
          topNodes = _.sortBy(filteredNodes, function (o) {
            return o.citation_count;
          }).reverse();
          data.morePapers = topNodes.length > 30 ? true : false;
          data.summaryData = summaryData;
          data.inFilter = Marionette.getOption(that, "filterCollection").get(groupData.node_name) ? true : false;
          data.topNodes = topNodes.slice(0, 30);
          data.groupIdToShow = groupData.node_name;
          data.backgroundColor = groupData.node_name < 8 ? that.scales.fill(groupData.node_name) : that.config.noGroupColor;

          this.$(".network-detail-area").html(GroupDataTemplate(data));
        }

      },

      changeMode: function () {

        var that = this,
          pie = this.cachedVals.pie,
          arc = this.cachedVals.arc,
          svg = this.cachedVals.svg,
          vals = [],
          fontScale,
          data,
          newData,
          groups,
          labelSum = [],
          graphData = this.model.get("graphData").summaryGraph,
          mode = this.model.get("mode");

        function arcTween(d, i) {
          var i = d3.interpolate({startAngle: this.oldStartAngle, endAngle: this.oldEndAngle}, d);
          return function (t) {
            var b = i(t);
            this.startAngle = b.startAngle;
            this.endAngle = b.endAngle;
            return arc(b);
          };
        }

        pie.value(function (d) {
          if (mode === "references") {
            var linkInfo = _.findWhere(graphData.links, function (l) {
              if (l.source === d.stable_index && l.target === d.stable_index) {
                return true
              }
            });
            vals.push(linkInfo.weight);
            return linkInfo.weight;
          }
          else {
            vals.push(d[mode]);
            return d[mode]
          }
        });

        data = pie(graphData.nodes);

        fontScale = d3.scale.linear()
          .domain([d3.min(vals), d3.max(vals)])
          .range([7, 17]);

        //can't be equal to zero or else the lines barf
        _.findWhere(data, {startAngle: 0 }).startAngle = 0.001;

        svg.selectAll(".node-path")
          .each(function (d) {
            //cache for the transition
            this.oldStartAngle = d.startAngle;
            this.oldEndAngle = d.endAngle;
          })
          .data(data, function (d) {
            return d.data.id
          })
          .transition()
          .duration(1000)
          .attrTween("d", arcTween);

        //take care of link placement
        this.updateLinkLayer();

        newData = _.map(data, function (d) {
          labelSum.push(d.value);
          return that.cachedVals.groupTicks(d);
        });

        labelSum = d3.sum(labelSum);

        groups = svg.selectAll(".groupLabel")
          .data(newData)
          .transition()
          .duration(1000)
          .attr("transform", function (d) {
            return "rotate(" + (d.angle * 180 / Math.PI - 90) + ")" + "translate(" + that.config.outerRadius + ",0)";
          });

        svg.selectAll(".summary-label-container")
          //why isn't this updating with the parent data ????
          //WHYY?????
          .data(newData)
          .classed("hidden", function (d) {
            if (d.data.value / labelSum < .08) {
              return true
            }
          })
          .transition()
          .duration(1000)

          .attr("transform", function (d, i) {
            return "rotate(" + -(d.angle * 180 / Math.PI - 90) + ")"
          });

        groups.selectAll("text")
          .attr("y", function (d, i, j) {
            var size = this.parentNode.__data__.data.value;
            return i * fontScale(size) - 30;
          })
          .attr("font-size", function (d, i, j) {
            var size = this.parentNode.__data__.data.value;
            return fontScale(size) + "px";
          });
      },

      getAllNodes: function (id) {

        var fullGraph = this.model.get("graphData").fullGraph;
        var filteredNodes = [];

        _.each(fullGraph.nodes, function (n, i) {
          if (n.group === id) {
            filteredNodes.push(n)
          }
        });

        return filteredNodes

      },

      getAllLinks: function (id) {
        var fullGraph = this.model.get("graphData").fullGraph;
        var indexes = [];
        links = [],
          filteredNodes = [];

        _.each(fullGraph.nodes, function (n, i) {
          if (n.group === id) {
            indexes.push(i)
          }
        });
        _.each(fullGraph.links, function (l, i) {
          if (indexes.indexOf(l.source) !== -1 || indexes.indexOf(l.target) !== -1) {
            links.push(l)
          }
        });

        return links;
      }

    });

    options.broadcastFilteredQuery = function () {

      var graphData = this.model.get("graphData"),
        that = this,
        filterBibcodes = [],
        updater = new ApiQueryUpdater("fq"),
        newQuery = newQuery = this.getCurrentQuery().clone();

      //get associated bibcodes
      this.filterCollection.each(function (model) {
        var name = model.get("name");
        var group = _.findWhere(graphData.summaryGraph.nodes, {"node_name": name});
        var bibcodes = that.view.graphView.getAllNodes(group.id);
        bibcodes = _.pluck(bibcodes, "node_name");
        Array.prototype.push.apply(filterBibcodes, bibcodes);
      });

      bibcodes = "bibcode:(" + filterBibcodes.join(" OR ") + ")";
      newQuery.unlock();
      updater.updateQuery(newQuery, "fq", "limit", bibcodes);

      this.resetWidget();
      this.pubsub.publish(this.pubsub.START_SEARCH, newQuery);
    };

    return function () {
      return new NetworkWidget(options);
    };

  });