define([
    'marionette',
    'backbone',
    'jquery',
    'xml2json',
    'js/bugutils/minimal_pubsub',
    'js/modules/orcid/widget/widget',
    'js/widgets/list_of_things/widget',
    'js/components/json_response',
    'js/modules/orcid/orcid_model_notifier/module',
    'js/modules/orcid/orcid_api_constants',
    '../../widgets/test_orcid_data/orcid_profile_data'
  ],
  function (
    Marionette,
    Backbone,
    $,
    xml2json,
    MinimalPubsub,
    OrcidWidget,
    ListOfThingsWidget,
    JsonResponse,
    OrcidNotifierModule,
    OrcidApiConstants,
    TestOrcidProfileData
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
      var minsub, beehive, notifier;
      // this is useful if you want to test widget getting data from Orcid
      // it simulates pubsub response
      beforeEach(function (done) {
        minsub = new (MinimalPubsub.extend({
          request: function (apiRequest) {
            return orcidData;
          }
        }))({verbose: false});

        beehive = minsub.beehive;

        notifier = new OrcidNotifierModule();
        notifier.activate(beehive);
        notifier.initialize();

        beehive.addService('OrcidModelNotifier', notifier);

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

      var getUserProfileJson = function () {
        return $.xml2json(TestOrcidProfileData)['orcid-message']['orcid-profile'];
      };

      it("should listen to ORCID_ANNOUNCEMENT UserProfileRefreshed and automatically render data", function (done) {

        // mock resolution
        minsub.beehive.Services.add('OrcidApi', {
          //getADSIdentifier: function(orcidId) {
          //  return 'ads:' + orcidId;
          //},
          getHardenedInstance: function() {
            return this;
          }
        });

        var widget = _getWidget();
        widget.model.set('perPage', 2);

        expect(widget.collection.length).to.eql(0);
        //minsub.publish(minsub.ORCID_ANNOUNCEMENT, {data:orcidData}); // can be triggered differently

        minsub.publish(minsub.ORCID_ANNOUNCEMENT, {
          msgType: OrcidApiConstants.Events.UserProfileRefreshed,
          data: getUserProfileJson()
        });

        expect(widget.collection.length).to.eql(2);
        // insert the widget into page
        var $w = widget.render().$el;
        $('#test').append($w);

        // check pagination works
        expect($w.find(".pagination li").length).to.eql(4);
        expect($w.find(".pagination li:first").text().trim()).to.eql("1");
        expect($w.find(".pagination li:last").text().trim()).to.eql("4");
        expect($w.find('.s-results-title').text()).to.eql('Near-infrared multispectral images for Gassendi crater: mineralogical and geological inferences.itions on Efficiency of Heller Dry Cooling TowerEpidemic spreading on complex networks with overlapping and non-overlapping community structure');

        widget.updatePagination(({page : 2})); // it is zero-based index
        expect($w.find(".pagination li").length).to.eql(4);
        expect($w.find(".pagination li:first").text().trim()).to.eql("1");
        expect($w.find(".pagination li:last").text().trim()).to.eql("4");
        expect($w.find('.s-results-title').text()).to.eql('Powo·lanie i przeznaczenie : wspomnienia oficera Komendy Glównej AKKto ratuje jedno zycie-- : polacy i zydzi, 1939-1945');

        done();
      });

    });
  });