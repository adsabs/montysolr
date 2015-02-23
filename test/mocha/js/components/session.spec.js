define([
  'js/components/session',
  'js/bugutils/minimal_pubsub',
  'js/services/api',
  'js/components/api_request'


], function(
  Session,
  MinSub,
  Api,
  ApiRequest
  ){


  describe("Session Object", function(){

    it("provides a hardened interface with all methods widgets might need (login, logout, register,  user model get/set, etc)", function(){

      var s = new Session();
      var hardened = s.getHardenedInstance();
      expect(hardened.__facade__).equals(true);
      expect(hardened.handleCallbackError).to.be.undefined;

      expect(hardened.start).to.be.undefined;
      expect(_.keys(hardened)).to.eql(["isLoggedIn", "login", "logout", "register", "resetPassword", "deleteAccount", "__facade__", "mixIn"]);

    });

    it("has an explicit method for every action (login, logout, register, etc) a user might need to do before he/she is authenticated", function(){

      var s = new Session();

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {
        }
      }))({verbose: false});

      var api = new Api();
      var requestStub = sinon.stub(Api.prototype, "request");
      minsub.beehive.removeService("Api");
      minsub.beehive.addService("Api", api);
      s.activate(minsub.beehive);

      s.login({username: "goo", password : "foo", "g-recaptcha-response" : "boo"});

      expect(requestStub.args[0][0]).to.be.instanceof(ApiRequest)
      expect(requestStub.args[0][0].toJSON().target).to.eql("accounts/user");
      expect(requestStub.args[0][0].toJSON().options.type).to.eql("POST");
      expect(requestStub.args[0][0].toJSON().options.data).to.eql('{"username":"goo","password":"foo","g-recaptcha-response":"boo"}');
      expect(requestStub.args[0][0].toJSON().options.done).to.eql(s.loginSuccess);
      expect(requestStub.args[0][0].toJSON().options.fail).to.eql(s.loginFail);

      s.logout();

      expect(requestStub.args[1][0]).to.be.instanceof(ApiRequest)
      expect(requestStub.args[1][0].toJSON().target).to.eql("accounts/logout");
      expect(requestStub.args[1][0].toJSON().options.type).to.eql("GET");
      expect(requestStub.args[1][0].toJSON().options.done).to.eql(s.logoutSuccess);

      s.register({email: "goo@goo.com", password1 : "foo", password2 : "foo", "g-recaptcha-response" : "boo"});

      expect(requestStub.args[2][0]).to.be.instanceof(ApiRequest)
      expect(requestStub.args[2][0].toJSON().target).to.eql("accounts/register");
      expect(requestStub.args[2][0].toJSON().options.type).to.eql("POST");
      expect(requestStub.args[2][0].toJSON().options.data).to.eql('{"email":"goo@goo.com","password1":"foo","password2":"foo","g-recaptcha-response":"boo"}');
      expect(requestStub.args[2][0].toJSON().options.done).to.eql(s.registerSuccess);
      expect(requestStub.args[2][0].toJSON().options.fail).to.eql(s.registerFail);

      debugger;

      s.resetPassword({email: "goo@goo.com", "g-recaptcha-response" : "boo"});

      expect(requestStub.args[3][0]).to.be.instanceof(ApiRequest)
      expect(requestStub.args[3][0].toJSON().target).to.eql('accounts/reset-password/goo@goo.com');
      expect(requestStub.args[3][0].toJSON().options.type).to.eql("POST");
      expect(requestStub.args[3][0].toJSON().options.data).to.eql('{"g-recaptcha-response":"boo"}');
      expect(requestStub.args[3][0].toJSON().options.done).to.eql(s.resetPasswordSuccess);
      expect(requestStub.args[3][0].toJSON().options.fail).to.eql(s.resetPasswordFail);

      s.deleteAccount();

      expect(requestStub.args[4][0]).to.be.instanceof(ApiRequest)
      expect(requestStub.args[4][0].toJSON().target).to.eql("accounts/user/delete");
      expect(requestStub.args[4][0].toJSON().options.type).to.eql("POST");
      expect(requestStub.args[4][0].toJSON().options.done).to.eql(s.deleteSuccess);
      expect(requestStub.args[4][0].toJSON().options.fail).to.eql(s.deleteFail);

    });

    it("sends a pubsub event to notify interested widgets of success/fail of the various methods", function(){

//      var minsub = new (MinSub.extend({
//        request: function (apiRequest) {
//        }
//      }))({verbose: false});
//
//      var s = new Session();
//      var beehive = minsub.beehive.getHardenedInstance();
//      s.pubsub =  beehive.Services.get('PubSub');
//      sinon.stub(s.pubsub, "publish");
//
//      s.user.fetchInitialUserData = function(){};


    });

    it("also sends the proper alerts to the alert widget on failure", function(){



    })



  });

});