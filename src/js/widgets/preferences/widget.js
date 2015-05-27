define([
  "marionette",
  "js/widgets/base/base_widget",
  "hbs!./preferences"
], function(
  Marionette,
  BaseWidget,
  PreferencesTemplate
  ){

  var PreferencesView = Marionette.ItemView.extend({

    template : PreferencesTemplate

  });

  var PreferencesWidget = BaseWidget.extend({

    initialize : function(options){
      options = options || {};
      this.view = new PreferencesView();
      BaseWidget.prototype.initialize.apply(this, arguments);
    },

    processResponse : function(){

    }


  });


  return PreferencesWidget

})