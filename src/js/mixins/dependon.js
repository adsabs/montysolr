/**
 * Created by rchyla on 3/13/14.
 */

/*
 * This module contains a set of utilities that can be added to classes
 * to give them certain functionality
 */
define(['underscore'], function(_) {
  var Mixin = {

    /*
     * Core is the object that allows modules to get access to objects
     * of the application (but we make sure these objects are protected
     * and only application can set/change them). This mixin gives objects
     * functions to receive/query 'Core'
     */
    Core: {
      // called by parents (app) to give modules access to Core
      initCore: function(core) {
        this.Core = core;
      },
      getCore: function() {
        return this.Core;
      },
      hasCore: function() {
        if (this.Core) {
          return true;
        }
        return false;
      }

    },

    /*
     * PubSub is the publish/subscribe queue used by our components
     * to send messages to/from.
     */
    PubSub: {
      initPubSub: function() {
        // we depend on presence of core
        if (!(this.hasCore && this.hasCore())) {
          throw new Error("Dependency error: PubSub needs Core");
        }
        if (this.hasPubSub()) {
          throw new Error("Dependency error: Core must provide PubSub");
        }
      },
      getPubSub: function() {
        return this.getCore().getPubSub();
      },
      hasPubSub: function() {
        try {
          if (!this.getCore().getPubSub()) {
            return false;
          }
        }
        catch (e) {
          return false;
        }
        return true;
      }
    }
  };

  return Mixin;
})
