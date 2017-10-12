define([
  'underscore',
  'js/modules/orcid/profile'
], function (_, Profile) {

  describe('Orcid Profile', function () {

    describe('Instantiation', function () {
      it('instantiates correctly', function () {
        expect(new Profile() instanceof Profile).to.equal(true);
      });
    });
  });
});