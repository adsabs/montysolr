/**
 * Suite of tests that deal with UI components
 * exclusively
 *
 * There are several tests suites, numbered
 * #1....#N
 */

var tbase = '../../test/mocha/js';

// These are the tests that we want to run for this app
var tests = [

    tbase + '/page_managers/all_tests.spec.js',

    //TBD 24/09/14 tbase + '/widgets/abstract_widget.spec.js',
    tbase + '/widgets/api_query_widget.spec.js',
    tbase + '/widgets/api_request_widget.spec.js',
    tbase + '/widgets/api_response_widget.spec.js',
    tbase + '/widgets/base_container_view.spec.js',
    tbase + '/widgets/base_tree_view.spec.js',
    tbase + '/widgets/base_widget.spec.js',

    tbase + '/widgets/breadcrumb_widget.spec.js',
    tbase + '/widgets/citation_graph_facet_widget.spec.js',
    tbase + '/widgets/export_widget.spec.js',
    tbase + '/widgets/facet_container_view.spec.js',
    //TBD 24/09/14 tbase + '/widgets/facet_graph_widget.spec.js',
    //TBD 24/09/14 tbase + '/widgets/facet_hier_widget.spec.js',
    tbase + '/widgets/facet_widget.spec.js',
    //XXX 24/09/14 tbase + '/widgets/facet_zoomable_graph_view.spec.js',

    tbase + '/widgets/sort_widget.spec.js'


];


// Run test on command line or in browser
require(tests, function() {
    (window.mochaPhantomJS || mocha).run();
});
