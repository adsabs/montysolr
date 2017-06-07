'use strict';
/**
 * Options for the `purifycss` grunt task
 *
 * Remove Unused CSS Rules
 *
 * @module grunt/purifycss
 */
module.exports = {
  options: {},
  target: {
    src: ['dist/bumblebee_app.js'],
    css: ['dist/styles/css/styles.css'],
    dest: 'dist/styles/css/styles.css',
  }
};
