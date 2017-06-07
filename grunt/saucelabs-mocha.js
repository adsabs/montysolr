'use strict';
/**
 * Options for the `saucelabs-mocha` grunt task
 *
 * @module grunt/saucelabs-mocha
 */
module.exports = function (grunt) {
  return {
    all: {
      options: {
        urls: ['http://localhost:<%= local.port || 8000 %>/test/' + (grunt.option('testname') || 'mocha/tests.html?bbbSuite=core-suite')],
        tunnelTimeout: 30,
        'tunnel-identifier': process.env.TRAVIS_JOB_NUMBER,
        build: process.env.TRAVIS_JOB_ID,
        concurrency: 5,
        throttled: 5,
        pollInterval: 2000,
        statusCheckAttempts: 180,
        // the logic here is to test browser versions
        // bbb does not depend on OS specific API's
        // but it could still happen that certain features
        // are not working in the same browser (different OS's)
        // if we discover that, we should add that pair here
        // Otherwise, we are testing against the 'worst'
        // OS - which may even by Linux in some cases; e.g.
        // (for drawing, flash playback etc)

        browsers: [
          {
            browserName: 'internet explorer',
            platform: 'Windows 8.1',
            version: '11.0'
          },
          {
            browserName: 'android',
            platform: 'linux',
            deviceName: 'Samsung Galaxy S4 Emulator',
            version: '4.3'
          },
          {
            browserName: 'android',
            platform: 'linux',
            deviceName: 'Google Nexus 7 HD Emulator',
            version: '4.2'
          },
          {
            browserName: 'firefox',
            platform: 'linux',
            version: '39'
          },
          {
            browserName: 'firefox',
            platform: 'linux',
            version: '38'
          },
          {
            browserName: 'firefox',
            platform: 'linux',
            version: '34'
          },
          {
            browserName: 'chrome',
            platform: 'linux',
            version: '43'
          },
          {
            browserName: 'chrome',
            platform: 'linux',
            version: '42'
          }
        ],
        testname: 'bbb',
        tags: ['<%= grunt.file.read("git-describe").trim() %>']
      }
    }
  };
};
