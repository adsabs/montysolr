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
          view : undefined,
          publicView : false
        }
      }

    });

    var Library = BaseWidget.extend({

      initialize :function(options){
        var options = options || {};
        this.view = new ContainerView();
        this.model = new StateModel();

        //need access to it persistantly
        this.libraryCollection = new LibraryView.Collection();
        this.headerModel = new HeaderView.Model();

        //need to change both header and subview
        this.listenTo(this.model, "change:id", this.updateView);
        //change only subview
        this.listenTo(this.model, "change:view", this.updateSubView);
      },

      activate: function(beehive) {
        this.setBeeHive(beehive);
        _.bindAll(this);
<<<<<<< HEAD
      },

      updateHeader : function(){
        var that = this;
        var id = this.model.get("id"),
            metadata = this.getBeeHive().getObject("LibraryController").getLibraryMetadata(id)
              .done(function(metadata){
=======
        this.pubsub = beehive.getService('PubSub');

      },

      //update header + subview
      updateView : function(){
>>>>>>>  basic library functionality, minus pagination for libraries, mostly working

        var id = this.model.get("id"), that = this;

        this.beehive.getObject("LibraryController").getLibraryData(id)

          .done(function(data){

            /*
            * the order of these functions are important,
            * should go back later and standardize events for views
            * so that the order doesnt matter
            * the question is :new view every time header model/collection changes? (like header)
            * or just have view listen to changes on model/collection? (like library collection view)
            * */


            that.headerModel.set(_.extend(data.metadata,
              {active : that.model.get("view"),
                publicView : that.model.get("publicView")}
            ));

            //need to do in this order or else old views keep reacting to reset events
            that.updateHeader();

            //replace the old view with the new
            that.updateSubView();

            that.libraryCollection.reset(data.solr.response.docs);

          });

      },

      updateHeader : function(a){

        var header = new HeaderView({model : this.headerModel});
        header.on("all", this.handleHeaderEvents, this);
        this.view.header.show( header );

      },

      updateSubView : function(){

        var that = this,
            id = this.model.get("id"),
            view = this.model.get("view");

        switch (view) {

          case "library":
<<<<<<< HEAD
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
=======
                var permission = that.headerModel.get("permission"),
                    editRecords = !!_.contains(["write", "admin", "owner"], permission) && !this.model.get("publicView"),
                    subView = new LibraryView({collection : that.libraryCollection, permission : editRecords });

>>>>>>>  basic library functionality, minus pagination for libraries, mostly working
                subView.on("all", that.handleLibraryEvents, that);
                subView.vid = _.uniqueId();
                that.view.main.show(subView);
            break;

          case "admin":
            var subView = new AdminView({ model : this.headerModel });
            subView.on("all", that.handleAdminEvents, that);
            this.view.main.show( subView );
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

      setSubView  : function(data) {

        this.model.set( "publicView", data.publicView );
        this.model.set("view", data.view);

        if (data.id) {
          this.model.set("id", data.id);
        }

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
                modelToRemove = that.libraryCollection.get(bibcode);
                that.libraryCollection.remove(modelToRemove);
            });
          break;
      }

    },

      handleAdminEvents : function (event, arg1, arg2) {

        var that = this;

        switch (event) {

          case "update-public-status":

            var data = {"public": arg1},
                id = this.model.get("id");
            this.beehive.getObject("LibraryController")
              .updateLibraryMetadata(id, data)
              .done(function(response, status){
                //re-render the admin view
                that.headerModel.set("public", response.public);

              })
              .fail(function(){
                debugger
              });
            break

        }

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
<<<<<<< HEAD
            var pubsub = this.getPubSub();
=======

            //set the proper view value into the model
            this.model.set("view", arg1);

>>>>>>>  basic library functionality, minus pagination for libraries, mostly working
            var other = ["export", "metrics", "visualization"];
            var publicView = this.model.get("publicView");
            if (_.contains(other, arg1)){
<<<<<<< HEAD
              var command = "library-" + arg1;
              pubsub.publish(pubsub.NAVIGATE, command, {bibcodes : this.bibcodeCollection.pluck("bibcode"), sub : arg2, id : id});
            }
            else {
              pubsub.publish(pubsub.NAVIGATE, "IndividualLibraryWidget", { sub : arg1, id : id });
            }
            break;
          case "delete-library":
            this.getBeeHive().getObject("LibraryController").deleteLibrary(id);
=======

              var command =  "library-" + arg1;
              this.pubsub.publish(this.pubsub.NAVIGATE, command, {bibcodes : this.libraryCollection.pluck("bibcode"), sub : arg2, id : id, publicView : publicView});
            }
            else {
              this.pubsub.publish(this.pubsub.NAVIGATE, "IndividualLibraryWidget", { sub : arg1, id : id, publicView : publicView });
            }
            break

          case "delete-library":
            this.beehive.getObject("LibraryController").deleteLibrary(id, this.headerModel.get("name"));
>>>>>>>  basic library functionality, minus pagination for libraries, mostly working
            break;
        }
      }


    });

    return Library



})