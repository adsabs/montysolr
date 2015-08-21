
/*
 * This module contains a set of utilities to bootstrap Discovery app
 */
define([
    'underscore',
    'backbone',
    'js/components/api_query',
    'js/components/api_request',
    'js/components/pubsub_events',
    'hbs'
    ],
  function(
    _,
    Backbone,
    ApiQuery,
    ApiRequest,
    PubSubEvents,
    HandleBars) {

  var Mixin = {

    configure: function() {

      var conf = this.getObject('DynamicConfig');

      if (conf) {

        var beehive = this.getBeeHive();
        var api = beehive.getService('Api');

        if (conf.root) {
          api.url = conf.root + "/" + api.url;
          this.root = conf.root;
        }
        if (conf.debug !== undefined) {
          beehive.debug = conf.debug;
          this.getController('QueryMediator').debug = conf.debug;
        }

        if (conf.apiRoot) {
          api.url = conf.apiRoot;
        }

        var orcidApi = beehive.getService('OrcidApi');

        if (conf.orcidProxy){
          orcidApi.orcidProxyUri = location.origin + conf.orcidProxy;
        }

        this.bootstrapUrls = conf.bootstrapUrls;

        if (conf.useCache) {
          this.triggerMethodOnAll('activateCache');
        }

      }
    },

    bootstrap: function() {
      // XXX:rca - solve this better, through config
      var beehive = this.getBeeHive();
      var dynConf = this.getObject('DynamicConfig');

      var defer = $.Deferred();

      // this is the application dynamic config
      var api = this.getBeeHive().getService('Api');

      // load configuration from remote endpoints
      if (this.bootstrapUrls) {

        var pendingReqs = this.bootstrapUrls.length;
        var retVal = {};

        // harvest information from the remote urls and merge it into one object
        var opts = {
          done: function (data) {
            pendingReqs--;
            _.extend(retVal, data);
            if (pendingReqs <= 0) defer.resolve(retVal);
          },
          fail: function () {
            pendingReqs--;
            if (pendingReqs <= 0) defer.resolve(retVal);
          },
          type: 'GET'
        };
        var redirect_uri = location.origin + location.pathname;

        _.each(this.bootstrapUrls, function (url) {
          if (url.indexOf('http') > -1) {
            opts.u = url;
            api.request(new ApiRequest({
                query: new ApiQuery({redirect_uri: redirect_uri}),
                target: ''}),
              opts);
          }
          else {
            delete opts.u;
            api.request(new ApiRequest({
                query: new ApiQuery({redirect_uri: redirect_uri}),
                target: url}),
              opts);
          }
        });

        setTimeout(function() {
            if (defer.state() == 'resolved')
              return;
            defer.reject();
          },
          3000);
      }
      else {
        setTimeout(function() {
          defer.resolve({}),
            1
        });
      }
      return defer;
    },

    /**
     * Reload the application - by simply changing the URL (append bbbRedirect=1)
     * If the url already contains 'bbbRedirect', redirect to the error page.
     * @param errorPage
     */
    reload: function(endPage) {
      if (location.search.indexOf('debug') > -1) {
        console.warn('Debug stop, normally would reload to: ' + endPage);
        return; // do nothing
      }

      if (location.search && location.search.indexOf('bbbRedirect=1') > -1) {
        return this.redirect(endPage);
      }
      location.search = location.search ? location.search + '&bbbRedirect=1' : 'bbbRedirect=1';
    },

    redirect: function(endPage) {
      if (this.router) {
        location.pathname = this.router.root + endPage;
      }
      // let's replace the last element from pathname - this code will run only when
      // router is not yet available; therefore it should hit situations when the app
      // was not loaded (but it is not bulletproof - the urls can vary greatly)
      // TODO: intelligently explore the rigth url (by sending HEAD requests)
      location.href = location.protocol + '//' + location.hostname + ':' + location.port +
        location.pathname.substring(0, location.pathname.lastIndexOf('/')) + '/' + endPage;
    },

    start: function(Router) {
      var app = this;
      var beehive = this.getBeeHive();
      var api = beehive.getService("Api");
      var conf = this.getObject('DynamicConfig');

      // set the config into the appstorage
      // TODO: find a more elegant solution
      this.getBeeHive().getObject("AppStorage").setConfig(conf);

      var complain = function(x) {
        throw new Error("Ooops. Check you config! There is no " + x + " component @#!")
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
      $('div#body-template-container').empty().append(masterPageManager.view.el);


      // kick off routing
      app.router = new Router();
      app.router.activate(beehive.getHardenedInstance());

      // get ready to handle navigation signals
      navigator.start(this);
      navigator.router = app.router; // this feels hackish

      // Trigger the initial route and enable HTML5 History API support
      Backbone.history.start(conf ? conf.routerConf : {});

      $(document).on("scroll", function () {

        if ($("#landing-page-layout").length > 0) {
          return
        }
        //navbar is currently 40 px height
        if ($(window).scrollTop() > 50) {
          $(".s-quick-add").addClass("hidden");
          $(".s-search-bar-full-width-container").addClass("s-search-bar-motion");
        }
        else {
          $(".s-search-bar-full-width-container").removeClass("s-search-bar-motion");
          $(".s-quick-add").removeClass("hidden");
        }
      });
    }

  };

  return Mixin;
});
