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
     u.pubsub = {publish : function(){}, getPubSubKey : function(){}};
     minsub.beehive.addObject("User", u);

     var n = new NavBarWidget();
     n.activate(minsub.beehive.getHardenedInstance())
     $("#test").append(n.view.render().el);

     expect($(".s-orcid-button-container").hasClass("s-active")).to.be.false;

     u.setOrcidMode(true);
     var n = new NavBarWidget();
     n.activate(minsub.beehive.getHardenedInstance());
     $("#test").append(n.view.render().el);

     expect($(".s-orcid-button-container").hasClass("s-active")).to.be.true;

   })


   it("should notify the user object when orcid mode is toggled", function(){


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

     $(".switch label").click();

     expect(signInStub.callCount).to.eql(1);
     expect(setOrcidModeStub.callCount).to.eql(1);
     expect(setOrcidModeStub.args[0][0]).to.eql(true);

   });



 })




})