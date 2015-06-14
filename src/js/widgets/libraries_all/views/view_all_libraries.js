define([
  "marionette",
  "hbs!../templates/libraries-list-container",
  "hbs!../templates/library-item",
  "hbs!../templates/no-libraries"

], function(
  Marionette,
  LibraryContainer,
  LibraryItem,
  NoLibrariesTemplate

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


  var LibraryCollection = Backbone.Collection.extend({

    initialize : function(models, options){

      if (!options.containerModel){
        throw new Error("didn't provide acccess to the parent model to know what sort val to use")
      }
      this.options = options;
    },

    model : LibraryModel,

    comparator : function(model1, model2){

      var sort = this.options.containerModel.get("sort"),
          type = this.options.containerModel.get("type"),
          order = this.options.containerModel.get("order");

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

  var LibraryItemView = Marionette.ItemView.extend({

    formatDate : function(d){
      var d = new Date(d);

      function formatAMPM(date) {
        var hours = date.getHours();
        var minutes = date.getMinutes();
        var ampm = hours >= 12 ? 'p' : 'a';
        hours = hours % 12;
        hours = hours ? hours : 12; // the hour '0' should be '12'
        minutes = minutes < 10 ? '0'+minutes : minutes;
        var strTime = hours + ':' + minutes  + ampm;
        return strTime;
      }

      var year = d.getFullYear().toString().slice(2,4),
          month = d.getMonth() + 1,
          day = d.getDay(),
          time = formatAMPM(d);

      return month + "/" + day + "/" + year + " " + time;

    },

    serializeData : function(){

      var data = this.model.toJSON();
      data.libNum = Marionette.getOption(this, "libNum")
      data.date_last_modified = this.formatDate(data.date_last_modified);
      return data;
    },

    template : LibraryItem,

    tagName : "tr",

    triggers : {
      "click" : "navigateToLibrary"
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


  var LibraryCollectionView = Marionette.CompositeView.extend({

    initialize : function(options){

      var options = options || {};

      if (!options.libraries){
        throw new Error("Libraries List widget was initialized without library list data-- this is not supported")
      }

      this.model = new LibraryContainerModel();

      this.collection = new LibraryCollection(options.libraries, {containerModel : this.model});

    },

    template : LibraryContainer,

    childViewContainer : ".libraries-list-container tbody",

    childView : LibraryItemView,

    childViewOptions: function(model, index) {
      return {
        libNum : index + 1
      }
    },

    childEvents : {
      "navigateToLibrary" : "triggerNavigate"
    },

    events : {
      "click thead button" : "sortCollection"
    },

    modelEvents :   {
      "change" : function(){
        this.collection.sort();
        this.render();
      }
    },

    triggerNavigate : function(childView){
      this.trigger("navigate:library", childView.model.get("id"));
    },

    sortCollection : function(e){

      var sortData = $(e.currentTarget).data("sort"), sort = sortData.sort;

      var order =  (sort !== this.model.get("sort") ) ? "asc" : (this.model.get("order") == "asc") ? "desc" : "asc";
      this.model.set({"sort" : sort, type : sortData.type, "order" : order});

    },

    render : function(){

      if (!this.collection.length){
        this.$el.html(NoLibrariesTemplate());
        return this
      }
      else {
        return Marionette.CompositeView.prototype.render.apply(this, arguments);
      }
    }

  });

  return LibraryCollectionView;

})