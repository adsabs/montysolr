define(['marionette',
    'd3',
    'jquery',
    'jquery-ui',
  'js/widgets/base/item_view',
    'hbs!./templates/graph',
    'hbs!./templates/axis-titles-template'
  ],
  function (Marionette,
            d3,
            $,
            $ui,
            BaseItemView,
            FacetGraphTemplate,
            axisTitlesTemplate

    ) {

  var ZoomableGraphView = BaseItemView.extend({

    className : "graph-facet",

    initialize   : function (options) {

      this.yAxisTitle = options.yAxisTitle;
      this.xAxisTitle = options.xAxisTitle;
      this.graphTitle = options.graphTitle;
      this.pastTenseTitle = options.pastTenseTitle;

      this.id = this.graphTitle + "-graph";

      //setting some constants for the graph
      this.bins = 12; //will be around 12, depending on remainders
      this.margin = {
        top   : 0,
        right : 0,
        bottom: 35,
        left  : 40
      };
      this.fullWidth = 280;
      this.fullHeight = 200;

      this.width = this.fullWidth - this.margin.left - this.margin.right;
      this.height = this.fullHeight - this.margin.top - this.margin.bottom;

      try {
        this.graphData = this.model.toJSON().graphData;
      } catch (e) {
        throw new Error("Graph widget has no model or else an incorrect model")
      }

      //for citation and reads graph
      this.currentScale = "linear"

      this.on("facet:inactive", function(){console.log("inactive!"), this.hideApplyButton()});
      this.on("facet:active", this.pulseApplyButton)

    },

    template: FacetGraphTemplate,

    insertLegend : function(){
      var graphVars = {}
      if (this.graphTitle){
        graphVars.graphTitle = this.graphTitle;
      }
      this.$(".graph-legend").html(this.legendTemplate(graphVars));
    },

    insertAxisTitles : function(){
      //this is getting inserted after the

      var axisVars = {};
      axisVars.yAxisTitle = this.yAxisTitle;
      axisVars.xAxisTitle = this.xAxisTitle;
      axisVars.xAxisClassName = this.xAxisClassName
      this.$("svg").after(axisTitlesTemplate(axisVars));

    },

    events: {
      "click .apply"         : "submitFacet",
      "blur input[type=text]": "triggerGraphChange"
    },

    hideApplyButton : function(){

      this.$(".apply").addClass("hidden")

    },

    pulseApplyButton : function(){

      var that = this;

      this.$(".apply").addClass("draw-attention-primary-faded");


      //this initiates an animation that lasts for 6 second

      setTimeout(function(){

        this.$(".apply").removeClass("draw-attention-primary-faded");

      }, 2000)

    },


    onRender: function () {

      if (this.model.get("graphData").length < 2){

        this.$el.html("Too little data to make a useful graph.")

      }
      else {
        this.insertLegend();
        this.buildGraph();
        this.insertAxisTitles();
        this.addSliderWindows();
        this.buildSlider();
        if (this.addToOnRender){
          this.addToOnRender()
        }
        if (this.addChartEventListeners){
          this.addChartEventListeners();
        }
      }
    }

  });

  return ZoomableGraphView;

});
