/**
 * Created by alex on 5/10/14.
 */
define([], function () {

  var Paginator = function (options) {
    this.pagination = {start: options.start || 0,
      rows                  : options.rows || 20,
      initialStart          : options.start || 0 };

    this.startName = options.startName || "start";
    this.rowsName = options.rowsName || "rows";
  };

  _.extend(Paginator.prototype, {
    run: function () {
      var currentPagination = {};
      currentPagination[this.startName] = "" + this.pagination.start;
      currentPagination[this.rowsName] = "" + this.pagination.rows;

      this.pagination.start += this.pagination.rows;
      return currentPagination;
    },

    reset: function () {
      this.pagination.start = this.pagination.initialStart
    },

    isInitial: function () {
      return this.pagination.start === (this.pagination.initialStart + this.pagination.rows)
    }

  })

  return Paginator;

});