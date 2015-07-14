define([
  "marionette",
  "js/widgets/base/base_widget",
  "hbs!./templates/preferences",
  "./views/openurl"
], function(
  Marionette,
  BaseWidget,
  PreferencesTemplate,
  OpenURLView
  ){

  var PreferencesModel = Backbone.Model.extend({


  });

  var PreferencesView = Marionette.LayoutView.extend({

    template : PreferencesTemplate,

    className : "preferences-widget s-preferences-widget",

    regions : {
      "openurl" : ".openurl-container"
    },

    onRender : function(){
      // for now, just render the subviews, when there are more,
      // come up with some way to organize them (tabs/accordion/something else)
      var openurl = new OpenURLView({model : this.model, collection : Marionette.getOption(this, "openURLCollection")});
      this.getRegion("openurl").show( openurl );

      //forward events
      this.listenTo(openurl, "all", this.forwardEvents);

    },

    forwardEvents : function(){
      this.trigger.apply(this, arguments);
    }

  });



  var OpenURLCollection = Backbone.Collection.extend({


  });

  var PreferencesWidget = BaseWidget.extend({

    initialize : function(options){
      options = options || {};

      this.openURLCollection = new OpenURLCollection();

      this.model = new PreferencesModel();

      this.view = new PreferencesView({model : this.model, openURLCollection : this.openURLCollection });
      this.listenTo(this.view, "all", this.handleViewEvents );

      BaseWidget.prototype.initialize.apply(this, arguments);

    },


    activate: function(beehive) {
      this.beehive = beehive;
      _.bindAll(this);
      this.pubsub = beehive.getService('PubSub');
      var pubsub = this.pubsub;
      pubsub.subscribe(pubsub.USER_ANNOUNCEMENT, this.handleUserAnnouncement);
      pubsub.subscribe(pubsub.APP_STARTED, this.onAppStarted);

    },

    onAppStarted : function(){

      var that = this;
      this.beehive.getObject("User").getOpenURLConfig().done(function(config){

        that.openURLCollection.reset(config);

      }).fail(function(){

        });

    },

    handleViewEvents : function(event, arg1, arg2){

      if (event == "change:link_server"){
        this.beehive.getObject("User").setUserData({link_server : arg1});
      }

    },

    handleUserAnnouncement : function(event, arg2){

      if (event == "user_info_change") {
        this.model.set(arg2);
      }

    }

  });

  return PreferencesWidget

})