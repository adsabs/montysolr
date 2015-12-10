define([

    "marionette",
    "js/components/api_query",
    "js/widgets/base/base_widget",
    "./views/library_header",
    "./views/manage_permissions",
    "./views/view_library",
    "hbs!./templates/layout-container",
    "hbs!./templates/loading-library",
  ],
  function(

    Marionette,
    ApiQuery,
    BaseWidget,
    HeaderView,
    AdminView,
    LibraryView,
    ContainerTemplate,
    LoadingTemplate

    ){

    var LoadingView = Marionette.ItemView.extend({
      template : LoadingTemplate
    });

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
        options = options || {};
        this.view = new ContainerView();
        this.model = new StateModel();

        //need access to it persistently, this is a collection of records within a library
        this.libraryCollection = new LibraryView.Collection();
        this.headerModel = new HeaderView.Model();

        //need to make sure view is rendered at least 1x before it shows a subview
        this.view.render();
      },

      activate: function(beehive) {
        this.setBeeHive(beehive);
        _.bindAll(this);
        var pubsub = beehive.getService('PubSub');
        pubsub.subscribe(pubsub.LIBRARY_CHANGE, this.onLibraryChange);
      },

      onLibraryChange : function(collectionJSON, info){

        if (info.ev == "change" &&
            info.id ==  this.model.get("id") &&
            _.findWhere(collectionJSON, {id : info.id}).num_documents > this.headerModel.get("num_documents")
          ){
          this.updateWidget();
        }
        //record was deleted from within widget, just update metadata
        else if (info.ev == "change" &&  info.id ==  this.model.get("id")){
          this.syncHeader(collectionJSON);
        }
        //this is a bit inexact: reset could have occured because initial library collection was loaded
        // (in which case we need to update view and sub view)
        //or it could have been a library added event, which we dont care about
        //revisit later
        else if (info.ev == "reset"){
          this.updateWidget();
        }
      },

      //called when ID changes
      updateWidget : function() {
        this.switchToNewLib();
        this.updateSubView();
      },

      //reset widget, update header view
      switchToNewLib : function(){

        var LibraryController = this.getBeeHive().getObject("LibraryController"),
            id = this.model.get("id");

        this.libraryCollection.reset();
        this.view.header.empty();
        this.view.main.empty();

        //PUBLIC LIBRARY
        //return--> library data + metadata will be fetched in updateSubView,
        //which will call createHeader
        if (this.model.get("publicView")) return;

        //PRIVATE LIBRARY
        if (!LibraryController.isDataLoaded()){
          //wait for LIBRARY_CHANGE event
          return
        }

        // could be for any view -- library, export, metrics, etc--so insert
        // the correct header now

        var metadata = _.findWhere(LibraryController.getAllMetadata(), {id : id});
        this.createHeader(metadata);

      },

      createHeader : function(metadata){

        var that = this;

        //updating header
        that.headerModel.set(_.extend(metadata,
          { active : that.model.get("view"),
            publicView : that.model.get("publicView")
          }
        ));

        var header = new HeaderView({model : this.headerModel});
        header.on("all", this.handleHeaderEvents, this);
        this.view.header.show( header );

      },

      //respond to library_collection change event

      syncHeader : function(data){

        var currentLibMetadata = _.findWhere(data, {id : this.model.get("id")});
        this.headerModel.set(currentLibMetadata);
        //only needs to render if it's currently in the DOM
        if (this.view.header.currentView && $("body").find(this.view.header.currentView.el).length > 0){
          this.view.header.currentView.render();
        }

      },

      updateSubView : function(){

        var that = this,
            id = this.model.get("id"),
            view = this.model.get("view"),
            LibraryController = that.getBeeHive().getObject("LibraryController");

        if (!id || !view){
          console.warn("library widget's updateSubView called without requisite library id and view name");
          return
        }

        //let header model know
        that.headerModel.set("active", view);

        switch (view) {

          case "library":
            var public = this.model.get("publicView"),
                permission = that.headerModel.get("permission"),
                editRecords = !!_.contains(["write", "admin", "owner"], permission) && !public,
                subView = new LibraryView({collection : that.libraryCollection, permission : editRecords, perPage : Marionette.getOption(this, "perPage") });

            subView.on("all", that.handleLibraryEvents, that);

            //check to see if we already have records for a private lib, if not, fetch them
            if (!public && this.libraryCollection.length == 0 && this.headerModel.get("num_documents") !== 0) {
              //add the loading view
              that.view.main.show(new LoadingView());
              //now fetch the data
              LibraryController.getLibraryData(id).done(function (data) {
                that.libraryCollection.reset(data.solr.response.docs);
                //remove the loading view
                that.view.main.show(subView);
              });
            }
            else if (public){
              //add the loading view
              that.view.main.show(new LoadingView());
              //now fetch the data
              LibraryController.getLibraryData(id).done(function (data) {
                that.createHeader(data.metadata);
                that.libraryCollection.reset(data.solr.response.docs);
                //remove the loading view
                that.view.main.show(subView);
              }).fail(function(data){
                //the collection wasn't public
                that.getPubSub().publish(that.getPubSub().NAVIGATE, "404");
              });
            }
            else {
              //just show the view, presumably user is navigating back to list view
              //after already having been there (for this library)
              that.view.main.show(subView);
            }
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

      /*
       * ****this is the only way to change the state of the view***
       * called by the navigator
       * a change of ID will trigger the function "switchToNewLib"
       * a change of view will only trigger "updateSubView"
       * */

      setSubView  : function(data) {

        data.view = data.view ? data.view : "library";

        //could contain "view", "id", and/or "publicView"
        this.model.set(data);

        //if id changed, library changed --> need to destroy everything and start again
        if (this.model.changedAttributes().hasOwnProperty("id")) {
          //this calls both switchToNewLib and updateSubView
          this.updateWidget();
        }
        else {
          //just update sub view info
          this.updateSubView();
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
            this.getBeeHive().getObject("LibraryController")
              .updateLibraryMetadata(id, data)
              .done(function(response, status){
                //re-render the admin view
                that.headerModel.set("public", response.public);
              })
              .fail(function(){
              });
            break
        }
      },

      handleHeaderEvents : function (event, arg1, arg2) {

        var that = this,
            id = this.model.get("id"),
            pubsub = this.getBeeHive().getService('PubSub'),
            query;

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
            //set the proper view value into the model
            this.model.set("view", arg1);

            var other = ["export", "metrics", "visualization"];
            var publicView = this.model.get("publicView");
            if (_.contains(other, arg1)){

              var command =  "library-" + arg1;
              pubsub.publish(pubsub.NAVIGATE, command, {bibcodes : this.libraryCollection.pluck("bibcode"), sub : arg2, id : id, publicView : publicView});
            }
            else {
              pubsub.publish(pubsub.NAVIGATE, "IndividualLibraryWidget", { sub : arg1, id : id, publicView : publicView });
            }
            break

          case "delete-library":
            this.getBeeHive().getObject("LibraryController").deleteLibrary(id, this.headerModel.get("name"));
            break;
          case "start-search":

            var query = new ApiQuery({
              __bigquery : this.libraryCollection.pluck("bibcode")
            });

            pubsub.publish(pubsub.START_SEARCH, query);
        }
      }

    });

    return Library

  });