define([
  "marionette",
  "hbs!js/widgets/library_individual/templates/manage-permissions-container",
  "hbs!js/widgets/library_individual/templates/make-public",
  "bootstrap"
], function(
  Marionette,
  ManagePermissionsContainer,
  MakePublicTemplate,
  Bootstrap
  ){

  var PermissionsModel = Backbone.Model.extend({

  });

  var PermissionsCollection = Backbone.Collection.extend({

    model : PermissionsModel

  });


  var ManagePermissionsView = Marionette.ItemView.extend({

    className : "library-admin-view",

    initialize : function(options){

      var options = options || {};

      this.model.set( "host", window.location.host );

    },

    events : {
      "click .public-button" : "togglePublicState"
    },

    togglePublicState : function(e){

      var public = $(e.target).hasClass("make-public");
      this.trigger("update-public-status", public);

    },

    modelEvents : {

      "change:public" : "render"

    },

    template :  ManagePermissionsContainer,

    onRender : function(){

      this.$(".public-container").html(MakePublicTemplate(this.model.toJSON()));

    }

  });


  return ManagePermissionsView


});
