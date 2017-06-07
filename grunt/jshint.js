'use strict';
/**
 * Options for the `jshint` grunt task
 *
 * @module grunt/jshint
 */
module.exports = {
  uses_defaults: [
    'src/js/**/*.js',
    'test/mocha/**/*.js'
  ],
  ignore_semicolons: {
    options: {
      '-W033': true
    },
    files: {
      src: [
        'src/js/**/*.js',
        'test/mocha/**/*.js'
      ]
    }
  }
};
