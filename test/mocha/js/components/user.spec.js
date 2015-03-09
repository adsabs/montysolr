define([
  'js/components/user'
], function(
  User
  ){

  describe("User Object", function(){


    it("should have a hardened interface", function(){

      var u = new User();
      var hardened = u.getHardenedInstance();
      expect(hardened.setOrcidMode).to.be.defined;
      expect(hardened.orcidUIOn).to.be.defined;

    });

    it("should offer a method for widgets to turn the 'orcidUIOn' value on and off", function(){

      var u = new User();
      u.pubsub = {publish : function(){}, getPubSubKey : function(){}};
      expect(u.orcidUIOn()).to.be.false;
      u.setOrcidMode(true);
      expect(u.orcidUIOn()).to.be.true;



    });


  });



})