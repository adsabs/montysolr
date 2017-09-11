'use strict';
/**
 * creating coverage task
 *
 * @module grunt/coverage
 */
module.exports = function (grunt) {

  var istanbul = require('istanbul');
  var COVERAGE_HTML_OUTPUT_DIR = 'test/coverage/reports/html';
  var COVERAGE_LCOV_OUTPUT_DIR = 'test/coverage/reports/lcov';
  var COVERAGE_COLLECTION_FILE = 'test/coverage/coverage.json';

  grunt.registerMultiTask('coverage-report', function () {

    var options = this.options({ htmlReport: false });

    // get the coverage object from the collection file generated
    var coverageObject = grunt.file.readJSON(COVERAGE_COLLECTION_FILE);
    var collector = new istanbul.Collector();
    collector.add(coverageObject);

    // Generate a quick summary to be shown in the output
    var finalCoverage = collector.getFinalCoverage();
    var summary = istanbul.utils.summarizeCoverage(finalCoverage);

    // Output the percentages of the coverage
    grunt.log.ok('Coverage:');
    grunt.log.ok('-  Lines: ' + summary.lines.pct + '%');
    grunt.log.ok('-  Statements: ' + summary.statements.pct + '%');
    grunt.log.ok('-  Functions: ' + summary.functions.pct + '%');
    grunt.log.ok('-  Branches: ' + summary.branches.pct + '%');

    // write reports
    if (options.htmlReport) {
      istanbul.Report.create('html', {
        dir: COVERAGE_HTML_OUTPUT_DIR
      }).writeReport(collector, true);
    }

    istanbul.Report.create('lcov', {
      dir: COVERAGE_LCOV_OUTPUT_DIR
    }).writeReport(collector, true);
  });

  return {
    'coveralls': {
      htmlReport: false
    },
    'html': {
      htmlReport: true
    }
  }
};


