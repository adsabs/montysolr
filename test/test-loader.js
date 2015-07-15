define([
    "chai",
    "sinon",
    'underscore',
    'jquery'
  ],
  function(
    chai,
    sinon,
    _,
    $
    ){
    "use strict";

    /*globals mocha, expect, chai,bbbTest */
    chai.Assertion.includeStack = true;

    try {
      Function.bind();
    }
    catch (e) {
      console.log('Adding es5shim because we run non-Ecma5 environment');

      // this will be asynchronous, but the hope is that when the other tests finish loading
      // the es5-shim was already installed and is there
      require(["../../bower_components/es5-shim/es5-shim"], function() {
        console.log('ES5 activated');
      });
    }

    mocha.setup('bdd');
    mocha.bail(false);

    window.expect = chai.expect;
    window.assert = chai.assert;
    window.should = chai.should;
    if (!window.bbbTest) {
      throw new Error('You should set window.bbbTest before you call test-loader');
    }

    if (window.mochaPhantomJS) {
      var stringify = JSON.stringify;
      console.log('Because we run inside PHANTOMJS, redefining JSON.stringify() to output acyclic structures:');
      JSON.stringify = function(obj) {
        var seen = [];

        return stringify(obj, function(key, val) {
          if (typeof val === "object") {
            if (seen.indexOf(val) >= 0) { return; }
            seen.push(val);
          }
          return val;
        });
      };
    }

    if (window.blanket) {
      if (window.PHANTOMJS) { // run inside mocha-phantomjs
        blanket.options("reporter", 
          "../../node_modules/grunt-blanket-mocha/support/grunt-reporter.js");
        //blanket.options('reporter',
        //  "../../node_modules/blanket/src/reporters/simple_json_reporter.js");
      }
      
      blanket.options('debug', false);
    }

    /*
     hack mocha to allow us specify tests dynamically through url parameter
     */
    function getUrlParam(n, deflt) {
      var half = location.search.split(n + '=')[1];
      var val = (half !== undefined) ? decodeURIComponent(half.split('&')[0]) : deflt;
      if (_.isString(val)) {
        if (val.indexOf('|')) {
          return _.compact(val.split('|'));
        }
        return _.compact([val]);
      }
      else {
        return deflt;
      }
    }
    mocha._reporter.prototype.suiteURL = function(suite){
      var t = getUrlParam('bbbSuite');
      if (t)
        return '?grep=' + encodeURIComponent(suite.fullTitle()) + '&bbbSuite=' + t.join('|');
      return '?grep=' + encodeURIComponent(suite.fullTitle());
    };
    mocha._reporter.prototype.testURL = function(test){
      var t = getUrlParam('bbbSuite');
      if (t)
        return '?grep=' + encodeURIComponent(test.fullTitle()) + '&bbbSuite=' + t.join('|');
      return '?grep=' + encodeURIComponent(test.fullTitle());
    };


    var args = getUrlParam('bbbSuite', ['discovery-suite']);
    var suites = _.map(args, function(m) {return '../test/mocha/' + m});
    var testBase = '../../test/mocha/js/';



    $.ajax({
      type: "GET",
      url: "/api/v1/search",
      data: "q=*:*",
      dataType: "json",
      success: function(data) {
        if (data.responseHeader) {
          window.bbbTest.serverReady = true;
          console.log('We seem to have access to the api, setting serverReady=true');
        }
      },
      timeout:100
    }).always(function() {
      console.log('About to load testsuite(s): ' + JSON.stringify(suites));
      console.log('window.location=' + window.location + ' (testBase=' + testBase + ')');

      require(suites, function() { // load the test suites

        var tests = [];
        _.each(arguments, function (suite) {
          tests = tests.concat(suite);
        });
        tests = _.map(tests, function(t) {return testBase + t});

        require(tests, function() {
          console.log('tests are loaded');
          $('document').ready(function () {
            // Run test on command line or in browser
            var runner = (window.mochaPhantomJS || mocha).run();

            var failedTests = [];
            runner.on('end', function(){
              window.mochaResults = runner.stats;
              window.mochaResults.reports = failedTests;
            });

            runner.on('fail', logFailure);

            function logFailure(test, err){

              var flattenTitles = function(test){
                var titles = [];
                while (test.parent.title){
                  titles.push(test.parent.title);
                  test = test.parent;
                }
                return titles.reverse();
              };

              failedTests.push({name: test.title, result: false, message: err.message, stack: err.stack, titles: flattenTitles(test) });
            };
          });

        })
      });
    });


  });