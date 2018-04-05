'use strict';
define([
  'module',
  'underscore',
  'jquery',
  './test_json/object_facet_solr_simbad',
  './test_json/object_service_response_simbad',
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
  'js/wraps/simbad_object_facet'
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
    $('test-area').empty();
  };

  describe('SIMBAD Object Facet Widget (simbad_object_facet.spec.js)', function () {
    describe('Main Widget', function () {
      beforeEach(init);
      afterEach(teardown);
      it('extends base widget', function (done) {
        expect((new ObjectFacet()) instanceof BaseWidget);
        done();
      });

      it('state is update after loading data', function (done) {
        const w = new ObjectFacet();
        const state = w.store.getState;
        const getPubSub = this.sb.stub(w, 'getPubSub');
        const pubStubs = {
          publish: this.sb.stub(),
          subscribe: this.sb.stub(),
          subscribeOnce: this.sb.stub()
        };
        getPubSub.returns(pubStubs);
        w.activate(this.pubsub.beehive);

        expect(state().pagination.state).to.eql(undefined);
        w.store.dispatch(w.actions.data_requested());
        expect(state().pagination.state).to.eql('loading');
        w.store.dispatch(w.actions.data_received(ObjectSolrResponse(0)));
        expect(state().pagination.state).to.eql('success');

        const prop = ObjectSolrResponse(0).facet_counts.facet_fields.simbad_object_facet_hier;
        const expectedFacets = _.filter(prop, _.isString);

        expect(state().children).to.eql(expectedFacets);
        expect(state().state.open).to.eql(false);
        expect(state().state.selected).to.eql([]);

        const hierarchicalResponse = _.cloneDeep(ObjectSolrResponse(0));
        hierarchicalResponse.facet_counts.facet_fields.simbad_object_facet_hier = ['1/Galaxy/529086', 5, '1/Galaxy/3133169', 10];
        w.store.dispatch(w.actions.data_received(hierarchicalResponse, '0/Galaxy'));

        // render
        $('#test-area').html(this.w.render().$el);

        w.store.dispatch(w.actions.toggle_facet('0/Galaxy'));
        // The top "Galaxy" facet should be open
        expect(state().facets['0/Galaxy'].state.open).to.eql(true);
        // The top "Star" facet should be closed
        expect(state().facets['0/Star'].state.open).to.eql(false);
        // The second level "Galaxy" entries should be closed
        expect(state().facets['1/Galaxy/529086'].state.open).to.eql(false);
        expect(state().facets['1/Galaxy/3133169'].state.open).to.eql(false);
        expect(state().state.selected).to.eql([]);

        w.store.dispatch(w.actions.select_facet('0/Galaxy'));
        expect(state().state.selected).to.eql(["1/Galaxy/529086", "1/Galaxy/3133169", "0/Galaxy"]);
        done();
      });

      it('state is updated for render for query', function (done) {
        const w = new ObjectFacet();
        const state = w.store.getState;
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

        const firstCallback = pubStubs.subscribeOnce.args[0][1];
        firstCallback({ toJSON: _.constant(ObjectSolrResponse(0)) });

        const prop = ObjectSolrResponse(0).facet_counts.facet_fields.simbad_object_facet_hier;
        const expectedFacets = _.filter(prop, _.isString);

        expect(state().children).to.eql(expectedFacets);
        expect(state().state.open).to.eql(false);
        expect(state().state.selected).to.eql([]);

        const id = '0/Galaxy';
        w.store.dispatch(w.actions.select_facet(id));
        expect(state().facets[id].children).to.eql([]);

        w.store.dispatch(w.actions.data_received(ObjectSolrResponse(1), id));
        const galaxyProp = ObjectSolrResponse(1).facet_counts.facet_fields.simbad_object_facet_hier;
        const expectedGalaxies = _.filter(galaxyProp, _.isString);

        // Now we expect the next level entries to be there
        expect(state().facets[id].children).to.eql(expectedGalaxies);

        // Now we need to open the "Galaxy" and force the translation of the SIMBAD identifiers
        w.store.dispatch(w.actions.fetch_data(id));

        const secondCallback = pubStubs.subscribeOnce.args[1][1];
        secondCallback({ toJSON: _.constant(ObjectSolrResponse(1)) });

        const thirdCallback = pubStubs.publish.args[2][1].toJSON().options.done;
        thirdCallback(ObjectApiResponse());

        // The widget should now have a cached map of SIMBAD identifiers
        expect(w).to.have.property('_simbidCache');

        // The cache should have the contents expected from the mock data
        // First build the hash with expected data from the mock data
        var expectedCache = {};
        var data = ObjectApiResponse();
        _.forEach(data, function (id) {
          expectedCache['1/Galaxy/' + data[id.id].canonical] = id.id;
        });

        expect(w._simbidCache).to.eql(expectedCache);
        done();
      });
    });
  });
});
