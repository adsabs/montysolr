define([
  'underscore',
  'js/components/user',
  'js/components/api_targets',
  'js/bugutils/minimal_pubsub',
  'js/components/api_request',
  'js/components/json_response',
  'js/components/api_query',
  'js/services/api',
  'js/components/app_storage',
  'js/modules/orcid/module'
], function(
  _,
  User,
  ApiTargets,
  MinSub,
  ApiRequest,
  JsonResponse,
  ApiQuery,
  Api,
  AppStorage,
  OrcidModule
  ){

 describe("User Object (user.spec.js)", function(){

   var fakeURLConfig = [
     {
       "name": "ohio wesleyan",
       "link": "ohio_wesleyan.edu"
     },
     {
       "name": "virginia wesleyan",
       "link": "virginia_wesleyan.edu"
     },
     {
       "name": "wesleyan university",
       "link": "wesleyan.edu"
     }
   ];



   it("keeps track of the user's signed in/anonymous state based on the content of the 'user' model, and responds to changes by updating user collection", function(){

     var u = new User();
     var mock = {publish : sinon.spy(), USER_ANNOUNCEMENT : "user_announcement"};
     u.getPubSub = function(){return mock};
     u.redirectIfNecessary = sinon.spy();

     var fetchStub = sinon.stub(u, "fetchData", function(){return $.Deferred().promise();});


     expect(u.isLoggedIn()).to.eql(false);

     u.setUser("foo");

     expect(u.model.get("user")).to.eql("foo");
     expect(u.isLoggedIn()).to.be.true;

     expect(fetchStub.args[0][0]).to.eql("USER_DATA");
     expect(u.redirectIfNecessary.callCount).to.eql(1);

     expect(u.getPubSub().publish.args[0]).to.eql(['user_announcement', u.USER_SIGNED_IN, 'foo' ] )

     fetchStub.restore();

   });

   it("has a log out method", function(){

     var u = new User();
     var mock = {publish : sinon.spy(), USER_ANNOUNCEMENT : "user_announcement"};
     u.getPubSub = function(){return mock};

     u.redirectIfNecessary = sinon.spy();

     var fetchStub = sinon.stub(u, "fetchData", function(){return $.Deferred().promise();});

     u.setUser("foo");
     expect(u.isLoggedIn()).to.eql(true);

     u.userDataModel.set("link_server", "foo");

     //clears the user
     u.completeLogOut();

     expect(u.isLoggedIn()).to.eql(false);
     //clears the user
     expect(u.model.get("user")).to.be.undefined;
     expect(u.userDataModel.get("link_server")).to.be.undefined;

     expect(u.redirectIfNecessary.callCount).to.eql(2);

     fetchStub.restore();
   });

   it("provides a hardened interface with the methods widgets and other objects might need", function() {

     var u = new User();
     var hardened = u.getHardenedInstance();
     expect(hardened.__facade__).equals(true);
     expect(hardened.handleCallbackError).to.be.undefined;
     expect(hardened).to.include.keys("setUser",
       "isLoggedIn",
       "getUserName",
       "isOrcidModeOn",
       "setOrcidMode",
       "getOpenURLConfig",
       "getUserData",
       "setUserData",
       "generateToken",
       "getToken",
       "deleteAccount",
       "changePassword",
       "changeEmail",
       "__facade__",
       "mixIn",
       "USER_SIGNED_IN",
       "USER_SIGNED_OUT",
       "USER_INFO_CHANGE",
       "ORCID_UI_CHANGE"
     );

   });

   describe("User Object: remote endpoint interaction", function() {

     var minsub;
     beforeEach(function() {
       var api = new Api();
       minsub = new MinSub({verbose: true, Api: api});
       this.server = sinon.fakeServer.create();
       this.server.autoRespond = false;

     });

     afterEach(function() {
       minsub.destroy();
       this.server.restore();
     });

     it('orcid settings reset if the contact with the OrcidApi gives a 401', function() {

       this.server.respondWith(/.*/,
         [401, { "Content-Type": "application/json" }, JSON.stringify({"you suck": "YOU FAILED"})]);

       //var minsub = new (MinSub.extend({
       //  request: function(apiRequest) {}
       //}))({verbose: false});

       // Activate the OrcidApi so that the User object can retrieve it from the BeeHive
       minsub.beehive.addObject('DynamicConfig', {
         orcidClientId: 'APP-P5ANJTQRRTMA6GXZ',
         orcidApiEndpoint: 'https://api.orcid.org',
         orcidRedirectUrlBase: 'http://localhost:8000',
         orcidLoginEndpoint: 'https://api.orcid.org/oauth/authorize'
       });
       var oModule = new OrcidModule();
       oModule.activate(minsub.beehive);
       var orcidApi = minsub.beehive.getService('OrcidApi');

       // Lets watch the signOut method and see if it is called
       sinon.spy(orcidApi, 'signOut');

       // Add the user to the beehive, and activate stuff
       var user = new User();
       minsub.beehive.addObject("User", user);
       user.activate(minsub.beehive);

       // Set some defaults that mimicks that the user is logged in to Orcid
       user.setOrcidMode(1);
       orcidApi.authData = {};

       // Lets watch the setOrcidMode method and see if it is called
       sinon.spy(user, 'setOrcidMode');

       // Publish the event "Application Starting" or "APP_STARTING" to PubSub
       minsub.publish(minsub.APP_STARTING);
       this.server.respond();

       // The user object should reset everything
       expect(orcidApi.signOut.called).to.eql(true);
       expect(user.setOrcidMode.called).to.eql(true);
       expect(orcidApi.authData).to.eql(null);
       expect(user.isOrcidModeOn()).to.eql(0);

     });

     it('orcid settings do not reset if the contact with the OrcidApi gives anything but a 401', function() {


       this.server.respondWith(/.*/,
         [500, { "Content-Type": "application/json" }, JSON.stringify({"you suck": "YOU FAILED"})]);

       //var minsub = new (MinSub.extend({
       //  request: function(apiRequest) {}
       //}))({verbose: false});

       // Activate the OrcidApi so that the User object can retrieve it from the BeeHive
       minsub.beehive.addObject('DynamicConfig', {
         orcidClientId: 'APP-P5ANJTQRRTMA6GXZ',
         orcidApiEndpoint: 'https://api.orcid.org',
         orcidRedirectUrlBase: 'http://localhost:8000',
         orcidLoginEndpoint: 'https://api.orcid.org/oauth/authorize'
       });
       var oModule = new OrcidModule();
       oModule.activate(minsub.beehive);
       var orcidApi = minsub.beehive.getService('OrcidApi');

       // Lets watch the signOut method and see if it is called
       sinon.spy(orcidApi, 'signOut');

       // Add the user to the beehive, and activate stuff
       var user = new User();
       minsub.beehive.addObject("User", user);
       user.activate(minsub.beehive);

       // Set some defaults that mimicks that the user is logged in to Orcid
       user.setOrcidMode(1);
       orcidApi.authData = {};

       // Lets watch the setOrcidMode method and see if it is called
       sinon.spy(user, 'setOrcidMode');

       // Publish the event "Application Starting" or "APP_STARTING" to PubSub
       minsub.publish(minsub.APP_STARTING);
       this.server.respond();

       // The user object should reset everything
       expect(orcidApi.signOut.called).to.eql(false);
       expect(user.setOrcidMode.called).to.eql(false);
       expect(orcidApi.authData).to.eql({});
       expect(user.isOrcidModeOn()).to.eql(1);

     });

   });


   it("broadcasts changes to its collection to interested widgets/other objects", function(){

     var u = new User();

     var minsub = new (MinSub.extend({
       request: function (apiRequest) {
       }
     }))({verbose: false})

     var appStorage = new AppStorage({csrf : "fake"});

     minsub.beehive.addObject("AppStorage", appStorage);
     u.activate(minsub.beehive);

     sinon.stub(u.getPubSub(), "publish");

     u.redirectIfNecessary = sinon.stub();

     u.userDataModel.set("link_server", "goo");

     expect(u.getPubSub().publish.args[0]).to.eql([
       "[PubSub]-User-Announcement",
       "user_info_change",
       {
         "link_server": "goo"
       }
     ]);

   });


     it("allows widgets to query user data using different methods", function(){

       var s = sinon.stub(User.prototype, "broadcastUserChange");
       var s2 = sinon.stub(User.prototype, "broadcastUserDataChange");


       var u = new User();

       u.model.set("user", "foobly@gmail.com");
       u.userDataModel.set("link_server", "woobly");

       expect(u.getUserData()).to.eql({
       link_server : "woobly"
      });

      expect(u.getUserName()).to.eql("foobly@gmail.com");

       s.restore();
       s2.restore();

     });


   it("allows widgets to post/put data to user endpoints (user data, change email, change password, delete account, get token)", function(){

     var u = new User();

     var minsub = new (MinSub.extend({
       request: function (apiRequest) {
       }
     }))({verbose: false});

     var api = new Api();
     var requestStub = sinon.stub(Api.prototype, "request", function(apiRequest){

       if (apiRequest.get("target") == "accounts/token" ){

         apiRequest.get("options").done({access_token : "foo"});

       }else if (apiRequest.get("target") == "vault/user-data"){

         apiRequest.get("options").done({"link_server" : "foo.com"});

       }

     });
     minsub.beehive.removeService("Api");
     minsub.beehive.addService("Api", api);

     var fakeCSRF = {getCSRF : sinon.spy(function(){
              var d = $.Deferred();d.resolve("fakeCSRFToken"); return d.promise();
     })};

     minsub.beehive.addObject("CSRFManager", fakeCSRF);

     u.activate(minsub.beehive)

     var a =  u.changePassword({"old_password":"foo","new_password1":"goo","new_password_2":"goo"});

     var request = requestStub.args[0][0];
     expect(request).to.be.instanceof(ApiRequest);
     expect(request.toJSON().target).to.eql("accounts/change-password");
     expect(request.toJSON().options.type).to.eql("POST");
     expect(request.toJSON().options.data).to.eql('{"old_password":"foo","new_password1":"goo","new_password_2":"goo"}');
     expect(fakeCSRF.getCSRF.callCount).to.eql(1);
     //returns a promise
     expect(a.then).to.be.instanceof(Function);


     u.changeEmail({email : "alex@alex.com", confirm_email: "alex@alex.com", password : "foo"});

     var request2 = requestStub.args[1][0];
     expect(request2.toJSON().target).to.eql("accounts/change-email");
     expect(request2.toJSON().options.type).to.eql("POST");
     expect(request2.toJSON().options.data).to.eql('{"email":"alex@alex.com","confirm_email":"alex@alex.com","password":"foo","verify_url":"http://localhost:8000/#user/account/verify/change-email"}');
     expect(fakeCSRF.getCSRF.callCount).to.eql(2);

     var token;
     var tokenPromise = u.generateToken();

     var request3 = requestStub.args[2][0];
     expect(request3.toJSON().target).to.eql("accounts/token");
     expect(request3.toJSON().options.type).to.eql("PUT");
     tokenPromise.done(function(data){token = data});
     expect(token).to.eql({access_token : "foo"});
     expect(fakeCSRF.getCSRF.callCount).to.eql(3);

     expect(u.userDataModel.get("link_server")).to.be.undefined;

     u.setUserData({link_server : "foo.com"});

     var request4 = requestStub.args[3][0];
     expect(request4.toJSON().target).to.eql("vault/user-data");
     expect(request4.toJSON().options.type).to.eql("POST");
     expect(request4.toJSON().options.data).to.eql('{"link_server":"foo.com"}');
     //doesn't require csrf token
     expect(fakeCSRF.getCSRF.callCount).to.eql(3);


     //automatically sets returned data into its model
     expect(u.userDataModel.get("link_server")).to.eql("foo.com")

     requestStub.restore();

   });

   it("has a method to get OpenURL Config", function(){

     var u = new User();

     var minsub = new (MinSub.extend({
       request: function (apiRequest) {
         apiRequest.toJSON().options.done(fakeURLConfig)
       }
     }))({verbose: false});

     u.activate(minsub.beehive);

     //returns a promise
     var returned;
     u.getOpenURLConfig().done(function(data){
       returned = data;
     })
     expect(returned).to.eql(fakeURLConfig);

   })


   it("handles the success/failure of GET and POST requests as appropriate, sometimes delegating to widgets", function(){

     var u = new User();

     var minsub = new (MinSub.extend({
       request: function (apiRequest) {
       }
     }))({verbose: false});


     var api = new Api();

     var requestStub = sinon.stub(Api.prototype, "request", function(apiRequest){
       apiRequest.toJSON().options.fail( {responseJSON : {error : "OH NO"}}, "400", "error");
     });
     minsub.beehive.removeService("Api");
     minsub.beehive.addService("Api", api);

     u.activate(minsub.beehive);

     sinon.stub(u.getPubSub(), "publish");

     u.redirectIfNecessary = sinon.stub();

     u.fetchData("fakeTarget");

     //on fail

     expect(u.getPubSub().publish.args[0][0]).to.eql("[Alert]-Message");
     expect(u.getPubSub().publish.args[0][1].toJSON()).to.eql( {"code":0,"msg":"Unable to retrieve information for endpoint fakeTarget (OH NO)"});

     u.postData("fakeTarget");

     expect(u.getPubSub().publish.args[1][0]).to.eql("[Alert]-Message");
     expect(u.getPubSub().publish.args[1][1].toJSON()).to.eql({"code":0,"msg":"Unable to update information for endpoint fakeTarget (OH NO)"});

     requestStub.restore();

   });

 });

});
