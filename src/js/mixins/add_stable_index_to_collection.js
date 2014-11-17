/**
 * This mixin (only has a single function) will reset the collection if numFound doesn't exist,
 * or else add  to the collection. Make sure the model for the collection has idAttribute : "resultsIndex"
 * to allow for the collection to add records properly.
 * 
 *  
 */
define(['underscore'], function (_) {

  var WidgetPaginator = {

    //returns correctly zero-indexed start val
    getStartVal: function (page, rows) {
      return (rows * page) - rows;
    },

    //returns final index needed for constructing a page (inclusive)
    getEndVal : function(page,rows, numFound){
      var endVal =  this.getStartVal(page, rows) + rows - 1;
      var finalIndex = numFound - 1;
      return (finalIndex <= endVal) ? finalIndex : endVal;
    },


    //returns current page number
    getPageVal: function (start, rows) {
      if (start  % rows !== 0){
        console.error("start and rows values will not yield a full page! start=" + start + ' rows=' + rows);
      }
      //also could do (start + rows) / rows
      return parseInt(start/rows) + 1;
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
        s += 1;
      });
      return docs;
    },

    /**
     *  create list of up to X page numbers to show
     *
     * @param pageStartingPoint
     * @returns {*}
     */
    generatePageNums: function (pageStartingPoint, numPagesAround) {
      numPagesAround = numPagesAround || 3;
      pageStartingPoint = pageStartingPoint - 1; // internally we count from zero
      var pageNums = _.map(_.range(pageStartingPoint-numPagesAround, pageStartingPoint+numPagesAround+1), function (d) {
        var current = (d === pageStartingPoint) ? true : false;
        return {p: pageStartingPoint + d + 1, current: current};
      });
      //page number can't be less than 1
      pageNums = _.filter(pageNums, function (d) {
        if (d.p > 0) {
          return true
        }
      });
      return pageNums;
    },

    //iterate through pageNums, keep them only if they're possible (< numFound)
    ensurePagePossible: function (pageNums, perPage, numFound) {
      var endIndex = numFound - 1;
      return  _.filter(pageNums, function (n) {
        if (this.getStartVal(n.p, perPage) <= endIndex) {
          return true;
        }
      }, this)
    }

  };

  return WidgetPaginator

});