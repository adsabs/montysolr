
define([
    'underscore',
    'js/widgets/list_of_things/details_widget'
  ],

  function (  _,  ListOfThingsWidget) {

    var Widget = ListOfThingsWidget.extend({
      queryOperator : "citations",
      sortOrder : "date desc",
      description : "List of papers which cite"
    });

    return Widget;

  });
