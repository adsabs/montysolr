define([
    "marionette",
    "js/components/api_query",
    "js/widgets/base/base_widget",
    "./views/library_header",
    "./views/manage_permissions",
    "hbs!./templates/layout-container",
    "hbs!./templates/loading-library"
  ],
  function(
    Marionette,
    ApiQuery,
    BaseWidget,
    HeaderView,
    AdminView,
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
          subView : undefined,
          publicView : false
        }
      }

    });

    var Library = BaseWidget.extend({

      initialize :function(options){
        options = options || {};
        this.view = new ContainerView();
        this.model = new StateModel();

        //need to make sure view is rendered at least 1x before it shows a subview
        this.view.render();

        this.headerModel = new HeaderView.Model();
        this.headerView = new HeaderView({model : this.headerModel});
        this.view.header.show( this.headerView );

      },

      activate: function(beehive) {
        this.setBeeHive(beehive);
        _.bindAll(this);
        var pubsub = beehive.getService('PubSub');
        pubsub.subscribe(pubsub.LIBRARY_CHANGE, this.onLibraryChange);
        //now that beehive is present, attach event handlers to header view
        this.headerView.on("all", this.handleHeaderEvents, this);
      },

      onLibraryChange : function(collectionJSON, info){
        //record was deleted from within widget, just update metadata
        if (info.ev == "change" &&  info.id ==  this.model.get("id")){
          this.updateSubView();
        }
      },

      updateHeader : function(){

        var done = function done(metadata){
          //updating header
          this.headerModel.set(_.extend(metadata,
              { active : this.model.get("subView"),
                publicView : this.model.get("publicView")
              }
          ));

        }.bind(this);

        this.getBeeHive()
            .getObject("LibraryController")
            .getLibraryMetadata(this.model.get("id"))
            .done(done);

      },

      updateSubView : function(){

        this.view.main.empty();

        var that = this,
            id = this.model.get("id"),
            view = this.model.get("subView");

        if (!id || !view){
          console.warn("library widget's updateSubView called without requisite library id and view name in model");
          return
        }

        that.view.main.show(new LoadingView());

        //create header
        this.updateHeader();

        if (["library", "export", "metrics", "visualization", "citation_helper"].indexOf(view) > -1){
          that.view.main.empty();
        }
        else if (view === "admin"){
          var subView = new AdminView({model: this.headerModel});
          subView.on("all", that.handleAdminEvents, that);
          this.view.main.show(subView);
        }
        else {
          throw new Error("don't recognize that subview: ", view)
        }

      },

      /*
       * ****this is the only way to change the state of the view***
       * called by the navigator
       * */

      setSubView  : function(data) {

        //this can be used to refresh the view with new data or just to reflect
        //changes in the model that came from elsewhere, like a library change event

        if (data){
          //data must have {id : X, subview : X, publicView : X}
          data = _.extend({ subView : "library", publicView : false }, data);
          this.model.set(data);
        }

        this.updateSubView();

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
            libController = this.getBeeHive().getObject("LibraryController");

        switch (event) {

          case "updateVal":
            //from header view
            libController
                .updateLibraryMetadata(id, arg1)
                .done(function (data) {
                  that.headerModel.set(data);
                });
            break;

          case "navigate":

              this.model.set("subView", arg1);

              var data = {
                id: id,
                publicView: this.model.get("publicView"),
                //subview is dependent on the tab and is used exclusively by individuallibrarywidget
                //in the nav function
                // to figure out which tab to highlight
                subView: arg1
              };
                  /*
                  * these subviews require requesting bibcode data first
                  * */
                if (_.contains(["export", "metrics", "visualization", "citation_helper"], data.subView )) {

                    switch (arg1) {
                      case "export":
                        data.widgetName = "ExportWidget";
                        data.additional = { format : arg2, libid: id };
                        break;
                      case "visualization":
                        data.widgetName = arg2;
                        data.additional = {};
                        break;
                      case "metrics":
                        data.widgetName = "Metrics";
                        data.additional = {};
                        break;
                      case "citation_helper":
                        data.widgetName = "CitationHelper";
                        data.additional = { libid: id,
                          permission: this.headerModel.get('permission'),
                          libname: this.headerModel.get('name')
                        };
                        break;
                    }
                    pubsub.publish(pubsub.NAVIGATE, "library-" + arg1, data);
                }

                else if (data.subView === "library"){
                  pubsub.publish(pubsub.NAVIGATE, "IndividualLibraryWidget", data);
                }
              else if ( data.subView ===  "admin"){
                  pubsub.publish(pubsub.NAVIGATE, "LibraryAdminView", data);
                }

              break;
              case "delete-library":
               libController.deleteLibrary(id, this.headerModel.get("name"));
                break;

          case "start-search":
                libController.getLibraryBibcodes(this.model.get("id")).done(function (bibcodes) {
                  var query = new ApiQuery({
                    __bigquery : bibcodes,
                    __bigquerySource : 'Library: ' + that.headerModel.get("name"),
                     sort : 'date desc'
                  });
                  pubsub.publish(pubsub.START_SEARCH, query);
                });
            }
        }
    });

    return Library

  });
