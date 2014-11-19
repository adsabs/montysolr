define(['marionette',
    'd3',
    'jquery',
    'jquery-ui',
  'js/widgets/base/item_view',
    'hbs!./templates/graph'
  ],
  function (Marionette,
            d3,
            $,
            $ui,
            BaseItemView,
            FacetGraphTemplate

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
        right : 10,
        bottom: 50,
        left  : 50
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

    events: {
      "click .apply"         : "submitFacet",
      "blur input[type=text]": "triggerGraphChange"
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
