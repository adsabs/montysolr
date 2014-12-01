define([
  'marionette',
  'js/widgets/base/base_widget',
  './tooltip_data',
  'hbs!./templates/metrics_container',
  'hbs!./templates/paper_table',
  'hbs!./templates/citations_table',
  'hbs!./templates/indices_table',
  'hbs!./templates/reads_table',

  'bootstrap'
], function(
  Marionette,
  BaseWidget,
  TooltipData,
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

    template : TableTemplate



  })


  var GraphView = Marionette.ItemView.extend({


  })


  var ContainerView = Marionette.Layout.extend({


  })


  var MetricsWidget = BaseWidget.extend({



    initialize : function(options){

    },

    createTableViews : function(response){

      function createRow (name, data, differentTitle){

        if (differentTitle){
          return [differentTitle, TooltipData[name], data.total[name], data.refereed[name]]
        }
        else {
          return [name, TooltipData[name], data.total[name], data.refereed[name]]
        }

      }

      var generalData = {refereed : response["refereed stats"], total : response["all stats"]};
      var readsData =   {refereed : response["refereed reads"], total : response["all reads"]};

      //initialize table views
      <td>
      Number of Papers
      </td>

      this.papersTableView =  new TableView({model : new TableModel(paperModelData)});

      var readsModelData = {
        title : ["Reads", "Help", "Total", "Refereed"],
        rows : [
          createRow("Total number of reads", readsData),
          createRow("Average number of reads", readsData),
          createRow("Median number of reads", readsData),
          createRow("Total number of downloads", readsData),
          createRow("Average number of downloads", readsData),
          createRow("Median number of downloads", readsData)

        ]
      };

      this.readsTableView = new TableView({model : new TableModel(readsModelData)});

      var citationsModelData = {
        title : ["Citations", "Help", "Total", "Refereed"],
        rows : [
          createRow("Number of citing papers", generalData),
          createRow("Total citations", generalData),
          createRow("Number of self-citations", generalData),
          createRow("Average citations", generalData),
          createRow("Median citations", generalData),
          createRow("Normalized citations", generalData),
          createRow("Refereed citations", generalData),
          createRow("Average refereed citations", generalData),
          createRow("Median refereed citations", generalData),
          createRow("Normalized refereed citations", generalData)
        ]

      };

      this.citationsTableView = new TableView({model : new TableModel(citationsModelData)});

      var indicesData = {
        title : ["Indices", "Help", "Total", "Refereed"],
        rows : [
          createRow("H-index", generalData),
          createRow("m-index", generalData),
          createRow("g-index", generalData),
          createRow("i10-index", generalData),
          createRow("i100-index", generalData),
          createRow("tori index", generalData),
          createRow("riq index", generalData),
          createRow("read10-index", generalData)

        ]

      }
      this.indicesTableView = new TableView({model : new TableModel(indicesData)});


      //provide them with data

    },

    processResponse: function(response){

      response = response.toJSON();

      this.createTableViews(response);

//      this.createGraphViews(response);
//
//      this.insertViews();




      this.papersGraphView = new GraphView();
      this.readsTableView = new GraphView();
      this.citationsTableView = new GraphView();
      this.indicesTableView = new GraphView();



    },

     //so I can test these individually
     components : {

      TableModel : TableModel,

      TableView : TableView,

      GraphView : GraphView,

      ContainerView : ContainerView

    },





  });


  return MetricsWidget;



})