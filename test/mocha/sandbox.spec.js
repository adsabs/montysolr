
// the base path of these tests is the location
// of the src/js/apps/XXXXX/config.js so we need
// to find relative homedir of tests

var tbase = '../../test/mocha/js';
var abase = tbase + '/apps/discovery';

// These are the tests that we want to run for this app
var tests = [
  tbase + '/widgets/api_response_widget.spec.js',
  tbase + '/widgets/api_query_widget.spec.js'
];


// Run test on command line or in browser
require(tests, function() {
  (window.mochaPhantomJS || mocha).run();
});

