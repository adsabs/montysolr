
// the base path of these tests is the location
// of the src/js/apps/XXXXX/config.js
var b = '../../test/mocha/js/apps/todo/';


// These are the tests that we want to run for this app
var tests = [
  b + 'app.spec.js',
  b + 'router.spec.js'
];


// Run test on command line or in browser
require(tests, function() {
  (window.mochaPhantomJS || mocha).run();
});

