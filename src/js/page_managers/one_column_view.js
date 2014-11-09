define([
    "marionette",
    "hbs!./templates/landing-page-layout",
    'js/widgets/base/base_widget'
  ],
  function (Marionette,
            pageTemplate,
            BaseWidget
            ) {

    var OneColumnView = Marionette.ItemView.extend({

      initialize : function(options){
        var options = options || {};
        this.widgets = options.widgets;
      },

      template : pageTemplate,

      onRender : function(){
        var self = this;
        //var widgets = this.getWidgetsFromTemplate(this.$el,
        //  !Marionette.getOption(this, "debug"));
        //_.extend(this.widgets, widgets);

      }



    });
    return OneColumnView;

  });