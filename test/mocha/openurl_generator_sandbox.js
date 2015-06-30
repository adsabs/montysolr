/**
 * Each suite can be executed separately, point your browser to:
 *
 * <host>:<port>/test/mocha/tests.html?bbbSuite=<suite-name>
 *
 * The @param 'tests' is an array of strings, paths to invididual unittests, e.g.
 *   [ 'js/components/api_query.spec.js',
 *     'js/widgets/hello_world_widget.spec.js']
 */

define([], function() {

  var tests  = [
    '/mixins/openurl_generator.spec.js'
  ];
  return tests;
});