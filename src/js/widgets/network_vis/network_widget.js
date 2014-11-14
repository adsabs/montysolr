define([
  "marionette",
  "d3",
  "js/widgets/base/base_widget",
  "hbs!./templates/container-template",
  'hbs!./templates/detail-graph-container-template',
  'hbs!./templates/summary-graph-container-template',
  'hbs!./templates/selected-items-template',
  'hbs!./templates/not-enough-data-template',
  "bootstrap"

  ],
  function (
    Marionette,
    d3,
    BaseWidget,
    ContainerTemplate,
    DetailTemplate,
    SummaryTemplate,
    selectedItemsTemplate,
    notEnoughDataTemplate
    ) {


  var ChosenNamesModel = Backbone.Model.extend({

    defaults: {
      name: undefined
    },

    idAttribute: "name"


  })

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

  })

  var EmptyItemsView = Marionette.ItemView.extend({

    tagName : "li",

    template: function (data) {
      return "Click on a node in the detail view to add it to this list. You can then filter your current search to include only the selected items."
    }


  })

  var ChosenNamesView = Marionette.CompositeView.extend({

    template : selectedItemsTemplate,

    className : "btn-group",

    itemView: ChosenNamesItemView,

    emptyView : EmptyItemsView,

    itemViewContainer : ".dropdown-menu",

    events : {
      "click .name-collection-container" : function(e){
        e.stopImmediatePropagation();
        e.stopPropagation();

      }
    },

    collectionEvents  : {
      "augment" : "render",
      "remove" : "render"
    },

    serializeData : function(){
      return {
        networkType : Marionette.getOption(this, "networkType"),
        selectedNum : this.collection.length

      }
    }

  })


  //these defaults won't typically change
  var SummaryStyleModel = Backbone.Model.extend({

    defaults: function () {
      return {
        highlightColor: "orange",
        linkDistance: 35,
        charge: -150,
        width: 100,
        height: 100
      }

    }
  })

  var NetworkModel = Backbone.Model.extend({

    defaults:function(){
      return  {
        //the summary graph data
        summaryData: {},
        //the full graph data
        fullData: {},
        scales: {},
        nodes: [],
          links: [],
        currentGroup: undefined,
        svg : undefined

      }
    }

  })

  //this stuff changes


 var ContainerView = Marionette.ItemView.extend({

   initialize : function(){

     this.chosenNamesCollection = new ChosenNamesCollection();
     this.chosenNamesView = new ChosenNamesView({collection: this.chosenNamesCollection, networkType : Marionette.getOption(this, "networkType") });

   },

   className: function () {

     var networkType = Marionette.getOption(this, "networkType")
     return networkType + "-graph s-" + networkType + "-graph"

   },

   serializeData : function(){

     var n = Marionette.getOption(this, "networkType");

     n =  n[0].toUpperCase() + n.slice(1)

     return {networkType : n}
   },

   template  : ContainerTemplate,


   ui : {
     selectedContainer : ".selected-items-container"

   },


   updateSingleName: function (name) {


     if (this.chosenNamesCollection.get(name)) {
       //remove it

       this.chosenNamesCollection.remove(name);

     }
     else {
       this.chosenNamesCollection.add({name: name})
       this.chosenNamesCollection.trigger("augment")

     }

   },

   updateGroupOfNames: function (names, action) {

     if (action === "add") {

       _.each(names, function (n) {

         this.chosenNamesCollection.add({name: n}, {silent : true})

       }, this)

       this.chosenNamesCollection.trigger("augment")

     }

     else {

       _.each(names, function (n) {

         this.chosenNamesCollection.remove(this.chosenNamesCollection.get(n))

       }, this)

     }

   },

   modelEvents : {

     "change:fullData" : "render"

   },


   onRender : function(){

     var newGraphView = false;


     if (_.keys(this.model.get("summaryData")).length){

       this.graphView = new SummaryGraphView({model : this.model, chosenNamesCollection : this.chosenNamesCollection})

       this.ui.selectedContainer.append(this.chosenNamesView.render().el);

       this.$(".graph-region").append(this.graphView.render().el);

       newGraphView = true;

     }

     else if (!this.model.get("fullData") || !this.model.get("fullData").nodes.length ) {

       this.$el.empty().append(notEnoughDataTemplate())

     }
     else if (this.model.get("fullData").nodes.length > 1 ){

       this.model.set("nodes", this.model.get("fullData").nodes);

       this.model.set("links", this.model.get("fullData").links);

       this.graphView = new DetailNetworkView({model : this.model})

       this.ui.selectedContainer.append(this.chosenNamesView.render().el);

       this.$(".graph-region").append(this.graphView.render().el);

       newGraphView = true;

     }

     if (newGraphView){

       this.listenTo(this.graphView, "name:toggle", this.updateSingleName);
       this.listenTo(this.graphView, "names:toggle", this.updateGroupOfNames)

       this.listenTo(this.graphView, "close", function(){
         this.stopListening(this.graphView);
         })

     }

   }


 })


  var SummaryGraphView = Marionette.ItemView.extend({


    initialize: function (options) {

      this.styleModel = new SummaryStyleModel;

      this.chosenNamesCollection = options.chosenNamesCollection;

      this.listenTo(this.model, "change:currentGroup", this.showDetailNetwork);

      this.listenTo(this.chosenNamesCollection, "remove", function () {
        if (this.detailNetworkView) {
          this.detailNetworkView.triggerMethod("node:removed", arguments)
        }
      })

      this.detailViews = new Backbone.ChildViewContainer();


    },

    className : "summary-graph-container s-summary-graph-container",


    ui: {

      detailContainer: ".detail-view .vis-container"

    },

    template: SummaryTemplate,

    events: {
      "click .summary-node-group": "setNodeDetail",
      "mouseenter .summary-node-group": "highlightNode",
      "mouseleave .summary-node-group": "unhighlightNode"
    },

    highlightNode: function (e) {

      var $kids = $(e.currentTarget).children();

      var $circle = $kids.eq(0);

      var $textNodes = [$kids.eq(1), $kids.eq(2)];

      $circle.addClass("s-hover-circle");

      $textNodes.addClass("s-hover-text");

    },

    unhighlightNode: function (e) {

      var $kids = $(e.currentTarget).children();

      var $circle = $kids.eq(0);

      var $textNodes = [$kids.eq(1), $kids.eq(2)];

      $circle.removeClass("s-hover-circle");

      $textNodes.removeClass("s-hover-text");

    },

    setNodeDetail: function (e) {
      e.stopPropagation();

      if ($(e.currentTarget).children().eq(0).hasClass("connector-node")) {
        return
      }
      this.model.set("currentGroup", e.currentTarget)

    },

    showDetailNetwork: function (model, targetGroupNode) {

      var group;

      group = targetGroupNode.__data__.id;


      if (this.detailNetworkView) {

        this.detailNetworkView.remove();

      }

      this.ui.detailContainer.hide();

      //remove all highlighting from group circles
      //jquery removeClass isn't working here

      this.$(".target-group").each(function () {

        $(this).removeClass("target-group")
      })

      if (this.detailViews.findByCustom(group + 1)){

        //adding 1 because if a group is zero, detailViews won't index it properly (?)

        this.detailNetworkView = this.detailViews.findByCustom(group + 1);

        this.detailNetworkView.delegateEvents();

      }

      else {

        //sticks graph data into model
        var data = this.extractGraphData(group)

        //now create a new view
        this.detailNetworkView = new DetailNetworkView({model: new DetailModel(data), chosenNamesCollection : this.chosenNamesCollection , groupNumber: group});

        this.listenTo(this.detailNetworkView, "names:toggle", function(n, a){ this.trigger("names:toggle", n, a)})
        this.listenTo(this.detailNetworkView, "name:toggle", function(n,a ){ this.trigger("name:toggle", n,a )})


        //need to stopListening to the detail network view or else there will be huge memory leaks!
        // this is because it will be held in this._listeningTo
        this.listenTo(this.detailNetworkView, "close", function () {
          this.stopListening(this.detailNetworkView)

        })

        this.detailNetworkView.render();

        this.detailViews.add(this.detailNetworkView, group + 1)

      }

      this.ui.detailContainer.empty().append(this.detailNetworkView.el);

      this.detailNetworkView.triggerMethod("show");

      //add highlight to node
      $(targetGroupNode).children().eq(0).addClass("target-group")

      this.ui.detailContainer
        .fadeIn()

    },

    extractGraphData: function (group) {

      var fullGraph = this.model.get("fullData");

      var nodes = [], indexDict = {}, links = [];

      _.each(fullGraph.nodes, function (n, i) {
        if (n.group === group) {
          var newIndex = nodes.push(_.clone(n));
          newIndex -= 1
          indexDict[i] = newIndex;
        }
      })

      var keys = _.map(_.keys(indexDict), function (k) {
        return parseInt(k)
      });

      _.each(fullGraph.links, function (l, i) {
        if (keys.indexOf(l.source) !== -1 && keys.indexOf(l.target) !== -1) {
          //re-assign the indexes to the links
          var newLink = _.clone(l)
          newLink.source = indexDict[l.source];
          newLink.target = indexDict[l.target];
          links.push(newLink)
        }
      })


      return {"nodes": nodes, "links": links};

    },

    computeScales: function () {

      var currentData, scalesDict, groupWeights, linkValues;

      currentData = this.model.get("summaryData");

      scalesDict = this.model.get("scales")

      groupWeights = _.pluck(currentData.nodes, "size");

      linkValues = _.pluck(currentData.links, "weight");

      scalesDict.lineScale = d3.scale.linear().domain([d3.min(linkValues), d3.max(linkValues)]).range([2, 10]);
      scalesDict.linkScale = d3.scale.linear().domain([d3.min(linkValues), d3.max(linkValues)]).range([.1, .3]);
      scalesDict.radiusScale = d3.scale.linear().domain([d3.min(groupWeights), d3.max(groupWeights)]).range([5, 13]);

    },

    initializeGraph: function () {

      var force, linkDistance, charge;

      var svg, g1, g2, link, node;

      var width, height;

      var scalesDict, lineScale, nodeScale, radiusScale, linkScale;

      var linksData, nodesData;

      var nodeColor, z;

      linkDistance = this.styleModel.get("linkDistance");

      charge = this.styleModel.get("charge");

      gravity = this.styleModel.get("gravity");

      linksData = this.model.get("summaryData").links;

      nodesData = this.model.get("summaryData").nodes;

      svg = d3.select(this.$("svg")[0]);

      scalesDict = this.model.get("scales");

      lineScale = scalesDict["lineScale"];

      nodeScale = scalesDict["nodeScale"];

      linkScale = scalesDict["linkScale"]

      radiusScale = scalesDict["radiusScale"];

      nodeColor = this.styleModel.get("nodeColor");

      width = this.styleModel.get("width");

      height = this.styleModel.get("height");

      force = d3.layout
        .force()
        .size([width, height])
        .distance(function (d) {
          if (d.source.connector || d.target.connector) {
            return 20
          }
          else {
            return 50
          }
        })
        .charge(charge);

      force
        .nodes(nodesData)
        .links(linksData)
        .start();

      this.model.set("force", force)

      //need two gs because of weird panning requirement
      g1 = svg.append("g")

      this.model.set("g1", g1)

      g2 = g1.append("g")

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

      node.append("circle")
        .classed({"network-node": true, "connector-node": function (d) {

          return d.connector ? true : false

        }  })
        .attr("r", function (d) {
          return d.size ? radiusScale(d.size) : 10
        })
        .each(function (d) {

          var radius = parseInt(this.parentElement.childNodes[0].attributes.r.value);

          var d3Group = d3.select(this.parentElement);

          if (d.connector) {

            d3Group.append("rect")
              .classed("central-author-background", true)
              .attr({width: 30, height: 8, x : -15, y: -5 })
            d3Group.append("text")
              .text(d.nodeName[0])
              .classed("group-author-name", true)

            return
          }

          //if there is just one entry, stick it in the middle of the circle and return
          else if (d.authorCount === 1){

            d3Group.append("text")
              .text(d.nodeName[0])
              .classed("group-author-name", true)

            return
          }

          _.each(d.nodeName, function (n, i) {
            //show only top 4 authors

            if (i > 3) {
              return
            }
            d3Group.append("text")
              .text(n)
              .classed("group-author-name", true)
              .attr("y", function () {
                return -radius / 2 + 4 * i
              })

          })

          if (d.nodeName.length < d.authorCount) {

            var more = d.authorCount - _.min([d.nodeName.length, 4]);

            d3Group.append("text")
              .classed("group-more", true)
              .text(" + " + more + " more")
              .attr("y", function () {
                return -radius / 2 + 4 * _.min([d.nodeName.length, 4])
              })
          }

        })

      force.on("tick", function () {


        node.attr("x", function (d) {
          return d.x
        })
          .attr("y", function (d) {
            return d.y
          });

        node.attr("transform", function (d) {

          return "translate(" + d.x + "," + d.y + ")"
        })

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


    },

    onRender: function () {

      this.computeScales();
      this.initializeGraph();

    },

    onClose : function(){

      this.model.get("force").stop();

      this.model.get("g1").on(null);

    }



  })


  var DetailStyleModel = Backbone.Model.extend({

    defaults: function () {
      return {
        highlightColor: "orange",
        linkStrength: .1,
        width: 100,
        height: 100
      }
    }


  });

  var DetailModel = Backbone.Model.extend({

    defaults: {
      links: [],
      nodes: [],
      scales: {},
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

      onShow : function(){

        var col =  Marionette.getOption(this, "chosenNamesCollection");

        this.$(".detail-node").each(function(i, el){

          if (col.get(el.textContent)){

            $(el).addClass("selected-node")

          }
          else {

            $(el).removeClass("selected-node")

          }
        })


    },

    className: function() {
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

      scalesDict.fontScale = d3.scale.linear().domain([d3.min(nodeWeights), d3.max(nodeWeights)]).range([2, 4]);
      scalesDict.lineScale = d3.scale.linear().domain([d3.min(linkWeights), d3.max(linkWeights)]).range([.5, 2]);
      scalesDict.colorScale = d3.scale.ordinal().range([
        "#1f77b4",
        "#2ca02c",
        "#d62728",
        "#9467bd",
        "#8c564b",
        "#e377c2",
        "#7f7f7f",
        "#bcbd22",
        "#17becf"]);
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
          $(this).removeClass("selected-node")

        }
      })
    },

    toggleNames: function (e) {

      var name = e.currentTarget.textContent;

      $(e.currentTarget).toggleClass("selected-node");

      this.trigger("name:toggle", name);

    },

    toggleAllNames: function () {

      var names = [];

      var action = this.$(".update-all").hasClass("add-all") ? "add" : "remove";

      if (action === "add") {
        this.$(".detail-node").each(function () {
          $(this).addClass("selected-node");
          names.push(this.textContent);

        });

      }
      else {
        this.$(".detail-node").each(function () {
          $(this).removeClass("selected-node");
          names.push(this.textContent);

        });

      }

      this.trigger("names:toggle", names, action);

      this.toggleUpdateButton();

    },

    toggleUpdateButton: function () {

      this.$(".update-all").toggleClass("add-all");

      if (this.$(".update-all").hasClass("add-all")) {

        this.$(".update-all").text("Add entire group to filter list.")

      }

      else {

        this.$(".update-all").text("Remove entire group from filter list.")

      }

    },


    drawGraph: function () {

      var svg, width, height, g1, g2, z;

      var scalesDict;

      var node, link;

      var numTicks;

      var self = this;

      width = this.styleModel.get("width");
      height = this.styleModel.get("height");

      scalesDict = this.model.get("scales")

      svg = d3.select(this.$("svg")[0]);

      this.model.set("svg", svg)

      var force = d3.layout.force()
        .size([width, height]);


      svg
        .attr("width", width)
        .attr("height", height)

      this.model.set("svg", svg)


      //container for network
      //need two gs because of weird panning requirement
      g1 = svg.append("g")

      g2 = g1.append("g")


      g2.append("rect")
        .attr("width", width)
        .attr("height", height)
        .style("fill", "none")
        .style("pointer-events", "all");


      this.model.set("g2", g2)


      force
        .nodes(this.model.get("nodes"))
        .links(this.model.get("links"))
        .start();

      this.model.set("force", force)

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
        .attr("font-size", function (d) {
          return scalesDict.fontScale(d.nodeWeight) + "px"
        })
        .classed({"detail-node": true, "selected-node": function (d) {
          return d.currentlySelected ? true : false
        }})
        .style("fill", function (d, i) {
          return scalesDict.colorScale(i);
        })

      node.append("title")
        .text(function (d) {
          return d.name;
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
        })

        maxX = _.max(xList);
        minX = _.min(xList);

        maxY = _.max(yList);
        minY = _.min(yList);

        buffer = 30

        largestDistance = _.max([(maxX - minX), (maxY - minY)]) + buffer;

        center = [(maxX + minX) / 2, (maxY + minY) / 2]

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

              z.translate([x,y]);


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

      if (oldScale >= 1.5) {
        newScale = oldScale - 1
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

    onClose : function(){

      this.model.get("force").stop();

      this.$("svg").empty();

      this.model.get("g2").on(null);



    }



  })



  var NetworkWidget = BaseWidget.extend({


    initialize: function (options) {

      this.model = new NetworkModel();

      this.view = new ContainerView({
        model : this.model,
        networkType: Marionette.getOption(this, "networkType")

      })

    },


    resetWidget : function(){

      //close graphView
      if (this.view.graphView){

        this.view.graphView.close();

        this.view.chosenNamesCollection.reset(null, {silent : true});

      }

      //reset model
      this.model.set(_.result(this.model, "defaults"), {silent : true});

    },

    processResponse: function (data) {

        this.model.set({fullData : data.fullGraph, summaryData : data.summaryGraph})

    }



  })


  return NetworkWidget


})