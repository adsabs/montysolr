/**
 * Created by rchyla on 3/18/14.
 */

define(['js/components/generic_module', 'js/mixins/dependon', 'js/components/beehive', 'js/services/pubsub',
  'backbone'], function(GenericModule, Dependon, BeeHive, PubSub, Backbone) {

  describe("BeeHive (Scaffolding)", function () {

    it("is a central component of bumblebee", function() {
      expect(new BeeHive()).to.be.instanceof(BeeHive);
      expect(new BeeHive()).to.be.instanceof(GenericModule);
    });

    it("cleans after itself", function() {
      var beehive = new BeeHive();
      var spy = sinon.spy();
      var M = GenericModule.extend({
        close: spy
      });
      var module = new M();
      beehive.addService('foo', module);
      beehive.close();
      expect(spy.callCount).to.be.equal(1);
    });



  });

  describe("BeeHive - advanced operations", function() {
    var beehive = null;

    beforeEach(function(done) {
      beehive = new BeeHive();
      var service1 = new (GenericModule.extend({
        close: sinon.spy()
      }))();
      var service2 = new (GenericModule.extend({
        close: sinon.spy()
      }))();
      beehive.addService('S1', service1);
      beehive.addService('S2', service2);
      done();
    });

    afterEach(function(done) {
      beehive.close();
      beehive = null;
      done();
    });

    it("knows how to create unique, write-protected version of itself", function() {
      beehive.addService('PubSub', new PubSub());
      var hardened = beehive.getHardenedInstance();

      expect(hardened.Services.__facade__).to.be.OK;
      expect(hardened.Services.PubSub.__facade__).to.be.true;

    });


  });
});

