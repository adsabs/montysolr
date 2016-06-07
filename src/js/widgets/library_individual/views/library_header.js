define([
  "marionette",
  "hbs!../templates/library-header",
  "moment",
  "bootstrap"

], function(
  Marionette,
  LibraryHeaderTemplate,
  moment,
  Bootstrap

  ){


  var LibraryTitleModel = Backbone.Model.extend({

    initialize : function(vals, options){

      options = options  || {};
      this.on("change:permission", this.checkEditPermission);

    },

    defaults : function(){
      return {
        //admin, libraries,edit,metrics, or vis
        active : "library",

        //from api
        date_created : undefined,
        date_last_modified : undefined,
        description : undefined,
        id : undefined,
        name : undefined,
        num_documents : 0,
        num_users : 0,
        permission : "read",
        owner : undefined,
        public : false

      }
    },

    checkEditPermission : function(){

      if (this.get("permission") == "admin" || this.get("permission") == "owner"){
        this.set("edit", true);
      }
      else {
        this.set("edit", false)
      }
    }

  });

  var LibraryTitleView = Marionette.ItemView.extend({

    template : LibraryHeaderTemplate,

    events : {
      "click .editable-item .toggle-form" : "showForm",
      "click .editable-item .btn-success" : "submitEdit",
      "click .editable-item .btn-default" : "cancelEdit",
      "click li[data-tab]:not(.active)" : "triggerSubviewNavigate",
      "click .delete-library" : "triggerDeleteLibrary",
      "click .bigquery-export" : "triggerStartSearch"
    },

    modelEvents : {
      "change" : "render"
    },


    formatDate : function(d){
     return moment.utc(d).local().format("MMM D YYYY, h:mma");
    },

    serializeData : function(){

      var data = this.model.toJSON();
      data.date_last_modified = this.formatDate(data.date_last_modified);
      data.date_created = this.formatDate(data.date_created);
      return data;

    },

    showForm: function(e){
      $(e.target).parents().eq(1).find("form").removeClass("hidden");
    },

    submitEdit : function(e){

      e.preventDefault();
      var $target = $(e.target),
        $editParent = $target.parent().parent(),
        $edited = $editParent.find("input").length ?  $editParent.find("input") : $editParent.find("textarea"),
        data = {};

      data[$editParent.data("field")] = $edited.val();
      this.trigger("updateVal", data);
      $target.html("<i class=\"fa fa-spinner fa-pulse\"></i>");

    },

    cancelEdit : function(e){

      e.preventDefault();
      var $target = $(e.currentTarget),
          $form = $target.parent();

      $form.find("input").val(this.model.get("name"));
      $form.find("textarea").val(this.model.get("description"));
      $form.addClass("hidden");

    },



    triggerSubviewNavigate : function(e){

      var $current = $(e.currentTarget),
        subView  = $current.data("tab");

      var tabToShow, additional;
      //dropdowns have multiple sub-options
      if  (subView.indexOf("-") > -1) {
        tabToShow = subView.split("-")[0];
        //this tells other widget which view to show
        additional = subView.split("-")[1];
      }
      else {
        tabToShow = subView;
      }

      this.trigger("navigate", tabToShow, additional);
    },

    triggerDeleteLibrary : function(){
      this.trigger("delete-library");
    },

    triggerStartSearch : function(){
      this.trigger("start-search");
    }

  });

  LibraryTitleView.Model = LibraryTitleModel


  return LibraryTitleView;

});