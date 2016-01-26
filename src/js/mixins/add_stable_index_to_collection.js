/**
 * This mixin (only has a single function) will reset the collection if numFound doesn't exist,
 * or else add  to the collection. Make sure the model for the collection has idAttribute : "resultsIndex"
 * to allow for the collection to add records properly.
 * 
 *  
 */
define(['underscore'], function (_) {

  var WidgetPaginator = {

    /**
     * returns zero-indexed start val (we expect the page
     * to be zero-indexed!)
     */

    getPageStart: function (page, perPage, numFound) {
      return numFound ? Math.min(page*perPage, numFound) : page*perPage;
    },

    /**
     * returns final row value for constructing a page (inclusive)
     */
    getPageEnd : function(page, perPage, numFound){
      var endVal =  this.getPageStart(page, perPage) + perPage;
      return (endVal > numFound) ? numFound : endVal;
    },


    /**
     * Returns the page number (on which the position falls)
     * It is zero-based count
     *
     * @param start (zero indexed start value of record, returned by solr)
     * @param perPage
     * @returns {number}
     */
    getPageVal: function (start, perPage) {
      return Math.floor(start/perPage);
    },


    /**
     * Add 'resultsIndex' attribute into the model.
     *
     * @param docs
     * @param start
     * @returns {*}
     */
    addPaginationToDocs: function (docs, start) {
      var s = _.isArray(start) ? start[0] : parseInt(start);
      _.each(docs, function (d) {
        d.resultsIndex = s;
        //non zero-indexed
        d.indexToShow = s + 1;
        s += 1;
      });
      return docs;
    }

  };

  return WidgetPaginator;

});