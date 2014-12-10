
define([
    'underscore',
    'js/widgets/list_of_things/details_widget'
  ],

  function (  _,  ListOfThingsWidget) {

    var Widget = ListOfThingsWidget.extend({
      queryOperator : "references",
      sortOrder: "first_author asc",

      description : "List of papers referenced by",

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
