define([
  'marionette',
  'js/widgets/base/base_widget',
  'hbs!./template/navbar',
  'bootstrap'
], function(
  Marionette,
  BaseWidget,
  NavBarTemplate

  ){

  var NavView, NavModel, NavWidget;

  NavModel = Backbone.Model.extend({
    defaults : function(){
      return {
        orcidModeOn : false,
        orcidLoggedIn : false,
        adsLoggedIn  : false
      }
    }
  });


  NavView = Marionette.ItemView.extend({

    template : NavBarTemplate,

    triggers : {
      "click .ads" : "ads-toggle-state"
    },

    events : {
      "change .orcid-mode" : "changeOrcidMode"
    },

    changeOrcidMode : function() {
      var that = this;

      if (this.$(".orcid-mode").is(":checked")){
        this.model.set("orcidModeOn", true);
      }
      else {
        this.model.set("orcidModeOn", false);
      }
      //allow animation to run before rerendering
      setTimeout(function(){
        that.render();
      }, 400);
    }

  });

  NavWidget = BaseWidget.extend({

    initialize: function (options) {
      options = options || {};
      this.model = new NavModel();
      this.view = new NavView({model: this.model});
      BaseWidget.prototype.initialize.apply(this, arguments);
    },

    activate: function (beehive) {
      this.setBeeHive(beehive);
      this.setInitialVals();
      var pubsub = beehive.getService('PubSub');
      pubsub.subscribe(pubsub.USER_ANNOUNCEMENT, _.bind(this.handleUserAnnouncement, this));
    },

    //to set the correct initial values for signed in statuses
    setInitialVals : function(){
      var user = this.getBeeHive().getObject("User");
      var orcidApi = this.getBeeHive().getService("OrcidApi");
      var val = false;
      if (user.isOrcidModeOn() && orcidApi.hasAccess()){
        val = true;
      }
      this.model.set({orcidModeOn : val}, {silent : true});
    },

    handleUserAnnouncement : function(key, val){
      if (key == 'orcidUIChange') {
        var orcidApi = this.getBeeHive().getService("OrcidApi");
        this.model.set('orcidModeOn', val && orcidApi.hasAccess());
      }
    },

    viewEvents : {
      "ads-toggle-state" : "triggerADSAction"
    },

    modelEvents : {
      "change:orcidModeOn" :"toggleOrcidMode"
    },

    toggleOrcidMode : function() {
      var user = this.getBeeHive().getObject('User'),
        orcidApi = this.getBeeHive().getService("OrcidApi");

      var newVal = this.model.get("orcidModeOn");
      if (newVal){
        //sign into orcid api if not signed in already
        if (!orcidApi.hasAccess() ){
          orcidApi.signIn();
        }
      }
      user.setOrcidMode(newVal);
      var pubsub = this.getBeeHive().getService('PubSub');
    },

    triggerADSAction : function(){
    }
  });

  return NavWidget;

});