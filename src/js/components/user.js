/*
 * A generic class that lazy-loads User info
 */

define([
  'backbone',
  'js/components/generic_module',
  'js/mixins/hardened',
  'js/mixins/dependon'

], function(
  Backbone,
  GenericModule,
  Hardened,
  Dependon
  ) {


  var UserModel = Backbone.Model.extend({

    defaults : function(){
      return {
        isOrcidModeOn : false
      }
    }
    });

  var User = GenericModule.extend({

    initialize : function(){
      this.model = new UserModel();
    },

    activate: function (beehive) {
      this.setBeeHive(beehive);
      this.key = beehive.getService('PubSub').getPubSubKey();

      var storage = beehive.getService('PersistentStorage');
      if (storage) {
        var prefs = storage.get('UserPreferences');
        if (prefs) {
          this.model.set(prefs);
        }
      }
    },

    setOrcidMode : function(val){

      if (!this.hasBeeHive())
        return;

      this.model.set("isOrcidModeOn", val);
      var pubsub = this.getBeeHive().getService('PubSub');

      if (_.has(this.model.changedAttributes(), "isOrcidModeOn")){
        pubsub.publish(this.key, pubsub.USER_ANNOUNCEMENT, "orcidUIChange", this.model.get("isOrcidModeOn"));
      }
      this._persistModel();
    },

    isOrcidModeOn : function(){
      return this.model.get("isOrcidModeOn");
    },

    hardenedInterface: {
      setOrcidMode : "toggle ORCID UI mode on/off (param true or false)",
      isOrcidModeOn : "find out if the ORCID UI state is active"
    },

    //XXX a quick hack
    _persistModel: function() {
      var storage = this.getBeeHive().getService('PersistentStorage');
      if (storage) {
        storage.set('UserPreferences', this.model.attributes);
      }
    }

  });

  _.extend(User.prototype, Hardened, Dependon.BeeHive);


  return User;

});