
/*
 * This module contains a set of utilities to bootstrap Discovery app
 */
define([
  'underscore',
  'jquery',
  'backbone',
  'js/components/api_query',
  'js/components/api_request',
  'js/components/pubsub_events'
],
  function(
    _,
    $,
    Backbone,
    ApiQuery,
    ApiRequest,
    PubSubEvents
  ) {
    var Mixin = {

      /**
       * Happens first, when the application starts (but before it starts
       * loading modules). Here is the time to retrieve whatever configuration
       * and/or files that should be available to the app instance
       */
      bootstrap: function(conf) {
        conf = conf || {};
        var defer = $.Deferred();

        // load configuration from remote endpoints
        if (conf.bootstrapUrls) {
          var pendingReqs = conf.bootstrapUrls.length;
          var retVal = {};

          // harvest information from the remote urls and merge it into one object
          var reqs = [];
          _.each(conf.bootstrapUrls, function(url) {
            if (! url) return;
            if (url.indexOf('.json') > -1) {
              var jqXhr = $.ajax({
                type: 'GET',
                url: url,
                dataType: url.indexOf('json') > -1 ? 'json' : 'script',
                contentType: 'application/x-www-form-urlencoded',
                cache: false,
                timeout: 3000,
                success: function (data) {
                  if (_.isString(data)) {
                    var v = eval(data);
                    if (_.isFunction(v)) {
                      data = v();
                    }
                    else {
                      data = v;
                    }
                  }
                  _.extend(retVal, data);
                }});
            }
            else {
              jqXhr = $.Deferred();
              require([url],
                function(data) {
                  _.extend(retVal, data);
                  jqXhr.resolve();
                },
                function() {
                  jqXhr.resolve();
                }
              )
            }
            reqs.push(jqXhr);
          });
          if (reqs.length > 0 ) {
            $.when.apply($, reqs).then(
              function () {
                defer.resolve(retVal)
              },
              function () {
                defer.reject(arguments)
              }
            );
          }
          else {
            defer.resolve({});
          }

        }
        else {
          defer.resolve({});
        }
        return defer.promise();
      },

      /**
       * Called after the dynamic config was retrieved. It has to return
       * a configuration to use for loading the application.
       *
       * @param app_config
       * @param dynamic_config
       * @returns {*}
       */
      onBootstrap: function (app_config, dynamic_config) {
        // this is little bit of a (necessary) hack, we'll
        // update the configuration of the requirejs's
        var rConfig = null;
        for (var k in requirejs.s.contexts) {
          var kontext = requirejs.s.contexts[k];
          if (kontext.config && kontext.config.config && kontext.config.config['js/apps/bumblebox/main']) {
            // ignore this context if it's used by some other app already
            if (kontext.config.config['js/apps/bumblebox/main']['bootstrap'])
              return;
            kontext.config.config['js/apps/bumblebox/main']['bootstrap'] = 'placeholder';
            rConfig = kontext.config;
          }
        }
        var enhanceConfig = function(targetConfig, dynamic_config) {
          _.each(dynamic_config, function(value, key, obj) {
            if (targetConfig[key]) {
              var target = rConfig[key];
              _.each(value, function(value, key, obj) {
                target[key] = _.defaults(value, target[key]); // use the new values as defaults
              });
            }
          })
        }
        if (rConfig) {
          enhanceConfig(rConfig, dynamic_config);
        }

        if (dynamic_config.TargetWidget) {
          app_config.widgets.TargetWidget = dynamic_config.TargetWidget;
        }
        return app_config;
      },

      /**
       * Called after the modules/controllers/libraries were loaded and
       * activated. Here you can configure the application instance.
       *
       * @param loadedConfig
       */
      configure: function(loadedConfig) {
        var conf = this.getObject('DynamicConfig') || {};
        conf = _.extend(conf, loadedConfig);

        if (conf) {
          var beehive = this.getBeeHive();
          var api = beehive.getService('Api');

          if (conf.root) {
            api.url = conf.root + '/' + api.url;
            this.root = conf.root;
          }
          if (conf.debug !== undefined) {
            beehive.debug = conf.debug;
            this.getController('QueryMediator').debug = conf.debug;
          }

          if (conf.apiRoot) {
            api.url = conf.apiRoot;
          }
          this.bootstrapUrls = conf.bootstrapUrls;
        }

        this.getBeeHive().getService('Api').setVals({
          access_token : 'Bearer:' + conf.access_token,
          refresh_token : conf.refresh_token, // will probably be null....
          expires_in : conf.expires_in,
          clientVersion: null, // to avoid sending the headers (for now)
          defaultTimeoutInMs: conf.defaultTimeoutInMs || 15000
        });

        // set the API key and other data from bootstrap
        if (conf.access_token) {
          console.warn('Redefining access_token: ' + conf.access_token);
        }
        else {
          console.warn("bootstrap didn't provide access_token!");
        }
      },

      reload: function(endPage) {
        throw new Error('Should never be called by an embedded app.');
      },

      redirect: function(endPage) {
        throw new Error('Should never be called by an embedded app.');
      },

      start: function(Router) {
        var app = this;
        var beehive = this.getBeeHive();
        var api = beehive.getService("Api");
        var conf = this.getObject('DynamicConfig');

        this.getBeeHive().getObject("AppStorage").setConfig(conf);

        var complain = function(x) {
          throw new Error("Ooops. Check your config! There is no " + x + " component @#!")
        };

        var navigator = app.getBeeHive().Services.get('Navigator');
        if (!navigator)
          complain('services.Navigator');

        var masterPageManager = app.getObject('MasterPageManager');
        if (!masterPageManager)
          complain('objects.MasterPageManager');

        // get together all pages and insert widgets there
        masterPageManager.assemble(app);

        // attach the master page to the body
        $(conf.targetElement || 'div#body-template-container').empty().append(masterPageManager.view.el);

        // kick off routing
        app.router = new Router();
        app.router.activate(beehive.getHardenedInstance());

        // get ready to handle navigation signals
        navigator.start(this);
        navigator.router = app.router; // this feels hackish

        // Trigger the initial route and enable HTML5 History API support
        Backbone.history.start(conf ? conf.routerConf : {});
      }
    };

    return Mixin;
  });
