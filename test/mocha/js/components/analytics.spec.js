
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

      /**
       * the async loading is ridiculously slow (ie. times out after 15s)
       * in chrome
       */
      this.timeout(50000);

      require(['js/components/analytics'], function(analytics) {

        expect(analytics.promise).to.be.defined;
        analytics.promise.fail(function() {

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

          for (var i=0; i<100; i++) {
            a(i);
          }
          expect(window[window.GoogleAnalyticsObject].q.length).to.eql(53);
          done();
        });

      });

    });

  });
});
