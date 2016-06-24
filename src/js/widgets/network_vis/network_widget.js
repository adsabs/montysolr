define([
    "marionette",
    "d3",
    'js/components/api_targets',
    'js/mixins/user_change_rows',
    "js/components/api_request",
    "js/components/api_query",
    "js/widgets/base/base_widget",
    "js/components/api_query_updater",
    "hbs!./templates/author-details-template",
    "hbs!./templates/container-template",
    "hbs!./templates/filter-container-template",
    "hbs!./templates/graph-container-template",
    "hbs!./templates/group-details-template",
    'hbs!./templates/not-enough-data-template',
    'hbs!./templates/network_metadata',
    'hbs!./templates/loading-template',
    'hbs!./templates/default-details-template',
    'bootstrap'
  ],
  function (Marionette,
            d3,
            ApiTargets,
            UserChangeMixin,
            ApiRequest,
            ApiQuery,
            BaseWidget,
            ApiQueryUpdater,
            authorDetailsTemplate,
            containerTemplate,
            filterContainerTemplate,
            graphContainerTemplate,
            groupDetailsTemplate,
            notEnoughDataTemplate,
            metadataTemplate,
            loadingTemplate,
            DefaultDetailsTemplate
    ) {

    function isInt(value) {
      return !isNaN(value) && (function(x) { return (x | 0) === x; })(parseFloat(value))
    }

    var FilterModel = Backbone.Model.extend({
      defaults: {
        name: undefined
      },
      idAttribute: "name"
    });

    var FilterCollection = Backbone.Collection.extend({
      model: FilterModel
    });

    var FilterView = Marionette.ItemView.extend({

      initialize : function(options){

      this.listenTo(this.collection, "add", this.render);
      this.listenTo(this.collection, "remove", this.render);
      this.listenTo(this.collection, "reset", this.render);
      },

      serializeData : function(){
        var that = this;
        var data = {};
        data.limit = this.collection.pluck("name");
        _.each(data.limit, function(d,i){
          if (isInt(d) && that.options.networkType == "author"){
            data.limit[i] = "at least one member of Group " + d;
          }
          else if (isInt(d) && that.options.networkType  == "paper"){
            data.limit[i] = "Group " + d;
          }
        });
        return data;
      },

      template: filterContainerTemplate,

      childViewContainer: ".dropdown-menu",

      events: {
        "click .apply-filter": "initiateFilter",
        "click .clear-filter": "clearFilter"
      },

      initiateFilter: function () {
        this.trigger("filter:initiated");
      },

      clearFilter: function () {
        this.collection.reset();
      }

    });

    //this will render a tiny tiny little graph

    var DefaultDetailsView = Marionette.ItemView.extend({

      template : DefaultDetailsTemplate,

      serializeData : function(){
        return {
          currentQuery : Marionette.getOption(this, "query"),
          networkType :  Marionette.getOption(this, "networkType"),
          cachedQuery :  Marionette.getOption(this, "cachedQuery")
        }
      },

      getData : function(){

        if (Marionette.getOption(this, "networkType") == "author") {

          var data = [];

          _.each(Marionette.getOption(this, "graphData").root.children, function (el, index) {

            var name = index;

            if (index > 6) {
              return
            }
            var allPapers = [];

            //get all papers
            _.each(el.children, function (author, index) {
              Array.prototype.push.apply(allPapers, author.papers);
            })

            //unique the list
            allPapers = _.uniq(allPapers);

            //fill in years with 0 papers
            var years = _.map(allPapers, function (bibcode) {
              return parseInt(bibcode.slice(0, 4));
            });

            //extract the years
            allPapers = _.countBy(allPapers, function (bibcode) {
              return bibcode.slice(0, 4);
            });

            var yearRange = _.range.apply(undefined, d3.extent(years));

            var skeleton = {}
            _.each(yearRange, function (y) {
              skeleton[y] = 0;
            });

            allPapers = _.extend(skeleton, allPapers)

            allPapers = _.map(allPapers, function (v, k) {
              return {year: parseInt(k), amount: v}
            })

            data.push({name: name, values: allPapers });

          });
        }
        else if ( Marionette.getOption(this, "networkType") == "paper" ){

          var summaryGraph = Marionette.getOption(this, "graphData").summaryGraph;
          var fullGraph = Marionette.getOption(this, "graphData").fullGraph
          var data = [];
          var nameDict = {};

          /*id is how you tie an entry in the summary graph with an entry in the full graph,
          * otherwise it is not used, it is assigned by louvain community detector in python script
          * */

          var ids = _.pluck(summaryGraph.nodes, "id");
          var names = _.pluck(summaryGraph.nodes, "node_name");

          _.each(ids, function(id, index) {

            var filteredNodes = [];

            _.each(fullGraph.nodes, function (n, i) {
              if (n.group === id) {
                filteredNodes.push(n.node_name)
              }
            });

            nameDict[names[index]] = filteredNodes;

          });

            _.each(nameDict, function(v, k ){

              if (k > 6){
                return
              }

              //unique the list
              allPapers = _.uniq(v);

              //fill in years with 0 papers
              var years = _.map(allPapers, function (bibcode) {
                return parseInt(bibcode.slice(0, 4));
              });

              //extract the years
              allPapers = _.countBy(allPapers, function (bibcode) {
                return bibcode.slice(0, 4);
              });

              var yearRange = _.range.apply(undefined, d3.extent(years));

              var skeleton = {}
              _.each(yearRange, function (y) {
                skeleton[y] = 0;
              });

              var allPapers = _.extend(skeleton, allPapers)

              allPapers = _.map(allPapers, function (v, k) {
                return {year: parseInt(k), amount: v}
              });

              //to make it the same as the author network: key is the index of the group based on size
              data.push({name: parseInt(k)-1, values: allPapers });

            });
        }

        return data;
      },

      onRender : function(){

        var data = this.getData();

        var oneYear = true;
        //if data is only from a single year, tell the user that and return
        _.each(data, function(d){
            if (d.values.length !== 1){
              oneYear = false;
            }
        });

        if (oneYear){
          var singleYear = data[0].values[0].year;
          this.$(".time-series-container").html("Not enough data to show a graph: All papers are from the year " + singleYear);
          return
        }

        var allX = [];
        var allY = [];
        _.each(data, function(group){
          _.each(group.values, function(entry){
            allX.push(entry.year);
            allY.push(entry.amount);
          });
        });

        allX = _.uniq(allX);

        var margin = {top: 20, right: 80, bottom: 30, left: 50},
          width = 960 - margin.left - margin.right,
          height = 500 - margin.top - margin.bottom;

        var x = d3.scale.linear()
          .range([0, width]);

        var y = d3.scale.linear()
          .range([height, 0]);

        var xAxis = d3.svg.axis()
          .scale(x)
          .orient("bottom")
          //specify explicitly, otherwise can get repeats for some weird reason
          .tickFormat(d3.format("0f"))
          .ticks(5);

          if (allX.length <= 10){
            xAxis.tickValues(allX)
          }

        var yAxis = d3.svg.axis()
          .scale(y)
          .tickFormat(d3.format("d"))
          .orient("left");

        var line = d3.svg.line()
          .interpolate("bundle")
          .x(function(d) { return x(d.year); })
          .y(function(d) { return y(d.amount); });

        var svg = d3.select(this.$("svg")[0])
          .attr("width", width + margin.left + margin.right)
          .attr("height", height + margin.top + margin.bottom)
            //for svg copying by svg crowbar
          .attr("style", "font-family:helvetica,arial,san-serif")
          .attr("id", "network-viz-time-series")
          .append("g")
          .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        var color = d3.scale.ordinal()
          .domain([0, 1, 2, 3, 4, 5, 6])
          .range(["hsla(282, 80%, 52%, 1)", "hsla(1, 80%, 51%, 1)", "hsla(42, 97%, 48%, 1)", "hsla(152, 80%, 40%, 1)", "hsla(193, 80%, 48%, 1)", "hsla(220, 80%, 56%, 1)", "hsla(250, 69%, 47%, 1)"]);

        x.domain(d3.extent(allX));
        y.domain([0, _.max(allY)]);

        svg.append("g")
          .attr("class", "axis")
          .attr("transform", "translate(0," + height + ")")
          .call(xAxis);

        svg.append("g")
          .attr("class", "axis")
          .call(yAxis);

        var groups = svg.selectAll(".group")
          .data(data)
          .enter().append("g")
          .attr("class", "group");

        groups.append("path")
          .attr("class", "line")
          .attr("d", function(d) { return line(d.values); })
          .style("stroke", function(d) { return color(d.name); });

        groups.append("text")
          .datum(function(d) { return {name: d.name, value: d.values[d.values.length - 1]}; })
          .attr("transform", function(d) { return "translate(" + x(d.value.year) + "," + y(d.value.amount) + ")"; })
          .attr("x", 3)
          .attr("dy", ".35em")
          .text(function(d) { return d.name + 1; });

        var oneYear = _.filter(data, function(d){
          if (d.values.length == 1){
            return true
          }
        });

        //hack to show groups with only one year (otherwise line has width 0)
        svg.selectAll(".one-year")
          .data(oneYear)
          .enter()
          .append("circle")
          .classed("one-year", true)
          .attr("r", 5)
          .attr("cx", function(d){return x(d.values[0].year)})
          .attr("cy", function(d){return y(d.values[0].amount)})
          .attr("fill", function(d){return color(d.name)});
      }

    });

    var ContainerView = Marionette.ItemView.extend({

      template: containerTemplate,

      className : function() {
        var n = Marionette.getOption(this, "networkType");
        return "network-widget s-network-widget " + n
      },

      serializeData : function(data){
        var n = Marionette.getOption(this, "networkType");
        n = n[0].toUpperCase() + n.slice(1);
        return {networkType: n,
          helpText: Marionette.getOption(this, "helpText")}
      },

      ui: {
        filterContainer: ".network-filter-container",
        graphContainer: ".graph-container",
        detailsContainerSelected : ".details-container #selected-item",
        detailsContainerHome : ".details-container #home"

      },

      events : {
        "click .load-author-network" : "forwardNewQueryRequest",
        "click button.limit" : "addItem",
        "click .submit-rows" : "changeRows",
        "click .close" : "triggerClose",
        "click .filter-remove" : function(e){
          if (Marionette.getOption(this, "networkType") == "paper"){
            var name = this.graphModel.get("selectedEntity").__data__.data.node_name;
            Marionette.getOption(this, "filterCollection").remove({id : name});
            //re-render detail sub view
            this.graphView.showSelectedEntity(this.graphModel.get("selectedEntity"));
          }
          else {
            var name = this.graphModel.get("selectedEntity").__data__.name;
            Marionette.getOption(this, "filterCollection").remove({id : name});
            //re-render detail sub view
            this.showDetailView(this.graphModel.get("selectedEntity"));
          }
        },
        "click .filter-add": function(e){
          if (Marionette.getOption(this, "networkType") == "paper"){
            var name = this.graphModel.get("selectedEntity").__data__.data.node_name;
            Marionette.getOption(this, "filterCollection").add({name: name});
            //re-render detail sub view
            this.graphView.showSelectedEntity(this.graphModel.get("selectedEntity"));
          }
          else {
            var papers = [];

            var data = this.graphModel.get("selectedEntity").__data__;
            if (data.papers){
              //author
              papers = data.papers
            }
            else {
              //group
              _.each(data.children, function(d){
                [].push.apply(papers, d.papers);
              });
            }
            Marionette.getOption(this, "filterCollection").add({name: data.name, papers : papers});
            //re-render detail sub view
            this.showDetailView(this.graphModel.get("selectedEntity"));
          }
        }
      },

      triggerClose : function(){
        this.trigger("close")
      },

      //user has requested author network for current author
      forwardNewQueryRequest : function(e){

        //show loading template
        this.ui.graphContainer.html(loadingTemplate());

        this.trigger("new-network-requested", $(e.target).data("query"));
      },

      changeRows : function(e) {
        e.preventDefault();
        var num = parseInt(this.$(".network-rows").val());
        this.$(".network-metadata").html(loadingTemplate);
        if (num){
            this.model.set("userVal", _.min([this.model.get("max"), num]));
        }
      },

      modelEvents: {
       "change:graphData": "render",
        "change:max":"renderMetadata",
        "change:current":"renderMetadata"
      },

      onRender: function () {
        //append sub views
        var graphViewToUse;

        // do nothing if there is no data
       var cachedQ;
        try {
          cachedQ = this.model.get("cachedQuery").get("q")[0];
        }
        catch(e){
          //ignore, there will be no cached query
        }

        //show loading view
        if (this.model.get("loading")) {
          this.$(".network-metadata").html("");
          this.$(".network-container").html(loadingTemplate());
          return
        }

        if (!this.model.get("graphData") || _.isEmpty(this.model.get("graphData"))) {
          this.$(".network-metadata").html("");
          this.$(".network-container").html(notEnoughDataTemplate({cachedQuery : cachedQ}));
          return
        }
        //it's a paper network with no summary info
        else if (!this.model.get("graphData").summaryGraph && !this.model.get("graphData").root) {
          //maybe later show something if we just have the fullGraph
          this.$(".network-metadata").html("");
          this.$(".network-container").html(notEnoughDataTemplate({cachedQuery : cachedQ }));
          return

        }
        //all the data the graph view will need
        this.graphModel = new GraphModel({graphData: this.model.get("graphData")});

        this.filterView = new FilterView({collection: Marionette.getOption(this, "filterCollection"), networkType: Marionette.getOption(this, "networkType")});
        this.listenTo(this.filterView, "filter:initiated", function () {
          //forward to controller
          this.trigger("filter:initiated");
        });
        //can pass another view in if inheriting
        graphViewToUse = this.options.graphView ? this.options.graphView : GraphView
        this.graphView = new graphViewToUse({model: this.graphModel, filterCollection: Marionette.getOption(this, "filterCollection")});

        this.graphView.on("show-detail-view", _.bind(this.showDetailView, this));

        this.ui.filterContainer.append(this.filterView.render().el);
        this.ui.graphContainer.append(this.graphView.render().el);

        this.ui.detailsContainerHome.append(new DefaultDetailsView({
            graphData: this.model.get("graphData"),
            query: this.model.get("query"),
            networkType : Marionette.getOption(this, "networkType"),
            cachedQuery : cachedQ
          }).render().el);

        //set the home tab as default
        this.$(".nav-tabs a[href='#home']").tab("show");

        this.renderMetadata();
      },

      //triggered by graph view
      showDetailView : function(selected){

        var d = selected.__data__;

        selected = d3.select(selected);
        //there might not be a label because it might be a group
        selected
          .select(".node-label").classed("selected-label", true)
        //but there should definitely be a path
        selected
          .select(".node-path").classed("selected-node", true);

        //now, get the data
        var allPapers = [],
          children,
          data = {},
          label, papers = [],
          graphData = this.model.get("graphData");

        //author node
        if (!d.children) {
          papers = _.map(d.papers, function (bib) {
            var paperDict = graphData.bibcode_dict[bib];
            paperDict["bibcode"] = bib;
            if (_.isArray(paperDict["title"])) {
              paperDict["title"] = paperDict["title"][0]
            }
            return paperDict;
          });

          papers = _.sortBy(papers, function (p) {
            return p.citation_count;
          }).reverse();

          //now find most recent year, and papers from that year
          var mostRecentYear = _.chain(papers)
            .pluck("bibcode")
            .sortBy(function (bibcode) {
              return bibcode.slice(0, 4)
            })
            .value()
            .reverse()[0].slice(0, 4);

          data.bibcodes = d.papers;
          data.mostRecentYear = mostRecentYear;
          data.papers = papers;
          data.total = papers.length;
          data.name = d.name;
          data.groupColor = d.parent.color;
          data.inFilter = Marionette.getOption(this, "filterCollection").get(d.name) ? true : false;
          //make the query that will be issued when user clicks "view network for this person"
          data.dataQuery = "author:\"" +  d.name + "\"";

          this.ui.detailsContainerSelected.empty().append(authorDetailsTemplate(data));

          //group node
        } else {

          children = d.children;
          _.each(children, function (c) {
            Array.prototype.push.apply(allPapers, c.papers)
          });

          //now find most recent year
          var mostRecentYear = _.sortBy(allPapers, function (bibcode) {
            return bibcode.slice(0, 4)
          }).reverse()[0].slice(0, 4);

          allPapers = _.pairs(_.countBy(allPapers));

          /*
           * compare three variables to get a final score : number of authors in the group,
           * percent authors in the group, and total number of citations
           * */

          var numAuthors = _.chain(allPapers).map(function(a){return a[1]}).sortBy(function(x){return x}).value();
          var percentAuthors = _.chain(allPapers).map(function(a){return a[1] / graphData.bibcode_dict[a[0]].authors.length}).sortBy(function(x){return x}).value();
          var numCitations = _.chain(allPapers).map(function(a){ return graphData.bibcode_dict[a[0]].citation_count/graphData.bibcode_dict[a[0]].authors.length}).sortBy(function(x){return x}).value();

          allPapers = _.sortBy(allPapers, function (list) {
            // percent of authors that are members of this group
            return (list[1]-numAuthors[0])/(numAuthors[numAuthors.length-1]-numAuthors[0]) *
              (list[1]/graphData.bibcode_dict[list[0]].authors.length - percentAuthors[0])/(percentAuthors[percentAuthors.length-1]-percentAuthors[0]) *
              (graphData.bibcode_dict[list[0]].citation_count/graphData.bibcode_dict[list[0]].authors.length-numCitations[0])/(numCitations[numCitations.length-1]-numCitations[0]);
          }).reverse();

          bibcodes = _.map(allPapers, function (l) {
            return l[0];
          });

          papers = _.map(bibcodes, function (b, i) {
            var d = {};
            d.bibcode = b;
            d.title = graphData.bibcode_dict[b].title;
            d.citation_count = graphData.bibcode_dict[b].citation_count;
            d.numAuthors = allPapers[i][1];
            return d
          });

          data.bibcodes = bibcodes;
          data.papers = papers;
          data.groupColor = d.color;
          data.total = allPapers.length;
          data.mostRecentYear = mostRecentYear;
          data.name = d.name;
          data.inFilter = Marionette.getOption(this, "filterCollection").get(d.name) ? true : false;

          this.ui.detailsContainerSelected.empty().append(groupDetailsTemplate(data));
        }
        //set the selected details tab as default
        this.$(".nav-tabs a[href='#selected-item']").tab("show");

      },

      //function to just re-render the metadata part
      renderMetadata : function(){
        var data = {};
        data.max = this.model.get("max");
        data.current = this.model.get("current");
        this.$(".network-metadata").html(metadataTemplate(data));
      }

    });

    var ContainerModel = UserChangeMixin.Model.extend({

      initialize : function(options) {

        var options = options  || {};
        this.on("newMetadata", function () {
          this.updateCurrent();
          this.updateMax();
        });

        if (!options.widgetName) {
          throw new Error("need to configure with widget name so we can get limit/default info from api_targets._limits");
        }

        var defaults = {
          graphData: {},
          rows: undefined,
          numFound: undefined,
          current: undefined,
          //current max
          max: undefined,
          //when this changes, make a new request
          userVal: undefined,
          //keep record of former query so user can return
          cachedQuery: undefined,
          loading : true,
        }

        _.extend(defaults, ApiTargets._limits[options.widgetName]);

        this.defaults = function () {
          return defaults
        }

        this.set(this.defaults());
      }
    });

    var GraphView = Marionette.ItemView.extend({

      initialize : function(options){
        //listen to filter collection in order to re-render current view
        this.listenTo(options.filterCollection, "reset", this.showSelectedEntity)
      },

      template: graphContainerTemplate,

      events : {
        "change input[name=mode]": function(e){
          this.model.set("mode", e.target.value);
        },
        "change input[name=show-link]" : function(e){
          this.model.set("linkLayer", e.target.checked);
        }
      },

      modelEvents: {
        "change:graphData": "renderGraph",
        "change:selectedEntity": "showSelectedEntity",
        "change:linkLayer": "changeLinkLayer",
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
        this.config = { numberOfLabelsToShow: 100,
          width: 900,
          height: 900,
          noGroupColor: "#a6a6a6"
        };

        this.config.radius = Math.min(this.config.width, this.config.height) / 3;
      },

///   for items generated over the course of regular d3 functions that might be useful later
      cachedVals : {
        partition : undefined,
        line : undefined,
        computeLabelPosition : undefined,
        svg : undefined,
        arc : undefined
      },

      renderGraph: function () {

        var graphData = this.model.get("graphData");
        var that = this;
        this.cachedVals.svg = d3.select(this.$("svg")[0]);

        this.getConfig();

        //creating all the scales that this and other functions need
        this.customizeScales(graphData);

        //some functions to be used below
        // stash the old values for transition.
        function stash(d) {
          d.x0 = d.x;
          d.dx0 = d.dx;
        };

        function computeLabelPosition(d) {
          var angle = (d.x + d.x + d.dx) / 2 * 180 / Math.PI - 90;
          var t = parseInt(that.config.radius) + 10;
          var flip = angle > 90 ? 180 : 0;
          if (flip) {
            d3.select(this).attr("text-anchor", "end")
          } else {
            d3.select(this).attr("text-anchor", "start");
          }
          return "rotate(" + angle + ")translate(" + t + ")rotate(" + flip + ")";
        }

        this.cachedVals.computeLabelPosition = computeLabelPosition;

        //append the groups containing the paths and the text labels
        var svg = that.cachedVals.svg
          .attr("width", that.config.width)
          .attr("height", that.config.height)
          .append("g")
          .classed("network-g", true);

        var g2 = svg.append("g").classed("real-container-of-stuff", true);

        //zoom behavior
        var zoom = d3.behavior.zoom()
          .scaleExtent([.7, 3])
          .on("zoom", zoomed);

        function zoomed() {
          g2.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
        }

        svg.call(zoom);

        svg.on("dblclick.zoom", null);

        svg.attr("transform", "translate(" + that.config.width / 2 + "," + (that.config.height / 2.1) + ")");

        var partition = d3.layout.partition()
          .value(function (d) {
            return d.size;
          })
          .size([2 * Math.PI, that.config.radius * that.config.radius ])
          .sort(function (a, b) {
            return b.value - a.value;
          });

        this.cachedVals.partition = partition;

        var arc = d3.svg.arc()
          .startAngle(function (d) {
            return d.x;
          })
          .endAngle(function (d) {
            return d.x + d.dx;
          })
          .innerRadius(function (d) {
            if (d.depth == 1) {
              return Math.sqrt(d.y) + 40;
            } else {
              return Math.sqrt(d.y);
            }
          })
          .outerRadius(function (d) {
            return Math.sqrt(d.y + d.dy);
          });

        this.cachedVals.arc = arc;

        //for mouse interaction
        //otherwise there is a weird hollow space between center and the colored sections

        g2.append("circle")
          .attr("r", that.config.radius)
          .attr("fill", "#fff")
          .style("cursor", "move");

        var containers = g2.selectAll("g")
          .data(partition.nodes(graphData.root))
          .enter()
          .append("g")
          .classed("node-containers", true)
          //cache sort value
          .each(function (d) {
            d.sortVal = d.value;
          });

        var colorDict = {};

        var path = containers
          .append("path")
          .attr("d", arc)

          .classed("node-path", function (d) {
            if (d.depth == 0) {
              return false
            } else {
              return true
            }
          })
          .style("cursor", function(d){
            if (d.depth == 0)
            return "move"
          })
          .style("fill", function (d) {
            if (d.depth == 0) {
              return "white"
            } else if (d.depth == 1) {
              var index = d.parent.children.indexOf(d);
              if (index < 7) {
                d.color = that.scales.color(index);
                colorDict[d.name] = d.color;
                return d.color;
              } else {
                return that.config.noGroupColor
              }
            } else if (d.depth == 2) {
              //child nodes
              if (!d.parent.color) {
                return that.config.noGroupColor
              } else {
                return d.parent.color;
              }
            }
          })
          .each(stash);

        //now taking care of labels
        containers.filter(function (d) {
          return (!d.children)
        })
          .classed("author-node", true)
          .append("text")
          .text(function (d) {
            return d.name
          })
          .classed("node-label", true)
          .on("mouseover", function (d) {
            //only show link stuff if link layer is active
            if (!that.model.get("linkLayer")){
              return
            }
            var thisD = d;

            // get all links, add selected link class
            d3.selectAll(".link").filter(function (d) {
              if (d[0] == thisD || d[d.length - 1] == thisD) {
                return true
              }
            }).classed("selected-link", true);

            //add linked label class to nodes connected by a link
             d3.selectAll(".link").each(function (linkD) {
              if (linkD[0] == thisD) {
                d3.selectAll(".node-label")
                  .filter(function(d){ return d.name == linkD[linkD.length - 1].name})
                  .classed("linked-label", true);
              }
              else if(linkD[linkD.length - 1] == thisD) {
                d3.selectAll(".node-label")
                  .filter(function(d){ return d.name == linkD[0].name})
                  .classed("linked-label", true);
              }
            });

          }).on("mouseout", function () {
            d3.selectAll(".link").classed(
              "selected-link", false);
            d3.selectAll(".node-label").classed("linked-label", false);
          })
          .attr("font-size", function (d) {
            return that.scales.occurrencesFontScale(d.size) + "px"
          })
          .attr("transform", computeLabelPosition);

        //append connector nodes if possible
        _.each(graphData.root.name, function (n, i) {
          g2.append("text")
            .classed("s-connector-node", true)
            .attr("y", function(){
              return (i * 25) - (graphData.root.name.length *25/2);
            })
            .text(n.nodeName);
        });

        //attach event listeners
        containers.on("click", function (d,i) {

          //ignore top-level node
          if (d.depth == 0){
            return
          }

          //prevent interaction when link layer is active
          if (that.model.get("linkLayer")){
            return
          }

          //could be a label or a path that was clicked
          //within the group
          if (that.model.get("selectedEntity") === this) {
            that.model.set("selectedEntity", null)
          }
          else {
            that.model.set("selectedEntity", this);
          }
        });
        //render the link layer
        this.renderLinkLayer();

      },

      renderLinkLayer: function () {
        var that = this;
        svg = d3.select(that.$(".real-container-of-stuff")[0]),
        tooltip = d3.select(that.$(".d3-tooltip")[0]),
        bibDict = this.model.get("graphData").bibcode_dict;
        var links, bundle, line, linkContainer;

        links = _.map(that.model.get("graphData").link_data, function (l) {
          var linkData = {};
          linkData.source = svg.selectAll(".node-path").filter(function (d) {
            return d.numberName == l[0]
          })[0][0].__data__;
          linkData.target = svg.selectAll(".node-path").filter(function (d) {
            return d.numberName == l[1]
          })[0][0].__data__;
          linkData.weight = l[2];
          return linkData
        });

        bundle = d3.layout.bundle();
        line = d3.svg.line.radial()
          .interpolate("bundle")
          .tension(.9)
          .radius(function (d) {
            return Math.sqrt(d.y + d.dy);
          })
          .angle(function (d) {
            // UGH IT WANTS RADIANS NOT DEGREES BLAAKSKDKDK
            return (d.x + d.x + d.dx) / 2;
          });

        //cache for event handlers
         this.cachedVals.line  = line;

        linkContainer = svg.append("g")
          .classed("link-container", true);

        linkContainer.append("circle")
          .attr("r", that.config.radius)
          .classed("link-background", true);

        linkContainer.selectAll(".link")
          .data(bundle(links))
          .enter().append("path")
          .attr("class", "link")
          .attr("d", function(d) {
            return line(d)
          })
          .attr("stroke-width", function (d) {
            // get link weight
            var weight = _.filter(links, function (l) {
              return (l.source == d[0] && l.target == d[d.length - 1] || l.target == d[0] && l.source == d[d.length - 1])
            })[0].weight;
            return that.scales.linkScale(weight);
          });

      },

      customizeScales: function (graphData) {
        //scales and a few other variables to share
        this.scales = {};

        this.scales.color = d3.scale.ordinal()
          .domain([0, 1, 2, 3, 4, 5, 6])
          .range(["hsla(282, 60%, 52%, 1)", "hsla(349, 61%, 47%, 1)", "hsla(26, 95%, 67%, 1)", "hsla(152, 60%, 40%, 1)", "hsla(193, 64%, 61%, 1)", "hsla(220, 70%, 56%, 1)", "hsla(250, 50%, 47%, 1)"]);

        var sizes = [], citation_counts = [], read_counts = [];

        _.each(graphData.root.children, function (group) {
          _.each(group.children, function (c) {
            sizes.push(c.size);
            citation_counts.push(c.citation_count);
            read_counts.push(c.read_count);
          })
        });

        citation_counts = _.sortBy(citation_counts, function (c) {
          return c
        }).reverse();

        read_counts = _.sortBy(read_counts, function (r) {
          return r
        }).reverse();

        //when the ring sizing is by citation, how many labels should be shown?
        this.scales.citationLimit = citation_counts[this.config.numberOfLabelsToShow] || citation_counts[citation_counts.length -1];
        //when the ring sizing is by reads, how many labels should be shown?
        this.scales.readLimit = read_counts[this.config.numberOfLabelsToShow] || read_counts[read_counts.length -1];

        this.scales.occurrencesFontScale = d3.scale.log()
          .domain([d3.min(sizes), d3.max(sizes)])
          .range([8, 20]);

        this.scales.citationFontScale = d3.scale.linear()
          .domain([d3.min(citation_counts), d3.max(citation_counts)])
          .range([8, 20]);

        this.scales.readFontScale = d3.scale.linear()
          .domain([d3.min(read_counts), d3.max(read_counts)])
          .range([8, 20]);

        //link scale
        var weights = _.map(graphData.link_data, function(l){
          return l[2];
        });

        this.scales.linkScale = d3.scale.pow().exponent(8)
          .domain([d3.min(weights), d3.max(weights)]).range([0.5, 3.5]);
      },

      /* functions to deal with user interaction */
      showSelectedEntity: function () {

        var selected = this.model.get("selectedEntity");
        var that = this,
          $div = that.$(".network-detail-area");

        this.cachedVals.svg.selectAll(".node-path").classed("selected-node", false);
        this.cachedVals.svg.selectAll(".node-label").classed("selected-label", false);
        $div.empty();

        if (!selected) {
          return
        }
        else {
          this.trigger("show-detail-view", selected);
        }
      },

      changeLinkLayer: function (model,val) {

        if (val){
          this.model.set("cachedEntity", this.model.get("selectedEntity"));
          this.model.set("selectedEntity", undefined);

          //show link layer
          var interval;
          d3.select(this.$(".link-container")[0]).style("display", "block")
            .selectAll(".link")
            .call(function(selection){
              interval = 3000/selection[0].length;
            })
            .sort(function(d){return d.weight})
            .style("display", "block");
        }
        else {
          //hide it
          this.model.set("selectedEntity", this.model.get("cachedEntity"));
          d3.select(this.$(".link-container")[0]).style("display", "none");
        }
      },

      changeMode: function (model, currentVal) {

        var that = this;
        function value (d) {
          return d[currentVal];
        };

        // Interpolate the arcs in data space.
        function arcTween(a) {
          var i = d3.interpolate({
            x: a.x0,
            dx: a.dx0
          }, a);
          return function (t) {
            var b = i(t);
            a.x0 = b.x;
            a.dx0 = b.dx;
            return that.cachedVals.arc(b);
          };
        }

        var containers = that.cachedVals.svg.selectAll(".node-containers");

        var data = that.cachedVals.partition
          .sort(function(a, b) {
            return b.sortVal - a.sortVal;
          }).value(value)
          .nodes(that.model.get("graphData").root);

        containers.data(data, function(d){return d.name})
          .selectAll("path")
          .transition()
          .duration(1500)
          .attrTween("d", arcTween);

        containers.selectAll("text")
          .attr("opacity", "0")
          .attr("display", function(d) {
            if (currentVal == "size") {
              return "block"
            } else if (currentVal == "citation_count" && d.citation_count > that.scales.citationLimit) {
              return "block"
            }
            else if (currentVal == "read_count" && d.read_count > that.scales.readLimit) {
              return "block"
            }
            else {
              return "none"
            }
          })
          .attr("font-size", function(d) {
            if (currentVal == "size") {
              return that.scales.occurrencesFontScale(d.size) + "px";
            } else if (currentVal == "citation_count") {
              return that.scales.citationFontScale(d.citation_count) + "px";
            }
            else if  (currentVal == "read_count") {
              return that.scales.readFontScale(d.read_count) + "px";
            }
          })
          .attr("transform", that.cachedVals.computeLabelPosition)
          .transition()
          .duration(1500)
          .attr("opacity", 1);

        d3.selectAll(".link")
          .transition()
          .duration(1500)
          .attr("d", function(d) {
            return that.cachedVals.line(d)
          });
      }
    });

    var GraphModel = Backbone.Model.extend({

      defaults: function () {
        return  {
          //data from the api
          graphData: {},
          //keep track of selection state
          selectedEntity: undefined,
          cachedEntity: undefined,

          query: undefined,
          linkLayer: false,
          mode: "occurrences"
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

        this.model = new ContainerModel({widgetName : options.widgetName || "AuthorNetwork"});
        this.listenTo(this.model, "change:userVal", this.requestDifferentRows);

        this.filterCollection = new FilterCollection();

        this.view = new ContainerView({
          graphView : options.graphView,
          model: this.model,
          filterCollection: this.filterCollection,
          networkType: options.networkType,
          helpText: options.helpText,
          graphMixin: options.graphMixin,
          showDetailGraphView: options.showDetailGraphView
        });

        this.listenTo(this.view, "new-network-requested", this.requestDifferentQuery);
        this.listenTo(this.view, "filter:initiated", this.broadcastFilteredQuery);
        this.listenTo(this.view, "close", this.broadcastClose);

        this.widgetName = "visualization_" + options.networkType;
        this.queryUpdater = new ApiQueryUpdater(this.widgetName);
      },

      generateApiRequest : function(query) {

        return new ApiRequest({
          target: Marionette.getOption(this, "endpoint"),
          query: new ApiQuery({ "query" : JSON.stringify(query.toJSON()) }),
          options : {
            type : "POST",
            contentType : "application/json"
          }
        });

      },

      //when a user requests a different number of documents
      requestDifferentRows : function(model, rows){

        var query = this.getCurrentQuery().clone();
        query.unlock();
        query.set("rows", this.model.get("userVal"));
        query.unset("hl");
        query.unset("hl.fl");

        var request = this.generateApiRequest(query);
        this.getPubSub().publish(this.getPubSub().EXECUTE_REQUEST, request);
      },

      //allows you to view network for another author
      requestDifferentQuery : function(newQuery){

        //cache the current query
        this.model.set("cachedQuery", this.getCurrentQuery());

        //make a new query, otherwise facet parameters remain

        var query = new ApiQuery();
        query.set("q", newQuery);
        query.set("rows", this.model.get("default"));
        //default setting sort to "date desc" since that is presumably what users will be used to
        //this isn't perfect (it might override the original query's sorted value)
        //but probably no one will notice
         query.set("sort", "date desc");

        var request = this.generateApiRequest(query);

        //update the current widget query
        this.setCurrentQuery(query);
        this.getPubSub().publish(this.getPubSub().EXECUTE_REQUEST, request);

      },

      activate: function (beehive) {

        this.setBeeHive(beehive);
        _.bindAll(this, "setOriginalQuery", "processResponse");
        var pubsub = this.getPubSub();
        //custom dispatchRequest function goes here
        pubsub.subscribe(pubsub.INVITING_REQUEST, this.setOriginalQuery);
        //custom handleResponse function goes here
        pubsub.subscribe(pubsub.DELIVERING_RESPONSE, this.processResponse);

        //on initialization, store the current query
        if (this.getBeeHive().getObject("AppStorage")){
          this.setOriginalQuery(this.getBeeHive().getObject("AppStorage").getCurrentQuery());
        }
      },

      //cache this so that the "broadcastFilteredResponse" still works even if user is looking at a
      //different network
      setOriginalQuery: function(query) {

        //clear the "cachedQuery" because it's a new search cycle
        this.model.set("cachedQuery", undefined);
        this._originalQuery = query;
        this.setCurrentQuery(query);
      },

      getOriginalQuery : function(){
        return this._originalQuery;
      },

      //empty all data
      //this will show the loading view by default
      //until new data comes in
      resetWidget: function () {

        //close graphView
        if (this.view && this.view.graphView) {
          this.view.graphView.$el.empty();
          this.view.graphView.stopListening();
        }
        this.filterCollection.reset(null, {silent : true});
        //reset model
        this.model.set(_.result(this.model, "defaults"), {silent: true});
      },

      //for now, called to show vis for library
      //and for bigquery
      renderWidgetForListOfBibcodes : function(bibcodes){

        //so the earlier state of the widget is not preserved
        this.resetWidget();

        //force a reset even if the data is the same
        //so the earlier state of the widget is not preserved
        this.model.set({ graphData :  {}, loading : true });

        var request =  new ApiRequest({
          target : Marionette.getOption(this, "endpoint"),
          query: new ApiQuery({ "bibcodes" : bibcodes }),
          options : {
            type : "POST",
            contentType : "application/json"
          }
        });

        this.getPubSub().publish(this.getPubSub().EXECUTE_REQUEST, request);

      },

      //fetch data
      renderWidgetForCurrentQuery : function () {

        //force a reset even if the data is the same
        //so the earlier state of the widget is not preserved
        this.resetWidget();

        var query = this.getCurrentQuery().clone();
        query.unlock();

       //if it's a bigquery, we need the bibcodes first
        if( query.get("__qid")){
          query.set("fl", "bibcode");
          //limit is 1000
          query.set("rows", "1000");
          var request = this.composeRequest(query);
          this.getPubSub().publish(this.getPubSub().DELIVERING_REQUEST, request);
        }
        else {
          query.set("rows", this.model.get("default"));
          query.unset("hl");
          query.unset("hl.fl");

          var request = this.generateApiRequest(query);
          this.getPubSub().publish(this.getPubSub().EXECUTE_REQUEST, request);
        }

      },

      processResponse: function (jsonResponse) {

        try {
          //it's a bigquery response with bibcodes, now request the vis data
          var qid = jsonResponse.get("responseHeader.params.__qid");
          this.renderWidgetForListOfBibcodes(jsonResponse.get("response").docs.map(function(b){return b.bibcode}));
        }
        catch(e){
          //it's from the network endpoint, loading is done
          var data = jsonResponse.toJSON();
          // let container view know how many bibcodes we have
          this.model.set({graphData : data.data,
            numFound: parseInt(data.msg.numFound),
            rows: parseInt(data.msg.rows),
            query: jsonResponse.getApiQuery().get("q"),
            loading: false
          });
          //so there is a one time render event
          this.model.trigger("newMetadata");
        }

      },

      //filter the original query
      broadcastFilteredQuery: function () {

        var allBibs = _.flatten(this.filterCollection.pluck("papers"));

        var newQuery = this.getOriginalQuery().clone()
        newQuery.unlock();

        newQuery.set("__bigquery", allBibs);

        this.resetWidget();
        this.getPubSub().publish(this.getPubSub().START_SEARCH, newQuery);
      },

      broadcastClose: function () {
        this.resetWidget();
        this.getPubSub().publish(this.getPubSub().NAVIGATE, "results-page");
      },

      testing : {
        detailView : DefaultDetailsView
      }

    });

    return NetworkWidget

  });
