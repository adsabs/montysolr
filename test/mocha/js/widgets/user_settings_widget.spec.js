define([
  'js/widgets/user_settings/widget',
  'js/bugutils/minimal_pubsub',
  'js/components/user',
  'js/components/session'
], function (
  UserSettings,
  MinSub,
  User,
  Session
  ) {

  describe("User Settings Widget (user_settings_widget.spec.js)", function () {

    afterEach(function () {
      $("#test").empty();
    });


    it("should consist of a Marionette layout that shows the correct subview based on the model with the 'active' attr in its navCollection", function () {

      var u = new UserSettings();

      $("#test").append(u.render().el);

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {
        }
      }))({verbose: false});

      var fakeUser = {getToken: function () {
        var d = $.Deferred();
        d.resolve({access_token: "foo"});
        return d
      }, getHardenedInstance: function () {
        return this
      }};
      var fakeCSRFManager = {getRecaptchaKey: function () {
        return "foo"
      }, getHardenedInstance: function () {
        return this
      }};

      minsub.beehive.addObject("User", fakeUser);
      minsub.beehive.addObject("CSRFManager", fakeCSRFManager);

      var hardened = minsub.beehive.getHardenedInstance();

      u.activate(hardened);

      $("#test").append(u.view.render().el);

      //initial view should be empty
      //the subview is set by the navigator

      expect($("#test .content-container").html().trim()).to.eql('');

      u.setSubView("email");
      expect($("#test .content-container").find(".change-email").length).to.eql(1);

      u.setSubView("password");
      expect($("#test .content-container").find(".change-password").length).to.eql(1);

      u.setSubView("token");
      expect($("#test .content-container").find(".change-token").length).to.eql(1);

      u.setSubView("delete");
      expect($("#test .content-container").find(".delete-account").length).to.eql(1);


    });


    it("should interactively validate form inputs, only allowing correctly filled forms to be submitted", function () {

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {
        }
      }))({verbose: false});

      var fakeUser = {getToken: function () {
        var d = $.Deferred();
        d.resolve({access_token: "foo"});
        return d
      }, changePassword: sinon.spy(), getHardenedInstance: function () {
        return this
      }};
      var fakeCSRFManager = {getRecaptchaKey: function () {
        return "foo"
      }, getHardenedInstance: function () {
        return this
      }};

      minsub.beehive.addObject("User", fakeUser);
      minsub.beehive.addObject("CSRFManager", fakeCSRFManager);

      var hardened = minsub.beehive.getHardenedInstance();

      var u = new UserSettings();
      u.activate(hardened);
      $("#test").append(u.view.render().el);

      //testing form validation for change password page
      u.setSubView("password");

      var triggerStub = sinon.stub(u.view, "trigger");

      $("#test").find("input[name=old_password]").val("foo");
      $("#test").find("input[name=old_password]").trigger("change");
      expect($("#test").find("input[name=old_password]").parent().hasClass("has-success")).to.be.false;
      expect($("#test").find("input[name=old_password]").parent().hasClass("has-error")).to.be.false;

      $("#test").find("input[name=old_password]").val("Foooo5");
      $("#test").find("input[name=old_password]").trigger("change");
      expect($("#test").find("input[name=old_password]").parent().hasClass("has-success")).to.be.true;
      expect($("#test").find("input[name=old_pasword]").parent().hasClass("has-error")).to.be.false;

      $("#test").find("input[name=new_password1]").val("boo");
      $("#test").find("input[name=new_password1]").trigger("change");
      expect($("#test").find("input[name=new_password1]").parent().hasClass("has-success")).to.be.false;
      expect($("#test").find("input[name=new_password1]").parent().hasClass("has-error")).to.be.false;

      $("#test").find("input[name=new_password1]").val("Boooo3");
      $("#test").find("input[name=new_password1]").trigger("change");
      expect($("#test").find("input[name=new_password1]").parent().hasClass("has-success")).to.be.true;
      expect($("#test").find("input[name=new_password1]").parent().hasClass("has-error")).to.be.false;

      //premature submit should trigger error message instead of submitting the form,
      // and show error highlight on invalid fields
      expect($("#test").find("button[type=submit]").hasClass("btn-success")).to.be.false;
      $("#test").find("button[type=submit]").click();

      expect(triggerStub.callCount).to.eql(0);

      expect($("#test").find("input[name=new_password1]").parent().hasClass("has-success")).to.be.true;
      expect($("#test").find("input[name=new_password2]").parent().hasClass("has-error")).to.be.true;

      expect($("#test").find("input[name=new_password1]").parent().find(".help-block").hasClass("no-show")).to.be.true;
      expect($("#test").find("input[name=new_password2]").parent().find(".help-block").hasClass("no-show")).to.be.false;

      $("#test").find("input[name=new_password2]").val("Boo");
      $("#test").find("input[name=new_password2]").trigger("change");

      expect($("#test").find("input[name=new_password2]").parent().hasClass("has-error")).to.be.true;

      $("#test").find("input[name=new_password2]").val("Boooo3");
      $("#test").find("input[name=new_password2]").trigger("change");

      expect($("#test").find("input[name=new_password2]").parent().hasClass("has-error")).to.be.false;

      //finally,  fake the g-recaptcha-response
      u.view.content.currentView.model.set("g-recaptcha-response", "foo");
      expect($("#test").find("button[type=submit]").hasClass("btn-success")).to.be.true;

      $("#test").find("button[type=submit]").click();
      expect(triggerStub.callCount).to.eql(1);

    });

    it("should listen to submit clicks and call the user's postData method", function () {

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {
        }
      }))({verbose: false});

      var fakeUser = {getToken: function () {
        var d = $.Deferred();
        d.resolve({access_token: "foo"});
        return d
      }, changePassword: sinon.spy(function () {
        return $.Deferred();
      }), getHardenedInstance: function () {
        return this
      }};
      var fakeCSRFManager = {getRecaptchaKey: function () {
        return "foo"
      }, getHardenedInstance: function () {
        return this
      }};

      minsub.beehive.addObject("User", fakeUser);
      minsub.beehive.addObject("CSRFManager", fakeCSRFManager);
      var u = new UserSettings();

      var hardened = minsub.beehive.getHardenedInstance();

      u.activate(hardened);
      $("#test").append(u.view.render().el);

      //testing form validation for change password page
      u.setSubView("password");

      $("#test").find("input[name=old_password]").val("Foooo5");
      $("#test").find("input[name=old_password]").trigger("change");

      $("#test").find("input[name=new_password1]").val("Boooo3");
      $("#test").find("input[name=new_password1]").trigger("change");

      $("#test").find("input[name=new_password2]").val("Boooo3");
      $("#test").find("input[name=new_password2]").trigger("change");

      $("#test").find("button[type=submit]").click();

      expect(fakeUser.changePassword.callCount).to.eql(1);
      expect(JSON.stringify(fakeUser.changePassword.args[0])).to.eql('[{"old_password":"Foooo5","new_password1":"Boooo3","new_password2":"Boooo3"}]');

    });

    it("should listen to the USER_ANNOUNCEMENT to see if user is logged in, and add done callbacks to user methods where appropriate ", function () {

      //the widget should respond when different data posts have been successful and show the user
      //the proper information, or else if it failed, offer them the opportunity to redo it

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {
        }
      }))({verbose: false})

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {
        }
      }))({verbose: false});

      var fakeUser = {
        generateToken: function () {
          var d = $.Deferred();
          d.resolve({access_token: "new_token"});
          return d.promise()
        },
        getToken: function () {
          var d = $.Deferred();
          d.resolve({access_token: "current_token"});
          return d
        },
        changePassword: sinon.spy(function () {
          return $.Deferred();
        }),
        getHardenedInstance: function () {
          return this
        },
        USER_SIGNED_IN: User.prototype.USER_SIGNED_IN,
        USER_SIGNED_OUT: User.prototype.USER_SIGNED_OUT
      };
      var fakeCSRFManager = {getRecaptchaKey: function () {
        return "foo"
      }, getHardenedInstance: function () {
        return this
      }};

      minsub.beehive.addObject("User", fakeUser);
      minsub.beehive.addObject("CSRFManager", fakeCSRFManager);
      var u = new UserSettings();

      var hardened = minsub.beehive.getHardenedInstance();

      u.activate(hardened);

      var ps = u.getPubSub();
      ps.publish(ps.USER_ANNOUNCEMENT, User.prototype.USER_SIGNED_IN, "alex");
      expect(u.model.get("user")).to.eql("alex");

      ps.publish(ps.USER_ANNOUNCEMENT, User.prototype.USER_SIGNED_OUT);
      expect(u.model.get("user")).to.be.undefined;

      $("#test").append(u.view.render().el);

      u.setSubView("token");
      debugger

      expect($("#test input").val()).to.eql("current_token");

      $("#test button[type=submit]").click();

      expect($("#test input").val()).to.eql("new_token");
    });

  });

});