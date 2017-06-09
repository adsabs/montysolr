'use strict';
/**
 * Options for the `assemble` grunt task
 *
 * @module grunt/assemble
 */
module.exports = function (grunt) {
  grunt.registerTask('assemble', 'Prepares release for distribution', function() {
      // Purpose of this task is to generate a release ready configuration
      // with paths that point to the proper locations; to do that we generate
      // new jsmap file and also copy few files

      var chalk = require('chalk');
      var src = 'dist/discovery.config.original.js';
      var jsMap = grunt.file.readJSON('dist/jsmap.json');
      var cssMap = grunt.file.readJSON('dist/cssmap.json');
      var config = grunt.file.read(src);
      var gitDescribe = grunt.file.read('git-describe').trim();

      // find out what version of bbb we are going to assemble
      var tagInfo = gitDescribe.split('-');
      var version;
      if (tagInfo.length == 1) {
        version = tagInfo[0]; // the latest tag is also the latest commit (we'll use tagname v1.x.x)
      }
      else {
        version = tagInfo[2]; // use commit number instead of a tag
      }


      // test there is 'paths:' only once
      var regex = /['"]*paths['"]*\s*:[ \n]*{/;
      var m = regex.exec(config);
      if (m.length !== 1) {
        throw new Error('I cant find paths section, and refuse to continue for fear of breaking somehting');
      }

      // modify the jsMap, remove .js suffix
      var newMap = {};
      for (var name in jsMap) {
        newMap[name.replace('.js', '')] = jsMap[name].replace('.js', '');
      }

      //now the tricky part, read the values from the discovery.config and collect config['paths']
      var paths = {};
      var window = {};
      var oldc = require.config; // just to be sure...
      require.config = function(c) {
        paths = c.paths;
      };

      console.log('Going to eval ' + tgt + '...');
      eval(config);
      require.config = oldc;

      // add version identifier to the file names
      paths['config'] = paths['config'] + '.' + version;
      paths['main'] = newMap['js/apps/discovery/main'];
      paths['router'] = newMap['js/apps/discovery/router'];

      // merge the two definition together (overwriting old defs if necessary)
      for (var k in newMap) {
        paths[k] = newMap[k];
      }


      // in development we use google-analytics, but for production we'll serve it
      // ourselves (because of silly Ghostery breaking Safari browsers)
      grunt.file.write('dist/libs/g.' + version + '.js', grunt.file.read('dist/libs/g.js'));
      paths['google-analytics'] = 'libs/g.' + version;

      // build the configuration
      var firstPart = config.substring(0, m.index);
      var lastPart = config.substring(m.index + m[0].length, config.length);
      var newConfig = firstPart + 'paths: ' + JSON.stringify(paths, null, '  ') + ',\n' + m[0].replace('paths', 'oldpaths') + lastPart;

      //console.log(paths);

      var tgt = src.replace('original.js', version + '.js');
      grunt.file.write(tgt, newConfig);
      console.log(chalk.green('Updated: ' + tgt + ' section paths, with bust cache hashes'));

      // now update the html
      var indexHtml = grunt.file.read('dist/index.original.html');

      // first the js path
      var newHtml = indexHtml.replace('bumblebee_app', 'bumblebee_app.' + version);
      // then also the css
      for (var css in cssMap) {
        newHtml = newHtml.replace(css, cssMap[css]);
      }

      grunt.file.write('dist/index.' + version + '.html', newHtml);
      grunt.file.write('dist/index.html', newHtml);

      console.log(chalk.green(
      "=====================================================================\n" +
      "OK, done! Your release is ready for deployment. But I recommend that\n"  +
      "you test it, first make sure the development web server is not running\n" +
      "then execute: grunt test:release (or 'grunt server:release' and look)\n" +
      "\n" +
      "Also make sure that you deploy correct values with dist/discovery.vars.js\n" +
      "=====================================================================\n"))
  });
};
