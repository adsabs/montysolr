define([
  'underscore',
  'js/components/persistent_storage'
], function(
  _,
  PersistentStorage
  ) {

  //TODO:rca - make sure that the service is loaded at the bootstrap and persisted
  //before the app exits (and that nothing can change the data inbetween; we can
  //let them do that; but we'll not care for it

  var Storage = PersistentStorage.extend( {
    activate: function(beehive) {
      //this.setBeeHive(beehive);
      //var pubsub = beehive.getService('PubSub');
      //pubsub.subscribeOnce(pubsub.getPubSubKey(), pubsub.APP_BOOTSTRAPPED, this.onAppBootstrapped);
    },

    onAppBootstrapped: function() {
      var beehive = this.getBeeHive();
      var rconf = beehive.getObject('DynamicConfig');
      if (!rconf) {
        throw new Error('DynamicConfig is missing');
      }
      var name = rconf.namespace; // the unique id that identifies this application (it serves as a namespace)
      if (!name) {
        console.warn('Application namespace not set; persistent storage will be created without it');
        name = '';
      }
      this._store = this._createStore(name);
    }
  });

  return function() {
    return new Storage({name: 'storage-service'});
  }
});