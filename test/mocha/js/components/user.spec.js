define([
  'js/components/user',
  'underscore'
], function(
  User,
  _
  ){

  describe("User Object", function(){

    it("should have a hardened interface", function(){
      var u = new User();
      var hardened = u.getHardenedInstance();
      expect(hardened.setOrcidMode).to.be.defined;
      expect(hardened.isOrcidModeOn).to.be.defined;
    });

    it("should offer a method for widgets to turn the 'isOrcidModeOn' value on and off", function(){
      var u = new User();
      sinon.stub(u, 'hasBeeHive').returns(true);
      sinon.stub(u, 'getBeeHive').returns({getService: function(name) {
        if (name == 'PubSub')
          return {publish: function() {
            expect(_.toArray(arguments)).to.eql([undefined, undefined, "orcidUIChange", true]);
      }}}});

      expect(u.isOrcidModeOn()).to.be.false;
      u.setOrcidMode(true);
      expect(u.isOrcidModeOn()).to.be.true;
    });
  });
});