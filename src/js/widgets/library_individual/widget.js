define([
    "marionette",
    "js/widgets/base/base_widget",
    "./views/library_header",
    "./views/manage_permissions",
    "./views/view_library",
    "hbs!./templates/layout-container"
  ],
  function(
  Marionette,
  BaseWidget,
  HeaderView,
  AdminView,
  LibraryView,
  ContainerTemplate

    ){



    var ContainerView  = Marionette.LayoutView.extend({

      className : "library-widget s-library-widget",

      template : ContainerTemplate,

      regions : {
        header  : ".header",
        main : ".main"
      }

    });

    var StateModel = Backbone.Model.extend({

      defaults : function(){
        return {
          id : undefined,
          view : undefined
        }
      }

    });

    var Library = BaseWidget.extend({

      initialize :function(options){
        var options = options || {};
        this.view = new ContainerView();
        this.model = new StateModel();

        //need access to it persistantly
        this.bibcodeCollection = new LibraryView.Collection();
        this.headerModel = new HeaderView.Model();

        this.listenTo(this.model, "change:id", this.updateHeader);

        this.listenTo(this.model, "change:id", this.updateView);
        this.listenTo(this.model, "change:view", this.updateView);
      },

      activate: function(beehive) {
        this.setBeeHive(beehive);
        _.bindAll(this);
      },

      updateHeader : function(){
        var that = this;
        var id = this.model.get("id"),
            metadata = this.getBeeHive().getObject("LibraryController").getLibraryMetadata(id)
              .done(function(metadata){

                that.headerModel.set(metadata);
                that.headerModel.set("active", that.model.get("view"));
                var header = new HeaderView({model : that.headerModel});
                header.on("all", that.handleHeaderEvents, that);
                that.view.header.show( header );

              });
      },

      updateView : function(){

        var that = this,
            id = this.model.get("id"),
            view = this.model.get("view");

        switch (view) {

          case "library":
            //get record data
            this.getBeeHive().getObject("LibraryController").getLibraryRecords(id)
              .done(function(data){
                data = _.map(data.documents, function(d){
                  return {bibcode : d}
                });

                var permission = that.headerModel.get("permission");
                var editRecords = !!_.contains(["write", "admin", "owner"], permission );
                var subView = new LibraryView({collection : that.bibcodeCollection, permission : editRecords });
                //cache it
                that._subView = subView;
                subView.on("all", that.handleLibraryEvents, that);
                subView.collection.reset(data);
                that.view.main.show(subView);
              });
            break;

          case "admin":
            var v = new AdminView();
            v.on("all", that.handleAdminEvents, that);
            this.view.main.show( v );
            break;

          //the following 3 cases insert different widgets, so empty the "main" container

          case "export":
            this.view.main.empty();
            break;

          case "metrics":
            this.view.main.empty();
            break;

          case "visualization":
            this.view.main.empty();
            break;

        }
      },

      setSubView  : function(view, id) {

        if (id){
          this.model.set("id", id, {silent : true});
        }

        this.model.set("view", view, {silent : true});

        //is there a better way to avoid duplicate updateView calls?
        this.updateHeader();
        this.updateView();
      },

      handleLibraryEvents : function (event, arg1, arg2){
      var that = this;

      switch (event) {

        case "removeRecord":
          //from library list view
          var data = {bibcode : [arg1], action : "remove"},
              id = this.model.get("id");
          this.getBeeHive().getObject("LibraryController").updateLibraryContents(id, data)
            .done(function(){
              var bibcode = data.bibcode[0],
                modelToRemove = that.bibcodeCollection.get(bibcode);
              that.bibcodeCollection.remove(modelToRemove);
            });
          break;
      }

    },

      handleAdminEvents : function (event, arg1, arg2) {


      },

      handleHeaderEvents : function (event, arg1, arg2) {

        var that = this, id = this.model.get("id");

        switch (event) {

          case "updateVal":
            //from header view
            this.getBeeHive().getObject("LibraryController")
              .updateLibraryMetadata(id, arg1)
              .done(function(data){
                //make a new view
                that.headerModel.set(data);
                var header = new HeaderView({ model : that.headerModel });
                header.on("all", that.handleHeaderEvents, that);
                that.view.header.show( header );

              });
            break;

          case "navigate":
            var pubsub = this.getPubSub();
            var other = ["export", "metrics", "visualization"];
            if (_.contains(other, arg1)){
              var command = "library-" + arg1;
              pubsub.publish(pubsub.NAVIGATE, command, {bibcodes : this.bibcodeCollection.pluck("bibcode"), sub : arg2, id : id});
            }
            else {
              pubsub.publish(pubsub.NAVIGATE, "IndividualLibraryWidget", { sub : arg1, id : id });
            }
            break;
          case "delete-library":
            this.getBeeHive().getObject("LibraryController").deleteLibrary(id);
            break;
        }
      }


    });


    return Library



})