// Application specific config (for require.js) it is also used
// by Grunt when we build the app from command line.


require.config({
    
  // Initialize the application with the main application file or if we run
  // as a test, then load the test unittests
  deps: window.mocha ? [ '../../../../test/mocha/todo.spec' ] : [ 'main' ],
  
  paths: {
    
    // generic paths
    'libs': '../../../libs',
    'js': '../../../js',
    
    // todo app files
    'todo-model': './models/todo',
    'todo-view': './views/todo-view',
    'app-view': './views/app-view',
    'todo-collection': './collections/todos',
    
    // dependencies
    'todo-mvc': '../../../libs/todomvc-common/base',
    'underscore': '../../../libs/lodash/lodash.compat',
    'jquery': '../../../libs/jquery/jquery',
    'backbone': '../../../libs/backbone/backbone',
    'backbone.localStorage': '../../../libs/backbone.localStorage/backbone.localStorage'
  },

  shim: {
    // This is required to ensure Backbone works as expected within the AMD
    // environment.
    'backbone': {
      // These are the two hard dependencies that will be loaded first.
      deps: ['jquery', 'underscore'],

      // This maps the global `Backbone` object to `require('backbone')`.
      exports: 'Backbone'
    }
  }
});
