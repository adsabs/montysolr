/**
 * Created by alex on 5/15/14.
 */
define(['marionette', 'backbone', 'js/widgets/tabs/tabs_widget'], function (Marionette, Backbone, TabsWidget) {
  describe("Tabs (UI Widget)", function () {

    var tabs

    var removeCount = 0;


    beforeEach(function () {
      view1 = new Backbone.View()
      view1.render = function () {
        this.$el.html("<p>this is view 1</p>")
        return this
      }

      view1.remove = function(){
        removeCount++
      }

      view2 = new Backbone.View()
      view2.render = function () {
        this.$el.html("<p>this is view 2<p/>")
        return this
      }
      view2.remove = function(){
        removeCount++
      }
      tabs = new TabsWidget({tabs: [
        {title: "view1", view: view1, id: "view1", default: true},
        {title: "view2", view: view2, id: "view2", default: false}
      ]})

      $("#test").append(tabs.render().el)

    })

    afterEach(function(){
      $("#test").empty();
    })

    it("should display the default view on render", function () {
      expect($("#test div.active p").text()).to.match(/this is view 1/)

    })

    it("should react to a click on another tab by displaying its view", function () {
      $("#test li:not(.active) a").click();

      expect($("#test div.active p").text()).to.match(/this is view 2/)


    })

    it("should close (Marionette) or remove (Backbone) all views when it itself is closed", function(){

     tabs.close()

     expect(removeCount).to.equal(2)
    })

  })

  return {};
});
