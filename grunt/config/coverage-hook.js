'use strict';

// phantomjs's version of `fs`
var fs = require('fs');
var COVERAGE_FILE = 'test/coverage/coverage.json';

var saveCoverage = function (page) {
  var coverage = page.evaluate(function () {
    return window.__coverage__;
  });

  // fail if we don't have coverage
  if (!coverage) {
    return;
  }

  // fix paths to files
  var coverageKeys = Object.keys(coverage);
  coverageKeys.forEach(function (key) {
    coverage[key].path = 'src/js/' + coverage[key].path;
  });

  try {
    console.log('writing coverage file...');
    fs.write(COVERAGE_FILE, JSON.stringify(coverage), 'w');
    console.log('Coverage File Written');
  } catch (e) {
    console.log('Unable to write to file...');
    console.error(e);
  }
};

module.exports = {
  afterEnd: function (runner) {
    saveCoverage(runner.page);
  }
};
