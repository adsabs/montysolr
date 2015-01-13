define([
    'underscore',
    'bootstrap',
    'js/components/generic_module',
    'js/mixins/dependon'
  ],
  function(_, Bootstrap, GenericModule, Mixins) {

  var setObject = function(key, value) {
    window.localStorage.setItem(key, JSON.stringify(value));
  };

  var setValue = function(key, value) {
    window.localStorage.setItem(key, value);
  };

  var getObject = function(key) {
    var value = window.localStorage.getItem(key);
    return JSON.parse(value);
  };

  var getValue = function(key) {
    var value = window.localStorage.getItem(key);
    return value;
  };

  var getHardenedInstance = function () {
    return this;
  };

  var LocalStorage = GenericModule.extend({
    getObject: getObject,
    setObject: setObject,
    setValue: setValue,
    getValue: getValue,
    getHardenedInstance: getHardenedInstance
  });

  _.extend(LocalStorage.prototype, Mixins.BeeHive);

  return LocalStorage;
});