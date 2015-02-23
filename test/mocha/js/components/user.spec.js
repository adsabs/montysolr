define([
  'underscore',
  'js/components/user',
  'js/components/api_targets',
  'js/bugutils/minimal_pubsub',
  'js/components/api_request',
  'js/components/json_response',
  'js/components/api_query',
  'js/services/api'

], function(
  _,
  User,
  ApiTargets,
  MinSub,
  ApiRequest,
  JsonResponse,
  ApiQuery,
  Api
  ){

 describe("User Object", function(){

   it("keeps track of the user's signed in/anonymous state based on the content of the 'user' model within its collection, and responds to changes by updating user collection", function(){

     sinon.stub(User.prototype, "broadcastChange");
     sinon.stub(User.prototype, "broadcastReset");

     var u = new User();

     var fetchStub = sinon.stub(u, "fetchData");

     expect(u.isLoggedIn()).to.eql(false);

     u.setUser("foo");

     expect(u.collection.get("USER").get("user")).to.eql("foo");
     expect(u.isLoggedIn()).to.be.true;

     expect(fetchStub.args[0][0]).to.eql("TOKEN");
     expect(fetchStub.args[1][0]).to.eql("USER");

     //clears the collection
     u.completeLogOut();

     expect(u.isLoggedIn()).to.eql(false);
     expect(u.collection.get("USER").get("user")).to.be.undefined;
     expect(u.collection.get("TOKEN").get("access_token")).to.be.undefined;

     User.prototype.broadcastChange.restore();
     User.prototype.broadcastReset.restore();


   });

   it("provides a hardened interface with the methods widgets and other objects might need", function(){

     var u = new User();
     var hardened = u.getHardenedInstance();
     expect(hardened.__facade__).equals(true);
     expect(hardened.handleCallbackError).to.be.undefined;
     expect(_.keys(hardened)).to.eql(["completeLogIn", "completeLogOut", "isLoggedIn", "postData", "getUserData", "getUserName", "isOrcidUIOn", "setOrcidMode", "__facade__", "mixIn"]);

   });

   it("broadcasts changes to its collection to interested widgets/other objects", function(){

     var u = new User();

     var minsub = new (MinSub.extend({
       request: function (apiRequest) {
       }
     }))({verbose: false});

     var beehive = minsub.beehive.getHardenedInstance()
     u.pubsub =  beehive.Services.get('PubSub');
     sinon.stub(u.pubsub, "publish");

     u.redirectIfNecessary = sinon.stub();

     u.collection.get("TOKEN").set("access_token", "boo");

     expect(u.pubsub.publish.args[0]).to.eql(["[PubSub]-User-Announcement", "user_info_change", "TOKEN"]);

   });


     it("allows widgets to query user data using different methods", function(){

     sinon.stub(User.prototype, "broadcastChange");

       var u = new User();
       u.collection.get("USER").set("user", "foobly@gmail.com");
       u.collection.get("TOKEN").set("api_token", "woobly");

      expect(u.getUserData()).to.eql({
        "TOKEN": {
          "api_token": "woobly"
        },
        "USER": {
          "user": "foobly@gmail.com"
        }
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
     }))({verbose: false});

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

   });


   it("handles both success and failure of GET and POST requests", function(){

     var u = new User();

     var minsub = new (MinSub.extend({
       request: function (apiRequest) {
       }
     }))({verbose: false});

     u.activate(minsub.beehive);

     sinon.stub(User.prototype, "broadcastChange");

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

     expect(publishStub.args[2]).to.eql(["[PubSub]-User-Announcement", "user_info_change", "TOKEN"]);
     expect(publishStub.args[3]).to.eql(["[PubSub]-User-Announcement", "data_post_successful", "TOKEN"]);
     //these two events seem redundant for the token endpoint but wont be for endpoints like change_password
     expect(u.collection.get("TOKEN").toJSON()).to.eql({target: "TOKEN", api_token: "boobly"});

     //post fail
     u.handleFailedPOST({target: "TOKEN"}, "error", "what??");

     expect(publishStub.args[4]).to.eql(["[PubSub]-User-Announcement", "data_post_unsuccessful", "TOKEN"]);
     expect(publishStub.args[5][0]).to.eql("[Alert]-Message")
     expect(publishStub.args[5][1].msg).to.eql("User update was unsuccessful (what??)");


     User.prototype.broadcastChange.restore();


   });

 });

});