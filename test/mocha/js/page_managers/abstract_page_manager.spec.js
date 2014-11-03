define([
    'js/page_managers/abstract_page_controller',
  'js/widgets/base/base_widget'
  ],
  function(
    AbstractPageController,
    BaseWidget

    ){

    describe("Abstract Page Manager (Page Manager)", function(){

      var abstractPageController;

      beforeEach(function(){

        var fakeWidget = new BaseWidget();

       abstractPageController = new AbstractPageController({widgetDict : {fake : fakeWidget}});

        //page controller expects to be able to insert template here


        $("#test").append($("<div/>", {"id":"body-template-container"}))


      })

      it("should show a back button only when there is a system-wide query to return to", function(){

        abstractPageController.controllerView.onRender = function(){};

        abstractPageController.controllerView.render();

        //so onShow doesn't raise an error
        abstractPageController.controllerView.displaySearchBar = function(){};

        abstractPageController.insertAbstractControllerView();

        //a base widget will by default have an empty api query

        expect(abstractPageController.getCurrentQuery().url()).to.eql('');

        expect(abstractPageController.controllerView.$(".back-button").attr("href")).to.eql("")

        expect(abstractPageController.controllerView.$('.back-button').hasClass("hidden")).to.eql(true);

        abstractPageController.getCurrentQuery = function(){return {url : function(){return "fakeURL"}}};

        expect(abstractPageController.getCurrentQuery().url()).to.eql("fakeURL");

        //now, the button should be shown and it should have the proper href

        abstractPageController.insertAbstractControllerView();

        expect(abstractPageController.controllerView.$('.back-button').hasClass("hidden")).to.eql(false);

        expect(abstractPageController.controllerView.$(".back-button").attr("href")).to.eql("search/fakeURL")




      })


    })




})