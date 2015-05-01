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
     * @param position
     * @param perPage
     * @returns {number}
     */
    getPageVal: function (position, perPage) {
      return Math.max(0, Math.ceil(position/perPage)-1);
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
    },

    /**
     *  create list of up to X page numbers to show;
     *  it is zero-based count and respects numFound
     *  boundary
     *
     * @param pageStartingPoint
     * @returns {*}
     */
    generatePageNums: function (pageStartingPoint, numPagesAround, perPage, numFound) {
      numPagesAround = numPagesAround || 2;
      var leftSide = Math.max(pageStartingPoint-numPagesAround, 0);
      var rightSide = pageStartingPoint + ((numPagesAround*2) - Math.abs(leftSide-pageStartingPoint));

      var pageNums = _.map(_.range(leftSide, rightSide+1), function (d) {
        return {p: d, current: (d === pageStartingPoint) ? true : false};
      });
      //page number can't be less than 0
      pageNums = _.filter(pageNums, function (d) {
        if (d.p < 0) return false;
        if (this.getPageStart(d.p, perPage) >= numFound) return false;
        return true;
      }, this);
      return pageNums;
    }
  };

  return WidgetPaginator;

});