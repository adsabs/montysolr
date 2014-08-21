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

      //don't listen to inviting_request
      activate: function (beehive) {
        this.pubsub = beehive.Services.get('PubSub');
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
      },

      solrField : "reference"

    });

    return ReferencesWidget;

  });
