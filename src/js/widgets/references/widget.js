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

    var ReferencesWidget = ListOfThingsWidget.extend({
      solrOperator: "references",
      sortOrder  : "first_author asc"
    });

    return ReferencesWidget;

  });
