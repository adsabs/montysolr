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
        adsLoggedIn  : false
      }
    }

  });


  NavView = Marionette.ItemView.extend({

    template : NavBarTemplate,

    modelEvents : {

      "change:adsLoggedIn" : "render"

    },

    triggers : {
      "click .ads" : "ads-toggle-state"
    },

    events : {
      "change .orcid-mode" : "changeOrcidMode"
    },

    changeOrcidMode : function(){
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
      this.beehive = beehive;
      this.pubsub = this.beehive.Services.get('PubSub');
      this.pubsub.subscribe(this.pubsub.ORCID_ANNOUNCEMENT, _.bind(this, "handleOrcidAnnouncement"));

      this.setInitialVals();
    },

    //to set the correct initial values for signed in statuses
    setInitialVals : function(){

      var orcidApi = this.beehive.getService('OrcidApi');
      if (orcidApi.hasAccess()){
        this.model.set({orcidLoggedIn : true}, {silent : true});
      }

    },

    handleOrcidAnnouncement : function(){

    },

    viewEvents : {

      "ads-toggle-state" : "triggerADSAction"
    },

    modelEvents : {

      "change:orcidModeOn" :"toggleOrcid"

    },

    toggleOrcid : function(){
      var user = this.beehive.getObject('User'),
        orcidApi = this.beehive.getService("OrcidApi");

      if (this.model.get("orcidModeOn")){
         user.toggleOrcidUI(true);
        //sign into orcid api if not signed in already
        if (!orcidApi.hasAccess() ){
          orcidApi.signIn();
        }
      }

      else {
        user.toggleOrcidUI(false);
      }
    },

    triggerADSAction : function(){


    }



  });

return NavWidget;





})