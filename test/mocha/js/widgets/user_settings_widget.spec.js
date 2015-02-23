define([
  'js/widgets/user_settings/widget',
  'js/bugutils/minimal_pubsub',
  'js/components/user',
  'js/components/session'
], function(
  UserSettings,
  MinSub,
  User,
  Session

  ){

  describe("User Settings Widget", function(){

    afterEach(function(){
      $("#test").empty();
    });


    it("should consist of a Marionette layout that shows the correct subview based on the model with the 'active' attr in its navCollection", function(){

      var u = new UserSettings();

      $("#test").append(u.render().el );

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {}
      }))({verbose: false});

      var hardened = minsub.beehive.getHardenedInstance();
      sinon.stub(hardened, "getObject", function(){return {getRecaptchaKey : function(){return "foo"}}})

      var u = new UserSettings();
      u.activate(hardened);
      $("#test").append(u.view.render().el);

      //initial view should be empty

      expect($("#test .content-container").html().trim()).to.eql('');

      u.setSubView("email");
      expect($("#test .content-container").find(".change-email").length).to.eql(1);

      u.setSubView("password");
      expect($("#test .content-container").find(".change-password").length).to.eql(1);

      u.setSubView("token");
      expect($("#test .content-container").find(".change-token").length).to.eql(1);

      u.setSubView("preferences");
      expect($("#test .content-container").find(".change-preferences").length).to.eql(1);

      u.setSubView("delete");
      expect($("#test .content-container").find(".delete-account").length).to.eql(1);


    });

      it("should allow the user to navigate to other options using the side nav", function(){

        var u = new UserSettings();

        $("#test").append(u.render().el );

        var minsub = new (MinSub.extend({
          request: function (apiRequest) {}
        }))({verbose: false});

        var hardened = minsub.beehive.getHardenedInstance();
        sinon.stub(hardened, "getObject", function(){return {getRecaptchaKey : function(){return "foo"}}});

        var u = new UserSettings();

        u.activate(hardened);
        var publishStub = sinon.stub(u.pubsub, "publish");
        $("#test").append(u.view.render().el);

        //get widget to render
        u.setSubView("email");

        var subViews = ["preferences", "email", "password", "token", "delete"];

        _.each(subViews,function(href){

          $("#test a[data-subpage=" + href + "]").click();

        });

        for (i = 0; i < subViews.length; i++ ){

          expect(publishStub.args[i]).to.eql([ "[Router]-Navigate-With-Trigger", "settings-page",  { "subView": subViews[i]} ]);

        }


      });

   it("should listen to submit clicks and call the user's postData method", function(){

   });

  it("should listen to the USER_ANNOUNCMENT and rerender with the proper data", function(){


  })


  });

});