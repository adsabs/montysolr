define(['jquery', 'js/widgets/search_bar/search_bar_widget',
  'js/components/beehive', 'js/services/pubsub', 'js/components/api_query', 'hoverIntent'],
  function($, SearchBarWidget, BeeHive, PubSub, ApiQuery) {


  describe("Search Bar (UI Widget)", function() {

    var beehive, pubsub, key, widget, $w;

    beforeEach(function(done) {
      beehive = new BeeHive();
      pubsub = new PubSub();
      beehive.addService('PubSub', pubsub);
      key = pubsub.getPubSubKey();

      widget = new SearchBarWidget();
      widget.activate(beehive.getHardenedInstance());
      var w = widget.render().el;

      $("#test").append(w)


      done();

    });

    afterEach(function(done) {
      beehive, pubsub, key, widget, $w = undefined;
      $("#test").empty();

      done();
    });


    it("should render a search bar and a submit button", function(done) {

      expect($(".q").length).to.equal(1);
      expect($(".search-submit").length).to.equal(1);
      done();
    });

    it("should trigger a START_SEARCH when the search-submit button is pressed", function(done) {

      var triggered = false;



      pubsub.subscribe(key, pubsub.START_SEARCH, function(apiQuery) {
        triggered = true;
      });

      $(".q").val("author:kurtz,m");
      $(".search-submit").click();

      expect(triggered).to.eql(true);
      done();

    });

    it("should allow the user to open and close a dropdown menu from the search bar", function(done){
      expect( widget.view.$(".input-group-btn").hasClass("open")).to.equal(false);
      widget.view.$(".show-form").click();
      expect( widget.view.$(".input-group-btn").hasClass("open")).to.equal(true);
      widget.view.$(".show-form").click();
      expect( widget.view.$(".input-group-btn").hasClass("open")).to.equal(false);
      done();
    });


    it("should allow the user to click to add fielded search words to search bar", function(done) {

//      can't easily trigger hoverIntent so calling the method directly
      var e = {};
      e.preventDefault = function(){};
      e.target = document.querySelector("#field-options div[data-field=author]");

      widget.view.$("#field-options div[data-field=author]").click();
      expect($(".q").val().trim()).to.equal("author:\"\"");
      done();
    });

    it("shows the query builder form on clicking 'Search Form' ", function() {

    });

    it("in QB form: when rule is removed, the form stays open", function() {

    });

    it("typing inside one of the input fields updates the search input", function() {

    });

    it("changing operator updates the search input", function() {

    });

    it("removing the rule/group updates the input", function() {

    });

    it("clicking outside the QB area, hides QB form", function() {

    });

    it("spinning wheel is shown when the query cannot be parsed immediately", function() {

    });

    it("you can type into the QB form without typing into search form first", function() {

    });

    it("you can create several input fields, some empty, and the form indicates which one are wrong", function() {

    });

    it("when the QB form opens, the search bar should become little opaque", function() {

    });

    it("when some of the fields have wrong input (and the query doesn't contain everything), the form should warn user before closing itself", function() {

    });

  });

})
