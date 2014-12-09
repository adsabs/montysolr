module.exports = function(grunt) {
  'use strict';

  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),

    // if you create 'local-config.json' some variables can be overriden there
    local: grunt.file.readJSON('local-config.json'),

    // Wipe out previous builds and test reporting.
    clean: {
      dist: {
        src: ['dist/', 'test/reports']
      },
      bower: {
        src: ['./bower_components']
      }
    },


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
    jshint: {
      uses_defaults: ['src/js/**/*.js', 'test/mocha/**/*.js'],

      // config used by watch tasks, the src gets set by 'watch'
      individual: {
        src: [],
        options: {
          
        }
      },
      ignore_semicolons: {
        options: {
          '-W033': true
        },
        files: {
          src: ['src/js/**/*.js', 'test/mocha/**/*.js']
        }
      }

    },


    exec: {
      // this is necessary to make the library AMD compatible
      convert_dsjslib: {
        cmd: 'node node_modules/requirejs/bin/r.js -convert src/libs/dsjslib src/libs/dsjslib'
      },
      //this has to go into css folder
      move_jqueryuicss : {
        cmd : 'cp -r bower_components/jqueryui/themes/smoothness/ src/styles/css/'
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
        SOLR_ENDPOINT: '<%= local.solr_endpoint || "http://localhost:9000/solr/select" %>',
        API_ENDPOINT: '<%= local.api_endpoint || "http://localhost:5000/api/1" %>'
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
        files: ['./Gruntfile', './src/js/**/*.js', './src/*.js', './src/*.html', './server.js', './src/styles/css/*.css'],
        tasks: ['env:dev', 'express:dev']
      },

      local_testing: {
        files: ['./Gruntfile', './src/js/**/*.js', './test/mocha/**/*.js', './src/*.js', './src/*.html'],
        tasks: ['mocha_phantomjs:local_testing', 'watch:local_testing']
      },

      web_testing: {
        files: ['./Gruntfile', './src/js/**/*.js', './test/mocha/**/*.js', './src/*.js', './src/*.html'],
        tasks: ['express:dev', 'mocha_phantomjs:web_testing', 'watch:web_testing']
      },

      sandbox_testing: {
        files: ['./Gruntfile', './src/js/**/*.js', './test/mocha/**/*.js', './src/*.js', './src/*.html'],
        tasks: ['express:dev', 'mocha_phantomjs:sandbox_testing', 'watch:sandbox_testing']
      },

      styles: {
        files: ['./src/styles/less/**/*.less'], // which files to watch
        tasks: ['less', 'express:dev', 'watch:styles'],
        options: {
          livereload: true
        }
      }
    },

    concurrent: {
        serverTasks: ['watch:server', 'watch:styles']
    },
    //**
    //* PhantomJS is a headless browser that runs our tests, by default it runs core-suite
    //* if you need to change the tested suite: grunt --testname='mocha/tests.html?bbbSuite=foo'
    //**
    mocha_phantomjs: {
      options: {
        //'reporter': 'progress',
        'output': 'test/reports/' + (grunt.option('testname') || 'mocha/discovery')
      },

      local_testing: ['test/' + (grunt.option('testname') || 'mocha/tests.html')],

      web_testing: {
        options: {
          urls: [
              'http://localhost:<%= local.port || 8000 %>/test/' + (grunt.option('testname') || 'mocha/tests.html?bbbSuite=discovery-suite')
          ]
        }
      },

      //**
      //* Another way: grunt test:web --testname='mocha/tests.html?bbbSuite=ui-suite'
      //**
      sandbox_testing: {
        options: {
          urls: [
            'http://localhost:<%= local.port || 8000 %>/test/mocha/tests.html?bbbSuite=sandbox'
          ]
        }
      },

      full_testing: {
        options: {
          output: null,
          urls: [
            'http://localhost:<%= local.port || 8000 %>/test/mocha/tests.html?bbbSuite=core-suite',
            'http://localhost:<%= local.port || 8000 %>/test/mocha/tests.html?bbbSuite=ui-suite',
            'http://localhost:<%= local.port || 8000 %>/test/mocha/tests.html?bbbSuite=qb-suite'
            //'http://localhost:<%= local.port || 8000 %>/test/mocha/tests.html?bbbSuite=discovery-suite'
          ]
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
      },
      discovery_vars: {
          src: 'src/discovery.vars.js.default',
          dest: 'src/discovery.vars.js'
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
        options : {
           compress: true,
          yuicompress: true,
          optimization: 2
        },
        files: {
          'src/styles/css/styles.css': 'src/styles/less/manifest.less'
        }
      }
    },

    blanket_mocha : {
      full: {
        options : {
          urls: [
            'http://localhost:<%= local.port || 8000 %>/test/mocha/coverage.html?bbbSuite=discovery-suite'
          ],
          threshold : 0,
          globalThreshold : 74,
          log : true,
          logErrors: true,
          moduleThreshold : 80,
          modulePattern : "../../src/js/(.*)",
          customModuleThreshold: {
            "widgets/facet/graph-facet/h_index_graph.js":2,
            "widgets/facet/graph-facet/year_graph.js":2,
            "wraps/graph_tabs.js":5,
            "widgets/facet/graph-facet/base_graph.js":8,
            "wraps/author_facet.js":13,
            "widgets/export/widget.js":23,
            "widgets/facet/collection.js":33,
            "mixins/widget_mixin_method.js":37,
            "page_managers/three_column_view.js":38,
            "mixins/widget_utility.js":40,
            "components/query_builder/rules_translator.js":45,
            "widgets/base/tree_view.js":50,
            "widgets/facet/factory.js":50,
            "widgets/list_of_things/item_view.js":50,
            "widgets/base/base_widget.js":51,
            "widgets/breadcrumb/widget.js":55,
            "components/navigator.js":58,
            "widgets/abstract/widget.js":58,
            "widgets/network_vis/network_widget.js":60,
            "mixins/dependon.js":61,
            "widgets/facet/tree_view.js":62,
            "widgets/search_bar/search_bar_widget.js":65,
            "widgets/facet/item_view.js":71,
            "widgets/resources/widget.js":72,
            "wraps/table_of_contents.js":73,
            "bugutils/minimal_pubsub.js":74,
            "components/history_manager.js":75,
            "services/api.js":76,
            "components/api_feedback.js":77,
            "components/transition.js":77,
            "widgets/dropdown-menu/widget.js":78,
            "widgets/list_of_things/paginated_view.js":78,
            "wraps/paper_network.js": 74 // some tests don't run properly in phantomjs
          }
        }
      },

      test: {
        options : {
          urls: [
            'http://localhost:<%= local.port || 8000 %>/test/mocha/coverage.html?bbbSuite=core-suite'
          ],
          threshold : 0,
          globalThreshold : 63,
          log : true,
          logErrors: true,
          moduleThreshold : 0,
          modulePattern : "../../src/js/(.*)",
          customThreshold: {

          }
          //reporter: 'JSONCov',
          //dest: './coverage/output'
          }
      }
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
  grunt.loadNpmTasks('grunt-mocha-phantomjs');
  grunt.loadNpmTasks('grunt-processhtml');
  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-bower-task');
  grunt.loadNpmTasks('grunt-install-dependencies');
  grunt.loadNpmTasks('grunt-concurrent');
  grunt.loadNpmTasks('grunt-mocha');
  grunt.loadNpmTasks("grunt-blanket-mocha");

  // Create an aliased test task.
  grunt.registerTask('setup', 'Sets up the development environment',
    ['install-dependencies', 'bower-setup', 'less', '_conditional_copy']);

  grunt.registerTask('_conditional_copy', function() {
    if (!grunt.file.exists('src/discovery.vars.js')) {
      grunt.task.run(['copy:discovery_vars']);
      grunt.log.write('Please modify src/discovery.vars.js if necessary...').ok();
    }
  });


  // When running the default Grunt command, just lint the code.
  grunt.registerTask('default', [
    'env:dev',
    'clean:dist',
    'jshint',
    'copy',
    'processhtml',
    'requirejs',
    'compress'
    //'cssmin',
  ]);

  // starts a web server (automatically reloading)
  grunt.registerTask('server', ['env:dev',  "less", 'express:dev', 'concurrent:serverTasks']);

  // runs tests in a web server (automatically reloading)
  grunt.registerTask('test:web', ['env:dev', 'watch:web_testing']);
  grunt.registerTask('test:once', ['env:dev', 'express:dev', 'mocha_phantomjs:web_testing']);
  grunt.registerTask('test:sandbox', ['env:dev', 'watch:sandbox_testing']);

  // runs tests (only once)
  grunt.registerTask('test', ['env:dev', 'express:dev', 'mocha_phantomjs:full_testing']);

  // run tests locally
  grunt.registerTask('test:local', ['env:dev', 'watch:local_testing']);

  grunt.registerTask('bower-setup', ['clean:bower', 'bower', 'exec:convert_dsjslib', 'exec:move_jqueryuicss']);
  grunt.registerTask('coverage', ['env:dev', 'express:dev', 'blanket_mocha:full']);

};
