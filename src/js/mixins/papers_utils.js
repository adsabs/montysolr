define([
    'underscore'
  ],
  function (
  _
  ) {


  var Utils = {
    formatDate : function(dateString){
      var monthAbb = [ undefined, "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sept", "Oct", "Nov", "Dec" ];
      var year = parseInt(dateString.slice(0,4));
      var month = parseInt(dateString.slice(5,7));
      month = month ? (monthAbb[month] + " ") : "";
      return month + year;
    },

    shortenAbstract : function(abs, maxLen){
      maxLen = maxLen || 500;
      if (abs.length >= maxLen) return abs;
      var i = abs.slice(0, maxLen).lastIndexOf(" ");
      return abs.slice(0, i + 1) + "...";
    },

    /**
     * This method prepares data for consumption by the template on a per-doc basis
     *
     * @returns {*}
     */
    prepareDocForViewing: function (data) {

      var shownAuthors;
      var maxAuthorNames = 3;

      if (data.author && data.author.length > maxAuthorNames) {
        data.extraAuthors = data.author.length - maxAuthorNames;
        shownAuthors = data.author.slice(0, maxAuthorNames);
      } else if (data.author) {
        shownAuthors = data.author
      }

      if (data.author) {
        var l = shownAuthors.length - 1;
        data.authorFormatted = _.map(shownAuthors, function (d, i) {
          if (i == l || l == 0) {
            return d; //last one, or only one
          } else {
            return d + ";";
          }
        })
      }
      //if details/highlights
      data.details = data.details? data.details.highlights : undefined;
      data.pubdate = data.pubdate ? this.formatDate(data.pubdate) : undefined;
      data.shortAbstract = data.abstract? this.shortenAbstract(data.abstract) : undefined;

      data.num_citations = data["[citations]"] ? data["[citations]"]["num_citations"] : undefined;

      if (data.pubdate || data.shortAbstract){
        data.popover = true;
      }

      if (this.model)
        data.orderNum = this.model.get("resultsIndex") + 1;

      return data;
    }
  };

  return Utils;

});
