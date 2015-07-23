
define([
  'require',
  'underscore'
], function(
  require,
  _
  ) {

  describe("Analytics (analytics.spec.js)", function () {

    beforeEach(function (done) {
      // squire can inject mock module, but it can't help us simulate failure
      // loading the module; so i'm resorting to hacking global requirejs
      this.oldGa = requirejs.s.contexts._.config.paths['google-analytics'];
      requirejs.s.contexts._.config.paths['google-analytics'] = 'foooxx';
      done();
    });

    afterEach(function (done) {
      requirejs.s.contexts._.config.paths['google-analytics'] = this.oldGa;
      done();
    });

    it("should handle load errors caused by Ghostery", function(done) {

      require(['js/components/analytics'], function(analytics) {
        setTimeout(function() {
          expect(analytics.loaded).to.eql(false);

          expect(window.GoogleAnalyticsObject).to.be.defined;
          expect(window.GoogleAnalyticsObject).to.eql('ga');
          expect(window[window.GoogleAnalyticsObject]).to.be.defined;
          expect(window[window.GoogleAnalyticsObject].q[0][0]).to.eql('create');

          // but the logging should still work (and do nothing)
          analytics('foo');
          expect(window[window.GoogleAnalyticsObject].q[2][0]).to.eql('foo');

          var a = require('js/components/analytics');
          a('bar');
          expect(window[window.GoogleAnalyticsObject].q[3][0]).to.eql('bar');
          done();
        }, 500);

      });

    });

  });
});
