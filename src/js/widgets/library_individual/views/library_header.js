define([
  "marionette",
  "hbs!../templates/library-header",
  "bootstrap"

], function(
  Marionette,
  LibraryHeaderTemplate,
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

    initialize : function(options) {

      options = options || {};

    },

    template : LibraryHeaderTemplate,

    events : {
      "click *[contenteditable]" : "focusContenteditable",
      "click .submit-edit" : "submitEdit",
      "click .cancel-edit" : "cancelEdit",
      "click li[data-tab]:not(.active)" : "triggerSubviewNavigate",
      "click .delete-library" : "triggerDeleteLibrary"

    },

    formatDate : function(d){

      var d = new Date(d);

      function formatAMPM(date) {
        var hours = date.getHours();
        var minutes = date.getMinutes();
        var ampm = hours >= 12 ? 'pm' : 'am';
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
      data.date_last_modified = this.formatDate(data.date_last_modified);
      data.date_created = this.formatDate(data.date_created);
      return data;

    },

    focusContenteditable : function(e){

      var $current = $(e.currentTarget),
          buttonsContainer = $current.next();
          buttonsContainer.removeClass("no-show").addClass("fadeIn");

    },

    submitEdit : function(e){

      var $current = $(e.currentTarget),
          $buttonContainer = $current.parent(),
          $edited = $buttonContainer.prev(),
          data = {};

      data[$edited.data("param")] = $edited.text().trim();

      this.trigger("updateVal", data);

      $buttonContainer.html("<i class=\"fa fa-spinner fa-pulse\"></i>");

    },

    cancelEdit : function(e){

      var $current = $(e.currentTarget),
          $buttonContainer = $current.parent(),
          $edited = $buttonContainer.prev();

      //return the value to original value
      $edited.text(this.model.get($edited.data("param")));
      $buttonContainer.addClass("no-show");
    },

    triggerSubviewNavigate : function(e){
      var $current = $(e.currentTarget);
      this.$(".tab.active").removeClass("active");
      $current.addClass("active");

      var subView  = $current.data("tab");
      var tabToShow, additional;
      //dropdowns have multiple sub-options
      if  (subView.indexOf("-") > -1) {
        tabToShow =  subView.split("-")[0];
        //this tells other widget which view to show
        additional  = subView.split("-")[1];
      }
      else {
        tabToShow = subView;
      }

      this.model.set("active", tabToShow);
      this.trigger("navigate", tabToShow, additional);
    },

    triggerDeleteLibrary : function(){

      this.trigger("delete-library");
    }

  });

  LibraryTitleView.Model = LibraryTitleModel


  return LibraryTitleView;

})