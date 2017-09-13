'use strict';
/**
 * Options for the `requirejs` grunt task
 *
 * Task to minify modules/css; it should run only after files were
 * copied over to the 'dist' folder
 *
 * @module grunt/requirejs
 */

module.exports = function (grunt) {
  var createIncludePaths = function () {
    var s = grunt.file.read('src/discovery.config.js');
    require = {
      config: function(s) {
        return s;
      }
    };
    var bumblebeeConfig = eval(s).config['js/apps/discovery/main'];

    function getPaths(obj) {
      var paths = [];

      function pushPaths(config_obj) {
        for (var k in config_obj) {
          var v = config_obj[k];
          if (v instanceof Object) {
            pushPaths(v);
          } else {
            paths.push(v);
          }
        }
      };

      pushPaths(obj);
      return paths;
    }

    return getPaths(bumblebeeConfig);
  };

  return {
    waitSeconds: 0,
    baseUrl: 'dist/js', // this is needed just for the 'stupid' list task
    release_concatenated: {
      options: {
        baseUrl: 'dist/',
        wrapShim: true,
        include : createIncludePaths(),
        allowSourceOverwrites: true,
        out: 'dist/bumblebee_app.js',
        name: 'js/apps/discovery/main',
        keepBuildDir: true,
        mainConfigFile : 'dist/discovery.config.js',
        findNestedDependencies: true,
        wrap: true,
        preserveLicenseComments: false,
        generateSourceMaps: false,
        stubModules : ['babel', 'es6'],
        optimize: 'none', //'uglify2',
        paths : {
          //use cdns for major libs
          react : 'empty:',
          'react-dom' : 'empty:',
          'jquery' : 'empty:',
          'jquery-ui' : 'empty:',
          'requirejs' : 'empty:'
        }
      }
    },
    release_css: {
      options: {
        keepBuildDir: true,
        allowSourceOverwrites: true,
        baseUrl: 'dist/styles/css',
        removeCombined: true,
        dir: 'dist/styles/css',
        optimizeCss: 'standard'
      }
    }
  };
};
