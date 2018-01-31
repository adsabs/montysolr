'use strict';
define([
  'underscore',
  'js/components/api_feedback'
], function (_, ApiFeedback) {

  /**
   * Abstract error pubsub-like manager.  Allows for hooks to be applied that
   * match API feedback codes
   *
   * @constructor
   */
  var ErrorHandlerManager = function () {
    this.handlers = {};

    /**
     * Attach a new handler to an API feedback code
     *
     * @param {Number} code - feedback code
     * @param {function} cb - callback function
     */
    this.on = function (code, cb) {
      this.handlers[code].push(cb);
    };

    /**
     * Remove handler by passing in the callback function
     *
     * @param {function} cb - callback function
     */
    this.off = function (cb) {
      _.forEach(this.handlers, function (v, k) {
        var idx = v.indexOf(cb);
        if (idx > -1) {
          v.splice(idx, 1);
        }
      });
    };

    /**
     * Fire off the callbacks for a particular code
     *
     * @param {Number} code - feedback code
     * @param {Array} args - arguments to pass to the callback
     * @param {object} ctx - callback's `this` property
     */
    this.fire = function (code, args, ctx) {
      _.forEach(this.handlers[code], function (cb) {
        cb.apply(ctx, args);
      });
    };

    _.reduce(ApiFeedback.CODES, function (res, code) {
      res[code] = [];
      return res;
    }, this.handlers);
  };

  /*
    Widget state manager mixin

    This mixin will allow widgets to call lifecycle updates which can then have
    handlers attached.  The mixin can also serve as a way for page managers to
    react to widget status updates, since certain widgets should be
    handled differently depending on their type and importance.

    The typical way this is used is to call `activateWidget` during the activate
    call on the widget itself.  Then call the various state updates when the widget
    is performing actions.

    The default states are: ready, loading, idle, errored

    Widgets can attach handlers to the various status updates (i.e. onReady, onLoading, etc.)

    note: during the activation, the widget's pubsubkey is captured and is used
    to identify the appropriate feedback.  This way the handlers aren't called for
    events the widget doesn't care about.
   */
  var WidgetStateManagerMixin = {

    /**
     * Creates a new instance of the handlerManager and subscribes to the
     * api feedback calls.  It also sets the first state update.
     */
    activateWidget: function () {
      this.__widgetHandlerManager = new ErrorHandlerManager();
      var pubsub = this.getPubSub();
      pubsub.subscribe(pubsub.FEEDBACK, _.bind(this.__handleFeedback, this));
      this.updateState('ready');
    },

    /**
     * Upon new feedback from the API, this function will grab the current
     * pubSubKey Id for this widget and compare it to the one listed on the
     * feedback object.  If they match, then the handlers are fired off for that
     * code.
     *
     * @param {ApiFeedback} feedback
     * @param {PubSubKey} pubSubKey
     * @private
     */
    __handleFeedback: function (feedback, pubSubKey) {
      var id = this.getPubSub().getCurrentPubSubKey().getId();
      if (id === pubSubKey.getId()) {
        this.__widgetHandlerManager.fire(feedback.code, arguments, this);
      }
    },

    /**
     * Attaches a new handler onto an API code
     *
     * @param {Number} code - feedback code
     * @param {function} handler - handler function
     */
    attachHandler: function (code, handler) {
      this.__widgetHandlerManager.on(code, handler);
    },

    /**
     * Attaches a general handler to all api feedback codes
     *
     * Includes the positive error codes and api request failures
     *
     * @param {function} handler - handler function
     */
    attachGeneralHandler: function (handler) {
      _.forEach(ApiFeedback.CODES, _.bind(function (code) {
        if (code > 0 || code === ApiFeedback.CODES.API_REQUEST_ERROR) {
          this.__widgetHandlerManager.on(code, handler);
        }
      }, this));
    },

    /**
     * Detaches a particular handler from all codes
     *
     * @param cb
     */
    detachHandler: function (cb) {
      this.__widgetHandlerManager.off(cb);
    },

    /**
     * Called when the state changes to 'ready'
     */
    onReady: _.noop,

    /**
     * Called when the state changes to 'loading'
     */
    onLoading: _.noop,

    /**
     * Called when the state changes to 'idle'
     */
    onIdle: _.noop,

    /**
     * Called when the state changes to 'errored'
     */
    onErrored: _.noop,

    /**
     * Called on all state changes
     */
    onStateChange: _.noop,

    /**
     * Called by widgets to update their own state.  This will update the current
     * state and also call any handlers.
     *
     * @param {string} state - the state to update to
     */
    updateState: function (state) {
      if (!state || !_.isString(state) || state === this.__state) {

        // do nothing
        return;
      }
      var prev = this.__state;
      this.__state = state;
      this.__fireStateHandler();
      this.onStateChange.call(this, state, prev);
    },

    /**
     * Get the current state value
     */
    getState: function () {
      return this.__state;
    },

    /**
     * This method looks at the current state and if a matching handler is
     * provided, it will call it accordingly.
     *
     * @private
     */
    __fireStateHandler: function () {
      var handlers = {};
      handlers['ready'] = this.onReady;
      handlers['loading'] = this.onLoading;
      handlers['idle'] = this.onIdle;
      handlers['errored'] = this.onErrored;
      handlers[this.__state] && handlers[this.__state].call(this, this);
    }
  };

  return WidgetStateManagerMixin;
});
