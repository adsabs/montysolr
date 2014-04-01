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
    tbase + '/widgets/results_render_module.spec.js',
    tbase + '/components/api_request.spec.js',
    tbase + '/services/api.spec.js',

    tbase + '/widgets/search_bar_widget.spec.js',
    tbase + '/widgets/api_response_widget.spec.js',
    tbase + '/widgets/api_query_widget.spec.js'
];


// Run test on command line or in browser
require(tests, function() {
    (window.mochaPhantomJS || mocha).run();
});