define([
  'js/widgets/navbar/widget',
  'js/bugutils/minimal_pubsub',
  'js/components/user',
  'js/modules/orcid/orcid_api'

], function(

  NavBarWidget,
  MinSub,
  User,
  OrcidApi

  ){

  describe("navigation bar widget", function(){

    afterEach(function(){
      $("#test").empty();
    });


    it("should query initial logged in / logged out orcid states in order to render the correct values", function(){

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

      var n = new NavBarWidget();
      n.activate(minsub.beehive.getHardenedInstance());
      $("#test").append(n.render().el);
      expect($(".s-orcid-button-container").hasClass("s-active")).to.be.false;
      $('#test').empty();

      u.setOrcidMode(true);
      var n = new NavBarWidget();
      n.activate(minsub.beehive.getHardenedInstance());
      $("#test").append(n.render().el);

      expect($(".orcid-dropdown li:first div:first").text().trim()).to.eql("You are signed in to ORCID and able to add papers from ADS to your ORCID profile.");

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

      var n = new NavBarWidget();
      n.activate(minsub.beehive.getHardenedInstance());

      $("#test").append(n.view.render().el);
      var $w = n.view.$el;
      $w.find('.orcid-sign-in').click();

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

      expect(n.pubsub.publish.args[0]).to.eql(["[Router]-Navigate-Without-Trigger", "orcid-page"]);



    })
  })

});