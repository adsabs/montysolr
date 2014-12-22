define([
    'marionette',
    'backbone',
    'js/bugutils/minimal_pubsub',
    'js/modules/orcid/widget/widget',
    'js/widgets/list_of_things/widget',
    'js/components/json_response'
  ],
  function (
    Marionette,
    Backbone,
    MinimalPubsub,
    OrcidWidget,
    ListOfThingsWidget,
    JsonResponse
    ) {

    describe("Render Results UI Widget (orcid_widget.spec.js)", function () {

      var orcidData = {
        "orcid-message": {
          "$": {
            "xmlns": "http://www.orcid.org/ns/orcid"
          },
          "message-version": "1.1",
          "orcid-profile": {
            "$": {
              "type": "user"
            },
            "orcid-identifier": {
              "$": {},
              "uri": "http://sandbox.orcid.org/0000-0001-7016-4666",
              "path": "0000-0001-7016-4666",
              "host": "sandbox.orcid.org"
            },
            "orcid-preferences": {
              "$": {},
              "locale": "en"
            },
            "orcid-history": {
              "$": {},
              "creation-method": "website",
              "submission-date": "2014-12-05T09:58:18.525Z",
              "last-modified-date": "2014-12-11T14:27:18.151Z",
              "claimed": "true"
            },
            "orcid-bio": {
              "$": {},
              "personal-details": {
                "$": {},
                "given-names": "Martin",
                "family-name": "Obrátil"
              }
            },
            "orcid-activities": {
              "$": {},
              "orcid-works": [ // BC:rca i havent found in your tests, an example of multiple orcid works; so i'm making it out
                {title: 'example 1', identifier: 'A'},
                {title: 'example 2', identifier: 'B'},
                {title: 'example 3', identifier: 'C'},
                {title: 'example 4', identifier: 'D'},
                {title: 'example 5', identifier: 'E'}
              ]
            }
          }
        }
      };
      var minsub;
      // this is useful if you want to test widget getting data from Orcid
      // it simulates pubsub response
      beforeEach(function (done) {
        minsub = new (MinimalPubsub.extend({
          request: function (apiRequest) {
            return orcidData;
          }
        }))({verbose: false});
        done();
      });

      afterEach(function (done) {
        minsub.close();
        var ta = $('#test');
        if (ta) {
          ta.empty();
        }
        done();
      });


      it("returns OrcidWidget object", function (done) {
        expect(new OrcidWidget()).to.be.instanceof(OrcidWidget);
        expect(new OrcidWidget()).to.be.instanceof(ListOfThingsWidget);
        done();
      });

      var _getWidget = function () {
        var widget = new OrcidWidget();
        widget.activate(minsub.beehive.getHardenedInstance());
        return widget;
      };

      it("should listen to DELIVERING_RESPONSE and automatically render data", function (done) {

        // mock resolution
        minsub.beehive.Services.add('OrcidApi', {
          getADSIdentifier: function(orcidId) {
            return 'ads:' + orcidId;
          },
          getHardenedInstance: function() {
            return this;
          }
        });

        var widget = _getWidget();
        widget.model.set('perPage', 2);

        expect(widget.collection.length).to.eql(0);
        minsub.publish(minsub.DELIVERING_RESPONSE, new JsonResponse(orcidData)); // can be triggered differently

        expect(widget.collection.length).to.eql(2);
        // insert the widget into page
        var $w = widget.render().$el;
        $('#test').append($w);

        // check pagination works
        expect($w.find(".pagination li").length).to.eql(3);
        expect($w.find(".pagination li:first").text().trim()).to.eql("1");
        expect($w.find(".pagination li:last").text().trim()).to.eql("3");
        expect($w.find('.s-results-title').text()).to.eql('example 1example 2');

        widget.updatePagination(({page : 2})); // it is zero-based index
        expect($w.find(".pagination li").length).to.eql(3);
        expect($w.find(".pagination li:first").text().trim()).to.eql("1");
        expect($w.find(".pagination li:last").text().trim()).to.eql("3");
        expect($w.find('.s-results-title').text()).to.eql('example 5');

        done();
      });

    });
  });