define(['backbone', 'marionette',
    './test_json/test1', './test_json/test2',
    'js/components/beehive', 'js/services/pubsub',
    'js/components/api_query', 'js/components/api_response',
    'js/widgets/facet/views/facet-container-views', 'js/widgets/facet/views/facet-item-views',
    'js/widgets/facet/facet-collection', 'js/widgets/facet/facet-controllers'
  ],

  function (Backbone, Marionette, Test1, Test2, BeeHive, PubSub, ApiQuery, ApiResponse, FacetContainerViews, FacetItemViews, IndividualFacetCollection, FacetControllers) {
    describe("facet widget (ui widget)", function () {
      describe("facet item views", function () {

        describe("simple checkbox view", function () {
          var sc, el;

          before(function () {
            var fakeFacetData = [
              {
                title          : "Wang, J (1496)",
                valWithoutSlash: "Wang, J",
                value          : "0/Wang, J"
              }
            ];

            var c = new IndividualFacetCollection(fakeFacetData);

            sc = new FacetItemViews.CheckboxOneLevelView({model: c.models[0]});
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

        describe("hierarchical checkbox view", function () {
          var hc, el;

          before(function () {
            var fakeFacetData = [
              {
                title          : "Wang, J (1496)",
                valWithoutSlash: "Wang, J",
                value          : "0/Wang, J"
              }
            ];

            var c = new IndividualFacetCollection(fakeFacetData);

            hc = new FacetItemViews.CheckboxHierarchicalView({model: c.models[0]});
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
            expect(hc.collection).to.be.instanceof(IndividualFacetCollection)
          })

        });

        describe("zoomable bar chart view", function () {
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
            var fakeModel = new IndividualFacetCollection({
              graphInfo: fakeGraphData,
              value    : [2000, 2004]
            }).models[0];

            var graphView = new FacetItemViews.ZoomableGraphView({
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
            expect(new FacetItemViews.ZoomableGraphView.prototype.constructor.__super__.constructor()).to.be.instanceof(Marionette.ItemView)

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

      describe("facet container views", function () {
        describe("change apply container view", function () {
          var changeApply, el;

          before(function () {
            var fakeFacetData = [
              {
                title          : "Wang, J (1496)",
                valWithoutSlash: "Wang, J",
                value          : "0/Wang, J"
              }
            ];

            var c = new IndividualFacetCollection(fakeFacetData);
            changeApply = new FacetContainerViews.ChangeApplyContainer({
              collection: c,
              model     : new Backbone.Model({title: "testTitle"}),
              itemView  : FacetItemViews.CheckboxOneLevelView
            })

            el = changeApply.render().el;

          });

          it("visually serves a blank container with only a title for the facet", function () {
            expect($(el).find("h5").text()).to.match(/testTitle/)

          });

          it("listens for a change in the view's collection (a model adds a \"new value\") then triggers submit event ", function () {
            expect(changeApply).to.not.trigger("changeApplySubmit").when(function () {
              changeApply.collection.models[0].set("title", "someFakeValue")
            })
            expect(changeApply).to.trigger("changeApplySubmit").when(function () {
              changeApply.collection.models[0].set("newValue", "someFakeValue")
            })

          })

        });

        describe("logic select container view", function () {
          var logicSelect, el, spy;

          before(function () {
            var fakeFacetData = [
              {
                title          : "Wang, J (1496)",
                valWithoutSlash: "Wang, J",
                value          : "0/Wang, J"
              }
            ];

            var c = new IndividualFacetCollection(fakeFacetData);
            var containerModel = new SelectLogicModel({
              title: "testTitle",
              value: "testTitleVal",

            });

            spy = sinon.spy(FacetContainerViews.SelectLogicContainer.prototype, "changeLogicAndSubmit");

            logicSelect = new FacetContainerViews.SelectLogicContainer({
              collection   : c,
              model        : containerModel,
              itemView     : FacetItemViews.CheckboxOneLevelView,
              openByDefault: true
            });

            el = logicSelect.render().el;

          });

          after(function () {
            $("#test").empty();
          })

          it("should have a logic dropdown initially hidden from view", function () {
            expect($(el).find(".logic-dropdown").length).to.equal(1);
            expect($(el).find(".logic-dropdown").hasClass("open")).to.equal(false);
            expect($(el).find(".dropdown-menu").text()).to.match(/\s*select one or more items\s*/);

          });

          it("should offer logic options when the user selects a facet", function () {
            $("#test").append(el);
            $("#test").find("input[type=checkbox]").click();
            expect($("#test").find(".logic-dropdown").hasClass("open")).to.equal(true);
            expect($("#test").find(".dropdown-menu").text()).match(/\s*limit to\s*/);

          });
          it("should issue a 'selectLogicSubmit' event when user chooses logic option", function () {
            $("#test").append(el);

            logicSelect.$(".logic-container input[type=radio]").eq(0).trigger("change");

            expect(spy.callCount).to.equal(1);

          });

        });
      });

      describe("facet collection", function () {
        /*this is checkbox-type data; data for graphs (change-apply) will consist of a series of 
         x-y values for aggregation and visualization*/
        var fakeFacetData = [
          {
            title          : " Wang, J (1496)",
            valWithoutSlash: "Wang, J",
            value          : "0/Wang, J"
          }
        ];

        var c = new IndividualFacetCollection(fakeFacetData);

        it("should have text preprocessing functions for raw solr data", function () {
          expect(c).to.have.property("titleCase");
          expect(c).to.have.property("allCaps");
          expect(c).to.have.property("removeSlash");

        })
        it("should initiate a facet model with appropriate default values", function () {
          //for change-apply containers
          expect(c.models[0].attributes).to.include.key("newValue");
          //for logic containers
          expect(c.models[0].attributes).to.include.key("selected");

        })

      });

      describe("facet controllers", function () {
        describe("base facet controller", function () {

          var base;

          before(function () {

            var fakeModel = new IndividualFacetCollection({
            }).models[0];

            var fakeFacetData = [
              {
                title          : " Wang, J (1496)",
                valWithoutSlash: "Wang, J",
                value          : "0/Wang, J"
              }
            ];

            var fakeContainerView = new FacetContainerViews.ChangeApplyContainer({
              model     : fakeModel,
              collection: new IndividualFacetCollection(fakeFacetData),
              itemView  : FacetItemViews.CheckboxOneLevelView,
              facetName : "TestFacet"

            })

            base = new FacetControllers.BaseFacetWidget({
              view     : fakeContainerView,
              facetName: "test"
            })

          })

          it("should require two config variables: an instantiated view with a collection, and the facet's solr name", function () {
            expect(function () {
              try {
                new FacetControllers.BaseFacetWidget()
              } catch (e) {
                throw e
              }
            }).to.throw(Error)

          })

          it("should provide two methods of applying facets to current search depending on type of facet", function () {
            expect(base).to.have.property("deliverApplySubmitQuery")
            expect(base).to.have.property("deliverSelectLogicSubmitQuery")
          })

          it("should inherit and provide methods of contacting pubsub")

        });

        describe("hierarchical facet controller", function () {
          var hierarchical, fakeHierRequest;

          before(function () {

            var fakeModel = new Backbone.Model();

            var fakeFacetData = [
              {
                title          : " Wang, J (1496)",
                valWithoutSlash: "Wang, J",
                value          : "0/Wang, J"
              }
            ];

           var fakeCollection =  new IndividualFacetCollection(fakeFacetData);

            var fakeContainerView = new FacetContainerViews.ChangeApplyContainer({
              model     : fakeModel,
              collection: fakeCollection,
              itemView  : FacetItemViews.CheckboxHierarchicalView,
              facetName : "TestFacet"
            })

           fakeHierRequest = sinon.stub(FacetControllers.HierarchicalFacetWidget.prototype, "requestChildData");

           hierarchical = new FacetControllers.HierarchicalFacetWidget({
              view     : fakeContainerView,
              facetName: "test"
            });


          })

          it("should extend base controller functionality with a way to get child data for a facet", function(){
            expect(hierarchical).to.have.property("requestChildData");
           hierarchical.view.trigger("hierarchicalDataRequest");
            expect(fakeHierRequest.callCount).to.equal(1)
          })


        });

      });

    })

  })
