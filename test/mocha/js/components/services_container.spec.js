/**
 * Created by rchyla on 3/31/14.
 */

define(['js/components/services_container', 'js/components/generic_module'],
  function(ServicesContainer, GenericModule) {

  describe("ServicesContainer (Scaffolding)", function() {
    it("should return API object", function() {
      expect(new ServicesContainer()).to.be.instanceof(ServicesContainer);
    });

    it("has methods to manipulate services", function() {
      var sc = new ServicesContainer();
      var test = new GenericModule();
      sinon.stub(test, 'activate');
      sinon.stub(test, 'close');

      expect(sc.add('test', test)).to.be.OK;
      expect(sc.has('test')).to.be.true;
      expect(sc.get('test')).to.be.equal(test);

      expect(sc.getAll()).to.be.eql([['test', test]]);

      expect(sc.remove('test')).to.be.OK;
      expect(sc.has('test')).to.be.false;

      expect(test.close.callCount).to.be.equal(1);
    });

    it("knows how to create unique, write-protected version of itself", function() {
      var sc = new ServicesContainer();
      var test = new GenericModule();
      var M = GenericModule.extend({
        getHardenedInstance: function() {
          return this;
        }
      });
      var test2 = new M();

      sc.add('test', test);
      sc.add('hardened', test2);

      var hardened = sc.getHardenedInstance();

      expect(hardened.__facade__).to.be.OK;
      expect(hardened.get('hardened')).to.be.equal(test2);
      expect(hardened.has('test')).to.be.false;
      expect(hardened.set).to.be.undefined;
      

    });

  });
});
