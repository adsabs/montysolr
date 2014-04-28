define(['marionette', 'd3', 'jquery-ui', 'hbs!../templates/facet-slider'], function(Marionette, d3, $, facetSliderTemplate) {


    var FacetSliderView = Marionette.ItemView.extend({
        template: facetSliderTemplate,

        events: {
            "slide .graph-slider": "showGraph",
            "click .facet": "toggleFacet",
            "slide .slider": "toggleApply",
            "click .apply": "submitFacet"


        },


        toggleFacet: function() {
            if (this.$(".facet").hasClass("facet-open")) {
                this.$(".facet").removeClass("facet-open");
                this.$(".facet").addClass("facet-closed");
                this.$(".graph").addClass("hide");

            } else {
                this.$(".facet").removeClass("facet-closed");
                this.$(".facet").addClass("facet-open");
                this.$(".graph").removeClass("hide");

            }
        },

        toggleApply: function(e, ui) {
            if (ui.value !== this.model.get("facetData")) {
                this.$(".apply").removeClass("no-show");
            } else {
                this.$(".apply").addClass("no-show");

            }

        },

        submitFacet: function() {
            val = this.$(".slider").slider("value");
            console.log(val)

        },

        getGraphData: function() {
            var fData = _.pairs(this.model.get("currentFacetData"));
            //put into nice d3 format
            fData = _.map(fData, function(x) {
                return {
                    'name': x[0],
                    'value': x[1]
                }
            })
            return fData
        },

        makeGraphSlider: function() {
            this.$(".slider").addClass("graph-slider");
            this.$(".graph").append("<div class=\"chart\"></div")

            var graphDiv = d3.select(".chart");

            return

            var width = 200,
                height = 100;

            var y = d3.scale.linear()
                .range([height, 0]);

            var chart = d3.select(graphDiv)
                .attr("width", width)
                .attr("height", height);

            y.domain([0, d3.max(data, function(d) {
                return d.value;
            })]);

            var barWidth = width / data.length;

            var bar = chart.selectAll("g")
                .data(data)
                .enter().append("g")
                .attr("transform", function(d, i) {
                    return "translate(" + i * barWidth + ",0)";
                })

            bar.append("rect")
                .attr("y", function(d) {
                    return y(d.value);
                })
                .attr("height", function(d) {
                    return height - y(d.value);
                })
                .attr("width", barWidth - 1)
                .attr("fill", "orange");;

            bar.append("text")
                .attr("x", barWidth / 2)
                .attr("y", function(d) {
                    return y(d.value) + 3;
                })
                .attr("dy", ".75em")
                .text(function(d) {
                    return d.value;
                });

            function type(d) {
                d.value = +d.value; // coerce to number
                return d;
            }


            this.$(".slider").slider({
                range: true,
                min: 0,
                max: 2014,
                values: [0, 2014],
                slide: function(event, ui) {
                    console.log(ui)
                }
            });
        },

        makeSimpleSlider: function() {
            var that = this;

            var maxVal = this.model.get("currentFacetData");

            this.$(".slider").slider({
                min: 0,
                max: maxVal,
                value: maxVal,
                slide: function(event, ui) {
                    that.$(".show-slider-data").val(ui.value)
                }

            });

            that.$(".show-slider-data").val(maxVal);
        },

        facetDataRender: function() {
            if (this.model.get("facetType").graph === true) {
                var data = this.getGraphData();
                // this.makeGraphSlider();
            } else {
                this.makeSimpleSlider();
            }
        },

        showGraph: function() {
            this.$(".graph").removeClass("hide")
        },


        initialize: function(options) {

            //assign model id (may get rid of this later)
            this.id = this.model.get("solrFacet");
            //re-insert on model change
            this.listenTo(this.model, "change:currentFacetData", this.facetDataRender);
            this.listenTo(this.model, "newHierFacetsReady", this.showHier);


        },

        onRender: function() {
            //initiate slider

            //insert facet data
            this.facetDataRender();

            if (!_.isEmpty(this.model.get("currentFacetData"))) {
                this.facetDataRender()
            }
        }


    })

    return FacetSliderView

})
