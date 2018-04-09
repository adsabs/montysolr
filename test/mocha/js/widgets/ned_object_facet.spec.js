'use strict';
define([
  'module',
  'underscore',
  'jquery',
  './test_json/object_facet_solr_ned',
  './test_json/object_service_response_ned',
  'react',
  'redux',
  'redux-thunk',
  'enzyme',
  'js/widgets/base/base_widget',
  'js/components/api_response',
  'js/components/api_request',
  'js/components/api_query',
  'js/components/api_targets',
  'js/bugutils/minimal_pubsub',
  'js/components/json_response',
  'js/widgets/facet/factory',
  'js/components/api_query_updater',
  'js/wraps/ned_object_facet'
], function (
    module,
	_,
	$,
	ObjectSolrResponse,
	ObjectApiResponse,
	React,
	Redux,
	ReduxThunk,
	Enzyme,
	BaseWidget,
    ApiResponse,
    ApiRequest,
    ApiQuery,
	ApiTargets,
    MinPubSub,
	JSONResponse,
	FacetFactory,
    ApiQueryUpdater,
    ObjectFacet
) {

  var init = function () {
    this.sb = sinon.sandbox.create();
    this.w = new ObjectFacet();
    this.pubsub = new (MinPubSub.extend({
      request: this.sb.stub()
    }))({ verbose: false });
  };

  var teardown = function () {
    this.sb.restore();
    this.pubsub.destroy();
  };

  describe('NED Object Facet Widget (ned_object_facet.spec.js)', function () {
    describe('Main Widget', function () {
      beforeEach(init);
      afterEach(teardown);

      it('extends base widget', function (done) {
        expect((new ObjectFacet()) instanceof BaseWidget);
        done();
      });

      it('state is updated after render for query with no id', function (done) {
        const w = new ObjectFacet();
        const getPubSub = this.sb.stub(w, 'getPubSub');
        const pubStubs = {
          DELIVERING_RESPONSE: '0',
          DELIVERING_REQUEST: '1',
          publish: this.sb.stub(),
          subscribe: this.sb.stub(),
          subscribeOnce: this.sb.stub()
        };
        getPubSub.returns(pubStubs);

        w.activate(this.pubsub.beehive);
        w.setCurrentQuery(new ApiQuery({ q: 'star' }));
        w.store.dispatch(w.actions.fetch_data());

        // there is a subscription first
        expect(pubStubs.subscribeOnce.calledOnce).to.eql(true);
        expect(pubStubs.subscribeOnce.args[0][0]).to.eql(pubStubs.DELIVERING_RESPONSE);

        // grab the callback
        const callback = pubStubs.subscribeOnce.args[0][1];

        expect(pubStubs.publish.calledOnce).to.eql(true);
        expect(pubStubs.publish.args[0][0]).to.eql(pubStubs.DELIVERING_REQUEST);

        const expectedRequest = {"q":["star"],"facet":["true"],"facet.mincount":["1"],"facet.limit":[20],"fl":["id"],"facet.prefix":["0/"],"facet.field":["ned_object_facet_hier"],"facet.offset":[0]};
        let actualRequest = pubStubs.publish.args[0][1];
        actualRequest = actualRequest.toJSON().query = actualRequest.get('query').toJSON();
        expect(actualRequest).to.eql(expectedRequest);

        // fire off the callback
        callback({ toJSON: _.constant(ObjectSolrResponse(0)) });

        // check the store for the facet details
        const state = w.store.getState();
        const prop = ObjectSolrResponse(0).facet_counts.facet_fields.ned_object_facet_hier;
        const expectedFacets = _.filter(prop, _.isString);

        expect(state.children).to.eql(expectedFacets);
        expect(state.state.open).to.eql(false);
        expect(state.state.selected).to.eql([]);

        done();
      });

      it('state is updated after render for query with id', function (done) {
        const w = new ObjectFacet();
        const getPubSub = this.sb.stub(w, 'getPubSub');
        const pubStubs = {
          publish: this.sb.stub(),
          subscribe: this.sb.stub(),
          subscribeOnce: this.sb.stub()
        };
        getPubSub.returns(pubStubs);

        w.activate(this.pubsub.beehive);
        w.setCurrentQuery(new ApiQuery({ q: 'star' }));
        w.store.dispatch(w.actions.fetch_data());
        const state = function () { return w.store.getState(); };

        // grab the callback
        const firstCallback = pubStubs.subscribeOnce.args[0][1];
        firstCallback({ toJSON: _.constant(ObjectSolrResponse(0)) });

        const id = '0/galaxy';
        w.store.dispatch(w.actions.select_facet(id));
        expect(state().facets[id].children).to.eql([]);

        w.store.dispatch(w.actions.data_received(ObjectSolrResponse(1), id));
        const prop = ObjectSolrResponse(1).facet_counts.facet_fields.ned_object_facet_hier;
        const expectedFacets = _.filter(prop, _.isString);
        expect(state().facets[id].children).to.eql(expectedFacets);

        // make a request
        w.store.dispatch(w.actions.fetch_data(id));
        const secondCallback = pubStubs.subscribeOnce.args[1][1];
        const data = ObjectApiResponse();
        secondCallback({ toJSON: _.constant(ObjectSolrResponse(1)) });

        expect(w).to.have.property('_nedidCache');

        const expectedCache = {};
        _.forEach(data, function (id) {
          expectedCache['1/galaxy/' + data[id.id].canonical] = id.id;
        });

        const thirdCallback = pubStubs.publish.args[2][1].toJSON().options.done;
        thirdCallback(ObjectApiResponse());

        expect(w._nedidCache).to.deep.equal(expectedCache);

        done();
      });
    });
  });
});
