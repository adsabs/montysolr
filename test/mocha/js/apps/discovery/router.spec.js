define([
  'js/apps/discovery/router',
  'js/components/beehive',
  'js/services/api',
  'js/services/pubsub',
  'js/components/session'
], function(
    Router,
    Beehive,
    Api,
    PubSub,
    Session
  ){


  describe("Router", function(){

    it("should handle the search endpoint", function(){

      var r = new Router();

      var beehive = new Beehive();

      var fakePubSub = new PubSub();
      sinon.spy(fakePubSub, "publish");
      beehive.addService("PubSub", fakePubSub);

      r.activate(beehive.getHardenedInstance());
      r.search("q=foo", "metrics");

      expect(fakePubSub.publish.args[0][1]).to.eql("[PubSub]-New-Query");
      expect(fakePubSub.publish.args[0][2].toJSON()).to.eql({
        "q": [
          "foo"
        ]
      });

      fakePubSub.publish(fakePubSub.pubSubKey, fakePubSub.NAVIGATE, "results-page");

      expect(fakePubSub.publish.args[2][1]).to.eql("[Router]-Navigate-With-Trigger");
      expect(fakePubSub.publish.args[2][2]).to.eql("show-metrics");

    })


    it("should handle the three verify routes which are contained in email links, passing verify token to servers ", function(){

      var r = new Router();
      r.getApiAccess = sinon.spy(function(){return $.Deferred().promise()});

      var beehive = new Beehive();

      var fakeApi = new Api();
      fakeApi.request = sinon.spy();
      beehive.addService("Api", fakeApi);

      var fakePubSub = new PubSub();
      fakePubSub.publish = sinon.spy()
      beehive.addService("PubSub", fakePubSub);

      var fakeSession = new Session();
      fakeSession.setChangeToken = sinon.spy();
      beehive.addObject("Session", fakeSession);

      r.activate(beehive.getHardenedInstance());

      //1. account verification email link
      r.execute(r.routeToVerifyPage, ["register", "fakeToken"]);
      expect(fakeApi.request.args[0][0].toJSON().target).to.eql("accounts/verify/fakeToken");

      fakeApi.request.args[0][0].get("options").done.call(r, {email:"foo"});
      expect(r.getApiAccess.callCount).to.eql(1);


      fakeApi.request.args[0][0].get("options").fail.call(r, {responseJSON : {error : "fakeError"}});;
      expect(fakePubSub.publish.args[0].slice(1)).to.eql([
        "[Router]-Navigate-With-Trigger",
        "index-page"
      ]);
      expect(fakePubSub.publish.args[1][1]).to.eql(
        "[Alert]-Message"
       );

      expect(fakePubSub.publish.args[1][2].toJSON()).to.eql({
        "code": 0,
        "msg": " <b>fakeError</b> <br/><p>Please try again, or contact <b> adshelp@cfa.harvard.edu for support </b></p>"
      })

      //2. change email link
      r.execute(r.routeToVerifyPage, ["change-email", "fakeToken2"]);

      expect(fakeApi.request.args[1][0].toJSON().target).to.eql("accounts/verify/fakeToken2");


      fakeApi.request.args[1][0].get("options").done.call(r, {email:"foo"});

      expect(r.getApiAccess.callCount).to.eql(2);

      fakeApi.request.args[1][0].get("options").fail.call(r, {email:"foo"});

      expect(fakePubSub.publish.args[2].slice(1)).to.eql([
        "[Router]-Navigate-With-Trigger",
        "index-page"
      ]);
      expect(fakePubSub.publish.args[3][1]).to.eql(
        "[Alert]-Message");


      //3. reset password is slightly more complicated, should redirect to part 2 of the form in authentication widget
      r.execute(r.routeToVerifyPage, ["reset-password" ,"fakeToken3"]);
      expect(fakeApi.request.args[2][0].toJSON().target).to.eql("accounts/reset-password/fakeToken3");


      fakeApi.request.args[2][0].get("options").done.call(r);

      //store the change token for re-submittal after user fills out second form
      expect(fakeSession.setChangeToken.args[0][0]).to.eql("fakeToken3");
      expect(fakePubSub.publish.args[4].slice(1)).to.eql([
        "[Router]-Navigate-With-Trigger",
        "authentication-page",
        {
          "subView": "reset-password-2"
        }
      ]);


      fakeApi.request.args[2][0].get("options").fail.call(r, {responseJSON : {error : "fakeError"}});

      expect(fakePubSub.publish.args[5].slice(1)).to.eql(["[Router]-Navigate-With-Trigger", "index-page"]);
      expect(fakePubSub.publish.args[6][1]).to.eql("[Alert]-Message");

    })
  })

});