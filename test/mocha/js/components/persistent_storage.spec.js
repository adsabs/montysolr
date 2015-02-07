define([
    'js/components/generic_module',
    'js/components/persistent_storage'
  ],
  function (
    GenericModule,
    PersistentStorage
    ) {
    describe("Persistent Storage (persistent_storage.spec.js)", function () {

      it('should be GenericModule', function (done) {
        expect(new PersistentStorage()).to.be.an.instanceof(GenericModule);
        expect(new PersistentStorage()).to.be.an.instanceof(PersistentStorage);
        done();
      });

      it('has namespace', function(){

        var s = new PersistentStorage();
        expect(s._store.name).to.equal('bumblebee');

        s = new PersistentStorage({name: 'foo'});
        expect(s._store.name).to.equal('bumblebeefoo');
      });

      it("has set/remove/get/clear methods", function() {
        var s = new PersistentStorage();
        s.set('foo', 'bar');
        expect(s.get('foo')).to.eql('bar');

        s.set('foo', ['bar']);
        expect(s.get('foo')).to.eql(['bar']);

        s.set('foo', {bar: 1});
        expect(s.get('foo')).to.eql({bar: 1});
        expect(s.keys()).to.eql({'foo': 1});

        s.remove('foo');
        expect(s.get('foo')).to.eql(null);
        expect(s.keys()).to.eql({});


        s.set('foo', 1);
        s.clear();
        expect(s.keys()).to.eql({});
      });

    });
  });