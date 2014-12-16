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


    var ContainerView = Marionette.ItemView.extend({

      initialize: function (options) {

        if (options.showDetailGraphView) {

          this.showDetailGraphView = options.showDetailGraphView;

        }

        this.chosenNamesCollection = new ChosenNamesCollection();
        this.chosenNamesView = new ChosenNamesView({collection: this.chosenNamesCollection, networkType: Marionette.getOption(this, "networkType") });

        this.listenTo(this.model, "change:currentGroup", this.showDetailGraphView);

      },

      adsColors: ["#5683e0", "#7ab889", "#ffb639", "#ed5e5b", "#ce5cff", "#1c459b", "#757575", "#b3b3b3", "#58b6d5"],

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

          this.model.get("currentlySelectedGroupIds").push(this.model.get("currentGroup"));

          _.each(names, function (n) {

            this.chosenNamesCollection.add({name: n}, {silent: true})

          }, this);

          this.chosenNamesCollection.trigger("augment")

        }

        else {

         this.model.set("currentlySelectedGroupIds",  _.filter(this.model.get("currentlySelectedGroupIds"), function(id){

            return (id !== this.model.get("currentGroup"));
          }, this));

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

        "change:data": "render"

      },

      signalSearchFiltered: function () {

        var names = _.pluck(this.chosenNamesCollection.toJSON(), "name");

        this.trigger("filterSearch", names);


      },

      signalCloseWidget: function () {

        this.trigger("close")

      },

      extractGraphData: function (group, limit) {

        var fullGraph = this.model.get("data").fullGraph;

        var nodes = [], indexDict = {}, indexReference = {}, links = [],
          filteredNodes = [], filteredIndexDict = {};

        _.each(fullGraph.nodes, function (n, i) {
          if (n.group === group && n.nodeName) {
            var newIndex = nodes.push(_.clone(n));
            newIndex -= 1;
            //in case we have to limit
            indexReference[newIndex] = i;

            indexDict[i] = newIndex;
          }
        });

        if (limit && limit < nodes.length){

          //an extra step to limit to only the top nodes

          indexDict = {};

          //find nodeWeight limit
         var minNodeWeight =  _.map(nodes, function(n){
            return n.nodeWeight
          })
         minNodeWeight =  _.sortBy(minNodeWeight, function(n){return n })

          minNodeWeight = minNodeWeight.reverse()[limit-1];

          _.each(nodes, function (n, i) {
            if (n.nodeWeight >= minNodeWeight) {

              var newIndex = filteredNodes.push(n);
              newIndex -= 1;
              var oldIndex = indexReference[i]
              indexDict[oldIndex] = newIndex;
            }

          });

          nodes = filteredNodes;

        }

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


      showDetailGraphView : function(){


        var groupId = this.model.get("currentGroup");

        var groupIndex;

        _.each(this.model.get("data").summaryGraph.nodes, function(n, i){

          if (n.id == groupId)
          groupIndex = i;

       });


      //get data
        var summaryData = _.filter(this.model.get("data").summaryGraph.nodes, function(n,i){

          return ( n.id == groupId)

        })[0];

        if (summaryData.connector){

          //just render the connector template and return
          this.$(".info-region").empty().append(connectorTemplate({nodeName : _.keys(summaryData.nodeName)[0]}));
          return

        }

        var n = 20;

        var detailGraph = this.extractGraphData(groupId, n);

        var detailModel = new Backbone.Model();

        detailModel.set("nodes", detailGraph.nodes);

        detailModel.set("links", detailGraph.links);

        detailModel.set("scales", {});

        detailModel.set("fill", this.adsColors[groupIndex]);

        if (this.model.get("currentlySelectedGroupIds").indexOf(groupId)!== -1){

          detailModel.set("currentlySelected", true);

        }
        else {

          detailModel.set("currentlySelected", false);

        }

      //render view
        var detailView = new DetailGraphView({model : detailModel});

        this.$(".info-region").empty().append(detailView.render().el);

        this.listenTo(detailView, "name:toggle", this.updateSingleName);
        this.listenTo(detailView, "names:toggle", this.updateGroupOfNames);

        this.listenTo(detailView, "close", function () {
          this.stopListening(detailView);
        });

      },

      //choose sub view once container is rendered
      onRender: function () {

        self = this;

        var data = this.model.get("data")

        //not enough data to make a graph
        if (_.isEmpty(data) || !data.fullGraph || !data.fullGraph.nodes || data.fullGraph.nodes.length < 10 ) {

          this.$el.empty().append(notEnoughDataTemplate())
          return

        }

        var model = new GraphModel({
            fullGraph : data.fullGraph,
            summaryGraph : data.summaryGraph

          });

        if (model.get("summaryGraph")){

          this.graphView = new SummaryGraphView({model: model,
            chosenNamesCollection: this.chosenNamesCollection,
            summaryGraph : true
          });

          _.extend(this.graphView, Marionette.getOption(this, "graphMixin"));

          //allowing events to be passed in the mixin, there must be a better way though
          this.graphView.delegateEvents();

          _.extend(this.graphView, {adsColors : this.adsColors});

          this.ui.selectedContainer.append(this.chosenNamesView.render().el);

          this.$(".graph-region").append(this.graphView.render().el);

          this.listenTo(this.graphView, "group:selected", function(currentGroup){

              this.model.set("currentGroup", currentGroup);

            });

        }

        else {

          //mimicking what the detail view gets from the summary view
          model.set("scales", {});
          model.set("nodes", model.get("fullGraph").nodes);
          model.set("links", model.get("fullGraph").links);


          this.graphView = new DetailGraphView({model: model,
            chosenNamesCollection: this.chosenNamesCollection,
            summaryGraph : true
          });

          //listening to selection events
          this.listenTo(this.graphView, "name:toggle", this.updateSingleName);
          this.listenTo(this.graphView, "names:toggle", this.updateGroupOfNames);

          this.listenTo(this.graphView, "close", function () {
            this.stopListening(this.graphView);
          });

          //hide the two panel structure so it's just a single div
          this.$(".info-region").addClass("hidden");

          this.ui.selectedContainer.append(this.chosenNamesView.render().el);

          this.$(".small-graph-region").append(this.graphView.render().el);

        }

        //initialize popover
        this.$(".icon-help").popover({trigger: "hover", placement: "bottom", html: true});

      }

    });



    var GraphModel = Backbone.Model.extend({

      defaults: function () {
        return  {
          //the summary graph data
          summaryData: {},
          //the full graph data
          fullGraph: {}
        }
      }

    })


    var SummaryGraphView = Marionette.ItemView.extend({

      initialize: function (options) {

        this.chosenNamesCollection = options.chosenNamesCollection;

        this.listenTo(this.model, "change:currentGroup", function(){
          this.trigger("groupChanged:", groupIndex)
        });

        this.on("summaryNode:mouseenter", this.showMoreData);

      },


      className: "summary-graph-container s-summary-graph-container",

      template: SummaryTemplate,

      onRender: function () {

        this.drawSummaryGraph();

      },


      drawSummaryGraph: function () {

        var d3Svg = d3.select(this.$("svg.summary-chart")[0]);

        var graphData = this.model.get("summaryGraph");

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
          .on("mouseover", fade("mouseenter"))
          .on("mouseout", fade("mouseleave"))
           //trigger group select events
          .on("click", function(d, i, j){

            var groupId = graphData.nodes[d.index].id;
            self.trigger("group:selected", groupId);

        });


        var ticks = svg.append("g").selectAll("g")
          .data(chord.groups)
          .enter().append("g").selectAll("g")
          .data(groupTicks)
          .enter().append("g")
          .classed("groupLabel", true)
          .attr("transform", function (d, i, j) {
            return "rotate(" + (d.angle * 180 / Math.PI - 90) + ")" + "translate(" + calculateLabelPosition(_.extend(d, {
              index: j
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
            .classed("text-labels", true)
            .attr("font-size", function (d, i, j) {
              var size = d[1]
              return fontScale(size) + "px";
            })
            .text(function (d, i) {
              return d[0]
            })

        }

        //show and fade labels
        text.on("mouseover", fadeText("mouseenter"))
          .on("mouseout", fadeText("mouseleave"));

        //trigger group select events
        text.on("click", function(){

          var groupId =  graphData.nodes[this.parentNode.__data__.index].id;

          self.trigger("group:selected", groupId);

        });

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
        function fade(event) {
          return function (g, i) {

            var textOpacity = event == "mouseenter" ? 1 : .25;
            var otherChordsOpacity = event == "mouseenter" ? .1 : 1;

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
              .style("opacity", otherChordsOpacity);
          };
        }

        // Returns an event handler for fading a given chord group.
        //have to add this to the text or else it interferes with mouseover
        function fadeText(event) {
          return function (g, j, i) {

            var textOpacity = event == "mouseenter" ? 1 : .25;

            var otherChordsOpacity = event == "mouseenter" ? .1 : 1;

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
              .style("opacity", otherChordsOpacity);
          };
        }
      }

    })

    var DetailGraphView = Marionette.ItemView.extend({

      template: DetailTemplate,

      onRender: function () {

        this.computeScales();

        this.drawDetailGraph();

      },

      className: function () {
        return Marionette.getOption(this, "className")

      },

      computeScales: function () {

        var nodeWeights, linkWeights, scalesDict;

        nodeWeights = _.pluck(this.model.get("nodes"), "nodeWeight");

        linkWeights = _.pluck(this.model.get("links"), "weight");

        scalesDict = this.model.get("scales");

        scalesDict.fontScale = d3.scale.linear().domain([d3.min(nodeWeights), d3.max(nodeWeights)]).range([1.8, 4.5]);
        scalesDict.lineScale = d3.scale.linear().domain([d3.min(linkWeights), d3.max(linkWeights)]).range([.1, 1]);

      },

      drawDetailGraph: function () {

        var svg, width, height, g2;

        var scalesDict;

        var node, link;

        var groupColor = this.model.get("fill") || "gray";

        /* STYLE variables */

        var   linkDistance = 20,
          width = 100,
          height = 100;

         /* end style */


        scalesDict = this.model.get("scales");

        svg = d3.select(this.$("svg")[0]);

        this.model.set("svg", svg);

        var force = d3.layout.force()
          .size([width, height])
          .linkDistance(linkDistance)
          .charge(-40);

        svg.attr("width", width)
          .attr("height", height);

        //container for network
        //need two gs because of weird panning requirement

        g2 = svg.append("g");

        g2.append("rect")
          .attr("width", width)
          .attr("height", height)
          .style("fill", "none")
          .style("pointer-events", "all")

        force.nodes(this.model.get("nodes"))
          .links(this.model.get("links"))
          .start();

        this.model.set("force", force);

        link = g2.selectAll(".detail-link")
          .data(this.model.get("links"))
          .enter().append("line")
          .classed("detail-link", true)
          .attr("class", "detail-link")
          .style("stroke-width", function (d) {
            return scalesDict.lineScale(d.weight);
          })
          .style("stroke" , groupColor);


        if (this.renderNodes) {

          //so paper network can render nodes its own way
          node = this.renderNodes({g2: g2, scalesDict: scalesDict});

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

        node.on("mouseover", function (node) {


          g2.selectAll(".detail-link").each(function (link, linkIndex) {

            if (link.target == node || link.source == node) {
              d3.select(this)
                .style({opacity :.8});
            }

          })

        })
          .on("mouseout", function (node) {

            g2.selectAll(".detail-link").each(function (link, linkIndex) {

              if (link.target == node || link.source == node) {
                d3.select(this)
                  .style({opacity: .15});
                ;
              }
            })

          });

        force.on("tick", function () {

          node.attr("x", function (d) {
            return d.x =  Math.max(15, Math.min(width - 3, d.x));
          })
            .attr("y", function (d) {
              return d.y  = Math.max(15, Math.min(height - 3, d.y));
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

      },

          events: {
        "click .detail-node": "toggleNames",
        "click .update-all": "toggleAllNames"

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

      onClose: function () {

        this.$("svg").empty();

      }

    });


    var NetworkModel = Backbone.Model.extend({

      defaults: function () {
        return  {
          currentGroup : undefined,
          data : undefined,
          currentlySelectedGroupIds : []
        }
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
          graphMixin: options.graphMixin,
          showDetailGraphView: options.showDetailGraphView

        });

        this.listenTo(this.view, "filterSearch", this.broadcastFilteredQuery);

        this.listenTo(this.view, "close", this.broadcastClose);

      },

      activate: function (beehive) {

        _.bindAll(this, "setCurrentQuery", "processResponse");

        this.pubsub = beehive.Services.get('PubSub');

        //custom dispatchRequest function goes here
        this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.setCurrentQuery);

        //custom handleResponse function goes here
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);

      },

      resetWidget: function () {

        //close graphView
        if (this.view && this.view.graphView) {

          this.view.graphView.$el.empty();
          this.view.graphView.stopListening();

        }

          this.view.chosenNamesCollection.reset(null);

        //reset model
        this.model.set(_.result(this.model, "defaults"), {silent: true});

      },

      destroyWidget : function(){

        //close graphView
        if (this.view.graphView) {

          this.view.graphView.close();

        }

        this.view.chosenNamesView.close();

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

        //to force change even if the data is the same

        this.model.set("data", {},  {silent : true});

        this.model.set({data : data});
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

        newQuery.unlock();

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