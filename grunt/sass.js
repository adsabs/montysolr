'use strict';
/**
 * Options for the `sass` grunt task
 *
 * @module grunt/sass
 */
module.exports = {
  options: {
    sourceMap: true
  },
  dist: {
    files: {
      'src/styles/css/styles.css' : 'src/styles/sass/manifest.scss'
    }
  }
};
