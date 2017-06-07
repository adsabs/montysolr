'use strict';
/**
 * Options for the `processhtml` grunt task
 *
 * modify the html based on the instructions inside the html code
 * this can be useful to modify links to css, minified version
 * of javascript etc...
 *
 * @module grunt/processhtml
 */
module.exports = {
  release: {
    files: {
      'dist/index.html': ['dist/index.html'], // use dist as source
      'dist/todo.html': ['dist/todo.html'],
      'dist/example.html': ['dist/example.html']
    }
  }
};
