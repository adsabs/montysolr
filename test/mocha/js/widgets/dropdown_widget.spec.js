define([
  'js/widgets/dropdown-menu/widget',
  'js/bugutils/minimal_pubsub'


], function (DropdownWidget, MinPubSub) {

  describe("Dropdown Widget (UI Widget)", function () {

    afterEach(function () {
      $("#test").empty();
    })


    it("should be a dropdown that shows a list of links ", function () {

      var links = [
        {href: '/test', description: 'Author Network', navEvent: 'show-author-network'}

      ];

      var btnType = "btn-primary-faded";
      var dropdownTitle = "Visualize";

      widget = new DropdownWidget({links: links, btnType: btnType, dropdownTitle: dropdownTitle });
      $("#test").append(widget.render().el);

      expect($("#test").find("li:first").text().trim()).to.eql("Author Network");
      expect($("#test").find("li:first a").attr("href")).to.eql("/test");

    })


    it("should emit a NAVIGATE event with the proper parameter", function () {

      var links = [
        {href: '/test', description: 'Author Network', navEvent: 'show-author-network'}

      ];

      var btnType = "btn-primary-faded";
      var dropdownTitle = "Visualize";

      var widget = new DropdownWidget({links: links, btnType: btnType, dropdownTitle: dropdownTitle });

      var spy = sinon.spy();
      widget.getPubSub = function(){return {publish : spy}};

      $("#test").append(widget.render().el);

      $("#test").find("li:first").click();

      expect(widget.getPubSub().publish.calledOnce).to.be.true;

      //it should be passed the model that was clicked, so it can publish the name
      expect(spy.args[0][1]).to.eql("show-author-network");
      expect(spy.args[0][2]).to.eql({onlySelected: false});

    });

    it("should allow the user to do bulk action to all records or selected records if records are selected", function(){

      var links = [
        {href: '/test', description: 'Author Network', navEvent: 'show-author-network'}

      ];

      var btnType = "btn-primary-faded";
      var dropdownTitle = "Visualize";

      var widget = new DropdownWidget({links: links, btnType: btnType, dropdownTitle: dropdownTitle, selectedOption : true  });

      var spy = sinon.spy();
      widget.getPubSub = function() {return {publish : spy}};


      $("#test").append(widget.render().el);

      //signal that papers are available
      widget.onStoragePaperChange(5);

      $("#test").find("li:first").click();

      expect(widget.getPubSub().publish.calledOnce).to.be.true;
      expect(widget.getPubSub().publish.args[0][1]).to.eql('show-author-network');

      expect(widget.getPubSub().publish.args[0][2]).to.eql({onlySelected: true});

      $("input[name=papers-Visualize]").click();
      $("#test").find("li:first").click();

      expect(widget.getPubSub().publish.callCount).to.eql(2);
      expect(widget.getPubSub().publish.args[0][1]).to.eql('show-author-network');
      expect(widget.getPubSub().publish.args[1][2]).to.eql({onlySelected: false});


      //now the user decides that even though there are selected docs, he/she wants to see all of them






    })


  })


})