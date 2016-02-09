define([
    'marionette',
    'hbs!./templates/page_top_alert'
], function(
    Marionette,
    BannerTemplate
){

  var AlertView = Marionette.ItemView.extend({

    tagName : "span",
    className : "alert-banner",
    template: BannerTemplate,

    modelEvents : {
      'change' : 'render'
    },

    events: {
      'click #alertBox button.close': 'close'
    },

    close : function(){

      this.$(".alert").css("display", "none");
    },

    render : function(){
      if (this.model.get("modal")) return this;
      if (!this.model.get("msg") && !this.model.get("title")){
        this.$el.html("");
        return this
      };
      return Marionette.ItemView.prototype.render.apply(this, arguments);
      //log the error to console as well
      if (this.model.get("type") === "danger"){
        console.error("error feedback: ", this.model.get("title"), this.model.get("msg"));
      }
    }

  });

 return AlertView;

});

