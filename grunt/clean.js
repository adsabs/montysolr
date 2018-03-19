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
      '!dist/index.html',
      '!dist/bumblebee_app.*.js',
      '!dist/styles',
      '!dist/styles/img',
      '!dist/styles/img/*',
      '!dist/styles/css',
      '!dist/styles/css/images',
      '!dist/styles/css/images/*',
      '!dist/styles/css/styles.*.css',
      '!dist/libs',
      '!dist/libs/requirejs',
      '!dist/libs/requirejs/require.js',
    ]
  },
  libs: {
    src: [
      'src/libs'
    ]
  },
  coverage: {
    src: ['test/coverage']
  }
};
