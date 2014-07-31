/**
 * Widget to display list of result hits - it allows to paginate through them
 * and display details
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
//        doc['highlights'] = doc.details.highlights;
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

      ItemModelClass     : ItemModelClass,
      ItemViewClass      : ItemViewClass,
      CollectionClass    : CollectionClass,
      CollectionViewClass: CollectionViewClass,

      defaultQueryArguments: {
        hl     : "true",
        "hl.fl": "title,abstract,body",
        fl     : 'title,abstract,bibcode,author,keyword,id,citation_count,pub,aff,email,volume,year'
      },

      parseResponse: function (apiResponse, orderNum) {
        var raw = apiResponse.toJSON();
        var highlights = raw.highlighting;
        orderNum = orderNum || 1;

        if (!this.defaultQueryArguments.fl) {
          return _.map(raw.response.docs, function (d) {
            orderNum += 1;
            d['orderNum'] = orderNum;
            d['identifier'] = d.bibcode;
            return d
          });
        }

        var keys = _.map(this.defaultQueryArguments.fl.split(','), function (v) {
          return v.trim()
        });

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
              _.each(_.pairs(hl), function (pair) {
                finalList = finalList.concat(pair[1]);
              });
              finalList = finalList;

              return {
                "highlights": finalList
              }
            }());
          }
          ;

          if (h.highlights && h.highlights.length > 0)
            d['details'] = h;

          d['orderNum'] = orderNum;

          orderNum += 1;
          return d;

        });
        return docs;
      },

      onAllInternalEvents: function(ev, arg1, arg2) {

        if (ev == 'composite:rendered') {
          this.view.disableShowMore();
          this.view.toggleDetailsControls(false);
          if (this.showMoreAfterRender){
            this.view.enableShowMore()
          }
          this.viewRendered = true;

        }
        else if (ev == 'reset') {
          var details = _.filter(this.view.collection.models, function(m) {return m.has('details')});
          if (details.length > 0) {
            this.view.toggleDetailsControls(true);
          }
          else {
            this.view.toggleDetailsControls(false);
          }

        }
        else if (ev == "fetchMore") {

          var p = this.handlePagination(this.displayNum, this.maxDisplayNum, arg1, this.paginator, this.view, this.collection);
          if (p && p.before) {
            p.before();
          }
          if (p && p.runQuery) {
            // ask for more data
            this.resetPagination = false;
            this.dispatchRequest(this.getCurrentQuery());
          }

          //letting other interested widgets know that more info was fetched
          this.pubsub.publish(this.pubsub.CUSTOM_EVENT, {event: "pagination", data: this.paginator});

        }
      }

    });

    return ResultsWidget;

  });
