define([
  "marionette",
  "js/widgets/base/base_widget",
  "hbs!./templates/preferences",
  "./views/openurl"
], function (
  Marionette,
  BaseWidget,
  PreferencesTemplate,
  OpenURLView
  ) {

  var PreferencesModel = Backbone.Model.extend({


  });

  var PreferencesView = Marionette.LayoutView.extend({

    template: PreferencesTemplate,

    className: "preferences-widget s-preferences-widget",

    regions: {
      "openurl": ".openurl-container"
    },

    onRender: function () {
      // for now, just render the subviews, when there are more,
      // come up with some way to organize them (tabs/accordion/something else)
      var openurl = new OpenURLView({model: this.model, collection: Marionette.getOption(this, "openURLCollection")});
      this.getRegion("openurl").show(openurl);

      //forward events
      this.listenTo(openurl, "all", this.forwardEvents);
    },

    forwardEvents: function () {
      this.trigger.apply(this, arguments);
    }

  });


  var OpenURLCollection = Backbone.Collection.extend({


  });

  var PreferencesWidget = BaseWidget.extend({

    initialize: function (options) {
      options = options || {};

      this.openURLCollection = new OpenURLCollection();
      this.model = new PreferencesModel();

      this.view = new PreferencesView({model: this.model, openURLCollection: this.openURLCollection });
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
        debugger
        that.openURLCollection.reset(config);
      });

      //and the user data from myads
      this.model.set(this.getBeeHive().getObject("User").getUserData());

    },

    handleViewEvents: function (event, arg1, arg2) {
      if (event == "change:link_server") {
        this.getBeeHive().getObject("User").setUserData({link_server: arg1});
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