/**
 * Created by rchyla on 3/18/14.
 */

define(['js/components/generic_module', 'js/mixins/dependon',
  'js/components/beehive', 'js/services/pubsub',
  'js/components/services_container', 'js/components/pubsub_events',
  'backbone'], function(GenericModule, Dependon, BeeHive, PubSub, ServicesContainer, PubSubEvents,
                        Backbone) {

  describe("BeeHive (Scaffolding)", function () {

    it("returns API object", function() {
      expect(new BeeHive()).to.be.instanceof(BeeHive);
      expect(new BeeHive()).to.be.instanceof(GenericModule);
      expect(new BeeHive().Services).to.be.instanceof(ServicesContainer);
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
      expect(hardened.__facade__).to.be.OK;

      // test we have the simplified interface
      var pubsub = beehive.Services.get('PubSub');
      var all = sinon.spy();
      pubsub.on('all', all);
      var spy = sinon.spy();

      var hardenedPubsub = hardened.Services.get('PubSub');
      hardenedPubsub.subscribe('event-foo', spy);
      hardenedPubsub.publish('event-foo', [1,2,3]);
      expect(all.args[0].slice(0,2)).to.eql(['event-foo', [1,2,3]]);
      expect(spy.args[0].slice(0,1)).to.eql([[1,2,3]]);



    });


  });
});

