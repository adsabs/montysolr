define([
    'js/components/generic_module',
    'js/services/localStorage'
  ],
  function (
    GenericModule,
   LocalStorage
  ) {
    describe("LocalStorage", function () {
      it('input to spec is not undefined', function(done){

        expect(LocalStorage!= undefined).to.be.true

        done();

      });

      it('should be GenericModule', function (done) {
        expect(new LocalStorage()).to.be.an.instanceof(GenericModule);
        expect(new LocalStorage()).to.be.an.instanceof(LocalStorage);
        done();
      });

      it('should pass all functions', function(done){

        var localStorage = new LocalStorage();

        localStorage.setObject('keyKey', {value:'value'});

        expect(localStorage.getObject('keyKey').value == 'value').to.be.true;

        localStorage.setValue('valueKey', 3);

        expect(localStorage.getValue('valueKey') == 3).to.be.true;

        done();
      });

    });
  });