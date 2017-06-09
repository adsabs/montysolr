'use strict';
/**
 * Options for the `express` grunt task
 *
 * @module grunt/express
 */
module.exports = {
  options: {
    // some defaults
    background: true
  },
  dev: {
    options: {
      port: '<%= local.port_development || 8000 %>',
      script: 'server.js'
    }
  },
  release: {
    options: {
      port: '<%= local.port_production || 5000 %>',
      script: 'server.js'
    }
  }
};
