
// the base path of these tests is the location
// of the src/js/apps/XXXXX/config.js so we need
// to find relative homedir of tests

var tbase = '../../test/mocha/js';
var abase = tbase + '/apps/discovery';

console.log('discovery tests');

// These are the tests that we want to run for this app
var tests = [
  tbase + '/components/multi_params.spec.js',
  tbase + '/components/solr_params.spec.js',
  tbase + '/components/api_query.spec.js',
  tbase + '/components/json_response.spec.js'
];


// Run test on command line or in browser
require(tests, function() {
  (window.mochaPhantomJS || mocha).run();
});

