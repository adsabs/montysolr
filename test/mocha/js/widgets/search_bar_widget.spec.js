define([
    'jquery',
    'js/widgets/search_bar/search_bar_widget',
    'js/components/beehive',
    'js/bugutils/minimal_pubsub',
    'js/components/api_query',
    './test_json/test1',
    'js/components/api_feedback'
  ],
  function(
      $,
      SearchBarWidget,
      BeeHive,
      MinimalPubSub,
      ApiQuery,
      Test,
      ApiFeedback
    ) {


  describe("Search Bar UI Widget (search_bar_widget.spec.js)", function() {

    var minsub, widget;
    beforeEach(function (done) {
      minsub = new (MinimalPubSub.extend({
        request: function (apiRequest) {
          if (apiRequest.get("target") == "objects/query"){
            // This mimics the response of the object service microservice, which translates a query like e.g.
            // "bibstem:ApJ object:Andromeda year:2001" into one where the "object:" part is replaced by its
            // "simbid:" equivalent
            return {'query':'bibstem:ApJ simbid:1277363 year:2001'}
          } else {
            return Test();
          }
        }
      }))({verbose: false});
      done();
    });

    afterEach(function (done) {
      if (widget)
        widget.onDestroy();

      minsub.destroy();
      var ta = $('#test');
      if (ta) {
        ta.empty();
      }
      done();
    });

    var _widget = function() {
      widget = new SearchBarWidget();
      widget.activate(minsub.beehive.getHardenedInstance());
      return widget;
    };

    it("should render a search bar and a submit button", function(done) {
      var widget = _widget();
      var $w = widget.render().$el;
      expect($w.find(".q").length).to.equal(1);
      expect($w.find(".search-submit").length).to.equal(1);
      done();
    });

    it("should trigger a START_SEARCH when the search-submit button is pressed", function(done) {
      var widget = _widget();
      var $w = widget.render().$el;
      $('#test').append($w); // watch out, if the form is not inserted in the page, the test gets reloaded

      var triggered = false;
      minsub.on(minsub.START_SEARCH, function(apiQuery) {
        triggered = true;
      });

      $w.find(".q").val("author:kurtz,m");
      $w.find(".search-submit").click();

      expect(triggered).to.eql(true);
      done();

    });

    it("should get a new query from FEEDBACK and show the number of results", function(done) {
      var widget = _widget();
      var $w = widget.render().$el;

      minsub.subscribe(minsub.INVITING_REQUEST, function() {
        minsub.publish(minsub.DELIVERING_REQUEST, minsub.createRequest(
          {'query': minsub.createQuery({'test': 'foo'})}));
      });

      minsub.publish(minsub.START_SEARCH, minsub.createQuery({'q': 'foo:bar'}));
      setTimeout(function() {
        expect(widget.view.getFormVal()).to.be.eql('foo:bar');
        expect($w.find(".num-found-container").html().trim()).to.eql('841,359');
        done();
      }, 5);


    });

    it("puts query in bar even when the search cycle failed", function(done){


      var widget = _widget();
      var $w = widget.render().$el;

      $("#test").append($w)

      //puts query in the search bar even when feedback is error
      var feedback = {
        request : minsub.createRequest({'query': minsub.createQuery({'q': 'fakeQuery'})}),
        //"search cycle failed to start"
        code : -3
      };

      minsub.publish(minsub.FEEDBACK, feedback);
      setTimeout(function() {
        expect(widget.view.getFormVal()).to.be.eql('fakeQuery');
        expect($w.find(".num-found-container").html().trim()).to.eql('0');
        done();
      }, 5);

    });


    it("should allow the user to click to add fielded search words to search bar", function(done) {
      var widget = _widget();
      $("#test").append(widget.render().el);
      var $w = widget.render().$el;

      //should just insert the field if user hasn't selected anything
      widget.view.$("#field-options button[data-field=author]").click();
      expect($w.find(".q").val().trim()).to.equal("author:\"\"");

      //should insert the field around the selected content if user has selected something
      $w.find(".q").val("author name");

      $w.find(".q").selectRange(0, 11);

      $w.find(".q").trigger("click");

      widget.view.$("#field-options button[data-field=author]").click();
      expect($w.find(".q").val().trim()).to.equal("author:\"author name\"");

      widget.view._cursorInfo.selected = undefined;



      done();
    });

    it("lowercases fields to prevent syntax errors", function(){

      var widget = _widget();
      $("#test").append(widget.render().el);
      var $w = widget.render().$el;


      widget.view.on("start_search", function(query){
        expect(query.get('q')[0]).to.eql("author:Accomazzi bib:Apj property:refereed");
      });

      //should insert the field around the selected content if user has selected something
      $w.find(".q").val("Author:Accomazzi Bib:Apj property:refereed");
      $w.find(".search-submit").click();


    });

    it("adds a default sort value of pubdate if the query doesnt have an operator and doesn't already have a sort", function(done){

      var widget = _widget();

      expect(widget.changeDefaultSort).to.be.instanceof(Function);

      //should add pubdate sort if there is no sort
      var q1 = new ApiQuery({q : "star"});
      widget.changeDefaultSort(q1);
      expect(q1.get("sort")[0]).to.eql("date desc");


      //shouldn't change the sort if a sort already exists
      var q2 = new ApiQuery({q : "star", sort : "date asc"});
      widget.changeDefaultSort(q2);
      expect(q2.get("sort")[0]).to.eql("date asc");

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
      expect(q5.get("sort")[0]).to.eql("date desc");

      //should change sort to first_author asc for references operator
      var q6 = new ApiQuery({q : "references(star)"});
      widget.changeDefaultSort(q6);
      expect(q6.get("sort")[0]).to.eql("first_author asc");

      //should add "relevancy desc" if the query is an operator (trending, instructive/reviews, useful)
      var q7 = new ApiQuery({q : "trending(star)"});
      widget.changeDefaultSort(q7);
      expect(q7.get("sort")[0]).to.eql("score desc");

      var q8 = new ApiQuery({q : "reviews(star)"});
      widget.changeDefaultSort(q8);
      expect(q8.get("sort")[0]).to.eql("score desc");

      done();
    });

    it("should have an x icon, only visible when search bar has content, that clears the search bar", function(){

      var s = new SearchBarWidget();

      $("#test").append(s.render().el);

      expect($("#test").find(".icon-clear").hasClass("hidden")).to.be.true;

      $("#test .q").val("a query").trigger("keyup");

      expect($("#test").find(".icon-clear").hasClass("hidden")).to.be.false;

      $("#test").find(".icon-clear").trigger("click");

      expect(Boolean($("test").val())).to.be.false;

      //finally, test whether x appears if controller adds content after render
      $("#test").empty();

      var s = new SearchBarWidget();
      s.setCurrentQuery(new ApiQuery({q : "foo"}));
      $("#test").append(s.render().el);

      expect( $("#test .q").val()).to.eql("foo");
      expect($("#test").find(".icon-clear").hasClass("hidden")).to.be.false;

    });

    it("should have an autocomplete that highlights suggestions and is activated again after the user hits space", function(){

      var s = new SearchBarWidget();

      $("#test").append(s.render().el);

      var $input = $("input.q");

      $input.autocomplete("search", "a");

      var autolist = $("ul.ui-autocomplete").last();

      expect(autolist.css("display")).to.eql("block");
      expect(autolist.find("a").first().text()).to.eql("Author");
      expect(autolist.find("a").last().text()).to.eql("Search abstract + title + keywords");

      autolist.find("li").first().click();
      expect($input.val()).to.eql("author:\"\"");

      //autocomplete should return unless user cursor is at end of input text
      //so this should not activate autocomplete options
      $input.autocomplete("search", "author:\"foo\" a");
      var autolist = $("ul.ui-autocomplete").last();

      expect(autolist.css("display")).to.eql("none");

      //this has set the cursor position in the right place, so it should work
      $input.val("author:\"foo\" a");
      $input.selectRange($input.val().length);

      $input.autocomplete("search", "author:\"foo\" a");

      //should try to autocomplete on only the 'a' just as before

      var autolist = $("ul.ui-autocomplete").last();

      expect(autolist.css("display")).to.eql("block");
      expect(autolist.find("a").first().text()).to.eql("Author");
      expect(autolist.find("a").last().text()).to.eql("Search abstract + title + keywords");

      //should not autocomplete if the last keypress was a backspace

      $input.val("");
      $input.autocomplete("close");

      var press =   jQuery.Event("keydown");
      press.ctrlKey = false;
      press.which = 8;
      press.keyCode = 8;

      $input.trigger(press);

      $input.autocomplete("search", "author:\"foo\" a");

      expect(!!$input.find('.ui-autocomplete.ui-widget:visible').length).to.be.false;

      $("ul.ui-autocomplete").remove();

    });



    it("shows the query builder form on clicking 'Search Form' ", function(done) {
      var widget = _widget();
      var $w = widget.render().$el;
      $('#test').append($w);

      expect( widget.view.$(".input-group-btn").hasClass("open")).to.equal(false);
      widget.view.$(".show-form").click();
      expect( widget.view.$(".input-group-btn").hasClass("open")).to.equal(true);
      widget.view.$(".show-form").click();
      expect( widget.view.$(".input-group-btn").hasClass("open")).to.equal(false);
      done();
    });

    it("in QB form: when rule is removed, the form stays open", function() {

    });

    it("typing inside one of the input fields updates the search input", function() {

    });

    it("changing function updates the search input", function() {
      var widget = _widget();
      var $w = widget.render().$el;
      $('#test').append($w);

      widget.view.$(".show-form").click();

      // test functions
      $w.find('.rule-container:first select:nth(0)').val('pos()').trigger('change');
      expect($w.find('.rule-value-container:first input[name=query]').is(':visible')).to.eql(true);
      expect($w.find('.rule-value-container:first input[name=start]').is(':visible')).to.eql(true);
      expect($w.find('.rule-value-container:first input[name=end]').is(':visible')).to.eql(true);

      $w.find('.rule-value-container:first input[name=query]').val('foo').trigger('change');
      $w.find('.rule-value-container:first input[name=start]').val(10).trigger('change');
      expect($w.find('.rule-value-container:first input[name$=_value]').val()).to.eql('foo|10');


      $w.find('.rule-container:first select:nth(0)').val('citations()').trigger('change');
      expect($w.find('.rule-value-container:first input[name=query]').is(':visible')).to.eql(true);
      $w.find('.rule-value-container:first input[name=query]').val('citations').trigger('change');
      expect($w.find('.rule-value-container:first input[name$=_value]').val()).to.eql('citations');

      $w.find('.rule-container:first select:nth(0)').val('references()').trigger('change');
      expect($w.find('.rule-value-container:first input[name=query]').is(':visible')).to.eql(true);
      $w.find('.rule-value-container:first input[name=query]').val('references').trigger('change');
      expect($w.find('.rule-value-container:first input[name$=_value]').val()).to.eql('references');

      $w.find('.rule-container:first select:nth(0)').val('trending()').trigger('change');
      expect($w.find('.rule-value-container:first input[name=query]').is(':visible')).to.eql(true);
      $w.find('.rule-value-container:first input[name=query]').val('trending').trigger('change');
      expect($w.find('.rule-value-container:first input[name$=_value]').val()).to.eql('trending');

      $w.find('.rule-container:first select:nth(0)').val('reviews()').trigger('change');
      expect($w.find('.rule-value-container:first input[name=query]').is(':visible')).to.eql(true);
      $w.find('.rule-value-container:first input[name=query]').val('reviews').trigger('change');
      expect($w.find('.rule-value-container:first input[name$=_value]').val()).to.eql('reviews');

      $w.find('.rule-container:first select:nth(0)').val('topn()').trigger('change');
      expect($w.find('.rule-value-container:first input[name=query]').is(':visible')).to.eql(true);
      expect($w.find('.rule-value-container:first input[name=number]').is(':visible')).to.eql(true);
      expect($w.find('.rule-value-container:first select[name=sorting]').is(':visible')).to.eql(true);
      $w.find('.rule-value-container:first input[name=query]').val('foo').trigger('change');
      $w.find('.rule-value-container:first input[name=number]').val(10).trigger('change');
      expect($w.find('.rule-value-container:first input[name$=_value]').val()).to.eql('foo|10');

    });

    it("removing the rule/group updates the input", function() {

    });

    it.skip("clicking outside the QB area, hides QB form", function() {

    });

    it("spinning wheel is shown when the query cannot be parsed immediately", function() {

    });

    it("you can type into the QB form without typing into search form first", function() {

    });

    it("you can create several input fields, some empty, and the form indicates which one are wrong", function() {

    });

    it("when the QB form opens, the search bar should become little opaque", function() {

    });

    it("displays a special tag when inside a bigquery, and hides *:* from the user", function(){

      var s = new SearchBarWidget();
      s.setCurrentQuery(new ApiQuery({q : "foo"}));
      $("#test").append(s.render().el);

      s.handleFeedback({
        code : ApiFeedback.CODES.SEARCH_CYCLE_STARTED,
        numFound : 1000,
        query : new ApiQuery({
          q : '*:*',
          __bigquerySource : 'Library: Cool Papers woo',
          __qid : 'hash string'
        })
      });

      expect($(".input-group .bigquery-tag").text().trim()).to.eql("Cool Papers woo");

      //it's a library so show a nice library icon
      expect($(".input-group .bigquery-tag i").eq(0).attr("class")).to.eql("fa fa-book");

      //hides the *:* from the user
      expect($("input.q").val()).to.eql("")

      //but if someone submits the 'empty' q, re-insert the *:*
      s.navigate = sinon.spy();

      $(".search-submit").click();

      expect(s.navigate.args[0][0].toJSON()).to.eql({
        "q": [
          "*:*"
        ],
        "sort": [
          "date desc"
        ]
      });

      //make sure bigquery can be removed
        $(".bigquery-close").click();

        expect(s.navigate.args[1][0].toJSON()).to.eql(
          {
            "q": [
              "*:*"
            ],
            "__clearBiqQuery": [
              "true"
            ],
            "sort": [
              "date desc"
            ]
          }
        );


    });

    it("when some of the fields have wrong input (and the query doesn't contain everything), the form should warn user before closing itself", function() {

    });
    // Internally, 'object:' queries get translated into 'simbid:' queries (because this is what the Solr documents contain)
    // but since SIMBAD identifiers are meaningless to the user, we need to keep the original 'object' query visible in the UI
    it("check if the SIMBAD 'object:' search stays the way it is", function() {
      var widget = _widget();
      $("#test").append(widget.render().el);
      var $w = widget.render().$el;

      //should insert the field around the selected content if user has selected something
      $w.find(".q").val("bibstem:ApJ object:Foo year:2001");
      $w.find(".search-submit").click();
      console.log($w.find(".s-num-found").html().trim());
    });

  });

})
