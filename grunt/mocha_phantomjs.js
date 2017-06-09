'use strict';
/**
 * Options for the `mocha_phantomjs` grunt task
 *
 * PhantomJS is a headless browser that runs our tests, by default it runs core-suite
 * if you need to change the tested suite: grunt --testname='mocha/tests.html?bbbSuite=foo'
 *
 * @module grunt/mocha_phantomjs
 */
module.exports = function (grunt) {
  var testName = grunt.option('testname');

  return {
    options: {
      //'reporter': 'progress',
      'output': 'test/reports/' + (testName || 'mocha/discovery')
    },

    local_testing: ['test/' + (testName || 'mocha/tests.html')],

    web_testing: {
      options: {
        urls: [
          'http://localhost:<%= local.port || 8000 %>/test/' + (testName || 'mocha/tests.html?bbbSuite=discovery-suite')
        ]
      }
    },

    //**
    //* Another way: grunt test:web --testname='mocha/tests.html?bbbSuite=ui-suite'
    //**
    sandbox_testing: {
      options: {
        urls: [
          'http://localhost:<%= local.port || 8000 %>/test/mocha/tests.html?bbbSuite=sandbox'
        ]
      }
    },

    full_testing: {
      options: {
        output: null,
        urls: [
          //'http://localhost:<%= local.port || 8000 %>/test/mocha/tests.html?bbbSuite=core-suite',
          //'http://localhost:<%= local.port || 8000 %>/test/mocha/tests.html?bbbSuite=ui-suite',
          //'http://localhost:<%= local.port || 8000 %>/test/mocha/tests.html?bbbSuite=qb-suite'
          'http://localhost:<%= local.port || 8000 %>/test/mocha/tests.html?bbbSuite=discovery-suite'
        ]
      }
    }
  };
};
