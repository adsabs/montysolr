define([
    "marionette",
    "js/widgets/base/base_widget",
    "./views/view_all_libraries"

  ],
  function(
    Marionette,
    BaseWidget,
    LibrariesView
    ){


    var LibraryModel = Backbone.Model.extend({

      defaults : function(){
        return {
          name: undefined,
          description: undefined,
          id : undefined,
          permission : undefined,
          num_papers : 0,
          date_created : undefined,
          date_last_modified : undefined
        }
      }

    });

    var LibraryContainerModel = Backbone.Model.extend({

      //this determines initial sort order

      defaults : {

        sort : "name",
        order: "asc",
        type: "string"
      }

    });

    var LibraryCollection = Backbone.Collection.extend({

      initialize : function(models, options){

        this.containerModel = new LibraryContainerModel();
        this.options = options;
      },

      model : LibraryModel,

      comparator : function(model1, model2){

        var sort = this.containerModel.get("sort"),
          type = this.containerModel.get("type"),
          order = this.containerModel.get("order");

        if (type == "string"){

          if (order == "asc"){
            return model1.get(sort).localeCompare(model2.get(sort));
          }
          else {
            return - model1.get(sort).localeCompare(model2.get(sort));
          }

        }
        else if (type == "date"){

          if (order == "asc"){
            return new Date(model1.get(sort)) - new Date(model2.get(sort));
          }
          else {
            return new Date(model2.get(sort)) - new Date(model1.get(sort));
          }

        }
        else if (type == "permission"){

          var permissionHierarchy = ["read", "write", "admin", "owner"];

          if (order == "asc"){
            return permissionHierarchy.indexOf(model1.get(sort)) - permissionHierarchy.indexOf(model2.get(sort));
          }
          else {
            return permissionHierarchy.indexOf(model2.get(sort)) - permissionHierarchy.indexOf(model1.get(sort));;
          }

        }
        else if (type == "int"){

          if (order == "asc"){
            return model1.get(sort) - model2.get(sort);
          }
          else {
            return model2.get(sort) - model1.get(sort);
          }
        }
      }

    });

    //layout container

    var ContainerView  = Marionette.LayoutView.extend({

      className : "all-libraries-widget s-all-libraries-widget",

      template : function(){return "<div class=\"all-libraries-container\"></div>"},

      regions : {
        container : ".all-libraries-container"
      }


    });


    //widget controller

    var LibrariesWidget = BaseWidget.extend({

      initialize :function(options){
        var options = options || {};
        this.view = new ContainerView();
        this.libraryCollection = new LibraryCollection();
      },

      activate: function(beehive) {
        this.setBeeHive(beehive);
        _.bindAll(this);
        this.getPubSub().subscribe(this.getPubSub().LIBRARY_CHANGE, this.updateCollection);
      },

      updateCollection : function(data){
        this.libraryCollection.reset(data);
      },

      setSubView  : function(data) {

        var view = data.view;

        switch (view) {

          case "libraries":
            var subView = new LibrariesView({ collection: this.libraryCollection, model : this.libraryCollection.containerModel });
            subView.on("all", this.handleSubViewEvents);
            this.view.container.show(subView);
            break;
        }

      },

      handleSubViewEvents : function (event, arg1, arg2) {

        switch (event) {

          case "navigate:library":
            //where arg1 = library's id
            this.getPubSub().publish(this.getPubSub().NAVIGATE, "IndividualLibraryWidget", {sub : "library", id : arg1});
            break
        }

      }

      });


    return LibrariesWidget


  })