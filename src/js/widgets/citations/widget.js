/**
 * Widget to display list of result hits - it allows to paginate through them
 * and display details
 *
 */

define([
    'underscore',
    'js/widgets/list_of_things/widget'
  ],

  function (  _,  ListOfThingsWidget) {

    var CitationsWidget = ListOfThingsWidget.extend({

      solrOperator : "citations"

    });

    return CitationsWidget;

  });
