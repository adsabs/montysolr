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

    defaults : function(){
      return {
        orcidUIOn : false
      }
    }
    });

  var User = GenericModule.extend({

    initialize : function(){
      this.model = new UserModel();
    },

    activate: function (beehive) {
      this.beehive = beehive;
      this.pubsub = this.beehive.Services.get('PubSub');

    },

    setOrcidMode : function(val){

      this.model.set("orcidUIOn", val);

      if (_.has(this.model.changedAttributes(), "orcidUIOn")){
        this.pubsub.publish(this.pubsub.getPubSubKey(), this.pubsub.USER_ANNOUNCEMENT, "orcidUIChange", this.model.get("orcidUIOn"));
      }

    },

    orcidUIOn : function(){
      return this.model.get("orcidUIOn");
    },

    hardenedInterface: {
      setOrcidMode : "toggle ORCID UI mode on/off (param true or false)",
      orcidUIOn : "find out if the ORCID UI state is active"
    }

  });

  _.extend(User.prototype, Hardened);


  return User;

});