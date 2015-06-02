define(['js/components/generic_module', 'js/services/pubsub', 'js/components/pubsub_key', 'js/components/pubsub_events',
  'js/components/facade', 'backbone'],
  function(GenericModule, PubSub, PubSubKey, PubSubEvents, Facade, Backbone) {

  describe("PubSub - default implementation of Service (pubsub.spec.js)", function () {
      
    it("should return PubSub object (generic module)", function() {
      expect(new PubSub()).to.be.an.instanceof(GenericModule);
      expect(new PubSub()).to.be.an.instanceof(PubSub);
    });

    it("provides keys", function() {
      var p = new PubSub();
      var k = p.getPubSubKey();
      expect(p.pubSubKey).to.be.instanceof(PubSubKey);
      expect(k.getCreator()).to.be.equal(p.pubSubKey);
      expect(k.getId()).to.not.be.undefined;

      expect(p.getPubSubKey()).to.not.be.equal(p.getPubSubKey());
    });

    it('has start/destroy methods', function() {
      var p = new PubSub();
      expect(p.running).to.be.true; // by default, it is ready
      p.running = false;
      sinon.spy(p, 'publish');
      p.start();
      expect(p.running).to.be.true;
      expect(p.publish.firstCall.args[1]).to.be.eql(PubSubEvents.OPENING_GATES);
      expect(p.publish.secondCall.args[1]).to.be.eql(PubSubEvents.OPEN_FOR_BUSINESS);

      p.publish.reset();
      p.destroy();
      expect(p.running).to.be.false;
      expect(p.publish.firstCall.args[1]).to.be.eql(PubSubEvents.CLOSING_GATES);
      expect(p.publish.secondCall.args[1]).to.be.eql(PubSubEvents.CLOSED_FOR_BUSINESS);

    });

    it("requires the key to subscribe/unsubscribe", function() {
      var p = new PubSub();
      var k = p.getPubSubKey();
      var spy = sinon.spy();

      p.subscribe(k, 'event', spy);
      p.publish(k, 'event');
      p.unsubscribe(k, 'event');
      p.publish(k, 'event');

      expect(spy.callCount).to.be.equal(1);

      expect(function() {p.subscribe({}, 'event', spy)}).to.throw(Error);
      expect(function() {p.subscribe('event', spy)}).to.throw(Error);
      expect(function() {p.unsubscribe({}, 'event', spy)}).to.throw(Error);
      expect(function() {p.unsubscribe('event', spy)}).to.throw(Error);

      // destroy pubsub and try to use it
      p.destroy();
      expect(function() {p.subscribe(k, 'event', spy);}).to.throw.Error;

    });

    it("checks the identity of the key in default (strict) mode", function() {
      var p = new PubSub();
      var p2 = new PubSub();
      var k = p.getPubSubKey();
      var k2 = p2.getPubSubKey();
      var spy = sinon.spy();

      p.subscribe(k, 'event', spy);
      p.publish(k, 'event');
      expect(spy.callCount).to.be.equal(1);

      expect(function() {p.subscribe(k2, 'event', spy)}).to.throw(Error);
      expect(function() {p.publish(k2, 'event', 'foo')}).to.throw(Error);

      // now in promiscuous mode
      spy = sinon.spy();
      p = new PubSub({strict: false});
      p.subscribe(k2, 'event', spy);
      p.publish(k, 'event');
      expect(spy.callCount).to.be.equal(1);

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
      pubsub.publish(module1.key, 'event');

      expect(spy1.callCount).to.be.equal(1);
      expect(spy2.callCount).to.be.equal(1);

      // remove only events of 1st module
      pubsub.unsubscribe(module1.key, 'event');
      pubsub.publish(module1.key, 'event');

      // test it worked
      expect(spy1.callCount).to.be.equal(1);
      expect(spy2.callCount).to.be.equal(2);

      // now unsubscribe all callbacks
      pubsub.unsubscribe(module1.key);
      pubsub.unsubscribe(module2.key);
      pubsub.publish(module1.key, 'event');

      // test it worked
      expect(spy1.callCount).to.be.equal(1);
      expect(spy2.callCount).to.be.equal(2);

      // now put them back
      pubsub.subscribe(module1.key, 'event', spy1);
      pubsub.subscribe(module2.key, 'event', spy2);
      pubsub.publish(module1.key, 'event');

      // test it worked
      expect(spy1.callCount).to.be.equal(2);
      expect(spy2.callCount).to.be.equal(3);

      // try detaching right callback, but wrong key
      pubsub.unsubscribe(module1.key, 'event', spy2);
      pubsub.publish(module1.key, 'event');

      // it should still be there
      expect(spy1.callCount).to.be.equal(3);
      expect(spy2.callCount).to.be.equal(4);

    });

    it("error in callback will not disrupt other callbacks", function() {
      var pubsub = new PubSub({errWarningCount: 5});
      var module = _.extend({}, Backbone.Events);
      var k = pubsub.getPubSubKey();

      var spy = sinon.spy();
      var errspy = sinon.spy();

      var r = 10; // num of triggers

      pubsub.subscribe(k, pubsub.BIG_FIRE, errspy);
      _.each(_.range(r), function(element, index, list) {
        if (index === r/2) {
          pubsub.subscribe(k, 'event', function() {throw new Error('Testing error handling: ignore me');});
        }
        else {
          pubsub.subscribe(k, 'event', function() {spy.apply(spy, [index].concat(_.toArray(arguments)));});
        }
      });

      _.each(_.range(r), function(element, index, list) {
        try {
          //console.log('triggering:', index);
          pubsub.publish(k, 'event', index);
        }
        catch(e) {
          //console.log('failed for:', index, 'spy.callCount', spy.callCount);
        }
      });
      //console.log(spy.args);
      expect(spy.callCount).to.be.equal((r * r) - r);
      expect(spy.calledWith(0, (r/2)-1)).to.be.true;
      expect(spy.calledWith(0, r-1)).to.be.true;
      expect(spy.calledWith(r-1, (r/2)-1)).to.be.true;
      expect(spy.calledWith(r-1, r-1)).to.be.true;


      // errors should be counted
      expect(pubsub._errors[k.getId()]).to.be.equal(r);

      // error handler should be called
      expect(errspy.callCount).to.be.equal(r/5);
      expect(errspy.firstCall.args[0]).to.be.equal(5);
      expect(errspy.firstCall.args[1] instanceof Error).to.be.true;
      expect(errspy.firstCall.args[2]).to.have.keys(['callback', 'context', 'ctx']);
      expect(errspy.firstCall.args[2].ctx).to.equal(k);


      // now set the pubsub to ignore error handling - errors
      // should stop the queue

      pubsub = new PubSub({errWarningCount: 5, handleErrors: false});
      k = pubsub.getPubSubKey();
      errspy = sinon.spy();
      spy = sinon.spy();

      pubsub.subscribe(k, pubsub.BIG_FIRE, errspy);
      _.each(_.range(r), function(element, index, list) {
        if (index === r/2) {
          pubsub.subscribe(k, 'event', function() {throw new Error('Testing error handling: ignore me');});
        }
        else {
          pubsub.subscribe(k, 'event', function() {spy.apply(spy, [index].concat(_.toArray(arguments)));});
        }
      });
      _.each(_.range(r), function(element, index, list) {
        try {
          pubsub.publish(k, 'event', index);
        }
        catch(e) {
        }
      });
      //console.log(spy.args);
      expect(spy.callCount).to.be.equal(r/2 * r);
      expect(spy.calledWith(0, (r/2)-1)).to.be.true;
      expect(spy.calledWith(0, r-1)).to.be.true;
      expect(spy.calledWith(r-1, (r/2)-1)).to.be.false;
      expect(spy.calledWith(r-1, r-1)).to.be.false;


    });

    it("provides a hardened version of itself", function() {
      var pubsub = new PubSub();
      var all = sinon.spy();
      pubsub.on('all', all);

      var hardened = pubsub.getHardenedInstance();
      expect(hardened.__facade__).equals(true);
      expect(hardened.handleCallbackError).to.be.undefined;
      expect(hardened.start).to.be.undefined;


      expect(hardened.getCurrentPubSubKey()).to.be.instanceof(PubSubKey);
      expect(hardened.getPubSubKey()).to.be.instanceof(PubSubKey);
      expect(hardened.getPubSubKey()).to.be.not.equal(hardened.getCurrentPubSubKey());

      var p = hardened;
      var spy = sinon.spy();

      hardened.subscribe('event', spy);
      hardened.publish('event', 'one');
      hardened.unsubscribe('event');
      hardened.publish('event', 'two');

      expect(spy.callCount).to.be.equal(1);
      expect(spy.args[0].slice(0,1)).to.be.eql(['one']);

      all.reset();
      spy = sinon.spy();
      hardened.subscribe('event-foo', spy);

      // it works for events triggered from another module
      var hardenedX = pubsub.getHardenedInstance();
      hardenedX.publish('event-foo', [4,5,6]);
      expect(all.args[0].slice(0,2)).to.eql(['event-foo', [4,5,6]]);
      expect(spy.args[0].slice(0,1)).to.eql([[4,5,6]]);

      hardened.unsubscribe('wrong-event');

      hardenedX.publish('event-foo', [7]);
      expect(all.args[1].slice(0,2)).to.eql(['event-foo', [7]]);
      expect(spy.args[1].slice(0,1)).to.eql([[7]]);

      hardened.unsubscribe('event-foo');

      hardenedX.publish('event-foo', [8]);
      expect(all.callCount).to.equal(3);
      expect(spy.callCount).to.equal(2);


      // iface can be redefined
      hardened = pubsub.getHardenedInstance({start: true, destroy: true});
      expect(hardened.start).to.be.defined;
      expect(hardened.destroy).to.be.defined;
      expect(hardened.publish).to.be.defined;
      expect(hardened.subscribe).to.be.defined;

    });

    it("exposes catalogue of global events", function() {
      var pubsub = new PubSub();

      for (var ev in PubSubEvents) {
        expect(pubsub[ev]).to.be.equal(PubSubEvents[ev]);
      }

      var hardened = pubsub.getHardenedInstance();
      for (var ev in PubSubEvents) {
        expect(hardened[ev]).to.be.equal(PubSubEvents[ev]);
      }

    });

    it("undefined events are checked and thrown", function() {
      var pubsub = new PubSub();
      var hardened = pubsub.getHardenedInstance();

      expect(function() {hardened.publish(undefined, sinon.spy())}).to.throw(Error);
      expect(function() {pubsub.publish(undefined, sinon.spy())}).to.throw(Error);
    });

    it("has subscribeOnce", function() {
      var pubsub = new PubSub();
      var hardened = pubsub.getHardenedInstance();
      var hardened2 = pubsub.getHardenedInstance();

      var spy = sinon.spy();
      var spy2 = sinon.spy();

      expect(hardened.subscribeOnce).to.be.defined;
      hardened2.subscribe('event', spy2);
      pubsub.subscribeOnce(pubsub.getPubSubKey(), 'event', spy);

      // there should be no generic signal present
      expect(pubsub._events['event'].length).to.be.eql(2);
      expect(pubsub._events['event' + pubsub.getPubSubKey().getId()]).to.be.defined;

      hardened.publish('event', 'one');

      expect(spy.callCount).to.be.eql(1);
      hardened.publish('event', 'one');
      expect(spy.callCount).to.be.eql(1);
      expect(spy2.callCount).to.be.eql(2);

      // there should be no generic signal present
      expect(pubsub._events['event'].length).to.be.eql(1);
      expect(pubsub._events['event' + pubsub.getPubSubKey().getId()]).to.be.undefined;

    });

  });
});
