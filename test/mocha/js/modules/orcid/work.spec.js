define([
  'underscore',
  'js/modules/orcid/work'
], function (_, Work) {

  describe('Orcid Work', function () {

    describe('Instantiation', function () {
      it('instantiates correctly', function () {
        expect(new Work({}) instanceof Work).to.equal(true);
      });
    });
  });
});
