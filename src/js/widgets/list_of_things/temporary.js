/**
 * Widget to display list of result hits - it allows to paginate through them
 * and display details. A temporary solution until I refactore the list-of-things
 *
 */

define([
    'underscore',
    'js/widgets/list_of_things/widget'
  ],

  function (  _,  ListOfThingsWidget) {

    var ListOfThingsForAbstractView = ListOfThingsWidget.extend({
      activate: function (beehive) {
        this.pubsub = beehive.Services.get('PubSub');

        _.bindAll(this, ['onNewQuery', 'dispatchRequest', 'processResponse', 'onDisplayDocuments']);
        this.pubsub.subscribe(this.pubsub.START_SEARCH, this.onNewQuery);
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
        this.pubsub.subscribe(this.pubsub.DISPLAY_DOCUMENTS, this.onDisplayDocuments);
      },
      onNewQuery: function() {
        this.trigger('page-manager-event', 'widget-ready', {'isActive': false});
      },
      onDisplayDocuments: function(apiQuery) {
        var bibcode = apiQuery.get('q');
        var self = this;
        if (bibcode.length > 0 && bibcode[0].indexOf('bibcode:') > -1) {
          bibcode = bibcode[0].replace('bibcode:', '');
          this.loadBibcodeData(bibcode).done(function(data) {
            self.trigger('page-manager-event', 'widget-ready', {'isActive': true, numFound: data});
          });
        }
      }
    });

    return ListOfThingsForAbstractView;

  });
