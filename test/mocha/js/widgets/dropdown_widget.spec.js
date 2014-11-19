define([
  'js/widgets/dropdown-menu/widget',
  'js/bugutils/minimal_pubsub'


], function (DropdownWidget, MinPubSub) {

  describe("Dropdown Widget (UI Widget)", function () {

    var widget, publish;


    beforeEach(function () {


      var links = [
        {href: '/test', description: 'Author Network', navEvent: 'show-author-network'}

      ];

      var btnType = "btn-primary-faded";

      var dropdownTitle = "Visualize";

      DropdownWidget.prototype.emitNavigateEvent = sinon.stub();

      widget = new DropdownWidget({links: links, btnType: btnType, dropdownTitle: dropdownTitle });


      $("#test").append(widget.render().el);

    })

    afterEach(function () {

      $("#test").empty();
    })


    it("should be a dropdown that shows a list of links ", function () {

      expect($("#test").find("li:first").text().trim()).to.eql("Author Network");
      expect($("#test").find("li:first a").attr("href")).to.eql("/test");


    })


    it("should emit a NAVIGATE event with the proper parameter", function () {


      $("#test").find("li:first").click();


      expect(widget.emitNavigateEvent.calledOnce).to.be.true;

      //it should be passed the model that was clicked, so it can publish the name

      expect(widget.emitNavigateEvent.args[0][0]).to.eql(widget.collection.models[0]);


    })


  })


})