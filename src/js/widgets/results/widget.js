/**
 * Widget to display list of result hits - it allows to paginate through them
 * and display details
 * 
 */

define([
    'underscore',
    'hbs!./templates/results-container-template',
    'hbs!./templates/item-template',
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
      template: ItemTemplate
    });

    var CollectionViewClass = ListOfThingsWidget.prototype.CollectionViewClass.extend({
      id: "search-results",
      itemView: ItemViewClass,
      template: ContainerTemplate
    });


    var ResultsWidget = ListOfThingsWidget.extend({

      ItemModelClass: ItemModelClass,
      ItemViewClass: ItemViewClass,
      CollectionClass: CollectionClass,
      CollectionViewClass: CollectionViewClass,

      defaultQueryArguments: {
        hl: "true",
        "hl.fl": "title,abstract",
        fl: 'title,abstract,bibcode,author,keyword,id,citation_count,pub,aff,email,volume,year'
      },

      parseResponse: function (apiResponse, orderNum) {
        var raw = apiResponse.toJSON();
        var highlights = raw.highlighting;
        orderNum = orderNum || 1;

        if (!this.defaultQueryArguments.fl) {
          return _.map(raw.response.docs, function(d) {orderNum+=1; d['orderNum'] = orderNum; d['identifier'] = d.bibcode; return d});
        }

        var keys = _.map(this.defaultQueryArguments.fl.split(','), function(v) {return v.trim()});

        var docs = _.map(raw.response.docs, function (doc) {
          var d = _.pick(doc, keys);
          d['identifier'] = d.bibcode;
          var id = d.id;
          var h = {};

          if (highlights) {
            h = (function () {

              var hl = highlights[id];
              var finalList = [];
              //adding abstract,title, etc highlights to one big list
              _.each(hl, function (val, key) {
                finalList.push(val);
              });
              finalList = _.flatten(finalList);

              return {
                "highlights": finalList
              }
            }());
          };

          if (h.highlights && h.highlights.length > 0)
            d['details'] = h;
          d['orderNum'] = orderNum;

          orderNum += 1;
          return d;

        });

        return docs;
      }
    });

    return ResultsWidget;

  });
