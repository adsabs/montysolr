define(['js/components/generic_module', 'js/services/pubsub',
  'backbone'], function(GenericModule, PubSub, Backbone) {

  describe("PubSub (Service)", function () {
      
    it("should return PubSub object (generic module)", function() {
      expect(new PubSub()).to.be.an.instanceof(GenericModule);
      expect(new PubSub()).to.be.an.instanceof(PubSub);
    });

    it.skip("can register subscribers and allows them to send/receive messages", function() {

      var moduleSpy = sinon.spy();
      var pubsubSpy = sinon.spy();

      var pubsub = new PubSub();
      var module = new GenericModule();

      expect(function() {pubsub.registerModule({})}).to.throw(/We can register only instances of GenericModule/);
      pubsub.registerModule(module);


      expect(spy.called).to.be.false;

      module.listenTo(module, 'all', moduleSpy);
      pubsub.listenTo(pubsub, 'all', pubsubSpy);

      module.trigger('module-only-event', {msg: 1});
      expect(moduleSpy.calledWith('module-only-event', {msg: 1})).to.be.true;

    });

    it("has publish/subscribe/unsubscribe methods", function() {
      var pubsub = new PubSub();
      var module = new GenericModule();
      var spy1 = sinon.spy();
      var spy2 = sinon.spy();
      var stopSpy = sinon.spy(module, "stopListening");

      // subscribe to the topic (module object must be passed in)
      expect(pubsub.subscribe(module, 'search:spy1', spy1)).to.be.OK;
      expect(pubsub.subscribe(module, 'search:spy2', spy2)).to.be.OK;
      expect(function() {pubsub.subscribe('search:spy1', spy1);}).to.throw(Error);
      expect(function() {pubsub.subscribe(module, 'search:spy1');}).to.throw(Error);

      // send a message
      pubsub.publish('search:spy1', {foo: 'bar'});
      expect(spy1.getCall(0).calledWith({foo: 'bar'})).to.be.true;
      expect(spy2.called).to.be.false;

      // unsubscribe
      pubsub.unsubscribe(module, 'search:spy1');
      pubsub.publish('search:spy1', {foo: '2'});
      expect(spy1.calledWith({foo: '2'})).to.be.false;
      expect(spy2.called).to.be.false;

      // check the module is still subscribed
      pubsub.publish('search:spy2', {foo: 'bar'});
      expect(spy2.getCall(0).calledWith({foo: 'bar'})).to.be.true;

      // detach all events and check no ghosts are left behind
      pubsub.unsubscribe(module);
      pubsub._listeners[module]

      pubsub.subscribe(module, 'search:facet', spy2);
      pubsub.publish('search:spy2', {foo: 'bar'});
      pubsub.unsubscribe(module);

      // module
      module.subscribe('search:spy1', spy1);
      module.subscribe('search:spy2', spy2);
      module.unsubscribe('search:spy2');
      module.unsubscribe();
      module.publish('search:spy2', spy2);

    });

  });
});
