define(['marionette', 'backbone', 'js/widgets/base/base_widget',
 'js/widgets/multi_callback/multi_callback_widget',
    'js/components/api_response', 'js/components/api_request',
     'js/components/api_query', './test_json/test1', './test_json/test2',
      'js/components/beehive', 'js/services/pubsub'],
  function(Marionette, Backbone, BaseWidget, MultiCallbackWidget, ApiResponse,
    ApiRequest, ApiQuery, Test1, Test2, BeeHive, PubSub) {

    describe("MultiCallback Widget (UI Widget)", function() {

      var r, beehive, pubsub, key, widget;

      beforeEach(function() {

        beehive = new BeeHive();
        pubsub = new PubSub();
        beehive.addService('PubSub', pubsub);
        key = pubsub.getPubSubKey();

        widget = new MultiCallbackWidget();

        widget.activate(beehive.getHardenedInstance());

      });

      afterEach(function() {
        r, beehive, pubsub, key, widget = undefined;

      });

      it("inherits from base widget", function(){
        expect(widget).to.be.instanceof(BaseWidget)
      })


      it("has a composeRequest function that lets you update the current \
        query, registers a callback to the widget and returns\
         the new apiRequest", function(done){

          var fakeFunction = function(data){
            return "you passed " + data
          };

          var apiRequest = widget.composeRequest({q: "pluto"}, fakeFunction)
          var key = apiRequest.get("query").url()

          expect(apiRequest).to.be.instanceof(ApiRequest)
          expect(apiRequest.get("query").get("q")).to.eql(["pluto"])
          expect(widget._queriesInProgress[key].callback("a test")).to.eql("you passed a test")

          done();

        })

      it("has a assignResponse function that will pass data from DELIVERING_RESPONSE to a callback", function(){

          var t = 0;
          widget._queriesInProgress["q=star"] = {callback : function(){t+=1}}
          var testApiResponse = new ApiResponse(Test1)
          testApiResponse.setApiQuery(new ApiQuery({q:"star"}));
          widget.processResponse(testApiResponse);

          expect(t).to.equal(1)

      })

    })

  })
