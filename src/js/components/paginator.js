/**
 * Created by alex on 5/10/14.
 * Updated by roman on 5/17/14
 *
 * The Paginator object updates the ApiQuery by setting the new pagination
 * parameters (or replacing the existing ones). It is typically used by
 * a widget before a new query is sent to the Forbidden City
 *
 * The paginator should be 'reset' if it is dealing with a totally new
 * query.
 *
 * Paginator doesn't know how many total results there are until
 * you call 'setMaxNum' -- this typically happens after the first
 * batch of results arrives from server (the widget must call 'setMaxNum')
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
    },

    /**
     * Removes any notion of pagination from the ApiQuery
     * @returns {ApiQuery}
     */
    cleanQuery: function(apiQuery) {
      apiQuery.unset(this.startName);
      apiQuery.unset(this.rowsName);
      return apiQuery;
    }

  });

  return Paginator;

});