/**
 * Application object contains methods for asynochronous loading of other modules
 * It will load BeeHive by default, and it recognizes the following types of
 * objects
 *
 *  core:
 *    modules - any module you want to load and give it access to the full
 *              BeeHive (these guys are loaded first)
 *    services - these instances will be inserted into Beehive.Services
 *              (loaded after modules)
 *    objects - these will be inserted into BeeHive.Objects
 *              (loaded after services)
 *
 *  plugins - any object you want to instantiate
 *  widgets - any visual object you want to instantiate
 *
 *
 *  this is the normal workflow
 *
 *  var app = new Application();
 *  var promise = app.loadModules({
 *       core: {
 *         services: {
 *           PubSub: 'js/services/pubsub',
 *           Api: 'js/services/api'
 *         },
 *         modules: {
 *           QueryMediator: 'js/components/query_mediator'
 *         }
 *       },
 *       widgets: {
 *         YearFacet: 'js/widgets/facets/factory'
 *       }
 *     });
 *   promise.done(function() {
 *     app.activate();
 *     //....continue setting up layout etc
 *   });
 *
 *
 */

define([
  'underscore',
  'jquery',
  'backbone',
  'module',
  'js/components/beehive'
], function(
  _,
  $,
  Backbone,
  module,
  BeeHive
  ) {


  var Application = function(options) {
    options || (options = {});
    this.aid = _.uniqueId('application');
    this.debug = false;
    _.extend(this, _.pick(options, ['timeout', 'debug']));
    this.initialize.apply(this, arguments);
  };

  var Container = function() {
    this.container = {};
  };
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
      // these are core (elevated access)
      this.__beehive = new BeeHive();
      this.__modules = new Container();
      this.__controllers = new Container();

      // these are barbarians behind the gates
      this.__widgets = new Container();
      this.__plugins = new Container();
      this.__barbarianRegistry = {};
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
        _.each(['controllers', 'modules', 'services', 'objects'], function(name) {
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
            if (_.isArray(promisedValues)) {
              if (self.debug) {
                console.log('application: registering ' + promisedValues[0]);
              }
              self._registerLoadedModules.apply(self, promisedValues);
            }
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
      var self = this;

      createInstance = function(key, module) {
        if (!module) {
          console.warn('Object ' + key + ' is empty, cannot instantiate it!');
          return;
        }
        if (self.debug) {
          console.log("Creating instance of: " + key);
        }
        if (module.prototype) {
          return new module()
        }
        if (module && module.hasOwnProperty(key)) {
          return module[key];
        }
        return module;
      };

      //console.log('registering', section, modules);

      if (section == "controllers") {
        hasKey = _.bind(this.hasController, this);
        removeKey = _.bind(function(key) {this.__controllers.remove(key)}, this);
        addKey = _.bind(function(key, module) {this.__controllers.add(key, module)}, this);
      }
      else if (section == "services") {
        hasKey = _.bind(beehive.hasService, beehive);
        removeKey = _.bind(beehive.removeService, beehive);
        addKey = _.bind(beehive.addService, beehive);
      }
      else if (section == 'objects') {
        hasKey = _.bind(beehive.hasObject, beehive);
        removeKey = _.bind(beehive.removeObject, beehive);
        addKey = _.bind(beehive.addObject, beehive);
      }
      else if (section == 'modules') {
        createInstance = function(key, module) {return module};
        hasKey = _.bind(this.hasModule, this);
        removeKey = _.bind(function(key) {this.__modules.remove(key)}, this);
        addKey = _.bind(function(key, module) {this.__modules.add(key, module)}, this);
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
        var inst = createInstance(key, module);
        if (!inst) {
          console.warn('Removing ' + key + '(because it is null!)');
          return;
        }
        addKey(key, inst);
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
     * Loads modules *asynchronously* from the following structure
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
      var self = this;
      this._checkPrescription(modulePrescription);

      if (this.debug) {
        console.log('application: loading ' + sectionName, modulePrescription);
      }

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
        if (self.debug) {
          console.log('Loaded: type=' + sectionName + ' state=' + defer.state(), ret);
        }
      };

      var errback = function (err) {
        var symbolicName = err.requireModules && err.requireModules[0];
        console.warn("Error loading impl=" + symbolicName);
        if (ignoreErrors) {
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
      var beehive = this.getBeeHive();
      var self = this;

      // services are activated by beehive itself
      if (self.debug) {console.log('application: beehive.activate()')};
      beehive.activate(beehive);

      // controllers receive application itself and elevated beehive object
      _.each(this.getAllControllers(), function(el) {
        if (self.debug) {console.log('application: controllers: ' + el[0] + '.activate(beehive, app)')};
        var plugin = el[1];
        if ('activate' in plugin) {
          plugin.activate(beehive, self);
        }
      });

      // modules receive elevated beehive object
      _.each(this.getAllModules(), function(el) {
        if (self.debug) {console.log('application: modules: ' + el[0] + '.activate(beehive)')};
        var plugin = el[1];
        if ('activate' in plugin) {
          plugin.activate(beehive);
        }
      });

      // all the rest receive hardened beehive
      var hardenedBee;
      _.each(this.getAllPlugins(), function(el) {
        if (self.debug) {console.log('application: plugins: ' + el[0] + '.activate(beehive)')};
        var plugin = el[1];
        if ('activate' in plugin) {
          plugin.activate(hardenedBee = beehive.getHardenedInstance());
          self.__barbarianRegistry[hardenedBee.getService('PubSub').getCurrentPubSubKey().getId()] = 'plugin:' + el[0];
        }
      });
      _.each(this.getAllWidgets(), function(el) {
        if (self.debug) {console.log('application: widget: ' + el[0] + '.activate(beehive)')};
        var plugin = el[1];
        if ('activate' in plugin) {
          plugin.activate(hardenedBee = beehive.getHardenedInstance());
          self.__barbarianRegistry[hardenedBee.getService('PubSub').getCurrentPubSubKey().getId()] = 'widget:'+ el[0];
        }
      });

      this.__activated = true;
    },

    getPluginOrWidgetByPubSubKey: function(psk) {
      var k;
      if (this.__barbarianRegistry[psk]) {
        k = this.__barbarianRegistry[psk];
      }
      else {
        return undefined;
      }

      var key = k.split(':');

      if (this.__widgets.has(key[1])) {
        return this.__widgets.get(key[1]);
      }
      else if (this.__plugins.has(key[1])) {
        return this.__plugins.get(key[1]);
      }

      throw new Error('Eeeek, thisis unexpectEED bEhAvjor! Cant find barbarian with ID: ' + psk);
    },

    isActivated: function() {
      return this.__activated || false;
    },

    hasService: function(name) {
      return this.getBeeHive().hasService(name);
    },
    getService: function(name) {
      return this.getBeeHive().getService(name);
    },

    hasObject: function(name) {
      return this.getBeeHive().hasObject(name);
    },
    getObject: function(name) {
      return this.getBeeHive().getObject(name);
    },

    hasController: function(name) {
      return this.__controllers.has(name);
    },
    getController: function(name) {
      return this.__controllers.get(name);
    },

    hasModule: function(name) {
      return this.__modules.has(name);
    },
    getModule: function(name) {
      return this.__modules.get(name);
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

    getAllControllers: function() {
      return _.pairs(this.__controllers.container);
    },
    getAllModules: function() {
      return _.pairs(this.__modules.container);
    },
    getAllPlugins: function() {
      return _.pairs(this.__plugins.container);
    },
    getAllWidgets: function() {
      return _.pairs(this.__widgets.container);
    },

    /**
     * Helper method to invoke a 'function' on all objects
     * that are inside the application
     *
     * @param funcName
     * @param options
     */
    triggerMethodOnAll: function(funcName, options) {
      this.triggerMethod(this.getAllControllers(), 'controllers', funcName, options);
      this.triggerMethod(this.getAllModules(), 'modules', funcName, options);
      this.triggerMethod(this.getAllPlugins(), 'plugins', funcName, options);
      this.triggerMethod(this.getAllWidgets(), 'widgets', funcName, options);
      this.triggerMethod(this.getBeeHive().getAllServices(), 'BeeHive:services', funcName, options);
      this.triggerMethod(this.getBeeHive().getAllObjects(), 'BeeHive:objects', funcName, options);
    },

    triggerMethod: function(objects, msg, funcName, options) {
      var self = this;
      _.each(objects, function(el) {
        var obj = el[1];
        if (funcName in obj) {
          if (self.debug) {console.log('application.triggerMethod: ' + msg + ": " + el[0] + '.' + funcName + '()')};
          obj[funcName].call(obj, options);
        }
      });
    }


  });


  // give it subclassing functionality
  Application.extend = Backbone.Model.extend;

  return Application;

});