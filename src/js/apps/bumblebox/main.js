/**
 * Bumblebox application: main bootstrapping routine
 *
 * Here we will bring up to life the bumblebox application,
 * all configuration is provided through the discovery.config.js
 *
 * Inside the config, there are sections for:
 *
 *  - where to find js libraries
 *  - which widgets to load (for this application)
 *  - which environmental variables are used
 *        (and how to bootstrap run-time values)
 *
 */

define([
  'module',
],
  function(
    module
  ) {
    require([
      'router',
      'js/components/application',
      'js/apps/bumblebox/bootstrap',
      'dynamic_config',
      'es5-shim'
    ],
    function(
      Router,
      Application,
      AppBootstrap,
      DynamicConfig
    ) {
      Application.prototype.shim();

      // at the beginning, we don't know anything about ourselves...
      var debug = window.location.href.indexOf('debug=true') > -1;

      // app object will load everything
      var app = new (Application.extend(AppBootstrap))({debug: debug, timeout: 30000});

      app.bootstrap(DynamicConfig)
        .done(function(loadedConfig) {
          var config = app.onBootstrap(module.config(), loadedConfig);

          // load the objects/widgets/modules
          app.loadModules(config)
            .done(function() {
              // this will activate all loaded modules
              app.activate();

              var pubsub = app.getService('PubSub');
              pubsub.publish(pubsub.getCurrentPubSubKey(), pubsub.APP_LOADED);

              // set some important urls, parameters before doing anything
              app.configure(loadedConfig);
              pubsub.publish(pubsub.getCurrentPubSubKey(), pubsub.APP_BOOTSTRAPPED);

              pubsub.publish(pubsub.getCurrentPubSubKey(), pubsub.APP_STARTING);
              app.start(Router);
              pubsub.publish(pubsub.getCurrentPubSubKey(), pubsub.APP_STARTED);

              var dynConf = app.getObject('DynamicConfig');
              if (dynConf && dynConf.debugExportBBB) {
                console.log('Exposing Bumblebee as global object: window.bbb');
                window.bbb = app;
              }
            })
            .fail(function(err) {
              if (debug) {
                // so error messages remain in the console
                return;
              }
              console.error('Failed to load the application (stage: loading modules)', err);
            });
        })
        .fail(function(err) {
          console.error('Failed to load the application (stage: bootstrap-config)', err);
        });

    });
  });
