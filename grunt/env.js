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
  dev: {
    NODE_ENV: 'development',
    SERVER_ENV: 'dev',
    TARGET: 'dev'
  },
  prod: {
    NODE_ENV: 'production',
    SERVER_ENV: 'dev',
    TARGET: 'prod'
  },
  'release-prod': {
    NODE_ENV: 'production',
    SERVER_ENV: 'release',
    TARGET: 'prod'
  },
  'release-dev': {
    NODE_ENV: 'development',
    SERVER_ENV: 'release',
    TARGET: 'dev'
  }
};
