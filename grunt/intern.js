'use strict';
/**
 * Options for the `intern` grunt task
 *
 * @module grunt/intern
 */
module.exports = {
  local: {
    options: {
      runType: 'runner', // defaults to 'client'
      config: 'test/intern-local',
      reporters: [ 'Console', 'Lcov' ]
    }
  },
  remote: {
    options: {
      runType: 'runner', // defaults to 'client'
      config: 'test/intern-remote',
      reporters: [ 'Console', 'Lcov' ]
    }
  }
};
