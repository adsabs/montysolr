/**
 * Widget to display list of result hits - it allows to paginate through them
 * and display details
 *
 */

define([
    'underscore',
    'js/widgets/list_of_things/widget',
    'js/components/api_query',
    'js/mixins/add_stable_index_to_collection'
  ],

  function (
    _,
    ListOfThingsWidget,
    ApiQuery,
    PaginationMixin
    ) {


    var SimilarWidget = ListOfThingsWidget.extend({

      defaultQueryArguments: _.extend({
        "mlt"     : "true",
        "mlt.fl"  : "title,abstract",
        "mlt.count": "20"
      }, ListOfThingsWidget.prototype.defaultQueryArguments),

      initialize: function (options) {
        ListOfThingsWidget.prototype.initialize.apply(this, arguments);
        this.defaultQueryArguments["mlt.count"] = this.model.get("perPage");
      },


      //mlt info is in a different place than standard response info
      extractDocs: function (apiResponse) {
        var mlt = _.values(apiResponse.get('moreLikeThis')[0]);
        var docs =  mlt.docs;
        var docs = _.map(docs, function(doc, index){
          doc.identifier = doc.bibcode;
          return doc;
        });
        return docs;
      },

      getPaginationInfo: function(apiResponse, docs) {
        var q = apiResponse.getApiQuery();
        var toSet = {
          "numFound":  apiResponse.get("moreLikeThis[0].numFound"),
          "currentQuery":q
        };

        //checking to see if we need to reset start or rows values
        var perPage =  q.get("mlt.count") || this.model.get('perPage');
        var start = q.get("mlt.start") || this.model.get('start');

        if (perPage){
          perPage = _.isArray(perPage) ? perPage[0] : perPage;
          toSet.perPage = perPage;
        }

        if (_.isNumber(start) || _.isArray(start)){
          start = _.isArray(start) ? start[0] : start;
          toSet.page = PaginationMixin.getPageVal(start, perPage);
        }
        else {
          toSet.page = 0;
        }
        toSet.start = start;

        var numVisible = this.hiddenCollection.getNumVisible();
        var showMore = 0;
        if (numVisible == 0) {
          showMore = perPage;
        } else if(numVisible % perPage !== 0) {
          showMore = numVisible % perPage;
        }
        toSet.showMore = showMore;

        // create pagination navigation links
        var pageNums = PaginationMixin.generatePageNums(toSet.page, 3);
        pageNums = PaginationMixin.ensurePagePossible(pageNums, toSet.perPage, toSet.numFound);

        var pageData = {};
        //only render pagination controls if there are more pages
        if (pageNums.length > 1) {
          //now, finally, generate links for each page number
          var pageData = _.map(pageNums, function (n) {
            var baseQ = q.clone();
            var s = PaginationMixin.getStartVal(n.p, perPage);
            baseQ.set("start", s);
            baseQ.set("rows", perPage);
            n.start = s;
            n.perPage = perPage;
            n.link = baseQ.url();
            return n;
          }, this);
        }
        toSet.pageData = pageData;

        //should we show a "back to first page" button?
        toSet.showFirst = (_.pluck(pageNums, "p").indexOf(1) !== -1) ? false : true;

        return toSet;
      }

    });

    return SimilarWidget;

  });
