module.exports = function(grunt) {
  'use strict';

  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),

    // if you create 'local-config.json' some variables can be overriden there
    local: grunt.file.readJSON('local-config.json'),

    // Wipe out previous builds and test reporting.
    clean: ['dist/', 'test/reports'],

    // This will install libraries (client-side dependencies)
    bower: {
      install: {
        options: {
          // config: 'bower.json',
          cleanTargetDir: true,
          targetDir: './src/libs'
        }
      }
    },

    // Run your source code through JSHint's defaults.
    jshint: ['src/js/**/*.js'],


    exec: {
      // this is necessary to make the library AMD compatible
      convert_dsjslib: {
        cmd: 'node node_modules/requirejs/bin/r.js -convert src/libs/dsjslib src/libs/dsjslib'
      }
    },

    // This task uses James Burke's excellent r.js AMD builder to take all
    // modules and concatenate them into a single file.
    requirejs: {

      baseUrl: 'src/js', // this is needed just for the 'stupid' list task

      options: {
        //baseUrl: 'src/js',
        //mainConfigFile: 'src/js/config.js',
        dir: 'dist/js',
        baseUrl: 'src/js'
        //appDir:'src/js'
      },

      release: {
        options: {
          generateSourceMaps: true,

          optimize: 'uglify2',

          // Since we bootstrap with nested `require` calls this option allows
          // R.js to find them.
          findNestedDependencies: true,

          // Include a minimal AMD implementation shim.
          name: '../libs/almond/almond',

          // Wrap everything in an IIFE.
          wrap: true,

          // Do not preserve any license comments when working with source
          // maps.  These options are incompatible.
          preserveLicenseComments: false
        }
      }
    },


    // Minfiy the distribution CSS. This is of limited value to us, for two
    // reasons: 1) css imports are not working 2) the task doesn't seem to
    // be able to discover css files; i have filed a github issue/question
    cssmin: {
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
    },

    // sets up some environment variables; these are important
    // only for the express task (our webserver)
    env: {
      options: {
        API_ENDPOINT: '<%= local.api_endpoint || "http://localhost:9000/solr/select" %>'
      },
      dev: {
        HOMEDIR: 'src'
        //DEBUG: 'express:*'
      },
      prod: {
        HOMEDIR: 'dist'
      }
    },

    // start a development webserver (if you want to keep it running, run 'grunt server'
    express: {
      options: {
        // some defaults
        background: true
      },
      dev: {
        options: {
          port: '<%= local.port_development || 8000 %>',
          script: 'server.js'
        }
      },
      prod: {
        options: {
          port: '<%= local.port_production || 5000 %>',
          script: 'server.js'
        }
      }
    },

    // **
    // * Watch files for modifications and reload certain tasks on change;
    // * if you try to run webserver or unittesting outside 'watch' tasks
    // * (ie. before starting watch) the reloading will probably not work
    // **
    watch: {
      options: {
        atBegin: true,
        spawn: false, // for grunt-contrib-watch v0.5.0+, "nospawn: true" for lower versions. Without this option specified express won't be reloaded
        interrupt: true // necessary for windows
      },

      server: {
        files: ['./src/js/**/*.js', './src/*.js', './src/*.html', './server.js'],
        tasks: ['env:dev', 'express:dev']
      },

      local_testing: {
        files: ['./src/js/**/*.js', './test/mocha/**/*.js', './src/*.js', './src/*.html'],
        tasks: ['mocha_phantomjs:local_testing', 'watch:local_testing']
      },

      web_testing: {
        files: ['./src/js/**/*.js', './test/mocha/**/*.js', './src/*.js', './src/*.html'],
        tasks: ['express:dev', 'mocha_phantomjs:web_testing', 'watch:web_testing']
      },
      less_compile: {
        files: ['*.less'],
        tasks: ['less']

      }
    },

    //**
    //* PhantomJS is a headless browser that runs our tests, by default it runs <mocha/discovery>.spec.html
    //* if you need to change the tested file: grunt --testname=foo ....
    //**
    mocha_phantomjs: {
      options: {
        //'reporter': 'progress',
        'output': 'test/reports/' + (grunt.option('testname') || 'mocha/discovery')
      },

      local_testing: ['test/' + (grunt.option('testname') || 'mocha/discovery') + '.spec.html'],

      web_testing: {
        options: {
          urls: ['http://localhost:<%= local.port || 8000 %>/test/' + (grunt.option('testname') || 'mocha/discovery') + '.spec.html']
        }
      }
    },

    // modify the html based on the instructions inside the html code
    // this can be useful to modify links to css, minified version 
    // of javascript etc...
    processhtml: {
      release: {
        files: {
          'dist/index.html': ['dist/index.html'], // use dist as source
          'dist/todo.html': ['dist/todo.html'],
          'dist/example.html': ['dist/example.html']
        }
      }
    },

    // copy files from src into the distribution folder (but remove
    // src top level)
    copy: {
      release: {
        files: [{
          expand: true,
          src: ['./src/**'],
          dest: 'dist/',
          rename: function(dest, src) {
            //grunt.verbose.writeln('src' + src);
            return dest + src.replace('/src/', '/');
          }
        }]
      }
    },

    // compress whatever we have in the dist and 
    // store it along-side with it (nginx can serve
    // such content automatically)
    compress: {
      release: {
        options: {
          mode: 'gzip'
        },
        expand: true,
        cwd: 'dist/',
        src: ['**/*.js', '**/*.htm*', '**/*.css'],
        dest: 'dist/'
      }
    },

    coveralls: {
      options: {
        src: ['src/js/**/*.js'],
        coverage_dir: 'test/coverage/PhantomJS 1.9.2 (Linux)/'
      }
    },


    // concatenates all javascript into one big minified file (of limited 
    // use for now)
    uglify: {
      options: {
        banner: "/*! <%= pkg.name %> <%= grunt.template.today('yyyy-mm-dd') %> */\n"
      },
      build: {
        src: 'src/js/**/*.js',
        dest: 'dist/<%= pkg.name %>.min.js'
      }
    },

    less: {
      development: {
        files: {
          'src/styles/css/styles.css': 'src/styles/less/manifest.less',

        }
      },
      //     production: {
      //         options: {
      //             // paths: ["assets/css"],
      //             // cleancss: true,
      //             // modifyVars: {
      //             //     imgPath: '"http://mycdn.com/path/to/images"',
      //             //     bgColor: 'red'
      //             // }
      //         },
      //         files: {
      //             'src/styles/css/styles.css': 'src/styles/less/manifest.less',

      //         }
      //     }
    }

  });

  // Basic environment config
  grunt.loadNpmTasks('grunt-env');

  // Grunt BBB tasks.
  grunt.loadNpmTasks('grunt-bbb-requirejs'); // we use 'list' target only, requirejs will get overriden



  // Grunt contribution tasks.
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-cssmin');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-compress');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-requirejs');
  grunt.loadNpmTasks('grunt-contrib-connect');
  grunt.loadNpmTasks('grunt-express-server');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-exec');


  // karma tasks. (to ditch?)
  // grunt.loadNpmTasks('grunt-karma');
  // grunt.loadNpmTasks('grunt-karma-coveralls');

  // for testing
  grunt.loadNpmTasks('grunt-mocha-phantomjs');

  // other 3rd party libs
  grunt.loadNpmTasks('grunt-processhtml');
  grunt.loadNpmTasks('grunt-contrib-less');

  // Bower tasks
  grunt.loadNpmTasks('grunt-bower-task');

  //npm install
  grunt.loadNpmTasks('grunt-install-dependencies');

  // Create an aliased test task.
  grunt.registerTask('setup', 'Sets up the development environment',
    ['install-dependencies', 'bower-setup', 'less']);

  // When running the default Grunt command, just lint the code.
  grunt.registerTask('default', [
    'env:dev',
    'clean',
    'jshint',
    'copy',
    'processhtml',
    'requirejs',
    'compress'
    //'cssmin',
  ]);

  // starts a web server (automatically reloading)
  grunt.registerTask('server', ['env:dev',  "less", 'express:dev', 'watch:server', 'watch:less_compile']);

  // runs tests in a web server (automatically reloading)
  grunt.registerTask('test:web', ['env:dev', 'watch:web_testing']);

  // run tests locally
  grunt.registerTask('test:local', ['env:dev', 'watch:local_testing']);

  grunt.registerTask('bower-setup', ['bower', 'exec:convert_dsjslib']);

};
