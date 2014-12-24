/**
 * Catalogue of Alerts (these are the messages that get displayed
 * to the user)
 */

define([
  'backbone',
  'js/mixins/hardened'
], function(
  Backbone,
  Hardened
  ) {

  var Alerts = function (options) {
    _.extend(this, _.defaults(options || {}, {code: 200, msg: undefined}));
    this.setType(this.type);
  };

  Alerts.TYPE = {
      ERROR: "error",
      INFO: "info",
      WARNING: "warning",
      SUCCESS: "success",
      DANGER: "danger"
    };

  Alerts.ACTION = {
      CALL_PUBSUB: 0,
      TRIGGER_FEEDBACK: 1
    };

  var _types = {};
  _.each(_.pairs(Alerts.TYPE), function (p) {
    _types[p[1]] = p[0];
  });

  var _actions = {};
  _.each(_.pairs(Alerts.ACTION), function (p) {
    _actions[p[1]] = p[0];
  });

  _.extend(Alerts.prototype, {
    hardenedInterface: {
      type: 'integer value of the code',
      msg: 'string message',
      toJSON: 'for cloning (all non-standard attributes will be lost)'
    },

    toJSON: function () {
      return {code: this.code, msg: this.msg};
    },
    setType: function (c) {
      if (!(_types[c])) {
        throw new Error("This type is not in Alerts.TYPE - please extend js/components/alerts first:", c);
      }
      this.type = c;
    },
    setAction: function (a) {
      if (!(_actions[a])) {
        throw new Error("This type is not in Alerts.ACTION - please extend js/components/alerts first:", a);
      }
      this.actions = a;
    },
    setMsg: function(msg) {
      this.msg = msg;
    }
  }, Hardened);

  Alerts.extend = Backbone.Model.extend;

  return Alerts;
});
