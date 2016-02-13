define([
    'marionette',
    'backbone',
    'js/widgets/base/base_widget',
    'js/components/api_response',
    'js/components/api_request',
    'js/components/api_query',
    'js/bugutils/minimal_pubsub',
    'js/widgets/green_button/widget'
  ],
  function(
           Marionette,
           Backbone,
           BaseWidget,
           ApiResponse,
           ApiRequest,
           ApiQuery,
           MinimalPubSub,
           GreenButtonWidget
    ) {

    describe("Green Button Widget (green_button_widget.spec.js)", function () {

      beforeEach(function () {
        this.minsub = new (MinimalPubSub.extend({
          request: function (apiRequest) {
            return [
              {
                application: 'prod',
                environment: 'adsws',
                version: 'v1.0.60',
                date_created: '2015-09-04T01:56:35.450686+00:00',
                date_last_modified: '2015-09-04T01:56:35.450686+00:00',
                deployed: true,
                tested: true,
                status: 'success',
                previous_version: 'v1.0.59'
              },
              {
                application: 'sandbox',
                environment: 'adsws',
                version: 'de9853dl',
                date_created: '2015-10-04T01:56:35.450686+00:00',
                date_last_modified: '2015-10-11T01:56:35.450686+00:00',
                deployed: true,
                tested: true,
                status: 'warning'
              },
              {
                application: 'prod',
                environment: 'orcid',
                version: 'v1.0.1',
                date_created: '2015-09-04T01:56:35.450686+00:00',
                date_last_modified: '2015-09-04T01:56:35.450686+00:00',
                deployed: true,
                tested: true,
                status: 'success'
              },
              {
                application: 'sandbox',
                environment: 'orcid',
                version: 'sd3434elk',
                date_created: '2015-09-04T01:56:35.450686+00:00',
                date_last_modified: '2015-09-04T01:56:35.450686+00:00',
                deployed: true,
                tested: true,
                status: 'pending',
                msg: 'Deployment running (5mins)'
              },
            ]
          }
        }))({verbose: false});
      });

      afterEach(function () {
        $("#test").empty();
      });


      it("returns API object", function() {
        expect(new GreenButtonWidget()).to.be.instanceof(BaseWidget);
      });

      it("should display stuff", function () {
        var widget = new GreenButtonWidget();
        widget.activate(this.minsub.beehive.getHardenedInstance()); // this is normally done by application
        var $w = widget.render().$el;
        $('#test').append($w);
        expect($('tbody>tr>td:first a:first').text().trim()).to.eql('adsws (v1.0.60)');
        expect($('tbody>tr:last>td:last a:first').text().trim()).to.eql('orcid (sd3434elk)');
      });

      it("knows to listen to the pubsub", function() {
        var widget = new GreenButtonWidget();
        widget.activate(this.minsub.beehive.getHardenedInstance()); // this is normally done by application

        var $w = widget.render().$el;
        $('#scratch').append($w);

      });


    })
  });