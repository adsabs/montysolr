define(['marionette',
    'backbone',
    'js/bugutils/minimal_pubsub',
    'js/widgets/results/widget',
    'js/modules/orcid/orcid_result_row_extension/extension',
    'js/modules/orcid/orcid_model_notifier/module',
    'js/components/api_query',
    './test_json/test1',
    './test_json/test2'
  ],
  function (Marionette,
            Backbone,
            MinimalPubsub,
            ResultsWidget,
            OrcidResultRowExtension,
            OrcidNotifierModule,
            ApiQuery,
            Test1,
            Test2) {

    describe('Orcid extension of results widget', function(){

      var minsub;
      beforeEach(function(done) {

        minsub = new (MinimalPubsub.extend({
          request: function(apiRequest) {
            if (this.requestCounter % 2 === 0) {
              return Test2();
            } else {
              return Test1();
            }
          }
        }))({verbose: false});

        var notifier = new OrcidNotifierModule();
        notifier.activate(minsub.beehive);
        notifier.initialize();

        minsub.beehive.addService('OrcidModelNotifier', notifier);

        done();
      });

      afterEach(function(done) {
        minsub.close();
        var ta = $('#test');
        if (ta) {
          ta.empty();
        }
        done();
      });

      it('should has OrcidResultRowExtension', function(done){
        expect(new ResultsWidget()).to.be.instanceof(ResultsWidget);

        var widget = new ResultsWidget();
        expect(widget.activateResultsExtension).to.be.ok;
        expect(widget.onAllInternalEvents_results).to.be.ok;
        expect(widget.routeOrcidPubSub).to.be.ok;


        done();
      });

      var _getWidget = function() {
        var widget = new ResultsWidget();
        widget.activate(minsub.beehive.getHardenedInstance());
        return widget;
      };

      it('should display gray orcid icon, when item is not in orcid', function(done){
        var widget = new _getWidget();

        var $renderResult = widget.render();

        setTimeout(function() {
          done();
        });
      });


    })
  });