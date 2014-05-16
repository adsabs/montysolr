/**
 * Created by alex on 5/10/14.
 */
define([], function () {

  var Paginator = function (options) {

    this.start = options.start || 0;
    this.rows = options.rows || 20;
    this.initialStart = options.start || 0;
    this.startName = options.startName || "start";
    this.rowsName = options.rowsName || "rows";
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
      var currentPagination = {};
      currentPagination[this.startName] = "" + this.start;
      currentPagination[this.rowsName] = "" + this.rows;
      this.start += this.rows;

      _.each(currentPagination, function (v, k) {
        apiQuery.set(k, v);
      });

      return apiQuery;
    },

    reset: function () {
      this.start = this.initialStart;
    },

    isInitial: function () {
      return this.start === (this.initialStart + this.rows);
    }

  });

  return Paginator;

});