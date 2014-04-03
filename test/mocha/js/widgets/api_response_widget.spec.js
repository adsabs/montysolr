/**
 * Created by rchyla on 3/19/14.
 */

define(['js/widgets/api_response/widget', 'js/components/api_response',
  'backbone', 'jquery',
  'js/components/beehive', 'js/services/pubsub'],
  function(ApiResponseWidget, ApiResponse, Backbone, $, BeeHive, PubSub) {
  describe("ApiResponse Debugging Widget (UI)", function () {

      var jsonData = {
        foo: 'bar',
        responseHeader: {
          params: {
            q: '*:*'
          },
          nested: {
            one: 'two'
          }
        }
      };

      var clearMe = function() {
        var ta = $('#test-area');
        if (ta) {
          ta.empty();
        }
      }
      beforeEach(clearMe);
      afterEach(clearMe);

      it("can returns object of ApiResponseWidget", function() {
        expect(new ApiResponseWidget()).to.be.instanceof(ApiResponseWidget);
      });

      it("should build a view of the ApiResponse values", function() {
        var W = ApiResponseWidget.extend({
          onRun: sinon.spy()
        });
        var widget = new W(new ApiResponse(jsonData));
        var w = $(widget.render());

        var ta = $('#test-area');
        ta.append(widget.render());

        // test it is there
        expect(ta.find('#api-response-input').val()).to.equal(
        '{"foo":"bar","responseHeader":{"params":{"q":"*:*"},"nested":{"one":"two"}}}');
        expect(ta.find('#api-response-result').val()).to.equal(
        'new ApiResponse({"foo":"bar","responseHeader":{"params":{"q":"*:*"},"nested":{"one":"two"}}})');

        // insert different data and click on load
        ta.find('#api-response-input').val('{"woo":"wah","responseHeader":{"params":{"q":"*:*"},"nested":{"one":"two"}}}');
        ta.find('button#api-response-load').click();
        // check the results changed
        expect(ta.find('#api-response-result').val()).to.equal(
        'new ApiResponse({"woo":"wah","responseHeader":{"params":{"q":"*:*"},"nested":{"one":"two"}}})');

        // click on run
        ta.find('button#api-response-run').click();
        expect(widget.onRun.callCount).to.be.equal(1);

        // now try loading erroneous data
        ta.find('#api-response-input').val('woo":"wah","responseHeader":{"params":{"q":"*:*"},"nested":{"one":"two"}}}');
        expect(function() {ta.find('button#api-response-load').click();}).to.throw(Error);
        // check the results are the same
        expect(ta.find('#api-response-result').val()).to.equal(
          'new ApiResponse({"woo":"wah","responseHeader":{"params":{"q":"*:*"},"nested":{"one":"two"}}})');
        // and error message is new
        expect(ta.find('#api-response-error').text()).to.not.equal('');

        // now correct string, error should go away
        ta.find('#api-response-input').val('{"woo":"wah","responseHeader":{"params":{"q":"*:*"},"nested":{"one":"two"}}}');
        ta.find('button#api-response-load').click();
        expect(ta.find('#api-response-error').text()).to.contain('');

      });

    it("knows how to interact with pubsub", function(done) {

      var beehive = new BeeHive();
      var pubsub = new PubSub();
      beehive.addService('PubSub', pubsub);

      var widget = new ApiResponseWidget(new ApiResponse(jsonData));
      widget.activate(beehive.getHardenedInstance());
      var $w = $(widget.render());

      expect($w.find('#api-response-result').val()).to.equal(
        'new ApiResponse({"foo":"bar","responseHeader":{"params":{"q":"*:*"},"nested":{"one":"two"}}})');

      // send a new response trough the pubsub, widget should catch it and display
      pubsub.trigger(pubsub.DELIVERING_RESPONSE, new ApiResponse(_.extend(jsonData, {foo: 'bazz'})));
      expect($w.find('#api-response-result').val()).to.equal(
        'new ApiResponse({"foo":"bazz","responseHeader":{"params":{"q":"*:*"},"nested":{"one":"two"}}})');

      done();
    });

  });
});
