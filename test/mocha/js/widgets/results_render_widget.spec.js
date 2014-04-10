define(['marionette', 'backbone', 'js/widgets/results_render/results_render_widget', 'js/components/api_response', 'js/components/api_query', './test_json/test1', './test_json/test2', 'js/components/beehive', 'js/services/pubsub'],
  function(Marionette, Backbone, ResultsListController, ApiResponse, ApiQuery, Test1, Test2, BeeHive, PubSub) {

    describe("Render Results (UI Widget)", function() {

      var r, beehive, pubsub, key, widget;

      var requestCounter = 0;


      beforeEach(function() {

        beehive = new BeeHive();
        pubsub = new PubSub();
        beehive.addService('PubSub', pubsub);
        key = pubsub.getPubSubKey();

        widget = new ResultsListController();

        widget.activate(beehive.getHardenedInstance());

        $("#test").append(widget.render());


        //so test pubsub will always respond to a delivering_request with test data
        //test data will alternate each time it is called

        var requestCallback = function(request) {
          if (requestCounter % 2 === 0) {
            var a = new ApiResponse(Test1);
          } else {
            var a = new ApiResponse(Test2);
          };
          a.setApiQuery(request.get("query"));
          pubsub.publish(key, pubsub.DELIVERING_RESPONSE, a);
          requestCounter++;
        };

        pubsub.subscribe(key, pubsub.DELIVERING_REQUEST, requestCallback);


      });

      afterEach(function() {
        $("#test").empty();
        r, beehive, pubsub, key, widget = undefined;

      });

      it("should update automatically when pubsub publishes an INVITING_REQUEST event", function(done) {

        //get widget to request info
        pubsub.publish(key, pubsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }))

        //find bibcode rendered
        expect($(".bib").eq(0).text()).to.equal("2013arXiv1305.3460H")


        pubsub.publish(key, pubsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        //find new first bib to confirm re-render

        expect($(".bib").eq(0).text()).to.equal("2006IEDL...27..896K")

        done();

      });


      it("should join highlights with their records on a model by model basis", function() {

        pubsub.publish(key, pubsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        expect(widget.collection.get("4189917").get("highlights")[0]).to.equal(
          "External triggers of <em>star</em> formation.")
      });


      it("should consist of a Marionette Controller with a Marionette Composite View as its main view", function() {

        expect(widget).to.be.instanceof(Backbone.Marionette.Controller);
        expect(widget.view).to.be.instanceof(Backbone.Marionette.CompositeView);

      });

      //should this go in all widget tests?
      it("should have a main widget with an activate, afterActivate, close, and render method", function() {

        expect(typeof widget.activate).to.equal("function");

        expect(typeof widget.close).to.equal("function");

        expect(typeof widget.render).to.equal("function");

      });


      it("should show highlights (if there are any) when a user clicks on 'show more'", function() {

        pubsub.publish(key, pubsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        expect($("span.view-more:first + div").hasClass("hide")).to.equal(true);
        $("span.view-more:first").click();
        expect($("span.view-more:first + div").hasClass("hide")).to.equal(false);

      })

    })

  })
