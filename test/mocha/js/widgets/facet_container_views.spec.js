/**
 * Created by rchyla on 2014-05-14.
 */

define(['js/widgets/facet/base_container_view', 
    'js/widgets/facet/change_apply_container_view',
    'js/widgets/facet/logic_container_view',
    'js/widgets/facet/collection',
    'js/widgets/facet/item_views'],
  function (BaseContainerView, ChangeApplyContainerView, SelectLogicContainerView, FacetCollection, AdditionalViews) {

  describe("Facet Container Views (UI)", function () {
    var changeApply, el;

    before(function () {
      var fakeFacetData = [
        {
          title: "Wang, J (1496)",
          valWithoutSlash: "Wang, J",
          value: "0/Wang, J"
        }
      ];

      var c = new FacetCollection(fakeFacetData);
      changeApply = new ChangeApplyContainerView({
        collection: c,
        model: new Backbone.Model({title: "testTitle"}),
        itemView: AdditionalViews.CheckboxOneLevelView
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
          title: "Wang, J (1496)",
          valWithoutSlash: "Wang, J",
          value: "0/Wang, J"
        }
      ];

      var c = new FacetCollection(fakeFacetData);
      var containerModel = new SelectLogicContainerView.prototype.ModelClass({
        title: "testTitle",
        value: "testTitleVal"

      });

      spy = sinon.spy(SelectLogicContainerView.prototype, "changeLogicAndSubmit");

      logicSelect = new SelectLogicContainerView({
        collection: c,
        model: containerModel,
        itemView: AdditionalViews.CheckboxOneLevelView,
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
    it("should issue a 'containerLogicSelected' event when user chooses logic option", function () {
      $("#test").append(el);

      logicSelect.$(".logic-container input[type=radio]").eq(0).trigger("change");

      expect(spy.callCount).to.equal(1);

    });

  });


});