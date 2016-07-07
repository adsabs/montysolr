module.exports = function(grunt) {
  'use strict';

  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),

    // if you create 'local-config.json' some variables can be overriden there
    local: grunt.file.exists('local-config.json') ? grunt.file.readJSON('local-config.json') : {},

    // Wipe out previous builds and test reporting.
    clean: {
      release: {
        src: ['dist/']
      },
      bower: {
        src: ['./bower_components', 'test/reports']
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
      latest_commit: {
        cmd: 'git rev-parse --short=7 --verify HEAD | cat > git-latest-commit'
      },
      latest_tag: {
        cmd: 'git describe --abbrev=0 | cat > git-latest-release-tag'
      },
      git_describe: {
        cmd: 'git describe | cat > git-describe'
      }
    },

    // Safari browser (v6) gets broken by Ghostery, the only solution that works
    // is to serve google-analytics locally; this is out of necessity!
    curl: {
      'google-analytics': {
        src: 'http://www.google-analytics.com/analytics.js',
        dest: 'src/libs/g.js'
      }
    },

    // Task to minify modules/css; it should run only after files were
    // copied over to the 'dist' folder
    requirejs: {
      waitSeconds: 0,
      baseUrl: 'dist/js', // this is needed just for the 'stupid' list task

      release_concatenated : {
        options: {
          baseUrl: 'dist/',
          wrapShim: true,
          include : (function(){

            var s = grunt.file.read("src/discovery.config.js"),
                require = {config : function(s){return s}},
                bumblebeeConfig = eval(s).config['js/apps/discovery/main'];

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

          }()),
          allowSourceOverwrites: true,
          out: "dist/bumblebee_app.js",
          name: "js/apps/discovery/main",
          keepBuildDir: true,
          mainConfigFile : "dist/discovery.config.js",
          findNestedDependencies: true,
          wrap: true,
          preserveLicenseComments: false,
          generateSourceMaps: false,

          stubModules : ['babel', 'es6'],
          optimize: 'uglify2',

          paths : {
            react : 'https://fb.me/react-15.2.0.min.js',
            'react-dom' : 'https://fb.me/react-dom-15.2.0.min.js'
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
          optimizeCss: "standard"
        }
      }
    },

    //remove unused css rules
    purifycss: {
      options: {},
      target: {
        src: ['dist/bumblebee_app.js'],
        css: ['dist/styles/css/styles.css'],
        dest: 'dist/styles/css/styles.css',
      },
    },

    // add md5 checksums to the distribution files
    'hash_require': {
      /* find js/css files, and add hash (md5 checksum) to them
       */
      js: {
        options: {
          mapping: 'dist/jsmap.json',
          destBasePath: 'dist/',
          srcBasePath: 'dist/',
          flatten:false
        },
        src: ['dist/js/**/*.js']
      },
      css: {
        options: {
          mapping: 'dist/cssmap.json',
          destBasePath: 'dist/',
          srcBasePath: 'dist/',
          flatten:false
        },
        src: ['dist/styles/css/styles.css']
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
        API_ENDPOINT: '<%= local.api_endpoint || "http://localhost:5000/api/1" %>',
        ORCID_OAUTH_CLIENT_ID: '<%= local.orcid_oauth_cliend_id || "" %>',
        ORCID_OAUTH_CLIENT_SECRET:'<%= local.orcid_oauth_client_secret || "" %>',
        ORCID_API_ENDPOINT :'<%= local.orcid_api_endpoint || "" %>'
      },
      dev: {
        HOMEDIR: 'src'
      },
      release: {
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
      release: {
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

      release: {
        files: ['./dist/*'],
        tasks: ['env:release', 'express:release']
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
        files: ['./src/styles/sass/ads-sass/*.scss'], // which files to watch
        tasks: ['sass', 'autoprefixer', 'express:dev', 'watch:styles'],
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
            //'http://localhost:<%= local.port || 8000 %>/test/mocha/tests.html?bbbSuite=core-suite',
            //'http://localhost:<%= local.port || 8000 %>/test/mocha/tests.html?bbbSuite=ui-suite',
            //'http://localhost:<%= local.port || 8000 %>/test/mocha/tests.html?bbbSuite=qb-suite'
            'http://localhost:<%= local.port || 8000 %>/test/mocha/tests.html?bbbSuite=discovery-suite'
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
    // 'src' top level)
    copy: {

      libraries: {
        files: [
          {
            src: 'bower_components/lodash/dist/*',
            dest: 'src/libs/lodash/',
            expand: true,
            flatten: true
          },
          {
            src: 'bower_components/marionette/lib/*',
            dest: 'src/libs/marionette/',
            expand: true,
            flatten: true
          },
          {
            src: 'bower_components/backbone.babysitter/lib/*',
            dest: 'src/libs/backbone.babysitter/',
            expand: true,
            flatten: true
          },
          {
            src: ['bower_components/bootstrap/dist/css/*', 'bower_components/bootstrap/dist/fonts/*', 'bower_components/bootstrap/dist/js/*'],
            dest: 'src/libs/bootstrap/',
            expand: true,
            flatten: true
          },
          {
            src: ['bower_components/d3/*.js'],
            dest: 'src/libs/d3/',
            expand: true,
            flatten: true
          },
          {
            src: ['bower_components/nvd3/*.js'],
            dest: 'src/libs/nvd3/',
            expand: true,
            flatten: true
          },
          {
            src: ['bower_components/requirejs-plugins/src/*.js'],
            dest: 'src/libs/requirejs-plugins/',
            expand: true,
            flatten: true
          },

          {
            src: ['bower_components/fontawesome/scss/*'],
            dest: 'src/libs/fontawesome/scss/',
            expand: true,
            flatten: true
          },
          {
            src: ['bower_components/fontawesome/fonts/*'],
            dest: 'src/libs/fontawesome/fonts',
            expand: true,
            flatten: true
          },
          {
            src: ['bower_components/jqueryui/themes/smoothness/jquery-ui.min.css'],
            dest: 'src/libs/jqueryui/',
            expand: true,
            flatten: true
          },
          {
            cwd: 'bower_components/bootstrap-sass/assets/stylesheets/',
            src: ['*', '**'],
            expand: true
          },
          {
            src: ['bower_components/react/*.js'],
            dest: 'src/libs/react/',
            expand: true,
            flatten: true
          },
          {
            src: ['bower_components/requirejs-babel/*.js'],
            dest: 'src/libs/requirejs-babel-plugin/',
            expand: true,
            flatten: true
          },


        ]
      },

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
      },
      keep_original: {
        files: [{
          expand: true,
          src: ['./dist/index.html', 'dist/discovery.config.js'],
          dest: 'dist/',
          rename: function(dest, src) {
            var x = src.split('.');
            return x.slice(0, x.length-1).join('.') + '.original.' + x[x.length-1];
          }
        }]
      },
      foo: {
        files: [{
          src: ['./src/js/components/**/*.js'],
          dest: 'dist/',
          expand: true,
          rename: function(dest, src) {
            return dest + src.replace('/src/', '/');
          }
        }]
      },

      //give the concatenated file a cache busting hash
      bumblebee_app : {
        files : [{
          src: ["dist/bumblebee_app.js"],
          dest: "dist/",
          expand: true,
          rename : function(dest, src){

            var gitDescribe = grunt.file.read('git-describe').trim();

            // find out what version of bbb we are going to assemble
            var tagInfo = gitDescribe.split('-');
            var version;
            if (tagInfo.length == 1) {
              version = tagInfo[0]; // the latest tag is also the latest commit (we'll use tagname v1.x.x)
            }
            else {
              version = tagInfo[2]; // use commit number instead of a tag
              return "dist/bumblebee_app." + version + ".js";
            }

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

    sass: {
        options: {
          sourceMap: true
        },
        dist: {
          files: {
            'src/styles/css/styles.css' : 'src/styles/sass/manifest.scss'
          }
        }
      },

    /* for changing the name of the data-main file in dist/index */

    'string-replace': {
      dist: {
        files: [{
          src: 'dist/index.html',
          dest: 'dist/index.html'
        }],
        options: {
          replacements: [{
            pattern: 'data-main="./discovery.config"',
            replacement: 'data-main="./bumblebee_app.js"'
          }]
        }
      }
    },

    autoprefixer:{
      dist:{
        files:{
          'src/styles/css/styles.css' : 'src/styles/css/styles.css'
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
          globalThreshold : 75,
          log : true,
          logErrors: true,
          moduleThreshold : 80,
          modulePattern : "../../js/(.*)",
          customModuleThreshold: {

            "components/api_query_updater.js" : 78,
            'widgets/facet/graph-facet/h_index_graph.js' : 38,

            'widgets/facet/graph-facet/widget.js' : 64,
            'widgets/facet/graph-facet/year_graph.js' : 58,
            'widgets/facet/graph-facet/h_index_graph.js ' : 38,
            "apps/discovery/navigator.js": 30,
            "apps/discovery/router.js": 37,
            "wraps/graph_tabs.js":5,
            "widgets/facet/graph-facet/base_graph.js":8,
            "widgets/export/widget.js":23,
            "mixins/widget_mixin_method.js":37,
            "page_managers/three_column_view.js":60,
            "mixins/widget_utility.js":40,
            "components/query_builder/rules_translator.js":45,
            "components/csrf_manager.js": 25,
            "widgets/base/tree_view.js":50,
            "widgets/list_of_things/item_view.js":50,
            "widgets/base/base_widget.js":51,
            "widgets/breadcrumb/widget.js":55,
            "components/navigator.js":60,
            "mixins/dependon.js":61,
            "widgets/query_info/query_info_widget.js":46,
            "widgets/resources/widget.js":72,
            "wraps/table_of_contents.js":73,
            "bugutils/minimal_pubsub.js":74,
            "components/history_manager.js":75,
            "components/api_feedback.js":77,
            "components/transition.js":77,
            "widgets/dropdown-menu/widget.js":78,
            "wraps/paper_network.js": 77, // some tests don't run properly in phantomjs,
            "wraps/paper_export.js": 68,
            "widgets/recommender/widget.js" : 65,
            "wraps/discovery_mediator.js": 5, // these two guys are complex to test (but i've already started)
            "mixins/feedback_handling.js": 35,
            "mixins/discovery_bootstrap.js": 1,
            "widgets/navbar/widget.js": 53,
            "widgets/success/view.js": 60,
            "components/library_controller.js" : 74,
            "widgets/wordcloud/widget.js": 78,
            "components/analytics.js": 71,
            "wraps/landing_page_manager/landing_page_manager" : 48,
            "widgets/libraries_all/views/view_all_libraries.js" : 78
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
    },

    'saucelabs-mocha': {
      all: {
        options: {
          username: '<%= local.sauce_username || process.env.SAUCE_USERNAME %>',
          key: '<%= local.sauce_access_key || process.env.SAUCE_ACCESS_KEY %>',
          urls: ['http://localhost:<%= local.port || 8000 %>/test/' + (grunt.option('testname') || 'mocha/tests.html?bbbSuite=core-suite')],
          tunnelTimeout: 30,
          "tunnel-identifier": process.env.TRAVIS_JOB_NUMBER,
          build: process.env.TRAVIS_JOB_ID,
          concurrency: 5,
          throttled: 5,
          maxRetries: 1,

          // the logic here is to test browser versions
          // bbb does not depend on OS specific API's
          // but it could still happen that certain features
          // are not working in the same browser (different OS's)
          // if we discover that, we should add that pair here
          // Otherwise, we are testing against the 'worst'
          // OS - which may even by Linux in some cases; e.g.
          // (for drawing, flash playback etc)

          browsers: [
            {
              browserName: 'internet explorer',
              platform: 'Windows 8.1',
              version: '11.0'
            },

            {
              browserName: 'safari',
              platform: 'OS X 10.6',
              version: '5.1'
            },
            {
              browserName: "android",
              platform: "linux",
              deviceName: 'Samsung Galaxy S4 Emulator',
              version: '4.3'
            },
            {
              browserName: "android",
              platform: "linux",
              deviceName: 'Google Nexus 7 HD Emulator',
              version: '4.2'
            },
            {
              browserName: "firefox",
              platform: "linux",
              version: '39'
            },
            {
              browserName: "firefox",
              platform: "linux",
              version: '38'
            },
            {
              browserName: "firefox",
              platform: "linux",
              version: '34'
            },
            {
              browserName: "chrome",
              platform: "linux",
              version: '43'
            },
            {
              browserName: "chrome",
              platform: "linux",
              version: '42'
            },
            {
              browserName: 'iphone',
              platform: 'OS X 10.9',
              version: '8.0',
              deviceName: 'iPhone Simulator'
            },
            {
              browserName: 'iphone',
              platform: 'OS X 10.9',
              version: '7.0',
              deviceName: 'iPhone Simulator'
            },

            {
              browserName: 'iphone',
              deviceName: 'iPad Simulator',
              deviceOrientation: 'portrait',
              platform: 'OS X 10.9',
              version: '8.0'
            },
            {
              browserName: 'iphone',
              deviceName: 'iPad Simulator',
              deviceOrientation: 'portrait',
              platform: 'OS X 10.9',
              version: '7.0'
            }
          ],
          testname: 'bbb',
          tags: ['<%= grunt.file.read("git-describe").trim() %>']
        }
      }
    },

    intern: {
      local: {
        options: {
          runType: 'runner', // defaults to 'client'
          config: 'test/intern-local',
          reporters: [ 'Console', 'Lcov' ]
        }
      },
      remote: {
        options: {
          runType: 'runner', // defaults to 'client'
          config: 'test/intern-remote',
          reporters: [ 'Console', 'Lcov' ]
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


  // Grunt contribution tasks.
  // so you dont have to do grunt.loadNpmTasks for each one
  require('load-grunt-tasks')(grunt);

  // Create an aliased test task.
  grunt.registerTask('setup', 'Sets up the development environment',
    ['install-dependencies',
      'bower-setup',
      '_conditional_copy',
      'copy:libraries',
      'sass',
      'curl:google-analytics'
    ]);

  grunt.registerTask('_conditional_copy', function() {
    if (!grunt.file.exists('src/discovery.vars.js')) {
      grunt.task.run(['copy:discovery_vars']);
      grunt.log.write('Please modify src/discovery.vars.js if necessary...').ok();
    }
  });

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

    // override certain libraries (they are always fetched from CDN and other locations)
    paths['jquery'] = '//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min'; //code.jquery.com/jquery-2.0.3.min.js';
    paths['jquery-ui'] = '//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min'; //code.jquery.com/ui/1.10.4/jquery-ui.min.js';
    paths['bootstrap'] = '//maxcdn.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min';
    paths['underscore'] = 'libs/lodash/lodash.compat.min';
    //paths['backbone'] = 'libs/backbone/backbone-min';
    paths['marionette'] = 'libs/marionette/backbone.marionette.min';
    paths['backbone.wreqr'] = 'libs/backbone.wreqr/lib/backbone.wreqr.min';
    paths['backbone.babysitter'] = 'libs/backbone.babysitter/backbone.babysitter.min';
    paths['d3'] = 'libs/d3/d3.min';
    paths['nvd3'] = 'libs/nvd3/nv.d3.min';
    paths['persist-js'] = 'libs/persist-js/persist-all-min';

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


  // When running the default Grunt command, just setup the environment
  grunt.registerTask('default', ['setup' ]);

  // starts a web server (automatically reloading)
  grunt.registerTask('server', ['env:dev',  "sass", "autoprefixer",  'express:dev', 'concurrent:serverTasks']);
  grunt.registerTask('server:watch', ['env:dev',  "sass", "autoprefixer",  'express:dev', 'watch:server']);
  grunt.registerTask('server:release', ['env:release',  'express:release', 'watch:release']);

  // runs tests in a web server (automatically reloading)
  grunt.registerTask('test:web', ['env:dev', 'watch:web_testing']);
  grunt.registerTask('test:once', ['env:dev', 'express:dev', 'mocha_phantomjs:web_testing']);
  grunt.registerTask('test:sandbox', ['env:dev', 'watch:sandbox_testing']);
  grunt.registerTask('test:release', ['env:release', 'express:dev', 'mocha_phantomjs:web_testing']);

  // runs tests (only once)
  grunt.registerTask('test', ['env:dev', 'express:dev', 'mocha_phantomjs:full_testing']);

  // runs test server only
  grunt.registerTask('test:server', ['env:dev', 'express:dev', 'watch:server']);
  // run tests locally
  grunt.registerTask('test:local', ['env:dev', 'watch:local_testing']);
  grunt.registerTask('bower-setup', ['clean:bower', 'bower', 'exec:convert_dsjslib']);
  grunt.registerTask('coverage', ['env:dev', 'express:dev', 'blanket_mocha:full']);

  grunt.registerTask('release',
    [ 'setup',
      'clean:release', 'copy:release',
      'exec:git_describe',
      'string-replace:dist',
      'requirejs:release_concatenated','requirejs:release_css',
      'hash_require:js', 'hash_require:css',
      'copy:keep_original', 'copy:bumblebee_app',
      'assemble'
  ]);

  grunt.registerTask("sauce", ['env:dev',  "sass", "autoprefixer", "exec:git_describe", 'express:dev', "saucelabs-mocha"]);

  grunt.registerTask("testfunc", ["testfunc:local"]);
  grunt.registerTask("testfunc:local", ['env:dev',  "sass", "autoprefixer", "exec:git_describe", 'express:dev', "intern:local"]);
  grunt.registerTask("testfunc:remote", ['env:dev',  "sass", "autoprefixer", "exec:git_describe", 'express:dev', "intern:remote"]);

};
