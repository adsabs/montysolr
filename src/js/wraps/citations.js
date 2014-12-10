
define([
    'underscore',
    'js/widgets/list_of_things/details_widget'
  ],

  function (  _,  ListOfThingsWidget) {

    var Widget = ListOfThingsWidget.extend({
      queryOperator : "citations",
      sortOrder : "pubdate desc",
      description : "List of papers which cite",

      initialize : function(options){

        this.on('page-manager-message', function(event, data){
          if (event === "article-title"){
            this.model.set("title", data.title);

          }
        });

        ListOfThingsWidget.prototype.initialize.call(this, options);

      }
    });

    return Widget;

  });
