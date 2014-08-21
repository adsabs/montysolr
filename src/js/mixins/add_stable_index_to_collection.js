/**
 * This mixin (only has a single function) will reset the collection if numFound doesn't exist,
 * or else add  to the collection. Make sure the model for the collection has idAttribute : "resultsIndex"
 * to allow for the collection to add records properly.
 * 
 *  
 */
define(['underscore'], function (_) {

  var WidgetPaginator = {

    //handing docs and apiResponse seperately because there may have already
    //been some pre-processing on docs
     insertPaginatedDocsIntoCollection : function(docs, apiResponse) {

      var start = apiResponse.get("response.start");

      var docs = _.map(docs, function(d){
        d.resultsIndex = start
        start++;
        return d
      });

      if (!this.numFound) {

        this.numFound = apiResponse.get("response.numFound");

        //might want other things to listen to this event
        this.trigger("change:numFound", this.numFound);

        this.collection.reset(docs, {
          parse: true
        });
      }
      else {
        //backbone ignores duplicate records because it has an idAttribute of "resultsIndex"
        this.collection.add(docs, {
          parse: true
        });

      }
     }
  }

  return WidgetPaginator

});