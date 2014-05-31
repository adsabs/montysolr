/**
 */

define([
    'marionette',
    'js/widgets/facet/zoomable_graph_view',
    'js/widgets/facet/collection'
    ],
  function (Marionette, ZoomableGraphView, FacetCollection) {

      describe("ZoomableBarChart", function () {
        var fakeGraphData, fakeModel;

        before(function () {
          fakeGraphData = [
            {
              x: 2000,
              y: 100
            },
            {
              x: 2002,
              y: 200
            },
            {
              x: 2004,
              y: 300
            }
          ];

          fakeModel = new FacetCollection({
            graphInfo: fakeGraphData,
            value    : [2000, 2004]
          }).models[0];
        });

        after(function () {
          $("#test").empty()
        });

        it("should inherit from Marionette ItemView", function () {
          expect(new ZoomableGraphView({xAxisTitle: "Year", model: fakeModel})).to.be.instanceof(Marionette.ItemView)

        });

        it("should render a bar chart and a slider", function () {

          var graphView = new ZoomableGraphView({
            facetField     : "test",
            userFacingName: "Year Test",
            xAxisTitle    : "Year",
            model         : fakeModel
          });

          var $w = $(graphView.render().el);
          //$("#test").append($w);
          
          expect($w.find("svg.chart").length).to.equal(1);
          expect($w.find("div.slider").length).to.equal(1);
        });

        it("should render an appropriate number of bars in the graph", function () {
          var graphView = new ZoomableGraphView({
            facetField     : "test",
            userFacingName: "Year Test",
            xAxisTitle    : "Year",
            model         : fakeModel
          });
          var $w = $(graphView.render().el);
          //since there are too few data points to bin them, we expect five, one for each year
          //(including missing years)
          expect($w.find("g.bar").length).to.equal(5)
        });

        it("should respond to changed values by re-rendering (zooming) graph with new number of bars", function (done) {

          var graphView = new ZoomableGraphView({
            facetField     : "test",
            userFacingName: "Year Test",
            xAxisTitle    : "Year",
            model         : fakeModel
          });
          var $w = $(graphView.render().el);

          $w.find(".show-slider-data-first").val(2002).blur();
          expect($w.find("g.bar").length).to.equal(3)


          //$("#test").append($w);

          done();

        });

      })

  });