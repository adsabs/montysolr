
var tbase = '../../test/mocha/js';

// These are the tests that we want to run for this app
var tests = [

    tbase + '/widgets/wordcloud_widget.spec.js'

];


// Run test on command line or in browser
require(tests, function() {
  (window.mochaPhantomJS || mocha).run();
});
