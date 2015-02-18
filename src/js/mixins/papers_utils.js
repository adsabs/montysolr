define([
    'underscore',
    'jquery-ui',
    'jquery'
  ],
  function (
  _,
  $ui,
  $
  ) {


  var Utils = {
    /**
     * Receives the  ISO8601 date string (actually, browsers will be able to parse
     * range of date strings, but you should be careful and not count on that!)
     *
     * And returns a string in requested format; when the minute is set to 0,
     * we will that the month was not given (ADS didn't know about it)
     *
     * @param dateString
     *    string in ISO8601 format
     * @param format
     *    string, jquery-ui datepicker for options
     * @param foolsFormat
     *    array, jquery-ui datepicker format to use when we detect that
     *    hour == minute == second == 0 (this is ADS convention to mark
     *    unknown publication dates) or when a day or month are missing
     * @returns {*}
     */
    formatDate : function(dateString, format){

      if (format && !_.isObject(format)) {
        throw new Error('format must be an object of string formats');
      }
      format = _.defaults((format || {}), {format: 'yy/mm/dd',
        missing: {day: 'yy/mm', month: 'yy'},
        separator: '-',
        junk: '-00'
      });

      var fooIndex = ['day', 'month'];

      var localDatePretendingToBeUtc, utc, formatToUse;
      formatToUse = format.format;

      utc = new Date(dateString);

      if (_.isNaN(utc.getYear())) {
        // we have to modify dateString, removing pattern until it parses
        var i = 0;
        var dateCopy = dateString;
        var p = format.junk;
        while (dateCopy.indexOf(p) > -1) {
          dateCopy = dateCopy.substring(0, dateCopy.lastIndexOf(p));
          try {
            utc = new Date(dateCopy);
            if (!_.isNaN(utc.getYear())) {
              formatToUse = format.missing[fooIndex[i]];
              if (!formatToUse) {
                throw new Error('format is missing: ' + fooIndex[i]);
              }
            }
          }
          catch (e) {
            // pass
          }
          i += 1;
        }
        if (!utc.getYear())
          throw new Error('Error parsing input: ' + dateString);
      }
      else {
        // it parsed well, but the string was too short
        var s = format.separator;
        if (dateString.indexOf(s) > -1) {
          var parts = dateString.split(s);
          if (parts.length == 2) {
            formatToUse = format.missing[fooIndex[0]];
          }
          else if (parts.length == 1) {
            formatToUse = format.missing[fooIndex[1]];
          }
          if (!formatToUse) {
            throw new Error('format is missing: missing.' + fooIndex[i]);
          }
        }
      }

      // the 'utc' contains UTC time, but it is displayed by browser in local time zone
      // so we'll create another time, which will pretend to be UTC (but in reality it
      // is just UTC+local offset); but it will display things as UTC; confused? ;-)
      localDatePretendingToBeUtc = new Date(utc.getTime() + utc.getTimezoneOffset() * 60000);

      return $.datepicker.formatDate(formatToUse, localDatePretendingToBeUtc);
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
      data.formattedDate = data.formattedDate || (data.pubdate ? this.formatDate(data.pubdate) : undefined);
      data.shortAbstract = data.abstract? this.shortenAbstract(data.abstract) : undefined;
      data.details = data.details || {shortAbstract: data.shortAbstract, pub: data.pub};
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
