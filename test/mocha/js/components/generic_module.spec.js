define(['js/components/generic_module', 'backbone'], function(GenericModule, Backbone) {
  describe("Generic Module (scaffolding)", function () {
      


    it("should return simple generic module with extend method", function() {
      expect(new GenericModule()).to.be.an.instanceof(GenericModule);

      var C = GenericModule.extend({
        className: 'foo'
      });

      expect(new C().className).to.be.equal('foo');

      var D = GenericModule.extend({
        initialize: function() {
          this.className = 'bar';
        }
      });

      expect(new D().className).to.be.equal('bar');
    });

    it("has Backbone.Events abilities", function() {
      var spy = sinon.spy();
      var module = new GenericModule();

      expect(spy.called).to.be.false;
      expect(module.on('foo', spy)).to.be.OK;
      expect(module.trigger('foo', 42)).to.be.OK;
      expect(spy.called).to.be.true;
      expect(spy.calledWith(42)).to.be.true;

      expect(module.on).to.be.OK;
      expect(module.off).to.be.OK;
      expect(module.bind).to.be.OK;
      expect(module.unbind).to.be.OK;
      expect(module.once).to.be.OK;
      expect(module.listenTo).to.be.OK;
      expect(module.stopListening).to.be.OK;

    });

    it("should have activate()/triggerPubSub()/isRegistered() methods", function() {
      var module = new GenericModule();
      var spy = sinon.spy();

      // default impl
      expect(function() {module.triggerPubSub('foo')}).to.throw(Error);
      expect(module.isRegistered()).to.be.false;

      // when module is activated (the method is ready)
      module.activate({triggerPubSub: function() {spy.apply(spy, arguments)}});

      expect(spy.called).to.be.false;
      module.triggerPubSub('foo', 42);
      expect(spy.called).to.be.true;
      expect(spy.calledWith('foo', 42)).to.be.true;

      module.activate({triggerPubSub: function() {spy.call(spy, this)}});
      module.triggerPubSub('baaaaar');
      expect(spy.calledWith(module)).to.be.true;

    });

  });
});
