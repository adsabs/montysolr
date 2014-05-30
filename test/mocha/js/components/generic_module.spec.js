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

    it("should have activate() method", function() {

      var spy = sinon.spy();
      var module = new GenericModule({activate: spy});
      module.activate('foo');
      expect(spy.calledWith('foo')).to.be.true;

    });

  });
});
