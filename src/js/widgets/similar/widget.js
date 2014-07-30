/**
 * Widget to display list of result hits - it allows to paginate through them
 * and display details
 *
 */

define([
    'underscore',
    'hbs!js/widgets/list_of_things/templates/results-container-template',
    'hbs!js/widgets/list_of_things/templates/item-template',
    'js/widgets/list_of_things/widget',
    'js/components/api_query'
  ],

  function (
    _,
    ContainerTemplate,
    ItemTemplate,
    ListOfThingsWidget,
    ApiQuery) {

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


    var SimilarWidget = ListOfThingsWidget.extend({

      activate: function (beehive) {
        this.pubsub = beehive.Services.get('PubSub');

        //custom handleResponse function goes here
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
      },

      loadBibcodeData: function (bibcode) {

        if (bibcode === this._bibcode) {

          this.deferredObject =  $.Deferred();

          this.deferredObject.resolve(this.collection);

          return this.deferredObject.promise();
        }

        this._bibcode = bibcode;

        this.deferredObject = $.Deferred();

        this.dispatchRequest(new ApiQuery({
          'q'     : "bibcode:"+this._bibcode,
          mlt     : "true",
          "mlt.fl": "title,body",
          fl      : 'title,bibcode,author,id,citation_count,pub,aff,volume,year'
        }));


        return this.deferredObject.promise();
      },

      ItemModelClass     : ItemModelClass,
      ItemViewClass      : ItemViewClass,
      CollectionClass    : CollectionClass,
      CollectionViewClass: CollectionViewClass,

      parseResponse: function (apiResponse, orderNum) {
        var raw = apiResponse.toJSON();
        orderNum = orderNum || 1;

        //adding order numbers

        var docs =  _.values(raw.moreLikeThis)[0].docs

        var docs = _.map(docs, function(doc){
          doc.identifier = doc.bibcode;
          doc.orderNum = orderNum;
          orderNum += 1
          return doc

        })

        return docs

      },

      //also overriding process response because numFound is in a different place

      processResponse: function (apiResponse) {

        //resetting this flag
        this.showMoreAfterRender = false;

        //this is for layout managers
        //need to override for the "similar" widget
        var numFound = this.collection.numFound = _.values(apiResponse.get('moreLikeThis'))[0].numFound;

        this.setCurrentQuery(apiResponse.getApiQuery());

        if (this.paginator.getCycle() <= 1) {
          //it's the first set of results
          this.collection.reset(this.parseResponse(apiResponse, 1), {
            parse: true
          });
          this.paginator.setMaxNum(numFound);
          if (this.paginator.maxNum > this.displayNum && this.viewRendered) {
            this.view.enableShowMore();
          }
          else if (this.paginator.maxNum > this.displayNum && !this.viewRendered){
            this.showMoreAfterRender = true;
          }
          else {
            this.view.disableShowMore();
          }
        } else {
          //it's in response to "load more"
          this.collection.add(this.parseResponse(apiResponse, this.collection.models.length+1), {
            parse: true
          })
        }

        if (this.deferredObject){
          this.deferredObject.resolve(this.collection)
        }

      }
    })

    return SimilarWidget;

  });
