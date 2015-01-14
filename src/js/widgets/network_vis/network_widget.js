define([
    "marionette",
    "d3",
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
    'bootstrap'
  ],
  function (Marionette,
            d3,
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
            loadingTemplate
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

      itemViewContainer: ".dropdown-menu",

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
        graphContainer: ".network-container"
      },

      events : {
        "click button.limit" : "addItem",
        "click .submit-rows" : "changeRows"
      },

      changeRows : function(e) {
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
        var graphModel, graphViewToUse;

        // do nothing if there is no data

        if (!this.model.get("graphData") || _.isEmpty(this.model.get("graphData"))){
          this.$(".network-metadata").html("");
          this.$(".network-container").html(notEnoughDataTemplate());
          return
        }
        //it's a paper network with no summary info
        else if (!this.model.get("graphData").summaryGraph && !this.model.get("graphData").root){
          //maybe later show something if we just have the fullGraph
          this.$(".network-metadata").html("");
          this.$(".network-container").html(notEnoughDataTemplate());
          return

        }
        graphModel = new GraphModel({graphData: this.model.get("graphData")});
        this.filterView = new FilterView({collection: Marionette.getOption(this, "filterCollection"), networkType : Marionette.getOption(this, "networkType")});
        this.listenTo(this.filterView, "filter:initiated", function(){
          //forward to controller
          this.trigger("filter:initiated");
        });
        //can pass another view in if inheriting
        graphViewToUse =  this.options.graphView ? this.options.graphView : GraphView
        this.graphView = new graphViewToUse({model: graphModel, filterCollection : Marionette.getOption(this, "filterCollection")});
        this.ui.filterContainer.append(this.filterView.render().el);
        this.ui.graphContainer.append(this.graphView.render().el);

        this.renderMetadata();

        //help popover
        this.$(".icon-help").popover({trigger: "hover", placement: "bottom", html: true});
      },

      //function to just re-render the metadata part
      renderMetadata : function(){
        var data = {};
        data.max = this.model.get("max");
        data.current = this.model.get("current");
        this.$(".network-metadata").html(metadataTemplate(data));
      }

    })

    var ContainerModel = Backbone.Model.extend({

      initialize : function(){
        this.on("change:numFound", this.updateMax);
        this.on("change:rows", this.updateCurrent);
      },

      updateMax : function() {
        var m = _.min([this.get("maxRows"), this.get("numFound")]);
        this.set("max", m);
      },

      updateCurrent : function(){
        this.set("current", _.min([this.get("rows"), this.get("numFound")]));
      },

      defaults : function(){
        return {
          graphData: {},
          rows : undefined,
          //stable max, from widget config, normally 1000
          maxRows : undefined,
          numFound : undefined,
          current : undefined,
          //current max
          max : undefined,
          //when this changes, make a new request
          userVal: undefined
        }
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
        },
        "click .filter-remove" : function(e){
          var name = this.model.get("selectedEntity").__data__.name;
          Marionette.getOption(this, "filterCollection").remove({id : name});
          //re-render detail sub view
          this.showSelectedEntity();
        },
        "click .filter-add": function(e){
          var name = this.model.get("selectedEntity").__data__.name;
          Marionette.getOption(this, "filterCollection").add({name: name});
          //re-render detail sub view
          this.showSelectedEntity();
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
          width: 960,
          height: 900,
          noGroupColor: "#a6a6a6"
        };

        this.config.radius = Math.min(this.config.width, this.config.height) / 2 - 140;
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
          .classed("network-g", true)
          .attr("transform", "translate(" + that.config.width / 2 + "," + (that.config.height / 2.1) + ")");

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

        var containers = svg.selectAll("g")
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
            var thisD = d, lines, otherLabels;

            // get all links, add selected link class
            lines = d3.selectAll(".link").filter(function (d) {
              if (d[0] == thisD || d[d.length - 1] == thisD) {
                return true
              }
            }).classed("selected-link", true);

            //add linked label class to nodes connected by a link
            otherLabels = d3.selectAll(".link").each(function (linkD) {
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
        _.each(graphData.root.name, function (n) {
          svg.append("text")
            .classed("s-connector-node", true)
            .text(n.nodeName);
        });

        //attach event listeners
        containers.on("click", function (d,i) {

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
        //finally, render the link layer
        this.renderLinkLayer();
      },

      renderLinkLayer: function () {
        var that = this;
        svg = d3.select(that.$(".network-g")[0]),
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
          .range(["hsla(282, 80%, 52%, 1)", "hsla(1, 80%, 51%, 1)", "hsla(42, 97%, 48%, 1)", "hsla(152, 80%, 40%, 1)", "hsla(193, 80%, 48%, 1)", "hsla(220, 80%, 56%, 1)", "hsla(250, 69%, 47%, 1)"]);

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

          $div.append(authorDetailsTemplate(data));

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

          $div.append(groupDetailsTemplate(data));

        }
      },

      changeLinkLayer: function (model,val) {

        if (val){
          this.model.set("cachedEntity", this.model.get("selectedEntity"));
          this.model.set("selectedEntity", undefined);

          //show link layer
          var interval;
          d3.select(".link-container").style("display", "block")
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
          d3.select(".link-container").style("display", "none");
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
          linkLayer: false,
          mode: "occurrences"
        }
      }
    });

    var NetworkWidget = BaseWidget.extend({

      initialRowsRequest : 300,
      maxRows : 1000,

      initialize: function (options) {
        if (!options.endpoint) {
          throw new Error("widget was not configured with an endpoint");
        }
        if (options.broadcastFilteredQuery) {
          this.broadcastFilteredQuery = options.broadcastFilteredQuery;
        }

        this.model = new ContainerModel({"maxRows": this.maxRows});
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

        this.listenTo(this.view, "filter:initiated", this.broadcastFilteredQuery);
        this.listenTo(this.view, "close", this.broadcastClose);

      },

      //when a user requests a different number of documents
      requestDifferentRows : function(model, rows){

        var query = this.getCurrentQuery().clone();
        query.unlock();
        query.set("rows", this.model.get("userVal"));
        query.unset("hl");
        query.unset("hl.fl");

        var request = new ApiRequest({
          target: Marionette.getOption(this, "endpoint"),
          query: query
        });

//        var options = {};
//        options.url = "http://127.0.0.1:5000/paper-network?"
//        request.set('options', options);

        this.pubsub.publish(this.pubsub.EXECUTE_REQUEST, request);
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
        this.filterCollection.reset(null);
        //reset model
        this.model.set(_.result(this.model, "defaults"), {silent: true});
      },

      destroyWidget: function () {
        //close graphView
        if (this.view.graphView) {
          this.view.graphView.close();
        }
        this.view.graphView.close();
      },

      //fetch data
      onShow: function () {

        var query = this.getCurrentQuery().clone();
        query.unlock();
        query.set("rows", this.initialRowsRequest);
        query.unset("hl");
        query.unset("hl.fl");

        var request = new ApiRequest({
          target: Marionette.getOption(this, "endpoint"),
          query: query
        });

//        var options = {};
//        options.url = "http://127.0.0.1:5000/paper-network?"
//        request.set('options', options);

        this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, request);
      },

      processResponse: function (data) {
        //force a reset even if the data is the same
        //so the earlier state of the widget is not preserved

        this.model.set({graphData: {}});
        data = data.toJSON();
        this.model.set({graphData: data.data});
        // let container view know how many bibcodes we have
        this.model.set({graphData : data.data,
          numFound: parseInt(data.msg.numFound),
          rows: parseInt(data.msg.rows)
        });
      },

      broadcastFilteredQuery: function () {
        var filterCollection = this.filterCollection,
          data = this.model.get("graphData").root,
          finalFQString, authorNames, groupAuthorNames, connector,
          names;

        var updater = new ApiQueryUpdater("fq");

        // get all author names
        authorNames = filterCollection.chain().filter(function (n) {
          return !isInt(n.get("name"));
        }).map(function (n) {
          return updater.quote(n.get("name"))
        }).value();

        groupAuthorNames = filterCollection.chain().filter(function (n) {
          return isInt(n.get("name"));
        }).map(function (n) {
          return n.get("name")
        }).map(function (number) {
          var group = _.filter(data.children, function (n) {
            return n.name == number
          })[0];
          return  _.map(group.children, function (g) {
            return updater.quote(g.name);
          })
        }).map(function (authorList) {
          return "(" + _.sortBy(authorList, function(a){return a}).join(" OR ") + ")"
        }).value();

        connector = (groupAuthorNames.length && authorNames.length) ? " OR " : "";
        finalFQString = authorNames.join(" OR ") + connector + groupAuthorNames;

        if (!finalFQString) {
          return
        }

        names = "author:(" + finalFQString + ")";
        var newQuery = this.getCurrentQuery().clone();
        newQuery.unlock();

        updater.updateQuery(newQuery, "fq", "limit", names);
        this.resetWidget();
        this.pubsub.publish(this.pubsub.START_SEARCH, newQuery);
      },

      broadcastClose: function () {
        this.resetWidget();
        this.pubsub.publish(this.pubsub.NAVIGATE, "results-page");
      }

    });

    return NetworkWidget

  });