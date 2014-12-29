define([
    'js/mixins/papers_utils'
  ],
  function(
    PapersUtils
    ){

    describe("Papers Utils Mixin (mixins/papers_utils.spec.js)", function(){

      it("should know to format dates", function() {
        expect(PapersUtils.formatDate('2011-01-01T00:00:00Z', {format: 'mm/yy'})).to.be.eql('01/2011');

        expect(PapersUtils.formatDate('2011-12-01', {format: 'mm/yy', missing: {day: 'mm/yy', month: '--/yy'}})).to.be.eql('12/2011');
        expect(PapersUtils.formatDate('2011-12-00', {format: 'mm/yy', missing: {day: 'mm/yy', month: '--/yy'}})).to.be.eql('12/2011');
        expect(PapersUtils.formatDate('2011-00-00', {format: 'mm/yy', missing: {day: 'mm/yy', month: '--/yy'}})).to.be.eql('--/2011');

        expect(function() {PapersUtils.formatDate('2011-12-0x', {format: 'mm/yy', missing: {day: 'mm/yy', month: '--/yy'}})}).to.throw.Exception;
      });
    })
});