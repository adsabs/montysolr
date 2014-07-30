/**
 * Widget to display list of result hits - it allows to paginate through them
 * and display details
 *
 */

define([
    'underscore',
    'hbs!js/widgets/list_of_things/templates/results-container-template',
    'hbs!js/widgets/list_of_things/templates/item-template',
    'js/widgets/list_of_things/widget', 'js/components/api_query'
  ],

  function (
    _,
    ContainerTemplate,
    ItemTemplate,
    ListOfThingsWidget, ApiQuery) {

    var ItemModelClass = ListOfThingsWidget.prototype.ItemModelClass.extend({
      parse: function(doc) {
        doc['identifier'] = doc.bibcode;
        return doc;
      }
    });

    var CollectionClass = ListOfThingsWidget.prototype.CollectionClass.extend({
      model: ItemModelClass
    });

    var ItemViewClass = ListOfThingsWidget.prototype.ItemViewClass.extend({
      template: ItemTemplate,
      serializeData: function () {
        var data = this.model.toJSON();
        var shownAuthors;

        if (data.author && data.author.length > 3) {
          data.extraAuthors = data.author.length - 3;
          shownAuthors = data.author.splice(0, 3);
        } else if (data.author) {
          shownAuthors = data.author
        }

        if (data.author) {
          var l = shownAuthors.length-1;
          data.authorFormatted = _.map(shownAuthors, function (d, i) {
            if (i == l || l == 1) {
              return d; //last one, or only one
            } else {
              return d + ";";
            }
          })
        }
        return data
      }
    });

    var CollectionViewClass = ListOfThingsWidget.prototype.CollectionViewClass.extend({
      id: "search-results",
      itemView: ItemViewClass,
      template: ContainerTemplate
    });


    var TableOfContentsWidget = ListOfThingsWidget.extend({

      activate: function (beehive) {
      this.pubsub = beehive.Services.get('PubSub');

      //custom handleResponse function goes here
      this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
    },

      //    a way to get data on command on a per-bibcode-basis
     loadBibcodeData: function (bibcode) {

       this.deferredObject =  $.Deferred();

      if (bibcode === this._bibcode){

        this.deferredObject.resolve(this.collection)
        
        return this.deferredObject
      }

      this._bibcode = bibcode;

      if (bibcode[13] == 'E'){
        //take first fourteen
        bibquery = _.first(bibcode, 14).join("");

      }
      else {
        //take first thirteen
        bibquery = _.first(bibcode, 13).join("");

      }

      var searchTerm = 'bibcode:' +  bibquery

      this.dispatchRequest(new ApiQuery({'q': searchTerm}));

      return this.deferredObject.promise()
    },

      ItemModelClass     : ItemModelClass,
      ItemViewClass      : ItemViewClass,
      CollectionClass    : CollectionClass,
      CollectionViewClass: CollectionViewClass

    });

    return TableOfContentsWidget;

  });
