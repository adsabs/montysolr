define(['js/components/generic_module', 'js/services/pubsub', 'js/components/pubsub_key',
  'backbone'], function(GenericModule, PubSub, PubSubKey, Backbone) {

  describe("PubSub (Service)", function () {
      
    it("should return PubSub object (generic module)", function() {
      expect(new PubSub()).to.be.an.instanceof(GenericModule);
      expect(new PubSub()).to.be.an.instanceof(PubSub);
    });

    it("provides keys", function() {
      var p = new PubSub();
      var k = p.getPubSubKey();
      expect(k.getCreator()).to.be.equal(p);
      expect(k.getId()).to.not.be.undefined;

      expect(p.getPubSubKey()).to.not.be.equal(p.getPubSubKey());
    });

    it("requires the key to subscribe/unsubscribe", function() {
      var p = new PubSub();
      var k = p.getPubSubKey();
      var spy = sinon.spy();

      p.subscribe(k, 'event', spy);
      p.trigger('event');
      p.unsubscribe(k, 'event');
      p.trigger('event');

      expect(spy.callCount).to.be.equal(1);

      expect(function() {p.subscribe({}, 'event', spy)}).to.throw(Error);
      expect(function() {p.subscribe('event', spy)}).to.throw(Error);
      expect(function() {p.unsubscribe({}, 'event', spy)}).to.throw(Error);
      expect(function() {p.unsubscribe('event', spy)}).to.throw(Error);

    });

    it("checks the identity of the key in default (strict) mode", function() {
      var p = new PubSub();
      var p2 = new PubSub();
      var k = p.getPubSubKey();
      var k2 = p2.getPubSubKey();
      var spy = sinon.spy();

      p.subscribe(k, 'event', spy);
      expect(p._events['event'].length).to.be.equal(1);

      expect(function() {p.subscribe(k2, 'event', spy)}).to.throw(Error);

      // now in promiscuous mode
      p = new PubSub({strict: false});
      p.subscribe(k2, 'event', spy);
      expect(p._events['event'].length).to.be.equal(1);

    });

    it("protects callbacks of other modules", function() {

      var pubsub = new PubSub();
      var module1 = _.extend({}, Backbone.Events);
      var module2 = {};

      module1.key = pubsub.getPubSubKey();
      module1.callback = sinon.spy();

      module2.key = pubsub.getPubSubKey();
      module2.callback = sinon.spy();

      var spy1 = module1.callback, spy2 = module2.callback;

      pubsub.subscribe(module1.key, 'event', module1.callback);
      pubsub.subscribe(module2.key, 'event', module2.callback);
      pubsub.trigger('event');

      expect(spy1.callCount).to.be.equal(1);
      expect(spy2.callCount).to.be.equal(1);

      // remove only events of 1st module
      pubsub.unsubscribe(module1.key, 'event');
      pubsub.trigger('event');

      // test it worked
      expect(spy1.callCount).to.be.equal(1);
      expect(spy2.callCount).to.be.equal(2);

      // now unsubscribe all callbacks
      pubsub.unsubscribe(module1.key);
      pubsub.unsubscribe(module2.key);
      pubsub.trigger('event');
      expect(pubsub._events).to.be.empty;

      // test it worked
      expect(spy1.callCount).to.be.equal(1);
      expect(spy2.callCount).to.be.equal(2);

      // now put them back
      pubsub.subscribe(module1.key, 'event', spy1);
      pubsub.subscribe(module2.key, 'event', spy2);
      pubsub.trigger('event');

      // test it worked
      expect(spy1.callCount).to.be.equal(2);
      expect(spy2.callCount).to.be.equal(3);

      // try detaching right callback, but wrong key
      expect(pubsub._events['event'].length).to.be.equal(2);
      pubsub.unsubscribe(module1.key, 'event', spy2);
      pubsub.trigger('event');
      expect(pubsub._events['event'].length).to.be.equal(2);

      // it should still be there
      expect(spy1.callCount).to.be.equal(3);
      expect(spy2.callCount).to.be.equal(4);

    });



  });
});
