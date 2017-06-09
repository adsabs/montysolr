'use strict';
/**
 * Options for the `watch` grunt task
 *
 * Watch files for modifications and reload certain tasks on change.
 * If you try to run webserver or unittesting outside 'watch' tasks
 * (i.e. before starting watch) the reloading probably will not work
 *
 * @module grunt/watch
 */
module.exports = {
  options: {
    atBegin: true,
    spawn: false,
    interrupt: true // necessary for windows
  },

  server: {
    files: [
      './Gruntfile',
      './src/js/**/*.js',
      './src/*.js',
      './src/*.html',
      './server.js',
      './src/styles/css/*.css'
    ],
    tasks: [
      'env:dev',
      'express:dev'
    ]
  },

  release: {
    files: ['./dist/*'],
    tasks: [
      'env:release',
      'express:release'
    ]
  },

  local_testing: {
    files: [
      './Gruntfile',
      './src/js/**/*.js',
      './test/mocha/**/*.js',
      './src/*.js',
      './src/*.html'
    ],
    tasks: [
      'mocha_phantomjs:local_testing',
      'watch:local_testing'
    ]
  },

  web_testing: {
    files: [
      './Gruntfile',
      './src/js/**/*.js',
      './test/mocha/**/*.js',
      './src/*.js',
      './src/*.html'
    ],
    tasks: [
      'express:dev',
      'mocha_phantomjs:web_testing',
      'watch:web_testing'
    ]
  },

  sandbox_testing: {
    files: [
      './Gruntfile',
      './src/js/**/*.js',
      './test/mocha/**/*.js',
      './src/*.js',
      './src/*.html'
    ],
    tasks: [
      'express:dev',
      'mocha_phantomjs:sandbox_testing',
      'watch:sandbox_testing'
    ]
  },

  styles: {
    files: ['./src/styles/sass/ads-sass/*.scss'], // which files to watch
    tasks: [
      'sass',
      'autoprefixer',
      'express:dev',
      'watch:styles'
    ],
  }
};
