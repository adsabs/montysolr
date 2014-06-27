/**
 * Created by alex on 5/15/14.
 */
define(['marionette', 'backbone', 'js/widgets/tabs/tabs_widget'], function (Marionette, Backbone, TabsWidget) {
  describe("Tabs (UI Widget)", function () {

    var tabs;
    var removeCount = 0;


    beforeEach(function (done) {
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
        {title: "view1", widget: view1, id: "view1", default: true},
        {title: "view2", widget: view2, id: "view2", default: false}
      ]})

      $("#test").append(tabs.render().el)
      done();
    })

    afterEach(function(done){
      $("#test").empty();
      done();
    });

    it("should display the default view on render", function (done) {
      expect($("#test div.active p").text()).to.match(/this is view 1/);
      done();

    });

    it("should react to a click on another tab by displaying its view", function (done) {
      $("#test li:not(.active) a").click();

      expect($("#test div.active p").text()).to.match(/this is view 2/);
      done();

    });

    it("should close (Marionette) or remove (Backbone) all views when it itself is closed", function(done){

     tabs.close();

     expect(removeCount).to.equal(2);
     done();
    });

  });
});
