
define([
    'marionette',
    'backbone',
    'js/widgets/base/base_widget',
    'js/components/api_response',
    'js/components/api_request',
    'js/components/api_query',
    'js/bugutils/minimal_pubsub',
    'js/widgets/sort/widget'
  ],
  function(Marionette, Backbone,
           BaseWidget,
           ApiResponse,
           ApiRequest,
           ApiQuery,
           MinimalPubSub,
           SortWidget) {

    describe("Sort Widget (UI Widget)", function(){

      var w, minSub;

      beforeEach(function() {
        w = new SortWidget();
        $("#test").append(w.render().el)
      });

      afterEach(function() {
        if (minSub){
          minSub.close();
        }
        $("#test").empty()
      });


      it("should display the name of the current sort on the button", function(){

        expect($("#test").find("#sort-button").text().trim()).to.eql("Current Sort: Relevancy")

      });



      it("should render a default form and button if there is no search parameter", function(){

        minsub = new (MinimalPubSub.extend({
          request: function(apiRequest) {
            return {
              "responseHeader":{
                "status":0,
                "QTime":7,
                "params":{
                  "fl":"title",
                  "indent":"true",
                  "wt":"json",
                  "q":"star"}}
            }
          }
        }))({verbose: false});

        w.activate(minsub.beehive.getHardenedInstance());

        minsub.publish(minsub.START_SEARCH, new ApiQuery({q: 'star'}));

        expect($("#test").find("#sort-button").text().trim()).to.eql("Current Sort: Relevancy");

        expect($("#test").find("input[name=sort-options]:checked").val()).to.eql("score");

        expect($("#test").find("input[name=order-options]:checked").val()).to.eql("desc");


      });


      it("should parse the api query and, if it has a sort parameter, render that as the default sort", function(){


        var minsub = new (MinimalPubSub.extend({
          request: function(apiRequest) {
            return {
              "responseHeader":{
                "status":0,
                "QTime":7,
                "params":{
                  "sort":"citation_count asc",
                  "fl":"title",
                  "indent":"true",
                  "wt":"json",
                  "q":"star"}}
            }
          }
        }))({verbose: false});

        w.activate(minsub.beehive.getHardenedInstance());

        minsub.publish(minsub.START_SEARCH, new ApiQuery({q: 'star'}));

        expect($("#test").find("input[name=sort-options]:checked").val()).to.eql("citation_count");

        expect($("#test").find("input[name=order-options]:checked").val()).to.eql("asc");


      });


      it("should generate a new request to pubsub when the user clicks the submit button on the form", function(){


        if (window.mochaPhantomJS){
          return
        }

        minsub = new (MinimalPubSub.extend({
          request: function(apiRequest) {
            return {
              "responseHeader":{
                "status":0,
                "QTime":7,
                "params":{
                  "fl":"title",
                  "indent":"true",
                  "wt":"json",
                  "q":"star"}}
            }
          }
        }))({verbose: false});

        w.activate(minsub.beehive.getHardenedInstance());

        var pubSubStub = sinon.stub(w.pubsub, "publish");

        // simulate the click (works in phantomjs)
        $("input[value=score]").attr('checked', false);
        $("input[value=pubdate]").attr('checked', true);
        $("input[value=desc]").attr('checked', false);
        $("input[value=asc]").attr('checked', true);
        w.view.$("input[value=pubdate]").click();
        w.view.$("input[value=asc]").click();

        w.view.$("button.choose-sort").click();

        expect(pubSubStub.lastCall.args[1].get("sort")[0]).to.eql("pubdate asc");

      })

    })


  });