
define([
    'underscore',
    'js/widgets/list_of_things/details_widget'
  ],

  function (  _,  ListOfThingsWidget) {

    var Widget = ListOfThingsWidget.extend({
      queryOperator : "trending"
    });

    return Widget;

  });
