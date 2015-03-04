/*
 * A generic class that lazy-loads User info
 */

define([
  'backbone',
  'js/components/generic_module',
  'js/mixins/hardened',

], function(
  Backbone,
  GenericModule,
  Hardened
  ) {


  var UserModel = Backbone.Model.extend({

    });

  var User = GenericModule.extend({

    initialize : function(){
      this.model = new UserModel();
    },


    activate: function (beehive) {
      this.beehive = beehive;
      this.pubsub = this.beehive.Services.get('PubSub');

    },

    toggleOrcidUI : function(val){
      if (val === true || val === false){
        this.model.set("orcidUIOn", val);
      }
      else {
        this.model.set("orcidUIOn", !this.model.get("orcidUIOn"));
      }

      if (_.has(this.model.changedAttributes(), "orcidUIOn")){
        this.pubsub.publish(this.pubsub.getPubSubKey(), this.pubsub.USER_ANNOUNCEMENT, "orcidUIChange", this.model.get("orcidUIOn"));
      }

    },

    orcidUIOn : function(){
      return this.model.get("orcidUIOn");
    },

    hardenedInterface: {
      toggleOrcidUI : "toggle ORCID UI state on/off",
      orcidUIOn : "find out if the ORCID UI state is active"
    }

  });

  _.extend(User.prototype, Hardened);


  return User;

});