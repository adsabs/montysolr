define([
    'underscore',
    'backbone',
    'js/mixins/hardened'],
  function (_, Backbone, Hardened) {


    var ApiFeedback = function (options) {
      _.extend(this, _.defaults(options || {}, {code: 200, msg: undefined}));
      this.setCode(this.code);
    };

    ApiFeedback.CODES = {
      INVALID_PASSWORD: 498,
      ACCOUNT_NOT_FOUND: 495, // Account not found during signin
      ALREADY_LOGGED_IN: 493, // Already signed during signup
      REQUIRES_LOGIN: 491,
      TOO_MANY_CHARACTERS: 486,
      BAD_REQUEST: 400,
      UNAUTHORIZED: 401,
      NOT_FOUND: 404,
      INTERNAL_SERVER_ERROR: 500,
      BAD_GATEWAY: 502,
      SERVER_ERROR: 503,
      TOO_MANY_FAILURES: 580,
      ALL_FINE: 200,
      KEEP_WAITING: 190,
      TESTING: 0,


      // Internal events
      MAKE_SPACE: -1,
      SEARCH_CYCLE_STARTED: -2,
      SEARCH_CYCLE_FAILED_TO_START: -3,
      SEARCH_CYCLE_PROGRESS: -4,
      SEARCH_CYCLE_STOP_MONITORING: -5,
      SEARCH_CYCLE_FINISHED: -6,
    };

    var _codes = {};
    _.each(_.pairs(ApiFeedback.CODES), function (p) {
      _codes[p[1]] = p[0];
    });

    _.extend(ApiFeedback.prototype, {
      hardenedInterface: {
        code: 'integer value of the code',
        msg: 'string message',
        toJSON: 'for cloning',
        getApiRequest: 'to get the original request',
        getSenderKey: 'retrieve the senders key'

      },
      initialize: function () {
      },
      toJSON: function () {
        return {code: this.code, msg: this.msg};
      },
      setCode: function (c) {
        if (!(_codes[c])) {
          throw new Error("This code is not in the list ApiCodes - please extend js/components/api_feedback first:", this.code);
        }
        this.code = c;
      },
      setApiRequest: function(apiRequest) {
        this.req = apiRequest;
      },
      getApiRequest: function() {
        return this.req;
      },

      setMsg: function(msg) {
        this.msg = msg;
      },

      getSenderKey: function() {
        return this.senderKey;
      },

      setSenderKey: function(key) {
        this.senderKey = key;
      }


    }, Hardened);

    ApiFeedback.extend = Backbone.Model.extend;
    return ApiFeedback;

  });