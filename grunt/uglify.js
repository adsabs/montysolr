'use strict';
/**
 * Options for the `uglify` grunt task
 *
 * @module grunt/uglify
 */
module.exports = {
  release: {
    options: {
      mangle: true,
      compress: {
        sequences: true,
        dead_code: true,
        conditionals: true,
        booleans: true,
        unused: true,
        if_return: true,
        join_vars: true
      },
      output: {
        comments: false
      }
    },
    files: [{
      expand: true,
      cwd: 'dist',
      src: 'bumblebee_app.js',
      dest: 'dist'
    }]
  }
};
