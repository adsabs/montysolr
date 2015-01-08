define([
    'marionette',
    'jquery',
    'xml2json',
    'backbone',
    'js/bugutils/minimal_pubsub',
    'js/widgets/results/widget',
    'js/modules/orcid/orcid_result_row_extension/extension',
    'js/modules/orcid/orcid_model_notifier/module',
    'js/modules/orcid/orcid_api_constants',
    'js/components/api_query',
    './test_json/test1',
    './test_json/test2',
    './test_orcid_data/orcid_profile_data'
  ],
  function (Marionette,
            $,
            xml2json,
            Backbone,
            MinimalPubsub,
            ResultsWidget,
            OrcidResultRowExtension,
            OrcidNotifierModule,
            OrcidApiConstants,
            ApiQuery,
            Test1,
            Test2,
            TestOrcidProfileData) {

    describe('Orcid extension of results widget', function () {

      var minsub, beehive, notifier;
      beforeEach(function (done) {

        minsub = new (MinimalPubsub.extend({
          request: function (apiRequest) {
            if (this.requestCounter % 2 === 0) {
              return Test2();
            } else {
              return Test1();
            }
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

      it('should have OrcidResultRowExtension', function (done) {
        expect(new ResultsWidget()).to.be.instanceof(ResultsWidget);

        var widget = new ResultsWidget();
        expect(widget.activateResultsExtension).to.be.ok;
        expect(widget.onAllInternalEvents_results).to.be.ok;
        expect(widget.routeOrcidPubSub_results).to.be.ok;


        done();
      });

      var _getWidget = function () {
        var widget = new ResultsWidget();
        widget.activate(beehive.getHardenedInstance());

        return widget;
      };

      var getUserProfileJson = function () {
        return $.xml2json(TestOrcidProfileData)['orcid-message']['orcid-profile'];
      };


      it('should display and hide orcid icon with proper set of actions - insert or update/delete', function (done) {
        var widget = new _getWidget();

        var $renderResult = widget.render().$el;
        $("#test").append($renderResult);

        minsub.publish(minsub.START_SEARCH, new ApiQuery({'q': 'foo:bar'}));


        var $allOrcidActions = $renderResult.find('.orcid-actions');
        var $hiddenOrcidActions = $renderResult.find('.orcid-actions.hidden');

        // all of them are hidden
        expect($allOrcidActions.length).to.be.equal($hiddenOrcidActions.length);

        // trigger event of user profile refreshment
        minsub.publish(minsub.ORCID_ANNOUNCEMENT, {
          msgType: OrcidApiConstants.Events.UserProfileRefreshed,
          data: getUserProfileJson()
        });

        // every icon now should be visible
        $hiddenOrcidActions = $renderResult.find('.orcid-actions.hidden');
        expect($hiddenOrcidActions.length).to.be.equal(0);

        // and one should be green - this is based on id 3557618 in mock data
        var $greenActionIcon = $renderResult.find('i.mini-orcid-icon.green');
        expect($greenActionIcon.length).to.be.equal(1);


        // and just update and delete should be visible, insert should be hidden
        var $buttonsContainer = $greenActionIcon.parentsUntil('.s-results-links').last();
        expect($buttonsContainer.find('.orcid-action-insert').hasClass('hidden')).to.be.true;
        expect($buttonsContainer.find('.orcid-action-update').hasClass('hidden')).to.be.false;
        expect($buttonsContainer.find('.orcid-action-delete').hasClass('hidden')).to.be.false;


        // and hide all icons when sign out from orcid
        minsub.publish(minsub.ORCID_ANNOUNCEMENT, {
          msgType: OrcidApiConstants.Events.SignOut,
          data: {}
        });

        $hiddenOrcidActions = $renderResult.find('.orcid-actions.hidden');
        expect($allOrcidActions.length).to.be.equal($hiddenOrcidActions.length);

        done();
      });


      it('should fill bulk insert bucket with row model data', function(done){
        var widget = new _getWidget();

        var $renderResult = widget.render().$el;
        $("#test").append($renderResult);

        minsub.publish(minsub.START_SEARCH, new ApiQuery({'q': 'foo:bar'}));

        // trigger event of user profile refreshment
        minsub.publish(minsub.ORCID_ANNOUNCEMENT, {
          msgType: OrcidApiConstants.Events.UserProfileRefreshed,
          data: getUserProfileJson()
        });

        notifier.startBulkInsert();

        var $toggles = $renderResult.find('input[name=identifier]');

        expect(notifier.model.attributes.bulkInsertWorks.length).to.be.equal(0);

        $toggles[0].click();
        $toggles[1].click();

        expect(notifier.model.attributes.bulkInsertWorks.length).to.be.equal(2);

        done();

      });

      it('should trigger proper action', function(done){
        var widget = new _getWidget();

        var $renderResult = $(widget.render().el);
        $("#test").append($renderResult);

        minsub.publish(minsub.START_SEARCH, new ApiQuery({'q': 'foo:bar'}));

        // trigger event of user profile refreshment
        minsub.publish(minsub.ORCID_ANNOUNCEMENT, {
          msgType: OrcidApiConstants.Events.UserProfileRefreshed,
          data: getUserProfileJson()
        });


        var $actionList = $renderResult.find('.orcid-actions ul').first().find('a.orcid-action');

        var insertTriggered = false;
        var deleteTriggered = false;
        var updateTriggered = false;

        minsub.subscribe(minsub.ORCID_ANNOUNCEMENT, function(msg){
          var data = msg.data;
          if (data.actionType == 'insert'){
            insertTriggered = true;
          }
          else if (data.actionType == 'update'){
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

        expect(insertTriggered).to.be.true;
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

    })
  });