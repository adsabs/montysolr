'use strict';
/**
 * Options for the `env` grunt task
 *
 * Sets up some environmental variables
 * These are important only for the express task (webserver)
 *
 * @module grunt/env
 */
module.exports = {
  qa: {
    NODE_ENV: 'production',
    TARGET: 'qa'
  },
  prod: {
    NODE_ENV: 'production',
    TARGET: 'prod'
  },
  dev: {
    NODE_ENV: 'development',
    TARGET: 'dev'
  },
  release: {
    NODE_ENV: 'release',
    TARGET: 'dev'
  }
};
