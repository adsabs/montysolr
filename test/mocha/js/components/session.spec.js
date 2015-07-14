define([
  'js/components/session',
  'js/bugutils/minimal_pubsub',
  'js/services/api',
  'js/components/api_request',
  'js/components/user',
  'js/components/csrf_manager'


], function(
  Session,
  MinSub,
  Api,
  ApiRequest,
  User,
  CSRFManager
  ){


  describe("Session Object", function(){

    it("provides a hardened interface with all methods widgets might need (login, logout, register,  user model get/set, etc)", function(){

      var s = new Session();
      var hardened = s.getHardenedInstance();
      expect(hardened.__facade__).equals(true);
      expect(hardened.handleCallbackError).to.be.undefined;

      expect(hardened.start).to.be.undefined;
      expect(_.keys(hardened)).to.eql(["login", "logout", "register", "resetPassword1", "resetPassword2", "setChangeToken", "__facade__", "mixIn"]);

    });

    it("has an explicit method for every action (login, logout, register, etc) a user might need to do before he/she is authenticated", function(){

      var s = new Session({test: true});
      var minsub = new (MinSub.extend({
        request: function (apiRequest) {
        }
      }))({verbose: false});

      var api = new Api();
      var csrfManager = new CSRFManager();
      csrfManager.getCSRF = function(){this.deferred = $.Deferred(); return this.deferred.promise()};
      csrfManager.resolvePromiseWithNewKey = function(){
        this.deferred.resolve("foo")
      };

      var requestStub = sinon.stub(Api.prototype, "request");
      minsub.beehive.removeService("Api");
      minsub.beehive.addService("Api", api);
      minsub.beehive.addObject("CSRFManager", csrfManager);
      s.activate(minsub.beehive);

      s.login({username: "goo", password : "foo", "g-recaptcha-response" : "boo"});

      csrfManager.resolvePromiseWithNewKey();

      expect(requestStub.args[0][0]).to.be.instanceof(ApiRequest)
      expect(requestStub.args[0][0].toJSON().target).to.eql("accounts/user");
      expect(requestStub.args[0][0].toJSON().options.type).to.eql("POST");
      expect(requestStub.args[0][0].toJSON().options.data).to.eql('{"username":"goo","password":"foo","g-recaptcha-response":"boo"}');
      expect(requestStub.args[0][0].toJSON().options.done).to.eql(s.loginSuccess);
      expect(requestStub.args[0][0].toJSON().options.fail).to.eql(s.loginFail);

      s.logout();

      csrfManager.resolvePromiseWithNewKey();

      expect(requestStub.args[1][0]).to.be.instanceof(ApiRequest)
      expect(requestStub.args[1][0].toJSON().target).to.eql("accounts/logout");
      expect(requestStub.args[1][0].toJSON().options.type).to.eql("POST");
      expect(requestStub.args[1][0].toJSON().options.done).to.eql(s.logoutSuccess);

      s.register({email: "goo@goo.com", password1 : "foo", password2 : "foo", "g-recaptcha-response" : "boo"});

      csrfManager.resolvePromiseWithNewKey();

      expect(requestStub.args[2][0]).to.be.instanceof(ApiRequest)
      expect(requestStub.args[2][0].toJSON().target).to.eql("accounts/register");
      expect(requestStub.args[2][0].toJSON().options.type).to.eql("POST");
      //test version just uses string  "location.origin", real version will use the actual origin
      expect(requestStub.args[2][0].toJSON().options.data).to.eql('{"email":"goo@goo.com","password1":"foo","password2":"foo","g-recaptcha-response":"boo","verify_url":"location.origin/#user/account/verify/register"}');
      expect(requestStub.args[2][0].toJSON().options.done).to.eql(s.registerSuccess);
      expect(requestStub.args[2][0].toJSON().options.fail).to.eql(s.registerFail);

      s.resetPassword1({email: "goo@goo.com", "g-recaptcha-response" : "boo"});

      csrfManager.resolvePromiseWithNewKey();


      expect(requestStub.args[3][0]).to.be.instanceof(ApiRequest)
      expect(requestStub.args[3][0].toJSON().target).to.eql('accounts/reset-password/goo@goo.com');
      expect(requestStub.args[3][0].toJSON().options.type).to.eql("POST");
      expect(requestStub.args[3][0].toJSON().options.data).to.eql('{"g-recaptcha-response":"boo","reset_url":"location.origin/#user/account/verify/reset-password"}');
      expect(requestStub.args[3][0].toJSON().options.done).to.eql(s.resetPassword1Success);
      expect(requestStub.args[3][0].toJSON().options.fail).to.eql(s.resetPassword1Fail);


      //this happens in the router after the user has clicked on a link in their email
      s.setChangeToken("fakeToken")

      s.resetPassword2({password1 : "1Aaaaa", password2 : "1Aaaaa"});

      csrfManager.resolvePromiseWithNewKey();

      expect(requestStub.args[4][0]).to.be.instanceof(ApiRequest)
      //test version just uses string  "location.origin", real version will use the actual origin
      expect(requestStub.args[4][0].toJSON().target).to.eql('accounts/reset-password/fakeToken');
      expect(requestStub.args[4][0].toJSON().options.type).to.eql("PUT");
      expect(requestStub.args[4][0].toJSON().options.data).to.eql('{"password1":"1Aaaaa","password2":"1Aaaaa"}' );
      expect(requestStub.args[4][0].toJSON().options.done).to.eql(s.resetPassword2Success);
      expect(requestStub.args[4][0].toJSON().options.fail).to.eql(s.resetPassword2Fail)


      requestStub.restore();

    });

    it("handles fail of method by 1) sending pubsub method and 2) sending alert", function(){

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {
        }
      }))({verbose: false});

      var s = new Session();
      var beehive = minsub.beehive.getHardenedInstance();
      s.pubsub =  beehive.Services.get('PubSub');
      sinon.stub(s.pubsub, "publish");

      var fakeXHR = {responseJson : {error :"no account"}};

      s.loginFail(fakeXHR);

      expect(s.pubsub.publish.args[0][0]).to.eql("[Alert]-Message");
      expect(s.pubsub.publish.args[0][1].msg).to.eql("Log in was unsuccessful (error unknown)");
      expect(s.pubsub.publish.args[1]).to.eql(["[PubSub]-User-Announcement", "login_fail"]);

      s.registerFail(fakeXHR);

      expect(s.pubsub.publish.args[2][0]).to.eql("[Alert]-Message");
      expect(s.pubsub.publish.args[2][1].msg).to.eql('Registration was unsuccessful (error unknown)');
      expect(s.pubsub.publish.args[3]).to.eql(["[PubSub]-User-Announcement", "register_fail"]);

      s.resetPassword1Fail(fakeXHR);

      expect(s.pubsub.publish.args[4][0]).to.eql("[Alert]-Message");
      expect(s.pubsub.publish.args[4][1].msg).to.eql("password reset step 1 was unsucessful (error unknown)")
      expect(s.pubsub.publish.args[5]).to.eql(["[PubSub]-User-Announcement", "reset_password_1_fail"]);

      s.resetPassword2Fail(fakeXHR);

      expect(s.pubsub.publish.args[6][0]).to.eql("[Alert]-Message");
      expect(s.pubsub.publish.args[6][1].msg).to.eql("password reset step 2 was unsucessful (error unknown)");
      expect(s.pubsub.publish.args[7]).to.eql(["[PubSub]-User-Announcement", "reset_password_2_fail"]);


    });

    it("handles success of methods by 1) sending pubsub method and 2) optionally doing additional work", function(){

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {
        }
      }))({verbose: false});

      var s = new Session();;
      var u = new User();

      u.completeLogOut = sinon.stub();

      minsub.beehive.addObject("User", u);

      s.activate(minsub.beehive);
      sinon.stub(s.pubsub, "publish");
      sinon.stub(s, "getApiAccess", function(){var d = $.Deferred(); d.resolve();return d.promise();});

      s.loginSuccess();

      expect(s.getApiAccess.callCount).to.eql(1);
      //called once getApiAccess is resolved
      expect(s.pubsub.publish.args[0]).to.eql(["[Router]-Navigate-With-Trigger", "UserPreferences"]);


      s.logoutSuccess();

      //navigate to index page
      expect(s.pubsub.publish.args[1]).to.eql(["[Router]-Navigate-With-Trigger", "index-page"]);
      // scrub the user object
      expect(u.completeLogOut.callCount).to.eql(1);

      s.registerSuccess();
      expect(s.pubsub.publish.args[2]).to.eql(["[PubSub]-User-Announcement", "register_success"]);

      s.resetPassword1Success();
      expect(s.pubsub.publish.args[3]).to.eql(["[PubSub]-User-Announcement", "reset_password_1_success"]);

      s.resetPassword2Success();
      expect(s.pubsub.publish.args[4]).to.eql(["[PubSub]-User-Announcement", "reset_password_2_success"]);

    });

  });

});