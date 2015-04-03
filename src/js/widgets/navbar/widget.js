define([
  'marionette',
  'js/widgets/base/base_widget',
  'hbs!./template/navbar',
  'js/components/api_query_updater',
  'js/components/api_query',
  'bootstrap'
], function (Marionette, BaseWidget, NavBarTemplate, ApiQueryUpdater, ApiQuery) {

  var NavView, NavModel, NavWidget;

  NavModel = Backbone.Model.extend({
    defaults: function () {
      return {
        orcidModeOn: false,
        orcidLoggedIn: false,
        currentUser: undefined,
        orcidFirstName: undefined,
        orcidLastName: undefined
      }
    }
  });


  NavView = Marionette.ItemView.extend({

    template: NavBarTemplate,


    modelEvents: {
      change: "render"
    },

    events: {
      "click .orcid-dropdown ul": "stopPropagation",
      "click button.orcid-sign-in": "orcidSignIn",
      "change .orcid-mode": "changeOrcidMode",
      'click li.ads button.sign-out': 'adsSignout',
      'click li.ads button.sign-out': 'adsSignout',
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
      "click .settings": function () {
        this.trigger("navigate-settings")
      },
      "click code": function (e) {
        this.trigger('search-author');
      }

    },

    modelEvents: {
      'change': 'render'
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
    },

    changeOrcidMode: function () {
      var that = this;
      //allow animation to run before rerendering
      setTimeout(function () {

        if (that.$(".orcid-mode").is(":checked")) {
          that.model.set("orcidModeOn", true);
        }
        else {
          that.model.set("orcidModeOn", false);
        }

        that.render();
      }, 400);
    },

    adsSignout: function () {
      this.trigger('ads-signout');
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
      _.bindAll(this, ["handleUserAnnouncement", "getOrcidUsername"]);
      this.beehive = beehive;
      this.pubsub = beehive.getService("PubSub");
      this.pubsub.subscribe(this.pubsub.USER_ANNOUNCEMENT, this.handleUserAnnouncement);
      this.pubsub.subscribe(this.pubsub.APP_STARTED, this.getOrcidUsername);

      this.setInitialVals();
      this.pubsub = beehive.getService('PubSub');
      this.pubsub.subscribe(this.pubsub.USER_ANNOUNCEMENT, _.bind(this.handleUserAnnouncement, this));
    },

    //to set the correct initial values for signed in statuses
    setInitialVals: function () {
      var user = this.getBeeHive().getObject("User");
      var orcidApi = this.getBeeHive().getService("OrcidApi");
      var hasAccess = orcidApi.hasAccess();
      this.model.set({orcidModeOn: user.isOrcidModeOn() && hasAccess, orcidLoggedIn: hasAccess}, {silent: true});
    },

    getOrcidUsername: function () {
      var that = this;
      var orcidApi = this.beehive.getService("OrcidApi");
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
        })
      }
    },

    handleUserAnnouncement: function (msg, data) {

      var user = this.beehive.getObject("User");
      var orcidApi = this.beehive.getService("OrcidApi");

      if (msg === "user_info_change" && data === "USER") {
        //if user logs out, username will be undefined
        this.model.set("currentUser", this.beehive.getObject("User").getUserName());
      }
      else if (msg == 'orcidUIChange') {
        this.model.set({orcidModeOn: user.isOrcidModeOn(), orcidLoggedIn: orcidApi.hasAccess()});
        if (this.model.get("orcidLoggedIn")) {
          this.getOrcidUsername();
        }
      }
    },

    viewEvents: {
      'ads-signout': 'signOut',
      "navigate-to-orcid-link": "navigateToOrcidLink",
      "logout-only-orcid": "orcidLogout",
      'search-author': 'searchAuthor'
    },

    modelEvents: {
      "change:orcidModeOn": "toggleOrcidMode"
    },

    toggleOrcidMode: function () {
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
      this.beehive.getService("OrcidApi").signOut();
      this.beehive.getObject("User").setOrcidMode(false);
      this.model.set('orcidModeOn', false);
    },

    navigateToOrcidLink: function () {
      this.pubsub.publish(this.pubsub.NAVIGATE, "orcid-page")
    }

  });

  return NavWidget;

});