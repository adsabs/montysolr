define(['./base_graph', 'hbs!./templates/year-graph-legend'],
  function(BaseGraphView, legendTemplate){


   var YearGraphView = BaseGraphView.extend({

     id : "year-graph",

     legendTemplate : legendTemplate,


     buildGraph  : function () {

       var that = this;

       var data, xLabels, x, y, xAxis, yAxis, chart, bar;

       data = _.clone(this.graphData);

       data = this.binData(data, this.bins);

       xLabels = _.pluck(data, "x"), maxVal = d3.max(_.pluck(data, "y"));

       x = d3.scale.ordinal().domain(xLabels).rangeRoundBands([0, this.width], .1);

       y = d3.scale.linear().domain([0, maxVal]).range([this.height, 0]);

       xAxis = d3.svg.axis().scale(x).orient("bottom");

       yAxis = d3.svg.axis().scale(y).orient("left").tickFormat(d3.format("s"));

       chart = d3.select(this.el).select(".chart").attr("width", this.fullWidth).attr("height", this.fullHeight);

       this.innerChart = chart.append("g").classed("inner-chart", true).attr("transform", "translate(" + this.margin.left + "," + this.margin.top + ")");

       bar = this.innerChart.selectAll("g").data(data).enter().append("g").classed("bar", true).attr("transform", function (d) {
         return "translate(" + x(d.x) + ",0)";
       });

       //full bar
       bar.append("rect").classed("full-bar", true)
         .attr("y", function (d) {
           return y(d.y);
         }).attr("height", function (d) {
           return that.height - y(d.y);
         }).attr("width", function (d) {
           //console.log(that.binSize, x.rangeBand(), d.width)
           return x.rangeBand() * d.width / that.binSize
         });

       //refereed bar
       bar.append("rect").classed("refereed", true)
         .attr("y", function (d) {
           return y(d.refCount);
         }).attr("height", function (d) {
           return that.height - y(d.refCount);
         }).attr("width", function (d) {
           //console.log(that.binSize, x.rangeBand(), d.width)
           return x.rangeBand() * d.width / that.binSize
         });

       this.innerChart.append("g").classed({
         "axis"  : true,
         "x-axis": true
       }).attr("transform", "translate(0," + this.height + ")")
         .call(xAxis).selectAll("text").style("text-anchor", "end")
         .attr("dx", "-.8em").attr("dy", ".15em")
         .attr("transform", function (d) {
           return "rotate(-65)"
         });

       this.innerChart.append("g").classed({
         "axis"  : true,
         "y-axis": true
       }).call(yAxis);


     },

     //takes current data and binNum, returns correctly binned data
     binData    : function (data, binNum) {
       var extraBar, remainder, binSize, indexList, binnedX, binnedY;

       if (data.length <= binNum) {
         this.binSize = 1;

         data = _.map(data, function (d, i) {
           return {
             x    : "" + d.x,
             y    : d.y,
             width: 1,
             refCount : d.refCount
           }
         })
         return data
       }

       extraBar = undefined;
       binSize = Math.floor(data.length / binNum);
       //storing for bar width calculations
       this.binSize = binSize;
       remainder = data.length % binNum;
       if (remainder) {
         binNum += (Math.floor(remainder / binSize))
         remainder = remainder % binSize
         if (remainder) {
           extraBar = remainder;
         }
       }

       indexList = [];
       binnedX = [];
       binnedY = [];

       for (var i = 0; i < binNum; i++) {
         indexList.push(binSize)
       }
       if (extraBar) {
         indexList.push(extraBar)
       }

       _.each(indexList, function (d, i) {
         var totalCount = 0;
         var refCount = 0;
         var dateRange = [];
         while (d >= 1) {
           var v = data[0];
           dateRange.push(v.x);
           totalCount += v.y;
           refCount += v.refCount

           data.shift();
           d -= 1
         }
         binnedX.push(dateRange);
         binnedY.push({totalCount: totalCount, refCount: refCount})
       })

       binnedX = _.map(binnedX, function (d, i) {
         if (d.length > 1) {
           return {
             x    : d[0] + "-" + d[d.length - 1],
             width: d.length

           }
         }
         else {
           //a remainder bar
           return {
             x    : d[0] + "",
             width: 1
           }
         }
       })

       data = _.map(binnedX, function (d, i) {
         return {
           x       : d.x,
           y       : binnedY[i]["totalCount"],
           refCount: binnedY[i]["refCount"],
           width   : d.width
         }
       });

       return data
     },

     graphChange: function (val1, val2) {

       var that = this;

       var data, x, xLabels, y, xAxis, yAxis, bar;

       data = _.clone(this.graphData);


       //now getting rid of anything outside of the new bounds
       data = _.filter(data, function (d, i) {
         return (d.x >= val1 && d.x <= val2)
       })

       data = this.binData(data, this.bins);

       if (this.sizeSort === true) {
         data = _.sortBy(data, function (d) {
           return d.y
         });
       }

       xLabels = _.pluck(data, "x"), maxVal = d3.max(_.pluck(data, "y"));

       x = d3.scale.ordinal().domain(xLabels).rangeRoundBands([0, this.width], .1)

       y = d3.scale.linear().domain([0, maxVal]).range([this.height, 0]);

       xAxis = d3.svg.axis().scale(x).orient("bottom");

       yAxis = d3.svg.axis().scale(y).orient("left").tickFormat(d3.format("s"));

       bar = this.innerChart.selectAll(".bar").data(data);

       bar.exit().remove();

       var enterSelection = bar.enter().append("g").classed("bar", true);

       enterSelection.append("rect").classed("full-bar", true);

       enterSelection.append("rect").classed("refereed", true)

       //update selection is within bar
       bar.attr("transform", function (d) {
         return "translate(" + x(d.x) + ",0)";
       }).transition().select("rect").attr("y", function (d) {
         return y(d.y);
       }).attr("height", function (d) {
         return that.height - y(d.y);
       }).attr("width", function (d) {
         return x.rangeBand() * d.width / that.binSize
       })

       bar.select(".refereed").transition().attr("y", function (d) {
         return y(d.refCount);
       }).attr("height", function (d) {
         return that.height - y(d.refCount);
       }).attr("width", function (d) {
         return x.rangeBand() * d.width / that.binSize
       });

       d3.select(this.el)
         .select(".x-axis")
         .call(xAxis)
         .selectAll("text")
         .style("text-anchor", "end")
         .attr("dx", "-.8em")
         .attr("dy", ".15em")
         .attr("transform", function (d) {
           return "rotate(-65)"
         });

       d3.select(this.el)
         .select(".y-axis")
         .transition()
         .call(yAxis);

       /*add hover event listener
        have to do this again because delegated events don't
        work with svg :( */

       this.addChartEventListeners();
     },

     buildSlider: function () {

       var that = this;
       var data = _.clone(this.graphData);
       min = data[0].x, max = data[data.length - 1].x;

       this.$(".slider").slider({
         range : true,
         min   : min,
         max   : max,
         values: [min, max],
         stop  : function (event, ui) {
           var ui1 = ui.values[0], ui2 = ui.values[1];
           if (!(ui1 === min && ui2 === max)) {
             that.$(".apply").removeClass("hidden");
             that.trigger("facet:active")
           }
           else {
             that.$(".apply").addClass("hidden");
             that.trigger("facet:inactive");
           }
           that.graphChange(ui1, ui2)
         },
         slide : function (event, ui) {
           var ui1 = ui.values[0], ui2 = ui.values[1];
           that.$(".show-slider-data-first").val(ui1)
           that.$(".show-slider-data-second").val(ui2)
         }

       });
       this.$(".show-slider-data-first").val(min);
       this.$(".show-slider-data-second").val(max);
     },

     addSliderWindows : function(){
       this.$(".slider-data").html("<input type=\"text\" class=\"show-slider-data-first\"></input> to" +
         " <input type=\"text\" class=\"show-slider-data-second\"></input>");
    },

     triggerGraphChange: function () {
       var val1, val2;
       val1 = this.$(".show-slider-data-first").val();
       val2 = this.$(".show-slider-data-second").val();

       this.$(".slider").slider("values", [val1, val2]);

       this.graphChange(val1, val2)
     },



     addChartEventListeners: function () {

       var that = this;

       this.$(".refereed, .full-bar").on("mouseenter", function (e) {

         var $ct = $(e.currentTarget);
         var b = d3.select($ct.parent()[0]);
         //change color
         b.select(".full-bar").style("fill", "#61ca7a");
         b.select(".refereed").style("fill", "#1D6BB5")

         var i = that.$(".bar").index($ct.parent())

         var t = that.innerChart.select(".x-axis").selectAll(".tick")[0][i];
         d3.select(t).classed("active", true);

       });

       this.$(".refereed, .full-bar").on("mouseout", function (e) {

         var $ct = $(e.currentTarget);

         var b = d3.select($ct.parent()[0]);
         //change color
         b.select(".full-bar").style("fill", null);
         b.select(".refereed").style("fill", null);

         var i = that.$(".bar").index($ct.parent())

         var t = that.innerChart.select(".x-axis").selectAll(".tick")[0][i];
         d3.select(t).classed("active", false);
       });
     },

     submitFacet: function () {
       this.model.set("value", this.$(".slider").slider("values").join("-"));
       this.trigger('itemClicked');
     }

   })



  return YearGraphView


})