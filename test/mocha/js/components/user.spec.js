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

 describe("User Object", function(){

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


   it("keeps track of the user's signed in/anonymous state based on the content of the 'user' model within its collection, and responds to changes by updating user collection", function(){

     sinon.stub(User.prototype, "broadcastChange");
     sinon.stub(User.prototype, "broadcastReset");

     var u = new User();
     u.pubsub = {publish : function(){}};

     var fetchStub = sinon.stub(u, "fetchData");

     expect(u.isLoggedIn()).to.eql(false);

     u.setUser("foo");

     //automatically logs in user before doing a double check

     expect(u.collection.get("USER").get("user")).to.eql("foo");
     expect(u.isLoggedIn()).to.be.true;

     expect(fetchStub.args[0][0]).to.eql("TOKEN");


     User.prototype.broadcastChange.restore();
     User.prototype.broadcastReset.restore();
   });

   it("has a log out method", function(){

     sinon.stub(User.prototype, "broadcastChange");
     sinon.stub(User.prototype, "broadcastReset");

     var u = new User();
     u.pubsub = {publish : function(){}};
     var fetchStub = sinon.stub(u, "fetchData");
     u.setUser("foo");
     expect(u.isLoggedIn()).to.eql(true);

     //clears the collection
     u.completeLogOut();

     expect(u.isLoggedIn()).to.eql(false);
     expect(u.collection.get("USER").get("user")).to.be.undefined;
     expect(u.collection.get("TOKEN").get("access_token")).to.be.undefined;

     User.prototype.broadcastChange.restore();
     User.prototype.broadcastReset.restore();

   });

   it("provides a hardened interface with the methods widgets and other objects might need", function() {

     var u = new User();
     var hardened = u.getHardenedInstance();
     expect(hardened.__facade__).equals(true);
     expect(hardened.handleCallbackError).to.be.undefined;
     expect(_.keys(hardened)).to.eql([
       "completeLogIn",
       "completeLogOut",
       "isLoggedIn",
       "postData",
       "putData",
       "getUserData",
       "getUserName",
       "isOrcidModeOn",
       "setOrcidMode",
       "setUser",
       "getOpenURLConfig",
       "setMyADSData",
       "__facade__",
       "mixIn"
     ]);

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

     sinon.stub(u.pubsub, "publish");

     u.redirectIfNecessary = sinon.stub();

     u.collection.get("TOKEN").set("access_token", "boo");

     expect(u.pubsub.publish.args[0]).to.eql(["[PubSub]-User-Announcement","user_info_change","TOKEN",{"access_token":"boo","target":"TOKEN"}]);

   });


     it("allows widgets to query user data using different methods", function(){

     sinon.stub(User.prototype, "broadcastChange");

       var minsub = new (MinSub.extend({
         request: function (apiRequest) {
         }
       }))({verbose: false});

       var u = new User();

       var minsub = new (MinSub.extend({
         request: function (apiRequest) {
         }
       }))({verbose: false});


       var appStorage = new AppStorage({csrf : "fake"});

       minsub.beehive.addObject("AppStorage", appStorage);

       u.collection.get("USER").set("user", "foobly@gmail.com");
       u.collection.get("TOKEN").set("api_token", "woobly");
       u.collection.get("USER_DATA").set("openurl", "foo.com");

       expect(u.getUserData()).to.eql({
        "TOKEN": {
          "api_token": "woobly"
        },
        "USER": {
          "user": "foobly@gmail.com"
        },
        "USER_DATA": {"openurl" : "foo.com"}
      });

      expect(u.getUserData("TOKEN")).to.eql( {
        "api_token": "woobly"
      });

      expect(u.getUserName()).to.eql("foobly@gmail.com");


      User.prototype.broadcastChange.restore();

     });


   it("allows widgets to post data to user endpoints", function(){

     var u = new User();

     var minsub = new (MinSub.extend({
       request: function (apiRequest) {
       }
     }))({verbose: false})

     var appStorage = new AppStorage({csrf : "fake"});

     minsub.beehive.addObject("AppStorage", appStorage);

     var api = new Api();
     var requestStub = sinon.stub(Api.prototype, "request");
     minsub.beehive.removeService("Api");
     minsub.beehive.addService("Api", api);

     u.activate(minsub.beehive);

     u.postData("CHANGE_PASSWORD", {old_password: "foo", new_password1 : "goo", new_password_2: "goo"});

     var request = requestStub.args[0][0];
     expect(request).to.be.instanceof(ApiRequest);
     expect(request.toJSON().target).to.eql("accounts/change-password");
     expect(request.toJSON().options.type).to.eql("POST");
     expect(request.toJSON().options.data).to.eql('{"old_password":"foo","new_password1":"goo","new_password_2":"goo"}');
     expect(request.toJSON().options.context).to.eql(u);


     u.putData("TOKEN")

     var request2 = requestStub.args[1][0];

     expect(request2).to.be.instanceof(ApiRequest);
     expect(request2.toJSON().target).to.eql("accounts/token");
     expect(request2.toJSON().options.type).to.eql("PUT");
     expect(request2.toJSON().options.data).to.eql(undefined);
     expect(request2.toJSON().options.context).to.eql(u);


     u.setMyADSData({link_server : "foo.com"});

     var request3 = requestStub.args[2][0];

     expect(request3.toJSON().target).to.eql("myads/user-data");
     expect(request3.toJSON().options.type).to.eql("POST");

     expect(request3.toJSON().options.data).to.eql('{"link_server":"foo.com"}' );



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


   it("handles both success and failure of GET and POST requests", function(){

     var u = new User();

     var minsub = new (MinSub.extend({
       request: function (apiRequest) {
       }
     }))({verbose: false});

     u.activate(minsub.beehive);

     sinon.stub(User.prototype, "broadcastChange");

     sinon.spy(u, "completeLogOut")

     var publishStub = sinon.stub(u.pubsub, "publish");

     u.redirectIfNecessary = sinon.stub();

     //get success
     u.handleSuccessfulGET({api_token : "foo"}, "success", {target: "TOKEN"});
     expect(u.collection.get("TOKEN").toJSON()).to.eql({target: "TOKEN", api_token: "foo"});

     //get fail
     u.handleFailedGET({target : "TOKEN"});
     expect(publishStub.args[1]).to.eql(["[PubSub]-User-Announcement", "data_get_unsuccessful", "TOKEN"]);

     //post success
     //should call the proper callback
     u.handleSuccessfulPOST({api_token : "boobly"}, "success", {target : "TOKEN"});

     expect(publishStub.args[2]).to.eql([
       "[PubSub]-User-Announcement",
       "user_info_change",
       "TOKEN",
       {
         "target": "TOKEN",
         "api_token": "boobly"
       }
     ]);
     expect(publishStub.args[3]).to.eql(["[PubSub]-User-Announcement", "data_post_successful", "TOKEN"]);
     //these two events seem redundant for the token endpoint but wont be for endpoints like change_password
     expect(u.collection.get("TOKEN").toJSON()).to.eql({target: "TOKEN", api_token: "boobly"});

     //post success to delete endpoint
     expect(u.completeLogOut.callCount).to.eql(0);

     u.handleSuccessfulPOST({api_token : "boobly"}, "success", {target : "DELETE"});
     expect(publishStub.args[4]).to.eql(["[PubSub]-User-Announcement", "delete_account_successful", "DELETE"]);
     //these two events seem redundant for the token endpoint but wont be for endpoints like change_password
     expect(u.completeLogOut.callCount).to.eql(1);

     //the deletion of the account should yield 2 more pubsub calls announcing that token and username have changed:
     expect(publishStub.args[5]).to.eql(["[PubSub]-User-Announcement","user_info_change","TOKEN", {"target":"TOKEN"}]);
     expect(publishStub.args[6]).to.eql([
       "[PubSub]-User-Announcement",
       "user_info_change",
       "USER",
       {
         "target": "USER"
       }
     ]);
     expect(publishStub.args[7]).to.eql([
       "[PubSub]-User-Announcement",
       "user_info_change",
       "USER_DATA",
       {
         "target": "USER_DATA"
       }
     ]);


     //post fail
     u.handleFailedPOST({target: "TOKEN", responseJSON : {error : "random error message"}});

     expect(publishStub.args[8]).to.eql(["[PubSub]-User-Announcement", "data_post_unsuccessful", "TOKEN"]);
     expect(publishStub.args[9][0]).to.eql("[Alert]-Message")
     expect(publishStub.args[9][1].msg).to.eql("User update was unsuccessful (random error message)");


     //user_data
     u.handleSuccessfulPOST({ link_server : "fakeServer"}, "success", {target : "USER_DATA"});


     expect(publishStub.args[10]).to.eql([
       "[PubSub]-User-Announcement",
       "user_info_change",
       "USER_DATA",
       {
         "target": "USER_DATA",
         "link_server": "fakeServer"
       }
     ])


     User.prototype.broadcastChange.restore();


   });

 });

});