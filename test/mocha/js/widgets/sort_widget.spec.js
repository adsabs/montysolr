
define([
    'jquery',
    'underscore',
    'js/components/api_response',
    'js/components/api_request',
    'js/components/api_query',
    'js/bugutils/minimal_pubsub',
    'es6!js/widgets/sort/widget.jsx',
    'es6!js/widgets/sort/redux/modules/sort-app',
    'js/components/api_feedback'
    ],
  function($, _, ApiResponse, ApiRequest, ApiQuery, MinimalPubSub, SortWidget, SortApp, ApiFeedback) {

    var init = function () {
      var minsub = new (MinimalPubSub.extend({
        request: function () {}
      }))({ verbose: false });
      this.sb = sinon.sandbox.create();
      this.pubsub = minsub;
      this.w = new SortWidget();
      this.w.activate(minsub.beehive.getHardenedInstance());
      this.getState = _.bind(function () {
        return this.w.store.getState().get('SortApp');
      }, this);
    };

    var teardown = function () {
      this.w.destroy();
      this.w = null;
      this.sb.restore();
      $('test-area').html('');
    };

    describe("Sort Widget (sort_widget.spec.js)", function (){

      beforeEach(init);
      afterEach(teardown);

      it('updates query and locks on new search', function (done) {
        var self = this;
        expect(self.getState()).to.eql(SortApp.initialState);

        // execute a new search
        this.pubsub.publish(this.pubsub.FEEDBACK, new ApiFeedback({
          code: ApiFeedback.CODES.SEARCH_CYCLE_STARTED,
          query: new ApiQuery({
            "q": ["test"],
            "sort": ["citation_count asc"]
          })
        }));

        _.defer(function () {
          var actual = self.getState().toJS();
          var expected = {
            "sort": {
              "id": "citation_count",
              "text": "Citation Count",
              "desc": "sort by number of citations"
            },
            "direction": "asc",
            "query": {
              "q": [ "test" ],
              "sort": [ "citation_count asc" ]
            },
            "locked": true
          };

          expect(expected.sort).to.eql(actual.sort);
          expect(expected.query).to.eql(actual.query);
          expect(expected.direction).to.eql(actual.direction);
          expect(expected.locked).to.eql(actual.locked);
          expect(actual.lockTimer).to.be.a('number');
          done();
        });
      });

      it('locks on progress signal', function (done) {
        var self = this;
        expect(self.getState()).to.eql(SortApp.initialState);

        // execute a new search
        this.pubsub.publish(this.pubsub.FEEDBACK, new ApiFeedback({
          code: ApiFeedback.CODES.SEARCH_CYCLE_PROGRESS
        }));

        _.defer(function () {
          var actual = self.getState().toJS();
          var expected = {
            "locked": true
          };

          expect(expected.locked).to.eql(actual.locked);
          expect(actual.lockTimer).to.be.a('number');
          done();
        });
      });

      it('unlocks on failure to start', function (done) {
        var self = this;
        expect(self.getState()).to.eql(SortApp.initialState);

        // execute a new search
        this.pubsub.publish(this.pubsub.FEEDBACK, new ApiFeedback({
          code: ApiFeedback.CODES.SEARCH_CYCLE_FAILED_TO_START
        }));

        _.defer(function () {
          var actual = self.getState().toJS();
          var expected = {
            "locked": false
          };

          expect(expected.locked).to.eql(actual.locked);
          expect(actual.lockTimer).to.be.a('number');
          done();
        });
      });

      it('unlocks on finish', function (done) {
        var self = this;
        expect(self.getState()).to.eql(SortApp.initialState);

        // execute a new search
        this.pubsub.publish(this.pubsub.FEEDBACK, new ApiFeedback({
          code: ApiFeedback.CODES.SEARCH_CYCLE_FINISHED
        }));

        _.defer(function () {
          var actual = self.getState().toJS();
          var expected = {
            "locked": false
          };

          expect(expected.locked).to.eql(actual.locked);
          expect(actual.lockTimer).to.be.a('number');
          done();
        });
      });

      it('changing selected sort field, fires new search', function (done) {
        var query = {
          q: ['test foo'],
          sort: ['date desc']
        };
        var spy = this.sb.spy();
        this.w.getPubSub = _.constant({
          publish: spy
        });
        this.w.store.dispatch(SortApp.setQuery(query));
        this.w.store.dispatch(SortApp.setSort('date'));

        _.delay(function () {
          expect(spy.calledOnce).to.eql(true);
          done();
        }, 500);
      });

      it('changing sort direction, fires new search', function (done) {
        var query = {
          q: ['test foo'],
          sort: ['date desc']
        };
        var spy = this.sb.spy();
        this.w.getPubSub = _.constant({
          publish: spy
        });
        this.w.store.dispatch(SortApp.setQuery(query));
        this.w.store.dispatch(SortApp.setDirection('desc'));

        _.delay(function () {
          expect(spy.calledOnce).to.eql(true);
          done();
        }, 500);
      });
    });
  });
