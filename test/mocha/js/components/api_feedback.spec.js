define([
  'underscore',
  'js/components/api_feedback'
  ], function(
  _,
  ApiFeedback) {

  describe("ApiFeedback (API)", function () {


    it("should return bare API object", function() {
      expect(new ApiFeedback()).to.be.instanceof(ApiFeedback);
      expect(new ApiFeedback().getHardenedInstance().__facade__).to.be.defined;
    });

    it("has Error codes", function() {
      var f = new ApiFeedback();
      expect(f.CODES.SERVER_ERROR).to.be.equal(503);
    });

    it("has code and msg", function() {
      var f = new ApiFeedback({code: 200, msg: 'whatever'});
      expect(f.code).to.be.equal(200);
      expect(f.msg).to.be.equal('whatever');

      expect(function() {new ApiFeedback({code: 2009, msg: 'whatever'});}).to.throw.Error;
    });

  });

});
