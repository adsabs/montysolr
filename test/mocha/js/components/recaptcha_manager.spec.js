define([
  "js/components/recaptcha_manager"
], function(RecaptchaManager){

  describe("Recaptcha Manager", function(){


    it("should have a deferred that is resolved when the sitekey is obtained and the google recaptcha global is loaded", function(){

      var testView = new Backbone.View();

      var r = new RecaptchaManager();

      r.renderRecaptcha = sinon.spy();

      r.activateRecaptcha(testView);

      expect(r.renderRecaptcha.callCount).to.eql(0);
      r.siteKeyDeferred.resolve("siteKey");
//      expect(r.renderRecaptcha.callCount).to.eql(0);
      r.grecaptchaDeferred.resolve();

      expect(r.renderRecaptcha.callCount).to.eql(1);


    })

  })

})