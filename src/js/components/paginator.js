/**
 * Created by alex on 5/10/14.
 */
define(['underscore'], function (_) {

  var Paginator = function (options) {

    this.start = options.start || 0; // the beginning offset
    this.rows = options.rows || 20;  // how many to fetch in one go
    this.initialStart = options.start || 0; // useful for reset
    this.startName = options.startName || "start"; // name of the parameter for offset
    this.rowsName = options.rowsName || "rows"; // name of the parameter for num of items to fetch
    this.cycle = 0; // counter of how many times we were called
    this.maxNum = -1; // set from outside to limit how many items there are to fetch (for this query)
  };

  _.extend(Paginator.prototype, {

    /**
     * Changes ApiQuery setting the correct parameters for the next
     * pagination
     *
     * @param apiQuery
     * @returns {*}
     */
    run: function (apiQuery) {
      if (!this.hasMore()) {
        return apiQuery;
      }

      apiQuery.set(this.startName, this.start);
      apiQuery.set(this.rowsName, this.rows);

      // increment the actual value
      this.start += this.rows;

      if (this.maxNum > 0 && this.maxNum < this.start)
        this.start = this.maxNum;

      this.cycle += 1;
      return apiQuery;
    },


    reset: function (initialStart) {
      this.start = initialStart || this.initialStart;
      this.maxNum = -1;
      this.cycle = 0;
    },

    getCycle: function() {
      return this.cycle;
    },

    setMaxNum: function(maxNum) {
      this.maxNum = maxNum;
    },

    hasMore: function() {
      if (this.maxNum == -1 || this.maxNum > this.start) {
        return true;
      }
      return false;
    }

  });

  return Paginator;

});