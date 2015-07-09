define([
  'marionette',
  'js/widgets/base/base_widget',
  'hbs!./template/navbar',
  'js/components/api_query_updater',
  'js/components/api_query',
  'bootstrap'

], function(
  Marionette,
  BaseWidget,
  NavBarTemplate,
  ApiQueryUpdater,
  ApiQuery
  ){

  var NavView, NavModel, NavWidget;

  NavModel = Backbone.Model.extend({
    defaults: function () {
      return {
        orcidModeOn: false,
        orcidLoggedIn: false,
        currentUser: undefined,
        orcidFirstName: undefined,
        orcidLastName: undefined,
        //should it show hourly banner?
        hourly : false
      }
    }
  });


  NavView = Marionette.ItemView.extend({

    template: NavBarTemplate,

    modelEvents : {
      change: "render"
    },

    events: {
      "click .orcid-dropdown ul": "stopPropagation",
      "click button.orcid-sign-in": "orcidSignIn",
      "change .orcid-mode": "changeOrcidMode",

      //to avoid stopPropagation as in triggers hash
      "click .orcid-link": function () {
        this.trigger("navigate-to-orcid-link")
      },
      "click .orcid-logout": function (e) {
        this.trigger("logout-only-orcid");
        e.preventDefault();
      },
      "click .logout": function () {
        this.trigger("logout")
      },
      "click .login": function () {
        this.trigger("navigate-login")
      },
      "click .register": function () {
        this.trigger("navigate-register")
      },
      "click code": function (e) {
        this.trigger('search-author');
      }

    },

    stopPropagation: function (e) {
      if (e.target.tagName.toLowerCase() == "button" || e.target.tagName.toLowerCase() == "a" || e.target.tagName.toLowerCase() == "code") {
        return true;
      }
      else {
        e.stopPropagation();
      }
    },

    orcidSignIn: function () {
      this.model.set("orcidModeOn", true);
     //need to explicitly trigger to widget that this has changed
     //otherwise it will be ignored, since it can also be changed
     //from outside
       this.trigger("user-change-orcid-mode");
    },

    changeOrcidMode : function() {
      var that = this;
      //allow animation to run before rerendering
      setTimeout(function () {

        if (that.$(".orcid-mode").is(":checked")) {
          that.model.set("orcidModeOn", true);
        }
        else {
          that.model.set("orcidModeOn", false);
        }

        //need to explicitly trigger to widget that this has changed
        //otherwise it will be ignored, since it can also be changed
        //from outside
        that.trigger("user-change-orcid-mode");

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
      this.qUpdater = new ApiQueryUpdater('NavBar');
    },

    activate: function (beehive) {
      this.setBeeHive(beehive);
      _.bindAll(this, ["handleUserAnnouncement", "getOrcidUserInfo"]);
      this.pubsub = beehive.getService("PubSub");
      this.pubsub.subscribe(this.pubsub.USER_ANNOUNCEMENT, this.handleUserAnnouncement);
      this.pubsub.subscribe(this.pubsub.APP_STARTED, this.getOrcidUserInfo);

      this.setInitialVals();
    },


    viewEvents : {
      //dealing with authentication/user
      "navigate-login" : function(){
        this.pubsub.publish(this.pubsub.NAVIGATE, "authentication-page", {subView: "login"});
      },
      "navigate-register" : function(){
        this.pubsub.publish(this.pubsub.NAVIGATE, "authentication-page", {subView: "register"});
      },
     "navigate-settings" : function() {
       this.pubsub.publish(this.pubsub.NAVIGATE, "UserPreferences");
     },
      "logout" : function() {
        //log the user out of both the session and orcid
        this.getBeeHive().getObject("Session").logout();
       //log out of ORCID too
       this.orcidLogout();
      },
       //dealing with orcid
      "navigate-to-orcid-link" : "navigateToOrcidLink",
      "user-change-orcid-mode" : "toggleOrcidMode",
      "logout-only-orcid" : "orcidLogout",
      'search-author': 'searchAuthor'
    },

    //to set the correct initial values for signed in statuses
    setInitialVals: function () {
      var user = this.getBeeHive().getObject("User");
      var orcidApi = this.getBeeHive().getService("OrcidApi");
      var hasAccess = orcidApi.hasAccess();
      this.model.set({orcidModeOn: user.isOrcidModeOn() && hasAccess, orcidLoggedIn: hasAccess});
      this.model.set("currentUser",  user.getUserName());
    },

    getOrcidUserInfo: function () {
      var orcidApi = this.getBeeHive().getService("OrcidApi");
      //get the orcid username if applicable
      if (this.model.get("orcidLoggedIn")) {
        //set the orcid username into the model
        var that = this;
        orcidApi.getUserProfile().done(function (info) {
          var firstName = info["orcid-bio"]["personal-details"]["given-names"]["value"];
          var lastName = info["orcid-bio"]["personal-details"]["family-name"]["value"];
          that.model.set("orcidFirstName", firstName);
          that.model.set("orcidLastName", lastName);
          that.model.set("orcidQueryName", lastName + ', ' + firstName);
          that.model.set("orcidURI", info["orcid-identifier"]["uri"]);
        })
      }

      //also set in the "hourly" flag
      var hourly = this.BeeHive.getObject("AppStorage").getConfigCopy().hourly;
      this.model.set("hourly", hourly);
    },

    handleUserAnnouncement : function(msg, arg2, arg3) {

      var user = this.getBeeHive().getObject("User");
      var orcidApi = this.getBeeHive().getService("OrcidApi");

      if (msg === "user_info_change" && arg2 === "USER") {
        //if user logs out, username will be undefined
        this.model.set("currentUser", this.getBeeHive().getObject("User").getUserName());
      }
      else if (msg == 'orcidUIChange') {
        this.model.set({orcidModeOn: user.isOrcidModeOn(), orcidLoggedIn: orcidApi.hasAccess()});

        if (this.model.get("orcidLoggedIn")) {
          this.getOrcidUserInfo();
        }
      }

    },

    //we don't want to respond to changes from pubsub or user object with this,
    //only changes that the user has initiated using the navbar widget,
    //otherwise things will be toggled incorrectly
    toggleOrcidMode : function() {
      var user = this.getBeeHive().getObject('User'),
        orcidApi = this.getBeeHive().getService("OrcidApi");

      var newVal = this.model.get("orcidModeOn");
      user.setOrcidMode(newVal);

      if (newVal) {
        //sign into orcid api if not signed in already
        if (!orcidApi.hasAccess()) {
          orcidApi.signIn();
        }
      }
    },

    searchAuthor: function () {
      this.pubsub.publish(this.pubsub.START_SEARCH, new ApiQuery(
        {
          'q': 'author:' + this.qUpdater.quote(this.model.get("orcidQueryName"))
        }));
    },

    signOut: function () {
      var user = this.getBeeHive().getObject('User'),
        orcidApi = this.getBeeHive().getService("OrcidApi");

      if (orcidApi)
        orcidApi.signOut();

      user.setOrcidMode(false);
    },

    orcidLogout: function () {
      this.getBeeHive().getService("OrcidApi").signOut();
      this.getBeeHive().getObject("User").setOrcidMode(false);
    },

    navigateToOrcidLink : function(){
      this.pubsub.publish(this.pubsub.NAVIGATE, "orcid-page")
    }


  });

  return NavWidget;

});