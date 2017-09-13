'use strict';
/**
 * Options for the `babel` grunt task
 *
 * @module grunt/babel
 */
module.exports = {
  release: {
    options: {
      plugins: ['transform-react-jsx'],
      presets: ['env', 'react']
    },
    files: [{
      expand: true,
      cwd: 'dist/js',
      src: '**/*.jsx.js',
      dest: 'dist/js'
    }]
  }
};
