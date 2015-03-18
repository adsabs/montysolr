define([
  'js/widgets/navbar/widget',
  'js/bugutils/minimal_pubsub',
  'js/components/user',
  'js/components/session',
  'js/modules/orcid/orcid_api'

], function(

  NavBarWidget,
  MinSub,
  User,
  Session,
  OrcidApi

  ){

  describe("navigation bar widget", function(){

    afterEach(function(){
      $("#test").empty();
    });

//orcid accounts

    it("should query initial logged in / logged out orcid states in order to render the correct values", function(){

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {}
      }))({verbose: false});

      var u = new User();
      u.activate(minsub.beehive);
      minsub.beehive.addObject("User", u);


      var profileInfo = {"orcid-bio":{"personal-details": {"family-name": {"value" : "Chyla"}, "given-names": {"value": "Roman"}}}}

      minsub.beehive.addService('OrcidApi', {
        hasAccess: function() {return true},
        getHardenedInstance: function() {return this},
        getUserProfile : function(){
          var d = $.Deferred();
          d.resolve(profileInfo);
          return d
        }
      });

      var n = new NavBarWidget();
      n.activate(minsub.beehive.getHardenedInstance());
      $("#test").append(n.render().el);
      expect($(".s-orcid-button-container").hasClass("s-active")).to.be.false;
      $('#test').empty();


      var n = new NavBarWidget();
      n.activate(minsub.beehive.getHardenedInstance());
      minsub.publish(minsub.USER_ANNOUNCEMENT, 'orcidUIChange');

      $("#test").append(n.render().el);

      //orcid signed in, orcid mode off

      expect($(".orcid-dropdown div:first").text()).to.eql(" Signed in to ORCID as Roman Chyla");
      expect($("input.orcid-mode").is(":checked")).to.eql(false);

      //orcid signed in, orcid mode on

      n.view.model.set("orcidModeOn", true);

      expect($(".orcid-dropdown div:first").text()).to.eql(" Signed in to ORCID as Roman Chyla");
      expect($("input.orcid-mode").is(":checked")).to.eql(true);


    });

    it("should notify the user object when orcid mode is toggled", function(done){

      var minsub = new (MinSub.extend({
        request: function (apiRequest) {}
      }))({verbose: false});
      var u = new User();

      var a = new OrcidApi();

      var signInStub = sinon.stub(a, "signIn");
      u.pubsub = {publish : function(){}, getPubSubKey : function(){}};
      var setOrcidModeStub = sinon.stub(u, "setOrcidMode");
      minsub.beehive.addObject("User", u);
      minsub.beehive.addService("OrcidApi", a);

      $("#test").empty();
      var n = new NavBarWidget();
      n.activate(minsub.beehive.getHardenedInstance());

      $("#test").append(n.view.render().el);
      var $w = n.view.$el;
      $w.find('.orcid-sign-in').click();
      debugger;

      expect(signInStub.callCount).to.eql(1);
      expect(setOrcidModeStub.callCount).to.eql(1);
      expect(setOrcidModeStub.args[0][0]).to.eql(true);
      done();

    });

    it("should trigger navigate event when 'my orcid papers' is clicked", function(){


      var minsub = new (MinSub.extend({
        request: function (apiRequest) {}
      }))({verbose: false});

      var u = new User();
      u.activate(minsub.beehive);
      minsub.beehive.addObject("User", u);

      minsub.beehive.addService('OrcidApi', {
        hasAccess: function() {return true},
        getHardenedInstance: function() {return this}
      });

      u.setOrcidMode(true);
      var n = new NavBarWidget();
      n.activate(minsub.beehive.getHardenedInstance());
      n.pubsub.publish = sinon.spy();
      $("#test").append(n.render().el);

      //show active view
      $("#test").find('.orcid-sign-in').click();

      $("#test").find('.orcid-link').click();
      expect(n.pubsub.publish.args[0]).to.eql(["[Router]-Navigate-With-Trigger", "orcid-page"]);

    });

//ADS user accounts

  it("should query initial user logged in/logged out state and show the correct options", function(){

    /*
    navigation bar queries logged in/logged out state solely on the basis
     * of whether user.getUserName() returns a name.
      * */

    var minsub = new (MinSub.extend({
      request: function (apiRequest) {}
    }))({verbose: false});

    sinon.stub(User.prototype, "redirectIfNecessary");
    var u = new User();

    minsub.beehive.addObject("User", u);

    minsub.beehive.addService('OrcidApi', {
      hasAccess: function() {return true},
      getHardenedInstance: function() {return this},
      getUserProfile : function(){
        var d = $.Deferred();
        d.resolve(profileInfo);
        return d
      }
    });

    var n = new NavBarWidget();
    n.activate(minsub.beehive.getHardenedInstance());
    u.activate(minsub.beehive);

    $("#test").append(n.view.render().el);

    u.collection.get("USER").set("user", "bumblebee");

    expect(n.view.$("li.login").length).to.eql(0);
    expect(n.view.$("li.register").length).to.eql(0);

    expect(n.view.$(".btn.btn-link.dropdown-toggle").length).to.eql(1);
    expect(n.view.$(".btn.btn-link.dropdown-toggle").html()).to.eql('\n                        bumblebee <span class="caret"></span>\n                    ');

    //lack of username indicates user is logged out
    u.collection.get("USER").set("user", undefined);

    minsub.publish(minsub.pubsub.USER_ANNOUNCEMENT, "user_info_change");

    expect(n.view.$(".btn.btn-link.dropdown-toggle").length).to.eql(0);

    expect(n.view.$("li.login").length).to.eql(1);
    expect(n.view.$("li.register").length).to.eql(1);
    expect(n.view.$(".btn.btn-link.dropdown-toggle").length).to.eql(0);

  });


  it("should emit the proper events when user links are clicked", function(){

    var minsub = new (MinSub.extend({
      request: function (apiRequest) {}
    }))({verbose: false});

    var u = new User();
    u.pubsub = {publish : function(){}, getPubSubKey : function(){}};
    minsub.beehive.addObject("User", u);

    var s = new Session();
    sinon.stub(s, "logout");

    minsub.beehive.addObject("Session", s);

    var orcidSignOutSpy = sinon.spy();

    minsub.beehive.addService('OrcidApi', {
      hasAccess: function() {return true},
      getHardenedInstance: function() {return this},
      signOut : orcidSignOutSpy,
      getUserProfile : function(){
        var d = $.Deferred();
        d.resolve(profileInfo);
        return d
      }
    });

    var n = new NavBarWidget();
    n.activate(minsub.beehive.getHardenedInstance());
    var publishSpy = sinon.stub(n.pubsub, "publish");

    $("#test").append(n.view.render().el);

    $("#test").find(".login").click();

    expect(publishSpy.callCount).to.eql(1);
    expect(publishSpy.args[0][0]).to.eql(minsub.pubsub.NAVIGATE);
    expect(publishSpy.args[0][1]).to.eql("authentication-page");
    expect(publishSpy.args[0][2].subView).to.eql("login");

    $("#test").find(".register").click();
    expect(publishSpy.callCount).to.eql(2);
    expect(publishSpy.args[1][0]).to.eql(minsub.pubsub.NAVIGATE);
    expect(publishSpy.args[1][1]).to.eql("authentication-page");
    expect(publishSpy.args[1][2].subView).to.eql("register");
    
    //now show navbar in logged in state
    
    u.collection.get("USER").set("user", "foo");
    minsub.publish(minsub.pubsub.USER_ANNOUNCEMENT, "user_info_change", "USER");

    $("#test").find(".settings").click();
    expect(publishSpy.callCount).to.eql(3);
    expect(publishSpy.args[2][0]).to.eql(minsub.pubsub.NAVIGATE);
    expect(publishSpy.args[2][1]).to.eql("settings-page");
    expect(publishSpy.args[2][2]).to.eql(undefined);

    $("#test").find(".logout").click();
    expect(publishSpy.callCount).to.eql(3);
    //calls session logout method explicitly

    expect(s.logout.callCount).to.eql(1);
    expect(orcidSignOutSpy.callCount).to.eql(1);

  });

});

});
