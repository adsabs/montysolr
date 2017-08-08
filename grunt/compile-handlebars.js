'use strict';
/**
 * Options for the `compile-handlebars` grunt task
 *
 * Process files through the handlebars templating engine
 *
 * @module grunt/compile-handlebars
 */
var stringify = function (arr) {
  return arr.map(JSON.stringify);
};

module.exports = function (grunt) {
  var indexConfig = grunt.file.readJSON('./src/index.config.json');
  indexConfig = stringify(indexConfig);

  return {
    index: {
      files: [
        { src: './src/index.html', dest: 'dist/index.html' }
      ],
      preHTML: '',
      postHTML: '',
      templateData: {
        data: indexConfig
      }
    }
  };
};
