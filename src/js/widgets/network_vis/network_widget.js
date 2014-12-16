define([
    "marionette",
    "d3",
    "js/components/api_request",
    "js/components/api_query",
    "js/widgets/base/base_widget",
    "js/components/api_query_updater",
    "hbs!./templates/container-template",
    'hbs!./templates/detail-graph-container-template',
    'hbs!./templates/summary-graph-container-template',
    'hbs!./templates/selected-items-template',
    'hbs!./templates/not-enough-data-template',
    'hbs!./templates/connector-template',
    "bootstrap"

  ],
  function (Marionette,
            d3,
            ApiRequest,
            ApiQuery,
            BaseWidget,
            ApiQueryUpdater,
            ContainerTemplate,
            DetailTemplate,
            SummaryTemplate,
            selectedItemsTemplate,
            notEnoughDataTemplate,
            connectorTemplate) {


    var ChosenNamesModel = Backbone.Model.extend({

      defaults: {
        name: undefined
      },

      idAttribute: "name"


    });

    var ChosenNamesCollection = Backbone.Collection.extend({

      model: ChosenNamesModel

    })

    var ChosenNamesItemView = Marionette.ItemView.extend({

      tagName: "li",

      template: function (data) {
        return data.name + "<button class=\"vis-close\">x</button>"
      },

      events: {
        "click .vis-close": "removeName"
      },

      removeName: function (e) {

        e.stopImmediatePropagation();

        this.model.collection.remove(this.model);

      }

    });

    var EmptyItemsView = Marionette.ItemView.extend({

      tagName: "li",

      template: function (data) {
        return "Click on a node in the detail view to add it to this list. You can then filter your current search to include only the selected items."
      }


    });

    var ChosenNamesView = Marionette.CompositeView.extend({

      template: selectedItemsTemplate,

      className: "btn-group",

      itemView: ChosenNamesItemView,

      emptyView: EmptyItemsView,

      itemViewContainer: ".dropdown-menu",

      events: {
        "click .name-collection-container": function (e) {
          e.stopImmediatePropagation();
          e.stopPropagation();

        }
      },

      collectionEvents: {
        "augment": "render",
        "remove": "render",
        "reset": "render"
      },

      serializeData: function () {
        return {
          networkType: Marionette.getOption(this, "networkType"),
          selectedNum: this.collection.length

        }
      }

    });


    var NetworkModel = Backbone.Model.extend({

      defaults: function () {
        return  {
          //the summary graph data
          summaryData: {},
          //the full graph data
          fullGraph: {},
          nodes: [],
          links: [],
          currentGroup: undefined,
          svg: undefined,
          scales : {}

        }
      }

    });

    //this stuff changes


    var ContainerView = Marionette.ItemView.extend({

      initialize: function (options) {

        this.chosenNamesCollection = new ChosenNamesCollection();
        this.chosenNamesView = new ChosenNamesView({collection: this.chosenNamesCollection, networkType: Marionette.getOption(this, "networkType") });


      },

      className: function () {

        var networkType = Marionette.getOption(this, "networkType");
        return networkType + "-graph s-" + networkType + "-graph"

      },

      serializeData: function () {

        var n = Marionette.getOption(this, "networkType");

        n = n[0].toUpperCase() + n.slice(1);

        return {networkType: n,
          helpText: Marionette.getOption(this, "helpText")}
      },

      template: ContainerTemplate,


      ui: {
        selectedContainer: ".selected-items-container"

      },


      updateSingleName: function (name) {

        if (this.chosenNamesCollection.get(name)) {
          //remove it

          this.chosenNamesCollection.remove(name);

        }
        else {
          this.chosenNamesCollection.add({name: name});
          this.chosenNamesCollection.trigger("augment");

        }

      },

      updateGroupOfNames: function (names, action) {

        if (action === "add") {

          _.each(names, function (n) {

            this.chosenNamesCollection.add({name: n}, {silent: true})

          }, this);

          this.chosenNamesCollection.trigger("augment")

        }

        else {

          _.each(names, function (n) {

            this.chosenNamesCollection.remove(this.chosenNamesCollection.get(n))

          }, this)

        }

      },

      events: {
        "click .filter-search": "signalSearchFiltered",
        "click .close-widget": "signalCloseWidget"
      },

      modelEvents: {

        "change:fullGraph": "render"

      },

      signalSearchFiltered: function () {

        var names = _.pluck(this.chosenNamesCollection.toJSON(), "name");

        this.trigger("filterSearch", names);


      },

      signalCloseWidget: function () {

        this.trigger("close")


      },


      onRender: function () {

        var newGraphView = false;

        //it's a big graph with summary nodes
        if (!_.isEmpty(this.model.get("summaryData"))) {

          this.graphView = new SummaryGraphView({model: this.model, chosenNamesCollection: this.chosenNamesCollection, detailMixin: Marionette.getOption(this, "detailMixin")})

          _.extend(this.graphView, Marionette.getOption(this, "summaryMixin"));

          this.ui.selectedContainer.append(this.chosenNamesView.render().el);

          this.$(".graph-region").append(this.graphView.render().el);

          newGraphView = true;

        }

        //not enough data to make a graph
        else if (_.isEmpty(this.model.get("fullGraph")) || !this.model.get("fullGraph").nodes.length) {

          this.$el.empty().append(notEnoughDataTemplate())

        }

        //enough data just for a basic network graph

        else if (this.model.get("fullGraph").nodes.length > 1) {

          this.model.set("nodes", this.model.get("fullGraph").nodes);

          this.model.set("links", this.model.get("fullGraph").links);

          this.graphView = new DetailNetworkView({model: this.model})

          this.ui.selectedContainer.append(this.chosenNamesView.render().el);

          this.$(".graph-region").append(this.graphView.render().el);

          newGraphView = true;

        }

        that = this;

        if (newGraphView) {

          this.listenTo(this.graphView, "name:toggle", this.updateSingleName);
          this.listenTo(this.graphView, "names:toggle", this.updateGroupOfNames)

          this.listenTo(this.graphView, "close", function () {
            this.stopListening(this.graphView);
          })

        }

        //initialize popover
        this.$(".icon-help").popover({trigger: "hover", placement: "bottom", html: true});

      }


    })


    var SummaryGraphView = Marionette.ItemView.extend({

      adsColors: ["#5683e0", "#7ab889", "#ffb639", "#ed5e5b", "#ce5cff", "#1c459b", "#757575", "#b3b3b3", "#58b6d5"],

      initialize: function (options) {

        this.chosenNamesCollection = options.chosenNamesCollection;

        this.listenTo(this.model, "change:currentGroup", this.showDetailNetwork);

        this.listenTo(this.chosenNamesCollection, "remove", function () {
          if (this.detailNetworkView) {
            this.detailNetworkView.triggerMethod("node:removed", arguments);
          }
        })

        this.detailViews = new Backbone.ChildViewContainer();


      },

      className: "summary-graph-container s-summary-graph-container",


      ui: {

        detailContainer: ".detail-view .vis-container"

      },

      template: SummaryTemplate,

      events: {
        "click .summary-node-group": "setNodeDetail"

      },

      setNodeDetail: function (e) {

        e.stopPropagation();

        this.model.set("currentGroup", e.currentTarget);

      },

      showDetailNetwork: function (model, targetGroupNode) {

        var node, group;

        node = this.model.get("summaryData").nodes[targetGroupNode.__data__.index];

        group = node.id;

        var fill = d3.select(targetGroupNode).attr("fill");

        if (this.detailNetworkView) {

          //getting rid of event listeners

          this.detailNetworkView.remove();

        }

        this.ui.detailContainer.hide();


        //is it a connector? if so there is nothing to show, just render the specific template
        if (node.connector) {

          this.ui.detailContainer.empty();
          this.ui.detailContainer.append(connectorTemplate({nodeName: _.keys(node.nodeName)[0] }));
          this.ui.detailContainer.fadeIn();

          return

        }

        var preexistingView = this.detailViews.findByCustom(group);

        if (preexistingView) {

          this.detailNetworkView = preexistingView;

          this.detailNetworkView.delegateEvents();

        }

        else {

          //sticks graph data into model
          var data = this.extractGraphData(group);

          var data = _.extend(data, {fill: fill })

          //now create a new view
          this.detailNetworkView = new DetailNetworkView({model: new DetailModel(data), chosenNamesCollection: this.chosenNamesCollection, groupNumber: group});

          //extend with possible mixin passed in during configuration
          _.extend(this.detailNetworkView, Marionette.getOption(this, "detailMixin"));

          this.listenTo(this.detailNetworkView, "names:toggle", function (n, a) {
            this.trigger("names:toggle", n, a);
          })
          this.listenTo(this.detailNetworkView, "name:toggle", function (n, a) {
            this.trigger("name:toggle", n, a);
          })


          //need to stopListening to the detail network view or else there will be huge memory leaks!
          // this is because it will be held in this._listeningTo
          this.listenTo(this.detailNetworkView, "close", function () {
            this.stopListening(this.detailNetworkView);

          })

          this.detailNetworkView.render();

          this.detailViews.add(this.detailNetworkView, group);

        }

        //currently the paper network needs to add the popover interaction

        if (this.detailNetworkView.addExtraListeners) {

          this.detailNetworkView.addExtraListeners();

        }

        this.ui.detailContainer.empty().append(this.detailNetworkView.el);

        this.detailNetworkView.triggerMethod("show");

        //add highlight to node
        //tried to do this without d3 and it worked fine in the browser but made tests fail
        d3.select(Array.prototype.slice.apply(targetGroupNode.children)[0]).classed("target-group", true);

        this.ui.detailContainer
          .fadeIn();

      },

      extractGraphData: function (group) {

        var fullGraph = this.model.get("fullGraph");

        var nodes = [], indexDict = {}, links = [];

        _.each(fullGraph.nodes, function (n, i) {
          if (n.group === group && n.nodeName) {
            var newIndex = nodes.push(_.clone(n));
            newIndex -= 1
            indexDict[i] = newIndex;
          }
        });

        var keys = _.map(_.keys(indexDict), function (k) {
          return parseInt(k);
        });

        _.each(fullGraph.links, function (l, i) {
          if (keys.indexOf(l.source) !== -1 && keys.indexOf(l.target) !== -1) {
            //re-assign the indexes to the links
            var newLink = _.clone(l);
            newLink.source = indexDict[l.source];
            newLink.target = indexDict[l.target];
            links.push(newLink);
          }
        });

        return {"nodes": nodes, "links": links};

      },


      initializeGraph: function () {

        var d3Svg = d3.select(this.$("svg.summary-chart")[0]);

        var graphData = this.model.get("summaryData");

        // get a  range for link weights
        var weights = [];

        for (var i = 0; i < graphData.links.length; i++) {
          weights.push(graphData.links[i].value);
        }

        weights.sort(function sortNumber(a, b) {
          return a - b;
        });

        var self = this;

        var matrix = [],
          nodes = graphData.nodes,
          n = nodes.length;

        // Compute index per node.
        nodes.forEach(function (node, i) {

          node.index = i;
          node.count = 0;
          matrix[i] = d3.range(n).map(function (j) {
            return 0
          });
        });

        // Convert links to matrix; count character occurrences.
        graphData.links.forEach(function (link) {
          //not showing self-links
          matrix[link.source][link.target] += link.weight;
          matrix[link.target][link.source] += link.weight;

        });

        var chord = d3.layout.chord()
          .padding(.1)
          .sortSubgroups(d3.descending)
          .matrix(matrix);

        // get sizes of each group for the outer radius scale
        var sizes = [];
        _.each(graphData.nodes, function (n) {
          if (n.size) {
            sizes.push(n.size)
          }
        });

        sizes = _.sortBy(sizes, function (s) {
          return s
        });

        var width = 1000,
          height = 1000,
          radiusScale = d3.scale.linear().domain([sizes[0], sizes[sizes.length - 1]]).range([1.3, 2.5]),

          innerRadius = Math.min(width, height) * .13;

        var calculateOuterRadius = function (d) {
          var size = graphData.nodes[d.index].size;
          return innerRadius * radiusScale(size)
        };

        //give the labels a little inner padding
        var calculateLabelPosition = function (d) {
          var size = graphData.nodes[d.index].size;
          return innerRadius * radiusScale(size) * ( self.labelSpaceMultiplier || 1.1);
        };

        //ads colors
        var fill = d3.scale.ordinal().range(this.adsColors);

        var svg = d3Svg
          .attr("width", width)
          .attr("height", height)
          .append("g")
          .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

        svg.append("g").selectAll("path")
          .data(chord.groups)
          .enter()
          .append("path")
          .attr("fill", function (d) {
            return fill(d.index);
          })
          .attr("stroke", function (d) {
            return fill(d.index);
          })
          .classed("summary-node-group", true)
          .attr("d", d3.svg.arc().innerRadius(innerRadius).outerRadius(calculateOuterRadius))
          .on("mouseover", fade(.1))
          .on("mouseout", fade(1));


        var ticks = svg.append("g").selectAll("g")
          .data(chord.groups)
          .enter().append("g").selectAll("g")
          .data(groupTicks)
          .enter().append("g")
          .classed("groupLabel", true)
          .attr("transform", function (d, i) {
            return "rotate(" + (d.angle * 180 / Math.PI - 90) + ")" + "translate(" + calculateLabelPosition(_.extend(d, {
              index: i
            })) + ",0)";
          });

        //paper network does it differently
        if (this.addLabels) {
          var text = this.addLabels({ticks :ticks, nodes: graphData.nodes });
        }

        else {

          // make a font size scale based on the values of each node label
          // from each group
          var fontSizes = []
          _.each(graphData.nodes, function (n) {
            _.each(n.nodeName, function (v, k) {
              fontSizes.push(v);
            })
          });

          fontSizes = _.sortBy(fontSizes, function (n) {
            return n;
          });

          var fontScale = d3.scale.linear()
            .domain([fontSizes[0], fontSizes[fontSizes.length - 1]])
            .range([20, 40]);

          // make a font size scale based on the values of each group
          //(diff than author network)

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
              return i * 25;
            })
            .attr("font-size", function (d, i, j) {
              var size = d[1]
              return fontScale(size) + "px";
            })
            .text(function (d, i) {
              return d[0]
            })

        }

        //show and fade labels
        text.on("mouseover", fadeText(.1))
          .on("mouseout", fadeText(1));


        svg.append("g")
          .attr("class", "chord")
          .selectAll("path")
          .data(chord.chords)
          .enter()
          .append("path")
          .attr("d", d3.svg.chord().radius(innerRadius))
          .style("opacity", 1)
        // Returns an array of tick angles and labels, given a group.
        function groupTicks(d) {
          var name = graphData.nodes[d.index].nodeName;
          var k = (d.endAngle - d.startAngle) / d.value;
          return [
            {
              angle: d.value / 2 * k + d.startAngle,
              label: name
            }
          ];
        }

        // Returns an event handler for fading a given chord group.
        function fade(opacity) {
          return function (g, i) {

            var textOpacity = opacity == .1 ? 1 : .25;

            //fade the text
            d3.selectAll(self.$(".summary-label-container"))
              .filter(function (d, i) {
                return g.index == i
              })
              .selectAll("text")
              .transition()
              .style("opacity", textOpacity);

            //fade all other nodes out
            svg.selectAll(".chord path")
              .filter(function (d) {
                return d.source.index != i && d.target.index != i;
              })
              .transition()
              .style("opacity", opacity);
          };
        }

        // Returns an event handler for fading a given chord group.
        //have to add this to the text or else it interferes with mouseover
        function fadeText(opacity) {
          return function (g, j, i) {

            var textOpacity = opacity == .1 ? 1 : .25;

            //fade the text
            d3.select(this.parentNode)
              .selectAll("text")
              .transition()
              .style("opacity", textOpacity);


            svg.selectAll(".chord path")
              .filter(function (d) {
                return d.source.index != i && d.target.index != i;
              })
              .transition()
              .style("opacity", opacity);
          };
        }
      },


      onRender: function () {

        this.initializeGraph();

      }

    })


    var DetailStyleModel = Backbone.Model.extend({

      defaults: function () {
        return {
          highlightColor: "orange",
          linkStrength: .1,
          linkDistance: 35,
          width: 100,
          height: 100
        }
      }


    });

    var DetailModel = Backbone.Model.extend({

      defaults: {
        links: [],
        nodes: [],
        scales: {}
      }

    })


    var DetailNetworkView = Marionette.ItemView.extend({

      initialize: function (options) {

        this.styleModel = new DetailStyleModel();
      },

      template: DetailTemplate,

      onRender: function () {

        this.computeScales();

        this.drawGraph();

      },

      /*
       * every time the graph is shown,
       * sweep over all nodes and update highlights
       * */

      onShow: function () {

        var col = Marionette.getOption(this, "chosenNamesCollection");

        this.$(".detail-node").each(function (i, el) {

          if (col.get(el.textContent)) {

            $(el).addClass("selected-node");

          }
          else {

            $(el).removeClass("selected-node");

          }
        })

      },

      className: function () {
        return Marionette.getOption(this, "className")

      },

      serializeData: function () {

        var data = {};

        data.groupNumber = Marionette.getOption(this, "groupNumber");

        return data

      },

      computeScales: function () {

        var nodeWeights, linkWeights, scalesDict;

        nodeWeights = _.pluck(this.model.get("nodes"), "nodeWeight");

        linkWeights = _.pluck(this.model.get("links"), "weight");

        scalesDict = this.model.get("scales");

        scalesDict.fontScale = d3.scale.linear().domain([d3.min(nodeWeights), d3.max(nodeWeights)]).range([3, 7]);
        scalesDict.lineScale = d3.scale.linear().domain([d3.min(linkWeights), d3.max(linkWeights)]).range([.5, 2]);

      },

      events: {
        "click .detail-node": "toggleNames",
        "click .update-all": "toggleAllNames",
        "click #zoom-in": "zoomIn",
        "click #zoom-out": "zoomOut"

      },

      onNodeRemoved: function () {

        var name = arguments[0][0].attributes.name.trim();

        this.$(".detail-node").each(function () {
          if (this.textContent.trim() === name.trim()) {
            d3.select(this).classed("selected-node", false);

          }
        })
      },

      toggleNames: function (e) {

        var name = e.currentTarget.textContent;

        var d3Target = d3.select(e.currentTarget);

        d3Target.classed("selected-node", !d3Target.classed("selected-node"));

        this.trigger("name:toggle", name);

      },

      toggleAllNames: function () {

        var names = [];

        var action = this.$(".update-all").hasClass("add-all") ? "add" : "remove";

        if (action === "add") {
          this.$(".detail-node").each(function () {
            d3.select(this).classed("selected-node", true);
            names.push(this.textContent);

          });

        }
        else {
          this.$(".detail-node").each(function () {
            d3.select(this).classed("selected-node", false);
            names.push(this.textContent);

          });

        }

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

      },

      drawGraph: function () {

        var svg, width, height, g1, g2, z;

        var scalesDict;

        var node, link;

        var numTicks;

        var self = this;

        var groupColor = this.model.get("fill");

        width = this.styleModel.get("width");
        height = this.styleModel.get("height");

        scalesDict = this.model.get("scales")

        svg = d3.select(this.$("svg")[0]);

        this.model.set("svg", svg);

        var force = d3.layout.force()
          .size([width, height])
          .linkDistance(this.styleModel.get("linkDistance"));

        svg.attr("width", width)
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
          .style("pointer-events", "all")

        this.model.set("g2", g2);

        force.nodes(this.model.get("nodes"))
          .links(this.model.get("links"))
          .charge(-500)
          .start();

        this.model.set("force", force);

        link = g2.selectAll(".detail-link")
          .data(this.model.get("links"))
          .enter().append("line")
          .style("display", "none")
          .classed("detail-link", true)
          .attr("class", "detail-link")
          .style({"stroke-width": function (d) {
            return scalesDict.lineScale(d.weight);
          }});


        if (this.renderNodes){

          //so paper network can render nodes its own way
          node = this.renderNodes({g2 : g2, scalesDict : scalesDict});

        }
        else {

          node = g2.selectAll(".detail-node")
            .data(this.model.get("nodes"))
            .enter()
            .append("text")
            .text(function (d) {
              return d.nodeName
            })
            .attr("font-size", function (d) {
              return scalesDict.fontScale(d.nodeWeight) + "px"
            })
            .classed({"detail-node": true, "selected-node": function (d) {
              return d.currentlySelected ? true : false
            }});

        }

        node.on("mouseover", function(node){


          g2.selectAll(".detail-link").each(function(link,linkIndex){

            if (link.target == node || link.source == node){
              d3.select(this)
                .style({display : "block", stroke: groupColor});
            }

          })

        })
          .on("mouseout", function(node){


            g2.selectAll(".detail-link").each(function(link,linkIndex){

              if (link.target == node || link.source == node){
                d3.select(this)
                  .style({display : "none"});
                ;
              }
            })

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

          var xList = [], yList = [];

          this.$(".detail-node").each(function () {

            xList.push(this.__data__.x);
            yList.push(this.__data__.y);
          })

          maxX = _.max(xList);
          minX = _.min(xList);

          maxY = _.max(yList);
          minY = _.min(yList);

          largestDistance = _.max([(maxX - minX), (maxY - minY)]);

          center = [(maxX + minX) / 2, (maxY + minY) / 2];

          var newScale = 100 / largestDistance;

          var oldScale = z.scale();

          var translateX = -newScale * ((center[0]) / oldScale) + width / 2;
          var translateY = -newScale * ((center[1]) / oldScale) + height / 2;

          z.scale(newScale);
          z.translate([translateX, translateY]);

          g2.transition().duration(3000)
            .attr('transform', 'translate(' + translateX + ', ' + translateY + ')' + 'scale(' + z.scale() + ')')

          self.model.set("z", z);
          self.model.set("g2", g2);

          setTimeout(zoomCallback, 3000)


          //finally, dealing with zoom in an iife
          //now initializing all zoom behaviors, including button zoom
          //will be called after 40 ticks
          function zoomCallback() {

            var drag = d3.behavior.drag()
              .on("drag", function (d, i) {
                var x = this.transform.animVal[0].matrix.e + d3.event.dx;
                var y = this.transform.animVal[0].matrix.f + d3.event.dy;
                d3.select(this).attr("transform", function (d, i) {
                  return "translate(" + [ x, y ] + ")"
                    + " scale(" + z.scale() + ")";

                });

                z.translate([x, y]);


              });

            g2.call(drag);

          }

        }

      },

      zoomIn: function () {

        var z = this.model.get("z");
        var g2 = this.model.get("g2");
        var width = this.styleModel.get("width");
        var height = this.styleModel.get("height");

        var oldscale = z.scale();
        var scale = z.scale() + .5;
        if (scale > 10) {
          return
        }
        var x = z.translate()[0]
        var y = z.translate()[1]
        // This is not intuitive. The next two lines are the most important part.
        // Basically, you need to calculate the offset of the current
        // center by doing ((w/2-x)/oldscale) and take that number and calculate the offset
        // based on the new scale (need to look up svg transforms to understand this because it's complicated)
        var translateX = -scale * ((width / 2 - x) / oldscale) + width / 2;
        var translateY = -scale * ((height / 2 - y) / oldscale) + height / 2;

        z.scale(scale);
        z.translate([translateX, translateY]);

        g2
          .transition()
          .duration(1000)
          .attr('transform', 'translate(' + translateX + ', ' + translateY + ')' + 'scale(' + z.scale() + ')')

        this.model.set("z", z);
        this.model.set("g2", g2)

      },

      zoomOut: function () {

        var z = this.model.get("z");
        var g2 = this.model.get("g2");
        var width = this.styleModel.get("width");
        var height = this.styleModel.get("height");

        var oldScale, newScale;
        oldScale = z.scale();

        if (oldScale >= 1) {
          newScale = oldScale - .5;
        }
        else {

          newScale = oldScale - .2;
        }
        if (newScale < .2) {
          return
        }
        var x = z.translate()[0]
        var y = z.translate()[1]
        var translateX = -newScale * ((width / 2 - x) / oldScale) + width / 2;
        var translateY = -newScale * ((height / 2 - y) / oldScale) + height / 2;
        z.scale(newScale);
        z.translate([translateX, translateY]);

        g2.transition().duration(1000)
          .attr('transform', 'translate(' + translateX + ', ' + translateY + ')' + 'scale(' + z.scale() + ')')

        this.model.set("z", z);
        this.model.set("g2", g2)

      },

      onClose: function () {

        this.model.get("force").stop();

        this.$("svg").empty();

        this.model.get("g2").on(null);

      }

    });


    var NetworkWidget = BaseWidget.extend({


      initialize: function (options) {


        if (!options.endpoint) {

          throw new Error("widget was not configured with an endpoint");
        }

        if (options.broadcastFilteredQuery) {
          this.broadcastFilteredQuery = options.broadcastFilteredQuery;
        }

        this.model = new NetworkModel();

        this.view = new ContainerView({
          model: this.model,
          networkType: Marionette.getOption(this, "networkType"),
          helpText: Marionette.getOption(this, "helpText"),
          summaryMixin: options.summaryMixin,
          detailMixin: options.detailMixin

        });

        this.listenTo(this.view, "filterSearch", this.broadcastFilteredQuery);

        this.listenTo(this.view, "close", this.broadcastClose);

      },

      activate: function (beehive) {

        _.bindAll(this, "setCurrentQuery", "processResponse", "conditionalResetWidget");

        this.pubsub = beehive.Services.get('PubSub');

        //custom dispatchRequest function goes here
        this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.setCurrentQuery);

        //custom handleResponse function goes here
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);

//        this.pubsub.subscribe(this.pubsub.NAVIGATE, this.conditionalResetWidget);

      },

      conditionalResetWidget: function (event) {
//
//        //how to check that widget is in the dom?
//
//          if (event !== "show-author-network"){
//            this.resetWidget();
//          }

      },

      resetWidget: function () {

        //close graphView
        if (this.view.graphView) {

          this.view.graphView.close();

          this.view.graphView.remove();

          this.view.chosenNamesCollection.reset(null);

        }

        //reset model
        this.model.set(_.result(this.model, "defaults"), {silent: true});

      },

      //fetch data
      onShow: function () {

        var request = new ApiRequest({

          target: Marionette.getOption(this, "endpoint"),
          query: this.getCurrentQuery()
        });

        this.startWidgetLoad();

        this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, request);

      },

      processResponse: function (data) {

        data = data.toJSON();

        this.model.set({fullGraph: data["fullGraph"], summaryData: data["summaryGraph"]});

      },


      broadcastFilteredQuery: function (names) {

        var updater = new ApiQueryUpdater("fq");

        if (!names.length) {
          return

        }

        names = _.map(names, function (n) {
          return updater.quote(n)
        }, this);

        names = "author:(" + names.join(" OR ") + ")";

        newQuery = this.getCurrentQuery().clone();

        updater.updateQuery(newQuery, "fq", "limit", names);

        this.resetWidget();

        this.pubsub.publish(this.pubsub.START_SEARCH, newQuery);

      },

      broadcastClose: function () {

        this.resetWidget();

        this.pubsub.publish(this.pubsub.NAVIGATE, "results-page");

      },

      startWidgetLoad: function () {

        if (!this.callbacksAdded) {

          var removeLoadingView = function () {
            this.view.$(".s-loading").remove();
          };

          this.listenTo(this.model, "change:fullData", removeLoadingView);

          this.callbacksAdded = true;

        }

        if (this.view.$el.find(".s-loading").length === 0) {
          this.view.$el.empty().append(this.loadingTemplate());
        }

      }

    })

    return NetworkWidget

  })