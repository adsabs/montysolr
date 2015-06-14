define([
    "marionette",
    "js/widgets/base/base_widget",
    "./views/view_all_libraries",

  ],
  function(
    Marionette,
    BaseWidget,
    LibrariesView
    ){



    var ContainerView  = Marionette.LayoutView.extend({

      className : "all-libraries-widget s-all-libraries-widget",

      template : function(){return "<div class=\"all-libraries-container\"></div>"},

      regions : {
        container : ".all-libraries-container"
      }


    });


    var LibrariesWidget = BaseWidget.extend({

      initialize :function(options){
        var options = options || {};
        this.view = new ContainerView();

      },

      activate: function(beehive) {
        this.beehive = beehive;
        _.bindAll(this);
        this.pubsub = beehive.getService('PubSub');
        var pubsub = this.pubsub;
        pubsub.subscribe(pubsub.LIBRARY_CHANGE, this.refreshLibrariesSubView);
      },

      refreshLibrariesSubView : function(){
        if (this._subView instanceof LibrariesView && $.contains(document.body, this._subView.el)){
          this.setSubView("libraries");
        }
      },

      setSubView  : function(view) {

        var that = this;

        switch (view) {

          case "libraries":
            this.beehive.getObject("LibraryController").getLibraryMetadata()
              .done(function(data){
              that._subView = new LibrariesView({libraries: data});
              that._subView.on("all", that.handleSubViewEvents);
              that.view.container.show(that._subView);
          });
            break;
        }

      },

      handleSubViewEvents : function (event, arg1, arg2) {

        switch (event) {

          case "navigate:library":
            //where arg1 = library's id
            this.pubsub.publish(this.pubsub.NAVIGATE, "IndividualLibraryWidget", {sub : "library", id : arg1});
            break
        }

      }



      });


    return LibrariesWidget


  })