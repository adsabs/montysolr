/**
 * Created by rchyla on 2014-05-14.
 */

define([
    'marionette',
    'js/widgets/facet/collection',
    'js/widgets/facet/change_apply_container_view',
    'js/widgets/facet/hierarchical_controller',
    'js/widgets/facet/base_controller',
    'js/widgets/facet/item_views'
  ],
  function (Marionette, FacetCollection, ChangeApplyContainerView, HierarchicalFacetController, BaseFacetController, AdditionalViews) {

    describe("Facet Controllers (UI)", function () {
      
      describe("BaseController", function () {

        var base;

        before(function () {

          var fakeModel = new FacetCollection({
          }).models[0];

          var fakeFacetData = [
            {
              title          : " Wang, J (1496)",
              valWithoutSlash: "Wang, J",
              value          : "0/Wang, J"
            }
          ];

          var fakeContainerView = new ChangeApplyContainerView({
            model     : fakeModel,
            collection: new FacetCollection(fakeFacetData),
            itemView  : AdditionalViews.CheckboxOneLevelView,
            facetName : "TestFacet"

          })

          base = new BaseFacetController({
            view     : fakeContainerView,
            facetName: "test"
          })

        })

        it("should require two config variables: an instantiated view with a collection, and the facet's solr name", function () {
          expect(function () {
            try {
              new BaseFacetController()
            } catch (e) {
              throw e
            }
          }).to.throw(Error)

        })

        it("should provide two methods of applying facets to current search depending on type of facet", function () {
          expect(base).to.have.property("onFacetApplySubmit")
          expect(base).to.have.property("onContainerLogicSelected")
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

          var fakeCollection =  new FacetCollection(fakeFacetData);

          var fakeContainerView = new ChangeApplyContainerView({
            model     : fakeModel,
            collection: fakeCollection,
            itemView  : AdditionalViews.CheckboxHierarchicalView,
            facetName : "TestFacet"
          })

          fakeHierRequest = sinon.stub(HierarchicalFacetController.prototype, "requestChildData");

          hierarchical = new HierarchicalFacetController({
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

  });