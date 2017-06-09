'use strict';
module.exports = function(grunt) {
  require('time-grunt')(grunt);
  require('load-grunt-config')(grunt, {
    data: {
      pkg: grunt.file.readJSON('package.json'),
      local: grunt.file.exists('local-config.json') ? grunt.file.readJSON('local-config.json') : {}
    },
    loadGruntTasks: {
      pattern: 'grunt-*',
      config: 'package.json',
      scope: 'devDependencies'
    }
  });

  // on watch events configure jshint to only run on changed file
  grunt.event.on('watch', function(action, filepath) {
    console.log("Linting ", filepath);
    grunt.config('jshint.individual.src', filepath);
    grunt.task.run(['jshint:individual']);
    return;
  });

  grunt.event.on('blanket:fileDone', function(total, filepath) {
    console.log("blanket:fileDone ", filepath);
    return;
  });
};
