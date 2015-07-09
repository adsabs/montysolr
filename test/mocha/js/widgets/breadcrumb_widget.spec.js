/**
 * Created by rchyla on 5/14/14.
 */

define([
    'js/widgets/breadcrumb/widget',
    'js/components/api_query',
    'js/services/pubsub',
    'js/components/beehive',
    'backbone',
    'jquery'
  ],
  function (
    BreadCrumbWidget,
    ApiQuery,
    PubSub,
    BeeHive,
    Backbone,
    $) {
    describe("BreadCrumb Widget (UI)", function () {

      var beehive, pubsub;

      beforeEach(function (done) {
        var ta = $('#test-area');
        if (ta) {
          ta.empty();
        }
        beehive = new BeeHive();
        pubsub = new PubSub();
        beehive.addService('PubSub', pubsub);
        beehive.activate();
        done();
      });

      it("returns BreadCrumb object", function () {
        expect(new BreadCrumbWidget()).to.be.instanceof(BreadCrumbWidget);
      });

      it("when created, it displays nothing", function () {
        var widget = new BreadCrumbWidget();
        widget.activate(beehive.getHardenedInstance());
        var $w = $(widget.render());

        expect($w.find('#simple-breadcrumb').length).to.be.equal(1);
        expect($w.find('#simple-breadcrumb').text().trim()).to.be.equal("");
      });


      it("knows how to interact with pubsub", function (done) {

        var widget = new BreadCrumbWidget();
        widget.activate(beehive.getHardenedInstance());
        var $w = $(widget.render());

        // send a new response trough the pubsub, widget should catch it and display
        pubsub.trigger(pubsub.START_SEARCH, new ApiQuery({foo: 'bar'}));

        //expect($w.find('#api-query-input').val()).to.equal("foo=bar");

        done();
      });

    });
  });
