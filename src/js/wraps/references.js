
define([
    'underscore',
    'js/widgets/list_of_things/details_widget'
  ],

  function (  _,  ListOfThingsWidget) {

    var Widget = ListOfThingsWidget.extend({
      queryOperator : "references",
      sortOrder: "first_author asc",
      description : "Papers referenced by",
      //show how to get this info from solr
      operator : true
    });

    return Widget;

  });
