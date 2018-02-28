'use strict';
define([
  'underscore',
  'jsonpath',
  'js/modules/orcid/work'
], function (_, jp, Work) {

  var PATHS = {
    firstName:     '$.person.name["given-names"].value',
    lastName:      '$.person.name["family-name"].value',
    orcid:         '$["orcid-identifier"].path',
    workSummaries: '$["activities-summary"].works.group'
  };

  /**
   *
   * @module Profile
   * @param profile
   * @constructor
   */
  var Profile = function Profile(profile) {
    this._root = profile;

    /**
     * search profile for value at specified path
     *
     * @param {String} path - path to search
     * @returns {*} value found at path
     */
    this.get = function (path) {
      var val = jp.query(this._root, path);
      if (_.isEmpty(val)) {
        return null;
      } else if (_.isArray(val) && val.length <= 1) {
        return val[0];
      }
      return val;
    };

    /**
     * Gets all the work summaries from the profile
     *
     * @returns {Work[]} - the array of Work summaries
     */
    this.getWorks = function () {
      return _.map(this.getWorkSummaries(), function (w) {
        return new Work(w);
      });
    };

    /**
     * Convenience method for generating an ADS response object
     * this can then be used to update the pagination of lists of orcid works
     *
     * @returns {{
     *  responseHeader: {
     *    params: {
     *      orcid: String,
     *      firstName: String,
     *      lastName: String
     *    }
     *  },
     *  response: {
     *    numFound: Number,
     *    start: Number,
     *    docs: (Object[])
     *  }
     * }}
     */
    this.toADSFormat = function () {
      var docs = _.sortBy(this.getWorks(), function (w) {
        return w.getTitle();
      });

      docs = _.map(docs, function (d) {
        return d.toADSFormat();
      });

      return {
        responseHeader: {
          params: {
            orcid: this.getOrcid(),
            firstName: this.getFirstName(),
            lastName: this.getLastName()
          }
        },
        response: {
          numFound: docs.length,
          start: 0,
          docs: docs
        }
      };
    };

    // generate getters for each path on PATHS
    _.reduce(PATHS, function (obj, p, k) {
      if (_.isString(k) && k.slice) {
        var prop = k[0].toUpperCase() + k.slice(1);
        obj['get' + prop] = _.partial(obj.get, p);
      }
      return obj;
    }, this);
  };

  return Profile;
});
