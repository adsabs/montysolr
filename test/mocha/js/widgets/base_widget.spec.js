define(['marionette', 'backbone', 'js/widgets/base/base_widget',
    'js/components/api_response', 'js/components/api_request',
     'js/components/api_query',
    './test_json/test1', './test_json/test2', 'js/components/beehive',
    'js/services/pubsub'
  ],
  function(Marionette, Backbone, BaseWidget, ApiResponse,
    ApiRequest, ApiQuery, Test1, Test2, BeeHive, PubSub) {

    describe("Base Widget (UI Widget)", function() {

      var r, beehive, pubsub, key, widget;

      beforeEach(function() {

        beehive = new BeeHive();
        pubsub = new PubSub();
        beehive.addService('PubSub', pubsub);
        key = pubsub.getPubSubKey();

        widget = new BaseWidget();

        widget.activate(beehive.getHardenedInstance());

      });

      afterEach(function() {
        r, beehive, pubsub, key, widget = undefined;

      });

      it("always has an up-to-date apiQuery, accessible as a copy through .getCurrentQuery()", function(done) {

        expect(widget.getCurrentQuery()).to.be.instanceof(ApiQuery);

        var q =  new ApiQuery({
          q: "pluto",
          fl: "author"
        });

        pubsub.publish(key, pubsub.INVITING_REQUEST, q);

        expect(widget.getCurrentQuery().get("q")).to.eql(["pluto"]);
        expect(widget.getCurrentQuery().get("fl")).to.eql(["author"]);

        expect(widget.getCurrentQuery()).to.not.eql(q);

        done();

      });

      it("has the activate, getView, and close methods necessary for most/all ui widgets", function(){
        expect(widget.activate).to.be.instanceof(Function);
        expect(widget.close).to.be.instanceof(Function);
        expect(widget.getView).to.be.instanceof(Function);
      });


    })

  })
