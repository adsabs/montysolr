'use strict';
/**
 * Options for the `compress` grunt task
 *
 * Compres whatever we have in the dist and store
 * it along-side with it (nginx can serve such content automatically)
 *
 * @module grunt/compress
 */
module.exports = {
  release: {
    options: {
      mode: 'gzip'
    },
    expand: true,
    cwd: 'dist/',
    src: ['**/*.js', '**/*.htm*', '**/*.css'],
    dest: 'dist/'
  }
};
