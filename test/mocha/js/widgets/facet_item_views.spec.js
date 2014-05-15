/**
 * Created by rchyla on 2014-05-14.
 */

define([
    'marionette',
    'js/widgets/facet/zoomable_graph_view',
    'js/widgets/facet/collection',
    'js/widgets/facet/item_views'],
  function (Marionette, ZoomableGraphView, FacetCollection, AdditionalViews) {

    describe("Facet Item Views (UI)", function () {

      describe("SimpleCheckbox", function () {
        var sc, el;

        before(function () {
          var fakeFacetData = [
            {
              title          : "Wang, J (1496)",
              valWithoutSlash: "Wang, J",
              value          : "0/Wang, J"
            }
          ];

          var c = new FacetCollection(fakeFacetData);

          sc = new AdditionalViews.CheckboxOneLevelView({model: c.models[0]});
          el = sc.render().el;
        });

        after(function () {
          $("#test").empty();
        })

        it("should inherit from Marionette ItemView", function () {
          expect(sc).to.be.instanceof(Marionette.ItemView)
        })

        it("should render the title as a label for a checkbox ", function () {

          expect($(el).find("input[type=checkbox]").length).to.equal(1)
          expect($(el).find("label").text()).to.match(/\s*Wang, J \(1496\)\s*/);

        })

        it("should update its model when facet is selected", function () {
          $("#test").append(el);
          $("#test").find("input").click();
          expect(sc.model.get("selected")).to.equal(true);
          $("#test").find("input").click();
          expect(sc.model.get("selected")).to.equal(false);

        })

      });

      describe("HierarchicalCheckbox", function () {
        var hc, el;

        before(function () {
          var fakeFacetData = [
            {
              title          : "Wang, J (1496)",
              valWithoutSlash: "Wang, J",
              value          : "0/Wang, J"
            }
          ];

          var c = new FacetCollection(fakeFacetData);

          hc = new AdditionalViews.CheckboxHierarchicalView({model: c.models[0]});
          el = hc.render().el;
        });

        after(function () {
          $("#test").empty();
        });

        it("should inherit from Marionette Composite View", function () {
          expect(hc).to.be.instanceof(Marionette.CompositeView);

        })

        it("should have the necessary attributes for composite functionality", function () {
          expect(hc).to.have.property("$itemViewContainer");
          //should initialize its own empty collection
          expect(hc.collection).to.be.instanceof(FacetCollection)
        })

      });

      describe("ZoomableBarChart", function () {
        var el, graphView;

        before(function () {
          var fakeGraphData = [
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
          ]
          var fakeModel = new FacetCollection({
            graphInfo: fakeGraphData,
            value    : [2000, 2004]
          }).models[0];

          var graphView = new ZoomableGraphView({
            facetName     : "test",
            userFacingName: "Year Test",
            xAxisTitle    : "Year",
            model         : fakeModel
          });

          el = graphView.render().el;
          $("#test").append(el)

        });

        after(function () {

          $("#test").empty()
        });

        it("should inherit from Marionette ItemView", function () {
          expect(new ZoomableGraphView.prototype.constructor.__super__.constructor()).to.be.instanceof(Marionette.ItemView)

        });

        it("should render a bar chart and a slider", function () {
          expect($(el).find("svg.chart").length).to.equal(1);
          expect($(el).find("div.slider").length).to.equal(1);
        })

        it("should render an appropriate number of bars in the graph", function () {
          //since there are too few data points to bin them, we expect five, one for each year
          //(including missing years)
          expect($(el).find("g.bar").length).to.equal(5)
        })

        it("should respond to changed values by re-rendering (zooming) graph with new number of bars", function (done) {

          $(el).find(".show-slider-data-first").val(2002).blur();
          expect($(el).find("g.bar").length).to.equal(3)

          done();

        })

      })

    });

  });