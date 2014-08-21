/**
 * Widget to display list of result hits - it allows to paginate through them
 * and display details
 *
 */

define([
    'underscore',
    'js/widgets/list_of_things/widget',
    'js/components/api_query'
  ],

  function (  _,
    ListOfThingsWidget, ApiQuery) {

    var TableOfContentsWidget = ListOfThingsWidget.extend({

      //don't listen to inviting_request
      activate: function (beehive) {
      this.pubsub = beehive.Services.get('PubSub');
      this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
    },

      //    a way to get data on command on a per-bibcode-basis
     loadBibcodeData: function (bibcode) {

       this.deferredObject =  $.Deferred();

       //check if its an arxivID, if so just return
       if (bibcode.match(/arXiv/)){
         this.deferredObject.resolve(0);
         return this.deferredObject;
       }

      if (bibcode === this._bibcode){

        this.deferredObject.resolve(this.collection.numFound);
        return this.deferredObject;
      }

      this._bibcode = bibcode;

      if (bibcode[13] == 'E'){
        //take first fourteen
        bibquery = _.first(bibcode, 14).join("")+"*";

      }
      else {
        //take first thirteen
        bibquery = _.first(bibcode, 13).join("")+"*";

      }

      var searchTerm = 'bibcode:' +  bibquery

      this.dispatchRequest(new ApiQuery({'q': searchTerm}));

      return this.deferredObject.promise()
    }


    });

    return TableOfContentsWidget;

  });
