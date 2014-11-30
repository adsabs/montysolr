define([
  'marionette',
  'js/widgets/base/base_widget',
  'hbs!./templates/metrics_container',
  'hbs!./templates/table',
  'bootstrap'
], function(
  Marionette,
  BaseWidget,
  MetricsContainer,
  TableTemplate

  ){


  var TableModel = Backbone.Model.extend({

    defaults : function(){
      return {
        title : [],
        rows : []
      }
    }
  })


  var TableView = Marionette.ItemView.extend({

    template : TableTemplate,

    //turn second item in each row
    //into a help popover

    serializeData : function(){

      var data = this.model.toJSON();

      _.each(data.rows, function(r,i){

        _.each(r, function(d, ii) {

          if(ii === 1){

            var description = data.rows[i][ii];

            var title = data.rows[i][ii - 1];

            var markup =

            data.rows[i][ii]
          }

        })


      })
    }


  })


  var GraphView = Marionette.ItemView.extend({


  })


  var ContainerView = Marionette.Layout.extend({


  })


  var MetricsWidget = BaseWidget.extend({

     components : {

      TableModel : TableModel,

      TableView : TableView,

      GraphView : GraphView,

      ContainerView : ContainerView

    },




  });


  return MetricsWidget;



})