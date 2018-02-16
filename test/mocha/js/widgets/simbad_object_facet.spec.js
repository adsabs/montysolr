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

  var desc = function (underTest, description, cb) {
    underTest = '[' + underTest + ']';
    describe(underTest + ' - ' + description, cb);
  };

  desc.skip = function (underTest, description, cb) {
    underTest = '[' + underTest + ']';
    describe.skip(underTest + ' - ' + description, cb);
  };

  var withServer = function (minsub) {
    var server = sinon.fakeServer.create();
    var reqCache = [];

    server.respondWith('POST', /objects/, function (xhr) {
	  var apiData = ObjectApiResponse();
	  console.log('responding to POST request');
      xhr.respond(200, {
        'Content-Type': 'application/json'
      }, JSON.stringify(apiData));
    });

    server.respondWith('GET', /facet\.prefix=(\d)/, function (xhr, facet_prefix) {
	  var facetData = ObjectSolrResponse(parseInt(facet_prefix));
	  console.log('getting facet data for level: ' + facet_prefix);
      xhr.respond(200, {
        'Content-Type': 'application/json'
      }, JSON.stringify(facetData));
    });

    var sendRequest = function (url, options, data) {
      var $dd = $.Deferred();

      $.ajax(url, _.extend(options, {
        data: JSON.stringify(data),
        dataType: 'json',
        headers: {
          'Content-Type': 'application/json'
        }
      }))
      .done(function () {
        options && options.done && options.done.apply(this, arguments);
        $dd.resolve.apply($dd, arguments);
      })
      .fail(function () {
        options && options.fail && options.fail.apply(this, arguments);
        $dd.reject.apply($dd, arguments);
      });

      server.respond();
      return $dd.promise().done(function (res) {
        reqCache.pop().call(null, new JSONResponse(res));
      });
    };

    minsub.request = _.constant('');

    sinon.stub(minsub.pubsub, 'subscribeOnce', function (key, action, cb) {
      reqCache.push(cb);
    });
	
    sinon.stub(minsub.pubsub, 'subscribe', function (key, action, cb) {
      reqCache.push(cb);
    });

    sinon.stub(minsub.pubsub, 'publish', function (key, action, apiRequest) {
	  var query = (apiRequest.has('query')) ? '?' + apiRequest.get('query').url() : '';
	  var url = apiRequest.get('target') + query;
      var options = apiRequest.get('options');
      var data = (options && options.data) || {};
      return sendRequest(url, options, data);
    });

    return minsub;
  };

  var init = function () {
    this.sb = sinon.sandbox.create();
    this.w = new ObjectFacet();
    var minsub = withServer(new MinPubSub());
    this.w.activate(minsub.beehive.getHardenedInstance());
  };

  var teardown = function () {
    this.sb.restore();
    this.w.destroy();
    this.w = null;
  };

  var test = function () {
    describe('SIMBAD Object Facet Widget (simbad_object_facet.spec.js)', function () {

      desc('Widget', 'Main Widget', function () {
        beforeEach(init);
        afterEach(teardown);

        it('extends base widget', function () {
          expect(this.w instanceof BaseWidget).to.eql(true);
        });

        it('state is updated after loading in data', function () {
         
          var state = this.w.store.getState();
          // at first the pagination state of the SIMBAD objects facet is 'undefined'
          expect(state.pagination.state).to.eql(undefined);
		  var id;
		  this.w.store.dispatch(this.w.actions.data_requested(id));
		  // now the pagination state of the SIMBAD objects facet is 'loading...'
		  state = this.w.store.getState();
		  expect(state.pagination.state).to.eql('loading');
		  // put data into the widget
		  this.w.store.dispatch(this.w.actions.data_received(ObjectSolrResponse(0)));
		  state = this.w.store.getState();
		  // now the pagination state of the SIMBAD objects facet is 'success'
		  expect(state.pagination.state).to.eql('success');
		  // there are supposed to be the top level facets from the mock data
		  var expected_facets = ObjectSolrResponse(0).facet_counts.facet_fields.simbad_object_facet_hier;
		  expected_facets = expected_facets.filter(function(el) {
		  	if (typeof el === 'string') {
		  		return el
		  	};
		  });
		  expect(state.children).to.eql(expected_facets)
		  // but nothing is opened to the next level
		  expect(state.state.open).to.eql(false)
          expect(state.state.selected).to.eql([]);
		  //
		  var hierarchicalResponse = _.cloneDeep(ObjectSolrResponse(0));
		  hierarchicalResponse.facet_counts.facet_fields.simbad_object_facet_hier = ['1/Galaxy/529086', 5, '1/Galaxy/3133169', 10]
          this.w.store.dispatch(this.w.actions.data_received(hierarchicalResponse, '0/Galaxy'));
		  $('#test').append(this.w.render().el);
		  // open the "0/Galaxy" facet
		  this.w.store.dispatch(this.w.actions.toggle_facet('0/Galaxy'));
		  state = this.w.store.getState();
		  // The top "Galaxy" facet should be open
		  expect(state.facets['0/Galaxy'].state.open).to.eql(true)
		  // The top "Star" facet should be closed
		  expect(state.facets['0/Star'].state.open).to.eql(false)
		  // The second level "Galaxy" entries should be closed
		  expect(state.facets['1/Galaxy/529086'].state.open).to.eql(false)
		  expect(state.facets['1/Galaxy/3133169'].state.open).to.eql(false)
		  expect(state.state.selected).to.eql([]);
		  // Now select the "Galaxy" top level facet
		  this.w.store.dispatch(this.w.actions.select_facet('0/Galaxy'));
		  state = this.w.store.getState();
		  expect(state.state.selected).to.eql(["1/Galaxy/529086", "1/Galaxy/3133169", "0/Galaxy"]);
		  
        });
		
        it('state is updated after render for query', function () {

          this.w.setCurrentQuery(new MinPubSub.prototype.T.QUERY({
            q: 'star'
          })); 
		  var id;
		  this.w.store.dispatch(this.w.actions.fetch_data(id));
		  // This should have fired off a Solr request and populated the 4 top level facets
		  var state = this.w.store.getState();
		  // Get the expected top level facet entries from the mock data and remove the counts
		  var expected_facets = ObjectSolrResponse(0).facet_counts.facet_fields.simbad_object_facet_hier;
		  expected_facets = expected_facets.filter(function(el) {
		  	if (typeof el === 'string') {
		  		return el
		  	};
		  });
		  // Does the generated top level facet contain the entries from the mock data?
		  expect(state.children).to.eql(expected_facets);
		  // and they should be closed
		  expect(state.state.open).to.eql(false)
          expect(state.state.selected).to.eql([]);
		  // Next we open the first top level facet
		  id = '0/Galaxy';
		  this.w.store.dispatch(this.w.actions.select_facet(id));
		  state = this.w.store.getState();
		  expect(state.facets[id].children).to.eql([]);
		  // Update the facet with hierarchical data
          this.w.store.dispatch(this.w.actions.data_received(ObjectSolrResponse(1), id));
		  state = this.w.store.getState();
		  var expected_galaxies = ObjectSolrResponse(1).facet_counts.facet_fields.simbad_object_facet_hier;
		  expected_galaxies = expected_galaxies.filter(function(el) {
		  	if (typeof el === 'string') {
		  		return el
		  	};
		  });
		  // Now we expect the next level entries to be there
		  expect(state.facets[id].children).to.eql(expected_galaxies);
		  // Now we need to open the "Galaxy" and force the translation of the SIMBAD identifiers
		  this.w.store.dispatch(this.w.actions.fetch_data(id));
		  state = this.w.store.getState();
		  // The widget should now have a cached map of SIMBAD identifiers
		  expect(this.w).to.have.property('_simbidCache');
		  // The cache should have the contents expected from the mock data
		  // First build the hash with expected data from the mock data
		  var expected_cache = {};
		  var objectData = ObjectApiResponse();
		  for (var simbid in objectData) {
			  var facetName = '1/Galaxy/' + objectData[simbid].canonical;
			  expected_cache[facetName] = simbid
		  };
		  // Now compare this with the contents of the cache assigned to the widget
		  expect(this.w._simbidCache).to.eql(expected_cache);		  
        });

      });
    });
  };

  sinon.test(test)();
});
