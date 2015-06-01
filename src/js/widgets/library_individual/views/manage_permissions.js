define([
  "marionette",
  "hbs!../templates/manage-permissions-container"
], function(
  Marionette,
  ManagePermissionsContainer
  ){

  var PermissionsModel = Backbone.Model.extend({


  });

  var ManagePermissionsView = Marionette.ItemView.extend({

    className : "library-admin-view",

    initialize : function(options){

      var options = options || {};

    },

    template :  ManagePermissionsContainer

  });



  return ManagePermissionsView








})