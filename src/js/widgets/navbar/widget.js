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

      "change" : "render"

    },

    triggers : {
      "click .orcid-on" : "orcid-on",
      "click .orcid-off" : "orcid-off",
      "click .ads" : "ads-toggle-state"
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

      debugger;

    },

    viewEvents : {
      "orcid-on" : "toggleOrcidOn",
      "orcid-off" : "toggleOrcidOff",
      "ads-toggle-state" : "triggerADSAction"
    },

    modelEvents : {

    },

    toggleOrcidOn : function(){

      var orcidApi = this.beehive.getService('OrcidApi');
      orcidApi.toggleOrcidUI(true);

      if (!orcidApi.hasAccess() ){
        orcidApi.signIn();
      }
    },

    toggleOrcidOff : function(){

      var orcidApi = this.beehive.getService('OrcidApi');
      orcidApi.toggleOrcidUI(false);

    },

    triggerADSAction : function(){


    }



  });

return NavWidget;





})