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

define(["config", 'module'], function(config, module) {

  require(["router",
      'js/components/application',
      'js/mixins/discovery_bootstrap'
    ],
    function(Router,
      Application,
      DiscoveryBootstrap
      ) {


      var app = new (Application.extend(DiscoveryBootstrap))();

      // load the objects/widgets/modules (using discovery.config.js)
      var defer = app.loadModules(module.config());

      // after they are loaded; we'll kick off the application
      defer.done(function() {

        // this will activate all loaded modules
        app.activate();

        // set some important urls, parameters before doing anything
        app.configure();

        var bootPromise = app.bootstrap();

        bootPromise.done(function (data) {

          // set the API key
          if (data.access_token) {
            var api = app.getBeeHive().getService('Api');
            api.access_token = data.token_type + ':' + data.access_token;
            api.refresh_token = data.refresh_token;
            api.expires_in = data.expires_in;
          }

          app.start(Router);
        }).fail(function () {
          app.redirect('/505.html');
        });

      }).fail(function() {
        // if we failed loading, retry *once again* (and give up eventually)
        app.reload('/404.html');
      });

    });
});


