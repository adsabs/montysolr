define(['marionette', 'backbone', 'js/widgets/base/base_widget',
    'js/widgets/multi_callback/multi_callback_widget',
    'js/components/api_response', 'js/components/api_request',
    'js/components/api_query', './test_json/test1', './test_json/test2',
    'js/components/beehive', 'js/services/pubsub'],
  function(Marionette, Backbone, BaseWidget, MultiCallbackWidget, ApiResponse,
           ApiRequest, ApiQuery, Test1, Test2, BeeHive, PubSub) {

    describe("MultiCallback Widget (UI Widget)", function() {

      var beehive, pubsub, widget;

      beforeEach(function() {

        beehive = new BeeHive();
        pubsub = new PubSub();
        beehive.addService('PubSub', pubsub);

        widget = new MultiCallbackWidget();
        widget.activate(beehive.getHardenedInstance());

      });

      afterEach(function() {
        beehive, pubsub, widget = undefined;

      });

      it("inherits from base widget", function(){
        expect(widget).to.be.instanceof(BaseWidget);
        expect(widget).to.be.instanceof(MultiCallbackWidget);
      });


      it("has standard PubSub facing methods: dispatchRequest/processResponse", function(done){

        var q = new ApiQuery({q: "pluto"});
        var response = new ApiResponse({foo: 'bar', responseHeader: { params: { q: '*:*'}}});
        response.setApiQuery(q.clone());

        var spy = sinon.spy();
        sinon.spy(widget, 'composeRequest');

        widget.registerCallback(q.url(), spy);
        widget.dispatchRequest(q);

        expect(widget.composeRequest.calledOnce).to.be.true;

        // now pretend we are calling it from pubsub
        widget.processResponse(response);

        expect(spy.calledOnce).to.be.true;

        // call it again (the callback was removed and nothing should happen)
        widget.processResponse(response);
        expect(spy.calledOnce).to.be.true;


        // test the helper method
        widget.dispatch(q, spy, {one: 2});
        expect(widget.composeRequest.calledTwice).to.be.true;

        widget.processResponse(response);
        expect(spy.calledTwice).to.be.true;
        expect(spy.lastCall.args[0]).to.be.equal(response);
        expect(spy.lastCall.args[1]).to.be.eql({one: 2});

        done();

      });


    });

  });
