define([
    'underscore',
    'backbone',
    'js/mixins/hardened'],
  function (_, Backbone, Hardened) {


    var ApiFeedback = function (options) {
      _.extend(this, _.defaults(options || {}, {code: 200, msg: undefined}));
      this.setCode(this.code);
    };

    ApiFeedback.prototype.CODES = {
      INVALID_PASSWORD: 498,
      ACCOUNT_NOT_FOUND: 495, // Account not found during signin
      ALREADY_LOGGED_IN: 493, // Already signed during signup
      REQUIRES_LOGIN: 491,
      TOO_MANY_CHARACTERS: 486,
      NOT_FOUND: 404,
      SERVER_ERROR: 503,
      ALL_FINE: 200,
      KEEP_WAITING: 190
    };

    var _codes = {};
    _.each(_.pairs(ApiFeedback.prototype.CODES), function (p) {
      _codes[p[1]] = p[0];
    });

    _.extend(ApiFeedback.prototype, {
      hardenedInterface: {
        code: 'integer value of the code',
        msg: 'string message',
        toJSON: 'for cloning'

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
      }
    }, Hardened);

    ApiFeedback.extend = Backbone.Model.extend;
    return ApiFeedback;

  });