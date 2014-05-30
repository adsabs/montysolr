/**
 * Created by rchyla on 3/19/14.
 */

define(['js/widgets/api_request/widget', 'js/components/api_request', 'js/components/api_query', 'js/services/pubsub', 'js/components/beehive', 'backbone', 'jquery'],
  function (ApiRequestWidget, ApiRequest, ApiQuery, PubSub, BeeHive, Backbone, $) {
    describe("ApiRequest Debugging Widget (UI)", function () {

      var clearMe = function (done) {
        var ta = $('#test-area');
        if (ta) {
          ta.empty();
        }
        done();
      };
      beforeEach(clearMe);
      afterEach(clearMe);

      it("can returns object of ApiRequestWidget", function (done) {
        expect(new ApiRequestWidget()).to.be.instanceof(ApiRequestWidget);
        done();
      });

      it("should build a view of the ApiRequest values", function (done) {
        var req = new ApiRequest().load('foo?q=one#sender=boo&sender=woo');
        var widget = new ApiRequestWidget(req);
        var $ta = $('#test-area');
        $ta.append(widget.render());

        // widget is initialized with the query
        expect($ta.find('#api-request-input').val()).to.equal("foo?q=one#sender=boo&sender=woo");
        expect($ta.find('tr:nth-child(1) >> input[name=value]').val()).to.equal('foo');
        expect($ta.find('tr:nth-child(2) >> input[name=value]').val()).to.equal('q=one');
        expect($ta.find('tr:nth-child(3) >> input[name=value]').val()).to.equal('boo|woo');
        done();
      });

      it("has interactive features: load/modify/run", function (done) {
        var req = new ApiRequest().load('foo?q=one#sender=boo&sender=woo');
        var widget = new ApiRequestWidget(req);
        var $ta = $('#test-area');
        $ta.append(widget.render());

        // edit the query
        $ta.find('tr:nth-child(2) >> input[name=value]').focus().val('q=two').blur();
        expect(widget.collection.models[1].attributes).to.eql({key: 'query', value: 'q=two'});

        // click on the 'run'
        $ta.find('#api-request-run').click();
        // it should generate the serialized version
        expect($ta.find('#api-request-result').text()).to.equal("foo?q=two#sender=boo&sender=woo");

        // and the whole thing can be reloaded
        $ta.find('#api-request-input').val('foo/bar?q=three#sender=booz');
        // click on 'load'
        $ta.find('#api-request-load').click();
        // should have new values
        expect($ta.find('tr:nth-child(1) >> input[name=value]').val()).to.equal('foo/bar');
        expect($ta.find('tr:nth-child(2) >> input[name=value]').val()).to.equal('q=three');
        expect($ta.find('tr:nth-child(3) >> input[name=value]').val()).to.equal('booz');


        $ta.find('#api-request-run').click();
        // result will be updated
        expect($ta.find('#api-request-result').text()).to.equal("foo/bar?q=three#sender=booz");
        // also input will
        expect($ta.find('#api-request-input').val()).to.equal("foo/bar?q=three#sender=booz");
        done();
      });

      it("knows how to interact with pubsub", function (done) {

        var beehive = new BeeHive();
        var pubsub = new PubSub();
        beehive.addService('PubSub', pubsub);

        var req = new ApiRequest().load('foo?q=one#sender=boo&sender=woo');
        var widget = new ApiRequestWidget(req);
        var $ta = $('#test-area');
        $ta.append(widget.render());

        widget.activate(beehive.getHardenedInstance());
        var $w = $(widget.render());

        expect($w.find('#api-request-input').val()).to.equal("foo?q=one#sender=boo&sender=woo");

        // send a new response trough the pubsub, widget should catch it and display
        pubsub.trigger(pubsub.DELIVERING_REQUEST, new ApiRequest().load('search?q=one#sender=external'));

        expect($w.find('#api-request-result').text()).to.equal("search?q=one#sender=external");

        done();
      });

    });
  });
