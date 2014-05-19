// the base path of these tests is the location
// of the src/js/apps/XXXXX/config.js so we need
// to find relative homedir of tests

var tbase = '../../test/mocha/js';
var abase = tbase + '/apps/discovery';

// These are the tests that we want to run for this app
var tests = [
  tbase + '/components/multi_params.spec.js',
  tbase + '/components/solr_params.spec.js',
  tbase + '/components/api_query.spec.js',
  tbase + '/components/json_response.spec.js',
  tbase + '/components/api_response.spec.js',
  tbase + '/components/generic_module.spec.js',
  tbase + '/services/backbone.events.spec.js',
  tbase + '/services/pubsub.spec.js',
  tbase + '/components/pubsub_key.spec.js',
  tbase + '/components/facade.spec.js',
  tbase + '/components/beehive.spec.js',
  tbase + '/components/api_request.spec.js',
  tbase + '/services/api.spec.js',
  tbase + '/components/query_mediator.spec.js',
  tbase + '/components/services_container.spec.js',
  tbase + '/components/application.spec.js',

  // UI components
  tbase + '/widgets/base_widget.spec.js',
  tbase + '/widgets/multi_callback_widget.spec.js',
  tbase + '/widgets/results_render_widget.spec.js',
  tbase + '/widgets/search_bar_widget.spec.js',
  tbase + '/widgets/api_response_widget.spec.js',
  tbase + '/widgets/api_query_widget.spec.js',
  tbase + '/widgets/api_request_widget.spec.js',
  tbase + '/widgets/facet_widget.spec.js',
  tbase + '/widgets/facet_container_views.spec.js',
  tbase + '/widgets/facet_item_views.spec.js',
  tbase + '/widgets/facet_controllers.spec.js'


];


// Run test on command line or in browser
require(tests, function() {
    (window.mochaPhantomJS || mocha).run();
});
