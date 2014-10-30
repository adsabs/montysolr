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
      e.target = document.querySelector("#field-options button[data-field=author]");

      widget.view.$("#field-options button[data-field=author]").click();
      expect($(".q").val().trim()).to.equal("author:\"\"");
      done();
    });

    it("adds a default sort value of pubdate if the query doesnt have an operator and doesn't already have a sort", function(done){


      expect(widget.changeDefaultSort).to.be.instanceof(Function);


      //should add pubdate sort if there is no sort

      var q1 = new ApiQuery({q : "star"});

      widget.changeDefaultSort(q1);

      expect(q1.get("sort")[0]).to.eql("pubdate desc");


      //shouldn't change the sort if a sort already exists

      var q2 = new ApiQuery({q : "star", sort : "pubdate asc"});

      widget.changeDefaultSort(q2);

      expect(q2.get("sort")[0]).to.eql("pubdate asc");

      //shouldn't change the sort for the following cases

      var q3 = new ApiQuery({q : "star", sort : "citation_count desc"});

      widget.changeDefaultSort(q3);

      expect(q3.get("sort")[0]).to.eql("citation_count desc");

      var q4 = new ApiQuery({q : "trending(star)", sort : "citation_count desc"});

      widget.changeDefaultSort(q4);

      expect(q4.get("sort")[0]).to.eql("citation_count desc");

      //should change sort to pubdate_desc for citations operator

      var q5 = new ApiQuery({q : "citations(star)"});

      widget.changeDefaultSort(q5);

      expect(q5.get("sort")[0]).to.eql("pubdate desc")

      //should change sort to first_author asc for references operator

      var q6 = new ApiQuery({q : "references(star)"});

      widget.changeDefaultSort(q6);

      expect(q6.get("sort")[0]).to.eql("first_author asc")

      //shouldn't add a sort if there is no sort and the query is an operator other than citations and references

      var q7 = new ApiQuery({q : "trending(star)"});

      widget.changeDefaultSort(q7);

      expect(q7.get("sort")).to.eql(undefined)


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
