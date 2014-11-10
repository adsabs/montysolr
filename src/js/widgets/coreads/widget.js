/**
 * Widget to display list of result hits - it allows to paginate through them
 * and display details
 *
 */

define([
    'underscore',
    'js/widgets/list_of_things/temporary'
  ],

  function (
    _,
    ListOfThingsWidget) {

    var CoreadsWidget = ListOfThingsWidget.extend({

      solrOperator : "trending"

    });

    return CoreadsWidget;

  });
