define([
    'js/widgets/authentication/widget',
    'js/bugutils/minimal_pubsub',
  ],

function(
  AuthenticationWidget,
  MinSub
  ){


  describe("Authentication Widget", function(){

    afterEach(function(){
      $("#test").empty();
    });

    it("should consist of a Marionette layout that shows the correct view based on the subview as set by the navigator ", function(){

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {}
      }))({verbose: false});

      var hardened = minsub.beehive.getHardenedInstance();
      sinon.stub(hardened, "getObject", function(){return {getRecaptchaKey : function(){return "foo"}}})

      var a = new AuthenticationWidget();
      a.activate(hardened);
      $("#test").append(a.view.render().el);

      //should render nothing since there is no subview indicated in the view model
      expect($("#test").html()).to.eql('<div class="s-authentication-container row s-form-widget"><div class="form-container s-form-container col-sm-6 col-sm-offset-3">\n\n</div></div>');

      a.setSubView("login");

      expect($("#test").find(".log-in").length).to.eql(1);

      a.setSubView("register");

      expect($("#test").find(".register").length).to.eql(1);

      a.setSubView("reset-password-1");

      expect($("#test").find(".reset-password-1").length).to.eql(1);

      a.setSubView("reset-password-2");

      expect($("#test").find(".reset-password-2").length).to.eql(1);

    });

    it("should allow the user to navigate to other views using the buttons on the forms", function(){

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {}
      }))({verbose: false});

      var hardened = minsub.beehive.getHardenedInstance();
      sinon.stub(hardened, "getObject", function(){return {getRecaptchaKey : function(){return "foo"}}})

      var a = new AuthenticationWidget();
      a.activate(hardened);

      var publishStub = sinon.stub(a.pubsub, "publish");

      $("#test").append(a.view.render().el);

      a.setSubView("login");

      $(".show-register").click();

      expect(publishStub.args[0]).to.eql([
        "[Router]-Navigate-With-Trigger",
        "authentication-page",
        {
          "subView": "register"
        }
      ]);

      a.setSubView("reset-password-1");

      $(".show-login").click();

      expect(publishStub.args[1]).to.eql([
        "[Router]-Navigate-With-Trigger",
        "authentication-page",
        {
          "subView": "login"
        }
      ]);

      a.setSubView("register");

      $(".show-login").click();

      expect(publishStub.args[1]).to.eql([
        "[Router]-Navigate-With-Trigger",
        "authentication-page",
        {
          "subView": "login"
        }
      ]);

    });

    it("should interactively validate form inputs, only allowing correctly filled forms to be submitted", function(){

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {}
      }))({verbose: false});

      var hardened = minsub.beehive.getHardenedInstance();
      sinon.stub(hardened, "getObject", function(){return {getRecaptchaKey : function(){return "foo"}}})

      var a = new AuthenticationWidget();
      a.activate(hardened);
      $("#test").append(a.view.render().el);



    });



    it("should handle both success and fail states on submit so the user sees the correct thing", function(){

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {}
      }))({verbose: false});

      var a = new AuthenticationWidget();
      a.viewKey = "foo";
      a.activate(minsub.beehive.getHardenedInstance());

      //register success

      $("#test").append(a.view.render().el);
//      a.;

      expect($(".panel-body").text().trim()).to.eql("Please check your email for further instructions.");
      $("#test").empty();

    });

    it()


    it("should listen to USER_ANNOUNCEMENT and reset the models and, if there was an error, show the relevant form", function(){



    });

    it("should be able to show the correct form depending on the route", function(){


    });



  });

});