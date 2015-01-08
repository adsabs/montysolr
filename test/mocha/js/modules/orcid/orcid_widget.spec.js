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
      var minsub, beehive, notifier;
      // this is useful if you want to test widget getting data from Orcid
      // it simulates pubsub response
      beforeEach(function (done) {
        minsub = new (MinimalPubsub.extend({

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
          getHardenedInstance: function() {
            return this;
          }
        });

        var widget = _getWidget();
        widget.model.set('perPage', 2);

        expect(widget.collection.length).to.eql(0);

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

      it('should trigger proper action', function(done){
        var widget = new _getWidget();

        var $renderResult = $(widget.render().el);
        $("#test").append($renderResult);

        // trigger event of user profile refreshment
        minsub.publish(minsub.ORCID_ANNOUNCEMENT, {
          msgType: OrcidApiConstants.Events.UserProfileRefreshed,
          data: getUserProfileJson()
        });


        var $actionList = $renderResult.find('.orcid-actions ul').first().find('a.orcid-action');

        var deleteTriggered = false;
        var updateTriggered = false;

        minsub.subscribe(minsub.ORCID_ANNOUNCEMENT, function(msg){
          var data = msg.data;
          if (data.actionType == 'update'){
            updateTriggered = true;
          }
          else if (data.actionType == 'delete') {
            deleteTriggered = true;
          }
        });

        expect($actionList.length > 0).to.be.true;

        _.each($actionList, function($action){
          eventFire($action, 'click');
        });

        expect(updateTriggered).to.be.true;
        expect(deleteTriggered).to.be.true;

        done();

      });


      function eventFire(el, etype){
        if (el.fireEvent) {
          el.fireEvent('on' + etype);
        } else {
          var evObj = document.createEvent('Events');
          evObj.initEvent(etype, true, false);
          el.dispatchEvent(evObj);
        }
      }

    });
  });