'use strict';
/**
 * Options for the `clean` grunt task
 *
 * @module grunt/clean
 */
module.exports = {
  release: {
    src: ['dist/']
  },
  bower: {
    src: [
      './bower_components',
      'test/reports'
    ]
  }
};
