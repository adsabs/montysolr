define([
  "js/components/csrf_manager",
  'js/bugutils/minimal_pubsub',
  'js/components/json_response'

], function(
  CSRFManager,
  MinSub,
  JSONResponse
  ){

  describe("CSRF Manager (Object)", function(){


    it("should have a getCSRF function exposed through the hardenedInterface that sends an execute_request to pubsub", function(){

      var manager = new CSRFManager();

      var minsub = new (MinSub.extend({
               request: function(apiRequest) {
                  return {some: 'foo'}
                 }
               }))({verbose: false});

      manager.activate(minsub.beehive.getHardenedInstance());
      expect(manager.getHardenedInstance().getCSRF).to.be.instanceof(Function);
      sinon.spy(manager.getPubSub(), "publish");

      //it's a promise
      expect(manager.getCSRF().then).to.be.instanceof(Function);
      expect(manager.getPubSub().publish.args[0][0]).to.eql("[PubSub]-Execute-Request");
      expect((manager.getPubSub().publish.args[0][1]).get("target")).to.eql("accounts/csrf");

    });

    it("should resolve the deferred when it receives a response from pubsub", function(){

      var manager = new CSRFManager();

      manager.getPubSub = function(){return {publish : function(){}}};

      var promise = manager.getCSRF();

      var p;

      promise.done(function(csrf){ p = csrf;})
      manager.resolvePromiseWithNewKey(new JSONResponse({csrf : "foo"}));
      expect(p).to.eql("foo");


    })


  });




})