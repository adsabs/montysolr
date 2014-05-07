

define(['underscore', 'jquery', 'backbone', 'module', 'js/components/beehive'], function(_, $, Backbone, module, BeeHive) {


  var Application = function(config, options) {
    options || (options = {});
    this.aid = _.uniqueId('application');
    _.extend(this, _.pick(options, ['timeout']));
    this.initialize.apply(this, arguments);

  };

  _.extend(Application.prototype, {

    /**
     * Purpose of this call is to load dynamically all modules
     * that you pass in a configuration. We'll load them using
     * requirejs and set them into BeeHive
     *
     * @param config
     * @param options
     */
    initialize: function(config, options) {
      this.__beehive = new BeeHive();

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

      var regions = config['regions'];
      if (regions) {
        _.each(_.keys(regions), function(name) {
          if (regions[name]) {
            promise = self._loadModules("region:" + name, regions[name]);
            if (promise)
              promises.push(regions);
          }
        });
      }

      var keepWaiting = true;
      var bigPromise = $.when.apply($, promises)
        .then(function () {
          _.each(arguments, function (promisedValues, idx) {
            if (_.isArray(promisedValues))
              self._registerLoadedModules.apply(self, promisedValues);
          })
        })
        .fail(function () {
          console.error("Generic error - we were not successul in loading all modules for config", config);
          throw new Error("We are screwed!");
        })
        .done(function() {
          console.log('DONE loading', this, config);
          keepWaiting = false;
        });

      function wait() {
        if (keepWaiting) {
          setTimeout(function() {
            console.log("waiting");
            wait();
          }, 5);
        }
        else {
          console.log("done waiting");
          return;
        }

      }
      wait();
      console.log('dunud')
      return;
    },

    getBeeHive: function() {
      return this.__beehive;
    },


    _registerLoadedModules: function(section, modules) {
      var beehive = this.getBeeHive();
      var key, module;
      console.log('registering', section, modules);
      if (section == "services") {
        _.each(_.pairs(modules), function(m) {
          key = m[0], module = m[1];
          if (beehive.hasService(key)) {
            console.warn("Removing (and adding new) service: ", key);
            beehive.removeService(key);
          }
          beehive.addService(key, new module());
        })
      }
      else if (section == 'objects') {
        // TODO: finish
      }
      else if (section.indexOf('regions:')) {
        // TODO: finish
      }
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

      var ret = {};

      // create the promise object - load the modules asynchronously
      var implNames = _.keys(modulePrescription);
      var impls = _.values(modulePrescription);
      var defer = $.Deferred();

      var callback = function () {
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
        throw new Error("Error loading impl=" + symbolicName);
      };

      // start loading the modules
      require(impls, callback, errback);

      return this._setTimeout(defer).promise();
    },

    _setTimeout: function(deferred) {
      setTimeout(function() {
        if (deferred.state() != 'resolved') {
          deferred.reject();
        }
      }, this.timeout || 5000);
      return deferred;
    },

    _loadRegions: function(regionName, modulePrescription) {
      return true;
    },

    close: function() {
      this.getBeeHive().close();
    },
    activate: function(options) {
      this.getBeeHive().activate();
    }
  });


  // give it subclassing functionality
  Application.extend = Backbone.Model.extend;

  return Application;

});