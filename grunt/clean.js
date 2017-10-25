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
  deploy: {
    src: [
      'dist/**/*',
      '!dist/bumblebee_app.*.js',
      '!dist/styles',
      '!dist/styles/css',
      '!dist/styles/css/styles.*.css',
      '!dist/index.*.html',
      'dist/index.original.html',
    ]
  },
  bower: {
    src: [
      './bower_components',
      'test/reports'
    ]
  },
  coverage: {
    src: ['test/coverage']
  }
};
