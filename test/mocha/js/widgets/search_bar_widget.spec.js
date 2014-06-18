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


    it("should render a search bar and a submit button", function() {

      expect($(".q").length).to.equal(1);
      expect($(".search-submit").length).to.equal(1);

    });

    it("should trigger a NEW_QUERY when the search-submit button is pressed", function() {

      var currentSearch;

      var pretendCurrentQuery = new ApiQuery({q:"star"});

      pubsub.publish(key, pubsub.NEW_QUERY, pretendCurrentQuery);

      pubsub.subscribe(key, pubsub.NEW_QUERY, function(apiQuery) {
        currentSearch = apiQuery.get("q")[0]
      });

      $(".q").val("author:kurtz,m");
      $(".search-submit").click();
      expect(currentSearch).to.equal('author:kurtz,m');

    });

    it("should allow the user to open and close a dropdown menu from the search bar", function(){
      expect( widget.view.$(".input-group-btn").hasClass("open")).to.equal(false);
      widget.view.$(".show-form").click();
      expect( widget.view.$(".input-group-btn").hasClass("open")).to.equal(true);
      widget.view.$(".show-form").click();
      expect( widget.view.$(".input-group-btn").hasClass("open")).to.equal(false);

    })


    it("should allow the user to click to add fielded search words to search bar", function() {

//      can't easily trigger hoverIntent so calling the method directly
      var e = {};
      e.preventDefault = function(){};
      e.target = document.querySelector("#field-options div[data-field=author]");

      widget.view.tempFieldInsert(e)
      widget.view.$("#field-options div[data-field=author]").click();
      expect($(".q").val().trim()).to.equal("author:\"\"");

    })

  });

})
