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

  });
});
