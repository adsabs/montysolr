define([
  "marionette",
  "hbs!./nav_template"
], function(
  Marionette,
  NavTemplate
  ){

  var NavModel = Backbone.Model.extend({

    defaults : function(){
      return {
        "page" : undefined,
        "userName" : undefined
      }
    }
  })

  var NavView = Marionette.ItemView.extend({

    template : NavTemplate,

    modelEvents : {
      "change" : "render"
    }

  });

  var NavWidget = Marionette.Controller.extend({

    initialize :function(options){
      options = options || {};
      this.model = new NavModel();
      this.view = new NavView({model : this.model});
    },

    activate: function (beehive) {
      this.beehive = beehive;
      this.pubsub = beehive.Services.get('PubSub');

      _.bindAll(this);

      //custom dispatchRequest function goes here
      this.pubsub.subscribe(this.pubsub.PAGE_CHANGE, this.updateCurrentView);
      this.pubsub.subscribe(this.pubsub.USER_ANNOUNCEMENT, this.updateUser)
    },

    updateCurrentView : function(page){
      this.model.set("page", page);
    },

    updateUser : function(event, arg ){

      if (event == "user_signed_in"){
        var userName = arg;
        if (userName && userName.indexOf("@") > -1){
          userName = userName.split("@")[0];
        }
        this.model.set("user", userName);
      }
      else if (event == "user_signed_out"){
        this.model.set("user", undefined);
      }
    },

    render : function(){
      this.view.render();
      return this.view.el;
    }


  })

  return NavWidget

})