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
    },

    //to set the correct initial values for signed in statuses
    setInitialVals : function(){
      var user = this.getBeeHive().getObject("User");
      if (user.isOrcidModeOn()){
        this.model.set({orcidModeOn : true}, {silent : true});
      }
    },

    handleUserAnnouncement : function(){
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

      if (this.model.get("orcidModeOn")){
        user.setOrcidMode(true);
        //sign into orcid api if not signed in already
        if (!orcidApi.hasAccess() ){
          orcidApi.signIn();
        }
      }
      else {
        user.setOrcidMode(false);
        if (!orcidApi.hasAccess()) {
          orcidApi.signOut();
        }
      }
    },

    triggerADSAction : function(){
    }
  });

  return NavWidget;

});