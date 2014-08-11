

define(['./base-graph', 'hbs!./templates/citation-graph-legend', 'hbs!./templates/citation-slider-window'],


  function(BaseGraphView, legendTemplate, sliderWindowTemplate) {

    var CitationGraphView = BaseGraphView.extend({

      id: "citation-graph",

      legendTemplate: legendTemplate,

      addToOnRender: function () {
        //show the h index
        this.$("#h-index-container").text(this.hIndex.x);

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

        this.innerChart.append("path").datum(data).attr("class", "line").attr("d", line);

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
          $(".ref-nonref").removeClass("no-display");

          var d = this.innerChart.selectAll("circle").data(data);

          d.enter().append("circle").attr("class", "dot").attr("r", 4).style("fill", function (d) {
              if (d.refereed) {
                return "#1D6BB5"
              }
              else {
                return "#61ca7a"
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

      graphChange: function (val) {

        var data, x, xLabels, y, xAxis, yAxis;

        data = _.clone(this.graphData);

        //now getting rid of anything outside of the new bounds
        data = _.filter(data, function (d, i) {
          return (d.x <= val)
        })

        xLabels = _.pluck(data, "x");
        var yVals = _.pluck(data, "y");
        maxVal = d3.max(yVals);
        minVal = d3.min(yVals);

        var xDomain = [xLabels[0] - 1, xLabels[xLabels.length - 1] + 1];

        x = d3.scale.linear().domain(xDomain).range([0, this.width]);

        y = d3.scale.linear().domain([minVal - 1 , maxVal + 1]).range([this.height, 0]);

        xAxis = d3.svg.axis().scale(x).orient("bottom").tickFormat(d3.format("d"));
        ;

        yAxis = d3.svg.axis().scale(y).orient("left").tickFormat(d3.format("d"));

        d3.select(this.el).select(".x-axis").call(xAxis).selectAll("text").style("text-anchor", "end").attr("dx", "-.8em").attr("dy", ".15em").attr("transform", function (d) {
            return "rotate(-65)"
          });

        d3.select(this.el).select(".y-axis").transition().call(yAxis);

        if (this.hIndex) {
          if (_.contains(data, this.hIndex)) {
            d3.selectAll(".h-index").classed("no-display", false)
          }
          else {
            d3.selectAll(".h-index").classed("no-display", true)
          }

          //first h index line
          this.innerChart.select(".h-index-1").transition().attr("x1", x(this.hIndex.x)).attr("x2", x(this.hIndex.x)).attr("y2", y(this.hIndex.y))

          //second h index line
          this.innerChart.select(".h-index-2").transition().attr("y1", y(this.hIndex.y)).attr("x2", x(this.hIndex.x)).attr("y2", y(this.hIndex.y));

        }

        if (data.length >= 40) {

          //  show legend
          $(".ref-nonref").addClass("no-display");

          line = d3.svg.line().x(function (d) {
              return x(d.x);
            }).y(function (d) {
              return y(d.y);
            });

          d3.selectAll(".dot").classed("no-display", true)

          d3.selectAll(".line").datum(data).attr("d", line).classed("no-display", false)

        }
        else {

          //        show legend
          $(".ref-nonref").removeClass("no-display");

          d3.selectAll(".line").classed("no-display", true);

          d3.selectAll(".dot").classed("no-display", false)

          var d = this.innerChart.selectAll("circle").data(data);

          d.exit().remove();

          d.enter().append("circle").attr("class", "dot").attr("r", 4)

          d.transition().style("fill", function (d) {
              if (d.refereed) {
                return "#1D6BB5"
              }
              else {
                return "#61ca7a"
              }
            }).attr("cx", function (d) {
              return x(d.x)
            }).attr("cy", function (d) {
              return y(d.y)
            })
        }

      },

      addChartEventListeners: function () {

        var that = this;

        this.$(".dot").on("mouseenter", function (e) {

          var $ct = $(e.currentTarget);
          if ($ct.hasClass("refereed")) {
            $ct.css("fill", "#1D6BB5")
          }
          else {
            $ct.css("fill", "#61ca7a")
          }

          var i = that.$(".dot").index($ct)

          var t = that.innerChart.select(".x-axis").selectAll(".tick")[0][i];
          d3.select(t).classed("active", true);

        });

        this.$(".dot").on("mouseout", function (e) {

          var $ct = $(e.currentTarget);
          if ($ct.hasClass("refereed")) {
            $ct.css("fill", "#4184c4")
          }
          else {
            $ct.css("fill", "#97c7a3")
          }

          var i = that.$(".dot").index($ct)

          var t = that.innerChart.select(".x-axis").selectAll(".tick")[0][i];
          d3.select(t).classed("active", false);
        });

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

            if (ui.value < max) {
              that.$(".apply").removeClass("no-display");
              that.trigger("facet:active")

            }
            else {
              that.$(".apply").addClass("no-display");
              that.trigger("facet:inactive");
            }

            that.graphChange(ui.value);

          },
          slide: function (event, ui) {
            that.$(".show-slider-data-first").val(ui.value)
          }

        });

        this.$(".show-slider-data-first").val(max);
      },

      addSliderWindows: function () {
        this.$(".slider-data").html(sliderWindowTemplate());
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

    return CitationGraphView


  })