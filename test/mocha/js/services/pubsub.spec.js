define(['js/components/generic_module', 'js/services/pubsub', 'js/components/pubsub_key', 'js/components/facade',
  'backbone'], function(GenericModule, PubSub, PubSubKey, Facade, Backbone) {

  describe("PubSub - default implementation (Service)", function () {
      
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
      p.publish(k, 'event');
      p.unsubscribe(k, 'event');
      p.publish(k, 'event');

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
      p.publish(k, 'event');
      expect(spy.callCount).to.be.equal(1);

      expect(function() {p.subscribe(k2, 'event', spy)}).to.throw(Error);

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

      pubsub.subscribe(k, 'pubsub.many_errors', errspy);
      _.each(_.range(r), function(element, index, list) {
        if (index === r/2) {
          pubsub.subscribe(k, 'event', function() {throw new Error('foo');});
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

      pubsub.subscribe(k, 'pubsub.many_errors', errspy);
      _.each(_.range(r), function(element, index, list) {
        if (index === r/2) {
          pubsub.subscribe(k, 'event', function() {throw new Error('foo');});
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
      var hardened = pubsub.getHardenedInstance();
      expect(hardened.__facade__).equals(true);
      expect(hardened.handleCallbackError).to.be.undefined;
      expect(hardened.start).to.be.undefined;

      var p = hardened;
      var k = p.getPubSubKey();
      var spy = sinon.spy();

      p.subscribe(k, 'event', spy);
      p.publish(k, 'event');
      p.unsubscribe(k, 'event');
      p.publish(k, 'event');

      expect(spy.callCount).to.be.equal(1);

      hardened = pubsub.getHardenedInstance({start: true, close: true});
      expect(hardened.start).to.not.be.undefined;
      expect(hardened.close).to.not.be.undefined;

    });

    it("exposes catalogue of global events", function() {
      var pubsub = new PubSub();
      expect(pubsub.NEW_QUERY).to.be.not.undefined;
      expect(pubsub.NEW_RESPONSE).to.be.not.undefined;
    });


  });
});
