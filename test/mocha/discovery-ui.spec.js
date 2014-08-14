// the base path of these tests is the location
// of the src/js/apps/XXXXX/config.js so we need
// to find relative homedir of tests

var tbase = '../../test/mocha/js';
var abase = tbase + '/apps/discovery';

// These are the tests that we want to run for this app
var tests = [

  // UI components
  tbase + '/widgets/base_tree_view.spec.js',
  tbase + '/widgets/base_container_view.spec.js',
  tbase + '/widgets/base_widget.spec.js',

  tbase + '/widgets/multi_callback_widget.spec.js',
  tbase + '/widgets/results_render_widget.spec.js',

  tbase + '/widgets/facet_container_view.spec.js',
  tbase + '/widgets/facet_widget.spec.js',

  tbase + '/widgets/api_response_widget.spec.js',
  tbase + '/widgets/api_query_widget.spec.js',
  tbase + '/widgets/api_request_widget.spec.js',
  tbase + '/widgets/tabs_widget.spec.js',
  tbase + '/widgets/search_bar_widget.spec.js',

  tbase + '/widgets/citations_widget.spec.js',
  tbase + '/widgets/references_widget.spec.js',
  tbase + '/widgets/similar_widget.spec.js',
  tbase + '/widgets/table_of_contents_widget.spec.js',
  tbase + '/widgets/coreads_widget.spec.js',
  tbase + '/page_managers/abstract_page_controller.spec.js',
  tbase + '/widgets/resources_widget.spec.js',


  //  tbase + '/page_managers/abstract_page_controller.spec.js',
//  tbase + '/page_managers/landing_page_controller.spec.js',
//  tbase + '/page_managers/results_page_controller.spec.js'


  tbase + '/widgets/year-graph-facet-widget.spec.js',
  tbase + '/widgets/citation-graph-facet-widget.spec.js'




  //tbase + '/widgets/facet_container_views.spec.js',
  //tbase + '/widgets/facet_controllers.spec.js'


];


// Run test on command line or in browser
require(tests, function() {
    (window.mochaPhantomJS || mocha).run();
});
