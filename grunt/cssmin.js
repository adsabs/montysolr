'use strict';
/**
 * Options for the `cssmin` grunt task
 *
 * Minify the distribution CSS.  This is of limited value to us, for two
 * reasons:
 * 1.) CSS imports are not working
 * 2.) The task doesn't seem to be able to discover css files - I have filed a github issue
 *
 * @module grunt/cssmin
 */
module.exports = {
  combine: {
    // without this the imports are simply eaten (though css-clean
    // added support for imports, it doesn't seem to work
    options: {
      processImport: false
    },
    files: {
      //cwd: 'dist',
      'dist/css/style.min.css': ['src/css/*.css', 'src/js/apps/**/css/*.css']
    }
  },
  minify: {
    options: {
      mode: 'gzip',
      report: 'min'
    },
    files: {
      'dist/foo': ['./dist/src/**/*.css', '!./dist/src/**/*.min.css']
    }
  },
  test: {
    options: {
      mode: 'gzip',
      report: 'min'
    },
    files: {
      //expand: true,
      //cwd: 'dist',
      src: ['./dist/**/*.css', '!./dist/**/*.min.css'],
      dest: 'dist/',
      ext: '.min.css'
    }
  }
};
