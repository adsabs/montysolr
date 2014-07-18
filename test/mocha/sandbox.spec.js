
// the base path of these tests is the location
// of the application config.js so we need
// to find relative homedir of tests

var tbase = '../../test/mocha/js';
var abase = tbase + '/apps/discovery';

// discover components that should be loaded dynamically
var additional = [];
if (window.location && window.location.search) {
  var elems = window.location.search.split('&');
  if (elems.length > 0) {
    for (var i=0; i<elems.length;i++) {
      var kv = elems[i].split('=');
      additional.push(tbase + kv[1].trim());
    }
  }
}


// These are the tests that we want to run for this app
var tests = [

    //tbase + '/widgets/facet_widget.spec.js',
    //tbase + '/widgets/results_render_widget.spec.js',
    //tbase + '/widgets/base_container_view.spec.js',

    //tbase + '/widgets/base_tree_view.spec.js',
    //tbase + '/widgets/facet_container_view.spec.js',

    //tbase + '/components/api_query_updater.spec.js',
    //tbase + '/widgets/facet_widget.spec.js',
    //tbase + '/components/query_mediator.spec.js'
    //tbase + '/widgets/multi_callback_widget.spec.js',
    //tbase + '/widgets/facet_zoomable_graph_view.spec.js',
    //tbase + '/widgets/facet_controllers.spec.js'
    //tbase + '/widgets/facet_hier_widget.spec.js'
    //tbase + '/widgets/facet_zoomable_graph_view.spec.js'
    //tbase + '/widgets/list_ot_things_widget.spec.js',
    //tbase + '/widgets/results_render_widget.spec.js'

    tbase + '/components/query_builder/plugin.spec.js',
    tbase + '/components/query_builder/rules_translator.spec.js',
    //tbase + '/components/query_builder/translation_ui2query.spec.js',
    tbase + '/components/query_builder/translation_query2ui.spec.js',

    tbase + '/components/query_builder/plugin.testcases.spec.js'

];

tests = tests.concat(additional);

// Run test on command line or in browser
require(tests, function() {
  (window.mochaPhantomJS || mocha).run();
});

