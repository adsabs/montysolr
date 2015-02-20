/**
 * Discovery application: main bootstrapping routine
 *
 * Here we will bring up to life the discovery application,
 * all configuration is provided through the discovery.config.js
 *
 * Inside the config, there are sections for:
 *
 *  - where to find js libraries
 *  - which widgets to load (for this application)
 *  - which environmental variables are used
 *        (and how to bootstrap run-time values)
 */

define(['config', 'module', 'analytics'], function(config, module, analytics) {

  require([
      'router',
      'js/components/application',
      'js/mixins/discovery_bootstrap'
    ],
    function(Router,
      Application,
      DiscoveryBootstrap
      ) {

      // at the beginning, we don't know anything about ourselves...
      var debug = window.location.href.indexOf('debug=true') > -1 ? true : false;

      // app object will load everything
      var app = new (Application.extend(DiscoveryBootstrap))({'debug': debug, timeout: 30000});

      // load the objects/widgets/modules (using discovery.config.js)
      var defer = app.loadModules(module.config());

      // after they are loaded; we'll kick off the application
      defer.done(function() {

        // this will activate all loaded modules
        app.activate();

        var pubsub = app.getService('PubSub');
        pubsub.publish(pubsub.getPubSubKey(), pubsub.APP_LOADED);

        // set some important urls, parameters before doing anything
        app.configure();

        app.bootstrap().done(function (data) {

          app.onBootstrap(data);
          pubsub.publish(pubsub.getPubSubKey(), pubsub.APP_BOOTSTRAPPED);

          pubsub.publish(pubsub.getPubSubKey(), pubsub.APP_STARTING);
          app.start(Router);
          pubsub.publish(pubsub.getPubSubKey(), pubsub.APP_STARTED);

          var dynConf = app.getObject('DynamicConfig');
          if (dynConf && dynConf.debugExportBBB) {
            console.log('Exposing Bumblebee as global object: window.bbb');
            window.bbb = app;
          }

        }).fail(function () {
          app.redirect('/500.html');
        });

      }).fail(function() {
        // if we failed loading, retry *once again* (and give up eventually)
        app.reload('/404.html');
      });

    });
});


