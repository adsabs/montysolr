
define([
    'underscore',
    'js/widgets/list_of_things/details_widget'
  ],

  function (  _,  ListOfThingsWidget) {

    var Widget = ListOfThingsWidget.extend({
      queryOperator : "references",
      sortOrder: "first_author asc",
      description : "List of papers referenced by"
    });

    return Widget;

  });
