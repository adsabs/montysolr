define([
  "marionette",
  "hbs!../templates/openurl",
  "bootstrap",
  "select2"
], function(
  Marionette,
  OpenURLTemplate,
  Bootstrap,
  Select2
  ){

  var OpenURLView = Marionette.ItemView.extend({

    template : OpenURLTemplate,

    onRender : function(){
      this.$("select[name=set-link-server]").select2();
    },

    serializeData : function(){
      var data = this.model.toJSON();
      data.openURLConfig = this.collection.toJSON();

      var current = _.findWhere(data.openURLConfig, {link : data.link_server});
      data.openURLName = current ? current.name : "";

      return data
    },

    collectionEvents : {
      "reset" : "render"
    },

    modelEvents : {
      "change" : "render"
    },

    events : {
      "click #link-server-container .submit" : "changeLinkServer"
    },

    changeLinkServer : function(e){

      e.preventDefault();
      this.trigger("change:link_server",  this.$("#link-server-container select").val())

      var loadingString = '<i class="fa fa-spinner fa-pulse"></i> Loading';
      this.$("#link-server-container .submit").html(loadingString);
      //loading string will be removed when view is re-rendered

    }


  });

  return OpenURLView;


})