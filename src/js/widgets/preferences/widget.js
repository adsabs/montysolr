define([
  "marionette",
  "js/widgets/base/base_widget",
  "./views/openurl",
  "./views/orcid",
  "js/components/api_feedback",
  "hbs!./templates/orcid-form-submit-modal"
], function (
  Marionette,
  BaseWidget,
  OpenURLView,
  OrcidView,
  ApiFeedback,
  OrcidModalTemplate
  ) {

  var PreferencesModel = Backbone.Model.extend({

    defaults : function(){
      return {
        openURLConfig : undefined,
        OrcidLoggedIn : undefined
      }
    }

  });

  var PreferencesView = Marionette.LayoutView.extend({

    template: function () {
      return "<div class=\"content-container\"></div>"
    },

    className: "s-preferences preferences-widget",

    regions: {
      content: ".content-container"
    },

    setSubView: function (viewConstructor) {
      //providing all views with a copy of the model
      var view = new viewConstructor({model: this.model});

      this.getRegion("content").show(view);

      //forward events
      this.listenTo(view, "all", function(){
        this.trigger.apply(this, arguments)
      });
    }

  });

  /*
  * the rule is that preferences widget provides sub views with its model,
  * and widgets do not touch the model-- instead, they emit form submitted events
  * with a json structure representing the form data
  * */


  var PreferencesWidget = BaseWidget.extend({

    initialize: function (options) {
      options = options || {};

      this.model = new PreferencesModel();
      this.view = new PreferencesView({ model: this.model });
      this.listenTo(this.view, "all", this.handleViewEvents);

      BaseWidget.prototype.initialize.apply(this, arguments);
    },

    activate: function (beehive) {
      var that = this;
      this.setBeeHive(beehive);
      _.bindAll(this);
      var pubsub = beehive.getService('PubSub');
      pubsub.subscribe(pubsub.USER_ANNOUNCEMENT, this.handleUserAnnouncement);

      //as soon as preferences widget is activated, get the open url config
      this.getBeeHive().getObject("User").getOpenURLConfig().done(function (config) {
        that.model.set("openURLConfig", config);
      });

    },

    //translates what comes from toc widget (e.g. userPreferences__orcid) to view name
    views : {
      orcid : OrcidView,
      librarylink : OpenURLView
    },

    setSubView: function (subView) {
      if (_.isArray(subView)) {
        //XXX:figure out why array
        subView = subView[0];
      }
      var viewConstructor = this.views[subView];
      if (!viewConstructor){
        console.warn("don't recognize this subview: ", subView );
        return
      }
      this.view.setSubView(viewConstructor);

      this.fetchNecessaryData.apply(this, arguments);
    },

    fetchNecessaryData : function(subView) {

      var that = this;

      this.model.set("orcidLoggedIn", this.getBeeHive().getService("OrcidApi").hasAccess());

      /*right now only orcid view needs extra data */

      if (subView === "orcid" && this.model.get("orcidLoggedIn") ){

        //get main orcid name
        var orcidProfile = this.getBeeHive().getService("OrcidApi").getUserProfile();
        var adsOrcidUserInfo = this.getBeeHive().getService("OrcidApi").getADSUserData();

        //doing it at once so there's no flicker of rapid rendering as different vals change
        $.when(orcidProfile, adsOrcidUserInfo).done(function(orcid, ads){
          var data = {userSubmitted : ads};
          try {
            var firstName = orcid["orcid-bio"]["personal-details"]["given-names"]["value"];
            var lastName = orcid["orcid-bio"]["personal-details"]["family-name"]["value"];
            //unchangeable orcid name
            data.orcidName =  lastName + ", " + firstName;
            data.prettyOrcidName = firstName + " " + lastName;
          } catch(e){
            data.orcidName = "unknown";
            data.prettyOrcidName = "unknown";
          }
          that.model.set(data);
        });
      }
    },

    handleViewEvents: function (event, arg1, arg2) {
      var that = this;

      if (event === "change:link_server") {
        this.getBeeHive().getObject("User").setUserData({link_server: arg1});
      }

      else if (event === "orcid-authenticate"){
        this.getBeeHive().getObject("AppStorage").setStashedNav("UserPreferences", {subView: "orcid"});
        this.getBeeHive().getService("OrcidApi").signIn();
      }

      else if (event === "orcid-form-submit"){
        this.getBeeHive().getService("OrcidApi").setADSUserData(arg1).done(function(){
          //show the success modal
          that.getPubSub().publish(that.getPubSub().ALERT, new ApiFeedback({
            code: ApiFeedback.CODES.ALERT,
            msg: OrcidModalTemplate(),
            type: "success",
            title: "Thanks for submitting your supplemental ORCID information",
            modal: true
          }));

          //this will re-render the form
          that.setSubView("orcid");
        }).fail(function(){
          //show the success modal
          that.getPubSub().publish(that.getPubSub().ALERT, new ApiFeedback({
            code: ApiFeedback.CODES.ALERT,
            msg: "Please try again later.",
            type: "danger",
            title: "Your ORCID information was not submitted",
            modal: true
          }));
        })
      }
    },

    handleUserAnnouncement: function (event, data) {
      //update the user model if it changes
      var user = this.getBeeHive().getObject('User');
      if (event == user.USER_INFO_CHANGE) {
        this.model.set(data);
      }
    }

  });

  return PreferencesWidget

});