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

  tbase + '/widgets/search_bar_widget.spec.js',
  tbase + '/widgets/api_response_widget.spec.js',
  tbase + '/widgets/api_query_widget.spec.js',
  tbase + '/widgets/api_request_widget.spec.js',
  tbase + '/widgets/tabs_widget.spec.js',
  tbase + '/widgets/facet_zoomable_graph_view.spec.js',
  tbase + '/widgets/search_bar_widget.spec.js',


  //tbase + '/widgets/contents_manager_widget.spec.js'

  //tbase + '/widgets/facet_container_views.spec.js',

  //tbase + '/widgets/facet_controllers.spec.js'

  tbase + '/components/query_builder/plugin.spec.js',
  tbase + '/components/query_builder/rules_translator.spec.js',
  tbase + '/components/query_builder/translation_query2ui.spec.js',
  tbase + '/components/query_builder/plugin.testcases.spec.js'

];


// Run test on command line or in browser
require(tests, function() {
    (window.mochaPhantomJS || mocha).run();
});
