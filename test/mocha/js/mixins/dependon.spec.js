/**
 * Created by rchyla on 3/16/14.
 */

define(['js/components/generic_module', 'js/mixins/dependon', 'js/components/beehive',
  'backbone'], function(GenericModule, Dependon, BeeHive, Backbone) {

  describe("Application Dependencies (Scaffolding)", function () {

    it("should be possible to attach BeeHive to generic module", function() {
      _.extend(GenericModule.prototype, Dependon.BeeHive);
      var module = new GenericModule();
      var beehive = new BeeHive();
      module.setBeeHive(beehive);
      expect(module.getBeeHive()).to.be.equal(beehive);
      expect(module.hasBeeHive()).to.be.true;
    });

    it("should be possible to attach BeeHive to any object", function() {
      var module = {};
      _.extend(module, Dependon.BeeHive);
      var beehive = new BeeHive();
      module.setBeeHive(beehive);
      expect(module.getBeeHive()).to.be.equal(beehive);
      expect(module.hasBeeHive()).to.be.true;
    });

  });
});
