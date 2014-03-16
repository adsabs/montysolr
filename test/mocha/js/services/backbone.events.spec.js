define(['backbone', 'underscore'], function(Backbone, _) {

  describe("BackBone.Events - test of problems (this test shows what we have to do to build robust PubSub)", function () {

    it("shows that BB events are synchronous and one bad egg can break the whole batch", function() {
      var pubsub = _.extend({}, Backbone.Events);
      var module = _.extend({}, Backbone.Events);
      module._xid = 'module';
      pubsub._xid = 'pubsub';

      var spy = sinon.spy();
      var r = 10; // num of triggers

      _.each(_.range(r), function(element, index, list) {
        if (index === r/2) {
          pubsub.on('event', function() {throw new Error('foo');});
        }
        else {
          pubsub.on('event', function() {spy.apply(spy, [index].concat(_.toArray(arguments)));});
        }
      });

      _.each(_.range(r), function(element, index, list) {
        try {
          //console.log('triggering:', index);
          pubsub.trigger('event', index);
        }
        catch(e) {
          //console.log('failed for:', index, 'spy.callCount', spy.callCount);
        }
      });
      //console.log(spy.args);
      expect(spy.callCount).to.be.equal(r/2 * r);
      expect(spy.calledWith(0, (r/2)-1)).to.be.true;
      expect(spy.calledWith(0, r-1)).to.be.true;
      expect(spy.calledWith(r-1, (r/2)-1)).to.be.false;
      expect(spy.calledWith(r-1, r-1)).to.be.false;

    });

    it("BB will not remove anonymous functions - and misbehaving modules could easily create them!", function() {
      var pubsub = _.extend({}, Backbone.Events);
      var module = _.extend({}, Backbone.Events);
      module._xid = 'will-survive';
      pubsub._xid = 'pubsub';

      var r = 3; // num of listeners/triggers
      var spy1 = sinon.spy();
      var spy2 = sinon.spy();

      // this shows that anonymous functions are not removed; but it is understandable
      // because each of the anonymous function points to a different place in memory
      // thus the test func === func fails; however it is NOT desirable for us; because
      // these functions can contain references to other variables and thus if they
      // are not removed by BB, we'll be leaking objects - EVEN IF we follow best
      // practices
      _.each(_.range(r), function(element, index, list) {
        module.listenTo(pubsub, 'event-foo', function() {spy1.call(spy1, arguments)});
      });
      _.each(_.range(r), function(element, index, list) {
        pubsub.trigger('event-foo', {id: element});
      });
      pubsub.trigger('event-foo');
      module.stopListening(pubsub, 'event-foo', function() {spy1.call(spy1, arguments)});

      var called = spy1.callCount;

      pubsub.trigger('event-foo');
      expect(spy1.callCount).to.be.above(called);

      // this should work - remove all callbacks by their name; however for us we need
      // to remove all callbacks *OF THIS MODULE ONLY*
      called = spy1.callCount;
      module.stopListening(pubsub, 'event-foo');
      pubsub.trigger('event-foo');
      expect(spy1.callCount).to.be.equal(called);
    });


  it("BB will remove anonymous functions when they have same identity", function() {
    var pubsub = _.extend({}, Backbone.Events);
    var module = _.extend({}, Backbone.Events);
    module._xid = 'will-survive';
    pubsub._xid = 'pubsub';

    var r = 3; // num of listeners/triggers
    var spy1 = sinon.spy();
    var spy2 = sinon.spy();
    var funcs = [];

    _.each(_.range(r), function(element, index, list) {
      funcs.push(function() {spy1.call(spy1, arguments)});
    });

    // this shows that anonymous functions are not removed; but it is understandable
    // because each of the anonymous function points to a different place in memory
    // thus the test func === func fails; however it is NOT desirable for us; because
    // these functions can contain references to other variables and thus if they
    // are not removed by BB, we'll be leaking objects - EVEN IF we follow best
    // practices
    _.each(_.range(r), function(element, index, list) {
      module.listenTo(pubsub, 'event-foo', funcs[index]);
    });
    _.each(_.range(r), function(element, index, list) {
      pubsub.trigger('event-foo', {id: element});
    });
    pubsub.trigger('event-foo');
    _.each(_.range(r), function(element, index, list) {
      module.stopListening(pubsub, 'event-foo', funcs[index]);
    });

    var called = spy1.callCount;

    pubsub.trigger('event-foo');
    expect(spy1.callCount).to.be.equal(called);

  });


  it("BB will remove anonymous functions when they are saved - but not when context changed", function() {
    // BTW: we cannot use listenTo() and stopListening() because context is there always
    // the same (we cannot change context)

    var pubsub = _.extend({}, Backbone.Events);
    var module = _.extend({}, Backbone.Events);
    module._xid = 'will-survive';
    pubsub._xid = 'pubsub';

    var r = 3; // num of listeners/triggers
    var spy1 = sinon.spy();
    var funcs = [];

    // pass context when registering callback, but pass NO context when unsubscribing
    // it works!
    _.each(_.range(r), function(element, index, list) {
      funcs.push(function() {spy1.call(spy1, arguments)});
    });
    _.each(_.range(r), function(element, index, list) {
      pubsub.on('event-foo', funcs[index], {context: 'c' + element});
    });
    _.each(_.range(r), function(element, index, list) {
      pubsub.trigger('event-foo', {id: element});
    });
    pubsub.trigger('event-foo');
    _.each(_.range(r), function(element, index, list) {
      pubsub.off('event-foo', funcs[index]);
    });

    var called = spy1.callCount;

    pubsub.trigger('event-foo');
    expect(spy1.callCount).to.be.equal(called);


    // pass a different context object each time (even if it is the same one all the time)
    // it fails (!!!) - so we can use context to differentiate between module (if we can
    // guarantee that modules are passing the same context all the time) and we use
    // _.bind() to provide context to the callback
    spy = sinon.spy();

    _.each(_.range(r), function(element, index, list) {
      funcs.push(function() {spy1.call(spy1, arguments)});
    });
    _.each(_.range(r), function(element, index, list) {
      pubsub.on('event-foo', funcs[index], {context: 'c' + element});
    });
    _.each(_.range(r), function(element, index, list) {
      pubsub.trigger('event-foo', {id: element});
    });
    pubsub.trigger('event-foo');
    _.each(_.range(r), function(element, index, list) {
      pubsub.off('event-foo', funcs[index], {context: 'c' + element});
    });

    var called = spy1.callCount;

    pubsub.trigger('event-foo');
    expect(spy1.callCount).to.not.be.equal(called);

  });


  it("The only workable solution for a really robust and secure PubSub", function() {
    // is to keep context - and make sure only callbacks with this context can be
    // manipulated by the caller
    var pubsub = _.extend({}, Backbone.Events);
    var module1 = _.extend({}, Backbone.Events);
    var module2 = _.extend({}, Backbone.Events);
    var context1 = {};
    var context2 = {};
    var spy1 = sinon.spy();
    var spy2 = sinon.spy();

    module1._xid = 'module1';
    module2._xid = 'module2';
    pubsub._xid = 'pubsub';


    // we must make sure each module has unique context
    pubsub.on('event', spy1, context1);
    pubsub.on('event', spy2, context2);
    pubsub.trigger('event');

    // they will be called with the context
    expect(spy1.callCount).to.be.equal(1);
    expect(spy2.callCount).to.be.equal(1);

    // to remove only events of this module - pass its context
    pubsub.off('event', null, context1);
    pubsub.trigger('event');

    // test it worked
    expect(spy1.callCount).to.be.equal(1);
    expect(spy2.callCount).to.be.equal(2);

    // now unsubscribe all callbacks
    pubsub.off('event');
    pubsub.trigger('event');

    // test it worked
    expect(spy1.callCount).to.be.equal(1);
    expect(spy2.callCount).to.be.equal(2);

    // now put them back
    pubsub.on('event', spy1, context1);
    pubsub.on('event', spy2, context2);
    pubsub.trigger('event');

    // test it worked
    expect(spy1.callCount).to.be.equal(2);
    expect(spy2.callCount).to.be.equal(3);

    // try detaching right callback, but wrong context
    pubsub.off('event', spy2, {});
    pubsub.trigger('event');

    // it should still be there
    expect(spy1.callCount).to.be.equal(3);
    expect(spy2.callCount).to.be.equal(4);

    // the only difficulty is that subscribers cannot
    // use context; but they can use _.bind() to
    // accomplish the same - we'll have to warn them
    var s1 = sinon.spy();
    var s2 = sinon.spy();
    var s3 = sinon.spy();

    var f1 = _.bind(function() {s1.call(s1, this.foo, arguments)}, {foo: 'bar'});
    var f2 = _.bind(function() {s2.call(s2, this.foo, arguments)}, {foo: 'buzz'});
    var f3 = function(){s3.call(s3, this.foo, arguments)};

    pubsub.on('foo', f1, context1);
    pubsub.on('foo', f2, context2);
    pubsub.trigger('foo', {hey: 'Joe!'});

    // test it worked
    expect(s1.calledWith('bar', [{hey: 'Joe!'}]));
    expect(s2.calledWith('buzz', [{hey: 'Joe!'}]));
    expect(s3.calledWith(undefined, [{hey: 'Joe!'}]));


    // but we'll have to add a special method to remove subscriber
    // and remove all of its callbacks (even if they were registered as
    // anonymous functions and cannot be identified anymore
    expect(pubsub._events).to.not.be.eql({});
    pubsub.off();
    expect(pubsub._events).to.be.eql({});

    var spy = sinon.spy();
    var spy2 = sinon.spy();


    _.each(_.range(10), function(element, index, list) {
      pubsub.on('event' + element, function() {spy.call(spy, arguments)}, context1);
      pubsub.on('event' + element, function() {spy2.call(spy2, arguments)}, context2);
    });
    _.each(_.range(10), function(element, index, list) {
      pubsub.trigger('event' + element);

    });
    expect(spy.callCount).to.be.equal(10);
    expect(spy2.callCount).to.be.equal(10);

    // remove them (doesn't work - removes all of them)
    // pubsub.off(null, null, context1);

    var context = context1;
    var names = _.keys(pubsub._events), name, events,ev, i, l, k, j;
    for (i = 0, l = names.length; i < l; i++) {
      name = names[i];
      if (events = pubsub._events[name]) {
        if (context) {
          var toRemove = [];
          for (j = 0, k = events.length; j < k; j++) {
            ev = events[j];
            if (ev.context === context) {
              toRemove.push(ev);
            }
          }
          for (ev in toRemove) {
            pubsub.off(name, ev.callback, context);
          }
        }
      }
    }



    _.each(_.range(10), function(element, index, list) {
      pubsub.trigger('event' + element);
    });
    expect(spy.callCount).to.be.equal(10);
    expect(spy2.callCount).to.be.equal(20);

  })

});
});
