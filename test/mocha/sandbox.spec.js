
// the base path of these tests is the location
// of the application config.js so we need
// to find relative homedir of tests

var tbase = '../../test/mocha/js';
var abase = tbase + '/apps/discovery';

// These are the tests that we want to run for this app
var tests = [

    //tbase + '/widgets/facet_widget.spec.js',
    tbase + '/widgets/base_container_view.spec.js',
    //tbase + '/widgets/facet_container_views.spec.js',
    //tbase + '/widgets/facet_item_views.spec.js',
    //tbase + '/widgets/facet_controllers.spec.js'



];


// Run test on command line or in browser
require(tests, function() {
  (window.mochaPhantomJS || mocha).run();
});

