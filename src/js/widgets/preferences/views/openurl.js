define([
  "marionette",
  "hbs!js/widgets/preferences/templates/openurl",
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

    className : "panel panel-default s-form-container",

    onRender : function(){
      this.$("select[name=set-link-server]").select2();
    },

    serializeData : function(){

      var data = this.model.toJSON();
      //either user data or openurl data has yet to load
      if (!data.openURLConfig || !data.user ){
        data.loading = true;
        return data
      }

      // in weird/buggy cases, the previously selected open url might not exist in our openURLCOnfig data
      // available now. So just act like there is no openURLName, which will prompt the user to select a new
      //openURL
      var current = _.findWhere(data.openURLConfig, {link : data.link_server});
      data.openURLName = current ? current.name : "";
      return data;

    },

    modelEvents : {
      "change:link_server" : "render",
      "change:user" : "render",
      "change:openURLConfig" : "render"
    },

    events : {
      "click #link-server-container .submit" : "changeLinkServer"
    },

    changeLinkServer : function(e){

      e.preventDefault();
      var newVal = this.$("#link-server-container select").val();

      //in the case that someone re-selected their link server a second time
      if  (newVal === this.model.get("link_server")){
        //just close the panel
        this.render();
        return;
      }
      this.trigger("change:link_server", newVal);

      var loadingString = '<i class="fa fa-spinner fa-pulse"></i> Loading';
      this.$("#link-server-container .submit").html(loadingString);
      //loading string will be removed when view is re-rendered

    }


  });

  return OpenURLView;

});
