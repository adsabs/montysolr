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
<<<<<<< HEAD

    tbase + '/widgets/list_of_things_widget.spec.js',
    tbase + '/widgets/multi_callback_widget.spec.js',
    tbase + '/widgets/reads_graph_facet_widget.spec.js',
    tbase + '/widgets/references_widget.spec.js',
    tbase + '/widgets/resources_widget.spec.js',

    tbase + '/widgets/references_widget.spec.js',
    tbase + '/widgets/results_render_widget.spec.js',
    tbase + '/widgets/coreads_widget.spec.js',
    tbase + '/widgets/citations_widget.spec.js',
    tbase + '/widgets/table_of_contents_widget.spec.js',


  tbase + '/widgets/search_bar_widget.spec.js',
    //TBD 24/09/14 tbase + '/widgets/similar_widget.spec.js',
    tbase + '/widgets/table_of_contents_widget.spec.js',
    tbase + '/widgets/tabs_widget.spec.js',
    tbase + '/widgets/wordcloud_widget.spec.js',
    tbase + '/widgets/year_graph_facet_widget.spec.js'

    tbase + '/widgets/network_widget.spec.js'


];


// Run test on command line or in browser
require(tests, function() {
    (window.mochaPhantomJS || mocha).run();
});
