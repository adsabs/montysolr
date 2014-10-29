

define(['./base_graph',
    'hbs!./templates/h-index-graph-legend',
    'hbs!./templates/h-index-slider-window'],


  function(BaseGraphView,
           legendTemplate,
           sliderWindowTemplate) {

    var HIndexGraphView = BaseGraphView.extend({

      legendTemplate: legendTemplate,

      xAxisClassName : "h-index-x-axis-title",

      addToOnRender: function () {
        //show the h index
        if (this.hIndex){
          this.$("#h-index-container").text(this.hIndex.x);

        }

      },

      events: {
        "click .apply"         : "submitFacet",
        "blur input[type=text]": "triggerGraphChange",
        //only relevant for reads and citation
        "change input[name*=scale]": "toggleScale"
      },


      buildGraph: function () {

        var data, xLabels, x, y, xAxis, yAxis, chart, line;

        data = _.clone(this.graphData);

        this.hIndex = data[_.indexOf(data, _.findWhere(data, function (d) {
          if (d.x > d.y) {
            return true
          }
        })) - 1];

        xLabels = _.pluck(data, "x");

        var yVals = _.pluck(data, "y");
        maxVal = d3.max(yVals);
        minVal = d3.min(yVals);

        var xDomain = [xLabels[0] - 1, xLabels[xLabels.length - 1] + 1];

        x = d3.scale.linear().domain(xDomain).range([0, this.width]);

        y = d3.scale.linear().domain([minVal - 1, maxVal + 1]).range([this.height, 0]);

        xAxis = d3.svg.axis().scale(x).orient("bottom").tickFormat(d3.format("d"));

        yAxis = d3.svg.axis().scale(y).orient("left").tickFormat(d3.format("d"));

        chart = d3.select(this.el).select(".chart").attr("width", this.fullWidth).attr("height", this.fullHeight);

        this.innerChart = chart.append("g").classed("inner-chart", true).attr("transform", "translate(" + this.margin.left + "," + this.margin.top + ")");

        this.innerChart.append("g").classed({
          "axis"  : true,
          "x-axis": true
        }).attr("transform", "translate(0," + this.height + ")").call(xAxis).selectAll("text").style("text-anchor", "end").attr("dx", "-.8em").attr("dy", ".15em").attr("transform", function (d) {
          return "rotate(-65)"
        });

        this.innerChart.append("g").classed({
          "axis"  : true,
          "y-axis": true
        }).call(yAxis);


        if (data.length >= 40) {

          line = d3.svg.line().x(function (d) {
            return x(d.x);
          }).y(function (d) {
            return y(d.y);
          });

          this.innerChart.append("path").datum(data).classed("line", true).attr("d", line);

        }
        else {

          //  show legend
          this.$(".ref-nonref").removeClass("hidden");

          var d = this.innerChart.selectAll("circle").data(data);

          d.enter().append("circle").attr("class", "dot")
            .attr("r", 4.5)
            .classed('refereed', function(d){
              if (d.refereed){
                return true
              }
            }).attr("cx", function (d) {
              return x(d.x)
            }).attr("cy", function (d) {
              return y(d.y)
            })

        }

        if (this.hIndex) {

          //first h index line
          this.innerChart.append("line").classed({"h-index": true, "h-index-1": true}).attr("x1", x(this.hIndex.x)).attr("y1", y(minVal - 1)).attr("x2", x(this.hIndex.x)).attr("y2", y(this.hIndex.y));

          //second h index line
          this.innerChart.append("line").classed({"h-index": true, "h-index-2": true}).attr("x1", x(xLabels[0] - 1)).attr("y1", y(this.hIndex.y)).attr("x2", x(this.hIndex.x)).attr("y2", y(this.hIndex.y));

        }

      },

      /*takes scale val (linear or log) and calls graphChange*/

      toggleScale: function(e){
        var v = $(e.target).attr("value");

        this.currentScale = v;

        this.graphChange();

      },

      graphChange: function (val) {

        var data, max, x, y, xAxis, yAxis;

        data = _.clone(this.graphData);

        max = data[data.length - 1].x;

        /* checking : do we need to signal
         that facet is active/ show apply button?
         */

        if (val !== max) {

          this.trigger("facet:active")
        }
        else {
          this.trigger("facet:inactive");
        }



        if (val) {
          this.limitVal = val;
        }
        else if (!this.limitVal) {
          this.limitVal = d3.max(_.pluck(data, "x"));;
        }
        //else, limitval has already been set previously


        //now getting rid of anything outside of the new bounds
        //(if log, y cant be less than 0)
        data = _.filter(data, function (d, i) {
          if (this.currentScale === "log"){
            return (d.x <= this.limitVal && d.y > 0)

          }else {
            return (d.x <= this.limitVal)
          }
        }, this);


        var xLabels = _.pluck(data, "x");
        var yVals = _.pluck(data, "y");
        maxVal = d3.max(yVals);
        minVal = d3.min(yVals);

        var xDomain = [xLabels[0] - 1, xLabels[xLabels.length - 1] + 1];

        x = d3.scale.linear().domain(xDomain).range([0, this.width]);


        xAxis = d3.svg.axis().scale(x).orient("bottom").tickFormat(d3.format("d"));


        if (this.currentScale === "linear"){
          y = d3.scale.linear().domain([minVal - 1 , maxVal + 1]).range([this.height, 0]);

        }
        else if (this.currentScale === "log"){
          y = d3.scale.log().domain([_.max([1, (minVal - 1)]), maxVal + 1]).range([this.height, 0]).clamp(true);

        }

        d3.select(this.el).select(".x-axis").call(xAxis).selectAll("text").style("text-anchor", "end").attr("dx", "-.8em").attr("dy", ".15em").attr("transform", function (d) {
          return "rotate(-65)"
        });

        yAxis = d3.svg.axis().scale(y).orient("left");

        if (this.currentScale === "log"){

          var numberFormat = d3.format(",f");
          function logFormat(d) {
            var x = Math.log(d) / Math.log(10) + 1e-6;
            return Math.abs(x - Math.floor(x)) < .7 ? numberFormat(d) : "";
          }

          yAxis.tickFormat(logFormat)
        }
        else {

          yAxis.tickFormat(d3.format("d"));

        }


        d3.select(this.el).select(".y-axis").transition().call(yAxis);

        if (this.hIndex) {
          if (_.contains(data, this.hIndex)) {
            this.innerChart.selectAll(".h-index").classed("hidden", false)
          }
          else {
            this.innerChart.selectAll(".h-index").classed("hidden", true)
          }

          //first h index line
          this.innerChart.select(".h-index-1").transition().attr("x1", x(this.hIndex.x)).attr("x2", x(this.hIndex.x)).attr("y2", y(this.hIndex.y))

          //second h index line
          this.innerChart.select(".h-index-2").transition().attr("y1", y(this.hIndex.y)).attr("x2", x(this.hIndex.x)).attr("y2", y(this.hIndex.y));

        }

        if (data.length >= 40) {

          //  show legend
          $(".ref-nonref").addClass("hidden");

          line = d3.svg.line().x(function (d) {
            return x(d.x);
          }).y(function (d) {
            return y(d.y);
          });

          this.innerChart.selectAll(".dot").classed("hidden", true)

          this.innerChart.selectAll(".line").datum(data).attr("d", line).classed("hidden", false)

        }
        else {

          //  show legend
          this.$(".ref-nonref").removeClass("hidden");

          this.innerChart.selectAll(".line").classed("hidden", true);

          this.innerChart.selectAll(".dot").classed("hidden", false)

          var d = this.innerChart.selectAll("circle").data(data);

          d.exit().remove();

          d.enter().append("circle").attr("class", "dot").attr("r", 4.5)

          d.classed('refereed', function(d){
            if (d.refereed){
              return true
            }
          }).attr("cx", function (d) {
            return x(d.x)
          }).attr("cy", function (d) {
            return y(d.y)
          })
        }

      },


      buildSlider: function () {

        var that = this;
        var data = _.clone(this.graphData);
        var max = data[data.length - 1].x;
        var min = data[0].x;

        this.$(".slider").slider({
          max  : max,
          min  : min,
          value: max,
          stop : function (event, ui) {

            that.graphChange(ui.value);

          },
          slide: function (event, ui) {
            that.$(".show-slider-data-first").val(ui.value)
          }

        });

        this.$(".show-slider-data-first").val(max);
      },

      addSliderWindows: function () {
        this.$(".slider-data").html(sliderWindowTemplate({pastTenseTitle : this.pastTenseTitle}));
      },

      triggerGraphChange: function () {

        var val;
        val = this.$(".show-slider-data-first").val();

        this.$(".slider").slider("value", val);

        this.graphChange(val)
      },

      submitFacet: function () {
        //find citation limit
        var limit = this.graphData[this.$(".slider").slider("value") - 1].y;
        this.model.set("value", "[" + limit + " TO 9999999]");
        this.trigger('itemClicked');
      }
    })

    return HIndexGraphView


  })