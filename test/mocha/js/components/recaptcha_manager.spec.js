define([
  "js/components/recaptcha_manager",
  'js/bugutils/minimal_pubsub'
], function(
  RecaptchaManager,
  MinSub
  ){

  describe("Recaptcha Manager", function(){



  it("should have a deferred that is resolved when the sitekey is obtained and the google recaptcha global is loaded", function(done){

      var r = new RecaptchaManager();

      r.renderRecaptcha = sinon.spy();
      
      var testView = new Backbone.View();

      r.activateRecaptcha(testView);

      expect(r.renderRecaptcha.callCount).to.eql(0);
      r.siteKeyDeferred.resolve("siteKey");
//      expect(r.renderRecaptcha.callCount).to.eql(0);
      r.grecaptchaDeferred.resolve();

      expect(r.renderRecaptcha.callCount).to.eql(1);

    done();

    });

    it("listens to APP_STARTED and requests the recaptcha key", function(){

      var r = new RecaptchaManager();

      var key;

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
        }

      }))({verbose: false});

      var fakeAppStorage = {getConfigCopy : function(){return {recaptchaKey : "here_is_a_fake_key"}}};

      minsub.beehive.addObject("AppStorage", fakeAppStorage);

      r.activate(minsub.beehive);

      r.siteKeyDeferred.done(function(data){

        key = data;
      });

      minsub.publish(minsub.APP_STARTED);

      //evidence that recaptcha manager successfully retrieved config variable recaptcha key

      expect(key).to.eql("here_is_a_fake_key");





    })

  })

})