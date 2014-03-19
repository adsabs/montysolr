/*
 * A generic class to be used for building modules (the Marionette.Module)
 * just complicates things. For simple things, just use this class.
 */

define(['backbone', 'underscore'], function(Backbone, _) {

  // A list of options to be attached directly to the module, if provided.
  var moduleOptions = ['className', 'activate'];

  var Module = function(attributes, options) {
    var defaults;
    var attrs = attributes || {};
    options || (options = {});
    this.mid = _.uniqueId('module');
    this.attributes = {};
    _.extend(this, _.pick(options, moduleOptions));

    if (defaults = _.result(this, 'defaults')) {
      attrs = _.defaults({}, attrs, defaults);
    }
    this.initialize.apply(this, arguments);
  };

  // every module has the Events mixin
  _.extend(Module.prototype, Backbone.Events, {
    className: 'GenericModule',
    initialize: function() {},
    close: function() {},
    activate: function(options) {
      _.extend(this, _.pick(options, moduleOptions));
    }
  });

  // give the module subclassing functionality
  Module.extend = Backbone.Model.extend;
  return Module;

});