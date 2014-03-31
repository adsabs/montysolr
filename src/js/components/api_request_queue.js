/**
 * Created by rchyla on 3/28/14.
 */

/**
 * Collection of ApiRequests wrapped into a convenient package
 * that can be passed to the Api service and it will call
 * callback 'success' once all of the requests were finished
 */


/**
 * class R.ApiRequestQueue
 *
 * Available options:
 *
 * - callAllSuccessCallbacks (Boolean): If true, success callbacks will only
 *   be called for every queued request, instead of just the final one.
 *   Default: false
 **/
define(['underscore', 'js/components/api_request'], function(_, ApiRequest) {


  var ApiRequestQueue = ApiRequest.extend({
    _queue : [],
    _lock : false,
    _options : {},
    successAllCallback: null,
    initialize: function(attrs, options) {
      _.extend(this, _.pick(attrs, ['successAllCallback']));
    },

    push: function(request) {
      if (!(request instanceof ApiRequest)) {
        throw Error("ApiRequestQueue works only with ApiRequest instances");
      }
      this._queue.push(request);
      if (!this.isLocked()) {
        this.lock();
        this._processQueue();
      }
    },

    _processQueue: function() {
      if (!this.isEmpty()) {
        var doNothing = function() {};
        var args = this._queue.shift();
        var _success = args.success ? args.success : doNothing;
        var _error = args.error ? args.error : doNothing;
        var self = this;
        args.success = function(response) { // Wrap the success callback
          if (self._callAllSuccessCallbacks || self.isEmpty()) {
            _success(response);
          }

          if (self.isEmpty()) {
            self.unlock();
          }

          self._processQueue();
        };
        args.error = function(response) { // Wrap the error callback
          _error(response);
          // Cancel the queued commands on error.
          // Aborting requests on error is the appropriate behavior because
          // queued requests depend on the outcome of the previous requests.
          // If the outcome is unsuccessful, the subsequent requests are probably invalid.
          if (!self.isEmpty()) {
            console.error("Error encountered while processing queued commands. Remaining commands will not be processed.");
            self._queue = [];
          }
          self.unlock();
        };
        R.Api.request(args);
      }
    },

    lock: function() {
      this._lock = true;
    },

    unlock: function() {
      this._lock = false;
    },

    isLocked: function() {
      return this._lock;
    },

    isEmpty: function() {
      return this._queue.length === 0;
    }
  });

  return ApiRequestQueue;
});