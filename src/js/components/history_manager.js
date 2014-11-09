define(["underscore"], function (_) {

  var HistoryManager = function () {
    this.history = []
  }

  _.extend(HistoryManager.prototype, {


    /*
     * This will return the prior PAGE--so the history entry before the current
     * one. If you are on the abstract page and were on the results page before that,
     * that is what will be returned. Even if you have been on several routes within
     * the abstract page, these will be ignored and you will be given the
     * actual page name.
     *
     * */
    getPriorPage       : function () {

      if (this.history.length <=1){
        return undefined
      }
      var currentPage, p, priorPage;

      currentPage = _.last(this.history).page;
      //first, simplify the dict to include only pages, not subpages
      p = 1;
      while (p < this.history.length){

        priorPage = this.history[(this.history.length -1) -p].page

        if (priorPage !== currentPage){
          return priorPage
        }
        p+=1

      }
      //nothing was found that differs from currentPage
      return undefined
    },
    getPriorPageVal    : function () {

      if (this.history.length <=1){
        return undefined
      }

      var currentPage, p, prior, priorPage;

      currentPage = _.last(this.history).page;
      //first, simplify the dict to include only pages, not subpages
      p = 1;

      while (p < this.history.length){
        prior= this.history[(this.history.length -1) -p]
        priorPage = prior.page

        if (priorPage !== currentPage){
          return prior.data
        }

        p+=1
      }
      return undefined
    },

    /*
     * Unlike get prior page, this will give you whatever the prior
     * route was, even if it was within the current page.
     * */

    getPriorRoute : function(){

      var subPage = this.history[this.history.length -1].subPage;
      var page = this.history[this.history.length -1].page;

      return {page: page, subPage : subPage}
    },

    getPriorRouteVal : function(){

      return this.history[this.history.length -1].data

    },

    addEntry           : function (item) {
      this.history.push(item)
    }
  })

  return HistoryManager

})
