
// the base path of these tests is the location
// of the application config.js so we need
// to find relative homedir of tests

var tbase = '../../test/mocha/js';
var abase = tbase + '/apps/discovery';

// These are the tests that we want to run for this app
var tests = [
  //tbase + '/components/services_container.spec.js',
  //tbase + '/services/api.spec.js',
  //tbase + '/components/beehive.spec.js',
  //tbase + '/components/query_mediator.spec.js'
  //tbase + '/services/pubsub.spec.js',
  // tbase + '/widgets/results_render_widget.spec.js',
  tbase + '/widgets/facet_container_views.spec.js',



  // tbase + '/widgets/app_test.spec.js'
];


// Run test on command line or in browser
require(tests, function() {
  (window.mochaPhantomJS || mocha).run();
});

