define([
    'underscore',
    'js/components/generic_module',
    'js/mixins/dependon',
    'persist-js',
    'module'
  ],
  function(
    _,
    GenericModule,
    Mixins,
    PersistJS,
    module
    ) {


  var namespace = module.config().namespace || '';

  var LocalStorage = GenericModule.extend({

    constructor: function (opts) {
      opts = opts || {};
      this._store = this.createStore(namespace + (opts.name || ''));
    },

    createStore: function(name) {
      return this._createStore(name);
    },

    _createStore: function(name) {
      var s = new PersistJS.Store(name, {
        about: 'This is bumblebee persistent storage',
        defer: true
      });
      var keys = s.get('#keys');
      if (!keys) {
        s.set('#keys', '{}');
      }
      else {
        try {
          keys = JSON.parse(keys);
          if (!_.isObject(keys)) {
            s.set('#keys', '{}');
          }
        }
        catch (e) {
          s.set('#keys', '{}');
        }
      }
      return s;
    },

    set: function(key, value) {
      this._checkKey(key);
      if (!_.isString(value)) {
        value = JSON.stringify(value);
      }
      this._store.set(key, value);
      this._setKey(key);
    },

    get: function(key) {
      this._checkKey(key);
      var v = this._store.get(key);
      if (!v) return v;
      try {
        return JSON.parse(v);
      }
      catch(e) {
        return v;
      }
    },

    remove: function(key) {
      this._checkKey(key);
      this._store.remove(key);
      this._delKey(key);
    },

    clear: function() {
      var keys = this.get('#keys');
      for (var k in keys) {
        this._store.remove(k);
      }
      this._store.set('#keys', '{}');
    },

    keys: function() {
      return JSON.parse(this._store.get('#keys'));
    },

    _setKey: function(key) {
      var keys = this.keys();
      keys[key] = 1;
      this._store.set('#keys', JSON.stringify(keys));
    },

    _delKey: function(key) {
      var keys = this.keys();
      delete keys[key];
      this._store.set('#keys', JSON.stringify(keys));
    },

    _checkKey: function(key) {
      if (!_.isString(key)) {
        throw new Error('key must be string, received: ' + key);
      }
    }

  });

  _.extend(LocalStorage.prototype, Mixins.BeeHive);

  return LocalStorage;
});