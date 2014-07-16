/**
 * Widget to display list of citations
 *
 */

define([
    'underscore',
    'hbs!js/widgets/list_of_things/templates/results-container-template',
    'hbs!js/widgets/list_of_things/templates/item-template',
    'js/widgets/list_of_things/widget'
  ],

  function (
    _,
    ContainerTemplate,
    ItemTemplate,
    ListOfThingsWidget) {

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


    var CitationsWidget = ListOfThingsWidget.extend({

      activate: function (beehive) {
      this.pubsub = beehive.Services.get('PubSub');

      //custom handleResponse function goes here
      this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
    },

      solrField : "citation",

      ItemModelClass     : ItemModelClass,
      ItemViewClass      : ItemViewClass,
      CollectionClass    : CollectionClass,
      CollectionViewClass: CollectionViewClass
    });

    return CitationsWidget;

  });
