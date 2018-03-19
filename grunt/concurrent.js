'use strict';
/**
 * Options for the `concurrent` grunt task
 *
 * @module grunt/concurrent
 */
module.exports = {
  install: [
    'bower:install',
    'exec:npm_install',
    'curl:google-analytics'
  ],
  convert: [
    'exec:convert_enzyme',
    'exec:convert_dsjslib'
  ],
  hash_require: [
    'hash_require:js',
    'hash_require:css'
  ]
};
