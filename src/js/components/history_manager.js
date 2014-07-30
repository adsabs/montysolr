define(["underscore"], function (_) {

  var HistoryManager = function () {
    this.history = []
  }

  _.extend(HistoryManager.prototype, {

    getPriorPage       : function () {
      return _.keys(_.last(this.history, 1)[0])[0]
    },
    getPriorPageVal    : function () {
      return _.values(_.last(this.history, 1)[0])[0]
    },

    addEntry           : function (item) {
      this.history.push(item)
    }
  })

  return HistoryManager

})
