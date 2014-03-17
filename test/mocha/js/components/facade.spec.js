/**
 * Created by rchyla on 3/16/14.
 */

define(['js/components/facade', 'underscore'], function(Facade, _) {

  describe("Facade protection (Component)", function () {

    it("should wrap an instance protecting all of its methods", function() {
      var spy = sinon.spy();

      // The interface itself
      var interface = {
        start: 'method to start',
        stop: 'method to stop',
        valueX: 'variable copied over',
        facade: 'this should be allowed'
      };

      // An implelemntation of the interface
      var imp = {
        start: function() { spy('start', _.toArray(arguments)); return this; },
        stop: function() { spy('stop', _.toArray(arguments)); return this; },
        valueX: 'foo',
        private: 'bar',
        array: [1,2],
        object: {foo: 'boo'},
        facade: new Facade()
      };

      var facade = new Facade(interface, imp );

      expect(facade.private).to.be.undefined;
      expect(facade.valueX).to.be.undefined;
      expect(facade.getValueX()).to.be.equal('foo');
      expect(facade.start({foo: 'bar'})).to.be.eql(imp);
      expect(facade.stop({foo: 'baz'})).to.be.eql(imp);
      expect(spy.firstCall.args).to.be.eql(['start', [{foo:'bar'}]]);
      expect(spy.secondCall.args).to.be.eql(['stop', [{foo:'baz'}]]);
      expect(facade.facade.__facade__).to.be.true;

      expect(function() {new Facade({array: 'should fail'}, imp)}).to.throw(Error);
      expect(function() {new Facade({object: 'should fail'}, imp)}).to.throw(Error);
      expect(function() {new Facade({objectx: 'should fail'}, imp)}).to.throw(Error);

    });

  });
});
