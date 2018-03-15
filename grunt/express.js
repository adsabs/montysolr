'use strict';
/**
 * Options for the `express` grunt task
 *
 * @module grunt/express
 */
module.exports = {
  options: {
    output: 'Listening on port.*',
    script: './server.js'
  },
  once: {
    options: {
      background: true
    }
  },
  server: {
    options: {
      background: false
    }
  }
};
