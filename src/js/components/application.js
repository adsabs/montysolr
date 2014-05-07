

define(['underscore', 'jquery', 'backbone', 'module', 'js/components/beehive'], function(_, $, Backbone, module, BeeHive) {


  var Application = function(config, options) {
    options || (options = {});
    this.aid = _.uniqueId('application');
    _.extend(this, _.pick(options, ['timeout']));
    this.initialize.apply(this, arguments);

  };

  var Container = function() {
    this.container = {};
  }
  _.extend(Container.prototype, {
    has: function(name) {
      return this.container.hasOwnProperty(name);
    },
    get: function(name) {
      return this.container[name];
    },
    remove: function(name) {
      delete this.container[name];
    },
    add: function(name, obj) {
      this.container[name] = obj;
    }
  });

  _.extend(Application.prototype, {


    initialize: function(config, options) {
      this.__beehive = new BeeHive();
      this.__widgets = new Container();
      this.__plugins = new Container();
    },

    /**
     * Purpose of this call is to load dynamically all modules
     * that you pass in a configuration. We'll load them using
     * requirejs and set them into BeeHive
     *
     * This method returns 'Deferred' object, which tells you
     * whether initialization has finished. You *have to* use it
     * in the following way;
     *
     * app = new Application();
     * defer = app.loadModules(config, options);
     * defer.done(function() {
     *    // .... do something with the application
     * })
     *
     * @param config
     * @param options
     */
    loadModules: function(config, options) {

      var promises = [];
      var self = this;
      var promise;

      var core = config['core'];
      if (core) {
        _.each(['services', 'objects'], function(name) {
          if (core[name]) {
            promise = self._loadModules(name, core[name]);
            if (promise)
              promises.push(promise);
          }
        });
      }

      var plugins = config['plugins'];
      if (plugins) {
        promise = self._loadModules("plugins", plugins);
        if (promise)
          promises.push(promise);
      }

      var widgets = config['widgets'];
      if (widgets) {
        promise = self._loadModules("widgets", widgets);
        if (promise)
          promises.push(promise);
      }

      var bigPromise = $.when.apply($, promises)
        .then(function () {
          _.each(arguments, function (promisedValues, idx) {
            //console.log('results', idx, promisedValues);
            if (_.isArray(promisedValues))
              self._registerLoadedModules.apply(self, promisedValues);
          })
        })
        .fail(function () {
          console.error("Generic error - we were not successul in loading all modules for config", config);
          //throw new Error("We are screwed!"); do not throw errors because then .fail() callbacks cannot be used
        });
        //.done(function() {
        //  console.log('DONE loading', this, config);
        //});

      return bigPromise;
    },

    getBeeHive: function() {
      return this.__beehive;
    },


    _registerLoadedModules: function(section, modules) {
      var beehive = this.getBeeHive();
      var key, module;
      var hasKey, addKey, removeKey, createInstance;
      createInstance = function(module) {return new module()};

      //console.log('registering', section, modules);

      if (section == "services") {
        hasKey = _.bind(beehive.hasService, beehive);
        removeKey = _.bind(beehive.removeService, beehive);
        addKey = _.bind(beehive.addService, beehive);
      }
      else if (section == 'objects') {
        hasKey = _.bind(beehive.hasObject, beehive);
        removeKey = _.bind(beehive.removeObject, beehive);
        addKey = _.bind(beehive.addObject, beehive);
      }
      else if (section == 'widgets') {
        hasKey = _.bind(this.hasWidget, this);
        removeKey = _.bind(function(key) {this.__widgets.remove(key)}, this);
        addKey = _.bind(function(key, module) {this.__widgets.add(key, module)}, this);
      }
      else if (section == 'plugins') {
        hasKey = _.bind(this.hasPlugin, this);
        removeKey = _.bind(function(key) {this.__plugins.remove(key)}, this);
        addKey = _.bind(function(key, module) {this.__plugins.add(key, module)}, this);
      }
      else {
        throw new Error("Unknown section: " + section);
      }

      _.each(_.pairs(modules), function(m) {
        key = m[0], module = m[1];
        if (hasKey(key)) {
          console.warn("Removing (existing) object into [" + section + "]: " + key);
          removeKey(key);
        }
        addKey(key, createInstance(module));
      })
    },

    _checkPrescription: function(modulePrescription) {
      // basic checking
      _.each(_.pairs(modulePrescription), function(module, idx, list) {
        var symbolicName = module[0];
        var impl = module[1];

        if (!_.isString(symbolicName) || !_.isString(impl))
          throw new Error("You are kidding me, the key/implementation must be string values");

      });
    },

    /**
     * Loads modules *one after another* from the following structure
     *
     * {
     *  'Api': 'js/services/api',
     *  'PubSub': 'js/services/pubsub'
     * },
     *
     * Returns Deferred - once that deferred object is resolved
     * all modules have been loaded.
     *
     * @param modulePrescription
     * @private
     */
    _loadModules: function(sectionName, modulePrescription, ignoreErrors) {

      this._checkPrescription(modulePrescription);

      //console.log('_loadModules', sectionName, modulePrescription);

      var ret = {};

      // create the promise object - load the modules asynchronously
      var implNames = _.keys(modulePrescription);
      var impls = _.values(modulePrescription);
      var defer = $.Deferred();

      var callback = function () {
        //console.log('callback', sectionName, arguments)
        var modules = arguments;
        _.each(implNames, function (name, idx, implList) {
          ret[name] = modules[idx];
        });
        defer.resolve(sectionName, ret);
        //console.log('sent results', sectionName, defer.state());
      };

      var errback = function (err) {
        var symbolicName = err.requireModules && err.requireModules[0];
        if (ignoreErrors) {
          console.warn("Error loading impl=" + symbolicName);
          console.warn("Ignoring error");
          return;
        }
        defer.reject();
      };

      // start loading the modules
      //console.log('loading', implNames, impls)
      require(impls, callback, errback);

      return this._setTimeout(defer).promise();
    },

    _setTimeout: function(deferred) {
      setTimeout(function () {
        if (deferred.state() != 'resolved') {
          deferred.reject();
        }
      }, this.timeout || 5000);
      return deferred;
    },

    close: function() {
      this.getBeeHive().close();
    },
    activate: function(options) {
      this.getBeeHive().activate();
    },

    hasWidget: function(name) {
      return this.__widgets.has(name);
    },
    getWidget: function(name) {
      return this.__widgets.get(name);
    },
    hasPlugin: function(name) {
      return this.__plugins.has(name);
    },
    getPlugin: function(name) {
      return this.__plugins.get(name);
    },

    getAllPlugins: function() {
      return _.pairs(this.__plugins.container);
    },
    getAllWidgets: function() {
      return _.pairs(this.__widgets.container);
    }


  });


  // give it subclassing functionality
  Application.extend = Backbone.Model.extend;

  return Application;

});