define([
    'marionette',
    'jquery',
    'xml2json',
    'backbone',
    'js/bugutils/minimal_pubsub',
    'js/modules/orcid/orcid_works/widget',
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
            OrcidWorksWidget,
            OrcidResultRowExtension,
            OrcidNotifierModule,
            OrcidApiConstants,
            ApiQuery,
            Test1,
            Test2,
            TestOrcidProfileData) {

    describe('Orcid widget to display and operate orcid works data', function () {

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

      var _getWidget = function () {
        var widget = new OrcidWorksWidget();
        widget.activate(beehive.getHardenedInstance());

        return widget;
      };

      var getUserProfileJson = function () {
        return $.xml2json(TestOrcidProfileData)['orcid-message']['orcid-profile'];
      };

      it('should be Orcid widget', function (done) {
        expect(new OrcidWorksWidget()).to.be.instanceof(OrcidWorksWidget);

        done();
      });

      it('should display orcid data with proper actions', function(done){
        var widget = _getWidget();

        var $renderResult = widget.render().$el;
        $("#test").append($renderResult);

        // the widget is not currently displayed - no orcid user
        expect($renderResult.html()).to.be.equal('');

        // trigger event of user profile refreshment
        minsub.publish(minsub.ORCID_ANNOUNCEMENT, {
          msgType: OrcidApiConstants.Events.UserProfileRefreshed,
          data: getUserProfileJson()
        });

        //.orcid-action-delete
        expect($renderResult.find('.orcid-actions:not(.hidden) .orcid-action-delete').length)
          .to.be.equal($renderResult.find('.orcid-actions:not(.hidden)').length);

        done();
      });

      it('should handle bulk insert mode start/finish/cancel', function(done){
        var widget = _getWidget();

        var $renderResult = widget.render().$el;
        $("#test").append($renderResult);

        // the widget is not currently displayed - no orcid user
        expect($renderResult.html()).to.be.equal('');

        // trigger event of user profile refreshment
        minsub.publish(minsub.ORCID_ANNOUNCEMENT, {
          msgType: OrcidApiConstants.Events.UserProfileRefreshed,
          data: getUserProfileJson()
        });

        var $startBulkInsertModeButton = $renderResult.find('button[name=doBulkInsert]');
        var $cancelBulkInsertModeButton = $renderResult.find('button[name=cancelBulkInsert]');
        var $finishBulkInsertModeButton = $renderResult.find('button[name=finishBulkInsert]');

        expect($startBulkInsertModeButton.hasClass('hidden')).to.be.false;
        expect($cancelBulkInsertModeButton.hasClass('hidden')).to.be.true;
        expect($finishBulkInsertModeButton.hasClass('hidden')).to.be.true;

        var lastMinSubMsg = undefined;

        minsub.subscribe(minsub.ORCID_ANNOUNCEMENT,
          function(msg){
            lastMinSubMsg = msg;
        });

        $startBulkInsertModeButton.click();

        expect($startBulkInsertModeButton.hasClass('hidden')).to.be.true;
        expect($cancelBulkInsertModeButton.hasClass('hidden')).to.be.false;
        expect($finishBulkInsertModeButton.hasClass('hidden')).to.be.false;

        expect(lastMinSubMsg != undefined).to.be.true;
        expect(lastMinSubMsg.msgType).to.be.equal(OrcidApiConstants.Events.IsBulkInsertMode);
        expect(lastMinSubMsg.data).to.be.true;

        lastMinSubMsg = undefined;

        $cancelBulkInsertModeButton.click();

        expect($startBulkInsertModeButton.hasClass('hidden')).to.be.false;
        expect($cancelBulkInsertModeButton.hasClass('hidden')).to.be.true;
        expect($finishBulkInsertModeButton.hasClass('hidden')).to.be.true;
        expect(lastMinSubMsg.data).to.be.false;

        lastMinSubMsg = undefined;

        $startBulkInsertModeButton.click();
        $finishBulkInsertModeButton.click();

        expect($startBulkInsertModeButton.hasClass('hidden')).to.be.false;
        expect($cancelBulkInsertModeButton.hasClass('hidden')).to.be.true;
        expect($finishBulkInsertModeButton.hasClass('hidden')).to.be.true;
        expect(lastMinSubMsg.data).to.be.false;


        done();
      });
    });
  });