/**
 * Suite of tests that deal with query builder components
 *
 * the base path of these tests is the location
 * of the src/js/apps/XXXXX/config.js so we need
 * to find relative homedir of tests
 *
 */

var tbase = '../../test/mocha/js';

var tests = [

  tbase + '/widgets/search_bar_widget.spec.js',
  tbase + '/components/query_builder/plugin.spec.js',
  tbase + '/components/query_builder/rules_translator.spec.js',
  tbase + '/components/query_builder/translation_query2ui.spec.js',
  tbase + '/components/query_builder/translation_ui2query.spec.js',
  tbase + '/components/query_builder/plugin.testcases.spec.js'

];


// Run test on command line or in browser
require(tests, function() {
    (window.mochaPhantomJS || mocha).run();
});
