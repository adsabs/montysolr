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
      'es5-shim',
       'underscore'
    ],
    function(
      Router,
      Application,
      AppBootstrap,
      DynamicConfig,
      Es5Shim,
      _
    ) {
      Application.prototype.shim();

      // load the urls of dynamic config from the script element
      _.each(document.getElementsByTagName('script'), function(scriptNode) {
        if (scriptNode.hasAttribute('data-main') &&
          (scriptNode.getAttribute('data-main') || '').indexOf('embed.config') > -1 &&
          !scriptNode.hasAttribute('data-bbb-inuse')) {
          scriptNode.setAttribute('data-bbb-inuse', true);
          var c = scriptNode.getAttribute('data-load');
          if (c) {
            var urls = c.split(',');
            DynamicConfig.bootstrapUrls = _.union(DynamicConfig.bootstrapUrls || [], urls);
          }
        }
      });

      // at the beginning, we don't know anything about ourselves...
      var debug = window.location.href.indexOf('debug=true') > -1;

      // app object will load everything
      var app = new (Application.extend(AppBootstrap))({debug: debug, timeout: 30000});

      app.bootstrap(DynamicConfig)
        .done(function(loadedConfig) {
          var loadConfig = app.onBootstrap(module.config(), loadedConfig);

          // load the objects/widgets/modules
          app.loadModules(loadConfig)
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
